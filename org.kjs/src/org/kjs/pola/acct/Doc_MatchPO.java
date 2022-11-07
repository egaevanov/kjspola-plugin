package org.kjs.pola.acct;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.acct.Doc;
import org.compiere.acct.DocLine;
import org.compiere.acct.Fact;
import org.compiere.acct.FactLine;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCostDetail;
import org.compiere.model.MCurrency;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MMatchPO;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLandedCostAllocation;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MTax;
import org.compiere.model.ProductCost;
import org.compiere.util.Env;
import org.compiere.util.Util;

public class Doc_MatchPO extends Doc
{
    private int m_C_OrderLine_ID;
    private MOrderLine m_oLine;
    private int m_M_InOutLine_ID;
    private MInOutLine m_ioLine;
    private int m_C_InvoiceLine_ID;
    private ProductCost m_pc;
    private int m_M_AttributeSetInstance_ID;
    private MMatchPO m_matchPO;
    
    public Doc_MatchPO(final MAcctSchema as, final ResultSet rs, final String trxName) {
        super(as, (Class)MMatchPO.class, rs, "MXP", trxName);
        this.m_C_OrderLine_ID = 0;
        this.m_oLine = null;
        this.m_M_InOutLine_ID = 0;
        this.m_ioLine = null;
        this.m_C_InvoiceLine_ID = 0;
        this.m_M_AttributeSetInstance_ID = 0;
    }
    
    protected String loadDocumentDetails() {
        this.setC_Currency_ID(-2);
        this.m_matchPO = (MMatchPO)this.getPO();
        this.setDateDoc(this.m_matchPO.getDateTrx());
        this.m_M_AttributeSetInstance_ID = this.m_matchPO.getM_AttributeSetInstance_ID();
        this.setQty(this.m_matchPO.getQty());
        this.m_C_OrderLine_ID = this.m_matchPO.getC_OrderLine_ID();
        this.m_oLine = new MOrderLine(this.getCtx(), this.m_C_OrderLine_ID, this.getTrxName());
        this.m_M_InOutLine_ID = this.m_matchPO.getM_InOutLine_ID();
        this.m_ioLine = new MInOutLine(this.getCtx(), this.m_M_InOutLine_ID, this.getTrxName());
        this.m_C_InvoiceLine_ID = this.m_matchPO.getC_InvoiceLine_ID();
        (this.m_pc = new ProductCost(Env.getCtx(), this.getM_Product_ID(), this.m_M_AttributeSetInstance_ID, this.getTrxName())).setQty(this.getQty());
        return null;
    }
    
    public BigDecimal getBalance() {
        return Env.ZERO;
    }
    
