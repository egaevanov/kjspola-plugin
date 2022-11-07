package org.kjs.pola.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Env;
import org.kjs.pola.model.X_KJS_ProductionPlanLine;

public class CalloutProductionSingle implements IColumnCallout
{
    public String start(final Properties ctx, final int WindowNo, final GridTab mTab, final GridField mField, final Object value, final Object oldValue) {
        if (mField.getColumnName().equals("KJS_ProductionPlanLine_ID")) {
            return this.ProductionPlanData(ctx, WindowNo, mTab, mField, value);
        }
        return null;
    }
    
    public String ProductionPlanData(final Properties ctx, final int WindowNo, final GridTab mTab, final GridField mField, final Object value) {
        if (value == null) {
            return "";
        }
        final int KJS_ProductionPlanLine_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "KJS_ProductionPlanLine_ID");
        final X_KJS_ProductionPlanLine ppLine = new X_KJS_ProductionPlanLine(ctx, KJS_ProductionPlanLine_ID, (String)null);
        mTab.setValue("M_Product_ID", (Object)ppLine.getM_Product_ID());
        mTab.setValue("AD_Org_ID", (Object)ppLine.getAD_Org_ID());
        mTab.setValue("KJS_ProductAsset_ID", (Object)ppLine.getKJS_ProductAsset_ID());
        mTab.setValue("BOMQty", (Object)ppLine.getBOMQty());

        return null;
    }
}
