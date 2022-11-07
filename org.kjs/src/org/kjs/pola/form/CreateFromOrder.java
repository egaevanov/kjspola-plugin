package org.kjs.pola.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.apps.form.WCreateFromWindow;
import org.adempiere.webui.component.ListModelTable;
import org.compiere.apps.IStatusBar;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MCharge;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MUOM;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

public class CreateFromOrder extends CreateFrom
{
	protected WCreateFromWindow window = new WCreateFromWindow(this, getGridTab().getWindowNo());
	protected int p_WindowNo = getGridTab().getWindowNo();


	public CreateFromOrder(GridTab gridTab) {
		super(gridTab);
		if (log.isLoggable(Level.INFO)) log.info(gridTab.toString());
	}

	public CLogger log = CLogger.getCLogger(CreateFrom.class);
	
	@Override
	public Object getWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean dynInit() throws Exception {
		log.config("");
		setTitle("Generate Order Line From Purchase Request");
		return true;
	}

	@Override
	public void info(IMiniTable miniTable, IStatusBar statusBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean save(IMiniTable miniTable, String trxName) {
		
		int C_Order_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_Order_ID");
		int M_Requisition_ID = 0;
		
		StringBuilder SQLGetTax = new StringBuilder();
		SQLGetTax.append("SELECT C_Tax_ID ");
		SQLGetTax.append(" FROM C_Tax ");
		SQLGetTax.append(" WHERE C_TaxCategory_ID =?");

		
		
		for (int i = 0; i < miniTable.getRowCount(); i++){
			
			if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue()) {
				
				KeyNamePair ReqLinepair = (KeyNamePair) miniTable.getValueAt(i, 1); 	
				final int M_RequisitionLine_ID = ReqLinepair.getKey();
//				KeyNamePair prodPair = (KeyNamePair) miniTable.getValueAt(i, 2);
//				KeyNamePair UOMPair = (KeyNamePair) miniTable.getValueAt(i, 3);
				BigDecimal QtyReq = (BigDecimal) miniTable.getValueAt(i, 4);
				BigDecimal QtyOrdered = (BigDecimal) miniTable.getValueAt(i, 5);
				BigDecimal QtyAvailable = (BigDecimal) miniTable.getValueAt(i, 6);
//				BigDecimal UnitPrice = (BigDecimal) miniTable.getValueAt(i, 7);
				
				if (QtyAvailable.compareTo(QtyReq.subtract(QtyOrdered))>0) {
					
					throw new AdempiereException("Over Qty Input (Qty Available for Order "+ QtyReq.subtract(QtyOrdered)+")");
					
				}
				
				if (QtyAvailable.compareTo(QtyReq.subtract(QtyOrdered))>0) {
					
					throw new AdempiereException("Selected Item No Available Qty");
					
				}
				
				if (QtyAvailable.compareTo(Env.ZERO) <= 0) {
					
					throw new AdempiereException("Qty Selected Item must greater than 0");
				}
				
				MRequisitionLine reqLine= new MRequisitionLine(Env.getCtx(), M_RequisitionLine_ID, null);
				M_Requisition_ID = reqLine.getM_Requisition_ID();
				MOrderLine ordLine = new MOrderLine(Env.getCtx(), 0, null);
				
				ordLine.setC_Order_ID(C_Order_ID);
				ordLine.setAD_Org_ID(reqLine.getAD_Org_ID());
				ordLine.setLine(reqLine.getLine());
				
				if(reqLine.getM_Product_ID() >0) {
					MProduct prod = new MProduct(Env.getCtx(), reqLine.getM_Product_ID(), null);
					int C_Tax_ID = DB.getSQLValueEx(null, SQLGetTax.toString(), new Object[] {prod.getC_TaxCategory_ID()});
					ordLine.setM_Product_ID(reqLine.getM_Product_ID());
					ordLine.setM_AttributeSetInstance_ID(reqLine.getM_AttributeSetInstance_ID());
					ordLine.setC_UOM_ID(reqLine.getC_UOM_ID());
					ordLine.setC_Tax_ID(C_Tax_ID);
				}
				
				if(reqLine.getC_Charge_ID() > 0) {			
					MCharge charge = new MCharge(Env.getCtx(), reqLine.getC_Charge_ID(), null);
					int C_Tax_ID = DB.getSQLValueEx(null, SQLGetTax.toString(), new Object[] {charge.getC_TaxCategory_ID()});
					ordLine.setC_Charge_ID(reqLine.getC_Charge_ID());
					ordLine.setC_Tax_ID(C_Tax_ID);
				}
				
				ordLine.setDescription(reqLine.getDescription());
				ordLine.setQtyEntered(QtyAvailable);
				ordLine.setQtyOrdered(QtyAvailable);
				ordLine.set_CustomColumn("M_RequisitionLine_ID", reqLine.getM_RequisitionLine_ID());
				ordLine.saveEx();
				
				reqLine.setC_OrderLine_ID(ordLine.getC_OrderLine_ID());
				reqLine.saveEx();
				
			}
			
			MOrder ord = new MOrder(Env.getCtx(), C_Order_ID, null);
			if(ord.get_ValueAsInt("M_Requisition_ID") <= 0) {
				
				ord.set_CustomColumn("M_Requisition_ID", M_Requisition_ID);
				ord.saveEx();
				
			}
			
		}
		
		return true;
	}
	
