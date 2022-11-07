package org.kjs.pola.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.apps.form.WCreateFromWindow;
import org.adempiere.webui.component.ListModelTable;
import org.compiere.apps.IStatusBar;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MUOM;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.kjs.pola.model.X_C_ARProInv;
import org.kjs.pola.model.X_C_ARProInvLine;

public class CreateFromProformaInv extends CreateFrom{
	
	protected WCreateFromWindow window = new WCreateFromWindow(this, getGridTab().getWindowNo());
	protected int p_WindowNo = getGridTab().getWindowNo();

	public CreateFromProformaInv(GridTab gridTab) {
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
		setTitle("Generate Proforma Line From Sales Order");
		return true;
	}

	@Override
	public void info(IMiniTable miniTable, IStatusBar statusBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean save(IMiniTable miniTable, String trxName) {
		
		int C_ARProInv_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_ARProInv_ID");
		int C_Order_ID = 0;
		X_C_ARProInv Inv = new X_C_ARProInv(null, C_ARProInv_ID, trxName);
	
		for (int i = 0; i < miniTable.getRowCount(); i++){
			
			if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue()) {
				
				KeyNamePair OrderLinepair = (KeyNamePair) miniTable.getValueAt(i, 1); 	
				final int C_OrderLine_ID = OrderLinepair.getKey();
//				KeyNamePair prodPair = (KeyNamePair) miniTable.getValueAt(i, 2);
//				KeyNamePair UOMPair = (KeyNamePair) miniTable.getValueAt(i, 3);
				BigDecimal QtyOrdered = (BigDecimal) miniTable.getValueAt(i, 4);
//				BigDecimal UnitPrice = (BigDecimal) miniTable.getValueAt(i, 7);
				
				
				MOrderLine ordLine= new MOrderLine(Env.getCtx(), C_OrderLine_ID, null);
				C_Order_ID = ordLine.getC_Order_ID();
				X_C_ARProInvLine invLine = new X_C_ARProInvLine(Env.getCtx(), 0, null);
				
				invLine.setAD_Org_ID(ordLine.getAD_Org_ID());
				invLine.setC_ARProInv_ID(Inv.getC_ARProInv_ID());

				if (ordLine.getC_Charge_ID() > 0) {
					invLine.setC_Charge_ID(ordLine.getC_Charge_ID());
				}

				if (ordLine.getM_Product_ID() > 0) {
					invLine.setM_Product_ID(ordLine.getM_Product_ID());
					MProduct prod = new MProduct(Env.getCtx(), ordLine.getM_Product_ID(), null);
					invLine.setC_UOM_ID(prod.getC_UOM_ID());
				}

				invLine.setLine(ordLine.getLine());
				invLine.setC_Tax_ID(ordLine.getC_Tax_ID());
				invLine.setC_OrderLine_ID(C_OrderLine_ID);

				invLine.setQtyEntered(QtyOrdered);
				invLine.setPriceEntered(ordLine.getPriceEntered());
				invLine.setPriceActual(ordLine.getPriceEntered());
				invLine.setLineNetAmt(invLine.getPriceEntered().multiply(QtyOrdered));
				invLine.saveEx();
				
			}
			
			if(Inv.get_ValueAsInt("C_Order_ID") <= 0) {
				
				Inv.set_CustomColumn("C_Order_ID", C_Order_ID);
				Inv.saveEx();
				
			}
			
		}
		
		return true;
	}
	
	protected Vector<Vector<Object>> getSODetailData (int C_Order_ID)
	{
		
		return getSODetail (C_Order_ID);
	}
	
	protected Vector<Vector<Object>> getSODetail (int C_Order_ID)
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
		if (log.isLoggable(Level.CONFIG)) log.config("C_Order_ID=" + C_Order_ID);

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuilder SQLGetPRDetail = new StringBuilder();
		SQLGetPRDetail.append("SELECT Line,M_Product_ID,C_UOM_ID,QtyOrdered,PriceActual,LineNetAmt,C_OrderLine_ID");
		SQLGetPRDetail.append(" FROM C_OrderLine ");
		SQLGetPRDetail.append(" WHERE C_Order_ID = ? ");
		
		
		if (log.isLoggable(Level.FINER)) log.finer(SQLGetPRDetail.toString());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(SQLGetPRDetail.toString(), null);
			pstmt.setInt(1, C_Order_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(Boolean.FALSE);           //  0-Selection
				Integer lineNo = rs.getInt(1);
				BigDecimal qty = rs.getBigDecimal(4);
				BigDecimal price = rs.getBigDecimal(5);
				
				
				KeyNamePair LinePair = new KeyNamePair(rs.getInt(7), lineNo.toString());
				line.add(LinePair);  																//  1-Line		
				MProduct prod = new MProduct(Env.getCtx(), rs.getInt(2), null);
				KeyNamePair ProdPair = new KeyNamePair(prod.getM_Product_ID(), prod.getName());
				line.add(ProdPair);                           										//  2-Product	
				MUOM uom = new MUOM(Env.getCtx(), rs.getInt(3), null);
				KeyNamePair UOMPair = new KeyNamePair(uom.getC_UOM_ID(), uom.getUOMSymbol());
				line.add(UOMPair);                          					 					//  3-uom	
				line.add(qty);																		//  4-qty
				line.add(price);                           											//  7-price
			
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
	    columnNames.add("Product Name");
	    columnNames.add("Units of Measure(UOM)");
	    columnNames.add("Qty Ordered");
	    columnNames.add("Unit Product Price");
	  
	    return columnNames;
	}
	
	protected void configureMiniTable (IMiniTable miniTable)
	{
		miniTable.setColumnClass(0, Boolean.class, false);     			//  Selection
		miniTable.setColumnClass(1, KeyNamePair.class, true);     		//  Line
		miniTable.setColumnClass(2, KeyNamePair.class, true);          	//  product
		miniTable.setColumnClass(3, KeyNamePair.class, true);  			//  uom
		miniTable.setColumnClass(4, BigDecimal.class, false);   		//  qty ordered
		miniTable.setColumnClass(5, BigDecimal.class, true); 			//  price actual
	
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
