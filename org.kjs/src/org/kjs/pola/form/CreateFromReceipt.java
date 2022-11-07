package org.kjs.pola.form;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.apps.IStatusBar;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLocator;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MRMA;
import org.compiere.model.MRMALine;
import org.compiere.model.MWarehouse;
import org.compiere.model.MWindow;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

public class CreateFromReceipt extends CreateFrom
{
    private MInvoice m_invoice;
    private MRMA m_rma;
    private int defaultLocator_ID;
    
    public CreateFromReceipt(final GridTab gridTab) {
        super(gridTab);
        this.m_invoice = null;
        this.m_rma = null;
        this.defaultLocator_ID = 0;
    }
    
    public boolean dynInit() throws Exception {
        this.log.config("");
        this.setTitle(String.valueOf(Msg.getElement(Env.getCtx(), "M_InOut_ID", false)) + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));
        return true;
    }
    
    protected ArrayList<KeyNamePair> loadRMAData(final int C_BPartner_ID) {
        final ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
        final String sqlStmt = "SELECT r.M_RMA_ID, r.DocumentNo || '-' || r.Amt from M_RMA r WHERE ISSOTRX='Y' AND r.DocStatus in ('CO', 'CL') AND r.C_BPartner_ID=? AND r.M_RMA_ID in (SELECT rl.M_RMA_ID FROM M_RMALine rl WHERE rl.M_RMA_ID=r.M_RMA_ID AND rl.QtyDelivered < rl.Qty AND rl.M_InOutLine_ID IS NOT NULL)";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sqlStmt, (String)null);
            pstmt.setInt(1, C_BPartner_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
            }
        }
        catch (SQLException e) {
            this.log.log(Level.SEVERE, sqlStmt.toString(), (Throwable)e);
            return list;
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        return list;
    }
    
    protected ArrayList<KeyNamePair> loadInvoiceData(final int C_BPartner_ID) {
        final ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
        final StringBuffer display = new StringBuffer("i.DocumentNo||' - '||").append(DB.TO_CHAR("DateInvoiced", 15, Env.getAD_Language(Env.getCtx()))).append("|| ' - ' ||").append(DB.TO_CHAR("GrandTotal", 12, Env.getAD_Language(Env.getCtx())));
        final StringBuffer sql = new StringBuffer("SELECT i.C_Invoice_ID,").append(display).append(" FROM C_Invoice i WHERE i.C_BPartner_ID=? AND i.IsSOTrx='N' AND i.DocStatus IN ('CL','CO') AND i.C_Invoice_ID IN (SELECT il.C_Invoice_ID FROM C_InvoiceLine il LEFT OUTER JOIN M_MatchInv mi ON (il.C_InvoiceLine_ID=mi.C_InvoiceLine_ID)  JOIN C_Invoice i2 ON (il.C_Invoice_ID = i2.C_Invoice_ID)  WHERE i2.C_BPartner_ID=? AND i2.IsSOTrx='N' AND i2.DocStatus IN ('CL','CO') GROUP BY il.C_Invoice_ID,mi.C_InvoiceLine_ID,il.QtyInvoiced HAVING (il.QtyInvoiced<>SUM(mi.Qty) AND mi.C_InvoiceLine_ID IS NOT NULL) OR mi.C_InvoiceLine_ID IS NULL) ORDER BY i.DateInvoiced");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, C_BPartner_ID);
            pstmt.setInt(2, C_BPartner_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
            }
        }
        catch (SQLException e) {
            this.log.log(Level.SEVERE, sql.toString(), (Throwable)e);
            return list;
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        return list;
    }
    
    protected Vector<Vector<Object>> getOrderData(final int C_Order_ID, final boolean forInvoice) {
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("C_Order_ID=" + C_Order_ID);
        }
        this.p_order = new MOrder(Env.getCtx(), C_Order_ID, (String)null);
        final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        final StringBuilder sql = new StringBuilder("SELECT l.QtyOrdered-SUM(COALESCE(m.Qty,0))-COALESCE((SELECT SUM(MovementQty) FROM M_InOutLine iol JOIN M_InOut io ON iol.M_InOut_ID=io.M_InOut_ID WHERE l.C_OrderLine_ID=iol.C_OrderLine_ID AND io.Processed='N'),0),CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END, l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name), p.M_Locator_ID, loc.Value,  COALESCE(l.M_Product_ID,0),COALESCE(p.Value,c.Name),  p.Name,  l.C_OrderLine_ID,l.Line FROM C_OrderLine l LEFT OUTER JOIN M_Product_PO po ON (l.M_Product_ID = po.M_Product_ID AND l.C_BPartner_ID = po.C_BPartner_ID)  LEFT OUTER JOIN M_MatchPO m ON (l.C_OrderLine_ID=m.C_OrderLine_ID AND ");
        sql.append(forInvoice ? "m.C_InvoiceLine_ID" : "m.M_InOutLine_ID");
        sql.append(" IS NOT NULL)").append(" LEFT OUTER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID) LEFT OUTER JOIN M_Locator loc on (p.M_Locator_ID=loc.M_Locator_ID) LEFT OUTER JOIN C_Charge c ON (l.C_Charge_ID=c.C_Charge_ID)");
        if (Env.isBaseLanguage(Env.getCtx(), "C_UOM")) {
            sql.append(" LEFT OUTER JOIN C_UOM uom ON (l.C_UOM_ID=uom.C_UOM_ID)");
        }
        else {
            sql.append(" LEFT OUTER JOIN C_UOM_Trl uom ON (l.C_UOM_ID=uom.C_UOM_ID AND uom.AD_Language='").append(Env.getAD_Language(Env.getCtx())).append("')");
        }
        sql.append(" WHERE l.C_Order_ID=? GROUP BY l.QtyOrdered,CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END, l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name), p.M_Locator_ID, loc.Value, p.Name, l.M_Product_ID,COALESCE(p.Value,c.Name), l.Line,l.C_OrderLine_ID ORDER BY l.Line");
        if (this.log.isLoggable(Level.FINER)) {
            this.log.finer(sql.toString());
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, C_Order_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final Vector<Object> line = new Vector<Object>();
                line.add(new Boolean(false));
                final BigDecimal qtyOrdered = rs.getBigDecimal(1);
                final BigDecimal multiplier = rs.getBigDecimal(2);
                final BigDecimal qtyEntered = qtyOrdered.multiply(multiplier);
                line.add(qtyEntered);
                KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(4).trim());
                line.add(pp);
                line.add(this.getLocatorKeyNamePair(rs.getInt(5)));
                pp = new KeyNamePair(rs.getInt(7), rs.getString(8));
                line.add(pp);
                line.add(rs.getString(9));
                pp = new KeyNamePair(rs.getInt(10), rs.getString(11));
                line.add(pp);
                line.add(null);
                line.add(null);
                data.add(line);
            }
        }
        catch (SQLException e) {
            this.log.log(Level.SEVERE, sql.toString(), (Throwable)e);
            return data;
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        return data;
    }
    
    protected Vector<Vector<Object>> getRMAData(final int M_RMA_ID) {
        this.m_invoice = null;
        this.p_order = null;
        this.m_rma = new MRMA(Env.getCtx(), M_RMA_ID, (String)null);
        final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        final StringBuilder sqlStmt = new StringBuilder();
        sqlStmt.append("SELECT rl.M_RMALine_ID, rl.line, rl.Qty - rl.QtyDelivered, p.M_Product_ID, COALESCE(p.Name, c.Name), uom.C_UOM_ID, COALESCE(uom.UOMSymbol,uom.Name) ");
        sqlStmt.append("FROM M_RMALine rl INNER JOIN M_InOutLine iol ON rl.M_InOutLine_ID=iol.M_InOutLine_ID ");
        if (Env.isBaseLanguage(Env.getCtx(), "C_UOM")) {
            sqlStmt.append("LEFT OUTER JOIN C_UOM uom ON (uom.C_UOM_ID=iol.C_UOM_ID) ");
        }
        else {
            sqlStmt.append("LEFT OUTER JOIN C_UOM_Trl uom ON (uom.C_UOM_ID=iol.C_UOM_ID AND uom.AD_Language='");
            sqlStmt.append(Env.getAD_Language(Env.getCtx())).append("') ");
        }
        sqlStmt.append("LEFT OUTER JOIN M_Product p ON p.M_Product_ID=iol.M_Product_ID ");
        sqlStmt.append("LEFT OUTER JOIN C_Charge c ON c.C_Charge_ID=iol.C_Charge_ID ");
        sqlStmt.append("WHERE rl.M_RMA_ID=? ");
        sqlStmt.append("AND rl.M_InOutLine_ID IS NOT NULL");
        sqlStmt.append(" UNION ");
        sqlStmt.append("SELECT rl.M_RMALine_ID, rl.line, rl.Qty - rl.QtyDelivered, p.M_Product_ID, p.Name, uom.C_UOM_ID, COALESCE(uom.UOMSymbol,uom.Name) ");
        sqlStmt.append("FROM M_RMALine rl INNER JOIN M_Product p ON p.M_Product_ID = rl.M_Product_ID ");
        if (Env.isBaseLanguage(Env.getCtx(), "C_UOM")) {
            sqlStmt.append("LEFT OUTER JOIN C_UOM uom ON (uom.C_UOM_ID=p.C_UOM_ID) ");
        }
        else {
            sqlStmt.append("LEFT OUTER JOIN C_UOM_Trl uom ON (uom.C_UOM_ID=100 AND uom.AD_Language='");
            sqlStmt.append(Env.getAD_Language(Env.getCtx())).append("') ");
        }
        sqlStmt.append("WHERE rl.M_RMA_ID=? ");
        sqlStmt.append("AND rl.M_Product_ID IS NOT NULL AND rl.M_InOutLine_ID IS NULL");
        sqlStmt.append(" UNION ");
        sqlStmt.append("SELECT rl.M_RMALine_ID, rl.line, rl.Qty - rl.QtyDelivered, 0, c.Name, uom.C_UOM_ID, COALESCE(uom.UOMSymbol,uom.Name) ");
        sqlStmt.append("FROM M_RMALine rl INNER JOIN C_Charge c ON c.C_Charge_ID = rl.C_Charge_ID ");
        if (Env.isBaseLanguage(Env.getCtx(), "C_UOM")) {
            sqlStmt.append("LEFT OUTER JOIN C_UOM uom ON (uom.C_UOM_ID=100) ");
        }
        else {
            sqlStmt.append("LEFT OUTER JOIN C_UOM_Trl uom ON (uom.C_UOM_ID=100 AND uom.AD_Language='");
            sqlStmt.append(Env.getAD_Language(Env.getCtx())).append("') ");
        }
        sqlStmt.append("WHERE rl.M_RMA_ID=? ");
        sqlStmt.append("AND rl.C_Charge_ID IS NOT NULL AND rl.M_InOutLine_ID IS NULL");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sqlStmt.toString(), (String)null);
            pstmt.setInt(1, M_RMA_ID);
            pstmt.setInt(2, M_RMA_ID);
            pstmt.setInt(3, M_RMA_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final Vector<Object> line = new Vector<Object>(7);
                line.add(new Boolean(false));
                line.add(rs.getBigDecimal(3));
                KeyNamePair pp = new KeyNamePair(rs.getInt(6), rs.getString(7));
                line.add(pp);
                line.add(this.getLocatorKeyNamePair(0));
                pp = new KeyNamePair(rs.getInt(4), rs.getString(5));
                line.add(pp);
                line.add(null);
                line.add(null);
                pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
                line.add(pp);
                line.add(null);
                data.add(line);
            }
        }
        catch (Exception ex) {
            this.log.log(Level.SEVERE, sqlStmt.toString(), (Throwable)ex);
            return data;
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        return data;
    }
    
    protected Vector<Vector<Object>> getInvoiceData(final int C_Invoice_ID) {
        this.m_invoice = new MInvoice(Env.getCtx(), C_Invoice_ID, (String)null);
        this.p_order = null;
        this.m_rma = null;
        final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        final StringBuilder sql = new StringBuilder("SELECT l.QtyInvoiced-SUM(NVL(mi.Qty,0)),l.QtyEntered/l.QtyInvoiced, l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name), p.M_Locator_ID, loc.Value,  l.M_Product_ID,p.Name, po.VendorProductNo, l.C_InvoiceLine_ID,l.Line, l.C_OrderLine_ID  FROM C_InvoiceLine l ");
        if (Env.isBaseLanguage(Env.getCtx(), "C_UOM")) {
            sql.append(" LEFT OUTER JOIN C_UOM uom ON (l.C_UOM_ID=uom.C_UOM_ID)");
        }
        else {
            sql.append(" LEFT OUTER JOIN C_UOM_Trl uom ON (l.C_UOM_ID=uom.C_UOM_ID AND uom.AD_Language='").append(Env.getAD_Language(Env.getCtx())).append("')");
        }
        sql.append(" LEFT OUTER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID)").append(" LEFT OUTER JOIN M_Locator loc on (p.M_Locator_ID=loc.M_Locator_ID)").append(" INNER JOIN C_Invoice inv ON (l.C_Invoice_ID=inv.C_Invoice_ID)").append(" LEFT OUTER JOIN M_Product_PO po ON (l.M_Product_ID = po.M_Product_ID AND inv.C_BPartner_ID = po.C_BPartner_ID)").append(" LEFT OUTER JOIN M_MatchInv mi ON (l.C_InvoiceLine_ID=mi.C_InvoiceLine_ID)").append(" WHERE l.C_Invoice_ID=? AND l.QtyInvoiced<>0 ").append("GROUP BY l.QtyInvoiced,l.QtyEntered/l.QtyInvoiced,l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name),p.M_Locator_ID, loc.Value, l.M_Product_ID,p.Name, po.VendorProductNo, l.C_InvoiceLine_ID,l.Line,l.C_OrderLine_ID ").append("ORDER BY l.Line");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, C_Invoice_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final Vector<Object> line = new Vector<Object>(7);
                line.add(new Boolean(false));
                final BigDecimal qtyInvoiced = rs.getBigDecimal(1);
                final BigDecimal multiplier = rs.getBigDecimal(2);
                final BigDecimal qtyEntered = qtyInvoiced.multiply(multiplier);
                line.add(qtyEntered);
                KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(4).trim());
                line.add(pp);
                line.add(this.getLocatorKeyNamePair(rs.getInt(5)));
                pp = new KeyNamePair(rs.getInt(7), rs.getString(8));
                line.add(pp);
                line.add(rs.getString(9));
                final int C_OrderLine_ID = rs.getInt(12);
                if (rs.wasNull()) {
                    line.add(null);
                }
                else {
                    line.add(new KeyNamePair(C_OrderLine_ID, "."));
                }
                line.add(null);
                pp = new KeyNamePair(rs.getInt(10), rs.getString(11));
                line.add(pp);
                data.add(line);
            }
        }
        catch (SQLException e) {
            this.log.log(Level.SEVERE, sql.toString(), (Throwable)e);
            return data;
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        return data;
    }
    
    protected KeyNamePair getLocatorKeyNamePair(final int M_Locator_ID) {
        MLocator locator = null;
        if (M_Locator_ID > 0) {
            locator = MLocator.get(Env.getCtx(), M_Locator_ID);
            if (locator != null && locator.getM_Warehouse_ID() != this.getM_Warehouse_ID()) {
                locator = null;
            }
        }
        if (locator == null && this.p_order != null && this.p_order.getM_Warehouse_ID() == this.getM_Warehouse_ID()) {
            final MWarehouse wh = MWarehouse.get(Env.getCtx(), this.p_order.getM_Warehouse_ID());
            if (wh != null) {
                locator = wh.getDefaultLocator();
            }
        }
        if (locator == null && this.defaultLocator_ID > 0) {
            locator = MLocator.get(Env.getCtx(), this.defaultLocator_ID);
        }
        if (locator == null || locator.getM_Warehouse_ID() != this.getM_Warehouse_ID()) {
            locator = MWarehouse.get(Env.getCtx(), this.getM_Warehouse_ID()).getDefaultLocator();
        }
        KeyNamePair pp = null;
        if (locator != null) {
            pp = new KeyNamePair(locator.get_ID(), locator.getValue());
        }
        return pp;
    }
    
    public void info(final IMiniTable miniTable, final IStatusBar statusBar) {
    }
    
    protected void configureMiniTable(final IMiniTable miniTable) {
        miniTable.setColumnClass(0, (Class)Boolean.class, false);
        miniTable.setColumnClass(1, (Class)BigDecimal.class, false);
        miniTable.setColumnClass(2, (Class)String.class, true);
        miniTable.setColumnClass(3, (Class)String.class, false);
        miniTable.setColumnClass(4, (Class)String.class, true);
        miniTable.setColumnClass(5, (Class)String.class, true);
        miniTable.setColumnClass(6, (Class)String.class, true);
        miniTable.setColumnClass(7, (Class)String.class, true);
        miniTable.setColumnClass(8, (Class)String.class, true);
        miniTable.autoSize();
    }
    
    public boolean save(final IMiniTable miniTable, final String trxName) {
        int M_Locator_ID = this.defaultLocator_ID;
        if (M_Locator_ID == 0) {
            return false;
        }
        final int M_InOut_ID = (int)this.getGridTab().getValue("M_InOut_ID");
        final MInOut inout = new MInOut(Env.getCtx(), M_InOut_ID, trxName);
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config(inout + ", C_Locator_ID=" + M_Locator_ID);
        }
        for (int i = 0; i < miniTable.getRowCount(); ++i) {
            if ((boolean) miniTable.getValueAt(i, 0)) {
                BigDecimal QtyEntered = (BigDecimal)miniTable.getValueAt(i, 1);
                KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(i, 2);
                final int C_UOM_ID = pp.getKey();
                pp = (KeyNamePair)miniTable.getValueAt(i, 3);
                M_Locator_ID = ((pp != null && pp.getKey() != 0) ? pp.getKey() : this.defaultLocator_ID);
                pp = (KeyNamePair)miniTable.getValueAt(i, 4);
                final int M_Product_ID = pp.getKey();
                int C_OrderLine_ID = 0;
                pp = (KeyNamePair)miniTable.getValueAt(i, 6);
                if (pp != null) {
                    C_OrderLine_ID = pp.getKey();
                }
                int M_RMALine_ID = 0;
                pp = (KeyNamePair)miniTable.getValueAt(i, 7);
                if (pp != null) {
                    M_RMALine_ID = pp.getKey();
                }
                int C_InvoiceLine_ID = 0;
                MInvoiceLine il = null;
                pp = (KeyNamePair)miniTable.getValueAt(i, 8);
                if (pp != null) {
                    C_InvoiceLine_ID = pp.getKey();
                }
                if (C_InvoiceLine_ID != 0) {
                    il = new MInvoiceLine(Env.getCtx(), C_InvoiceLine_ID, trxName);
                }
                int precision = 2;
                if (M_Product_ID != 0) {
                    final MProduct product = MProduct.get(Env.getCtx(), M_Product_ID);
                    precision = product.getUOMPrecision();
                }
                QtyEntered = QtyEntered.setScale(precision, 5);
                if (this.log.isLoggable(Level.FINE)) {
                    this.log.fine("Line QtyEntered=" + QtyEntered + ", Product=" + M_Product_ID + ", OrderLine=" + C_OrderLine_ID + ", InvoiceLine=" + C_InvoiceLine_ID);
                }
                if (this.m_invoice != null && this.m_invoice.isCreditMemo()) {
                    QtyEntered = QtyEntered.negate();
                }
                final MProduct product = new MProduct(Env.getCtx(), M_Product_ID, trxName);
                final boolean isASI = product.isASIMandatory(true);
                final BigDecimal qtyPacking = QtyEntered;
                int idxpacking = 1;
                if (isASI) {
                    idxpacking = QtyEntered.divide(qtyPacking, 0, RoundingMode.HALF_UP).intValue();
                    final BigDecimal remainder = qtyPacking.multiply(BigDecimal.valueOf(idxpacking)).subtract(QtyEntered);
                    if (remainder.compareTo(Env.ZERO) > 0) {
                        this.log.severe("Qty Entered is not mutiplier of Packing Qty");
                        throw new AdempiereException("Qty Entered is not mutiplier of Packing Qty");
                    }
                }
                for (int j = 1; j <= idxpacking; ++j) {
                    final MInOutLine iol = new MInOutLine(inout);
                    iol.setM_Product_ID(M_Product_ID, C_UOM_ID);
                    iol.setQty(QtyEntered);
                    MOrderLine ol = null;
                    MRMALine rmal = null;
                    if (C_OrderLine_ID != 0) {
                        iol.setC_OrderLine_ID(C_OrderLine_ID);
                        ol = new MOrderLine(Env.getCtx(), C_OrderLine_ID, trxName);
                        if (ol.getQtyEntered().compareTo(ol.getQtyOrdered()) != 0) {
                            iol.setMovementQty(QtyEntered.multiply(ol.getQtyOrdered()).divide(ol.getQtyEntered(), 12, 4));
                            iol.setC_UOM_ID(ol.getC_UOM_ID());
                        }
                        iol.setDescription(ol.getDescription());
                        iol.setC_Project_ID(ol.getC_Project_ID());
                        iol.setC_ProjectPhase_ID(ol.getC_ProjectPhase_ID());
                        iol.setC_ProjectTask_ID(ol.getC_ProjectTask_ID());
                        iol.setC_Activity_ID(ol.getC_Activity_ID());
                        iol.setC_Campaign_ID(ol.getC_Campaign_ID());
                        iol.setAD_OrgTrx_ID(ol.getAD_OrgTrx_ID());
                        iol.setUser1_ID(ol.getUser1_ID());
                        iol.setUser2_ID(ol.getUser2_ID());
                    }
                    else if (il != null) {
                        if (il.getQtyEntered().compareTo(il.getQtyInvoiced()) != 0) {
                            iol.setQtyEntered(QtyEntered.multiply(il.getQtyInvoiced()).divide(il.getQtyEntered(), 12, 4));
                            iol.setC_UOM_ID(il.getC_UOM_ID());
                        }
                        iol.setDescription(il.getDescription());
                        iol.setC_Project_ID(il.getC_Project_ID());
                        iol.setC_ProjectPhase_ID(il.getC_ProjectPhase_ID());
                        iol.setC_ProjectTask_ID(il.getC_ProjectTask_ID());
                        iol.setC_Activity_ID(il.getC_Activity_ID());
                        iol.setC_Campaign_ID(il.getC_Campaign_ID());
                        iol.setAD_OrgTrx_ID(il.getAD_OrgTrx_ID());
                        iol.setUser1_ID(il.getUser1_ID());
                        iol.setUser2_ID(il.getUser2_ID());
                    }
                    else if (M_RMALine_ID != 0) {
                        rmal = new MRMALine(Env.getCtx(), M_RMALine_ID, trxName);
                        iol.setM_RMALine_ID(M_RMALine_ID);
                        iol.setQtyEntered(QtyEntered);
                        iol.setDescription(rmal.getDescription());
                        iol.setM_AttributeSetInstance_ID(rmal.getM_AttributeSetInstance_ID());
                        iol.setC_Project_ID(rmal.getC_Project_ID());
                        iol.setC_ProjectPhase_ID(rmal.getC_ProjectPhase_ID());
                        iol.setC_ProjectTask_ID(rmal.getC_ProjectTask_ID());
                        iol.setC_Activity_ID(rmal.getC_Activity_ID());
                        iol.setAD_OrgTrx_ID(rmal.getAD_OrgTrx_ID());
                        iol.setUser1_ID(rmal.getUser1_ID());
                        iol.setUser2_ID(rmal.getUser2_ID());
                    }
                    if (M_Product_ID == 0) {
                        if (ol != null && ol.getC_Charge_ID() != 0) {
                            iol.setC_Charge_ID(ol.getC_Charge_ID());
                        }
                        else if (il != null && il.getC_Charge_ID() != 0) {
                            iol.setC_Charge_ID(il.getC_Charge_ID());
                        }
                        else if (rmal != null && rmal.getC_Charge_ID() != 0) {
                            iol.setC_Charge_ID(rmal.getC_Charge_ID());
                        }
                    }
                    iol.setM_Locator_ID(M_Locator_ID);
                    iol.saveEx();
                    if (il != null) {
                        il.setM_InOutLine_ID(iol.getM_InOutLine_ID());
                        il.saveEx();
                    }
                }
            }
        }
        if (this.p_order != null && this.p_order.getC_Order_ID() != 0) {
            inout.setC_Order_ID(this.p_order.getC_Order_ID());
            inout.setAD_OrgTrx_ID(this.p_order.getAD_OrgTrx_ID());
            inout.setC_Project_ID(this.p_order.getC_Project_ID());
            inout.setC_Campaign_ID(this.p_order.getC_Campaign_ID());
            inout.setC_Activity_ID(this.p_order.getC_Activity_ID());
            inout.setUser1_ID(this.p_order.getUser1_ID());
            inout.setUser2_ID(this.p_order.getUser2_ID());
            if (this.p_order.isDropShip()) {
                inout.setM_Warehouse_ID(this.p_order.getM_Warehouse_ID());
                inout.setIsDropShip(this.p_order.isDropShip());
                inout.setDropShip_BPartner_ID(this.p_order.getDropShip_BPartner_ID());
                inout.setDropShip_Location_ID(this.p_order.getDropShip_Location_ID());
                inout.setDropShip_User_ID(this.p_order.getDropShip_User_ID());
            }
        }
        if (this.m_invoice != null && this.m_invoice.getC_Invoice_ID() != 0) {
            if (inout.getC_Order_ID() == 0) {
                inout.setC_Order_ID(this.m_invoice.getC_Order_ID());
            }
            inout.setC_Invoice_ID(this.m_invoice.getC_Invoice_ID());
            inout.setAD_OrgTrx_ID(this.m_invoice.getAD_OrgTrx_ID());
            inout.setC_Project_ID(this.m_invoice.getC_Project_ID());
            inout.setC_Campaign_ID(this.m_invoice.getC_Campaign_ID());
            inout.setC_Activity_ID(this.m_invoice.getC_Activity_ID());
            inout.setUser1_ID(this.m_invoice.getUser1_ID());
            inout.setUser2_ID(this.m_invoice.getUser2_ID());
        }
        if (this.m_rma != null && this.m_rma.getM_RMA_ID() != 0) {
            final MInOut originalIO = this.m_rma.getShipment();
            inout.setIsSOTrx(this.m_rma.isSOTrx());
            inout.setC_Order_ID(0);
            inout.setC_Invoice_ID(0);
            inout.setM_RMA_ID(this.m_rma.getM_RMA_ID());
            inout.setAD_OrgTrx_ID(originalIO.getAD_OrgTrx_ID());
            inout.setC_Project_ID(originalIO.getC_Project_ID());
            inout.setC_Campaign_ID(originalIO.getC_Campaign_ID());
            inout.setC_Activity_ID(originalIO.getC_Activity_ID());
            inout.setUser1_ID(originalIO.getUser1_ID());
            inout.setUser2_ID(originalIO.getUser2_ID());
        }
        inout.saveEx();
        return true;
    }
    
    protected Vector<String> getOISColumnNames() {
        final Vector<String> columnNames = new Vector<String>(7);
        columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
        columnNames.add(Msg.translate(Env.getCtx(), "Quantity"));
        columnNames.add(Msg.translate(Env.getCtx(), "C_UOM_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "M_Locator_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "Value"));
        columnNames.add(Msg.getElement(Env.getCtx(), "M_Product_ID", false));
        final boolean isSOTrx = new MWindow(Env.getCtx(), this.getGridTab().getAD_Window_ID(), (String)null).isSOTrx();
        columnNames.add(Msg.getElement(Env.getCtx(), "C_OrderLine_ID", isSOTrx));
        columnNames.add(Msg.getElement(Env.getCtx(), "M_RMA_ID", false));
        columnNames.add(Msg.getElement(Env.getCtx(), "C_Invoice_ID", false));
        return columnNames;
    }
    
    protected Vector<Vector<Object>> getOrderData(final int C_Order_ID, final boolean forInvoice, final int M_Locator_ID) {
        this.defaultLocator_ID = M_Locator_ID;
        return this.getOrderData(C_Order_ID, forInvoice);
    }
    
    protected Vector<Vector<Object>> getRMAData(final int M_RMA_ID, final int M_Locator_ID) {
        this.defaultLocator_ID = M_Locator_ID;
        return this.getRMAData(M_RMA_ID);
    }
    
    protected Vector<Vector<Object>> getInvoiceData(final int C_Invoice_ID, final int M_Locator_ID) {
        this.defaultLocator_ID = M_Locator_ID;
        return this.getInvoiceData(C_Invoice_ID);
    }
    
    protected ArrayList<KeyNamePair> loadOrderData(final int C_BPartner_ID, final boolean forInvoice, final boolean sameWarehouseOnly) {
        final ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
        final String isSOTrxParam = this.isSOTrx ? "Y" : "N";
        final StringBuffer display = new StringBuffer("o.DocumentNo||' - ' ||").append(DB.TO_CHAR("o.DateOrdered", 15, Env.getAD_Language(Env.getCtx()))).append("||' - '||").append(DB.TO_CHAR("o.GrandTotal", 12, Env.getAD_Language(Env.getCtx())));
        String column = "ol.QtyDelivered";
        if (forInvoice) {
            column = "ol.QtyInvoiced";
        }
        StringBuffer sql = new StringBuffer("SELECT o.C_Order_ID,").append(display).append(" FROM C_Order o WHERE o.C_BPartner_ID=? AND o.IsSOTrx=? AND o.DocStatus IN ('CL','CO') AND o.C_Order_ID IN (SELECT ol.C_Order_ID FROM C_OrderLine ol WHERE ol.QtyOrdered - ").append(column).append(" != 0) ");
        if (sameWarehouseOnly) {
            sql = sql.append(" AND o.M_Warehouse_ID=? ");
        }
        sql = sql.append("ORDER BY o.DateOrdered,o.DocumentNo");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, C_BPartner_ID);
            pstmt.setString(2, isSOTrxParam);
            if (sameWarehouseOnly) {
                pstmt.setInt(3, this.getM_Warehouse_ID());
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
            }
        }
        catch (SQLException e) {
            this.log.log(Level.SEVERE, sql.toString(), (Throwable)e);
            return list;
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        return list;
    }
    
    public Object getWindow() {
        return null;
    }
}
