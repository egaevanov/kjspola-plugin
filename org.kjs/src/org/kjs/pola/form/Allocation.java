package org.kjs.pola.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.MRole;
import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;

public class Allocation
{
    public DecimalFormat format;
    public static CLogger log;
    private boolean m_calculating;
    public int m_C_Currency_ID;
    public int m_C_Charge_ID;
    public int m_C_DocType_ID;
    public int m_C_BPartner_ID;
    private int m_noInvoices;
    private int m_noPayments;
    public BigDecimal totalInv;
    public BigDecimal totalPay;
    public BigDecimal totalDiff;
    public Timestamp allocDate;
    private int i_payment;
    private int i_open;
    private int i_discount;
    private int i_writeOff;
    private int i_applied;
    private int i_overUnder;
    public int m_AD_Org_ID;
    private ArrayList<Integer> m_bpartnerCheck;
    
    static {
        Allocation.log = CLogger.getCLogger((Class)Allocation.class);
    }
    
    public Allocation() {
        this.format = DisplayType.getNumberFormat(12);
        this.m_calculating = false;
        this.m_C_Currency_ID = 0;
        this.m_C_Charge_ID = 0;
        this.m_C_DocType_ID = 0;
        this.m_C_BPartner_ID = 0;
        this.m_noInvoices = 0;
        this.m_noPayments = 0;
        this.totalInv = Env.ZERO;
        this.totalPay = Env.ZERO;
        this.totalDiff = Env.ZERO;
        this.allocDate = null;
        this.i_payment = 7;
        this.i_open = 6;
        this.i_discount = 7;
        this.i_writeOff = 8;
        this.i_applied = 9;
        this.i_overUnder = 10;
        this.m_AD_Org_ID = 0;
        this.m_bpartnerCheck = new ArrayList<Integer>();
    }
    
