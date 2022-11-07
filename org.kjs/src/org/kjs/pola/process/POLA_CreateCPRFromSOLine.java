package org.kjs.pola.process;

import java.util.logging.Level;

import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.kjs.pola.model.MRequisitionExt;

public class POLA_CreateCPRFromSOLine extends SvrProcess{

	private int p_C_DocType_ID = 0;
	private MOrderLine OrdLine = null;
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		OrdLine = new MOrderLine(getCtx(), getRecord_ID(), get_TrxName());
		
	}

	@Override
	protected String doIt() throws Exception {
		
		MOrder ord  = new MOrder(getCtx(), OrdLine.getC_Order_ID(), get_TrxName());

		if (OrdLine != null) {
			
			MRequisition requistion = new MRequisitionExt(getCtx(), 0, get_TrxName());
			
			requistion.setAD_Org_ID(OrdLine.getAD_Org_ID());
			requistion.setAD_User_ID(Env.getAD_User_ID(getCtx()));
			requistion.setC_DocType_ID(p_C_DocType_ID);
			requistion.setDescription("Auto Generate : Sales Order->"+ord.getDocumentNo()+"  "+" LineNo -> "+OrdLine.getLine());
			requistion.setDateRequired(ord.getDatePromised());
			requistion.setDateDoc(ord.getDateAcct());
			requistion.setM_Warehouse_ID(ord.getM_Warehouse_ID());
			requistion.setM_PriceList_ID(ord.getM_PriceList_ID());
			
			requistion.saveEx();
			
			MRequisitionLine RLine = new MRequisitionLine(getCtx(), 0, get_TrxName());
			
			RLine.setAD_Org_ID(requistion.getAD_Org_ID());
			RLine.setLine(10);
			RLine.setM_Requisition_ID(requistion.getM_Requisition_ID());
			RLine.setC_BPartner_ID(ord.getC_BPartner_ID());
			RLine.setM_Product_ID(OrdLine.getM_Product_ID());
			RLine.setC_UOM_ID(OrdLine.getC_UOM_ID());
			RLine.setQty(OrdLine.getQtyEntered());
			RLine.setPriceActual(OrdLine.getPriceActual());
			RLine.setDescription(OrdLine.getDescription());
			RLine.saveEx();
			
		}
		
		return "";
	}

}
