package org.kjs.pola.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import org.adempiere.exceptions.AverageCostingZeroQtyException;
import org.compiere.acct.Doc;
import org.compiere.acct.DocLine;
import org.compiere.acct.Fact;
import org.compiere.acct.FactLine;
import org.compiere.model.I_C_Order;
import org.compiere.model.I_C_OrderLine;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCostDetail;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MMatchInv;
import org.compiere.model.MOrderLandedCostAllocation;
import org.compiere.model.ProductCost;
import org.compiere.util.Env;
import org.compiere.util.Trx;

public class Doc_MatchInv extends Doc
{
    private MInvoiceLine m_invoiceLine;
    private MInOutLine m_receiptLine;
    private ProductCost m_pc;
    private MMatchInv m_matchInv;
    private ArrayList<FactLine> m_lines;
    
    public Doc_MatchInv(final MAcctSchema as, final ResultSet rs, final String trxName) {
        super(as, (Class)MMatchInv.class, rs, "MXI", trxName);
        this.m_invoiceLine = null;
        this.m_receiptLine = null;
        this.m_pc = null;
        this.m_lines = new ArrayList<FactLine>();
    }
    
    protected String loadDocumentDetails() {
        this.setC_Currency_ID(-2);
        this.m_matchInv = (MMatchInv)this.getPO();
        this.setDateDoc(this.m_matchInv.getDateTrx());
        this.setQty(this.m_matchInv.getQty());
        final int C_InvoiceLine_ID = this.m_matchInv.getC_InvoiceLine_ID();
        this.m_invoiceLine = new MInvoiceLine(this.getCtx(), C_InvoiceLine_ID, this.getTrxName());
        final int C_BPartner_ID = this.m_invoiceLine.getParent().getC_BPartner_ID();
        this.setC_BPartner_ID(C_BPartner_ID);
        final int M_InOutLine_ID = this.m_matchInv.getM_InOutLine_ID();
        this.m_receiptLine = new MInOutLine(this.getCtx(), M_InOutLine_ID, this.getTrxName());
        (this.m_pc = new ProductCost(Env.getCtx(), this.getM_Product_ID(), this.m_matchInv.getM_AttributeSetInstance_ID(), this.getTrxName())).setQty(this.getQty());
        return null;
    }
    
    public BigDecimal getBalance() {
        return Env.ZERO;
    }
    
    public boolean isReversal() {
        final int docID = this.m_matchInv.get_ID();
        final int reversalID = this.m_matchInv.getReversal_ID();
        return reversalID > 0 && reversalID < docID;
    }
    
