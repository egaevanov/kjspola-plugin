package org.kjs.pola.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.compiere.model.MProduction;
import org.compiere.model.MProductionLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.kjs.pola.model.X_KJS_ProductionPlan;
import org.kjs.pola.model.X_KJS_ProductionPlanLine;

public class POLA_JOBPHASE_CreateProduction extends SvrProcess {
	
	
	private BigDecimal p_QtyProcess = Env.ZERO;
	private int p_KJS_ProductionPlanLine_ID = 0;
	private int p_KJS_ProductionPlan_ID = 0;
	private MProduction LHP = null;
	private int p_M_Production_ID = 0;

	@Override
	protected void prepare() {
	
		final ProcessInfoParameter[] para = this.getParameter();
			for (int i = 0; i < para.length; ++i) {
			 
			final String name = para[i].getParameterName();
	         if (name.equals("QtyProcess")) {
	        	 p_QtyProcess = para[i].getParameterAsBigDecimal();
	         }else {
	            this.log.log(Level.SEVERE, "Unknown Parameter: " + name);
	            
	         }
	    }
			
			LHP = new MProduction(getCtx(), getRecord_ID(), get_TrxName());
			p_KJS_ProductionPlan_ID = (int) LHP.get_Value("KJS_ProductionPlan_ID");
			p_KJS_ProductionPlanLine_ID = (int) LHP.get_Value("KJS_ProductionPlanLine_ID");
			
			p_M_Production_ID = getRecord_ID();

	}

	@Override
	protected String doIt() throws Exception {
		X_KJS_ProductionPlanLine JobPhase = new X_KJS_ProductionPlanLine(getCtx(), p_KJS_ProductionPlanLine_ID, get_TrxName());
		X_KJS_ProductionPlan JobParent = new X_KJS_ProductionPlan(getCtx(), p_KJS_ProductionPlan_ID, get_TrxName());
		
		MProduction production = new MProduction(getCtx(), p_M_Production_ID, get_TrxName());
		
//		production.setAD_Org_ID(JobPhase.getAD_Org_ID());
////		production.set_CustomColumn("KJS_ProductionPlan_ID", JobPhase.getKJS_ProductionPlan_ID());
////		production.set_CustomColumn("KJS_ProductionPlanLine_ID", JobPhase.getKJS_ProductionPlanLine_ID());
//		production.set_CustomColumn("KJS_ProductAsset_ID" , JobPhase.getKJS_ProductAsset_ID());
//		production.setMovementDate(JobParent.getDateDoc());
//		production.setDatePromised(JobParent.getDateDoc());
//		production.setDescription((String) JobParent.get_Value("Description"));
//		production.setM_Product_ID(JobPhase.getM_Product_ID());
//		production.setM_Locator_ID(JobPhase.getM_Locator_ID());
		production.setProductionQty(p_QtyProcess);
////		production.setC_OrderLine_ID(JobPhase.getC_OrderLine_ID());
////		production.setC_BPartner_ID(prodPlanning.getC_BPartner_ID());
//		production.setDocStatus("DR");
//		production.setIsCreated("Y");
		production.saveEx();
		
		int line  = 10;
		
		MProductionLine productionLine = new MProductionLine(getCtx(), 0, get_TrxName());
		productionLine.setAD_Org_ID(production.getAD_Org_ID());
		productionLine.setM_Production_ID(production.getM_Production_ID());
		productionLine.setLine(line);
		productionLine.setM_Product_ID(production.getM_Product_ID());
		productionLine.setIsActive(true);
		productionLine.setPlannedQty(production.getProductionQty());
		productionLine.setM_Locator_ID(production.getM_Locator_ID());
		productionLine.setDescription(production.getDescription());
		productionLine.saveEx();
		
		
		StringBuilder SQLUpdate = new StringBuilder();
		SQLUpdate.append("UPDATE M_ProductionLine");
		SQLUpdate.append(" SET MovementQty = "+production.getProductionQty());
		SQLUpdate.append(" ,IsEndProduct = 'Y'");
		SQLUpdate.append(" WHERE M_ProductionLine_ID = "+productionLine.getM_ProductionLine_ID());
		DB.executeUpdate(SQLUpdate.toString(), true, get_TrxName());
		
//        final StringBuilder sqlBOM = new StringBuilder("SELECT prod.M_Product_ID,prod.Value,pplb.Qty,asi.M_AttributeSetInstance_ID,asi.Description FROM KJS_ProductionPlanLineBOM pplb JOIN M_Product prod ON pplb.M_Product_ID=prod.M_Product_ID LEFT JOIN M_AttributeSetInstance asi ON pplb.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID WHERE KJS_ProductionPlanLine_ID=?");

		StringBuilder SQLGetBOM = new StringBuilder();
		SQLGetBOM.append("SELECT pplb.M_ProductBOM_ID ,prod.value,pplb.BOMqty");
		SQLGetBOM.append(" FROM M_Product_BOM pplb");
		SQLGetBOM.append(" JOIN M_Product prod ON pplb.M_Product_ID=prod.M_Product_ID ");
		SQLGetBOM.append(" WHERE prod.M_product_id = ?");
		SQLGetBOM.append(" AND pplb.M_ALternate_ID = ?");

        PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    	try {
	            pstmt = (PreparedStatement)DB.prepareStatement(SQLGetBOM.toString(), (String)null);
                pstmt.setInt(1, JobPhase.getM_Product_ID());
                pstmt.setInt(2, JobParent.get_ValueAsInt("M_Alternate_ID"));

	            rs = pstmt.executeQuery();
	            while (rs.next()) {
	            	
	            	line = line+10;
	                final MProductionLine pl = new MProductionLine(Env.getCtx(), 0, this.get_TrxName());
                    final int M_ProductBOM_ID = rs.getInt(1);
                    final BigDecimal Qty = rs.getBigDecimal(3).multiply(production.getProductionQty());
//                    final int M_AttributeSetInstance_ID = rs.getInt(4);
                    pl.setAD_Org_ID(production.getAD_Org_ID());
                    pl.setM_Product_ID(M_ProductBOM_ID);
                    pl.setM_Locator_ID(production.getM_Locator_ID());
                    pl.setPlannedQty(Qty);
                    pl.setQtyUsed(Qty);
//                    pl.setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
                    pl.setM_Production_ID(production.getM_Production_ID());
                    pl.setLine(line);
                    pl.saveEx(this.get_TrxName());
	            	
	            }
	        }
	        catch (SQLException err) {
	            this.log.log(Level.SEVERE, SQLGetBOM.toString(), (Throwable)err);
	            rollback();
	        }
	        finally {
	            DB.close(rs, (Statement)pstmt);
	            rs = null;
	            pstmt = null;
	        }
		
	    	
	    	production.setIsCreated("Y");
	    	production.saveEx();
		
	    	
	    BigDecimal Current = (BigDecimal) JobPhase.get_Value("QtyToDeliver");	
	    	
	    JobPhase.set_CustomColumn("QtyToDeliver", Current.add(p_QtyProcess));
	    JobPhase.saveEx();
		
		return null;
	}

}
