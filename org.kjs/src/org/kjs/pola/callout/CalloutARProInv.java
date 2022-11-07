package org.kjs.pola.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MOrder;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class CalloutARProInv implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (mField.getColumnName().equals("C_BPartner_ID")) {
			return this.BPartnerCallout(ctx, WindowNo, mTab, mField, value);
		}else if(mField.getColumnName().equals("C_Order_ID")) {
			return this.OrderCallout(ctx, WindowNo, mTab, mField, value);
		}
		return null;
	}
	
	
	public String BPartnerCallout(final Properties ctx, final int WindowNo, final GridTab mTab, final GridField mField,final Object value) {
		if (value == null) {
			return "";
		}

		int C_BPartner_ID = (int) value;

		StringBuilder SQLGetBPLoc = new StringBuilder();
		SQLGetBPLoc.append("SELECT C_BPartner_Location_ID");
		SQLGetBPLoc.append(" FROM C_BPartner_Location");
		SQLGetBPLoc.append(" WHERE AD_Client_ID = ?");
		SQLGetBPLoc.append(" AND C_BPartner_ID = ?");

		int C_BPartner_Location_ID = DB.getSQLValueEx(null, SQLGetBPLoc.toString(),new Object[] { Env.getAD_Client_ID(ctx), C_BPartner_ID });

		mTab.setValue("C_BPartner_Location_ID", C_BPartner_Location_ID);

		return null;
	}
	
	
	public String OrderCallout(final Properties ctx, final int WindowNo, final GridTab mTab, final GridField mField,final Object value) {
		if (value == null) {
			return "";
		}

		int C_Order_ID = (int) value;

		MOrder ord = new MOrder(ctx, C_Order_ID, null);
		mTab.setValue("DateOrdered", ord.getDateOrdered());
		mTab.setValue("C_BPartner_ID", ord.getC_BPartner_ID());
		mTab.setValue("C_BPartner_Location_ID", ord.getC_BPartner_Location_ID());
		mTab.setValue("M_PriceList_ID", ord.getM_PriceList_ID());
		mTab.setValue("C_Currency_ID", ord.getC_Currency_ID());


		return null;
	}

}
