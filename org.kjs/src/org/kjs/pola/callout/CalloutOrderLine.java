package org.kjs.pola.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.kjs.pola.model.X_C_Quotation;
import org.kjs.pola.model.X_C_QuotationLine;

public class CalloutOrderLine implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		
		if (mField.getColumnName().equals("C_QuotationLine_ID")) {
			return this.QuotationLineCallout(ctx, WindowNo, mTab, mField, value);
		}
		
		return "";
	}
	
	public String QuotationLineCallout(final Properties ctx, final int WindowNo, final GridTab mTab, final GridField mField,final Object value) {
		if (value == null) {
			return "";
		}

		int C_QuotationLine_ID = (int) value;

		X_C_QuotationLine quoLine = new X_C_QuotationLine(ctx, C_QuotationLine_ID, null);
		
		X_C_Quotation quo = new X_C_Quotation(ctx, quoLine.getC_Quotation_ID(), null);

		StringBuilder SQLGetBPLoc = new StringBuilder();
		SQLGetBPLoc.append("SELECT C_BPartner_Location_ID");
		SQLGetBPLoc.append(" FROM C_BPartner_Location");
		SQLGetBPLoc.append(" WHERE AD_Client_ID = ?");
		SQLGetBPLoc.append(" AND C_BPartner_ID = ?");

		int C_BPartner_Location_ID = DB.getSQLValueEx(null, SQLGetBPLoc.toString(),new Object[] { Env.getAD_Client_ID(ctx), quo.getC_BPartner_ID() });

		mTab.setValue("C_BPartner_ID", quo.getC_BPartner_ID());
		mTab.setValue("C_BPartner_Location_ID", C_BPartner_Location_ID);
		mTab.setValue("M_Warehouse_ID", quo.getM_Warehouse_ID());
		mTab.setValue("M_Product_ID", quoLine.getM_Product_ID());
		mTab.setValue("Description", quoLine.getDescription());
		mTab.setValue("QtyEntered", quoLine.getQtyOrdered());
		mTab.setValue("QtyOrdered", quoLine.getQtyOrdered());
		mTab.setValue("C_Tax_ID", quoLine.getC_Tax_ID());
		mTab.setValue("PriceEntered", quoLine.getPriceEntered());
		mTab.setValue("PriceActual", quoLine.getPriceEntered());
		mTab.setValue("LineNetAmt", quoLine.getQtyOrdered().multiply(quoLine.getPriceEntered()));

		return "";
	}

}
