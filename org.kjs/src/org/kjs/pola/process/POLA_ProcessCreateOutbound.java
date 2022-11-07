package org.kjs.pola.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MProduct;
import org.compiere.model.MStorageOnHand;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;

public class POLA_ProcessCreateOutbound extends SvrProcess {

	private int p_C_DocType_ID = 0;
	private int p_DD_Order_ID = 0;
	
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = para[i].getParameterAsInt();
			
			else if (name.equals("DD_Order_ID"))
				p_DD_Order_ID = para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}

	protected String doIt() throws Exception {
		
	String rs = "";	
		
	try {
		
		
		//Validate DD_Order_ID
		if (p_DD_Order_ID <= 0) {
			return "Error: No Selected Inter-warehouse Document";
		}
		
		//Validate status of InterWarehouse Movement= CO
		MDDOrder interWH = new MDDOrder(getCtx(), p_DD_Order_ID, get_TrxName());
		if (!interWH.getDocStatus().equals(DocAction.ACTION_Complete)) {
			return "Error: Only Completed Inter-warehouse Document Can be Processed";
		}
		
				
		//Check whether an existing inbound is already exists
		StringBuilder SQLGetOutbound = new StringBuilder();
		SQLGetOutbound.append("SELECT InBound_ID ");
		SQLGetOutbound.append(" FROM DD_Order");
		SQLGetOutbound.append(" WHERE DD_Order_ID ="+p_DD_Order_ID);
		
		
		int outbound_ID = DB.getSQLValueEx(null, SQLGetOutbound.toString());
		
		
		if (outbound_ID > 0) {
			return "Outbound Movement Has Been Created";
		}

		//Validate DocType = Material Movement
		if (p_C_DocType_ID <= 0) {
			return "Error: No Document Type Selected for Outbound Movement";
		} else {
			MDocType docType = new MDocType(getCtx(), p_C_DocType_ID, get_TrxName());
			if (!docType.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialMovement)) {
				return "Error: Selected Document Type Is Not Outbound Movement";
			}
		}
		
		//Create outbound movement
		MMovement outbound = new MMovement(getCtx(), 0, get_TrxName());		
		outbound.setAD_Org_ID(interWH.getAD_Org_ID());
		outbound.setMovementDate(new Timestamp(System.currentTimeMillis()));
		outbound.setDocAction(DocAction.ACTION_Complete);
		outbound.setDocStatus(DocAction.STATUS_Drafted);
		outbound.setC_DocType_ID(p_C_DocType_ID);
		outbound.setDD_Order_ID(interWH.getDD_Order_ID());
	
		outbound.saveEx();
		
		//Create outbound movement lines
				
		MDDOrderLine[] lines = interWH.getLines();	

		for (MDDOrderLine line : lines) {
			
			MMovementLine moveLine = new MMovementLine(getCtx(), 0, get_TrxName());
			moveLine.setLine(line.getLine());
			moveLine.setM_Product_ID(line.getM_Product_ID());
			moveLine.setMovementQty(line.getQtyEntered());
	
			if (moveLine.getM_Product_ID()> 0) {
				BigDecimal qtyOnHand = MStorageOnHand.getQtyOnHand(moveLine.getM_Product_ID(),interWH.getM_Warehouse_ID(), line.getM_AttributeSetInstance_ID(), get_TrxName());
				
				MProduct product = MProduct.get(getCtx(), moveLine.getM_Product_ID());
				
				if (qtyOnHand.compareTo(moveLine.getMovementQty()) < 0)
					return product.getName() + " Out Of Stock";
			}
				
				
			StringBuilder SQLGetTransitWH = new StringBuilder();
			SQLGetTransitWH.append("SELECT Transit_Warehouse_ID FROM AD_OrgInfo WHERE AD_Org_ID=?");
			
			int Transit_Warehouse_ID = DB.getSQLValue(get_TrxName(), SQLGetTransitWH.toString(), interWH.getAD_Org_ID());
				
			StringBuilder SQLGetTransitLoc = new StringBuilder();
			SQLGetTransitLoc.append("SELECT M_Locator_ID FROM M_Locator WHERE M_Warehouse_ID=? AND M_LocatorType_ID =(SELECT M_LocatorType_ID FROM M_LocatorType WHERE Name = 'Intransit')"); 
			int LocTransit = DB.getSQLValue(get_TrxName(), SQLGetTransitLoc.toString(), Transit_Warehouse_ID);
			
			moveLine.setAD_Org_ID(outbound.getAD_Org_ID());
			moveLine.setM_Movement_ID(outbound.getM_Movement_ID());
			moveLine.setM_Locator_ID(interWH.get_ValueAsInt("M_Locator_ID"));
			moveLine.setM_LocatorTo_ID(LocTransit);
			moveLine.setDD_OrderLine_ID(line.getDD_OrderLine_ID());
//			moveLine.setM_AttributeSetInstance_ID(line.getM_AttributeSetInstance_ID());
//			moveLine.setM_AttributeSetInstanceTo_ID(line.getM_AttributeSetInstance_ID());
			moveLine.saveEx();
			
		}
		
		//Complete movement
		outbound.processIt(DocAction.ACTION_Complete);
		if(outbound.save()) {
			
			interWH.set_ValueOfColumn("OutBound_ID", outbound.getM_Movement_ID());
			interWH.setIsInTransit(true);
			interWH.saveEx();
		}
		
		String message = Msg.parseTranslation(getCtx(), "@GeneratedOutbound@"+ outbound.getDocumentNo());
		addBufferLog(0, null, null, message, outbound.get_Table_ID(),outbound.getM_Movement_ID());
		
		rs ="";
	} catch (Exception e) {
		rs = "Error: Outbound Movement Not Created : "+ e;
		rollback();
	}
	return rs;

}

}