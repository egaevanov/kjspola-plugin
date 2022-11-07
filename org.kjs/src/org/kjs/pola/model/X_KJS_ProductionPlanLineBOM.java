package org.kjs.pola.model;

import org.compiere.util.Env;
import java.math.BigDecimal;
import org.compiere.model.I_M_Product;
import org.compiere.model.I_M_AttributeSetInstance;
import org.compiere.model.MTable;
import org.compiere.model.POInfo;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.PO;

public class X_KJS_ProductionPlanLineBOM extends PO implements I_KJS_ProductionPlanLineBOM, I_Persistent
{
    private static final long serialVersionUID = 20190401L;
    
    public X_KJS_ProductionPlanLineBOM(final Properties ctx, final int KJS_ProductionPlanLineBOM_ID, final String trxName) {
        super(ctx, KJS_ProductionPlanLineBOM_ID, trxName);
    }
    
    public X_KJS_ProductionPlanLineBOM(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
    }
    
    protected int get_AccessLevel() {
        return X_KJS_ProductionPlanLineBOM.accessLevel.intValue();
    }
    
    protected POInfo initPO(final Properties ctx) {
        final POInfo poi = POInfo.getPOInfo(ctx, X_KJS_ProductionPlanLineBOM.Table_ID, this.get_TrxName());
        return poi;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer("X_KJS_ProductionPlanLineBOM[").append(this.get_ID()).append("]");
        return sb.toString();
    }
    
    public I_KJS_ProductionPlanLine getKJS_ProductionPlanLine() throws RuntimeException {
        return (I_KJS_ProductionPlanLine)MTable.get(this.getCtx(), "KJS_ProductionPlanLine").getPO(this.getKJS_ProductionPlanLine_ID(), this.get_TrxName());
    }
    
    public void setKJS_ProductionPlanLine_ID(final int KJS_ProductionPlanLine_ID) {
        if (KJS_ProductionPlanLine_ID < 1) {
            this.set_ValueNoCheck("KJS_ProductionPlanLine_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("KJS_ProductionPlanLine_ID", (Object)KJS_ProductionPlanLine_ID);
        }
    }
    
    public int getKJS_ProductionPlanLine_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_ProductionPlanLine_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setKJS_ProductionPlanLineBOM_ID(final int KJS_ProductionPlanLineBOM_ID) {
        if (KJS_ProductionPlanLineBOM_ID < 1) {
            this.set_ValueNoCheck("KJS_ProductionPlanLineBOM_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("KJS_ProductionPlanLineBOM_ID", (Object)KJS_ProductionPlanLineBOM_ID);
        }
    }
    
    public int getKJS_ProductionPlanLineBOM_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_ProductionPlanLineBOM_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setKJS_ProductionPlanLineBOM_UU(final String KJS_ProductionPlanLineBOM_UU) {
        this.set_ValueNoCheck("KJS_ProductionPlanLineBOM_UU", (Object)KJS_ProductionPlanLineBOM_UU);
    }
    
    public String getKJS_ProductionPlanLineBOM_UU() {
        return (String)this.get_Value("KJS_ProductionPlanLineBOM_UU");
    }
    
    public void setLine(final int Line) {
        this.set_ValueNoCheck("Line", (Object)Line);
    }
    
    public int getLine() {
        final Integer ii = (Integer)this.get_Value("Line");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public I_M_AttributeSetInstance getM_AttributeSetInstance() throws RuntimeException {
        return (I_M_AttributeSetInstance)MTable.get(this.getCtx(), "M_AttributeSetInstance").getPO(this.getM_AttributeSetInstance_ID(), this.get_TrxName());
    }
    
    public void setM_AttributeSetInstance_ID(final int M_AttributeSetInstance_ID) {
        if (M_AttributeSetInstance_ID < 0) {
            this.set_Value("M_AttributeSetInstance_ID", (Object)null);
        }
        else {
            this.set_Value("M_AttributeSetInstance_ID", (Object)M_AttributeSetInstance_ID);
        }
    }
    
    public int getM_AttributeSetInstance_ID() {
        final Integer ii = (Integer)this.get_Value("M_AttributeSetInstance_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
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
