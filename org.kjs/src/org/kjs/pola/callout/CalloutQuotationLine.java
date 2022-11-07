package org.kjs.pola.callout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Properties;

import org.adempiere.base.Core;
import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IProductPricing;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MPriceList;
import org.compiere.model.MProduct;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.kjs.pola.model.X_C_Quotation;

public class CalloutQuotationLine implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {

		if (mField.getColumnName().equals("M_Product_ID")) {
			return this.ProductCallout(ctx, WindowNo, mTab, mField, value);
		}else if ((mField.getColumnName().equals("QtyOrdered"))) {	
			return this.Qty(ctx, WindowNo, mTab, mField, value);
		}else if ((mField.getColumnName().equals("PriceEntered"))) {	
			return this.price(ctx, WindowNo, mTab, mField, value);
		}
		
		return "";
	}
	
	public String ProductCallout(final Properties ctx, final int WindowNo, final GridTab mTab, final GridField mField,final Object value) {
		if (value == null) {
			
			mTab.setValue("PriceList", Env.ZERO);
			mTab.setValue("PriceActual", Env.ZERO);
			mTab.setValue("PriceEntered", Env.ZERO);
			mTab.setValue("C_UOM_ID", null);
			mTab.setValue("LineNetAmt", Env.ZERO);

			
			return "";
		}

		int M_Product_ID = (int) value;

		MProduct prod = new MProduct(ctx, M_Product_ID, null);
		mTab.setValue("C_UOM_ID", prod.getC_UOM_ID());
		
        int C_Quotation_ID = (int) mTab.getValue("C_Quotation_ID");
        		
        X_C_Quotation quo = new X_C_Quotation(ctx, C_Quotation_ID, null);
        
        int M_PriceList_ID = quo.getM_PriceList_ID(); 
        
        
		mTab.setValue("C_Charge_ID", null);
		
		BigDecimal Qty = (BigDecimal)mTab.getValue("QtyOrdered");
		int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");

		boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
		IProductPricing pp = Core.getProductPricing();
		
		pp.setInitialValues(M_Product_ID, C_BPartner_ID, Qty, IsSOTrx, null);
		Timestamp orderDate =  Env.getContextAsDate(ctx, WindowNo, "DateOrdered");
		pp.setPriceDate(orderDate);
		pp.setM_PriceList_ID(M_PriceList_ID);

		int M_PriceList_Version_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_Version_ID");
		if ( M_PriceList_Version_ID == 0 && M_PriceList_ID > 0){
			StringBuilder SQLGetPriceListVersion = new StringBuilder();
			
			SQLGetPriceListVersion.append("SELECT plv.M_PriceList_Version_ID");
			SQLGetPriceListVersion.append(" FROM M_PriceList_Version plv ");
			SQLGetPriceListVersion.append(" WHERE plv.M_PriceList_ID=? ");
			SQLGetPriceListVersion.append(" AND plv.ValidFrom <= ? ");
			SQLGetPriceListVersion.append(" ORDER BY plv.ValidFrom DESC");

			M_PriceList_Version_ID = DB.getSQLValueEx(null, SQLGetPriceListVersion.toString(), M_PriceList_ID, orderDate);
			if ( M_PriceList_Version_ID > 0 )
				Env.setContext(ctx, WindowNo, "M_PriceList_Version_ID", M_PriceList_Version_ID );
		}
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
		//
		mTab.setValue("PriceList", pp.getPriceList());
		mTab.setValue("PriceActual", pp.getPriceStd());
		mTab.setValue("PriceEntered", pp.getPriceStd());
		
		BigDecimal priceEnter = (BigDecimal)mTab.getValue("PriceEntered");
		BigDecimal qty = (BigDecimal)mTab.getValue("QtyOrdered"); 
		mTab.setValue("LineNetAmt", priceEnter.multiply(qty));

		
		return null;
	}
	
	public String Qty (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		if (value == null) {
			return"";		
		}
				
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_Product_ID");
		if(M_Product_ID <= 0) {		
			return "";
		}
		
		
		int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_PriceList_ID");
		int StdPrecision = MPriceList.getStandardPrecision(ctx, M_PriceList_ID);	
		BigDecimal  QtyOrdered, PriceEntered;
		
		QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
		if (QtyOrdered == null)
			QtyOrdered = Env.ZERO;
		
		PriceEntered = (BigDecimal)mTab.getValue("PriceEntered");
		BigDecimal LineNetAmt = QtyOrdered.multiply(PriceEntered);
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, RoundingMode.HALF_UP);
		mTab.setValue("LineNetAmt", LineNetAmt);

		return "";	
	}
	
	public String price(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		if (value == null) {
			return"";		
		}
				
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_Product_ID");
		if(M_Product_ID <= 0) {		
			return "";
		}
		
		
		int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_PriceList_ID");
		int StdPrecision = MPriceList.getStandardPrecision(ctx, M_PriceList_ID);	
		BigDecimal  PriceEntered, QtyOrdered;
		
		PriceEntered = (BigDecimal) value;
		QtyOrdered = (BigDecimal) mTab.getValue("QtyOrdered");
		
		if (QtyOrdered == null)
			QtyOrdered = Env.ZERO;
				
		BigDecimal LineNetAmt = QtyOrdered.multiply(PriceEntered);
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, RoundingMode.HALF_UP);
		mTab.setValue("PriceActual", PriceEntered);
		mTab.setValue("LineNetAmt", LineNetAmt);

		return "";	
	}
	

}
