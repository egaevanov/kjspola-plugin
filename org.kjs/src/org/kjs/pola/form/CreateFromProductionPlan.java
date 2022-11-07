package org.kjs.pola.form;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
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
import org.kjs.pola.model.X_KJS_ProductionPlanLine;

public class CreateFromProductionPlan extends CreateFrom
{
    protected int M_Product_ID;
    protected int KJS_ProductionPlan_ID;
    protected int AD_Org_ID;
    protected BigDecimal QtyOrdered;
    protected BigDecimal Capacity;
    
    public CreateFromProductionPlan(final GridTab gridTab) {
        super(gridTab);
        this.M_Product_ID = 0;
        this.KJS_ProductionPlan_ID = 0;
        this.AD_Org_ID = 0;
        this.QtyOrdered = null;
        this.Capacity = null;
    }
    
    public Object getWindow() {
        return null;
    }
    
    public boolean dynInit() throws Exception {
        this.M_Product_ID = (int)this.getGridTab().getValue("M_Product_ID");
        this.KJS_ProductionPlan_ID = (int)this.getGridTab().getValue("KJS_ProductionPlan_ID");
        this.AD_Org_ID = (int)this.getGridTab().getValue("AD_Org_ID");
        this.QtyOrdered = (BigDecimal)this.getGridTab().getValue("QtyOrdered");
        return true;
    }
    
    public void info(final IMiniTable miniTable, final IStatusBar statusBar) {
    }
    
    protected Vector<Vector<Object>> getProductPhaseData(final int M_Product_ID) {
        final Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        final StringBuilder sql = new StringBuilder("SELECT ppl.Line, p.KJS_Phase_ID, p.Name as PhaseName, prod.M_Product_ID, prod.Name, ppl.Duration FROM KJS_ProductPhase pp, KJS_ProductPhaseLine ppl, KJS_Phase p, M_Product prod WHERE pp.KJS_ProductPhase_ID=ppl.KJS_ProductPhase_ID AND ppl.KJS_Phase_ID=p.KJS_Phase_ID AND ppl.M_Product_ID=prod.M_Product_ID AND p.KJS_Phase_ID NOT IN (SELECT KJS_Phase_ID FROM KJS_ProductionPlanLine WHERE KJS_ProductionPlan_ID=?) AND pp.M_Product_ID=?");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
            pstmt.setInt(1, this.KJS_ProductionPlan_ID);
            pstmt.setInt(2, M_Product_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final Vector<Object> line = new Vector<Object>();
                line.add(new Boolean(false));
                line.add(rs.getInt(1));
                KeyNamePair pp = new KeyNamePair(rs.getInt(2), rs.getString(3));
                line.add(pp);
                pp = new KeyNamePair(rs.getInt(4), rs.getString(5));
                line.add(pp);
                line.add(rs.getBigDecimal(6));
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
        final int KJS_ProductionPlan_ID = (int)this.getGridTab().getValue("KJS_ProductionPlan_ID");
        for (int i = 0; i < miniTable.getRowCount(); ++i) {
            if ((boolean) miniTable.getValueAt(i, 0)) {
                final X_KJS_ProductionPlanLine ppline = new X_KJS_ProductionPlanLine(Env.getCtx(), 0, trxName);
                final int Line = (int)miniTable.getValueAt(i, 1);
                KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(i, 2);
                final int KJS_Phase_ID = pp.getKey();
                pp = (KeyNamePair)miniTable.getValueAt(i, 3);
                final int M_Product_WIP = pp.getKey();
                final String sql = "SELECT KJS_ProductAsset_ID,KJS_Capacity FROM KJS_ProductAsset WHERE KJS_Phase_ID=? LIMIT 1";
                final String calculate = "SELECT ShelfWidth, ShelfHeight, ShelfDepth FROM M_Product WHERE M_Product_ID=?";
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                PreparedStatement pstmt2 = null;
                ResultSet rs2 = null;
                Label_0524: {
                    try {
                        pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), (String)null);
                        pstmt.setInt(1, KJS_Phase_ID);
                        rs = pstmt.executeQuery();
                        while (rs.next()) {
                            ppline.setKJS_ProductAsset_ID(rs.getInt(1));
                            this.Capacity = rs.getBigDecimal(2);
                        }
                        pstmt2 = (PreparedStatement)DB.prepareStatement(calculate.toString(), (String)null);
                        pstmt2.setInt(1, M_Product_WIP);
                        rs2 = pstmt2.executeQuery();
                        while (rs2.next()) {
                            final double JumlahKawat = rs2.getBigDecimal(1).doubleValue();
                            final double Per = rs2.getBigDecimal(2).doubleValue();
                            final double Core = rs2.getBigDecimal(3).doubleValue();
                            final double JumlahProses = JumlahKawat * Per * Core * this.QtyOrdered.doubleValue() / this.Capacity.doubleValue() * 24.0 * 60.0;
                            ppline.set_ValueOfColumn("KJS_JumlahProses", (Object)new BigDecimal(JumlahProses).setScale(0, RoundingMode.HALF_UP));
                            final String lastEstEndDate = "SELECT KJS_EstEndDate FROM KJS_ProductionPlanLine WHERE Line=?";
                            PreparedStatement pstmt3 = null;
                            ResultSet rs3 = null;
                            pstmt3 = (PreparedStatement)DB.prepareStatement(lastEstEndDate.toString(), (String)null);
                            pstmt3.setInt(1, Line);
                            rs3 = pstmt3.executeQuery();
                            while (rs3.next()) {
                                final Calendar cal = Calendar.getInstance();
                                cal.setTime(new Date(System.currentTimeMillis()));
                                cal.add(12, (int)JumlahProses);
                                ppline.setKJS_EstStartDate(new Timestamp(System.currentTimeMillis()));
                                ppline.setKJS_EstEndDate(new Timestamp(cal.getTimeInMillis()));
                            }
                        }
                    }
                    catch (SQLException e) {
                        this.log.log(Level.SEVERE, sql.toString(), (Throwable)e);
                        break Label_0524;
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
                ppline.setAD_Org_ID(this.AD_Org_ID);
                ppline.setLine(Line);
                ppline.setKJS_Phase_ID(KJS_Phase_ID);
                ppline.setM_Product_ID(M_Product_WIP);
                ppline.setKJS_ProductionPlan_ID(KJS_ProductionPlan_ID);
                ppline.saveEx(trxName);
            }
        }
        return true;
    }
    
    protected Vector<String> getOISColumnNames() {
        final Vector<String> columnNames = new Vector<String>(10);
        columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
        columnNames.add(Msg.translate(Env.getCtx(), "Line"));
        columnNames.add(Msg.translate(Env.getCtx(), "Phase"));
        columnNames.add(Msg.translate(Env.getCtx(), "Product WIP"));
        columnNames.add(Msg.translate(Env.getCtx(), "Duration"));
        return columnNames;
    }
    
    protected void configureMiniTable(final IMiniTable miniTable) {
        miniTable.setColumnClass(0, (Class)Boolean.class, false);
        miniTable.setColumnClass(1, (Class)String.class, true);
        miniTable.setColumnClass(2, (Class)String.class, true);
        miniTable.setColumnClass(3, (Class)String.class, true);
        miniTable.setColumnClass(4, (Class)BigDecimal.class, true);
        miniTable.autoSize();
    }
}