	protected Vector<Vector<Object>> getPRDetailData (int M_Requisition_ID)
	{
		
		return getPRDetail (M_Requisition_ID);
	}
	
	protected Vector<Vector<Object>> getPRDetail (int M_Requisition_ID)
	{
		/**
		 *  Selected        	- 0
		 *  Line             	- 1
		 *  M_Product_ID        - 2
		 *  C_UOM_ID    		- 3
		 *  Qty    				- 4
		 *  PriceActual 		- 5
		 *  LineNetAmt       	- 6
		
		 */
		if (log.isLoggable(Level.CONFIG)) log.config("M_Requisition_ID=" + M_Requisition_ID);

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuilder SQLGetPRDetail = new StringBuilder();
		SQLGetPRDetail.append("SELECT Line,M_Product_ID,C_UOM_ID,Qty,PriceActual,LineNetAmt,C_OrderLine_ID,M_RequisitionLine_ID");
		SQLGetPRDetail.append(" FROM M_RequisitionLine ");
		SQLGetPRDetail.append(" WHERE M_Requisition_ID = ? ");
		
		StringBuilder SQLGetQyOrdered = new StringBuilder();
		SQLGetQyOrdered.append("SELECT coalesce(SUM(QtyOrdered),0)");
		SQLGetQyOrdered.append(" FROM C_OrderLine");
		SQLGetQyOrdered.append(" WHERE M_RequisitionLine_ID =?");

		
		if (log.isLoggable(Level.FINER)) log.finer(SQLGetPRDetail.toString());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(SQLGetPRDetail.toString(), null);
			pstmt.setInt(1, M_Requisition_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				BigDecimal qtyOrdered = Env.ZERO;
				BigDecimal QtyAvailToOrder = Env.ZERO;
				Vector<Object> line = new Vector<Object>();
				line.add(Boolean.FALSE);           //  0-Selection
				Integer lineNo = rs.getInt(1);
				BigDecimal qty = rs.getBigDecimal(4);
				BigDecimal price = rs.getBigDecimal(5);
//				BigDecimal LineNetAmt =  rs.getBigDecimal(6);
				
				qtyOrdered = DB.getSQLValueBD(null, SQLGetQyOrdered.toString(), new Object[] {rs.getInt(8)});
				QtyAvailToOrder = qty.subtract(qtyOrdered);
				
				KeyNamePair LinePair = new KeyNamePair(rs.getInt(8), lineNo.toString());
				line.add(LinePair);  																//  1-Line
				MProduct prod = new MProduct(Env.getCtx(), rs.getInt(2), null);
				KeyNamePair ProdPair = new KeyNamePair(prod.getM_Product_ID(), prod.getName());
				line.add(ProdPair);                           										//  2-Product
				
				MUOM uom = new MUOM(Env.getCtx(), rs.getInt(3), null);
				KeyNamePair UOMPair = new KeyNamePair(uom.getC_UOM_ID(), uom.getUOMSymbol());
				line.add(UOMPair);                          					 					//  3-uom
				line.add(qty);																		//  4-qty
				line.add(qtyOrdered);																//  5-qty already ordered
				line.add(QtyAvailToOrder);															//  6-qty available to order
				line.add(price);                           											//  7-price
//				line.add(LineNetAmt);                      	   										//  8-Linenetamt
			
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQLGetPRDetail.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return data;
	}   //  LoadOrder
	
	protected Vector<String> getOISColumnNames()
	{
		//  Header Info
	    Vector<String> columnNames = new Vector<String>(7);
	    columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
	    columnNames.add(Msg.translate(Env.getCtx(), "Line"));
	    columnNames.add(Msg.translate(Env.getCtx(), "M_Product_ID"));
	    columnNames.add(Msg.translate(Env.getCtx(), "C_UOM_ID"));
	    columnNames.add("Qty Request");
	    columnNames.add("Qty Already Ordered");
	    columnNames.add("Qty Available To Order");
	    columnNames.add("Unit Product Price");
//	    columnNames.add(Msg.getElement(Env.getCtx(), "LineNetAmt"));
	  
	    return columnNames;
	}
	
	protected void configureMiniTable (IMiniTable miniTable)
	{
		miniTable.setColumnClass(0, Boolean.class, false);     		//  Selection
		miniTable.setColumnClass(1, KeyNamePair.class, true);     		//  Line
		miniTable.setColumnClass(2, KeyNamePair.class, true);          	//  product
		miniTable.setColumnClass(3, KeyNamePair.class, true);  			//  uom
		miniTable.setColumnClass(4, BigDecimal.class, true);   		//  qty requisition
		miniTable.setColumnClass(5, BigDecimal.class, true);   		//  qty ordered
		miniTable.setColumnClass(6, BigDecimal.class, false);   	//  qty to order
		miniTable.setColumnClass(7, BigDecimal.class, true); 		//  price actual
//		miniTable.setColumnClass(8, BigDecimal.class, true);     	//  Line Net Amt
	
		//  Table UI
		miniTable.autoSize();
		
	}
	
	protected void loadTableOIS (Vector<?> data)
	{
		window.getWListbox().clear();
		
		//  Remove previous listeners
		window.getWListbox().getModel().removeTableModelListener(window);
		//  Set Model
		ListModelTable model = new ListModelTable(data);
		model.addTableModelListener(window);
		window.getWListbox().setData(model, getOISColumnNames());
		//
		
		configureMiniTable(window.getWListbox());
	}
	
	
	


}
