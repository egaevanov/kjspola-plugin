package org.kjs.pola.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.IStatusBar;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MProduct;
import org.compiere.model.MProduction;
import org.compiere.model.MProductionLine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

public class CreateFromProduction extends CreateFrom
{
    protected int AD_Org_ID;
    protected int KJS_ProductionPlanLine_ID;
    protected int M_Locator_ID;
    protected int M_Product_ID;
    protected int M_Production_ID;
    
    public CreateFromProduction(final GridTab gridTab) {
        super(gridTab);
        this.AD_Org_ID = 0;
        this.KJS_ProductionPlanLine_ID = 0;
        this.M_Locator_ID = 0;
        this.M_Product_ID = 0;
        this.M_Production_ID = 0;
    }
    
    public Object getWindow() {
        return null;
    }
    
    public boolean dynInit() throws Exception {
        this.AD_Org_ID = (int)this.getGridTab().getValue("AD_Org_ID");
        this.KJS_ProductionPlanLine_ID = (int)this.getGridTab().getValue("KJS_ProductionPlanLine_ID");
        this.M_Locator_ID = (int)this.getGridTab().getValue("M_Locator_ID");
        this.M_Product_ID = (int)this.getGridTab().getValue("M_Product_ID");
        this.M_Production_ID = (int)this.getGridTab().getValue("M_Production_ID");
        return false;
    }
    
    public void info(final IMiniTable miniTable, final IStatusBar statusBar) {
    }
    
    protected Vector<Vector<Object>> getBOMData(final int KJS_ProductionPlanLine_ID) {
        final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        final StringBuilder sql = new StringBuilder("SELECT prod.M_Product_ID,prod.Value,pplb.Qty,asi.M_AttributeSetInstance_ID,asi.Description FROM KJS_ProductionPlanLineBOM pplb JOIN M_Product prod ON pplb.M_Product_ID=prod.M_Product_ID LEFT JOIN M_AttributeSetInstance asi ON pplb.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID WHERE KJS_ProductionPlanLine_ID=?");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, KJS_ProductionPlanLine_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final Vector<Object> line = new Vector<Object>();
                line.add(new Boolean(false));
                KeyNamePair pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
                line.add(pp);
                line.add(rs.getBigDecimal(3));
                pp = new KeyNamePair(rs.getInt(4), rs.getString(5));
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
    
    public boolean save(final IMiniTable miniTable, final String trxName) {
        for (int i = 0; i < miniTable.getRowCount(); ++i) {
            if ((boolean) miniTable.getValueAt(i, 0)) {
                final MProductionLine pl = new MProductionLine(Env.getCtx(), 0, trxName);
                KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(i, 1);
                final int M_ProductBOM_ID = pp.getKey();
                final BigDecimal Qty = (BigDecimal)miniTable.getValueAt(i, 2);
                pp = (KeyNamePair)miniTable.getValueAt(i, 3);
                final int M_AttributeSetInstance_ID = pp.getKey();
                pl.setAD_Org_ID(this.AD_Org_ID);
                pl.setM_Product_ID(M_ProductBOM_ID);
                pl.setM_Locator_ID(this.M_Locator_ID);
                pl.setPlannedQty(Qty);
                pl.setQtyUsed(Qty);
                pl.setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
                pl.setM_Production_ID(this.M_Production_ID);
                pl.saveEx(trxName);
            }
        }
        final String sql = "SELECT M_Locator_ID FROM KJS_ProductionPlanLine WHERE KJS_ProductionPlanLine_ID=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, this.KJS_ProductionPlanLine_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final MProduct finishedProduct = new MProduct(Env.getCtx(), this.M_Product_ID, trxName);
                final int M_Locator_ID = rs.getInt(1);
                final MProduction p = new MProduction(Env.getCtx(), this.M_Production_ID, trxName);
                final MProductionLine line = new MProductionLine(Env.getCtx(), 0, trxName);
                line.setAD_Org_ID(this.AD_Org_ID);
                line.setM_Product_ID(finishedProduct.get_ID());
                line.setM_Locator_ID(M_Locator_ID);
                line.setMovementQty(p.getProductionQty());
                line.setPlannedQty(p.getProductionQty());
                line.setIsEndProduct(true);
                line.setM_Production_ID(this.M_Production_ID);
                line.saveEx();
            }
        }
        catch (SQLException e) {
            this.log.log(Level.SEVERE, sql.toString(), (Throwable)e);
            return true;
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        return true;
    }
    
    protected Vector<String> getOISColumnNames() {
        final Vector<String> columnNames = new Vector<String>(10);
        columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
        columnNames.add(Msg.translate(Env.getCtx(), "M_Product_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "Qty"));
        columnNames.add(Msg.translate(Env.getCtx(), "M_AttributeSetInstance_ID"));
        return columnNames;
    }
    
    protected void configureMiniTable(final IMiniTable miniTable) {
        miniTable.setColumnClass(0, (Class)Boolean.class, false);
        miniTable.setColumnClass(1, (Class)String.class, true);
        miniTable.setColumnClass(2, (Class)BigDecimal.class, true);
        miniTable.setColumnClass(3, (Class)String.class, true);
        miniTable.autoSize();
    }
}