    public ArrayList<Fact> createFacts(final MAcctSchema as) {
        final ArrayList<Fact> facts = new ArrayList<Fact>();
        if (this.getM_Product_ID() == 0 || this.getQty().signum() == 0 || this.m_receiptLine.getMovementQty().signum() == 0) {
            if (this.log.isLoggable(Level.FINE)) {
                this.log.fine("No Product/Qty - M_Product_ID=" + this.getM_Product_ID() + ",Qty=" + this.getQty() + ",InOutQty=" + this.m_receiptLine.getMovementQty());
            }
            return facts;
        }
        Fact fact = new Fact((Doc)this, as, "A");
        this.setC_Currency_ID(as.getC_Currency_ID());
        final boolean isInterOrg = this.isInterOrg(as);
        BigDecimal multiplier = this.getQty().divide(this.m_receiptLine.getMovementQty(), 12, 4).abs();
        final FactLine dr = fact.createLine((DocLine)null, this.getAccount(51, as), as.getC_Currency_ID(), Env.ONE, (BigDecimal)null);
        if (dr == null) {
            this.p_Error = "No Product Costs";
            return null;
        }
        dr.setQty(this.getQty());
        BigDecimal temp = dr.getAcctBalance();
        if (this.isReversal()) {
            if (!dr.updateReverseLine(472, this.m_matchInv.getReversal_ID(), 0, BigDecimal.ONE)) {
                this.p_Error = "Failed to create reversal entry";
                return null;
            }
        }
        else {
            BigDecimal effMultiplier = multiplier;
            if (this.getQty().signum() < 0) {
                effMultiplier = effMultiplier.negate();
            }
            if (!dr.updateReverseLine(319, this.m_receiptLine.getM_InOut_ID(), this.m_receiptLine.getM_InOutLine_ID(), effMultiplier)) {
                this.p_Error = "Mat.Receipt not posted yet";
                return null;
            }
        }
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("CR - Amt(" + temp + "->" + dr.getAcctBalance() + ") - " + dr.toString());
        }
        MAccount expense = this.m_pc.getAccount(10, as);
        if (this.m_pc.isService()) {
            expense = this.m_pc.getAccount(2, as);
        }
        BigDecimal LineNetAmt = this.m_invoiceLine.getLineNetAmt();
        multiplier = this.getQty().divide(this.m_invoiceLine.getQtyInvoiced(), 12, 4).abs();
        if (multiplier.compareTo(Env.ONE) != 0) {
            LineNetAmt = LineNetAmt.multiply(multiplier);
        }
        if (this.m_pc.isService()) {
            LineNetAmt = dr.getAcctBalance();
        }
        FactLine cr = null;
        if (as.isAccrual()) {
            cr = fact.createLine((DocLine)null, expense, as.getC_Currency_ID(), (BigDecimal)null, LineNetAmt);
            if (cr == null) {
                if (this.log.isLoggable(Level.FINE)) {
                    this.log.fine("Line Net Amt=0 - M_Product_ID=" + this.getM_Product_ID() + ",Qty=" + this.getQty() + ",InOutQty=" + this.m_receiptLine.getMovementQty());
                }
                cr = fact.createLine((DocLine)null, expense, as.getC_Currency_ID(), (BigDecimal)null, Env.ONE);
                cr.setAmtAcctCr(BigDecimal.ZERO);
                cr.setAmtSourceCr(BigDecimal.ZERO);
            }
            temp = cr.getAcctBalance();
            if (this.isReversal()) {
                if (!cr.updateReverseLine(472, this.m_matchInv.getReversal_ID(), 0, BigDecimal.ONE)) {
                    this.p_Error = "Failed to create reversal entry";
                    return null;
                }
            }
            else {
                cr.setQty(this.getQty().negate());
                BigDecimal effMultiplier2 = multiplier;
                if (this.getQty().signum() < 0) {
                    effMultiplier2 = effMultiplier2.negate();
                }
                if (!cr.updateReverseLine(318, this.m_invoiceLine.getC_Invoice_ID(), this.m_invoiceLine.getC_InvoiceLine_ID(), effMultiplier2)) {
                    this.p_Error = "Invoice not posted yet";
                    return null;
                }
            }
            if (this.log.isLoggable(Level.FINE)) {
                this.log.fine("DR - Amt(" + temp + "->" + cr.getAcctBalance() + ") - " + cr.toString());
            }
        }
        else {
            final MInvoice invoice = this.m_invoiceLine.getParent();
            if (as.getC_Currency_ID() != invoice.getC_Currency_ID()) {
                LineNetAmt = MConversionRate.convert(this.getCtx(), LineNetAmt, invoice.getC_Currency_ID(), as.getC_Currency_ID(), invoice.getDateAcct(), invoice.getC_ConversionType_ID(), invoice.getAD_Client_ID(), invoice.getAD_Org_ID());
            }
            cr = fact.createLine((DocLine)null, expense, as.getC_Currency_ID(), (BigDecimal)null, LineNetAmt);
            if (this.isReversal()) {
                if (!cr.updateReverseLine(472, this.m_matchInv.getReversal_ID(), 0, BigDecimal.ONE)) {
                    this.p_Error = "Failed to create reversal entry";
                    return null;
                }
            }
            else {
                cr.setQty(this.getQty().multiply(multiplier).negate());
            }
        }
        if (this.isReversal()) {
            cr.setC_Activity_ID(this.m_invoiceLine.getC_Activity_ID());
            cr.setC_Campaign_ID(this.m_invoiceLine.getC_Campaign_ID());
            cr.setC_Project_ID(this.m_invoiceLine.getC_Project_ID());
            cr.setC_ProjectPhase_ID(this.m_invoiceLine.getC_ProjectPhase_ID());
            cr.setC_ProjectTask_ID(this.m_invoiceLine.getC_ProjectTask_ID());
            cr.setC_UOM_ID(this.m_invoiceLine.getC_UOM_ID());
            cr.setUser1_ID(this.m_invoiceLine.getUser1_ID());
            cr.setUser2_ID(this.m_invoiceLine.getUser2_ID());
        }
        else {
            this.updateFactLine(cr);
        }
        if (dr.getC_Currency_ID() != cr.getC_Currency_ID()) {
            this.setIsMultiCurrency(true);
        }
        final MAccount acct_db = dr.getAccount();
        final MAccount acct_cr = cr.getAccount();
        if (!as.isPostIfClearingEqual() && acct_db.equals((Object)acct_cr) && !isInterOrg) {
            final BigDecimal debit = dr.getAmtSourceDr();
            final BigDecimal credit = cr.getAmtSourceCr();
            if (debit.compareTo(credit) == 0) {
                fact.remove(dr);
                fact.remove(cr);
            }
        }
        final BigDecimal ipv = cr.getSourceBalance().add(dr.getSourceBalance()).negate();
        this.processInvoicePriceVariance(as, fact, ipv, dr.getC_Currency_ID());
        if (cr.getC_Currency_ID() != as.getC_Currency_ID()) {
            this.setIsMultiCurrency(true);
        }
        final String error = this.createMatchInvCostDetail(as);
        if (error != null && error.trim().length() > 0) {
            this.p_Error = error;
            return null;
        }
        facts.add(fact);
        if (as.isAccrual() && as.isCreatePOCommitment()) {
            fact = Doc_Order.getCommitmentRelease(as, (Doc)this, this.getQty(), this.m_invoiceLine.getC_InvoiceLine_ID(), Env.ONE);
            if (fact == null) {
                return null;
            }
            facts.add(fact);
        }
        this.createRealizedGainLoss(as, fact);
        return facts;
    }
    
    private String createRealizedGainLoss(final MAcctSchema as, final Fact fact) {
        BigDecimal acctDifference = Env.ZERO;
        final MAccount gain = MAccount.get(as.getCtx(), as.getAcctSchemaDefault().getRealizedGain_Acct());
        final MAccount loss = MAccount.get(as.getCtx(), as.getAcctSchemaDefault().getRealizedLoss_Acct());
        if (fact.isSourceBalanced() && !fact.isAcctBalanced()) {
            acctDifference = this.getAcctBalance().negate();
            fact.createLine((DocLine)null, loss, gain, as.getC_Currency_ID(), acctDifference);
        }
        return null;
    }
    
    protected BigDecimal getAcctBalance() {
        BigDecimal result = Env.ZERO;
        for (int i = 0; i < this.m_lines.size(); ++i) {
            final FactLine line = this.m_lines.get(i);
            result = result.add(line.getAcctBalance());
        }
        return result;
    }
    
    protected void processInvoicePriceVariance(final MAcctSchema as, final Fact fact, final BigDecimal ipv, final int C_Currency_ID) {
        if (ipv.signum() == 0) {
            return;
        }
        final FactLine pv = fact.createLine((DocLine)null, this.m_pc.getAccount(6, as), C_Currency_ID, ipv);
        this.updateFactLine(pv);
        final MMatchInv matchInv = (MMatchInv)this.getPO();
        final Trx trx = Trx.get(this.getTrxName(), false);
        Savepoint savepoint = null;
        boolean zeroQty = false;
        try {
            savepoint = trx.setSavepoint((String)null);
            if (!MCostDetail.createMatchInvoice(as, this.m_invoiceLine.getAD_Org_ID(), this.m_invoiceLine.getM_Product_ID(), this.m_invoiceLine.getM_AttributeSetInstance_ID(), matchInv.getM_MatchInv_ID(), 0, ipv, BigDecimal.ZERO, "Invoice Price Variance", this.getTrxName())) {
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
        final String costingMethod = this.m_pc.getProduct().getCostingMethod(as);
        if ("A".equals(costingMethod)) {
            final MAccount account = zeroQty ? this.m_pc.getAccount(23, as) : this.m_pc.getAccount(3, as);
            FactLine line = fact.createLine((DocLine)null, this.m_pc.getAccount(6, as), as.getC_Currency_ID(), ipv.negate());
            this.updateFactLine(line);
            line = fact.createLine((DocLine)null, account, as.getC_Currency_ID(), ipv);
            this.updateFactLine(line);
        }
        else if ("I".equals(costingMethod) && !zeroQty) {
            final MAccount account = this.m_pc.getAccount(3, as);
            FactLine line = fact.createLine((DocLine)null, this.m_pc.getAccount(6, as), as.getC_Currency_ID(), ipv.negate());
            this.updateFactLine(line);
            line = fact.createLine((DocLine)null, account, as.getC_Currency_ID(), ipv);
            this.updateFactLine(line);
        }
    }
    
    private boolean isInterOrg(final MAcctSchema as) {
        final MAcctSchemaElement elementorg = as.getAcctSchemaElement("OO");
        return elementorg != null && elementorg.isBalanced() && (this.m_receiptLine != null && this.m_invoiceLine != null && this.m_receiptLine.getAD_Org_ID() != this.m_invoiceLine.getAD_Org_ID());
    }
    
    private String createMatchInvCostDetail(final MAcctSchema as) {
        if (this.m_invoiceLine != null && this.m_invoiceLine.get_ID() > 0 && this.m_receiptLine != null && this.m_receiptLine.get_ID() > 0) {
            final MMatchInv matchInv = (MMatchInv)this.getPO();
            BigDecimal LineNetAmt = this.m_invoiceLine.getLineNetAmt();
            BigDecimal multiplier = this.getQty().divide(this.m_invoiceLine.getQtyInvoiced(), 12, 4).abs();
            if (multiplier.compareTo(Env.ONE) != 0) {
                LineNetAmt = LineNetAmt.multiply(multiplier);
            }
            final MMatchInv[] mInv = MMatchInv.getInvoiceLine(this.getCtx(), this.m_invoiceLine.getC_InvoiceLine_ID(), this.getTrxName());
            BigDecimal tQty = Env.ZERO;
            BigDecimal tAmt = Env.ZERO;
            for (int i = 0; i < mInv.length; ++i) {
                if (mInv[i].isPosted() && mInv[i].getM_MatchInv_ID() != this.get_ID() && mInv[i].getM_AttributeSetInstance_ID() == matchInv.getM_AttributeSetInstance_ID()) {
                    tQty = tQty.add(mInv[i].getQty());
                    multiplier = mInv[i].getQty().divide(this.m_invoiceLine.getQtyInvoiced(), 12, 4).abs();
                    tAmt = tAmt.add(this.m_invoiceLine.getLineNetAmt().multiply(multiplier));
                }
            }
            tAmt = tAmt.add(LineNetAmt);
            final MInvoice invoice = this.m_invoiceLine.getParent();
            if (as.getC_Currency_ID() != invoice.getC_Currency_ID()) {
                tAmt = MConversionRate.convert(this.getCtx(), tAmt, invoice.getC_Currency_ID(), as.getC_Currency_ID(), invoice.getDateAcct(), invoice.getC_ConversionType_ID(), invoice.getAD_Client_ID(), invoice.getAD_Org_ID());
                if (tAmt == null) {
                    return "AP Invoice not convertible - " + as.getName();
                }
            }
            final MInOut receipt = this.m_receiptLine.getParent();
            if (receipt.getMovementType().equals("V-")) {
                tQty = tQty.add(this.getQty().negate());
            }
            else {
                tQty = tQty.add(this.getQty());
            }
            if (!MCostDetail.createInvoice(as, this.getAD_Org_ID(), this.getM_Product_ID(), matchInv.getM_AttributeSetInstance_ID(), this.m_invoiceLine.getC_InvoiceLine_ID(), 0, tAmt, tQty, this.getDescription(), this.getTrxName())) {
                return "Failed to create cost detail record";
            }
            final Map<Integer, BigDecimal> landedCostMap = new LinkedHashMap<Integer, BigDecimal>();
            final I_C_OrderLine orderLine = this.m_receiptLine.getC_OrderLine();
            if (orderLine == null) {
                return "";
            }
            final int C_OrderLine_ID = orderLine.getC_OrderLine_ID();
            final MOrderLandedCostAllocation[] allocations = MOrderLandedCostAllocation.getOfOrderLine(C_OrderLine_ID, this.getTrxName());
            MOrderLandedCostAllocation[] array;
            for (int length = (array = allocations).length, j = 0; j < length; ++j) {
                final MOrderLandedCostAllocation allocation = array[j];
                final BigDecimal totalAmt = allocation.getAmt();
                final BigDecimal totalQty = allocation.getQty();
                BigDecimal amt = totalAmt.multiply(tQty).divide(totalQty, 12, 4);
                if (orderLine.getC_Currency_ID() != as.getC_Currency_ID()) {
                    final I_C_Order order = orderLine.getC_Order();
                    final Timestamp dateAcct = order.getDateAcct();
                    final BigDecimal rate = MConversionRate.getRate(order.getC_Currency_ID(), as.getC_Currency_ID(), dateAcct, order.getC_ConversionType_ID(), order.getAD_Client_ID(), order.getAD_Org_ID());
                    if (rate == null) {
                        this.p_Error = "Purchase Order not convertible - " + as.getName();
                        return null;
                    }
                    amt = amt.multiply(rate);
                    if (amt.scale() > as.getCostingPrecision()) {
                        amt = amt.setScale(as.getCostingPrecision(), 4);
                    }
                }
                final int elementId = allocation.getC_OrderLandedCost().getM_CostElement_ID();
                BigDecimal elementAmt = landedCostMap.get(elementId);
                if (elementAmt == null) {
                    elementAmt = amt;
                }
                else {
                    elementAmt = elementAmt.add(amt);
                }
                landedCostMap.put(elementId, elementAmt);
            }
            for (final Integer elementId2 : landedCostMap.keySet()) {
                final BigDecimal amt2 = landedCostMap.get(elementId2);
                if (!MCostDetail.createShipment(as, this.getAD_Org_ID(), this.getM_Product_ID(), matchInv.getM_AttributeSetInstance_ID(), this.m_receiptLine.getM_InOutLine_ID(), (int)elementId2, amt2, tQty, this.getDescription(), false, this.getTrxName())) {
                    return "Failed to create cost detail record";
                }
            }
        }
        return "";
    }
    
    protected void updateFactLine(final FactLine factLine) {
        factLine.setC_Activity_ID(this.m_invoiceLine.getC_Activity_ID());
        factLine.setC_Campaign_ID(this.m_invoiceLine.getC_Campaign_ID());
        factLine.setC_Project_ID(this.m_invoiceLine.getC_Project_ID());
        factLine.setC_ProjectPhase_ID(this.m_invoiceLine.getC_ProjectPhase_ID());
        factLine.setC_ProjectTask_ID(this.m_invoiceLine.getC_ProjectTask_ID());
        factLine.setC_UOM_ID(this.m_invoiceLine.getC_UOM_ID());
        factLine.setUser1_ID(this.m_invoiceLine.getUser1_ID());
        factLine.setUser2_ID(this.m_invoiceLine.getUser2_ID());
        factLine.setM_Product_ID(this.m_invoiceLine.getM_Product_ID());
        factLine.setQty(this.getQty());
    }
}
