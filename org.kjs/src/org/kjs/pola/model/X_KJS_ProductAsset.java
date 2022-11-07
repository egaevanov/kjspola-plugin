package org.kjs.pola.model;

import org.compiere.model.I_M_Product;
import org.compiere.model.MTable;
import org.compiere.model.POInfo;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.PO;

public class X_KJS_ProductAsset extends PO implements I_KJS_ProductAsset, I_Persistent
{
    private static final long serialVersionUID = 20181013L;
    
    public X_KJS_ProductAsset(final Properties ctx, final int KJS_ProductAsset_ID, final String trxName) {
        super(ctx, KJS_ProductAsset_ID, trxName);
    }
    
    public X_KJS_ProductAsset(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
    }
    
    protected int get_AccessLevel() {
        return X_KJS_ProductAsset.accessLevel.intValue();
    }
    
    protected POInfo initPO(final Properties ctx) {
        final POInfo poi = POInfo.getPOInfo(ctx, X_KJS_ProductAsset.Table_ID, this.get_TrxName());
        return poi;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer("X_KJS_ProductAsset[").append(this.get_ID()).append("]");
        return sb.toString();
    }
    
    public I_KJS_Phase getKJS_Phase() throws RuntimeException {
        return (I_KJS_Phase)MTable.get(this.getCtx(), "KJS_Phase").getPO(this.getKJS_Phase_ID(), this.get_TrxName());
    }
    
    public void setKJS_Phase_ID(final int KJS_Phase_ID) {
        if (KJS_Phase_ID < 1) {
            this.set_ValueNoCheck("KJS_Phase_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("KJS_Phase_ID", (Object)KJS_Phase_ID);
        }
    }
    
    public int getKJS_Phase_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_Phase_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setKJS_ProductAsset_ID(final int KJS_ProductAsset_ID) {
        if (KJS_ProductAsset_ID < 1) {
            this.set_ValueNoCheck("KJS_ProductAsset_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("KJS_ProductAsset_ID", (Object)KJS_ProductAsset_ID);
        }
    }
    
    public int getKJS_ProductAsset_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_ProductAsset_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setKJS_ProductAsset_UU(final String KJS_ProductAsset_UU) {
        this.set_ValueNoCheck("KJS_ProductAsset_UU", (Object)KJS_ProductAsset_UU);
    }
    
    public String getKJS_ProductAsset_UU() {
        return (String)this.get_Value("KJS_ProductAsset_UU");
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
