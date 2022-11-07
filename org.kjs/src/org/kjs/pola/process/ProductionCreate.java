package org.kjs.pola.process;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import org.compiere.util.DB;
import org.compiere.model.MProductionLine;
import org.compiere.util.Env;
import org.compiere.util.AdempiereUserError;
import org.compiere.process.ProcessInfoParameter;
import java.util.logging.Level;
import java.math.BigDecimal;
import org.kjs.pola.model.MProductionExt;
import org.compiere.process.SvrProcess;

public class ProductionCreate extends SvrProcess
{
    private int p_M_Production_ID;
    private MProductionExt m_production;
    private boolean recreate;
    private BigDecimal newQty;
    
    public ProductionCreate() {
        this.p_M_Production_ID = 0;
        this.m_production = null;
        this.recreate = false;
        this.newQty = null;
    }
    
    protected void prepare() {
        final ProcessInfoParameter[] para = this.getParameter();
        for (int i = 0; i < para.length; ++i) {
            final String name = para[i].getParameterName();
            if ("Recreate".equals(name)) {
                this.recreate = "Y".equals(para[i].getParameter());
            }
            else if ("ProductionQty".equals(name)) {
                this.newQty = (BigDecimal)para[i].getParameter();
            }
            else {
                this.log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
        }
        this.p_M_Production_ID = this.getRecord_ID();
        this.m_production = new MProductionExt(this.getCtx(), this.p_M_Production_ID, this.get_TrxName());
    }
    
    protected String doIt() throws Exception {
        if (this.m_production.get_ID() == 0) {
            throw new AdempiereUserError("Could not load production header");
        }
        if (this.m_production.isProcessed()) {
            return "Already processed";
        }
        return this.createLines();
    }
    
    protected String createLines() throws Exception {
        final int KJS_ProductionPlanLine_ID = this.m_production.get_ValueAsInt("KJS_ProductionPlanLine_ID");
        this.m_production.deleteLines(this.get_TrxName());
        if (!this.recreate && "Y".equalsIgnoreCase(this.m_production.getIsCreated())) {
            throw new AdempiereUserError("Production already created.");
        }
        if (this.newQty != null) {
            this.m_production.setProductionQty(this.newQty);
        }
        final MProductionLine line = new MProductionLine(Env.getCtx(), 0, this.get_TrxName());
        line.setAD_Org_ID(this.m_production.getAD_Org_ID());
        line.setM_Product_ID(this.m_production.getM_Product_ID());
        line.setM_Locator_ID(this.m_production.getM_Locator_ID());
        line.setMovementQty(this.m_production.getProductionQty());
        line.setPlannedQty(this.m_production.getProductionQty());
        line.setIsEndProduct(true);
        line.setM_Production_ID(this.m_production.getM_Production_ID());
        line.setLine(10);
        line.saveEx(this.get_TrxName());
        final StringBuilder sqlBOM = new StringBuilder("SELECT prod.M_Product_ID,prod.Value,pplb.Qty,asi.M_AttributeSetInstance_ID,asi.Description FROM KJS_ProductionPlanLineBOM pplb JOIN M_Product prod ON pplb.M_Product_ID=prod.M_Product_ID LEFT JOIN M_AttributeSetInstance asi ON pplb.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID WHERE KJS_ProductionPlanLine_ID=?");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Label_0456: {
            try {
                pstmt = (PreparedStatement)DB.prepareStatement(sqlBOM.toString(), (String)null);
                pstmt.setInt(1, KJS_ProductionPlanLine_ID);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    final MProductionLine pl = new MProductionLine(Env.getCtx(), 0, this.get_TrxName());
                    final int M_ProductBOM_ID = rs.getInt(1);
                    final BigDecimal Qty = rs.getBigDecimal(3).multiply(this.m_production.getProductionQty());
                    final int M_AttributeSetInstance_ID = rs.getInt(4);
                    pl.setAD_Org_ID(this.m_production.getAD_Org_ID());
                    pl.setM_Product_ID(M_ProductBOM_ID);
                    pl.setM_Locator_ID(this.m_production.getM_Locator_ID());
                    pl.setPlannedQty(Qty);
                    pl.setQtyUsed(Qty);
                    pl.setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
                    pl.setM_Production_ID(this.m_production.getM_Production_ID());
                    final String getLine = "SELECT COALESCE(MAX(Line),0)+10 FROM M_ProductionLine WHERE M_Production_ID=?";
                    final int ii = DB.getSQLValue(this.get_TrxName(), getLine, this.m_production.getM_Production_ID());
                    pl.setLine(ii);
                    pl.saveEx(this.get_TrxName());
                }
            }
            catch (SQLException e) {
                this.log.log(Level.SEVERE, sqlBOM.toString(), (Throwable)e);
                break Label_0456;
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
        final String UpdateEndProduct = "UPDATE M_ProductionLine SET IsEndProduct='Y' WHERE Line=10 AND M_Production_ID=?";
        DB.executeUpdate(UpdateEndProduct, this.m_production.getM_Production_ID(), this.get_TrxName());
        this.m_production.setIsCreated("Y");
        this.m_production.save(this.get_TrxName());
        return "";
    }
}