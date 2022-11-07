package org.kjs.pola.process;

import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.model.MLocator;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;

public class POLA_ProcessCreateInbound extends SvrProcess {

	private int p_C_DocType_ID = 0;
	private int p_DD_Order_ID = 0;
	
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DD_Order_ID"))
				p_DD_Order_ID = para[i].getParameterAsInt();
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	protected String doIt() throws Exception {
		
		//Validate DD_Order_ID
		if (p_DD_Order_ID <= 0) {
			return "Error: No Selected Inter-warehouse Document";
		}
				
		//Validate status of InterWarehouse Movement= CO
		MDDOrder interWH = new MDDOrder(getCtx(), p_DD_Order_ID, get_TrxName());
		if (!interWH.getDocStatus().equals(DocAction.ACTION_Complete)) {
			return "Error: Only Completed Inter-warehouse Document Can be Processed";
		}
		
		//only allow running process for role with access to warehouse destination
//		boolean match = new Query(getCtx(), X_AD_Role_WHAccess.Table_Name, "AD_Role_ID=? AND M_Warehouse_ID=?", get_TrxName())
//						.setParameters(Env.getContextAsInt(getCtx(), Env.AD_ROLE_ID), p_M_Warehouse_ID)
//						.setOnlyActiveRecords(true)
//						.match();
//		
//		if (!match) {
//			return "Error: User Role Does Not Access to Warehouse Destination";
//		}
		
		//Check whether an existing inbound is already exists
		
		//Check whether an existing inbound is already exists
		StringBuilder SQLGetOutbound = new StringBuilder();
		SQLGetOutbound.append("SELECT OutBound_ID");
		SQLGetOutbound.append(" FROM DD_Order");
		SQLGetOutbound.append(" WHERE DD_Order_ID ="+p_DD_Order_ID);
				
				
		int OutBound_ID = DB.getSQLValueEx(null, SQLGetOutbound.toString());
		
		
		if(OutBound_ID < 0 ) {
			return "Error: Must Create Outbound Movement Before Create Inbound";
		}
		
		
		//Validate DocType = Material Movement
		if (p_C_DocType_ID <= 0) {
			return "Error: No Document Type Selected for Inbound Movement";
		} else {
			MDocType docType = new MDocType(getCtx(), p_C_DocType_ID, get_TrxName());
			if (!docType.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialMovement)) {
				return "Error: Selected Document Type Is Not Material Movement";
			}
		}	
		
		
		//Validate Locator
		if (interWH.get_ValueAsInt("M_LocatorTo_ID") <= 0) {
			return "Error: No Destination Locator";
		} else {
			MLocator locator = new MLocator(getCtx(), interWH.get_ValueAsInt("M_LocatorTo_ID"), get_TrxName());
			if (locator.getM_Warehouse_ID()!= interWH.get_ValueAsInt("M_WarehouseTo_ID")) {
				return "Error: Selected Locator Is Not in The Destination Warehouse";
			}
		}
		
		//Create inbound movement
	
		
			MMovement inbound = new MMovement(getCtx(), 0, get_TrxName());
			inbound.setAD_Org_ID(interWH.getAD_Org_ID());
			inbound.setMovementDate(new Timestamp(System.currentTimeMillis()));
			inbound.setC_DocType_ID(p_C_DocType_ID);
			//
			StringBuilder SQLGetTransitWH = new StringBuilder();
			SQLGetTransitWH.append("SELECT Transit_Warehouse_ID FROM AD_OrgInfo WHERE AD_Org_ID=?");
			
			int Transit_Warehouse_ID = DB.getSQLValue(get_TrxName(), SQLGetTransitWH.toString(), interWH.getAD_Org_ID());
	//		MWarehouse WHTransit = new MWarehouse(getCtx(), Transit_Warehouse_ID, get_TrxName());
				
	//		MLocator LocTransit = WHTransit.getDefaultLocator();
			StringBuilder SQLGetTransitLoc = new StringBuilder();
			SQLGetTransitLoc.append("SELECT M_Locator_ID FROM M_Locator WHERE M_Warehouse_ID=? AND M_LocatorType_ID =(SELECT M_LocatorType_ID FROM M_LocatorType WHERE Name = 'Intransit')"); 
			int LocTransit = DB.getSQLValue(get_TrxName(), SQLGetTransitLoc.toString(), Transit_Warehouse_ID);
			
		
			inbound.setDocStatus(DocAction.STATUS_Drafted);
			inbound.setDocAction(DocAction.ACTION_Complete);
			inbound.setDD_Order_ID(interWH.getDD_Order_ID());
			
			inbound.saveEx();
			
			StringBuilder SQLGetTracking = new StringBuilder();
			SQLGetTracking.append("SELECT M_MovementTrack_ID");
			SQLGetTracking.append(" FROM M_MovementTrack");
			SQLGetTracking.append(" WHERE DD_Order_ID = ?");
			SQLGetTracking.append(" AND DD_OrderLine_ID =?");
			
			
			
			MDDOrderLine[] lines = interWH.getLines();	
			for (MDDOrderLine line : lines) {
				
		
			MMovementLine moveLine = new MMovementLine(inbound);
			moveLine.setLine(line.getLine());
			moveLine.setAD_Org_ID(interWH.getAD_Org_ID());
			moveLine.setM_Product_ID(line.getM_Product_ID());
			moveLine.setMovementQty(line.getQtyEntered());
			
//			moveLine.set_CustomColumn("M_Warehouse_ID", Transit_Warehouse_ID);
			moveLine.setM_Locator_ID(LocTransit);
//			moveLine.set_CustomColumn("M_WarehouseTo_ID", interWH.get_ValueAsInt("M_WarehouseTo_ID"));		
			moveLine.setM_LocatorTo_ID(interWH.get_ValueAsInt("M_LocatorTo_ID"));
				
			moveLine.setDD_OrderLine_ID(line.getDD_OrderLine_ID());
			moveLine.setM_AttributeSetInstance_ID(line.getM_AttributeSetInstance_ID());
			moveLine.setM_AttributeSetInstanceTo_ID(line.getM_AttributeSetInstance_ID());
	
			moveLine.saveEx();
							
		}
		
		inbound.processIt(MMovement.ACTION_Complete);
		if(inbound.save()) {
			
			interWH.set_ValueOfColumn("InBound_ID", inbound.getM_Movement_ID());
			interWH.setIsInTransit(false);
			interWH.setIsDelivered(true);
			interWH.saveEx();
		}
		
		String message = Msg.parseTranslation(getCtx(), "@GeneratedInbound@"+ inbound.getDocumentNo());
		addBufferLog(0, null, null, message, inbound.get_Table_ID(),inbound.getM_Movement_ID());

		
		return "";
	}

}
