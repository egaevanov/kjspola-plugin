package org.kjs.pola.process;

import java.util.logging.Level;

import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.kjs.pola.model.MRequisitionExt;
import org.kjs.pola.model.X_C_Contract;
import org.kjs.pola.model.X_C_ContractLine;

public class POLA_CreateCPRFromSOContractLine extends SvrProcess{

	private int p_C_DocType_ID = 0;
	private int p_C_ContractLine_ID = 0;
	private int p_M_Warehouse_ID = 0;
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		p_C_ContractLine_ID = getRecord_ID();
		
	}

	@Override
	protected String doIt() throws Exception {
		X_C_ContractLine conttractLine = new X_C_ContractLine(getCtx(), p_C_ContractLine_ID, get_TrxName());
		X_C_Contract contract  = new X_C_Contract(getCtx(), conttractLine.getC_Contract_ID(), get_TrxName());
				
		if(!contract.getDocStatus().toUpperCase().equalsIgnoreCase("CO")) {
			
			return "Process Failed, Contract Document Status Not Complete";
		}

		if (conttractLine != null) {
			
			MRequisition requistion = new MRequisitionExt(getCtx(), 0, get_TrxName());
			
			requistion.setAD_Org_ID(conttractLine.getAD_Org_ID());
			requistion.setAD_User_ID(Env.getAD_User_ID(getCtx()));
			requistion.setC_DocType_ID(p_C_DocType_ID);
			requistion.setDescription("Auto Generate From: SO Contract->"+contract.getDocumentNo()+"  "+" LineNo -> "+conttractLine.getLineNo());
			requistion.setDateRequired(contract.getDateOrdered());
			requistion.setDateDoc(contract.getDateOrdered());
			
			
			if(p_M_Warehouse_ID <= 0) {
				StringBuilder SQLGetWHOrg = new StringBuilder();
				SQLGetWHOrg.append("SELECT M_Warehouse_ID");
				SQLGetWHOrg.append(" FROM AD_OrgInfo");
				SQLGetWHOrg.append(" WHERE AD_Client_ID = "+Env.getAD_Client_ID(getCtx()));
				SQLGetWHOrg.append(" AND AD_Org_ID = "+contract.getAD_Org_ID());

				p_M_Warehouse_ID = DB.getSQLValueEx(get_TrxName(), SQLGetWHOrg.toString());
				
				if(p_M_Warehouse_ID <= 0) {
					
					return "Warehouse not set, please check parameter or default warehouse of organization";
				}
				
			}
			requistion.setM_Warehouse_ID(p_M_Warehouse_ID);
			requistion.setM_PriceList_ID(contract.getM_PriceList_ID());
			
			requistion.saveEx();
			
			MRequisitionLine RLine = new MRequisitionLine(getCtx(), 0, get_TrxName());
			
			RLine.setAD_Org_ID(requistion.getAD_Org_ID());
			RLine.setLine(10);
			RLine.setM_Requisition_ID(requistion.getM_Requisition_ID());
			RLine.setC_BPartner_ID(contract.getC_BPartner_ID());
			RLine.setM_Product_ID(conttractLine.getM_Product_ID());
			RLine.setC_UOM_ID(conttractLine.getC_UOM_ID());
			RLine.setQty(conttractLine.getQtyEntered());
			RLine.setPriceActual(conttractLine.getPriceEntered());
			RLine.setDescription(conttractLine.getDescription());
			RLine.saveEx();
			
		}
		
		return "";
	}

}