    public ArrayList<Fact> createFacts(final MAcctSchema as) {
        final ArrayList<Fact> facts = new ArrayList<Fact>();
        if (this.getM_Product_ID() == 0 || this.getQty().signum() == 0) {
            if (this.log.isLoggable(Level.FINE)) {
                this.log.fine("No Product/Qty - M_Product_ID=" + this.getM_Product_ID() + ",Qty=" + this.getQty());
            }
            return facts;
        }
        if (this.m_M_InOutLine_ID == 0) {
            final MMatchPO[] matchPOs = MMatchPO.getOrderLine(this.getCtx(), this.m_oLine.getC_OrderLine_ID(), this.getTrxName());
            MMatchPO[] array;
            for (int length = (array = matchPOs).length, i = 0; i < length; ++i) {
                final MMatchPO matchPO = array[i];
                if (matchPO.getM_InOutLine_ID() > 0 && matchPO.getC_InvoiceLine_ID() == 0) {
                    this.m_M_InOutLine_ID = matchPO.getM_InOutLine_ID();
                    break;
                }
            }
        }
        if (this.m_M_InOutLine_ID == 0) {
            this.p_Error = "No posting if not matched to Shipment";
            return null;
        }
        final Fact fact = new Fact((Doc)this, as, "A");
        this.setC_Currency_ID(as.getC_Currency_ID());
        final boolean isInterOrg = this.isInterOrg(as);
        BigDecimal poCost = this.m_oLine.getPriceCost();
        if (poCost == null || poCost.signum() == 0) {
            poCost = this.m_oLine.getPriceActual();
            final int C_Tax_ID = this.m_oLine.getC_Tax_ID();
            if (this.m_oLine.isTaxIncluded() && C_Tax_ID != 0) {
                final MTax tax = MTax.get(this.getCtx(), C_Tax_ID);
                if (!tax.isZeroTax()) {
                    final int stdPrecision = MCurrency.getStdPrecision(this.getCtx(), this.m_oLine.getC_Currency_ID());
                    final BigDecimal costTax = tax.calculateTax(poCost, true, stdPrecision);
                    if (this.log.isLoggable(Level.FINE)) {
                        this.log.fine("Costs=" + poCost + " - Tax=" + costTax);
                    }
                    poCost = poCost.subtract(costTax);
                }
            }
        }
        final MInOutLine receiptLine = new MInOutLine(this.getCtx(), this.m_M_InOutLine_ID, this.getTrxName());
        final MInOut inOut = receiptLine.getParent();
        final boolean isReturnTrx = inOut.getMovementType().equals("V-");
        final BigDecimal deliveredCost = poCost.multiply(this.getQty());
        final Map<Integer, BigDecimal> landedCostMap = new LinkedHashMap<Integer, BigDecimal>();
        BigDecimal landedCost = BigDecimal.ZERO;
        final int C_OrderLine_ID = this.m_oLine.getC_OrderLine_ID();
        final MOrderLandedCostAllocation[] allocations = MOrderLandedCostAllocation.getOfOrderLine(C_OrderLine_ID, this.getTrxName());
        MOrderLandedCostAllocation[] array2;
        for (int length2 = (array2 = allocations).length, j = 0; j < length2; ++j) {
            final MOrderLandedCostAllocation allocation = array2[j];
            final BigDecimal totalAmt = allocation.getAmt();
            final BigDecimal totalQty = allocation.getQty();
            BigDecimal amt = totalAmt.multiply(this.m_ioLine.getMovementQty()).divide(totalQty, 12, RoundingMode.HALF_UP);
            if (this.m_oLine.getC_Currency_ID() != as.getC_Currency_ID()) {
                final MOrder order = this.m_oLine.getParent();
                final MInOut inout = this.m_ioLine.getParent();
                final Timestamp dateAcct = inout.getDateAcct();
                final BigDecimal rate = MConversionRate.getRate(order.getC_Currency_ID(), as.getC_Currency_ID(), dateAcct, order.getC_ConversionType_ID(), this.m_oLine.getAD_Client_ID(), this.m_oLine.getAD_Org_ID());
                if (rate == null) {
                    this.p_Error = "Purchase Order not convertible - " + as.getName();
                    return null;
                }
                amt = amt.multiply(rate);
            }
            amt = amt.divide(this.getQty(), 12, RoundingMode.HALF_UP);
            landedCost = landedCost.add(amt);
            if (landedCost.scale() > as.getCostingPrecision()) {
                landedCost = landedCost.setScale(as.getCostingPrecision(), 4);
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
        final BigDecimal totalCost = deliveredCost.add(landedCost);
        if (this.m_oLine.getC_Currency_ID() != as.getC_Currency_ID()) {
            final MOrder order2 = this.m_oLine.getParent();
            final MInOut inout2 = this.m_ioLine.getParent();
            final Timestamp dateAcct2 = inout2.getDateAcct();
            final BigDecimal rate2 = MConversionRate.getRate(order2.getC_Currency_ID(), as.getC_Currency_ID(), dateAcct2, order2.getC_ConversionType_ID(), this.m_oLine.getAD_Client_ID(), this.m_oLine.getAD_Org_ID());
            if (rate2 == null) {
                this.p_Error = "Purchase Order not convertible - " + as.getName();
                return null;
            }
            poCost = poCost.multiply(rate2);
            if (poCost.scale() > as.getCostingPrecision()) {
                poCost = poCost.setScale(as.getCostingPrecision(), 4);
            }
        }
        final String costingError = this.createMatchPOCostDetail(as, poCost, landedCostMap);
        if (costingError != null && costingError.trim().length() > 0) {
            this.p_Error = costingError;
            return null;
        }
        final MProduct product = MProduct.get(this.getCtx(), this.getM_Product_ID());
        final String costingMethod = product.getCostingMethod(as);
        BigDecimal costs = this.m_pc.getProductCosts(as, this.getAD_Org_ID(), "S", this.m_C_OrderLine_ID, false);
        if ("S".equals(costingMethod)) {
            if (this.m_matchPO.getReversal_ID() > 0) {
                FactLine cr = fact.createLine((DocLine)null, this.m_pc.getAccount(5, as), as.getC_Currency_ID(), Env.ONE);
                if (!cr.updateReverseLine(473, this.m_matchPO.getM_MatchPO_ID(), 0, Env.ONE)) {
                    fact.remove(cr);
                    cr = null;
                }
                if (cr != null) {
                    final FactLine dr = fact.createLine((DocLine)null, this.getAccount(101, as), as.getC_Currency_ID(), Env.ONE);
                    if (!dr.updateReverseLine(473, this.m_matchPO.getM_MatchPO_ID(), 0, Env.ONE)) {
                        this.p_Error = "Failed to create reversal entry for ACCTTYPE_PPVOffset";
                        return null;
                    }
                }
            }
            else {
                if (costs == null || costs.signum() == 0) {
                    if (this.m_oLine.getPriceActual().signum() != 0) {
                        this.p_Error = "Resubmit - No Costs for " + product.getName();
                        this.log.log(Level.SEVERE, this.p_Error);
                        return null;
                    }
                    costs = BigDecimal.ZERO;
                }
                final BigDecimal difference = totalCost.subtract(costs);
                if (difference.signum() == 0) {
                    if (this.log.isLoggable(Level.FINE)) {
                        this.log.log(Level.FINE, "No Cost Difference for M_Product_ID=" + this.getM_Product_ID());
                    }
                    return facts;
                }
                final FactLine cr2 = fact.createLine((DocLine)null, this.m_pc.getAccount(5, as), as.getC_Currency_ID(), isReturnTrx ? difference.negate() : difference);
                MAccount acct_cr = null;
                if (cr2 != null) {
                    cr2.setQty(isReturnTrx ? this.getQty().negate() : this.getQty());
                    cr2.setC_BPartner_ID(this.m_oLine.getC_BPartner_ID());
                    cr2.setC_Activity_ID(this.m_oLine.getC_Activity_ID());
                    cr2.setC_Campaign_ID(this.m_oLine.getC_Campaign_ID());
                    cr2.setC_Project_ID(this.m_oLine.getC_Project_ID());
                    cr2.setC_ProjectPhase_ID(this.m_oLine.getC_ProjectPhase_ID());
                    cr2.setC_ProjectTask_ID(this.m_oLine.getC_ProjectTask_ID());
                    cr2.setC_UOM_ID(this.m_oLine.getC_UOM_ID());
                    cr2.setUser1_ID(this.m_oLine.getUser1_ID());
                    cr2.setUser2_ID(this.m_oLine.getUser2_ID());
                    acct_cr = cr2.getAccount();
                }
                final FactLine dr2 = fact.createLine((DocLine)null, this.getAccount(101, as), as.getC_Currency_ID(), isReturnTrx ? difference : difference.negate());
                MAccount acct_db = null;
                if (dr2 != null) {
                    dr2.setQty(isReturnTrx ? this.getQty() : this.getQty().negate());
                    dr2.setC_BPartner_ID(this.m_oLine.getC_BPartner_ID());
                    dr2.setC_Activity_ID(this.m_oLine.getC_Activity_ID());
                    dr2.setC_Campaign_ID(this.m_oLine.getC_Campaign_ID());
                    dr2.setC_Project_ID(this.m_oLine.getC_Project_ID());
                    dr2.setC_ProjectPhase_ID(this.m_oLine.getC_ProjectPhase_ID());
                    dr2.setC_ProjectTask_ID(this.m_oLine.getC_ProjectTask_ID());
                    dr2.setC_UOM_ID(this.m_oLine.getC_UOM_ID());
                    dr2.setUser1_ID(this.m_oLine.getUser1_ID());
                    dr2.setUser2_ID(this.m_oLine.getUser2_ID());
                    acct_db = dr2.getAccount();
                }
                if (!as.isPostIfClearingEqual() && acct_db != null && acct_db.equals((Object)acct_cr) && !isInterOrg) {
                    final BigDecimal debit = dr2.getAmtSourceDr();
                    final BigDecimal credit = cr2.getAmtSourceCr();
                    if (debit.compareTo(credit) == 0) {
                        fact.remove(dr2);
                        fact.remove(cr2);
                    }
                }
            }
            facts.add(fact);
            return facts;
        }
        return facts;
    }
    
    private boolean isInterOrg(final MAcctSchema as) {
        final MAcctSchemaElement elementorg = as.getAcctSchemaElement("OO");
        return elementorg != null && elementorg.isBalanced() && (this.m_ioLine != null && this.m_oLine != null && this.m_ioLine.getAD_Org_ID() != this.m_oLine.getAD_Org_ID());
    }
    
    private String createMatchPOCostDetail(final MAcctSchema as, BigDecimal poCost, final Map<Integer, BigDecimal> landedCostMap) {
        if (this.m_ioLine != null && this.m_ioLine.getM_InOutLine_ID() > 0 && this.m_oLine != null && this.m_oLine.getC_OrderLine_ID() > 0) {
            final MMatchPO mMatchPO = (MMatchPO)this.getPO();
            final MInOut inOut = this.m_ioLine.getParent();
            final boolean isReturnTrx = inOut.getMovementType().equals("V-");
            final MMatchPO[] mPO = MMatchPO.getOrderLine(this.getCtx(), this.m_oLine.getC_OrderLine_ID(), this.getTrxName());
            BigDecimal tQty = Env.ZERO;
            BigDecimal tAmt = Env.ZERO;
            for (int i = 0; i < mPO.length; ++i) {
                if (mPO[i].getM_AttributeSetInstance_ID() == mMatchPO.getM_AttributeSetInstance_ID() && mPO[i].getM_MatchPO_ID() != mMatchPO.getM_MatchPO_ID()) {
                    final BigDecimal qty = isReturnTrx ? mPO[i].getQty().negate() : mPO[i].getQty();
                    tQty = tQty.add(qty);
                    tAmt = tAmt.add(poCost.multiply(qty));
                }
            }
            poCost = poCost.multiply(this.getQty());
            tAmt = tAmt.add(isReturnTrx ? poCost.negate() : poCost);
            tQty = tQty.add(isReturnTrx ? this.getQty().negate() : this.getQty());
            if (mMatchPO.getReversal_ID() > 0) {
                final String error = this.createLandedCostAdjustments(as, landedCostMap, mMatchPO, tQty);
                if (!Util.isEmpty(error)) {
                    return error;
                }
            }
            if (tAmt.scale() > as.getCostingPrecision()) {
                tAmt = tAmt.setScale(as.getCostingPrecision(), 4);
            }
            if (!MCostDetail.createOrder(as, this.m_oLine.getAD_Org_ID(), this.getM_Product_ID(), mMatchPO.getM_AttributeSetInstance_ID(), this.m_oLine.getC_OrderLine_ID(), 0, tAmt, tQty, this.m_oLine.getDescription(), this.getTrxName())) {
                return "SaveError";
            }
            if (mMatchPO.getReversal_ID() <= 0) {
                final String error = this.createLandedCostAdjustments(as, landedCostMap, mMatchPO, tQty);
                if (!Util.isEmpty(error)) {
                    return error;
                }
            }
        }
        return "";
    }
    
    private String createLandedCostAdjustments(final MAcctSchema as, final Map<Integer, BigDecimal> landedCostMap, final MMatchPO mMatchPO, final BigDecimal tQty) {
        for (final Integer elementId : landedCostMap.keySet()) {
            BigDecimal amt = landedCostMap.get(elementId);
            amt = amt.multiply(tQty);
            if (amt.scale() > as.getCostingPrecision()) {
                amt = amt.setScale(as.getCostingPrecision(), 4);
            }
            if (!MCostDetail.createOrder(as, this.m_oLine.getAD_Org_ID(), this.getM_Product_ID(), mMatchPO.getM_AttributeSetInstance_ID(), this.m_oLine.getC_OrderLine_ID(), (int)elementId, amt, tQty, this.m_oLine.getDescription(), this.getTrxName())) {
                return "SaveError";
            }
        }
        return null;
    }
}
