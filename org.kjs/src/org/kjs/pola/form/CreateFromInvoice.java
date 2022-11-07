package org.kjs.pola.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.IStatusBar;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MCurrency;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrderPaySchedule;
import org.compiere.model.MProduct;
import org.compiere.model.MRMA;
import org.compiere.model.MRMALine;
import org.compiere.model.MUOMConversion;
import org.compiere.model.PO;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

public abstract class CreateFromInvoice extends CreateFrom
{
    public CreateFromInvoice(final GridTab mTab) {
        super(mTab);
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info(mTab.toString());
        }
    }
    
    public boolean dynInit() throws Exception {
        this.log.config("");
        this.setTitle(String.valueOf(Msg.getElement(Env.getCtx(), "C_Invoice_ID", false)) + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));
        return true;
    }
    
    protected ArrayList<KeyNamePair> loadShipmentData(final int C_BPartner_ID) {
        final String isSOTrxParam = this.isSOTrx ? "Y" : "N";
        final ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
        final StringBuffer display = new StringBuffer("s.DocumentNo||' - '||").append(DB.TO_CHAR("s.MovementDate", 15, Env.getAD_Language(Env.getCtx())));
        final StringBuffer sql = new StringBuffer("SELECT s.M_InOut_ID,").append(display).append(" FROM M_InOut s WHERE s.C_BPartner_ID=? AND s.IsSOTrx=? AND s.DocStatus IN ('CL','CO') AND s.M_InOut_ID IN (SELECT sl.M_InOut_ID FROM M_InOutLine sl");
        if (!this.isSOTrx) {
            sql.append(" LEFT OUTER JOIN M_MatchInv mi ON (sl.M_InOutLine_ID=mi.M_InOutLine_ID)  JOIN M_InOut s2 ON (sl.M_InOut_ID=s2.M_InOut_ID)  WHERE s2.C_BPartner_ID=? AND s2.IsSOTrx=? AND s2.DocStatus IN ('CL','CO')  GROUP BY sl.M_InOut_ID,sl.MovementQty,mi.M_InOutLine_ID HAVING (sl.MovementQty<>SUM(mi.Qty) AND mi.M_InOutLine_ID IS NOT NULL) OR mi.M_InOutLine_ID IS NULL ");
        }
        else {
            sql.append(" INNER JOIN M_InOut s2 ON (sl.M_InOut_ID=s2.M_InOut_ID) LEFT JOIN C_InvoiceLine il ON sl.M_InOutLine_ID = il.M_InOutLine_ID WHERE s2.C_BPartner_ID=? AND s2.IsSOTrx=? AND s2.DocStatus IN ('CL','CO') GROUP BY sl.M_InOutLine_ID HAVING sl.MovementQty - sum(COALESCE(il.QtyInvoiced,0)) > 0");
        }
        sql.append(") ORDER BY s.MovementDate");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, C_BPartner_ID);
            pstmt.setString(2, isSOTrxParam);
            pstmt.setInt(3, C_BPartner_ID);
            pstmt.setString(4, isSOTrxParam);
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
    
    protected ArrayList<KeyNamePair> loadRMAData(final int C_BPartner_ID) {
        final ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
        final String sqlStmt = "SELECT r.M_RMA_ID, r.DocumentNo || '-' || r.Amt from M_RMA r WHERE ISSOTRX='N' AND r.DocStatus in ('CO', 'CL') AND r.C_BPartner_ID=? AND NOT EXISTS (SELECT * FROM C_Invoice inv WHERE inv.M_RMA_ID=r.M_RMA_ID AND inv.DocStatus IN ('CO', 'CL'))";
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
    
    protected Vector<Vector<Object>> getShipmentData(final int M_InOut_ID) {
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("M_InOut_ID=" + M_InOut_ID);
        }
        final MInOut inout = new MInOut(Env.getCtx(), M_InOut_ID, (String)null);
        this.p_order = null;
        if (inout.getC_Order_ID() != 0) {
            this.p_order = new MOrder(Env.getCtx(), inout.getC_Order_ID(), (String)null);
        }
        this.m_rma = null;
        if (inout.getM_RMA_ID() != 0) {
            this.m_rma = new MRMA(Env.getCtx(), inout.getM_RMA_ID(), (String)null);
        }
        final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        final StringBuilder sql = new StringBuilder("SELECT ");
        if (!this.isSOTrx) {
            sql.append("l.MovementQty-SUM(COALESCE(mi.Qty, 0)),");
        }
        else {
            sql.append("l.MovementQty-SUM(COALESCE(il.QtyInvoiced,0)),");
        }
        sql.append(" l.QtyEntered/l.MovementQty, l.C_UOM_ID, COALESCE(uom.UOMSymbol, uom.Name), l.M_Product_ID, p.Name, po.VendorProductNo, l.M_InOutLine_ID, l.Line, l.C_OrderLine_ID  FROM M_InOutLine l ");
        if (Env.isBaseLanguage(Env.getCtx(), "C_UOM")) {
            sql.append(" LEFT OUTER JOIN C_UOM uom ON (l.C_UOM_ID=uom.C_UOM_ID)");
        }
        else {
            sql.append(" LEFT OUTER JOIN C_UOM_Trl uom ON (l.C_UOM_ID=uom.C_UOM_ID AND uom.AD_Language='").append(Env.getAD_Language(Env.getCtx())).append("')");
        }
        sql.append(" LEFT OUTER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID)").append(" INNER JOIN M_InOut io ON (l.M_InOut_ID=io.M_InOut_ID)");
        if (!this.isSOTrx) {
            sql.append(" LEFT OUTER JOIN M_MatchInv mi ON (l.M_InOutLine_ID=mi.M_InOutLine_ID)");
        }
        else {
            sql.append(" LEFT JOIN C_InvoiceLine il ON l.M_InOutLine_ID = il.M_InOutLine_ID");
        }
        sql.append(" LEFT OUTER JOIN M_Product_PO po ON (l.M_Product_ID = po.M_Product_ID AND io.C_BPartner_ID = po.C_BPartner_ID)").append(" WHERE l.M_InOut_ID=? AND l.MovementQty<>0 ").append("GROUP BY l.MovementQty, l.QtyEntered/l.MovementQty, l.C_UOM_ID, COALESCE(uom.UOMSymbol, uom.Name), l.M_Product_ID, p.Name, po.VendorProductNo, l.M_InOutLine_ID, l.Line, l.C_OrderLine_ID ");
        if (!this.isSOTrx) {
            sql.append(" HAVING l.MovementQty-SUM(COALESCE(mi.Qty, 0)) <>0");
        }
        else {
            sql.append(" HAVING l.MovementQty-SUM(COALESCE(il.QtyInvoiced,0)) <>0");
        }
        sql.append("ORDER BY l.Line");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, M_InOut_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final Vector<Object> line = new Vector<Object>(7);
                line.add(new Boolean(false));
                final BigDecimal qtyMovement = rs.getBigDecimal(1);
                final BigDecimal multiplier = rs.getBigDecimal(2);
                final BigDecimal qtyEntered = qtyMovement.multiply(multiplier);
                line.add(qtyEntered);
                KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(4).trim());
                line.add(pp);
                pp = new KeyNamePair(rs.getInt(5), rs.getString(6));
                line.add(pp);
                line.add(rs.getString(7));
                final int C_OrderLine_ID = rs.getInt(10);
                if (rs.wasNull()) {
                    line.add(null);
                }
                else {
                    line.add(new KeyNamePair(C_OrderLine_ID, "."));
                }
                pp = new KeyNamePair(rs.getInt(8), rs.getString(9));
                line.add(pp);
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
        this.p_order = null;
        final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        final StringBuilder sqlStmt = new StringBuilder();
        sqlStmt.append("SELECT rl.M_RMALine_ID, rl.line, rl.Qty - COALESCE(rl.QtyInvoiced, 0), iol.M_Product_ID, p.Name, uom.C_UOM_ID, COALESCE(uom.UOMSymbol,uom.Name) ");
        sqlStmt.append("FROM M_RMALine rl INNER JOIN M_InOutLine iol ON rl.M_InOutLine_ID=iol.M_InOutLine_ID ");
        if (Env.isBaseLanguage(Env.getCtx(), "C_UOM")) {
            sqlStmt.append("LEFT OUTER JOIN C_UOM uom ON (uom.C_UOM_ID=iol.C_UOM_ID) ");
        }
        else {
            sqlStmt.append("LEFT OUTER JOIN C_UOM_Trl uom ON (uom.C_UOM_ID=iol.C_UOM_ID AND uom.AD_Language='");
            sqlStmt.append(Env.getAD_Language(Env.getCtx())).append("') ");
        }
        sqlStmt.append("LEFT OUTER JOIN M_Product p ON p.M_Product_ID=iol.M_Product_ID ");
        sqlStmt.append("WHERE rl.M_RMA_ID=? ");
        sqlStmt.append("AND rl.M_INOUTLINE_ID IS NOT NULL");
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
        sqlStmt.append("AND rl.C_Charge_ID IS NOT NULL");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sqlStmt.toString(), (String)null);
            pstmt.setInt(1, M_RMA_ID);
            pstmt.setInt(2, M_RMA_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final Vector<Object> line = new Vector<Object>(7);
                line.add(new Boolean(false));
                line.add(rs.getBigDecimal(3));
                KeyNamePair pp = new KeyNamePair(rs.getInt(6), rs.getString(7));
                line.add(pp);
                pp = new KeyNamePair(rs.getInt(4), rs.getString(5));
                line.add(pp);
                line.add(null);
                line.add(null);
                pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
                line.add(null);
                line.add(pp);
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
    
    protected ArrayList<KeyNamePair> loadOrderData(final int C_BPartner_ID, final boolean forInvoice, final boolean sameWarehouseOnly) {
        final ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
        final String isSOTrxParam = this.isSOTrx ? "Y" : "N";
        final StringBuffer display = new StringBuffer("o.DocumentNo||' - ' ||").append(DB.TO_CHAR("o.DateOrdered", 15, Env.getAD_Language(Env.getCtx()))).append("||' - '||").append(DB.TO_CHAR("o.GrandTotal", 12, Env.getAD_Language(Env.getCtx())));
        String column = "ol.QtyDelivered";
        if (forInvoice) {
            column = "ol.QtyInvoiced";
        }
        StringBuffer sql = new StringBuffer("SELECT o.C_Order_ID,").append(display).append(" FROM C_Order o WHERE o.C_BPartner_ID=? AND o.IsSOTrx=? AND o.DocStatus IN ('WP') AND o.C_Order_ID IN (SELECT ol.C_Order_ID FROM C_OrderLine ol WHERE ol.QtyOrdered - ").append(column).append(" != 0) ");
        if (sameWarehouseOnly) {
            sql = sql.append(" AND o.M_Warehouse_ID=? ");
        }
        sql = sql.append("ORDER BY o.DateOrdered");
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
    
    protected Vector<Vector<Object>> getOrderData(final int C_Order_ID, final boolean forInvoice) {
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("C_Order_ID=" + C_Order_ID);
        }
        this.p_order = new MOrder(Env.getCtx(), C_Order_ID, (String)null);
        final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        final StringBuilder sql = new StringBuilder("SELECT l.QtyOrdered-SUM(COALESCE(m.Qty,0)),CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END, l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name), COALESCE(l.M_Product_ID,0),COALESCE(p.Name,c.Name),po.VendorProductNo, l.C_OrderLine_ID,l.Line FROM C_OrderLine l LEFT OUTER JOIN M_Product_PO po ON (l.M_Product_ID = po.M_Product_ID AND l.C_BPartner_ID = po.C_BPartner_ID)  LEFT OUTER JOIN M_MatchPO m ON (l.C_OrderLine_ID=m.C_OrderLine_ID AND ");
        sql.append(forInvoice ? "m.C_InvoiceLine_ID" : "m.M_InOutLine_ID");
        sql.append(" IS NOT NULL)").append(" LEFT OUTER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID) LEFT OUTER JOIN C_Charge c ON (l.C_Charge_ID=c.C_Charge_ID)");
        if (Env.isBaseLanguage(Env.getCtx(), "C_UOM")) {
            sql.append(" LEFT OUTER JOIN C_UOM uom ON (l.C_UOM_ID=uom.C_UOM_ID)");
        }
        else {
            sql.append(" LEFT OUTER JOIN C_UOM_Trl uom ON (l.C_UOM_ID=uom.C_UOM_ID AND uom.AD_Language='").append(Env.getAD_Language(Env.getCtx())).append("')");
        }
        sql.append(" WHERE l.C_Order_ID=? GROUP BY l.QtyOrdered,CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END, l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name),po.VendorProductNo, l.M_Product_ID,COALESCE(p.Name,c.Name), l.Line,l.C_OrderLine_ID, p.upc ORDER BY l.Line");
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
                pp = new KeyNamePair(rs.getInt(5), rs.getString(6));
                line.add(pp);
                line.add(rs.getString(7));
                pp = new KeyNamePair(rs.getInt(8), rs.getString(9));
                line.add(pp);
                line.add(null);
                line.add(null);
                data.add(line);
            }
        }
        catch (Exception e) {
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
    
    public void info(final IMiniTable miniTable, final IStatusBar statusBar) {
    }
    
    protected void configureMiniTable(final IMiniTable miniTable) {
        miniTable.setColumnClass(0, (Class)Boolean.class, false);
        miniTable.setColumnClass(1, (Class)BigDecimal.class, true);
        miniTable.setColumnClass(2, (Class)String.class, true);
        miniTable.setColumnClass(3, (Class)String.class, true);
        miniTable.setColumnClass(4, (Class)String.class, true);
        miniTable.setColumnClass(5, (Class)String.class, true);
        miniTable.setColumnClass(6, (Class)String.class, true);
        miniTable.setColumnClass(7, (Class)String.class, true);
        miniTable.autoSize();
    }
    
    public boolean save(final IMiniTable miniTable, final String trxName) {
        final int C_Invoice_ID = (int)this.getGridTab().getValue("C_Invoice_ID");
        final MInvoice invoice = new MInvoice(Env.getCtx(), C_Invoice_ID, trxName);
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config(invoice.toString());
        }
        if (this.p_order != null) {
            invoice.setOrder(this.p_order);
            invoice.saveEx();
        }
        if (this.m_rma != null) {
            invoice.setM_RMA_ID(this.m_rma.getM_RMA_ID());
            invoice.saveEx();
        }
        for (int i = 0; i < miniTable.getRowCount(); ++i) {
            if ((boolean) miniTable.getValueAt(i, 0)) {
                MProduct product = null;
                BigDecimal QtyEntered = (BigDecimal)miniTable.getValueAt(i, 1);
                KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(i, 2);
                final int C_UOM_ID = pp.getKey();
                pp = (KeyNamePair)miniTable.getValueAt(i, 3);
                int M_Product_ID = 0;
                if (pp != null) {
                    M_Product_ID = pp.getKey();
                }
                int C_OrderLine_ID = 0;
                pp = (KeyNamePair)miniTable.getValueAt(i, 5);
                if (pp != null) {
                    C_OrderLine_ID = pp.getKey();
                }
                int M_InOutLine_ID = 0;
                pp = (KeyNamePair)miniTable.getValueAt(i, 6);
                if (pp != null) {
                    M_InOutLine_ID = pp.getKey();
                }
                int M_RMALine_ID = 0;
                pp = (KeyNamePair)miniTable.getValueAt(i, 7);
                if (pp != null) {
                    M_RMALine_ID = pp.getKey();
                }
                int precision = 2;
                if (M_Product_ID != 0) {
                    product = MProduct.get(Env.getCtx(), M_Product_ID);
                    precision = product.getUOMPrecision();
                }
                QtyEntered = QtyEntered.setScale(precision, 5);
                if (this.log.isLoggable(Level.FINE)) {
                    this.log.fine("Line QtyEntered=" + QtyEntered + ", Product_ID=" + M_Product_ID + ", OrderLine_ID=" + C_OrderLine_ID + ", InOutLine_ID=" + M_InOutLine_ID);
                }
                final MInvoiceLine invoiceLine = new MInvoiceLine(invoice);
                invoiceLine.setM_Product_ID(M_Product_ID, C_UOM_ID);
                invoiceLine.setQty(QtyEntered);
                BigDecimal QtyInvoiced = null;
                if (M_Product_ID > 0 && product.getC_UOM_ID() != C_UOM_ID) {
                    QtyInvoiced = MUOMConversion.convertProductFrom(Env.getCtx(), M_Product_ID, C_UOM_ID, QtyEntered);
                }
                if (QtyInvoiced == null) {
                    QtyInvoiced = QtyEntered;
                }
                invoiceLine.setQtyInvoiced(QtyInvoiced);
                MOrderLine orderLine = null;
                if (C_OrderLine_ID != 0) {
                    orderLine = new MOrderLine(Env.getCtx(), C_OrderLine_ID, trxName);
                }
                MRMALine rmaLine = null;
                if (M_RMALine_ID > 0) {
                    rmaLine = new MRMALine(Env.getCtx(), M_RMALine_ID, (String)null);
                }
                MInOutLine inoutLine = null;
                if (M_InOutLine_ID != 0) {
                    inoutLine = new MInOutLine(Env.getCtx(), M_InOutLine_ID, trxName);
                    if (orderLine == null && inoutLine.getC_OrderLine_ID() != 0) {
                        C_OrderLine_ID = inoutLine.getC_OrderLine_ID();
                        orderLine = new MOrderLine(Env.getCtx(), C_OrderLine_ID, trxName);
                    }
                }
                else if (C_OrderLine_ID > 0) {
                    final String whereClause = "EXISTS (SELECT 1 FROM M_InOut io WHERE io.M_InOut_ID=M_InOutLine.M_InOut_ID AND io.DocStatus IN ('CO','CL'))";
                    final MInOutLine[] lines = MInOutLine.getOfOrderLine(Env.getCtx(), C_OrderLine_ID, whereClause, trxName);
                    if (this.log.isLoggable(Level.FINE)) {
                        this.log.fine("Receipt Lines with OrderLine = #" + lines.length);
                    }
                    if (lines.length > 0) {
                        for (int j = 0; j < lines.length; ++j) {
                            final MInOutLine line = lines[j];
                            if (line.getQtyEntered().compareTo(QtyEntered) == 0) {
                                inoutLine = line;
                                M_InOutLine_ID = inoutLine.getM_InOutLine_ID();
                                break;
                            }
                        }
                    }
                }
                else if (M_RMALine_ID != 0) {
                    final String whereClause = "EXISTS (SELECT 1 FROM M_InOut io WHERE io.M_InOut_ID=M_InOutLine.M_InOut_ID AND io.DocStatus IN ('CO','CL'))";
                    final MInOutLine[] lines = MInOutLine.getOfRMALine(Env.getCtx(), M_RMALine_ID, whereClause, (String)null);
                    if (this.log.isLoggable(Level.FINE)) {
                        this.log.fine("Receipt Lines with RMALine = #" + lines.length);
                    }
                    if (lines.length > 0) {
                        for (int j = 0; j < lines.length; ++j) {
                            final MInOutLine line = lines[j];
                            if (rmaLine.getQty().compareTo(QtyEntered) == 0) {
                                inoutLine = line;
                                M_InOutLine_ID = inoutLine.getM_InOutLine_ID();
                                break;
                            }
                        }
                        if (rmaLine == null) {
                            inoutLine = lines[0];
                            M_InOutLine_ID = inoutLine.getM_InOutLine_ID();
                        }
                    }
                }
                if (inoutLine != null) {
                    invoiceLine.setShipLine(inoutLine);
                }
                else {
                    this.log.fine("No Receipt Line");
                    if (orderLine != null) {
                        invoiceLine.setOrderLine(orderLine);
                    }
                    else {
                        this.log.fine("No Order Line");
                        invoiceLine.setPrice();
                        invoiceLine.setTax();
                    }
                    if (rmaLine != null) {
                        invoiceLine.setRMALine(rmaLine);
                    }
                    else {
                        this.log.fine("No RMA Line");
                    }
                }
                invoiceLine.saveEx();
            }
        }
        if (this.p_order != null) {
            invoice.setPaymentRule(this.p_order.getPaymentRule());
            invoice.setC_PaymentTerm_ID(this.p_order.getC_PaymentTerm_ID());
            invoice.saveEx();
            invoice.load(invoice.get_TrxName());
            final MOrderPaySchedule[] opss = MOrderPaySchedule.getOrderPaySchedule(invoice.getCtx(), this.p_order.getC_Order_ID(), 0, invoice.get_TrxName());
            final MInvoicePaySchedule[] ipss = MInvoicePaySchedule.getInvoicePaySchedule(invoice.getCtx(), invoice.getC_Invoice_ID(), 0, invoice.get_TrxName());
            if (ipss.length == 0 && opss.length > 0) {
                final BigDecimal ogt = this.p_order.getGrandTotal();
                final BigDecimal igt = invoice.getGrandTotal();
                BigDecimal percent = Env.ONE;
                if (ogt.compareTo(igt) != 0) {
                    percent = igt.divide(ogt, 10, 4);
                }
                final MCurrency cur = MCurrency.get(this.p_order.getCtx(), this.p_order.getC_Currency_ID());
                final int scale = cur.getStdPrecision();
                MOrderPaySchedule[] array;
                for (int length = (array = opss).length, k = 0; k < length; ++k) {
                    final MOrderPaySchedule ops = array[k];
                    final MInvoicePaySchedule ips = new MInvoicePaySchedule(invoice.getCtx(), 0, invoice.get_TrxName());
                    PO.copyValues((PO)ops, (PO)ips);
                    if (percent != Env.ONE) {
                        BigDecimal propDueAmt = ops.getDueAmt().multiply(percent);
                        if (propDueAmt.scale() > scale) {
                            propDueAmt = propDueAmt.setScale(scale, 4);
                        }
                        ips.setDueAmt(propDueAmt);
                    }
                    ips.setC_Invoice_ID(invoice.getC_Invoice_ID());
                    ips.setAD_Org_ID(ops.getAD_Org_ID());
                    ips.setProcessing(ops.isProcessing());
                    ips.setIsActive(ops.isActive());
                    ips.saveEx();
                }
                invoice.validatePaySchedule();
                invoice.saveEx();
            }
        }
        return true;
    }
    
    protected Vector<String> getOISColumnNames() {
        final Vector<String> columnNames = new Vector<String>(7);
        columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
        columnNames.add(Msg.translate(Env.getCtx(), "Quantity"));
        columnNames.add(Msg.translate(Env.getCtx(), "C_UOM_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "M_Product_ID"));
        columnNames.add(Msg.getElement(Env.getCtx(), "VendorProductNo", this.isSOTrx));
        columnNames.add(Msg.getElement(Env.getCtx(), "C_Order_ID", this.isSOTrx));
        columnNames.add(Msg.getElement(Env.getCtx(), "M_InOut_ID", this.isSOTrx));
        columnNames.add(Msg.getElement(Env.getCtx(), "M_RMA_ID", this.isSOTrx));
        return columnNames;
    }
}
