package org.kjs.pola.acct;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.compiere.acct.Doc;
import org.compiere.acct.DocLine;
import org.compiere.acct.DocTax;
import org.compiere.acct.Fact;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.MCurrency;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MTax;
import org.compiere.model.PO;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class Doc_Order extends Doc
{
    private DocTax[] m_taxes;
    private DocLine[] m_requisitions;
    private int m_precision;
    
    public Doc_Order(final MAcctSchema as, final ResultSet rs, final String trxName) {
        super(as, MOrder.class, rs, (String)null, trxName);
        this.m_taxes = null;
        this.m_requisitions = null;
        this.m_precision = -1;
    }
    
    protected String loadDocumentDetails() {
        final MOrder order = (MOrder)this.getPO();
        this.setDateDoc(order.getDateOrdered());
        this.setIsTaxIncluded(order.isTaxIncluded());
        this.setAmount(0, order.getGrandTotal());
        this.setAmount(1, order.getTotalLines());
        this.setAmount(2, order.getChargeAmt());
        this.m_taxes = this.loadTaxes();
        this.p_lines = this.loadLines(order);
        return null;
    }
    
    private DocLine[] loadLines(final MOrder order) {
        final ArrayList<DocLine> list = new ArrayList<DocLine>();
        final MOrderLine[] lines = order.getLines();
        for (int i = 0; i < lines.length; ++i) {
            final MOrderLine line = lines[i];
            final DocLine docLine = new DocLine((PO)line, (Doc)this);
            final BigDecimal Qty = line.getQtyOrdered();
            docLine.setQty(Qty, order.isSOTrx());
            BigDecimal PriceCost = null;
            if (this.getDocumentType().equals("POO")) {
                PriceCost = line.getPriceCost();
            }
            BigDecimal LineNetAmt = null;
            if (PriceCost != null && PriceCost.signum() != 0) {
                LineNetAmt = Qty.multiply(PriceCost);
            }
            else {
                LineNetAmt = line.getLineNetAmt();
            }
            docLine.setAmount(LineNetAmt);
            BigDecimal PriceList = line.getPriceList();
            final int C_Tax_ID = docLine.getC_Tax_ID();
            if (this.isTaxIncluded() && C_Tax_ID != 0) {
                final MTax tax = MTax.get(this.getCtx(), C_Tax_ID);
                if (!tax.isZeroTax()) {
                    final BigDecimal LineNetAmtTax = tax.calculateTax(LineNetAmt, true, this.getStdPrecision());
                    if (this.log.isLoggable(Level.FINE)) {
                        this.log.fine("LineNetAmt=" + LineNetAmt + " - Tax=" + LineNetAmtTax);
                    }
                    LineNetAmt = LineNetAmt.subtract(LineNetAmtTax);
                    for (int t = 0; t < this.m_taxes.length; ++t) {
                        if (this.m_taxes[t].getC_Tax_ID() == C_Tax_ID) {
                            this.m_taxes[t].addIncludedTax(LineNetAmtTax);
                            break;
                        }
                    }
                    final BigDecimal PriceListTax = tax.calculateTax(PriceList, true, this.getStdPrecision());
                    PriceList = PriceList.subtract(PriceListTax);
                }
            }
            docLine.setAmount(LineNetAmt, PriceList, Qty);
            list.add(docLine);
        }
        final DocLine[] dl = new DocLine[list.size()];
        list.toArray(dl);
        return dl;
    }
    
    private DocLine[] loadRequisitions() {
        final MOrder order = (MOrder)this.getPO();
        final MOrderLine[] oLines = order.getLines();
        final HashMap<Integer, BigDecimal> qtys = new HashMap<Integer, BigDecimal>();
        for (int i = 0; i < oLines.length; ++i) {
            final MOrderLine line = oLines[i];
            qtys.put(line.getC_OrderLine_ID(), line.getQtyOrdered());
        }
        final ArrayList<DocLine> list = new ArrayList<DocLine>();
        final String sql = "SELECT * FROM M_RequisitionLine rl WHERE EXISTS (SELECT * FROM C_Order o  INNER JOIN C_OrderLine ol ON (o.C_Order_ID=ol.C_Order_ID) WHERE ol.C_OrderLine_ID=rl.C_OrderLine_ID AND o.C_Order_ID=?) ORDER BY rl.C_OrderLine_ID";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Label_0342: {
            try {
                pstmt = (PreparedStatement)DB.prepareStatement(sql, (String)null);
                pstmt.setInt(1, order.getC_Order_ID());
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    final MRequisitionLine line2 = new MRequisitionLine(this.getCtx(), rs, (String)null);
                    final DocLine docLine = new DocLine((PO)line2, (Doc)this);
                    final Integer key = line2.getC_OrderLine_ID();
                    final BigDecimal maxQty = qtys.get(key);
                    final BigDecimal Qty = line2.getQty().max(maxQty);
                    if (Qty.signum() == 0) {
                        continue;
                    }
                    docLine.setQty(Qty, false);
                    qtys.put(key, maxQty.subtract(Qty));
                    final BigDecimal PriceActual = line2.getPriceActual();
                    BigDecimal LineNetAmt = line2.getLineNetAmt();
                    if (line2.getQty().compareTo(Qty) != 0) {
                        LineNetAmt = PriceActual.multiply(Qty);
                    }
                    docLine.setAmount(LineNetAmt);
                    list.add(docLine);
                }
            }
            catch (Exception e) {
                this.log.log(Level.SEVERE, sql, (Throwable)e);
                break Label_0342;
            }
            finally {
                DB.close(rs, (Statement)pstmt);
                rs = null;
                pstmt = null;
            }
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        final DocLine[] dls = new DocLine[list.size()];
        list.toArray(dls);
        return dls;
    }
    
    private int getStdPrecision() {
        if (this.m_precision == -1) {
            this.m_precision = MCurrency.getStdPrecision(this.getCtx(), this.getC_Currency_ID());
        }
        return this.m_precision;
    }
    
    private DocTax[] loadTaxes() {
        final ArrayList<DocTax> list = new ArrayList<DocTax>();
        final StringBuilder sql = new StringBuilder("SELECT it.C_Tax_ID, t.Name, t.Rate, it.TaxBaseAmt, it.TaxAmt, t.IsSalesTax ").append("FROM C_Tax t, C_OrderTax it ").append("WHERE t.C_Tax_ID=it.C_Tax_ID AND it.C_Order_ID=?");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Label_0237: {
            try {
                pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), this.getTrxName());
                pstmt.setInt(1, this.get_ID());
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    final int C_Tax_ID = rs.getInt(1);
                    final String name = rs.getString(2);
                    final BigDecimal rate = rs.getBigDecimal(3);
                    final BigDecimal taxBaseAmt = rs.getBigDecimal(4);
                    final BigDecimal amount = rs.getBigDecimal(5);
                    final boolean salesTax = "Y".equals(rs.getString(6));
                    final DocTax taxLine = new DocTax(C_Tax_ID, name, rate, taxBaseAmt, amount, salesTax);
                    list.add(taxLine);
                }
            }
            catch (SQLException e) {
                this.log.log(Level.SEVERE, sql.toString(), (Throwable)e);
                break Label_0237;
            }
            finally {
                DB.close(rs, (Statement)pstmt);
                rs = null;
                pstmt = null;
            }
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        final DocTax[] tl = new DocTax[list.size()];
        list.toArray(tl);
        return tl;
    }
    
    public BigDecimal getBalance() {
        BigDecimal retValue = Env.ZERO;
        final StringBuilder sb = new StringBuilder(" [");
        retValue = retValue.add(this.getAmount(0));
        sb.append(this.getAmount(0));
        retValue = retValue.subtract(this.getAmount(2));
        sb.append("-").append(this.getAmount(2));
        if (this.m_taxes != null) {
            for (int i = 0; i < this.m_taxes.length; ++i) {
                retValue = retValue.subtract(this.m_taxes[i].getAmount());
                sb.append("-").append(this.m_taxes[i].getAmount());
            }
        }
        if (this.p_lines != null) {
            for (int i = 0; i < this.p_lines.length; ++i) {
                retValue = retValue.subtract(this.p_lines[i].getAmtSource());
                sb.append("-").append(this.p_lines[i].getAmtSource());
            }
            sb.append("]");
        }
        if (retValue.signum() != 0 && this.getDocumentType().equals("POO")) {
            if (this.log.isLoggable(Level.FINE)) {
                this.log.fine(String.valueOf(this.toString()) + " Balance=" + retValue + sb.toString() + " (ignored)");
            }
            retValue = Env.ZERO;
        }
        else if (this.log.isLoggable(Level.FINE)) {
            this.log.fine(String.valueOf(this.toString()) + " Balance=" + retValue + sb.toString());
        }
        return retValue;
    }
    
    public ArrayList<Fact> createFacts(final MAcctSchema as) {
        final ArrayList<Fact> facts = new ArrayList<Fact>();
        if (this.getDocumentType().equals("POO")) {
            this.updateProductPO(as);
            if (as.isCreatePOCommitment()) {
                final Fact fact = new Fact((Doc)this, as, "E");
                BigDecimal total = Env.ZERO;
                for (int i = 0; i < this.p_lines.length; ++i) {
                    final DocLine line = this.p_lines[i];
                    final BigDecimal cost = line.getAmtSource();
                    total = total.add(cost);
                    final MAccount expense = line.getAccount(2, as);
                    fact.createLine(line, expense, this.getC_Currency_ID(), cost, (BigDecimal)null);
                }
                final MAccount offset = this.getAccount(111, as);
                if (offset == null) {
                    this.p_Error = "@NotFound@ @CommitmentOffset_Acct@";
                    this.log.log(Level.SEVERE, this.p_Error);
                    return null;
                }
                fact.createLine((DocLine)null, offset, this.getC_Currency_ID(), (BigDecimal)null, total);
                facts.add(fact);
            }
            if (as.isCreateReservation()) {
                final Fact fact = new Fact((Doc)this, as, "R");
                BigDecimal total = Env.ZERO;
                if (this.m_requisitions == null) {
                    this.m_requisitions = this.loadRequisitions();
                }
                for (int i = 0; i < this.m_requisitions.length; ++i) {
                    final DocLine line = this.m_requisitions[i];
                    final BigDecimal cost = line.getAmtSource();
                    total = total.add(cost);
                    final MAccount expense = line.getAccount(2, as);
                    fact.createLine(line, expense, this.getC_Currency_ID(), (BigDecimal)null, cost);
                }
                if (this.m_requisitions.length > 0) {
                    final MAccount offset = this.getAccount(111, as);
                    if (offset == null) {
                        this.p_Error = "@NotFound@ @CommitmentOffset_Acct@";
                        this.log.log(Level.SEVERE, this.p_Error);
                        return null;
                    }
                    fact.createLine((DocLine)null, offset, this.getC_Currency_ID(), total, (BigDecimal)null);
                }
                facts.add(fact);
            }
        }
        else if (this.getDocumentType().equals("SOO") && as.isCreateSOCommitment()) {
            final Fact fact = new Fact((Doc)this, as, "E");
            BigDecimal total = Env.ZERO;
            for (int i = 0; i < this.p_lines.length; ++i) {
                final DocLine line = this.p_lines[i];
                final BigDecimal cost = line.getAmtSource();
                total = total.add(cost);
                final MAccount revenue = line.getAccount(1, as);
                fact.createLine(line, revenue, this.getC_Currency_ID(), (BigDecimal)null, cost);
            }
            final MAccount offset = this.getAccount(112, as);
            if (offset == null) {
                this.p_Error = "@NotFound@ @CommitmentOffsetSales_Acct@";
                this.log.log(Level.SEVERE, this.p_Error);
                return null;
            }
            fact.createLine((DocLine)null, offset, this.getC_Currency_ID(), total, (BigDecimal)null);
            facts.add(fact);
        }
        return facts;
    }
    
    private void updateProductPO(final MAcctSchema as) {
        final MClientInfo ci = MClientInfo.get(this.getCtx(), as.getAD_Client_ID());
        if (ci.getC_AcctSchema1_ID() != as.getC_AcctSchema_ID()) {
            return;
        }
        final StringBuilder sql = new StringBuilder("UPDATE M_Product_PO po ").append("SET PriceLastPO = (SELECT currencyConvert(ol.PriceActual,ol.C_Currency_ID,po.C_Currency_ID,o.DateOrdered,o.C_ConversionType_ID,o.AD_Client_ID,o.AD_Org_ID) ").append("FROM C_Order o, C_OrderLine ol ").append("WHERE o.C_Order_ID=ol.C_Order_ID").append(" AND po.M_Product_ID=ol.M_Product_ID AND po.C_BPartner_ID=o.C_BPartner_ID ");
        if (DB.isOracle()) {
            sql.append(" AND ROWNUM=1 ");
        }
        else {
            sql.append(" AND ol.C_OrderLine_ID = (SELECT MIN(ol1.C_OrderLine_ID) ").append("FROM C_Order o1, C_OrderLine ol1 ").append("WHERE o1.C_Order_ID=ol1.C_Order_ID").append(" AND po.M_Product_ID=ol1.M_Product_ID AND po.C_BPartner_ID=o1.C_BPartner_ID").append("  AND o1.C_Order_ID=").append(this.get_ID()).append(") ");
        }
        sql.append("  AND o.C_Order_ID=").append(this.get_ID()).append(") ").append("WHERE EXISTS (SELECT * ").append("FROM C_Order o, C_OrderLine ol ").append("WHERE o.C_Order_ID=ol.C_Order_ID").append(" AND po.M_Product_ID=ol.M_Product_ID AND po.C_BPartner_ID=o.C_BPartner_ID").append(" AND o.C_Order_ID=").append(this.get_ID()).append(")");
        final int no = DB.executeUpdate(sql.toString(), this.getTrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Updated=" + no);
        }
    }
    
    protected static DocLine[] getCommitments(final Doc doc, BigDecimal maxQty, final int C_InvoiceLine_ID) {
        int precision = -1;
        final ArrayList<DocLine> list = new ArrayList<DocLine>();
        final StringBuilder sql = new StringBuilder("SELECT * FROM C_OrderLine ol ").append("WHERE EXISTS ").append("(SELECT * FROM C_InvoiceLine il ").append("WHERE il.C_OrderLine_ID=ol.C_OrderLine_ID").append(" AND il.C_InvoiceLine_ID=?)").append(" OR EXISTS ").append("(SELECT * FROM M_MatchPO po ").append("WHERE po.C_OrderLine_ID=ol.C_OrderLine_ID").append(" AND po.C_InvoiceLine_ID=?)");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Label_0517: {
            try {
                pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
                pstmt.setInt(1, C_InvoiceLine_ID);
                pstmt.setInt(2, C_InvoiceLine_ID);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (maxQty.signum() == 0) {
                        continue;
                    }
                    final MOrderLine line = new MOrderLine(doc.getCtx(), rs, (String)null);
                    final DocLine docLine = new DocLine((PO)line, doc);
                    if (precision == -1) {
                        doc.setC_Currency_ID(docLine.getC_Currency_ID());
                        precision = MCurrency.getStdPrecision(doc.getCtx(), docLine.getC_Currency_ID());
                    }
                    final BigDecimal Qty = line.getQtyOrdered().max(maxQty);
                    docLine.setQty(Qty, false);
                    final BigDecimal PriceActual = line.getPriceActual();
                    final BigDecimal PriceCost = line.getPriceCost();
                    BigDecimal LineNetAmt = null;
                    if (PriceCost != null && PriceCost.signum() != 0) {
                        LineNetAmt = Qty.multiply(PriceCost);
                    }
                    else if (Qty.equals(maxQty)) {
                        LineNetAmt = line.getLineNetAmt();
                    }
                    else {
                        LineNetAmt = Qty.multiply(PriceActual);
                    }
                    maxQty = maxQty.subtract(Qty);
                    docLine.setAmount(LineNetAmt);
                    BigDecimal PriceList = line.getPriceList();
                    final int C_Tax_ID = docLine.getC_Tax_ID();
                    if (C_Tax_ID != 0 && line.getParent().isTaxIncluded()) {
                        final MTax tax = MTax.get(doc.getCtx(), C_Tax_ID);
                        if (!tax.isZeroTax()) {
                            final BigDecimal LineNetAmtTax = tax.calculateTax(LineNetAmt, true, precision);
                            if (Doc_Order.s_log.isLoggable(Level.FINE)) {
                                Doc_Order.s_log.fine("LineNetAmt=" + LineNetAmt + " - Tax=" + LineNetAmtTax);
                            }
                            LineNetAmt = LineNetAmt.subtract(LineNetAmtTax);
                            final BigDecimal PriceListTax = tax.calculateTax(PriceList, true, precision);
                            PriceList = PriceList.subtract(PriceListTax);
                        }
                    }
                    docLine.setAmount(LineNetAmt, PriceList, Qty);
                    list.add(docLine);
                }
            }
            catch (Exception e) {
                Doc_Order.s_log.log(Level.SEVERE, sql.toString(), (Throwable)e);
                break Label_0517;
            }
            finally {
                DB.close(rs, (Statement)pstmt);
                rs = null;
                pstmt = null;
            }
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        final DocLine[] dl = new DocLine[list.size()];
        list.toArray(dl);
        return dl;
    }
    
    protected static Fact getCommitmentRelease(final MAcctSchema as, final Doc doc, final BigDecimal Qty, final int C_InvoiceLine_ID, final BigDecimal multiplier) {
        final Fact fact = new Fact(doc, as, "E");
        final DocLine[] commitments = getCommitments(doc, Qty, C_InvoiceLine_ID);
        BigDecimal total = Env.ZERO;
        int C_Currency_ID = -1;
        for (int i = 0; i < commitments.length; ++i) {
            final DocLine line = commitments[i];
            if (C_Currency_ID == -1) {
                C_Currency_ID = line.getC_Currency_ID();
            }
            else if (C_Currency_ID != line.getC_Currency_ID()) {
                return null;
            }
            final BigDecimal cost = line.getAmtSource().multiply(multiplier);
            total = total.add(cost);
            final MAccount expense = line.getAccount(2, as);
            fact.createLine(line, expense, C_Currency_ID, (BigDecimal)null, cost);
        }
        final MAccount offset = doc.getAccount(111, as);
        if (offset == null) {
            return null;
        }
        fact.createLine((DocLine)null, offset, C_Currency_ID, total, (BigDecimal)null);
        return fact;
    }
    
    protected static DocLine[] getCommitmentsSales(final Doc doc, BigDecimal maxQty, final int M_InOutLine_ID) {
        int precision = -1;
        final ArrayList<DocLine> list = new ArrayList<DocLine>();
        final StringBuilder sql = new StringBuilder("SELECT * FROM C_OrderLine ol ").append("WHERE EXISTS ").append("(SELECT * FROM M_InOutLine il ").append("WHERE il.C_OrderLine_ID=ol.C_OrderLine_ID").append(" AND il.M_InOutLine_ID=?)");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Label_0484: {
            try {
                pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
                pstmt.setInt(1, M_InOutLine_ID);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (maxQty.signum() == 0) {
                        continue;
                    }
                    final MOrderLine line = new MOrderLine(doc.getCtx(), rs, (String)null);
                    final DocLine docLine = new DocLine((PO)line, doc);
                    if (precision == -1) {
                        doc.setC_Currency_ID(docLine.getC_Currency_ID());
                        precision = MCurrency.getStdPrecision(doc.getCtx(), docLine.getC_Currency_ID());
                    }
                    final BigDecimal Qty = line.getQtyOrdered().max(maxQty);
                    docLine.setQty(Qty, false);
                    final BigDecimal PriceActual = line.getPriceActual();
                    final BigDecimal PriceCost = line.getPriceCost();
                    BigDecimal LineNetAmt = null;
                    if (PriceCost != null && PriceCost.signum() != 0) {
                        LineNetAmt = Qty.multiply(PriceCost);
                    }
                    else if (Qty.equals(maxQty)) {
                        LineNetAmt = line.getLineNetAmt();
                    }
                    else {
                        LineNetAmt = Qty.multiply(PriceActual);
                    }
                    maxQty = maxQty.subtract(Qty);
                    docLine.setAmount(LineNetAmt);
                    BigDecimal PriceList = line.getPriceList();
                    final int C_Tax_ID = docLine.getC_Tax_ID();
                    if (C_Tax_ID != 0 && line.getParent().isTaxIncluded()) {
                        final MTax tax = MTax.get(doc.getCtx(), C_Tax_ID);
                        if (!tax.isZeroTax()) {
                            final BigDecimal LineNetAmtTax = tax.calculateTax(LineNetAmt, true, precision);
                            if (Doc_Order.s_log.isLoggable(Level.FINE)) {
                                Doc_Order.s_log.fine("LineNetAmt=" + LineNetAmt + " - Tax=" + LineNetAmtTax);
                            }
                            LineNetAmt = LineNetAmt.subtract(LineNetAmtTax);
                            final BigDecimal PriceListTax = tax.calculateTax(PriceList, true, precision);
                            PriceList = PriceList.subtract(PriceListTax);
                        }
                    }
                    docLine.setAmount(LineNetAmt, PriceList, Qty);
                    list.add(docLine);
                }
            }
            catch (Exception e) {
                Doc_Order.s_log.log(Level.SEVERE, sql.toString(), (Throwable)e);
                break Label_0484;
            }
            finally {
                DB.close(rs, (Statement)pstmt);
                rs = null;
                pstmt = null;
            }
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        final DocLine[] dl = new DocLine[list.size()];
        list.toArray(dl);
        return dl;
    }
    
    protected static Fact getCommitmentSalesRelease(final MAcctSchema as, final Doc doc, final BigDecimal Qty, final int M_InOutLine_ID, final BigDecimal multiplier) {
        final Fact fact = new Fact(doc, as, "E");
        final DocLine[] commitments = getCommitmentsSales(doc, Qty, M_InOutLine_ID);
        BigDecimal total = Env.ZERO;
        int C_Currency_ID = -1;
        for (int i = 0; i < commitments.length; ++i) {
            final DocLine line = commitments[i];
            if (C_Currency_ID == -1) {
                C_Currency_ID = line.getC_Currency_ID();
            }
            else if (C_Currency_ID != line.getC_Currency_ID()) {
                return null;
            }
            final BigDecimal cost = line.getAmtSource().multiply(multiplier);
            total = total.add(cost);
            final MAccount revenue = line.getAccount(1, as);
            fact.createLine(line, revenue, C_Currency_ID, cost, (BigDecimal)null);
        }
        final MAccount offset = doc.getAccount(112, as);
        if (offset == null) {
            return null;
        }
        fact.createLine((DocLine)null, offset, C_Currency_ID, (BigDecimal)null, total);
        return fact;
    }
}
