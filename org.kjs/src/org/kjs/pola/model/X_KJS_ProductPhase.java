package org.kjs.pola.model;

import org.compiere.model.MTable;
import org.compiere.model.I_M_Product;
import org.compiere.model.POInfo;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.PO;

public class X_KJS_ProductPhase extends PO implements I_KJS_ProductPhase, I_Persistent
{
    private static final long serialVersionUID = 20181012L;
    
    public X_KJS_ProductPhase(final Properties ctx, final int KJS_ProductPhase_ID, final String trxName) {
        super(ctx, KJS_ProductPhase_ID, trxName);
    }
    
    public X_KJS_ProductPhase(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
    }
    
    protected int get_AccessLevel() {
        return X_KJS_ProductPhase.accessLevel.intValue();
    }
    
    protected POInfo initPO(final Properties ctx) {
        final POInfo poi = POInfo.getPOInfo(ctx, X_KJS_ProductPhase.Table_ID, this.get_TrxName());
        return poi;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer("X_KJS_ProductPhase[").append(this.get_ID()).append("]");
        return sb.toString();
    }
    
    public void setKJS_ProductPhase_ID(final int KJS_ProductPhase_ID) {
        if (KJS_ProductPhase_ID < 1) {
            this.set_ValueNoCheck("KJS_ProductPhase_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("KJS_ProductPhase_ID", (Object)KJS_ProductPhase_ID);
        }
    }
    
    public int getKJS_ProductPhase_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_ProductPhase_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setKJS_ProductPhase_UU(final String KJS_ProductPhase_UU) {
        this.set_ValueNoCheck("KJS_ProductPhase_UU", (Object)KJS_ProductPhase_UU);
    }
    
    public String getKJS_ProductPhase_UU() {
        return (String)this.get_Value("KJS_ProductPhase_UU");
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
    
    public void setProcessed(final boolean Processed) {
        this.set_Value("Processed", (Object)Processed);
    }
    
    public boolean isProcessed() {
        final Object oo = this.get_Value("Processed");
        if (oo == null) {
            return false;
        }
        if (oo instanceof Boolean) {
            return (boolean)oo;
        }
        return "Y".equals(oo);
    }
}
