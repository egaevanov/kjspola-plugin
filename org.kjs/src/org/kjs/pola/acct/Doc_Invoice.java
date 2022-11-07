package org.kjs.pola.acct;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.adempiere.exceptions.AverageCostingZeroQtyException;
import org.compiere.acct.Doc;
import org.compiere.acct.DocLine;
import org.compiere.acct.DocTax;
import org.compiere.acct.Fact;
import org.compiere.acct.FactLine;
import org.compiere.model.I_M_InOutLine;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCostDetail;
import org.compiere.model.MCurrency;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLandedCostAllocation;
import org.compiere.model.MOrderLandedCostAllocation;
import org.compiere.model.MTax;
import org.compiere.model.PO;
import org.compiere.model.ProductCost;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

public class Doc_Invoice extends Doc
{
    protected DocTax[] m_taxes;
    protected int m_precision;
    protected boolean m_allLinesService;
    protected boolean m_allLinesItem;
    
    public Doc_Invoice(final MAcctSchema as, final ResultSet rs, final String trxName) {
        super(as, (Class)MInvoice.class, rs, (String)null, trxName);
        this.m_taxes = null;
        this.m_precision = -1;
        this.m_allLinesService = true;
        this.m_allLinesItem = true;
    }
    
    protected String loadDocumentDetails() {
        final MInvoice invoice = (MInvoice)this.getPO();
        this.setDateDoc(invoice.getDateInvoiced());
        this.setIsTaxIncluded(invoice.isTaxIncluded());
        this.setAmount(0, invoice.getGrandTotal());
        this.setAmount(1, invoice.getTotalLines());
        this.setAmount(2, invoice.getChargeAmt());
        this.m_taxes = this.loadTaxes();
        this.p_lines = this.loadLines(invoice);
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Lines=" + this.p_lines.length + ", Taxes=" + this.m_taxes.length);
        }
        return null;
    }
    
    private DocTax[] loadTaxes() {
        final ArrayList<DocTax> list = new ArrayList<DocTax>();
        final String sql = "SELECT it.C_Tax_ID, t.Name, t.Rate, it.TaxBaseAmt, it.TaxAmt, t.IsSalesTax FROM C_Tax t, C_InvoiceTax it WHERE t.C_Tax_ID=it.C_Tax_ID AND it.C_Invoice_ID=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql, this.getTrxName());
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
                if (this.log.isLoggable(Level.FINE)) {
                    this.log.fine(taxLine.toString());
                }
                list.add(taxLine);
            }
        }
        catch (SQLException e) {
            this.log.log(Level.SEVERE, sql, (Throwable)e);
            return null;
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        final DocTax[] tl = new DocTax[list.size()];
        list.toArray(tl);
        return tl;
    }
    
    private DocLine[] loadLines(final MInvoice invoice) {
        final ArrayList<DocLine> list = new ArrayList<DocLine>();
        final MInvoiceLine[] lines = invoice.getLines(false);
        for (int i = 0; i < lines.length; ++i) {
            final MInvoiceLine line = lines[i];
            if (!line.isDescription()) {
                final DocLine docLine = new DocLine((PO)line, (Doc)this);
                final BigDecimal Qty = line.getQtyInvoiced();
                final boolean cm = this.getDocumentType().equals("ARC") || this.getDocumentType().equals("APC");
                docLine.setQty(cm ? Qty.negate() : Qty, invoice.isSOTrx());
                BigDecimal LineNetAmt = line.getLineNetAmt();
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
                        if (tax.isSummary()) {
                            BigDecimal sumChildLineNetAmtTax = Env.ZERO;
                            DocTax taxToApplyDiff = null;
                            MTax[] childTaxes = null;
                            for (int length = (childTaxes = tax.getChildTaxes((boolean)(0 != 0))).length, l = 0; l < length; ++l) {
                                final MTax childTax = childTaxes[l];
                                if (!childTax.isZeroTax()) {
                                    final BigDecimal childLineNetAmtTax = childTax.calculateTax(LineNetAmt, false, this.getStdPrecision());
                                    if (this.log.isLoggable(Level.FINE)) {
                                        this.log.fine("LineNetAmt=" + LineNetAmt + " - Child Tax=" + childLineNetAmtTax);
                                    }
                                    for (int t = 0; t < this.m_taxes.length; ++t) {
                                        if (this.m_taxes[t].getC_Tax_ID() == childTax.getC_Tax_ID()) {
                                            this.m_taxes[t].addIncludedTax(childLineNetAmtTax);
                                            taxToApplyDiff = this.m_taxes[t];
                                            sumChildLineNetAmtTax = sumChildLineNetAmtTax.add(childLineNetAmtTax);
                                            break;
                                        }
                                    }
                                }
                            }
                            final BigDecimal diffChildVsSummary = LineNetAmtTax.subtract(sumChildLineNetAmtTax);
                            if (diffChildVsSummary.signum() != 0 && taxToApplyDiff != null) {
                                taxToApplyDiff.addIncludedTax(diffChildVsSummary);
                            }
                        }
                        else {
                            for (int t2 = 0; t2 < this.m_taxes.length; ++t2) {
                                if (this.m_taxes[t2].getC_Tax_ID() == C_Tax_ID) {
                                    this.m_taxes[t2].addIncludedTax(LineNetAmtTax);
                                    break;
                                }
                            }
                        }
                        final BigDecimal PriceListTax = tax.calculateTax(PriceList, true, this.getStdPrecision());
                        PriceList = PriceList.subtract(PriceListTax);
                    }
                }
                docLine.setAmount(LineNetAmt, PriceList, Qty);
                if (docLine.isItem()) {
                    this.m_allLinesService = false;
                }
                else {
                    this.m_allLinesItem = false;
                }
                if (this.log.isLoggable(Level.FINE)) {
                    this.log.fine(docLine.toString());
                }
                list.add(docLine);
            }
        }
        final DocLine[] dls = new DocLine[list.size()];
        list.toArray(dls);
        if (this.isTaxIncluded()) {
            for (int j = 0; j < this.m_taxes.length; ++j) {
                if (this.m_taxes[j].isIncludedTaxDifference()) {
                    BigDecimal diff = this.m_taxes[j].getIncludedTaxDifference();
                    for (int k = 0; k < dls.length; ++k) {
                        final MTax lineTax = MTax.get(this.getCtx(), dls[k].getC_Tax_ID());
                        MTax[] composingTaxes = null;
                        if (lineTax.isSummary()) {
                            composingTaxes = lineTax.getChildTaxes(false);
                        }
                        else {
                            composingTaxes = new MTax[] { lineTax };
                        }
                        MTax[] array;
                        for (int length2 = (array = composingTaxes).length, n = 0; n < length2; ++n) {
                            final MTax mTax = array[n];
                            if (mTax.getC_Tax_ID() == this.m_taxes[j].getC_Tax_ID()) {
                                dls[k].setLineNetAmtDifference(diff);
                                this.m_taxes[j].addIncludedTax(diff.negate());
                                diff = Env.ZERO;
                                break;
                            }
                        }
                        if (diff.signum() == 0) {
                            break;
                        }
                    }
                }
            }
        }
        return dls;
    }
    
    private int getStdPrecision() {
        if (this.m_precision == -1) {
            this.m_precision = MCurrency.getStdPrecision(this.getCtx(), this.getC_Currency_ID());
        }
        return this.m_precision;
    }
    
    public BigDecimal getBalance() {
        BigDecimal retValue = Env.ZERO;
        final StringBuilder sb = new StringBuilder(" [");
        retValue = retValue.add(this.getAmount(0));
        sb.append(this.getAmount(0));
        retValue = retValue.subtract(this.getAmount(2));
        sb.append("-").append(this.getAmount(2));
        for (int i = 0; i < this.m_taxes.length; ++i) {
            retValue = retValue.subtract(this.m_taxes[i].getAmount());
            sb.append("-").append(this.m_taxes[i].getAmount());
        }
        for (int i = 0; i < this.p_lines.length; ++i) {
            retValue = retValue.subtract(this.p_lines[i].getAmtSource());
            sb.append("-").append(this.p_lines[i].getAmtSource());
        }
        sb.append("]");
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine(String.valueOf(this.toString()) + " Balance=" + retValue + sb.toString());
        }
        return retValue;
    }
    
    public ArrayList<Fact> createFacts(final MAcctSchema as) {
        final ArrayList<Fact> facts = new ArrayList<Fact>();
        Fact fact = new Fact((Doc)this, as, "A");
        if (!as.isAccrual()) {
            return facts;
        }
        if (this.getDocumentType().equals("ARI") || this.getDocumentType().equals("ARF")) {
            BigDecimal grossAmt = this.getAmount(0);
            BigDecimal serviceAmt = Env.ZERO;
            BigDecimal amt = this.getAmount(2);
            if (amt != null && amt.signum() != 0) {
                fact.createLine((DocLine)null, this.getAccount(0, as), this.getC_Currency_ID(), (BigDecimal)null, amt);
            }
            for (int i = 0; i < this.m_taxes.length; ++i) {
                amt = this.m_taxes[i].getAmount();
                if (amt != null && amt.signum() != 0) {
                    final FactLine tl = fact.createLine((DocLine)null, this.m_taxes[i].getAccount(0, as), this.getC_Currency_ID(), (BigDecimal)null, amt);
                    if (tl != null) {
                        tl.setC_Tax_ID(this.m_taxes[i].getC_Tax_ID());
                    }
                }
            }
            for (int i = 0; i < this.p_lines.length; ++i) {
                amt = this.p_lines[i].getAmtSource();
                BigDecimal dAmt = null;
                if (as.isTradeDiscountPosted()) {
                    final BigDecimal discount = this.p_lines[i].getDiscount();
                    if (discount != null && discount.signum() != 0) {
                        amt = amt.add(discount);
                        dAmt = discount;
                        fact.createLine(this.p_lines[i], this.p_lines[i].getAccount(8, as), this.getC_Currency_ID(), dAmt, (BigDecimal)null);
                    }
                }
                if (this.getDocumentType().equals("ARF")) {
                    fact.createLine(this.p_lines[i], this.getAccount(13, as), this.getC_Currency_ID(), (BigDecimal)null, amt);
                }
                else {
                    fact.createLine(this.p_lines[i], this.p_lines[i].getAccount(1, as), this.getC_Currency_ID(), (BigDecimal)null, amt);
                }
                if (!this.p_lines[i].isItem()) {
                    grossAmt = grossAmt.subtract(amt);
                    serviceAmt = serviceAmt.add(amt);
                }
            }
            final FactLine[] fLines = fact.getLines();
            for (int j = 0; j < fLines.length; ++j) {
                if (fLines[j] != null) {
                    fLines[j].setLocationFromOrg(fLines[j].getAD_Org_ID(), true);
                    fLines[j].setLocationFromBPartner(this.getC_BPartner_Location_ID(), false);
                }
            }
            final int receivables_ID = this.getValidCombination_ID(1, as);
            final int receivablesServices_ID = this.getValidCombination_ID(4, as);
            if (this.m_allLinesItem || !as.isPostServices() || receivables_ID == receivablesServices_ID) {
                grossAmt = this.getAmount(0);
                serviceAmt = Env.ZERO;
            }
            else if (this.m_allLinesService) {
                serviceAmt = this.getAmount(0);
                grossAmt = Env.ZERO;
            }
            if (grossAmt.signum() != 0) {
                fact.createLine((DocLine)null, MAccount.get(this.getCtx(), receivables_ID), this.getC_Currency_ID(), grossAmt, (BigDecimal)null);
            }
            if (serviceAmt.signum() != 0) {
                fact.createLine((DocLine)null, MAccount.get(this.getCtx(), receivablesServices_ID), this.getC_Currency_ID(), serviceAmt, (BigDecimal)null);
            }
        }
        else if (this.getDocumentType().equals("ARC")) {
            BigDecimal grossAmt = this.getAmount(0);
            BigDecimal serviceAmt = Env.ZERO;
            BigDecimal amt = this.getAmount(2);
            if (amt != null && amt.signum() != 0) {
                fact.createLine((DocLine)null, this.getAccount(0, as), this.getC_Currency_ID(), amt, (BigDecimal)null);
            }
            for (int i = 0; i < this.m_taxes.length; ++i) {
                amt = this.m_taxes[i].getAmount();
                if (amt != null && amt.signum() != 0) {
                    final FactLine tl = fact.createLine((DocLine)null, this.m_taxes[i].getAccount(0, as), this.getC_Currency_ID(), amt, (BigDecimal)null);
                    if (tl != null) {
                        tl.setC_Tax_ID(this.m_taxes[i].getC_Tax_ID());
                    }
                }
            }
            for (int i = 0; i < this.p_lines.length; ++i) {
                amt = this.p_lines[i].getAmtSource();
                BigDecimal dAmt = null;
                if (as.isTradeDiscountPosted()) {
                    final BigDecimal discount = this.p_lines[i].getDiscount();
                    if (discount != null && discount.signum() != 0) {
                        amt = amt.add(discount);
                        dAmt = discount;
                        fact.createLine(this.p_lines[i], this.p_lines[i].getAccount(8, as), this.getC_Currency_ID(), (BigDecimal)null, dAmt);
                    }
                }
                fact.createLine(this.p_lines[i], this.p_lines[i].getAccount(1, as), this.getC_Currency_ID(), amt, (BigDecimal)null);
                if (!this.p_lines[i].isItem()) {
                    grossAmt = grossAmt.subtract(amt);
                    serviceAmt = serviceAmt.add(amt);
                }
            }
            final FactLine[] fLines = fact.getLines();
            for (int j = 0; j < fLines.length; ++j) {
                if (fLines[j] != null) {
                    fLines[j].setLocationFromOrg(fLines[j].getAD_Org_ID(), true);
                    fLines[j].setLocationFromBPartner(this.getC_BPartner_Location_ID(), false);
                }
            }
            final int receivables_ID = this.getValidCombination_ID(1, as);
            final int receivablesServices_ID = this.getValidCombination_ID(4, as);
            if (this.m_allLinesItem || !as.isPostServices() || receivables_ID == receivablesServices_ID) {
                grossAmt = this.getAmount(0);
                serviceAmt = Env.ZERO;
            }
            else if (this.m_allLinesService) {
                serviceAmt = this.getAmount(0);
                grossAmt = Env.ZERO;
            }
            if (grossAmt.signum() != 0) {
                fact.createLine((DocLine)null, MAccount.get(this.getCtx(), receivables_ID), this.getC_Currency_ID(), (BigDecimal)null, grossAmt);
            }
            if (serviceAmt.signum() != 0) {
                fact.createLine((DocLine)null, MAccount.get(this.getCtx(), receivablesServices_ID), this.getC_Currency_ID(), (BigDecimal)null, serviceAmt);
            }
        }
        else if (this.getDocumentType().equals("API")) {
            BigDecimal grossAmt = this.getAmount(0);
            BigDecimal serviceAmt = Env.ZERO;
            fact.createLine((DocLine)null, this.getAccount(0, as), this.getC_Currency_ID(), this.getAmount(2), (BigDecimal)null);
            for (int k = 0; k < this.m_taxes.length; ++k) {
                final FactLine tl2 = fact.createLine((DocLine)null, this.m_taxes[k].getAccount(this.m_taxes[k].getAPTaxType(), as), this.getC_Currency_ID(), this.m_taxes[k].getAmount(), (BigDecimal)null);
                if (tl2 != null) {
                    tl2.setC_Tax_ID(this.m_taxes[k].getC_Tax_ID());
                }
            }
            for (int k = 0; k < this.p_lines.length; ++k) {
                final DocLine line = this.p_lines[k];
                final boolean landedCost = this.landedCost(as, fact, line, true);
                if (landedCost && as.isExplicitCostAdjustment()) {
                    fact.createLine(line, line.getAccount(2, as), this.getC_Currency_ID(), line.getAmtSource(), (BigDecimal)null);
                    final FactLine fl = fact.createLine(line, line.getAccount(2, as), this.getC_Currency_ID(), (BigDecimal)null, line.getAmtSource());
                    String desc = line.getDescription();
                    if (desc == null) {
                        desc = "100%";
                    }
                    else {
                        desc = String.valueOf(desc) + " 100%";
                    }
                    fl.setDescription(desc);
                }
                if (!landedCost) {
                    MAccount expense = line.getAccount(2, as);
                    if (line.isItem()) {
                        expense = line.getAccount(10, as);
                    }
                    BigDecimal amt2 = line.getAmtSource();
                    BigDecimal dAmt2 = null;
                    if (as.isTradeDiscountPosted() && !line.isItem()) {
                        final BigDecimal discount2 = line.getDiscount();
                        if (discount2 != null && discount2.signum() != 0) {
                            amt2 = amt2.add(discount2);
                            dAmt2 = discount2;
                            final MAccount tradeDiscountReceived = line.getAccount(7, as);
                            fact.createLine(line, tradeDiscountReceived, this.getC_Currency_ID(), (BigDecimal)null, dAmt2);
                        }
                    }
                    fact.createLine(line, expense, this.getC_Currency_ID(), amt2, (BigDecimal)null);
                    if (!line.isItem()) {
                        grossAmt = grossAmt.subtract(amt2);
                        serviceAmt = serviceAmt.add(amt2);
                    }
                    if (line.getM_Product_ID() != 0 && line.getProduct().isService()) {
                        MCostDetail.createInvoice(as, line.getAD_Org_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), line.get_ID(), 0, line.getAmtSource(), line.getQty(), line.getDescription(), this.getTrxName());
                    }
                }
            }
            final FactLine[] fLines2 = fact.getLines();
            for (int i = 0; i < fLines2.length; ++i) {
                if (fLines2[i] != null) {
                    fLines2[i].setLocationFromBPartner(this.getC_BPartner_Location_ID(), true);
                    fLines2[i].setLocationFromOrg(fLines2[i].getAD_Org_ID(), false);
                }
            }
            final int payables_ID = this.getValidCombination_ID(2, as);
            final int payablesServices_ID = this.getValidCombination_ID(3, as);
            if (this.m_allLinesItem || !as.isPostServices() || payables_ID == payablesServices_ID) {
                grossAmt = this.getAmount(0);
                serviceAmt = Env.ZERO;
            }
            else if (this.m_allLinesService) {
                serviceAmt = this.getAmount(0);
                grossAmt = Env.ZERO;
            }
            if (grossAmt.signum() != 0) {
                fact.createLine((DocLine)null, MAccount.get(this.getCtx(), payables_ID), this.getC_Currency_ID(), (BigDecimal)null, grossAmt);
            }
            if (serviceAmt.signum() != 0) {
                fact.createLine((DocLine)null, MAccount.get(this.getCtx(), payablesServices_ID), this.getC_Currency_ID(), (BigDecimal)null, serviceAmt);
            }
            this.updateProductPO(as);
        }
        else if (this.getDocumentType().equals("APC")) {
            BigDecimal grossAmt = this.getAmount(0);
            BigDecimal serviceAmt = Env.ZERO;
            fact.createLine((DocLine)null, this.getAccount(0, as), this.getC_Currency_ID(), (BigDecimal)null, this.getAmount(2));
            for (int k = 0; k < this.m_taxes.length; ++k) {
                final FactLine tl2 = fact.createLine((DocLine)null, this.m_taxes[k].getAccount(this.m_taxes[k].getAPTaxType(), as), this.getC_Currency_ID(), (BigDecimal)null, this.m_taxes[k].getAmount());
                if (tl2 != null) {
                    tl2.setC_Tax_ID(this.m_taxes[k].getC_Tax_ID());
                }
            }
            for (int k = 0; k < this.p_lines.length; ++k) {
                final DocLine line = this.p_lines[k];
                final boolean landedCost = this.landedCost(as, fact, line, false);
                if (landedCost && as.isExplicitCostAdjustment()) {
                    fact.createLine(line, line.getAccount(2, as), this.getC_Currency_ID(), (BigDecimal)null, line.getAmtSource());
                    final FactLine fl = fact.createLine(line, line.getAccount(2, as), this.getC_Currency_ID(), line.getAmtSource(), (BigDecimal)null);
                    String desc = line.getDescription();
                    if (desc == null) {
                        desc = "100%";
                    }
                    else {
                        desc = String.valueOf(desc) + " 100%";
                    }
                    fl.setDescription(desc);
                }
                if (!landedCost) {
                    MAccount expense = line.getAccount(2, as);
                    if (line.isItem()) {
                        expense = line.getAccount(10, as);
                    }
                    BigDecimal amt2 = line.getAmtSource();
                    BigDecimal dAmt2 = null;
                    if (as.isTradeDiscountPosted() && !line.isItem()) {
                        final BigDecimal discount2 = line.getDiscount();
                        if (discount2 != null && discount2.signum() != 0) {
                            amt2 = amt2.add(discount2);
                            dAmt2 = discount2;
                            final MAccount tradeDiscountReceived = line.getAccount(7, as);
                            fact.createLine(line, tradeDiscountReceived, this.getC_Currency_ID(), dAmt2, (BigDecimal)null);
                        }
                    }
                    fact.createLine(line, expense, this.getC_Currency_ID(), (BigDecimal)null, amt2);
                    if (!line.isItem()) {
                        grossAmt = grossAmt.subtract(amt2);
                        serviceAmt = serviceAmt.add(amt2);
                    }
                    if (line.getM_Product_ID() != 0 && line.getProduct().isService()) {
                        MCostDetail.createInvoice(as, line.getAD_Org_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), line.get_ID(), 0, line.getAmtSource().negate(), line.getQty(), line.getDescription(), this.getTrxName());
                    }
                }
            }
            final FactLine[] fLines2 = fact.getLines();
            for (int i = 0; i < fLines2.length; ++i) {
                if (fLines2[i] != null) {
                    fLines2[i].setLocationFromBPartner(this.getC_BPartner_Location_ID(), true);
                    fLines2[i].setLocationFromOrg(fLines2[i].getAD_Org_ID(), false);
                }
            }
            final int payables_ID = this.getValidCombination_ID(2, as);
            final int payablesServices_ID = this.getValidCombination_ID(3, as);
            if (this.m_allLinesItem || !as.isPostServices() || payables_ID == payablesServices_ID) {
                grossAmt = this.getAmount(0);
                serviceAmt = Env.ZERO;
            }
            else if (this.m_allLinesService) {
                serviceAmt = this.getAmount(0);
                grossAmt = Env.ZERO;
            }
            if (grossAmt.signum() != 0) {
                fact.createLine((DocLine)null, MAccount.get(this.getCtx(), payables_ID), this.getC_Currency_ID(), grossAmt, (BigDecimal)null);
            }
            if (serviceAmt.signum() != 0) {
                fact.createLine((DocLine)null, MAccount.get(this.getCtx(), payablesServices_ID), this.getC_Currency_ID(), serviceAmt, (BigDecimal)null);
            }
        }
        else {
            this.p_Error = "DocumentType unknown: " + this.getDocumentType();
            this.log.log(Level.SEVERE, this.p_Error);
            fact = null;
        }
        facts.add(fact);
        return facts;
    }
    
    public BigDecimal createFactCash(final MAcctSchema as, final Fact fact, final BigDecimal multiplier) {
        final boolean creditMemo = this.getDocumentType().equals("ARC") || this.getDocumentType().equals("APC");
        final boolean payables = this.getDocumentType().equals("API") || this.getDocumentType().equals("APC");
        BigDecimal acctAmt = Env.ZERO;
        FactLine fl = null;
        for (int i = 0; i < this.p_lines.length; ++i) {
            final DocLine line = this.p_lines[i];
            boolean landedCost = false;
            if (payables) {
                landedCost = this.landedCost(as, fact, line, false);
            }
            if (landedCost && as.isExplicitCostAdjustment()) {
                fact.createLine(line, line.getAccount(2, as), this.getC_Currency_ID(), (BigDecimal)null, line.getAmtSource());
                fl = fact.createLine(line, line.getAccount(2, as), this.getC_Currency_ID(), line.getAmtSource(), (BigDecimal)null);
                String desc = line.getDescription();
                if (desc == null) {
                    desc = "100%";
                }
                else {
                    desc = String.valueOf(desc) + " 100%";
                }
                fl.setDescription(desc);
            }
            if (!landedCost) {
                MAccount acct = line.getAccount(payables ? 2 : 1, as);
                if (payables && line.isItem()) {
                    acct = line.getAccount(10, as);
                }
                BigDecimal amt = line.getAmtSource().multiply(multiplier);
                BigDecimal amt2 = null;
                if (creditMemo) {
                    amt2 = amt;
                    amt = null;
                }
                if (payables) {
                    fl = fact.createLine(line, acct, this.getC_Currency_ID(), amt, amt2);
                }
                else {
                    fl = fact.createLine(line, acct, this.getC_Currency_ID(), amt2, amt);
                }
                if (fl != null) {
                    acctAmt = acctAmt.add(fl.getAcctBalance());
                }
            }
        }
        for (int i = 0; i < this.m_taxes.length; ++i) {
            BigDecimal amt3 = this.m_taxes[i].getAmount();
            BigDecimal amt4 = null;
            if (creditMemo) {
                amt4 = amt3;
                amt3 = null;
            }
            FactLine tl = null;
            if (payables) {
                tl = fact.createLine((DocLine)null, this.m_taxes[i].getAccount(this.m_taxes[i].getAPTaxType(), as), this.getC_Currency_ID(), amt3, amt4);
            }
            else {
                tl = fact.createLine((DocLine)null, this.m_taxes[i].getAccount(0, as), this.getC_Currency_ID(), amt4, amt3);
            }
            if (tl != null) {
                tl.setC_Tax_ID(this.m_taxes[i].getC_Tax_ID());
            }
        }
        final FactLine[] fLines = fact.getLines();
        for (int j = 0; j < fLines.length; ++j) {
            if (fLines[j] != null) {
                if (payables) {
                    fLines[j].setLocationFromBPartner(this.getC_BPartner_Location_ID(), true);
                    fLines[j].setLocationFromOrg(fLines[j].getAD_Org_ID(), false);
                }
                else {
                    fLines[j].setLocationFromOrg(fLines[j].getAD_Org_ID(), true);
                    fLines[j].setLocationFromBPartner(this.getC_BPartner_Location_ID(), false);
                }
            }
        }
        return acctAmt;
    }
    
    protected boolean landedCost(final MAcctSchema as, final Fact fact, final DocLine line, final boolean dr) {
        final int C_InvoiceLine_ID = line.get_ID();
        final MLandedCostAllocation[] lcas = MLandedCostAllocation.getOfInvoiceLine(this.getCtx(), C_InvoiceLine_ID, this.getTrxName());
        if (lcas.length == 0) {
            return false;
        }
        double totalBase = 0.0;
        for (int i = 0; i < lcas.length; ++i) {
            totalBase += lcas[i].getBase().doubleValue();
        }
        final Map<String, BigDecimal> costDetailAmtMap = new HashMap<String, BigDecimal>();
        final MInvoiceLine il = new MInvoiceLine(this.getCtx(), C_InvoiceLine_ID, this.getTrxName());
        for (int j = 0; j < lcas.length; ++j) {
            final MLandedCostAllocation lca = lcas[j];
            if (lca.getBase().signum() != 0) {
                final double percent = lca.getBase().doubleValue() / totalBase;
                String desc = il.getDescription();
                if (desc == null) {
                    desc = String.valueOf(percent) + "%";
                }
                else {
                    desc = String.valueOf(desc) + " - " + percent + "%";
                }
                if (line.getDescription() != null) {
                    desc = String.valueOf(desc) + " - " + line.getDescription();
                }
                BigDecimal drAmt = null;
                BigDecimal crAmt = null;
                MAccount account = null;
                final ProductCost pc = new ProductCost(Env.getCtx(), lca.getM_Product_ID(), lca.getM_AttributeSetInstance_ID(), this.getTrxName());
                final String costingMethod = pc.getProduct().getCostingMethod(as);
                if ("I".equals(costingMethod) || "A".equals(costingMethod)) {
                    BigDecimal allocationAmt = lca.getAmt();
                    BigDecimal estimatedAmt = BigDecimal.ZERO;
                    if (lca.getM_InOutLine_ID() > 0) {
                        final I_M_InOutLine iol = lca.getM_InOutLine();
                        if (iol.getC_OrderLine_ID() > 0) {
                            final MOrderLandedCostAllocation[] allocations = MOrderLandedCostAllocation.getOfOrderLine(iol.getC_OrderLine_ID(), this.getTrxName());
                            MOrderLandedCostAllocation[] array;
                            for (int length = (array = allocations).length, k = 0; k < length; ++k) {
                                final MOrderLandedCostAllocation allocation = array[k];
                                if (allocation.getC_OrderLandedCost().getM_CostElement_ID() == lca.getM_CostElement_ID()) {
                                    BigDecimal amt = allocation.getAmt();
                                    final BigDecimal qty = allocation.getQty();
                                    if (qty.compareTo(iol.getMovementQty()) != 0) {
                                        amt = amt.multiply(iol.getMovementQty()).divide(qty, 12, 4);
                                    }
                                    estimatedAmt = estimatedAmt.add(amt);
                                }
                            }
                        }
                    }
                    if (estimatedAmt.scale() > as.getCostingPrecision()) {
                        estimatedAmt = estimatedAmt.setScale(as.getCostingPrecision(), 4);
                    }
                    BigDecimal costAdjustmentAmt = allocationAmt;
                    if (estimatedAmt.signum() > 0) {
                        final StringBuilder sql = new StringBuilder("SELECT Sum(Amt) FROM C_LandedCostAllocation WHERE M_InOutLine_ID=? ").append("AND C_LandedCostAllocation_ID<>? ").append("AND M_CostElement_ID=? ").append("AND AD_Client_ID=? ");
                        final BigDecimal otherAmt = DB.getSQLValueBD(this.getTrxName(), sql.toString(), new Object[] { lca.getM_InOutLine_ID(), lca.getC_LandedCostAllocation_ID(), lca.getM_CostElement_ID(), lca.getAD_Client_ID() });
                        if (otherAmt != null) {
                            estimatedAmt = estimatedAmt.subtract(otherAmt);
                            if (allocationAmt.signum() < 0) {
                                estimatedAmt = estimatedAmt.add(allocationAmt.negate());
                            }
                        }
                        if (estimatedAmt.signum() > 0) {
                            if (allocationAmt.signum() > 0) {
                                costAdjustmentAmt = allocationAmt.subtract(estimatedAmt);
                            }
                            else if (allocationAmt.signum() < 0) {
                                costAdjustmentAmt = allocationAmt.add(estimatedAmt);
                            }
                        }
                    }
                    if (!dr) {
                        costAdjustmentAmt = costAdjustmentAmt.negate();
                    }
                    boolean zeroQty = false;
                    if (costAdjustmentAmt.signum() != 0) {
                        final Trx trx = Trx.get(this.getTrxName(), false);
                        Savepoint savepoint = null;
                        try {
                            savepoint = trx.setSavepoint((String)null);
                            BigDecimal costDetailAmt = costAdjustmentAmt;
                            if (this.getC_Currency_ID() != as.getC_Currency_ID()) {
                                costDetailAmt = MConversionRate.convert(this.getCtx(), costDetailAmt, this.getC_Currency_ID(), as.getC_Currency_ID(), this.getDateAcct(), this.getC_ConversionType_ID(), this.getAD_Client_ID(), this.getAD_Org_ID());
                            }
                            if (costDetailAmt.scale() > as.getCostingPrecision()) {
                                costDetailAmt = costDetailAmt.setScale(as.getCostingPrecision(), 4);
                            }
                            final String key = String.valueOf(lca.getM_Product_ID()) + "_" + lca.getM_AttributeSetInstance_ID();
                            final BigDecimal prevAmt = costDetailAmtMap.remove(key);
                            if (prevAmt != null) {
                                costDetailAmt = costDetailAmt.add(prevAmt);
                            }
                            costDetailAmtMap.put(key, costDetailAmt);
                            if (!MCostDetail.createInvoice(as, lca.getAD_Org_ID(), lca.getM_Product_ID(), lca.getM_AttributeSetInstance_ID(), C_InvoiceLine_ID, lca.getM_CostElement_ID(), costDetailAmt, lca.getQty(), desc, this.getTrxName())) {
                                throw new RuntimeException("Failed to create cost detail record.");
                            }
                        }
                        catch (SQLException e) {
                            throw new RuntimeException(e.getLocalizedMessage(), e);
                        }
                        catch (AverageCostingZeroQtyException ex) {
                            zeroQty = true;
                            try {
                                trx.rollback(savepoint);
                                savepoint = null;
                            }
                            catch (SQLException e2) {
                                throw new RuntimeException(e2.getLocalizedMessage(), e2);
                            }
                        }
                        finally {
                            if (savepoint != null) {
                                try {
                                    trx.releaseSavepoint(savepoint);
                                }
                                catch (SQLException ex2) {}
                            }
                        }
                        if (savepoint != null) {
                            try {
                                trx.releaseSavepoint(savepoint);
                            }
                            catch (SQLException ex3) {}
                        }
                    }
                    boolean reversal = false;
                    if (allocationAmt.signum() < 0) {
                        allocationAmt = allocationAmt.negate();
                        reversal = true;
                    }
                    if (allocationAmt.signum() > 0) {
                        if (allocationAmt.scale() > as.getStdPrecision()) {
                            allocationAmt = allocationAmt.setScale(as.getStdPrecision(), 4);
                        }
                        if (estimatedAmt.scale() > as.getStdPrecision()) {
                            estimatedAmt = estimatedAmt.setScale(as.getStdPrecision(), 4);
                        }
                        final int compare = allocationAmt.compareTo(estimatedAmt);
                        if (compare > 0) {
                            drAmt = (dr ? (reversal ? null : estimatedAmt) : (reversal ? estimatedAmt : null));
                            crAmt = (dr ? (reversal ? estimatedAmt : null) : (reversal ? null : estimatedAmt));
                            account = pc.getAccount(24, as);
                            FactLine fl = fact.createLine(line, account, this.getC_Currency_ID(), drAmt, crAmt);
                            fl.setDescription(desc);
                            fl.setM_Product_ID(lca.getM_Product_ID());
                            fl.setQty(line.getQty());
                            final BigDecimal overAmt = allocationAmt.subtract(estimatedAmt);
                            drAmt = (dr ? (reversal ? null : overAmt) : (reversal ? overAmt : null));
                            crAmt = (dr ? (reversal ? overAmt : null) : (reversal ? null : overAmt));
                            account = (zeroQty ? pc.getAccount(23, as) : pc.getAccount(3, as));
                            fl = fact.createLine(line, account, this.getC_Currency_ID(), drAmt, crAmt);
                            fl.setDescription(desc);
                            fl.setM_Product_ID(lca.getM_Product_ID());
                            fl.setQty(line.getQty());
                        }
                        else if (compare < 0) {
                            drAmt = (dr ? (reversal ? null : estimatedAmt) : (reversal ? estimatedAmt : null));
                            crAmt = (dr ? (reversal ? estimatedAmt : null) : (reversal ? null : estimatedAmt));
                            account = pc.getAccount(24, as);
                            FactLine fl = fact.createLine(line, account, this.getC_Currency_ID(), drAmt, crAmt);
                            fl.setDescription(desc);
                            fl.setM_Product_ID(lca.getM_Product_ID());
                            fl.setQty(line.getQty());
                            final BigDecimal underAmt = estimatedAmt.subtract(allocationAmt);
                            drAmt = (dr ? (reversal ? underAmt : null) : (reversal ? null : underAmt));
                            crAmt = (dr ? (reversal ? null : underAmt) : (reversal ? underAmt : null));
                            account = (zeroQty ? pc.getAccount(23, as) : pc.getAccount(3, as));
                            fl = fact.createLine(line, account, this.getC_Currency_ID(), drAmt, crAmt);
                            fl.setDescription(desc);
                            fl.setM_Product_ID(lca.getM_Product_ID());
                            fl.setQty(line.getQty());
                        }
                        else {
                            drAmt = (dr ? (reversal ? null : allocationAmt) : (reversal ? allocationAmt : null));
                            crAmt = (dr ? (reversal ? allocationAmt : null) : (reversal ? null : allocationAmt));
                            account = pc.getAccount(24, as);
                            final FactLine fl = fact.createLine(line, account, this.getC_Currency_ID(), drAmt, crAmt);
                            fl.setDescription(desc);
                            fl.setM_Product_ID(lca.getM_Product_ID());
                            fl.setQty(line.getQty());
                        }
                    }
                }
                else {
                    if (dr) {
                        drAmt = lca.getAmt();
                    }
                    else {
                        crAmt = lca.getAmt();
                    }
                    account = pc.getAccount(9, as);
                    final FactLine fl2 = fact.createLine(line, account, this.getC_Currency_ID(), drAmt, crAmt);
                    fl2.setDescription(desc);
                    fl2.setM_Product_ID(lca.getM_Product_ID());
                    fl2.setQty(line.getQty());
                }
            }
        }
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("Created #" + lcas.length);
        }
        return true;
    }
    
    protected void updateProductPO(final MAcctSchema as) {
        final MClientInfo ci = MClientInfo.get(this.getCtx(), as.getAD_Client_ID());
        if (ci.getC_AcctSchema1_ID() != as.getC_AcctSchema_ID()) {
            return;
        }
        final StringBuilder sql = new StringBuilder("UPDATE M_Product_PO po ").append("SET PriceLastInv = ").append("(SELECT currencyConvert(il.PriceActual,i.C_Currency_ID,po.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID) ").append("FROM C_Invoice i, C_InvoiceLine il ").append("WHERE i.C_Invoice_ID=il.C_Invoice_ID").append(" AND po.M_Product_ID=il.M_Product_ID AND po.C_BPartner_ID=i.C_BPartner_ID");
        if (DB.isOracle()) {
            sql.append(" AND ROWNUM=1 ");
        }
        else {
            sql.append(" AND il.C_InvoiceLine_ID = (SELECT MIN(il1.C_InvoiceLine_ID) ").append("FROM C_Invoice i1, C_InvoiceLine il1 ").append("WHERE i1.C_Invoice_ID=il1.C_Invoice_ID").append(" AND po.M_Product_ID=il1.M_Product_ID AND po.C_BPartner_ID=i1.C_BPartner_ID").append("  AND i1.C_Invoice_ID=").append(this.get_ID()).append(") ");
        }
        sql.append("  AND i.C_Invoice_ID=").append(this.get_ID()).append(") ").append("WHERE EXISTS (SELECT * ").append("FROM C_Invoice i, C_InvoiceLine il ").append("WHERE i.C_Invoice_ID=il.C_Invoice_ID").append(" AND po.M_Product_ID=il.M_Product_ID AND po.C_BPartner_ID=i.C_BPartner_ID").append(" AND i.C_Invoice_ID=").append(this.get_ID()).append(")");
        final int no = DB.executeUpdate(sql.toString(), this.getTrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Updated=" + no);
        }
    }
}
