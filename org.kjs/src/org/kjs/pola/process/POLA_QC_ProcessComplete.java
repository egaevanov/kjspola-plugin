package org.kjs.pola.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.compiere.model.MProduct;
import org.compiere.model.MProduction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.kjs.pola.model.X_KJS_ProductionPlan;
import org.kjs.pola.model.X_KJS_ProductionPlanLine;
import org.kjs.pola.model.X_KJS_QC;
import org.kjs.pola.model.X_M_Alternate;

public class POLA_QC_ProcessComplete extends SvrProcess{
	
	private X_KJS_QC QC = null;
	private MProduction LHP = null;
	private X_KJS_ProductionPlan JOB = null;
	private X_KJS_ProductionPlanLine JOBPHASE = null;

	@Override
	protected void prepare() {
	
		final ProcessInfoParameter[] para = this.getParameter();
		for (int i = 0; i < para.length; ++i) {
		 
//		final String name = para[i].getParameterName();
//         if (name.equals("QtyProcess")) {
////        	 p_QtyProcess = para[i].getParameterAsBigDecimal();
//         }else {
//            this.log.log(Level.SEVERE, "Unknown Parameter: " + name);
//            
//         }
    }
		
		
		QC = new X_KJS_QC(getCtx(), getRecord_ID(), get_TrxName());
		LHP = new MProduction(getCtx(), QC.getM_Production_ID(), get_TrxName());
		JOB = new X_KJS_ProductionPlan(getCtx(), (int) LHP.get_Value("KJS_ProductionPlan_ID"), get_TrxName());
		JOBPHASE = new X_KJS_ProductionPlanLine(getCtx(), (int) LHP.get_Value("KJS_ProductionPlanLine_ID"), get_TrxName());
		
	}

	@Override
	protected String doIt() throws Exception {
		
		if(QC.getKJS_Status().toUpperCase().equals("R")) {
			
			StringBuilder SQLGetPhaseNext = new StringBuilder();
			
			SQLGetPhaseNext.append("SELECT KJS_ProductionPlanLine_ID");
			SQLGetPhaseNext.append(" FROM KJS_ProductionPlanLine");
			SQLGetPhaseNext.append(" WHERE KJS_ProductionPlan_ID = ?");
			SQLGetPhaseNext.append(" AND Line > "+JOBPHASE.getLine());
			
			
			 PreparedStatement pstmt = null;
			    ResultSet rs = null;
			    	try {
			            pstmt = (PreparedStatement)DB.prepareStatement(SQLGetPhaseNext.toString(), (String)null);
		                pstmt.setInt(1, JOB.getKJS_ProductionPlan_ID());

			            rs = pstmt.executeQuery();
			            while (rs.next()) {
			            	
			            	X_KJS_ProductionPlanLine nextPhase = new X_KJS_ProductionPlanLine(getCtx(), rs.getInt(1), get_TrxName());
			        		MProduct prodKJPSPP = new MProduct(getCtx(), JOB.getM_Product_ID(), get_TrxName());
			        		MProduct prodNextPhase = new MProduct(getCtx(), nextPhase.getM_Product_ID(), get_TrxName());
			        		MProduct prodCurPhase = new MProduct(getCtx(), JOBPHASE.getM_Product_ID(), get_TrxName());

			        		X_M_Alternate alternate = new X_M_Alternate(getCtx(), (int) JOB.get_Value("M_Alternate_ID"), get_TrxName());

			        		
			        		if(alternate.get_Value("AlternateType").toString().toUpperCase().equals("OF")) {
			        		
				            	if(nextPhase.getM_Product_ID() == JOB.getM_Product_ID() || prodNextPhase.getValue().equals(prodKJPSPP.getValue()+"-BLC")) {
				            		
				            		BigDecimal current = nextPhase.getBOMQty();
				            		BigDecimal reject = (BigDecimal) QC.get_Value("QtyEntered");
				            		BigDecimal kons = (BigDecimal) JOBPHASE.get_Value("QtySheet");
				            		BigDecimal rslt = current.subtract(reject.multiply(kons));
				            		nextPhase.setBOMQty(rslt);
				            		
				            	}else {
				            		BigDecimal current = nextPhase.getBOMQty();
				            		BigDecimal reject = (BigDecimal) QC.get_Value("QtyEntered"); 
				            		BigDecimal rslt = current.subtract(reject);
				            		nextPhase.setBOMQty(rslt);
				            	}
			        		}else if(alternate.get_Value("AlternateType").toString().toUpperCase().equals("FL-RCF")) {
			        			
			        			
			        			BigDecimal gearUp = alternate.getKJS_CavityGearFeed();
		            			BigDecimal gearCons = (BigDecimal) alternate.get_Value("GearConstanta");
		            			BigDecimal multiply = gearUp.multiply(gearCons);
			        			
			        			if(nextPhase.getM_Product_ID() == JOB.getM_Product_ID() || prodNextPhase.getValue().equals(prodKJPSPP.getValue()+"-BLC")) {
				            		
				            		BigDecimal current = nextPhase.getBOMQty();
				            		BigDecimal reject = (BigDecimal) QC.get_Value("QtyEntered");
				            		BigDecimal kons = (BigDecimal) JOBPHASE.get_Value("QtySheet");
				            		BigDecimal BHP = reject.divide(multiply,2,RoundingMode.HALF_UP);
				            		BigDecimal rslt = current.subtract(BHP.multiply(kons));
				            		nextPhase.setBOMQty(rslt);
				            		
				            	}else if(prodCurPhase.getValue().equals(prodKJPSPP.getValue()+"-RCF")) {
				            		
				            		BigDecimal current = nextPhase.getBOMQty();
				            		BigDecimal reject = (BigDecimal) QC.get_Value("QtyEntered");
			            			BigDecimal result = current.subtract(reject.divide(multiply,2,RoundingMode.HALF_UP));
			            			
				            		nextPhase.setBOMQty(result);
				            		
				            	}
			        			
			        			
			        		}else if(alternate.get_Value("AlternateType").toString().toUpperCase().equals("FL-RCS")) {
			        			
			        			
			        			
			        		}else if(alternate.get_Value("AlternateType").toString().toUpperCase().equals("FL-PB")) {
			        			
			        			BigDecimal gearUp = alternate.getKJS_CavityGearFeed();
		            			BigDecimal gearCons = (BigDecimal) alternate.get_Value("GearConstanta");
		            			BigDecimal multiply = gearUp.multiply(gearCons);
			        			
			        			if(prodCurPhase.getValue().equals(prodKJPSPP.getValue()+"-RCF")) {
				            		
				            		BigDecimal current = nextPhase.getBOMQty();
				            		BigDecimal reject = (BigDecimal) QC.get_Value("QtyEntered");
			            			BigDecimal result = current.subtract(reject.divide(multiply,2,RoundingMode.HALF_UP));
				            		nextPhase.setBOMQty(result);
				            		
				            	}
			        			
			        		}
			        		
			            	nextPhase.saveEx();
			            }
			        }
			        catch (SQLException err) {
			            this.log.log(Level.SEVERE, SQLGetPhaseNext.toString(), (Throwable)err);
			            rollback();
			        }
			        finally {
			            DB.close(rs, (Statement)pstmt);
			            rs = null;
			            pstmt = null;
			        }
			
			
		}
		
		return "";
	}

}
