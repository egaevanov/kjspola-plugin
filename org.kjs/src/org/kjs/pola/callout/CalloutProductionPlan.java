package org.kjs.pola.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisitionLine;
import org.compiere.util.Env;

public class CalloutProductionPlan implements IColumnCallout
{
    public String start(final Properties ctx, final int WindowNo, final GridTab mTab, final GridField mField, final Object value, final Object oldValue) {
        if (mField.getColumnName().equals("KJS_Tolerance")) {
            return this.CalculateQtyEntered(ctx, WindowNo, mTab, mField, value);
        }
        return null;
    }
    
    public String OrderData(final Properties ctx, final int WindowNo, final GridTab mTab, final GridField mField, final Object value) {
        if (value == null) {
            return "";
        }
        final int C_OrderLine_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_OrderLine_ID");
        final MOrderLine orderLine = new MOrderLine(ctx, C_OrderLine_ID, (String)null);
        mTab.setValue("QtyOrdered", (Object)orderLine.getQtyOrdered());
        mTab.setValue("QtyEntered", (Object)orderLine.getQtyOrdered());
        mTab.setValue("M_Product_ID", (Object)orderLine.getM_Product_ID());
        mTab.setValue("AD_Org_ID", (Object)orderLine.getAD_Org_ID());
        return null;
    }
    
    public String RequisitionData(final Properties ctx, final int WindowNo, final GridTab mTab, final GridField mField, final Object value) {
        if (value == null) {
            return "";
        }
        final int M_RequisitionLine_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_RequisitionLine_ID");
        final MRequisitionLine rLine = new MRequisitionLine(ctx, M_RequisitionLine_ID, (String)null);
        mTab.setValue("QtyOrdered", (Object)rLine.getQty());
        mTab.setValue("QtyEntered", (Object)rLine.getQty());
        mTab.setValue("M_Product_ID", (Object)rLine.getM_Product_ID());
        mTab.setValue("AD_Org_ID", (Object)rLine.getAD_Org_ID());
        return null;
    }
    
    public String CalculateQtyEntered(final Properties ctx, final int WindowNo, final GridTab mTab, final GridField mField, final Object value) {
        if (value == null) {
            return "";
        }
        return null;
    }
}
