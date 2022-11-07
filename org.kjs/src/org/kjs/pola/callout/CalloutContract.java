package org.kjs.pola.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class CalloutContract implements IColumnCallout {

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (mField.getColumnName().equals("C_BPartner_ID")) {
			return this.BPartnerCallout(ctx, WindowNo, mTab, mField, value);
		}
		
		return "";
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

}
