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
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.kjs.pola.model.X_KJS_ProductionPlanLineBOM;

public class CreateFromProductionPlanLine extends CreateFrom
{
    protected int M_Product_ID;
    protected int AD_Org_ID;
    protected int KJS_ProductionPlan_ID;
    protected String BOMType;
    
    public CreateFromProductionPlanLine(final GridTab gridTab) {
        super(gridTab);
        this.M_Product_ID = 0;
        this.AD_Org_ID = 0;
        this.KJS_ProductionPlan_ID = 0;
    }
    
    public Object getWindow() {
        return null;
    }
    
    public boolean dynInit() throws Exception {
        this.M_Product_ID = (int)this.getGridTab().getValue("M_Product_ID");
        this.AD_Org_ID = (int)this.getGridTab().getValue("AD_Org_ID");
        this.KJS_ProductionPlan_ID = (int)this.getGridTab().getValue("KJS_ProductionPlan_ID");
        final String check = "SELECT BOMType FROM KJS_ProductionPlan WHERE KJS_ProductionPlan_ID=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(check.toString(), (String)null);
            pstmt.setInt(1, this.KJS_ProductionPlan_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.BOMType = rs.getString(1);
            }
        }
        catch (SQLException e) {
            this.log.log(Level.SEVERE, check.toString(), (Throwable)e);
            return false;
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        return false;
    }
    
    public void info(final IMiniTable miniTable, final IStatusBar statusBar) {
    }
    
    protected Vector<Vector<Object>> getBOMData(final int M_Product_ID) {
        final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        final StringBuilder sql = new StringBuilder("SELECT pb.Line,pb.M_ProductBOM_ID,prod2.name,pb.BOMQty FROM M_Product_BOM pb JOIN M_Product prod ON prod.M_Product_ID=pb.M_Product_ID LEFT JOIN M_Product prod2 ON prod2.M_Product_ID=pb.M_ProductBOM_ID WHERE prod.M_Product_ID=? and pb.BOMType=? ORDER BY pb.Line");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, M_Product_ID);
            pstmt.setString(2, this.BOMType);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final Vector<Object> line = new Vector<Object>();
                line.add(new Boolean(false));
                line.add(rs.getInt(1));
                final KeyNamePair pp = new KeyNamePair(rs.getInt(2), rs.getString(3));
                line.add(pp);
                line.add(rs.getBigDecimal(4));
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
        final int KJS_ProductionPlanLine_ID = (int)this.getGridTab().getValue("KJS_ProductionPlanLine_ID");
        for (int i = 0; i < miniTable.getRowCount(); ++i) {
            if ((boolean) miniTable.getValueAt(i, 0)) {
                final X_KJS_ProductionPlanLineBOM pplb = new X_KJS_ProductionPlanLineBOM(Env.getCtx(), 0, trxName);
                final int Line = (int)miniTable.getValueAt(i, 1);
                final KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(i, 2);
                final int M_ProductBOM_ID = pp.getKey();
                final BigDecimal Qty = (BigDecimal)miniTable.getValueAt(i, 3);
                pplb.setAD_Org_ID(this.AD_Org_ID);
                pplb.setKJS_ProductionPlanLine_ID(KJS_ProductionPlanLine_ID);
                pplb.setLine(Line);
                pplb.setM_Product_ID(M_ProductBOM_ID);
                pplb.setQty(Qty);
                pplb.saveEx(trxName);
            }
        }
        return true;
    }
    
    protected Vector<String> getOISColumnNames() {
        final Vector<String> columnNames = new Vector<String>(10);
        columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
        columnNames.add(Msg.translate(Env.getCtx(), "Line"));
        columnNames.add(Msg.translate(Env.getCtx(), "M_ProductBOM_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "Qty"));
        return columnNames;
    }
    
    protected void configureMiniTable(final IMiniTable miniTable) {
        miniTable.setColumnClass(0, (Class)Boolean.class, false);
        miniTable.setColumnClass(1, (Class)String.class, true);
        miniTable.setColumnClass(2, (Class)String.class, true);
        miniTable.setColumnClass(3, (Class)BigDecimal.class, true);
        miniTable.autoSize();
    }
}
