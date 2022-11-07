package org.kjs.pola.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AverageCostingNegativeQtyException;
import org.adempiere.exceptions.AverageCostingZeroQtyException;
import org.adempiere.exceptions.DBException;
import org.compiere.Adempiere;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCost;
import org.compiere.model.MCostDetail;
import org.compiere.model.MCostElement;
import org.compiere.model.MCostQueue;
import org.compiere.model.MOrg;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPO;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.model.MUOMConversion;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MCostExt extends MCost
{
    private static final long serialVersionUID = 2850286239121651718L;
    private static CLogger s_log;
    private boolean m_manual;
    
    static {
        MCostExt.s_log = CLogger.getCLogger((Class)MCost.class);
    }
    
    public static BigDecimal getCurrentCost(final MProduct product, int M_AttributeSetInstance_ID, final MAcctSchema as, int AD_Org_ID, String costingMethod, final BigDecimal qty, final int C_OrderLine_ID, final boolean zeroCostsOK, final String trxName) {
        final String CostingLevel = product.getCostingLevel(as);
        if ("C".equals(CostingLevel)) {
            AD_Org_ID = 0;
            M_AttributeSetInstance_ID = 0;
        }
        else if ("O".equals(CostingLevel)) {
            M_AttributeSetInstance_ID = 0;
        }
        else if ("B".equals(CostingLevel)) {
            AD_Org_ID = 0;
        }
        if (costingMethod == null) {
            costingMethod = product.getCostingMethod(as);
            if (costingMethod == null) {
                throw new IllegalArgumentException("No Costing Method");
            }
        }
        MCostDetail.processProduct(product, trxName);
        return getCurrentCost(product, M_AttributeSetInstance_ID, as, AD_Org_ID, as.getM_CostType_ID(), costingMethod, qty, C_OrderLine_ID, zeroCostsOK, trxName);
    }
    
    protected static BigDecimal getCurrentCost(final MProduct product, final int M_ASI_ID, final MAcctSchema as, final int Org_ID, final int M_CostType_ID, final String costingMethod, final BigDecimal qty, final int C_OrderLine_ID, final boolean zeroCostsOK, final String trxName) {
        String costElementType = null;
        BigDecimal percent = null;
        BigDecimal materialCostEach = Env.ZERO;
        BigDecimal otherCostEach = Env.ZERO;
        BigDecimal percentage = Env.ZERO;
        int count = 0;
        final String sql = "SELECT COALESCE(SUM(c.CurrentCostPrice),0), ce.CostElementType, ce.CostingMethod, c.Percent, c.M_CostElement_ID , COALESCE(SUM(c.CurrentCostPriceLL),0)  FROM M_Cost c LEFT OUTER JOIN M_CostElement ce ON (c.M_CostElement_ID=ce.M_CostElement_ID) WHERE c.AD_Client_ID=? AND c.AD_Org_ID=? AND c.M_Product_ID=? AND (c.M_AttributeSetInstance_ID=? OR c.M_AttributeSetInstance_ID=0) AND c.M_CostType_ID=? AND c.C_AcctSchema_ID=? AND (ce.CostingMethod IS NULL OR ce.CostingMethod=?) GROUP BY ce.CostElementType, ce.CostingMethod, c.Percent, c.M_CostElement_ID";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql, trxName);
            pstmt.setInt(1, product.getAD_Client_ID());
            pstmt.setInt(2, Org_ID);
            pstmt.setInt(3, product.getM_Product_ID());
            pstmt.setInt(4, M_ASI_ID);
            pstmt.setInt(5, M_CostType_ID);
            pstmt.setInt(6, as.getC_AcctSchema_ID());
            pstmt.setString(7, costingMethod);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final BigDecimal currentCostPrice = rs.getBigDecimal(1);
                final BigDecimal currentCostPriceLL = rs.getBigDecimal(6);
                costElementType = rs.getString(2);
                final String cm = rs.getString(3);
                percent = rs.getBigDecimal(4);
                if (MCostExt.s_log.isLoggable(Level.FINEST)) {
                    MCostExt.s_log.finest("CurrentCostPrice=" + currentCostPrice + ", CurrentCostPriceLL=" + currentCostPriceLL + ", CostElementType=" + costElementType + ", CostingMethod=" + cm + ", Percent=" + percent);
                }
                if (currentCostPrice != null && currentCostPrice.signum() != 0) {
                    if (cm != null) {
                        materialCostEach = materialCostEach.add(currentCostPrice);
                    }
                    else {
                        otherCostEach = otherCostEach.add(currentCostPrice);
                    }
                }
                if (percent != null && percent.signum() != 0) {
                    percentage = percentage.add(percent);
                }
                ++count;
            }
        }
        catch (SQLException e) {
            throw new DBException(e, sql);
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        if (count > 1 && MCostExt.s_log.isLoggable(Level.FINEST)) {
            MCostExt.s_log.finest("MaterialCost=" + materialCostEach + ", OtherCosts=" + otherCostEach + ", Percentage=" + percentage);
        }
        if (materialCostEach.signum() == 0 && zeroCostsOK) {
            return Env.ZERO;
        }
        BigDecimal materialCost = materialCostEach.multiply(qty);
        if ("S".equals(costingMethod)) {
            if (MCostExt.s_log.isLoggable(Level.FINER)) {
                MCostExt.s_log.finer("MaterialCosts = " + materialCost);
            }
            return materialCost;
        }
        if ("F".equals(costingMethod) || "L".equals(costingMethod)) {
            final MCostElement ce = MCostElement.getMaterialCostElement((PO)as, costingMethod);
            materialCost = MCostQueue.getCosts(product, M_ASI_ID, as, Org_ID, ce, qty, trxName);
        }
        final BigDecimal otherCost = otherCostEach.multiply(qty);
        BigDecimal costs = otherCost.add(materialCost);
        if (costs.signum() == 0) {
            return null;
        }
        if (MCostExt.s_log.isLoggable(Level.FINER)) {
            MCostExt.s_log.finer("Sum Costs = " + costs);
        }
        final int precision = as.getCostingPrecision();
        if (percentage.signum() == 0) {
            if (costs.scale() > precision) {
                costs = costs.setScale(precision, 4);
            }
            return costs;
        }
        BigDecimal percentCost = costs.multiply(percentage);
        percentCost = percentCost.divide(Env.ONEHUNDRED, precision, 4);
        costs = costs.add(percentCost);
        if (costs.scale() > precision) {
            costs = costs.setScale(precision, 4);
        }
        if (MCostExt.s_log.isLoggable(Level.FINER)) {
            MCostExt.s_log.finer("Sum Costs = " + costs + " (Add=" + percentCost + ")");
        }
        return costs;
    }
    
    public static BigDecimal getSeedCosts(final MProduct product, final int M_ASI_ID, final MAcctSchema as, final int Org_ID, final String costingMethod, final int C_OrderLine_ID) {
        BigDecimal retValue = null;
        if ("I".equals(costingMethod)) {
            return null;
        }
        if ("A".equals(costingMethod)) {
            return null;
        }
        if ("F".equals(costingMethod)) {
            return null;
        }
        if ("L".equals(costingMethod)) {
            return null;
        }
        if ("i".equals(costingMethod)) {
            retValue = getLastInvoicePrice(product, M_ASI_ID, Org_ID, as.getC_Currency_ID());
        }
        else if ("p".equals(costingMethod)) {
            if (C_OrderLine_ID != 0) {
                retValue = getPOPrice(product, C_OrderLine_ID, as.getC_Currency_ID());
            }
            if (retValue == null || retValue.signum() == 0) {
                retValue = getLastPOPrice(product, M_ASI_ID, Org_ID, as.getC_Currency_ID());
            }
        }
        else if (!"S".equals(costingMethod)) {
            if (!"U".equals(costingMethod)) {
                throw new IllegalArgumentException("Unknown Costing Method = " + costingMethod);
            }
        }
        if (retValue != null && retValue.signum() > 0) {
            if (MCostExt.s_log.isLoggable(Level.FINE)) {
                MCostExt.s_log.fine(String.valueOf(product.getName()) + ", CostingMethod=" + costingMethod + " - " + retValue);
            }
            return retValue;
        }
        if (C_OrderLine_ID != 0) {
            retValue = getPOPrice(product, C_OrderLine_ID, as.getC_Currency_ID());
            if (retValue != null && retValue.signum() > 0) {
                if (MCostExt.s_log.isLoggable(Level.FINE)) {
                    MCostExt.s_log.fine(String.valueOf(product.getName()) + ", PO - " + retValue);
                }
                return retValue;
            }
        }
        if (!"S".equals(costingMethod)) {
            final MCostElement ce = MCostElement.getMaterialCostElement((PO)as, "S");
            final MCost cost = get(product, M_ASI_ID, as, Org_ID, ce.getM_CostElement_ID(), product.get_TrxName());
            if (cost != null && cost.getCurrentCostPrice().signum() > 0) {
                if (MCostExt.s_log.isLoggable(Level.FINE)) {
                    MCostExt.s_log.fine(String.valueOf(product.getName()) + ", Standard - " + cost);
                }
                return cost.getCurrentCostPrice();
            }
        }
        if ("p".equals(costingMethod) || "S".equals(costingMethod)) {
            retValue = getLastPOPrice(product, M_ASI_ID, Org_ID, as.getC_Currency_ID());
            if (Org_ID != 0 && (retValue == null || retValue.signum() == 0)) {
                retValue = getLastPOPrice(product, M_ASI_ID, 0, as.getC_Currency_ID());
            }
            if (retValue != null && retValue.signum() > 0) {
                if (MCostExt.s_log.isLoggable(Level.FINE)) {
                    MCostExt.s_log.fine(String.valueOf(product.getName()) + ", LastPO = " + retValue);
                }
                return retValue;
            }
        }
        else {
            retValue = getLastInvoicePrice(product, M_ASI_ID, Org_ID, as.getC_Currency_ID());
            if (Org_ID != 0 && (retValue == null || retValue.signum() == 0)) {
                retValue = getLastInvoicePrice(product, M_ASI_ID, 0, as.getC_Currency_ID());
            }
            if (retValue != null && retValue.signum() != 0) {
                if (MCostExt.s_log.isLoggable(Level.FINE)) {
                    MCostExt.s_log.fine(String.valueOf(product.getName()) + ", LastInv = " + retValue);
                }
                return retValue;
            }
        }
        if ("p".equals(costingMethod) || "S".equals(costingMethod)) {
            retValue = getLastInvoicePrice(product, M_ASI_ID, Org_ID, as.getC_Currency_ID());
            if (Org_ID != 0 && (retValue == null || retValue.signum() == 0)) {
                retValue = getLastInvoicePrice(product, M_ASI_ID, 0, as.getC_Currency_ID());
            }
            if (retValue != null && retValue.signum() > 0) {
                if (MCostExt.s_log.isLoggable(Level.FINE)) {
                    MCostExt.s_log.fine(String.valueOf(product.getName()) + ", LastInv = " + retValue);
                }
                return retValue;
            }
        }
        else {
            retValue = getLastPOPrice(product, M_ASI_ID, Org_ID, as.getC_Currency_ID());
            if (Org_ID != 0 && (retValue == null || retValue.signum() == 0)) {
                retValue = getLastPOPrice(product, M_ASI_ID, 0, as.getC_Currency_ID());
            }
            if (retValue != null && retValue.signum() > 0) {
                if (MCostExt.s_log.isLoggable(Level.FINE)) {
                    MCostExt.s_log.fine(String.valueOf(product.getName()) + ", LastPO = " + retValue);
                }
                return retValue;
            }
        }
        final MProductPO[] pos = MProductPO.getOfProduct(product.getCtx(), product.getM_Product_ID(), product.get_TrxName());
        for (int i = 0; i < pos.length; ++i) {
            BigDecimal price = pos[i].getPricePO();
            if (price == null || price.signum() == 0) {
                price = pos[i].getPriceList();
            }
            if (price != null && price.signum() != 0) {
                price = MConversionRate.convert(product.getCtx(), price, pos[i].getC_Currency_ID(), as.getC_Currency_ID(), as.getAD_Client_ID(), Org_ID);
                if (price != null && price.signum() != 0 && pos[i].getC_UOM_ID() != product.getC_UOM_ID()) {
                    price = MUOMConversion.convertProductTo(Env.getCtx(), product.getM_Product_ID(), pos[i].getC_UOM_ID(), price);
                }
                if (price != null && price.signum() != 0) {
                    retValue = price;
                    if (MCostExt.s_log.isLoggable(Level.FINE)) {
                        MCostExt.s_log.fine(String.valueOf(product.getName()) + ", Product_PO = " + retValue);
                    }
                    return retValue;
                }
            }
        }
        final BigDecimal price2 = getSeedCostFromPriceList(product, as, Org_ID);
        if (price2 != null && price2.signum() > 0) {
            retValue = price2;
        }
        if (MCostExt.s_log.isLoggable(Level.FINE)) {
            MCostExt.s_log.fine(String.valueOf(product.getName()) + " = " + retValue);
        }
        return retValue;
    }
    
    protected static BigDecimal getSeedCostFromPriceList(final MProduct product, final MAcctSchema as, final int orgID) {
        final String sql = "SELECT pp.PriceList, pp.PriceStd FROM M_ProductPrice pp INNER JOIN M_PriceList_Version plv ON (pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID AND plv.ValidFrom <= trunc(sysdate)) INNER JOIN M_PriceList pl ON (plv.M_PriceList_ID = pl.M_PriceList_ID AND pl.IsSOPriceList = 'N') WHERE pp.AD_Client_ID = ? AND pp.AD_Org_ID IN (0, ?) AND pp.M_Product_ID = ? AND pp.PriceList > 0 AND pp.IsActive = 'Y'  ORDER BY pp.AD_Org_ID Desc, plv.ValidFrom Desc";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = (PreparedStatement)DB.prepareStatement(sql, product.get_TrxName());
            st.setInt(1, as.getAD_Client_ID());
            st.setInt(2, orgID);
            st.setInt(3, product.getM_Product_ID());
            rs = st.executeQuery();
            if (rs.next()) {
                final BigDecimal priceList = rs.getBigDecimal(1);
                final BigDecimal priceStd = rs.getBigDecimal(2);
                if (priceStd != null && priceStd.signum() > 0) {
                    return priceStd;
                }
                return priceList;
            }
        }
        catch (SQLException e) {
            throw new DBException(e, sql);
        }
        finally {
            DB.close(rs, (Statement)st);
        }
        DB.close(rs, (Statement)st);
        return BigDecimal.ZERO;
    }
    
    public static BigDecimal getLastInvoicePrice(final MProduct product, final int M_ASI_ID, final int AD_Org_ID, final int C_Currency_ID) {
        BigDecimal retValue = null;
        final StringBuilder sql = new StringBuilder("SELECT currencyConvert(il.PriceActual, i.C_Currency_ID, ?, i.DateAcct, i.C_ConversionType_ID, il.AD_Client_ID, il.AD_Org_ID) ").append("FROM C_InvoiceLine il ").append(" INNER JOIN C_Invoice i ON (il.C_Invoice_ID=i.C_Invoice_ID) ").append("WHERE il.M_Product_ID=?").append(" AND i.IsSOTrx='N'");
        if (AD_Org_ID != 0) {
            sql.append(" AND il.AD_Org_ID=?");
        }
        else if (M_ASI_ID != 0) {
            sql.append(" AND il.M_AttributeSetInstance_ID=?");
        }
        sql.append(" ORDER BY i.DateInvoiced DESC, il.Line DESC");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Label_0244: {
            try {
                pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), product.get_TrxName());
                pstmt.setInt(1, C_Currency_ID);
                pstmt.setInt(2, product.getM_Product_ID());
                if (AD_Org_ID != 0) {
                    pstmt.setInt(3, AD_Org_ID);
                }
                else if (M_ASI_ID != 0) {
                    pstmt.setInt(3, M_ASI_ID);
                }
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    retValue = rs.getBigDecimal(1);
                }
            }
            catch (Exception e) {
                MCostExt.s_log.log(Level.SEVERE, sql.toString(), (Throwable)e);
                break Label_0244;
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
        if (retValue != null) {
            if (MCostExt.s_log.isLoggable(Level.FINER)) {
                MCostExt.s_log.finer(String.valueOf(product.getName()) + " = " + retValue);
            }
            return retValue;
        }
        return null;
    }
    
    public static BigDecimal getLastPOPrice(final MProduct product, final int M_ASI_ID, final int AD_Org_ID, final int C_Currency_ID) {
        BigDecimal retValue = null;
        final StringBuilder sql = new StringBuilder("SELECT currencyConvert(ol.PriceCost, o.C_Currency_ID, ?, o.DateAcct, o.C_ConversionType_ID, ol.AD_Client_ID, ol.AD_Org_ID),").append(" currencyConvert(ol.PriceActual, o.C_Currency_ID, ?, o.DateAcct, o.C_ConversionType_ID, ol.AD_Client_ID, ol.AD_Org_ID) ").append("FROM C_OrderLine ol").append(" INNER JOIN C_Order o ON (ol.C_Order_ID=o.C_Order_ID) ").append("WHERE ol.M_Product_ID=?").append(" AND o.IsSOTrx='N'");
        if (AD_Org_ID != 0) {
            sql.append(" AND ol.AD_Org_ID=?");
        }
        else if (M_ASI_ID != 0) {
            sql.append(" AND ol.M_AttributeSetInstance_ID=?");
        }
        sql.append(" ORDER BY o.DateOrdered DESC, ol.Line DESC");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), product.get_TrxName());
            pstmt.setInt(1, C_Currency_ID);
            pstmt.setInt(2, C_Currency_ID);
            pstmt.setInt(3, product.getM_Product_ID());
            if (AD_Org_ID != 0) {
                pstmt.setInt(4, AD_Org_ID);
            }
            else if (M_ASI_ID != 0) {
                pstmt.setInt(4, M_ASI_ID);
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                retValue = rs.getBigDecimal(1);
                if (retValue == null || retValue.signum() == 0) {
                    retValue = rs.getBigDecimal(2);
                }
            }
        }
        catch (SQLException e) {
            throw new DBException(e, sql.toString());
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        if (retValue != null) {
            if (MCostExt.s_log.isLoggable(Level.FINER)) {
                MCostExt.s_log.finer(String.valueOf(product.getName()) + " = " + retValue);
            }
            return retValue;
        }
        return null;
    }
    
    public static BigDecimal getPOPrice(final MProduct product, final int C_OrderLine_ID, final int C_Currency_ID) {
        BigDecimal retValue = null;
        final String sql = "SELECT currencyConvert(ol.PriceCost, o.C_Currency_ID, ?, o.DateAcct, o.C_ConversionType_ID, ol.AD_Client_ID, ol.AD_Org_ID), currencyConvert(ol.PriceActual, o.C_Currency_ID, ?, o.DateAcct, o.C_ConversionType_ID, ol.AD_Client_ID, ol.AD_Org_ID) FROM C_OrderLine ol INNER JOIN C_Order o ON (ol.C_Order_ID=o.C_Order_ID) WHERE ol.C_OrderLine_ID=? AND o.IsSOTrx='N'";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Label_0164: {
            try {
                pstmt = (PreparedStatement)DB.prepareStatement(sql, product.get_TrxName());
                pstmt.setInt(1, C_Currency_ID);
                pstmt.setInt(2, C_Currency_ID);
                pstmt.setInt(3, C_OrderLine_ID);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    retValue = rs.getBigDecimal(1);
                    if (retValue == null || retValue.signum() == 0) {
                        retValue = rs.getBigDecimal(2);
                    }
                }
            }
            catch (Exception e) {
                MCostExt.s_log.log(Level.SEVERE, sql, (Throwable)e);
                break Label_0164;
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
        if (retValue != null) {
            if (MCostExt.s_log.isLoggable(Level.FINER)) {
                MCostExt.s_log.finer(String.valueOf(product.getName()) + " = " + retValue);
            }
            return retValue;
        }
        return null;
    }
    
    public static void create(final MClient client) {
        final MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(client.getCtx(), client.getAD_Client_ID());
        String trxNameUsed;
        final String trxName = trxNameUsed = client.get_TrxName();
        Trx trx = null;
        if (trxName == null) {
            trxNameUsed = Trx.createTrxName("Cost");
            trx = Trx.get(trxNameUsed, true);
        }
        boolean success = true;
        final String sql = "SELECT * FROM M_Product p WHERE AD_Client_ID=? AND EXISTS (SELECT * FROM M_CostDetail cd WHERE p.M_Product_ID=cd.M_Product_ID AND Processed='N')";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Label_0263: {
            try {
                pstmt = (PreparedStatement)DB.prepareStatement(sql, trxNameUsed);
                pstmt.setInt(1, client.getAD_Client_ID());
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    final MProduct product = new MProduct(client.getCtx(), rs, trxNameUsed);
                    for (int i = 0; i < ass.length; ++i) {
                        final BigDecimal cost = getCurrentCost(product, 0, ass[i], 0, null, Env.ONE, 0, false, trxNameUsed);
                        if (MCostExt.s_log.isLoggable(Level.INFO)) {
                            MCostExt.s_log.info(String.valueOf(product.getName()) + " = " + cost);
                        }
                    }
                }
            }
            catch (Exception e) {
                MCostExt.s_log.log(Level.SEVERE, sql, (Throwable)e);
                success = false;
                break Label_0263;
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
        if (trx != null) {
            if (success) {
                trx.commit();
            }
            else {
                trx.rollback();
            }
            trx.close();
        }
    }
    
    protected static void create(final MProduct product) {
        MCostExt.s_log.config(product.getName());
        final MCostElement[] ces = MCostElement.getCostingMethods((PO)product);
        MCostElement ce = null;
        MCostElement[] array;
        for (int length = (array = ces).length, i = 0; i < length; ++i) {
            final MCostElement element = array[i];
            if ("S".equals(element.getCostingMethod())) {
                ce = element;
                break;
            }
        }
        if (ce == null) {
            MCostExt.s_log.fine("No Standard Costing in System");
            return;
        }
        final MAcctSchema[] mass = MAcctSchema.getClientAcctSchema(product.getCtx(), product.getAD_Client_ID(), product.get_TrxName());
        MOrg[] orgs = null;
        final int M_ASI_ID = 0;
        MAcctSchema[] array2;
        for (int length2 = (array2 = mass).length, j = 0; j < length2; ++j) {
            final MAcctSchema as = array2[j];
            final String cl = product.getCostingLevel(as);
            if ("C".equals(cl)) {
                createCostingRecord(product, M_ASI_ID, as, 0, ce.getM_CostElement_ID());
            }
            else if ("O".equals(cl)) {
                if (as.getAD_OrgOnly_ID() > 0 && MOrg.get(product.getCtx(), as.getAD_OrgOnly_ID()).isSummary()) {
                    final MClient client = MClient.get(product.getCtx(), product.getAD_Client_ID());
                    final MClientInfo ci = client.getInfo();
                    final MTree vTree = new MTree(product.getCtx(), ci.getAD_Tree_Org_ID(), false, true, true, product.get_TrxName());
                    final MTreeNode root = vTree.getRoot();
                    createForChildOrg(root, product, as, M_ASI_ID, ce, false);
                }
                else {
                    if (orgs == null) {
                        orgs = MOrg.getOfClient((PO)product);
                    }
                    MOrg[] array3;
                    for (int length3 = (array3 = orgs).length, k = 0; k < length3; ++k) {
                        final MOrg o = array3[k];
                        if (!o.isSummary()) {
                            if (as.getAD_OrgOnly_ID() == o.getAD_Org_ID() || as.getAD_OrgOnly_ID() == 0) {
                                createCostingRecord(product, M_ASI_ID, as, o.getAD_Org_ID(), ce.getM_CostElement_ID());
                            }
                        }
                    }
                }
            }
            else {
                MCostExt.s_log.warning("Not created: Std.Cost for " + product.getName() + " - Costing Level on Batch/Lot");
            }
        }
    }
    
    private static void createForChildOrg(final MTreeNode root, final MProduct product, final MAcctSchema as, final int M_ASI_ID, final MCostElement ce, boolean found) {
        final int parentId = root.getNode_ID();
        if (!found) {
            found = (parentId == as.getAD_OrgOnly_ID());
        }
        final Enumeration<?> nodeEnum = (Enumeration<?>)root.children();
        MTreeNode child = null;
        while (nodeEnum.hasMoreElements()) {
            child = (MTreeNode)nodeEnum.nextElement();
            if (child != null && child.getChildCount() > 0) {
                createForChildOrg(child, product, as, M_ASI_ID, ce, found);
            }
            else {
                if (!found) {
                    continue;
                }
                final int orgId = child.getNode_ID();
                final MOrg org = MOrg.get(product.getCtx(), orgId);
                if (org.isSummary()) {
                    continue;
                }
                createCostingRecord(product, M_ASI_ID, as, orgId, ce.getM_CostElement_ID());
            }
        }
    }
    
    private static void createCostingRecord(final MProduct product, final int M_ASI_ID, final MAcctSchema as, final int AD_Org_ID, final int M_CostElement_ID) {
        final MCost cost = MCost.get(product, M_ASI_ID, as, AD_Org_ID, M_CostElement_ID, product.get_TrxName());
        final int AverageCostElementID = 1000004;
        final MCost cost2 = MCost.get(product, M_ASI_ID, as, AD_Org_ID, AverageCostElementID, product.get_TrxName());
        if (cost.is_new()) {
            if (cost.save()) {
                if (MCostExt.s_log.isLoggable(Level.CONFIG)) {
                    MCostExt.s_log.config("Std.Cost for " + product.getName() + " - " + as.getName());
                }
            }
            else {
                MCostExt.s_log.warning("Not created: Std.Cost for " + product.getName() + " - " + as.getName());
            }
        }
        if (cost2.is_new()) {
            if (cost2.save()) {
                if (MCostExt.s_log.isLoggable(Level.CONFIG)) {
                    MCostExt.s_log.config("Std.Cost for " + product.getName() + " - " + as.getName());
                }
            }
            else {
                MCostExt.s_log.warning("Not created: Std.Cost for " + product.getName() + " - " + as.getName());
            }
        }
    }
    
    protected static void delete(final MProduct product) {
        MCostExt.s_log.config(product.getName());
        final List<MCostElement> ces = (List<MCostElement>)MCostElement.getCostElementsWithCostingMethods((PO)product);
        final MAcctSchema[] mass = MAcctSchema.getClientAcctSchema(product.getCtx(), product.getAD_Client_ID(), product.get_TrxName());
        MOrg[] orgs = null;
        final int M_ASI_ID = 0;
        MAcctSchema[] array;
        for (int length = (array = mass).length, i = 0; i < length; ++i) {
            final MAcctSchema as = array[i];
            final String cl = product.getCostingLevel(as);
            if ("C".equals(cl)) {
                for (final MCostElement ce : ces) {
                    final MCost cost = MCost.get(product, M_ASI_ID, as, 0, ce.getM_CostElement_ID(), product.get_TrxName());
                    if (cost != null) {
                        cost.deleteEx(true);
                    }
                }
            }
            else if ("O".equals(cl)) {
                if (orgs == null) {
                    orgs = MOrg.getOfClient((PO)product);
                }
                MOrg[] array2;
                for (int length2 = (array2 = orgs).length, j = 0; j < length2; ++j) {
                    final MOrg o = array2[j];
                    for (final MCostElement ce2 : ces) {
                        final MCost cost2 = MCost.get(product, M_ASI_ID, as, o.getAD_Org_ID(), ce2.getM_CostElement_ID(), product.get_TrxName());
                        if (cost2 != null) {
                            cost2.deleteEx(true);
                        }
                    }
                }
            }
            else {
                MCostExt.s_log.warning("Not created: Std.Cost for " + product.getName() + " - Costing Level on Batch/Lot");
            }
        }
    }
    
    public static BigDecimal calculateAverageInv(final MProduct product, final int M_AttributeSetInstance_ID, final MAcctSchema as, final int AD_Org_ID) {
        final StringBuilder sql = new StringBuilder("SELECT t.MovementQty, mi.Qty, il.QtyInvoiced, il.PriceActual,").append(" i.C_Currency_ID, i.DateAcct, i.C_ConversionType_ID, i.AD_Client_ID, i.AD_Org_ID, t.M_Transaction_ID ").append("FROM M_Transaction t").append(" INNER JOIN M_MatchInv mi ON (t.M_InOutLine_ID=mi.M_InOutLine_ID)").append(" INNER JOIN C_InvoiceLine il ON (mi.C_InvoiceLine_ID=il.C_InvoiceLine_ID)").append(" INNER JOIN C_Invoice i ON (il.C_Invoice_ID=i.C_Invoice_ID) ").append("WHERE t.M_Product_ID=?");
        if (AD_Org_ID != 0) {
            sql.append(" AND t.AD_Org_ID=?");
        }
        else if (M_AttributeSetInstance_ID != 0) {
            sql.append(" AND t.M_AttributeSetInstance_ID=?");
        }
        sql.append(" ORDER BY t.M_Transaction_ID");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal newStockQty = Env.ZERO;
        BigDecimal newAverageAmt = Env.ZERO;
        final int oldTransaction_ID = 0;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, product.getM_Product_ID());
            if (AD_Org_ID != 0) {
                pstmt.setInt(2, AD_Org_ID);
            }
            else if (M_AttributeSetInstance_ID != 0) {
                pstmt.setInt(2, M_AttributeSetInstance_ID);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final BigDecimal oldStockQty = newStockQty;
                final BigDecimal movementQty = rs.getBigDecimal(1);
                int M_Transaction_ID = rs.getInt(10);
                if (M_Transaction_ID != oldTransaction_ID) {
                    newStockQty = oldStockQty.add(movementQty);
                }
                M_Transaction_ID = oldTransaction_ID;
                final BigDecimal matchQty = rs.getBigDecimal(2);
                if (matchQty == null) {
                    if (!MCostExt.s_log.isLoggable(Level.FINER)) {
                        continue;
                    }
                    MCostExt.s_log.finer("Movement=" + movementQty + ", StockQty=" + newStockQty);
                }
                else {
                    final BigDecimal price = rs.getBigDecimal(4);
                    final int C_Currency_ID = rs.getInt(5);
                    final Timestamp DateAcct = rs.getTimestamp(6);
                    final int C_ConversionType_ID = rs.getInt(7);
                    final int Client_ID = rs.getInt(8);
                    final int Org_ID = rs.getInt(9);
                    final BigDecimal cost = MConversionRate.convert(product.getCtx(), price, C_Currency_ID, as.getC_Currency_ID(), DateAcct, C_ConversionType_ID, Client_ID, Org_ID);
                    final BigDecimal oldAverageAmt = newAverageAmt;
                    final BigDecimal averageCurrent = oldStockQty.multiply(oldAverageAmt);
                    final BigDecimal averageIncrease = matchQty.multiply(cost);
                    BigDecimal newAmt = averageCurrent.add(averageIncrease);
                    newAmt = newAmt.setScale(as.getCostingPrecision(), 4);
                    newAverageAmt = newAmt.divide(newStockQty, as.getCostingPrecision(), 4);
                    if (!MCostExt.s_log.isLoggable(Level.FINER)) {
                        continue;
                    }
                    MCostExt.s_log.finer("Movement=" + movementQty + ", StockQty=" + newStockQty + ", Match=" + matchQty + ", Cost=" + cost + ", NewAvg=" + newAverageAmt);
                }
            }
        }
        catch (SQLException e) {
            throw new DBException(e, sql.toString());
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        if (newAverageAmt != null && newAverageAmt.signum() != 0) {
            if (MCostExt.s_log.isLoggable(Level.FINER)) {
                MCostExt.s_log.finer(String.valueOf(product.getName()) + " = " + newAverageAmt);
            }
            return newAverageAmt;
        }
        return null;
    }
    
    public static BigDecimal calculateAveragePO(final MProduct product, final int M_AttributeSetInstance_ID, final MAcctSchema as, final int AD_Org_ID) {
        final StringBuilder sql = new StringBuilder("SELECT t.MovementQty, mp.Qty, ol.QtyOrdered, ol.PriceCost, ol.PriceActual,").append(" o.C_Currency_ID, o.DateAcct, o.C_ConversionType_ID,").append(" o.AD_Client_ID, o.AD_Org_ID, t.M_Transaction_ID ").append("FROM M_Transaction t").append(" INNER JOIN M_MatchPO mp ON (t.M_InOutLine_ID=mp.M_InOutLine_ID)").append(" INNER JOIN C_OrderLine ol ON (mp.C_OrderLine_ID=ol.C_OrderLine_ID)").append(" INNER JOIN C_Order o ON (ol.C_Order_ID=o.C_Order_ID) ").append("WHERE t.M_Product_ID=?");
        if (AD_Org_ID != 0) {
            sql.append(" AND t.AD_Org_ID=?");
        }
        else if (M_AttributeSetInstance_ID != 0) {
            sql.append(" AND t.M_AttributeSetInstance_ID=?");
        }
        sql.append(" ORDER BY t.M_Transaction_ID");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal newStockQty = Env.ZERO;
        BigDecimal newAverageAmt = Env.ZERO;
        final int oldTransaction_ID = 0;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, product.getM_Product_ID());
            if (AD_Org_ID != 0) {
                pstmt.setInt(2, AD_Org_ID);
            }
            else if (M_AttributeSetInstance_ID != 0) {
                pstmt.setInt(2, M_AttributeSetInstance_ID);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final BigDecimal oldStockQty = newStockQty;
                final BigDecimal movementQty = rs.getBigDecimal(1);
                int M_Transaction_ID = rs.getInt(11);
                if (M_Transaction_ID != oldTransaction_ID) {
                    newStockQty = oldStockQty.add(movementQty);
                }
                M_Transaction_ID = oldTransaction_ID;
                final BigDecimal matchQty = rs.getBigDecimal(2);
                if (matchQty == null) {
                    if (!MCostExt.s_log.isLoggable(Level.FINER)) {
                        continue;
                    }
                    MCostExt.s_log.finer("Movement=" + movementQty + ", StockQty=" + newStockQty);
                }
                else {
                    BigDecimal price = rs.getBigDecimal(4);
                    if (price == null || price.signum() == 0) {
                        price = rs.getBigDecimal(5);
                    }
                    final int C_Currency_ID = rs.getInt(6);
                    final Timestamp DateAcct = rs.getTimestamp(7);
                    final int C_ConversionType_ID = rs.getInt(8);
                    final int Client_ID = rs.getInt(9);
                    final int Org_ID = rs.getInt(10);
                    final BigDecimal cost = MConversionRate.convert(product.getCtx(), price, C_Currency_ID, as.getC_Currency_ID(), DateAcct, C_ConversionType_ID, Client_ID, Org_ID);
                    final BigDecimal oldAverageAmt = newAverageAmt;
                    final BigDecimal averageCurrent = oldStockQty.multiply(oldAverageAmt);
                    final BigDecimal averageIncrease = matchQty.multiply(cost);
                    BigDecimal newAmt = averageCurrent.add(averageIncrease);
                    newAmt = newAmt.setScale(as.getCostingPrecision(), 4);
                    newAverageAmt = newAmt.divide(newStockQty, as.getCostingPrecision(), 4);
                    if (!MCostExt.s_log.isLoggable(Level.FINER)) {
                        continue;
                    }
                    MCostExt.s_log.finer("Movement=" + movementQty + ", StockQty=" + newStockQty + ", Match=" + matchQty + ", Cost=" + cost + ", NewAvg=" + newAverageAmt);
                }
            }
        }
        catch (SQLException e) {
            throw new DBException(e, sql.toString());
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        if (newAverageAmt != null && newAverageAmt.signum() != 0) {
            if (MCostExt.s_log.isLoggable(Level.FINER)) {
                MCostExt.s_log.finer(String.valueOf(product.getName()) + " = " + newAverageAmt);
            }
            return newAverageAmt;
        }
        return null;
    }
    
    public static BigDecimal calculateFiFo(final MProduct product, final int M_AttributeSetInstance_ID, final MAcctSchema as, final int AD_Org_ID) {
        final StringBuilder sql = new StringBuilder("SELECT t.MovementQty, mi.Qty, il.QtyInvoiced, il.PriceActual,").append(" i.C_Currency_ID, i.DateAcct, i.C_ConversionType_ID, i.AD_Client_ID, i.AD_Org_ID, t.M_Transaction_ID ").append("FROM M_Transaction t").append(" INNER JOIN M_MatchInv mi ON (t.M_InOutLine_ID=mi.M_InOutLine_ID)").append(" INNER JOIN C_InvoiceLine il ON (mi.C_InvoiceLine_ID=il.C_InvoiceLine_ID)").append(" INNER JOIN C_Invoice i ON (il.C_Invoice_ID=i.C_Invoice_ID) ").append("WHERE t.M_Product_ID=?");
        if (AD_Org_ID != 0) {
            sql.append(" AND t.AD_Org_ID=?");
        }
        else if (M_AttributeSetInstance_ID != 0) {
            sql.append(" AND t.M_AttributeSetInstance_ID=?");
        }
        sql.append(" ORDER BY t.M_Transaction_ID");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final int oldTransaction_ID = 0;
        final ArrayList<QtyCost> fifo = new ArrayList<QtyCost>();
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, product.getM_Product_ID());
            if (AD_Org_ID != 0) {
                pstmt.setInt(2, AD_Org_ID);
            }
            else if (M_AttributeSetInstance_ID != 0) {
                pstmt.setInt(2, M_AttributeSetInstance_ID);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final BigDecimal movementQty = rs.getBigDecimal(1);
                int M_Transaction_ID = rs.getInt(10);
                if (M_Transaction_ID == oldTransaction_ID) {
                    continue;
                }
                M_Transaction_ID = oldTransaction_ID;
                final BigDecimal matchQty = rs.getBigDecimal(2);
                if (matchQty == null) {
                    if (fifo.size() > 0) {
                        QtyCost pp = fifo.get(0);
                        pp.Qty = pp.Qty.add(movementQty);
                        BigDecimal remainder = pp.Qty;
                        if (remainder.signum() == 0) {
                            fifo.remove(0);
                        }
                        else {
                            while (remainder.signum() != 0) {
                                if (fifo.size() == 1) {
                                    pp.Cost = Env.ZERO;
                                    remainder = Env.ZERO;
                                }
                                else {
                                    fifo.remove(0);
                                    pp = fifo.get(0);
                                    pp.Qty = pp.Qty.add(movementQty);
                                    remainder = pp.Qty;
                                }
                            }
                        }
                    }
                    else {
                        final QtyCost pp = new QtyCost(movementQty, Env.ZERO);
                        fifo.add(pp);
                    }
                    if (!MCostExt.s_log.isLoggable(Level.FINER)) {
                        continue;
                    }
                    MCostExt.s_log.finer("Movement=" + movementQty + ", Size=" + fifo.size());
                }
                else {
                    final BigDecimal price = rs.getBigDecimal(4);
                    final int C_Currency_ID = rs.getInt(5);
                    final Timestamp DateAcct = rs.getTimestamp(6);
                    final int C_ConversionType_ID = rs.getInt(7);
                    final int Client_ID = rs.getInt(8);
                    final int Org_ID = rs.getInt(9);
                    final BigDecimal cost = MConversionRate.convert(product.getCtx(), price, C_Currency_ID, as.getC_Currency_ID(), DateAcct, C_ConversionType_ID, Client_ID, Org_ID);
                    boolean used = false;
                    if (fifo.size() == 1) {
                        final QtyCost pp2 = fifo.get(0);
                        if (pp2.Qty.signum() < 0) {
                            pp2.Qty = pp2.Qty.add(movementQty);
                            if (pp2.Qty.signum() == 0) {
                                fifo.remove(0);
                            }
                            else {
                                pp2.Cost = cost;
                            }
                            used = true;
                        }
                    }
                    if (!used) {
                        final QtyCost pp2 = new QtyCost(movementQty, cost);
                        fifo.add(pp2);
                    }
                    if (!MCostExt.s_log.isLoggable(Level.FINER)) {
                        continue;
                    }
                    MCostExt.s_log.finer("Movement=" + movementQty + ", Size=" + fifo.size());
                }
            }
        }
        catch (SQLException e) {
            throw new DBException(e, sql.toString());
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        if (fifo.size() == 0) {
            return null;
        }
        final QtyCost pp3 = fifo.get(0);
        if (MCostExt.s_log.isLoggable(Level.FINER)) {
            MCostExt.s_log.finer(String.valueOf(product.getName()) + " = " + pp3.Cost);
        }
        return pp3.Cost;
    }
    
    public static BigDecimal calculateLiFo(final MProduct product, final int M_AttributeSetInstance_ID, final MAcctSchema as, final int AD_Org_ID) {
        final StringBuilder sql = new StringBuilder("SELECT t.MovementQty, mi.Qty, il.QtyInvoiced, il.PriceActual,").append(" i.C_Currency_ID, i.DateAcct, i.C_ConversionType_ID, i.AD_Client_ID, i.AD_Org_ID, t.M_Transaction_ID ").append("FROM M_Transaction t").append(" INNER JOIN M_MatchInv mi ON (t.M_InOutLine_ID=mi.M_InOutLine_ID)").append(" INNER JOIN C_InvoiceLine il ON (mi.C_InvoiceLine_ID=il.C_InvoiceLine_ID)").append(" INNER JOIN C_Invoice i ON (il.C_Invoice_ID=i.C_Invoice_ID) ").append("WHERE t.M_Product_ID=?");
        if (AD_Org_ID != 0) {
            sql.append(" AND t.AD_Org_ID=?");
        }
        else if (M_AttributeSetInstance_ID != 0) {
            sql.append(" AND t.M_AttributeSetInstance_ID=?");
        }
        sql.append(" ORDER BY t.M_Transaction_ID DESC");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final int oldTransaction_ID = 0;
        final ArrayList<QtyCost> lifo = new ArrayList<QtyCost>();
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, product.getM_Product_ID());
            if (AD_Org_ID != 0) {
                pstmt.setInt(2, AD_Org_ID);
            }
            else if (M_AttributeSetInstance_ID != 0) {
                pstmt.setInt(2, M_AttributeSetInstance_ID);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final BigDecimal movementQty = rs.getBigDecimal(1);
                int M_Transaction_ID = rs.getInt(10);
                if (M_Transaction_ID == oldTransaction_ID) {
                    continue;
                }
                M_Transaction_ID = oldTransaction_ID;
                final BigDecimal matchQty = rs.getBigDecimal(2);
                if (matchQty == null) {
                    if (lifo.size() > 0) {
                        QtyCost pp = lifo.get(lifo.size() - 1);
                        pp.Qty = pp.Qty.add(movementQty);
                        BigDecimal remainder = pp.Qty;
                        if (remainder.signum() == 0) {
                            lifo.remove(lifo.size() - 1);
                        }
                        else {
                            while (remainder.signum() != 0) {
                                if (lifo.size() == 1) {
                                    pp.Cost = Env.ZERO;
                                    remainder = Env.ZERO;
                                }
                                else {
                                    lifo.remove(lifo.size() - 1);
                                    pp = lifo.get(lifo.size() - 1);
                                    pp.Qty = pp.Qty.add(movementQty);
                                    remainder = pp.Qty;
                                }
                            }
                        }
                    }
                    else {
                        final QtyCost pp = new QtyCost(movementQty, Env.ZERO);
                        lifo.add(pp);
                    }
                    if (!MCostExt.s_log.isLoggable(Level.FINER)) {
                        continue;
                    }
                    MCostExt.s_log.finer("Movement=" + movementQty + ", Size=" + lifo.size());
                }
                else {
                    final BigDecimal price = rs.getBigDecimal(4);
                    final int C_Currency_ID = rs.getInt(5);
                    final Timestamp DateAcct = rs.getTimestamp(6);
                    final int C_ConversionType_ID = rs.getInt(7);
                    final int Client_ID = rs.getInt(8);
                    final int Org_ID = rs.getInt(9);
                    final BigDecimal cost = MConversionRate.convert(product.getCtx(), price, C_Currency_ID, as.getC_Currency_ID(), DateAcct, C_ConversionType_ID, Client_ID, Org_ID);
                    final QtyCost pp2 = new QtyCost(movementQty, cost);
                    lifo.add(pp2);
                    if (!MCostExt.s_log.isLoggable(Level.FINER)) {
                        continue;
                    }
                    MCostExt.s_log.finer("Movement=" + movementQty + ", Size=" + lifo.size());
                }
            }
        }
        catch (SQLException e) {
            throw new DBException(e, sql.toString());
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        if (lifo.size() == 0) {
            return null;
        }
        final QtyCost pp3 = lifo.get(lifo.size() - 1);
        if (MCostExt.s_log.isLoggable(Level.FINER)) {
            MCostExt.s_log.finer(String.valueOf(product.getName()) + " = " + pp3.Cost);
        }
        return pp3.Cost;
    }
    
    public static MCost get(final MProduct product, final int M_AttributeSetInstance_ID, final MAcctSchema as, final int AD_Org_ID, final int M_CostElement_ID, final String trxName) {
        MCost cost = null;
        cost = (MCost)new Query(product.getCtx(), "M_Cost", "AD_Client_ID=? AND AD_Org_ID=? AND M_Product_ID=? AND M_AttributeSetInstance_ID=? AND M_CostType_ID=? AND C_AcctSchema_ID=? AND M_CostElement_ID=?", trxName).setParameters(new Object[] { product.getAD_Client_ID(), AD_Org_ID, product.getM_Product_ID(), M_AttributeSetInstance_ID, as.getM_CostType_ID(), as.getC_AcctSchema_ID(), M_CostElement_ID }).firstOnly();
        if (cost == null) {
            cost = new MCost(product, M_AttributeSetInstance_ID, as, AD_Org_ID, M_CostElement_ID);
        }
        return cost;
    }
    
    @Deprecated
    public static MCost get(final MProduct product, final int M_AttributeSetInstance_ID, final MAcctSchema as, final int AD_Org_ID, final int M_CostElement_ID) {
        return get(product, M_AttributeSetInstance_ID, as, AD_Org_ID, M_CostElement_ID, product.get_TrxName());
    }
    
    public static MCost get(final Properties ctx, final int AD_Client_ID, final int AD_Org_ID, final int M_Product_ID, final int M_CostType_ID, final int C_AcctSchema_ID, final int M_CostElement_ID, final int M_AttributeSetInstance_ID, final String trxName) {
        final Object[] params = { AD_Client_ID, AD_Org_ID, M_Product_ID, M_CostType_ID, C_AcctSchema_ID, M_CostElement_ID, M_AttributeSetInstance_ID };
        return (MCost)new Query(ctx, "M_Cost", "AD_Client_ID=? AND AD_Org_ID=? AND M_Product_ID=? AND M_CostType_ID=? AND C_AcctSchema_ID=? AND M_CostElement_ID=? AND M_AttributeSetInstance_ID=?", trxName).setOnlyActiveRecords(true).setParameters(params).firstOnly();
    }
    
    @Deprecated
    public static MCost get(final Properties ctx, final int AD_Client_ID, final int AD_Org_ID, final int M_Product_ID, final int M_CostType_ID, final int C_AcctSchema_ID, final int M_CostElement_ID, final int M_AttributeSetInstance_ID) {
        return get(ctx, AD_Client_ID, AD_Org_ID, M_Product_ID, M_CostType_ID, C_AcctSchema_ID, M_CostElement_ID, M_AttributeSetInstance_ID, null);
    }
    
    public MCostExt(final Properties ctx, final int ignored, final String trxName) {
        super(ctx, ignored, trxName);
        this.m_manual = true;
        if (ignored == 0) {
            this.setM_AttributeSetInstance_ID(0);
            this.setCurrentCostPrice(Env.ZERO);
            this.setFutureCostPrice(Env.ZERO);
            this.setCurrentQty(Env.ZERO);
            this.setCumulatedAmt(Env.ZERO);
            this.setCumulatedQty(Env.ZERO);
            return;
        }
        throw new IllegalArgumentException("Multi-Key");
    }
    
    public MCostExt(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
        this.m_manual = true;
        this.m_manual = false;
    }
    
    public MCostExt(final MProduct product, final int M_AttributeSetInstance_ID, final MAcctSchema as, final int AD_Org_ID, final int M_CostElement_ID) {
        this(product.getCtx(), 0, product.get_TrxName());
        this.setClientOrg(product.getAD_Client_ID(), AD_Org_ID);
        this.setC_AcctSchema_ID(as.getC_AcctSchema_ID());
        this.setM_CostType_ID(as.getM_CostType_ID());
        this.setM_Product_ID(product.getM_Product_ID());
        this.setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
        this.setM_CostElement_ID(M_CostElement_ID);
        this.m_manual = false;
    }
    
    public void add(final BigDecimal amt, final BigDecimal qty) {
        final MCostElement costElement = (MCostElement)this.getM_CostElement();
        if ((costElement.isAveragePO() || costElement.isAverageInvoice()) && this.getCurrentQty().add(qty).signum() < 0) {
            throw new AverageCostingNegativeQtyException("Product(ID)=" + this.getM_Product_ID() + ", Current Qty=" + this.getCurrentQty() + ", Trx Qty=" + qty + ", CostElement=" + costElement.getName() + ", Schema=" + this.getC_AcctSchema().getName());
        }
        this.setCumulatedAmt(this.getCumulatedAmt().add(amt));
        this.setCumulatedQty(this.getCumulatedQty().add(qty));
        this.setCurrentQty(this.getCurrentQty().add(qty));
    }
    
    public void setWeightedAverage(BigDecimal amt, final BigDecimal qty) {
        if (amt.signum() != 0 && qty.signum() != 0 && amt.signum() != qty.signum()) {
            amt = amt.negate();
        }
        if (qty.signum() == 0 && this.getCurrentQty().signum() <= 0) {
            throw new AverageCostingZeroQtyException("Product(ID)=" + this.getM_Product_ID() + ", Current Qty=" + this.getCurrentQty() + ", Trx Qty=" + qty + ", CostElement=" + this.getM_CostElement().getName() + ", Schema=" + this.getC_AcctSchema().getName());
        }
        if (this.getCurrentQty().add(qty).signum() < 0) {
            throw new AverageCostingNegativeQtyException("Product(ID)=" + this.getM_Product_ID() + ", Current Qty=" + this.getCurrentQty() + ", Trx Qty=" + qty + ", CostElement=" + this.getM_CostElement().getName() + ", Schema=" + this.getC_AcctSchema().getName());
        }
        final BigDecimal sumQty = this.getCurrentQty().add(qty);
        if (sumQty.signum() != 0) {
            final BigDecimal oldSum = this.getCurrentCostPrice().multiply(this.getCurrentQty());
            final BigDecimal oldCost = oldSum.divide(sumQty, 12, 4);
            final BigDecimal newCost = amt.divide(sumQty, 12, 4);
            BigDecimal cost = oldCost.add(newCost);
            if (cost.scale() > this.getPrecision() * 2) {
                cost = cost.setScale(this.getPrecision() * 2, 4);
            }
            this.setCurrentCostPrice(cost);
        }
        this.setCumulatedAmt(this.getCumulatedAmt().add(amt));
        this.setCumulatedQty(this.getCumulatedQty().add(qty));
        this.setCurrentQty(this.getCurrentQty().add(qty));
    }
    
    public void setWeightedAverageInitial(final BigDecimal amtUnit) {
        BigDecimal cost = amtUnit;
        if (cost.scale() > this.getPrecision() * 2) {
            cost = cost.setScale(this.getPrecision() * 2, 4);
        }
        this.setCurrentCostPrice(cost);
    }
    
    protected int getPrecision() {
        final MAcctSchema as = MAcctSchema.get(this.getCtx(), this.getC_AcctSchema_ID());
        if (as != null) {
            return as.getCostingPrecision();
        }
        return 6;
    }
    
    public void setCurrentCostPrice(final BigDecimal currentCostPrice) {
        if (currentCostPrice != null) {
            super.setCurrentCostPrice(currentCostPrice);
        }
        else {
            super.setCurrentCostPrice(Env.ZERO);
        }
    }
    
    public BigDecimal getHistoryAverage() {
        BigDecimal retValue = null;
        if (this.getCumulatedQty().signum() != 0 && this.getCumulatedAmt().signum() != 0) {
            retValue = this.getCumulatedAmt().divide(this.getCumulatedQty(), this.getPrecision(), 4);
        }
        return retValue;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder("MCost[");
        sb.append("AD_Client_ID=").append(this.getAD_Client_ID());
        if (this.getAD_Org_ID() != 0) {
            sb.append(",AD_Org_ID=").append(this.getAD_Org_ID());
        }
        sb.append(",M_Product_ID=").append(this.getM_Product_ID());
        if (this.getM_AttributeSetInstance_ID() != 0) {
            sb.append(",AD_ASI_ID=").append(this.getM_AttributeSetInstance_ID());
        }
        sb.append(",M_CostElement_ID=").append(this.getM_CostElement_ID());
        sb.append(", CurrentCost=").append(this.getCurrentCostPrice()).append(", C.Amt=").append(this.getCumulatedAmt()).append(",C.Qty=").append(this.getCumulatedQty()).append("]");
        return sb.toString();
    }
    
    public MCostElement getCostElement() {
        final int M_CostElement_ID = this.getM_CostElement_ID();
        if (M_CostElement_ID == 0) {
            return null;
        }
        return MCostElement.get(this.getCtx(), M_CostElement_ID);
    }
    
    protected boolean beforeSave(final boolean newRecord) {
        final MCostElement ce = (MCostElement)this.getM_CostElement();
        if (this.m_manual) {
            final MAcctSchema as = new MAcctSchema(this.getCtx(), this.getC_AcctSchema_ID(), (String)null);
            final MProduct product = MProduct.get(this.getCtx(), this.getM_Product_ID());
            final String CostingLevel = product.getCostingLevel(as);
            if ("C".equals(CostingLevel)) {
                if (this.getAD_Org_ID() != 0 || this.getM_AttributeSetInstance_ID() != 0) {
                    this.log.saveError("CostingLevelClient", "");
                    return false;
                }
            }
            else if ("B".equals(CostingLevel)) {
                if (this.getM_AttributeSetInstance_ID() == 0 && ce.isCostingMethod()) {
                    this.log.saveError("FillMandatory", Msg.getElement(this.getCtx(), "M_AttributeSetInstance_ID"));
                    return false;
                }
                if (this.getAD_Org_ID() != 0) {
                    this.setAD_Org_ID(0);
                }
            }
        }
        if (this.m_manual && ce != null && ce.isCalculated()) {
            this.log.saveError("Error", Msg.getElement(this.getCtx(), "IsCalculated"));
            return false;
        }
        if (ce != null && (ce.isCalculated() || ("M".equals(ce.getCostElementType()) && this.getPercent() != 0))) {
            this.setPercent(0);
        }
        if (this.getPercent() != 0) {
            if (this.getCurrentCostPrice().signum() != 0) {
                this.setCurrentCostPrice(Env.ZERO);
            }
            if (this.getFutureCostPrice().signum() != 0) {
                this.setFutureCostPrice(Env.ZERO);
            }
            if (this.getCumulatedAmt().signum() != 0) {
                this.setCumulatedAmt(Env.ZERO);
            }
            if (this.getCumulatedQty().signum() != 0) {
                this.setCumulatedQty(Env.ZERO);
            }
        }
        if (ce != null && (ce.isAveragePO() || ce.isAverageInvoice()) && this.is_ValueChanged("CurrentQty") && this.getCurrentQty().signum() < 0) {
            throw new AverageCostingNegativeQtyException("Product(ID)=" + this.getM_Product_ID() + ", Current Qty=" + this.getCurrentQty() + ", CostElement=" + this.getM_CostElement().getName() + ", Schema=" + this.getC_AcctSchema().getName());
        }
        return true;
    }
    
    protected boolean beforeDelete() {
        return true;
    }
    
    public void setCurrentQty(final BigDecimal CurrentQty) {
        final MCostElement ce = (MCostElement)this.getM_CostElement();
        if ((ce.isAveragePO() || ce.isAverageInvoice()) && CurrentQty.signum() < 0) {
            throw new AverageCostingNegativeQtyException("Product=" + this.getM_Product().getName() + ", Current Qty=" + this.getCurrentQty() + ", New Current Qty=" + CurrentQty + ", CostElement=" + ce.getName() + ", Schema=" + this.getC_AcctSchema().getName());
        }
        super.setCurrentQty(CurrentQty);
    }
    
    public static void main(final String[] args) {
        Adempiere.startup(true);
        final MClient client = MClient.get(Env.getCtx(), 11);
        create(client);
    }
    
    public static class QtyCost
    {
        public BigDecimal Qty;
        public BigDecimal Cost;
        
        public QtyCost(final BigDecimal qty, final BigDecimal cost) {
            this.Qty = null;
            this.Cost = null;
            this.Qty = qty;
            this.Cost = cost;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Qty=").append(this.Qty).append(",Cost=").append(this.Cost);
            return sb.toString();
        }
    }
}
