package org.kjs.pola.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.kjs.pola.model.X_C_Quotation;

public class CalloutOrder implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		
		if (mField.getColumnName().equals("C_Quotation_ID")) {
			return this.QuotationCallout(ctx, WindowNo, mTab, mField, value);
		}
		
		return "";
	}
	
	public String QuotationCallout(final Properties ctx, final int WindowNo, final GridTab mTab, final GridField mField,final Object value) {
		if (value == null) {
			return "";
		}

		int C_Quotation_ID = (int) value;
		
		X_C_Quotation quo = new X_C_Quotation(ctx, C_Quotation_ID, null);

		StringBuilder SQLGetBPLoc = new StringBuilder();
		SQLGetBPLoc.append("SELECT C_BPartner_Location_ID");
		SQLGetBPLoc.append(" FROM C_BPartner_Location");
		SQLGetBPLoc.append(" WHERE AD_Client_ID = ?");
		SQLGetBPLoc.append(" AND C_BPartner_ID = ?");

		int C_BPartner_Location_ID = DB.getSQLValueEx(null, SQLGetBPLoc.toString(),new Object[] { Env.getAD_Client_ID(ctx), quo.getC_BPartner_ID() });

		mTab.setValue("C_BPartner_ID", quo.getC_BPartner_ID());
		mTab.setValue("C_BPartner_Location_ID", C_BPartner_Location_ID);
		mTab.setValue("Description", quo.getDescription());
		mTab.setValue("SalesRep_ID", quo.getSalesRep_ID());
		mTab.setValue("M_Warehouse_ID", quo.getM_Warehouse_ID());
		mTab.setValue("M_PriceList_ID", quo.getM_PriceList_ID());
		mTab.setValue("C_PaymentTerm_ID", quo.getC_PaymentTerm_ID());
		mTab.setValue("PaymentRule", quo.getPaymentRule());

		return null;
	}

}