    public void dynInit() throws Exception {
        this.m_C_Currency_ID = Env.getContextAsInt(Env.getCtx(), "$C_Currency_ID");
        if (Allocation.log.isLoggable(Level.INFO)) {
            Allocation.log.info("Currency=" + this.m_C_Currency_ID);
        }
        this.m_AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());
        this.m_C_DocType_ID = MDocType.getDocType("CMA");
    }
    
    public void checkBPartner() {
        if (Allocation.log.isLoggable(Level.CONFIG)) {
            Allocation.log.config("BPartner=" + this.m_C_BPartner_ID + ", Cur=" + this.m_C_Currency_ID);
        }
        if (this.m_C_BPartner_ID == 0 || this.m_C_Currency_ID == 0) {
            return;
        }
        final Integer key = new Integer(this.m_C_BPartner_ID);
        if (!this.m_bpartnerCheck.contains(key)) {
            new Thread() {
                @Override
                public void run() {
                    MPayment.setIsAllocated(Env.getCtx(), Allocation.this.m_C_BPartner_ID, (String)null);
                    MInvoice.setIsPaid(Env.getCtx(), Allocation.this.m_C_BPartner_ID, (String)null);
                }
            }.start();
            this.m_bpartnerCheck.add(key);
        }
    }
    
    public Vector<Vector<Object>> getPaymentData(final boolean isMultiCurrency, final Object date, final IMiniTable paymentTable) {
        final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        StringBuilder sql = new StringBuilder("SELECT p.DateTrx,p.DocumentNo,p.C_Payment_ID,c.ISO_Code,p.PayAmt,currencyConvert(p.PayAmt,p.C_Currency_ID,?,?,p.C_ConversionType_ID,p.AD_Client_ID,p.AD_Org_ID),currencyConvert(paymentAvailable(C_Payment_ID),p.C_Currency_ID,?,?,p.C_ConversionType_ID,p.AD_Client_ID,p.AD_Org_ID),p.MultiplierAP FROM C_Payment_v p INNER JOIN C_Currency c ON (p.C_Currency_ID=c.C_Currency_ID) WHERE p.IsAllocated='N' AND p.Processed='Y' AND p.C_Charge_ID IS NULL AND p.C_BPartner_ID=?");
        if (!isMultiCurrency) {
            sql.append(" AND p.C_Currency_ID=?");
        }
        if (this.m_AD_Org_ID != 0) {
            sql.append(" AND p.AD_Org_ID=" + this.m_AD_Org_ID);
        }
        sql.append(" ORDER BY p.DateTrx,p.DocumentNo");
        sql = new StringBuilder(MRole.getDefault(Env.getCtx(), false).addAccessSQL(sql.toString(), "p", true, false));
        if (Allocation.log.isLoggable(Level.FINE)) {
            Allocation.log.fine("PaySQL=" + sql.toString());
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, this.m_C_Currency_ID);
            pstmt.setTimestamp(2, (Timestamp)date);
            pstmt.setInt(3, this.m_C_Currency_ID);
            pstmt.setTimestamp(4, (Timestamp)date);
            pstmt.setInt(5, this.m_C_BPartner_ID);
            if (!isMultiCurrency) {
                pstmt.setInt(6, this.m_C_Currency_ID);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final Vector<Object> line = new Vector<Object>();
                line.add(new Boolean(false));
                line.add(rs.getTimestamp(1));
                final KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(2));
                line.add(pp);
                if (isMultiCurrency) {
                    line.add(rs.getString(4));
                    line.add(rs.getBigDecimal(5));
                }
                line.add(rs.getBigDecimal(6));
                final BigDecimal available = rs.getBigDecimal(7);
                if (available != null) {
                    if (available.signum() == 0) {
                        continue;
                    }
                    line.add(available);
                    line.add(Env.ZERO);
                    data.add(line);
                }
            }
        }
        catch (SQLException e) {
            Allocation.log.log(Level.SEVERE, sql.toString(), (Throwable)e);
            return data;
        }
        finally {
            DB.close(rs, (Statement)pstmt);
        }
        DB.close(rs, (Statement)pstmt);
        return data;
    }
    
    public Vector<String> getPaymentColumnNames(final boolean isMultiCurrency) {
        final Vector<String> columnNames = new Vector<String>();
        columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
        columnNames.add(Msg.translate(Env.getCtx(), "Date"));
        columnNames.add(Util.cleanAmp(Msg.translate(Env.getCtx(), "DocumentNo")));
        if (isMultiCurrency) {
            columnNames.add(Msg.getMsg(Env.getCtx(), "TrxCurrency"));
            columnNames.add(Msg.translate(Env.getCtx(), "Amount"));
        }
        columnNames.add(Msg.getMsg(Env.getCtx(), "ConvertedAmount"));
        columnNames.add(Msg.getMsg(Env.getCtx(), "OpenAmt"));
        columnNames.add(Msg.getMsg(Env.getCtx(), "AppliedAmt"));
        return columnNames;
    }
    
    public void setPaymentColumnClass(final IMiniTable paymentTable, final boolean isMultiCurrency) {
        int i = 0;
        paymentTable.setColumnClass(i++, (Class)Boolean.class, false);
        paymentTable.setColumnClass(i++, (Class)Timestamp.class, true);
        paymentTable.setColumnClass(i++, (Class)String.class, true);
        if (isMultiCurrency) {
            paymentTable.setColumnClass(i++, (Class)String.class, true);
            paymentTable.setColumnClass(i++, (Class)BigDecimal.class, true);
        }
        paymentTable.setColumnClass(i++, (Class)BigDecimal.class, true);
        paymentTable.setColumnClass(i++, (Class)BigDecimal.class, true);
        paymentTable.setColumnClass(i++, (Class)BigDecimal.class, false);
        this.i_payment = (isMultiCurrency ? 7 : 5);
        paymentTable.autoSize();
    }
    
    public Vector<Vector<Object>> getInvoiceData(final boolean isMultiCurrency, final Object date, final IMiniTable invoiceTable) {
        final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        StringBuilder sql = new StringBuilder("SELECT i.DateInvoiced,i.DocumentNo,i.C_Invoice_ID,c.ISO_Code,i.GrandTotal*i.MultiplierAP, currencyConvert(i.GrandTotal*i.MultiplierAP,i.C_Currency_ID,?,?,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID), currencyConvert(invoiceopenkjs(C_Invoice_ID,C_InvoicePaySchedule_ID),i.C_Currency_ID,?,?,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP, currencyConvert(invoiceDiscount(i.C_Invoice_ID,?,C_InvoicePaySchedule_ID),i.C_Currency_ID,?,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.Multiplier*i.MultiplierAP,i.MultiplierAP FROM C_Invoice_v i INNER JOIN C_Currency c ON (i.C_Currency_ID=c.C_Currency_ID) WHERE invoiceopenkjs(i.C_Invoice_ID,C_InvoicePaySchedule_ID)<>0 AND i.Processed='Y' AND i.docstatus = 'CO' AND i.C_BPartner_ID=?");
        if (!isMultiCurrency) {
            sql.append(" AND i.C_Currency_ID=?");
        }
        if (this.m_AD_Org_ID != 0) {
            sql.append(" AND i.AD_Org_ID=" + this.m_AD_Org_ID);
        }
        sql.append(" ORDER BY i.DateInvoiced, i.DocumentNo");
        if (Allocation.log.isLoggable(Level.FINE)) {
            Allocation.log.fine("InvSQL=" + sql.toString());
        }
        sql = new StringBuilder(MRole.getDefault(Env.getCtx(), false).addAccessSQL(sql.toString(), "i", true, false));
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, this.m_C_Currency_ID);
            pstmt.setTimestamp(2, (Timestamp)date);
            pstmt.setInt(3, this.m_C_Currency_ID);
            pstmt.setTimestamp(4, (Timestamp)date);
            pstmt.setTimestamp(5, (Timestamp)date);
            pstmt.setInt(6, this.m_C_Currency_ID);
            pstmt.setInt(7, this.m_C_BPartner_ID);
            if (!isMultiCurrency) {
                pstmt.setInt(8, this.m_C_Currency_ID);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final Vector<Object> line = new Vector<Object>();
                line.add(new Boolean(false));
                line.add(rs.getTimestamp(1));
                final KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(2));
                line.add(pp);
                if (isMultiCurrency) {
                    line.add(rs.getString(4));
                    line.add(rs.getBigDecimal(5));
                }
                line.add(rs.getBigDecimal(6));
                BigDecimal open = rs.getBigDecimal(7);
                if (open == null) {
                    open = Env.ZERO;
                }
                line.add(open);
                BigDecimal discount = rs.getBigDecimal(8);
                if (discount == null) {
                    discount = Env.ZERO;
                }
                line.add(discount);
                line.add(Env.ZERO);
                line.add(Env.ZERO);
                line.add(open);
                if (Env.ZERO.compareTo(open) != 0) {
                    data.add(line);
                }
            }
        }
        catch (SQLException e) {
            Allocation.log.log(Level.SEVERE, sql.toString(), (Throwable)e);
            return data;
        }
        finally {
            DB.close(rs, (Statement)pstmt);
        }
        DB.close(rs, (Statement)pstmt);
        return data;
    }
    
    public Vector<String> getInvoiceColumnNames(final boolean isMultiCurrency) {
        final Vector<String> columnNames = new Vector<String>();
        columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
        columnNames.add(Msg.translate(Env.getCtx(), "Date"));
        columnNames.add(Util.cleanAmp(Msg.translate(Env.getCtx(), "DocumentNo")));
        if (isMultiCurrency) {
            columnNames.add(Msg.getMsg(Env.getCtx(), "TrxCurrency"));
            columnNames.add(Msg.translate(Env.getCtx(), "Amount"));
        }
        columnNames.add(Msg.getMsg(Env.getCtx(), "ConvertedAmount"));
        columnNames.add(Msg.getMsg(Env.getCtx(), "OpenAmt"));
        columnNames.add(Msg.getMsg(Env.getCtx(), "Discount"));
        columnNames.add(Msg.getMsg(Env.getCtx(), "WriteOff"));
        columnNames.add(Msg.getMsg(Env.getCtx(), "AppliedAmt"));
        columnNames.add(Msg.getMsg(Env.getCtx(), "OverUnderAmt"));
        return columnNames;
    }
    
    public void setInvoiceColumnClass(final IMiniTable invoiceTable, final boolean isMultiCurrency) {
        int i = 0;
        invoiceTable.setColumnClass(i++, (Class)Boolean.class, false);
        invoiceTable.setColumnClass(i++, (Class)Timestamp.class, true);
        invoiceTable.setColumnClass(i++, (Class)String.class, true);
        if (isMultiCurrency) {
            invoiceTable.setColumnClass(i++, (Class)String.class, true);
            invoiceTable.setColumnClass(i++, (Class)BigDecimal.class, true);
        }
        invoiceTable.setColumnClass(i++, (Class)BigDecimal.class, true);
        invoiceTable.setColumnClass(i++, (Class)BigDecimal.class, true);
        invoiceTable.setColumnClass(i++, (Class)BigDecimal.class, false);
        invoiceTable.setColumnClass(i++, (Class)BigDecimal.class, false);
        invoiceTable.setColumnClass(i++, (Class)BigDecimal.class, false);
        invoiceTable.setColumnClass(i++, (Class)BigDecimal.class, true);
        invoiceTable.autoSize();
    }
    
    public void calculate(final boolean isMultiCurrency) {
        this.i_open = (isMultiCurrency ? 6 : 4);
        this.i_discount = (isMultiCurrency ? 7 : 5);
        this.i_writeOff = (isMultiCurrency ? 8 : 6);
        this.i_applied = (isMultiCurrency ? 9 : 7);
        this.i_overUnder = (isMultiCurrency ? 10 : 8);
    }
    
    public String writeOff(final int row, final int col, final boolean isInvoice, final IMiniTable payment, final IMiniTable invoice, final boolean isAutoWriteOff) {
        String msg = "";
        if (this.m_calculating) {
            return msg;
        }
        this.m_calculating = true;
        if (Allocation.log.isLoggable(Level.CONFIG)) {
            Allocation.log.config("Row=" + row + ", Col=" + col + ", InvoiceTable=" + isInvoice);
        }
        if (!isInvoice) {
            final BigDecimal open = (BigDecimal)payment.getValueAt(row, this.i_open);
            BigDecimal applied = (BigDecimal)payment.getValueAt(row, this.i_payment);
            if (col == 0) {
                if ((boolean) payment.getValueAt(row, 0)) {
                    applied = open;
                    if (this.totalDiff.abs().compareTo(applied.abs()) < 0 && this.totalDiff.signum() == -applied.signum()) {
                        applied = this.totalDiff.negate();
                    }
                }
                else {
                    applied = Env.ZERO;
                }
            }
            if (col == this.i_payment) {
                if (!MSysConfig.getBooleanValue("ALLOW_APPLY_PAYMENT_TO_CREDITMEMO", false, Env.getAD_Client_ID(Env.getCtx())) && open.signum() > 0 && applied.signum() == -open.signum()) {
                    applied = applied.negate();
                }
                if (!MSysConfig.getBooleanValue("ALLOW_OVER_APPLIED_PAYMENT", false, Env.getAD_Client_ID(Env.getCtx())) && open.abs().compareTo(applied.abs()) < 0) {
                    applied = open;
                }
            }
            payment.setValueAt((Object)applied, row, this.i_payment);
        }
        else {
            final boolean selected = (boolean)invoice.getValueAt(row, 0);
            final BigDecimal open2 = (BigDecimal)invoice.getValueAt(row, this.i_open);
            BigDecimal discount = (BigDecimal)invoice.getValueAt(row, this.i_discount);
            BigDecimal applied2 = (BigDecimal)invoice.getValueAt(row, this.i_applied);
            BigDecimal writeOff = (BigDecimal)invoice.getValueAt(row, this.i_writeOff);
            BigDecimal overUnder = (BigDecimal)invoice.getValueAt(row, this.i_overUnder);
            final int openSign = open2.signum();
            if (col == 0) {
                if (selected) {
                    applied2 = open2;
                    applied2 = applied2.subtract(discount);
                    writeOff = Env.ZERO;
                    overUnder = Env.ZERO;
                    this.totalDiff = Env.ZERO;
                    if (this.totalDiff.abs().compareTo(applied2.abs()) < 0 && this.totalDiff.signum() == applied2.signum()) {
                        applied2 = this.totalDiff;
                    }
                    if (isAutoWriteOff) {
                        writeOff = open2.subtract(applied2.add(discount));
                    }
                    else {
                        overUnder = open2.subtract(applied2.add(discount));
                    }
                }
                else {
                    writeOff = Env.ZERO;
                    applied2 = Env.ZERO;
                    overUnder = Env.ZERO;
                }
            }
            if (selected && col != 0) {
                if (discount.signum() == -openSign) {
                    discount = discount.negate();
                }
                if (writeOff.signum() == -openSign) {
                    writeOff = writeOff.negate();
                }
                if (applied2.signum() == -openSign) {
                    applied2 = applied2.negate();
                }
                if (discount.abs().compareTo(open2.abs()) > 0) {
                    discount = open2;
                }
                if (writeOff.abs().compareTo(open2.abs()) > 0) {
                    writeOff = open2;
                }
                final BigDecimal newTotal = discount.add(writeOff).add(applied2).add(overUnder);
                BigDecimal difference = newTotal.subtract(open2);
                final BigDecimal diffWOD = writeOff.add(discount).subtract(open2);
                if (diffWOD.signum() == open2.signum()) {
                    if (col == this.i_discount) {
                        writeOff = writeOff.subtract(diffWOD);
                    }
                    else {
                        discount = discount.subtract(diffWOD);
                    }
                    difference = difference.subtract(diffWOD);
                }
                if (col == this.i_applied) {
                    overUnder = overUnder.subtract(difference);
                }
                else {
                    applied2 = applied2.subtract(difference);
                }
            }
            if (isAutoWriteOff && writeOff.doubleValue() / open2.doubleValue() > 0.3) {
                msg = "AllocationWriteOffWarn";
            }
            invoice.setValueAt((Object)discount, row, this.i_discount);
            invoice.setValueAt((Object)applied2, row, this.i_applied);
            invoice.setValueAt((Object)writeOff, row, this.i_writeOff);
            invoice.setValueAt((Object)overUnder, row, this.i_overUnder);
        }
        this.m_calculating = false;
        return msg;
    }
    
    public String calculatePayment(final IMiniTable payment, final boolean isMultiCurrency) {
        Allocation.log.config("");
        this.totalPay = Env.ZERO;
        final int rows = payment.getRowCount();
        this.m_noPayments = 0;
        for (int i = 0; i < rows; ++i) {
            if ((boolean) payment.getValueAt(i, 0)) {
                final Timestamp ts = (Timestamp)payment.getValueAt(i, 1);
                if (!isMultiCurrency) {
                    this.allocDate = TimeUtil.max(this.allocDate, ts);
                }
                final BigDecimal bd = (BigDecimal)payment.getValueAt(i, this.i_payment);
                this.totalPay = this.totalPay.add(bd);
                ++this.m_noPayments;
                if (Allocation.log.isLoggable(Level.FINE)) {
                    Allocation.log.fine("Payment_" + i + " = " + bd + " - Total=" + this.totalPay);
                }
            }
        }
        return String.valueOf(String.valueOf(this.m_noPayments)) + " - " + Msg.getMsg(Env.getCtx(), "Sum") + "  " + this.format.format(this.totalPay) + " ";
    }
    
    public String calculateInvoice(final IMiniTable invoice, final boolean isMultiCurrency) {
        this.totalInv = Env.ZERO;
        final int rows = invoice.getRowCount();
        this.m_noInvoices = 0;
        for (int i = 0; i < rows; ++i) {
            if ((boolean) invoice.getValueAt(i, 0)) {
                final Timestamp ts = (Timestamp)invoice.getValueAt(i, 1);
                if (!isMultiCurrency) {
                    this.allocDate = TimeUtil.max(this.allocDate, ts);
                }
                final BigDecimal bd = (BigDecimal)invoice.getValueAt(i, this.i_applied);
                this.totalInv = this.totalInv.add(bd);
                ++this.m_noInvoices;
                if (Allocation.log.isLoggable(Level.FINE)) {
                    Allocation.log.fine("Invoice_" + i + " = " + bd + " - Total=" + this.totalPay);
                }
            }
        }
        return String.valueOf(String.valueOf(this.m_noInvoices)) + " - " + Msg.getMsg(Env.getCtx(), "Sum") + "  " + this.format.format(this.totalInv) + " ";
    }
    
    public MAllocationHdr saveData(final int m_WindowNo, final Object date, final IMiniTable payment, final IMiniTable invoice, final String trxName) {
        if (this.m_noInvoices + this.m_noPayments == 0) {
            return null;
        }
        final int AD_Client_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Client_ID");
        final int AD_Org_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Org_ID");
        final int C_BPartner_ID = this.m_C_BPartner_ID;
        final int C_Order_ID = 0;
        final int C_CashLine_ID = 0;
        final Timestamp DateTrx = (Timestamp)date;
        final int C_Currency_ID = this.m_C_Currency_ID;
        if (AD_Org_ID == 0) {
            throw new AdempiereException("@Org0NotAllowed@");
        }
        if (Allocation.log.isLoggable(Level.CONFIG)) {
            Allocation.log.config("Client=" + AD_Client_ID + ", Org=" + AD_Org_ID + ", BPartner=" + C_BPartner_ID + ", Date=" + DateTrx);
        }
        final int pRows = payment.getRowCount();
        final ArrayList<Integer> paymentList = new ArrayList<Integer>(pRows);
        final ArrayList<BigDecimal> amountList = new ArrayList<BigDecimal>(pRows);
        BigDecimal paymentAppliedAmt = Env.ZERO;
        for (int i = 0; i < pRows; ++i) {
            if ((boolean) payment.getValueAt(i, 0)) {
                final KeyNamePair pp = (KeyNamePair)payment.getValueAt(i, 2);
                final int C_Payment_ID = pp.getKey();
                paymentList.add(new Integer(C_Payment_ID));
                final BigDecimal PaymentAmt = (BigDecimal)payment.getValueAt(i, this.i_payment);
                amountList.add(PaymentAmt);
                paymentAppliedAmt = paymentAppliedAmt.add(PaymentAmt);
                if (Allocation.log.isLoggable(Level.FINE)) {
                    Allocation.log.fine("C_Payment_ID=" + C_Payment_ID + " - PaymentAmt=" + PaymentAmt);
                }
            }
        }
        if (Allocation.log.isLoggable(Level.CONFIG)) {
            Allocation.log.config("Number of Payments=" + paymentList.size() + " - Total=" + paymentAppliedAmt);
        }
        final int iRows = invoice.getRowCount();
        final MAllocationHdr alloc = new MAllocationHdr(Env.getCtx(), true, DateTrx, C_Currency_ID, Env.getContext(Env.getCtx(), "#AD_User_Name"), trxName);
        alloc.setAD_Org_ID(AD_Org_ID);
        alloc.setC_DocType_ID(this.m_C_DocType_ID);
        alloc.setDescription(alloc.getDescriptionForManualAllocation(this.m_C_BPartner_ID, trxName));
        alloc.saveEx();
        BigDecimal unmatchedApplied = Env.ZERO;
        for (int j = 0; j < iRows; ++j) {
            if ((boolean) invoice.getValueAt(j, 0)) {
                final KeyNamePair pp2 = (KeyNamePair)invoice.getValueAt(j, 2);
                final int C_Invoice_ID = pp2.getKey();
                BigDecimal AppliedAmt = (BigDecimal)invoice.getValueAt(j, this.i_applied);
                BigDecimal DiscountAmt = (BigDecimal)invoice.getValueAt(j, this.i_discount);
                BigDecimal WriteOffAmt = (BigDecimal)invoice.getValueAt(j, this.i_writeOff);
                final BigDecimal OverUnderAmt = ((BigDecimal)invoice.getValueAt(j, this.i_open)).subtract(AppliedAmt).subtract(DiscountAmt).subtract(WriteOffAmt);
                if (Allocation.log.isLoggable(Level.CONFIG)) {
                    Allocation.log.config("Invoice #" + j + " - AppliedAmt=" + AppliedAmt);
                }
                for (int k = 0; k < paymentList.size() && AppliedAmt.signum() != 0; ++k) {
                    final int C_Payment_ID2 = paymentList.get(k);
                    BigDecimal PaymentAmt2 = amountList.get(k);
                    if (PaymentAmt2.signum() == AppliedAmt.signum()) {
                        if (Allocation.log.isLoggable(Level.CONFIG)) {
                            Allocation.log.config(".. with payment #" + k + ", Amt=" + PaymentAmt2);
                        }
                        BigDecimal amount = AppliedAmt;
                        if (amount.abs().compareTo(PaymentAmt2.abs()) > 0) {
                            amount = PaymentAmt2;
                        }
                        final MAllocationLine aLine = new MAllocationLine(alloc, amount, DiscountAmt, WriteOffAmt, OverUnderAmt);
                        aLine.setDocInfo(C_BPartner_ID, C_Order_ID, C_Invoice_ID);
                        aLine.setPaymentInfo(C_Payment_ID2, C_CashLine_ID);
                        aLine.saveEx();
                        DiscountAmt = Env.ZERO;
                        WriteOffAmt = Env.ZERO;
                        AppliedAmt = AppliedAmt.subtract(amount);
                        PaymentAmt2 = PaymentAmt2.subtract(amount);
                        if (Allocation.log.isLoggable(Level.FINE)) {
                            Allocation.log.fine("Allocation Amount=" + amount + " - Remaining  Applied=" + AppliedAmt + ", Payment=" + PaymentAmt2);
                        }
                        amountList.set(k, PaymentAmt2);
                    }
                }
                if (AppliedAmt.signum() != 0 || DiscountAmt.signum() != 0 || WriteOffAmt.signum() != 0) {
                    final int C_Payment_ID3 = 0;
                    final MAllocationLine aLine2 = new MAllocationLine(alloc, AppliedAmt, DiscountAmt, WriteOffAmt, OverUnderAmt);
                    aLine2.setDocInfo(C_BPartner_ID, C_Order_ID, C_Invoice_ID);
                    aLine2.setPaymentInfo(C_Payment_ID3, C_CashLine_ID);
                    aLine2.saveEx();
                    if (Allocation.log.isLoggable(Level.FINE)) {
                        Allocation.log.fine("Allocation Amount=" + AppliedAmt);
                    }
                    unmatchedApplied = unmatchedApplied.add(AppliedAmt);
                }
            }
        }
        for (int j = 0; j < paymentList.size(); ++j) {
            final BigDecimal payAmt = amountList.get(j);
            if (payAmt.signum() != 0) {
                final int C_Payment_ID4 = paymentList.get(j);
                if (Allocation.log.isLoggable(Level.FINE)) {
                    Allocation.log.fine("Payment=" + C_Payment_ID4 + ", Amount=" + payAmt);
                }
                final MAllocationLine aLine3 = new MAllocationLine(alloc, payAmt, Env.ZERO, Env.ZERO, Env.ZERO);
                aLine3.setDocInfo(C_BPartner_ID, 0, 0);
                aLine3.setPaymentInfo(C_Payment_ID4, 0);
                aLine3.saveEx();
                unmatchedApplied = unmatchedApplied.subtract(payAmt);
            }
        }
        if (this.m_C_Charge_ID > 0 && unmatchedApplied.compareTo(Env.ZERO) != 0) {
            final BigDecimal chargeAmt = this.totalDiff;
            final MAllocationLine aLine4 = new MAllocationLine(alloc, chargeAmt.negate(), Env.ZERO, Env.ZERO, Env.ZERO);
            aLine4.setC_Charge_ID(this.m_C_Charge_ID);
            aLine4.setC_BPartner_ID(this.m_C_BPartner_ID);
            if (!aLine4.save(trxName)) {
                final StringBuilder msg = new StringBuilder("Allocation Line not saved - Charge=").append(this.m_C_Charge_ID);
                throw new AdempiereException(msg.toString());
            }
            unmatchedApplied = unmatchedApplied.add(chargeAmt);
        }
        if (unmatchedApplied.signum() != 0) {
            throw new AdempiereException("Allocation not balanced -- out by " + unmatchedApplied);
        }
        if (alloc.get_ID() != 0) {
            if (!alloc.processIt("CO")) {
                throw new AdempiereException("Cannot complete allocation: " + alloc.getProcessMsg());
            }
            alloc.saveEx();
        }
        for (int j = 0; j < iRows; ++j) {
            if ((boolean) invoice.getValueAt(j, 0)) {
                final KeyNamePair pp2 = (KeyNamePair)invoice.getValueAt(j, 2);
                final int C_Invoice_ID = pp2.getKey();
                String sql = "SELECT invoiceOpen(C_Invoice_ID, 0) FROM C_Invoice WHERE C_Invoice_ID=?";
                final BigDecimal open = DB.getSQLValueBD(trxName, sql, C_Invoice_ID);
                if (open != null && open.signum() == 0) {
                    sql = "UPDATE C_Invoice SET IsPaid='Y' WHERE C_Invoice_ID=" + C_Invoice_ID;
                    final int no = DB.executeUpdate(sql, trxName);
                    if (Allocation.log.isLoggable(Level.CONFIG)) {
                        Allocation.log.config("Invoice #" + j + " is paid - updated=" + no);
                    }
                }
                else if (Allocation.log.isLoggable(Level.CONFIG)) {
                    Allocation.log.config("Invoice #" + j + " is not paid - " + open);
                }
            }
        }
        for (int j = 0; j < paymentList.size(); ++j) {
            final int C_Payment_ID5 = paymentList.get(j);
            final MPayment pay = new MPayment(Env.getCtx(), C_Payment_ID5, trxName);
            if (pay.testAllocation()) {
                pay.saveEx();
            }
            if (Allocation.log.isLoggable(Level.CONFIG)) {
                Allocation.log.config("Payment #" + j + (pay.isAllocated() ? " not" : " is") + " fully allocated");
            }
        }
        final MBPartner bpartner = new MBPartner(Env.getCtx(), this.m_C_BPartner_ID, trxName);
        bpartner.setTotalOpenBalance();
        bpartner.save();
        paymentList.clear();
        amountList.clear();
        return alloc;
    }
}
