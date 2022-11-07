package org.kjs.pola.model;

import org.compiere.util.Env;
import java.math.BigDecimal;
import org.compiere.model.I_M_RequisitionLine;
import org.compiere.model.I_M_Product;
import org.compiere.model.MTable;
import org.compiere.model.I_C_OrderLine;
import org.compiere.model.POInfo;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.PO;

public class X_KJS_ProductionPlanLink extends PO implements I_KJS_ProductionPlanLink, I_Persistent
{
    private static final long serialVersionUID = 20190401L;
    
    public X_KJS_ProductionPlanLink(final Properties ctx, final int KJS_ProductionPlanLink_ID, final String trxName) {
        super(ctx, KJS_ProductionPlanLink_ID, trxName);
    }
    
    public X_KJS_ProductionPlanLink(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
    }
    
    protected int get_AccessLevel() {
        return X_KJS_ProductionPlanLink.accessLevel.intValue();
    }
    
    protected POInfo initPO(final Properties ctx) {
        final POInfo poi = POInfo.getPOInfo(ctx, X_KJS_ProductionPlanLink.Table_ID, this.get_TrxName());
        return poi;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer("X_KJS_ProductionPlanLink[").append(this.get_ID()).append("]");
        return sb.toString();
    }
    
    public I_C_OrderLine getC_OrderLine() throws RuntimeException {
        return (I_C_OrderLine)MTable.get(this.getCtx(), "C_OrderLine").getPO(this.getC_OrderLine_ID(), this.get_TrxName());
    }
    
    public void setC_OrderLine_ID(final int C_OrderLine_ID) {
        if (C_OrderLine_ID < 1) {
            this.set_ValueNoCheck("C_OrderLine_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("C_OrderLine_ID", (Object)C_OrderLine_ID);
        }
    }
    
    public int getC_OrderLine_ID() {
        final Integer ii = (Integer)this.get_Value("C_OrderLine_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public I_KJS_ProductionPlan getKJS_ProductionPlan() throws RuntimeException {
        return (I_KJS_ProductionPlan)MTable.get(this.getCtx(), "KJS_ProductionPlan").getPO(this.getKJS_ProductionPlan_ID(), this.get_TrxName());
    }
    
    public void setKJS_ProductionPlan_ID(final int KJS_ProductionPlan_ID) {
        if (KJS_ProductionPlan_ID < 1) {
            this.set_ValueNoCheck("KJS_ProductionPlan_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("KJS_ProductionPlan_ID", (Object)KJS_ProductionPlan_ID);
        }
    }
    
    public int getKJS_ProductionPlan_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_ProductionPlan_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setKJS_ProductionPlanLink_ID(final int KJS_ProductionPlanLink_ID) {
        if (KJS_ProductionPlanLink_ID < 1) {
            this.set_ValueNoCheck("KJS_ProductionPlanLink_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("KJS_ProductionPlanLink_ID", (Object)KJS_ProductionPlanLink_ID);
        }
    }
    
    public int getKJS_ProductionPlanLink_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_ProductionPlanLink_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setKJS_ProductionPlanLink_UU(final String KJS_ProductionPlanLink_UU) {
        this.set_ValueNoCheck("KJS_ProductionPlanLink_UU", (Object)KJS_ProductionPlanLink_UU);
    }
    
    public String getKJS_ProductionPlanLink_UU() {
        return (String)this.get_Value("KJS_ProductionPlanLink_UU");
    }
    
    public I_M_Product getM_Product() throws RuntimeException {
        return (I_M_Product)MTable.get(this.getCtx(), "M_Product").getPO(this.getM_Product_ID(), this.get_TrxName());
    }
    
    public void setM_Product_ID(final int M_Product_ID) {
        if (M_Product_ID < 1) {
            this.set_Value("M_Product_ID", (Object)null);
        }
        else {
            this.set_Value("M_Product_ID", (Object)M_Product_ID);
        }
    }
    
    public int getM_Product_ID() {
        final Integer ii = (Integer)this.get_Value("M_Product_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public I_M_RequisitionLine getM_RequisitionLine() throws RuntimeException {
        return (I_M_RequisitionLine)MTable.get(this.getCtx(), "M_RequisitionLine").getPO(this.getM_RequisitionLine_ID(), this.get_TrxName());
    }
    
    public void setM_RequisitionLine_ID(final int M_RequisitionLine_ID) {
        if (M_RequisitionLine_ID < 1) {
            this.set_Value("M_RequisitionLine_ID", (Object)null);
        }
        else {
            this.set_Value("M_RequisitionLine_ID", (Object)M_RequisitionLine_ID);
        }
    }
    
    public int getM_RequisitionLine_ID() {
        final Integer ii = (Integer)this.get_Value("M_RequisitionLine_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setQty(final BigDecimal Qty) {
        this.set_Value("Qty", (Object)Qty);
    }
    
    public BigDecimal getQty() {
        final BigDecimal bd = (BigDecimal)this.get_Value("Qty");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }
}
