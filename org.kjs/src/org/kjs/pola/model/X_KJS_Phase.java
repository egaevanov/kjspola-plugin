package org.kjs.pola.model;

import org.compiere.model.POInfo;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.PO;

public class X_KJS_Phase extends PO implements I_KJS_Phase, I_Persistent
{
    private static final long serialVersionUID = 20181012L;
    
    public X_KJS_Phase(final Properties ctx, final int KJS_Phase_ID, final String trxName) {
        super(ctx, KJS_Phase_ID, trxName);
    }
    
    public X_KJS_Phase(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
    }
    
    protected int get_AccessLevel() {
        return X_KJS_Phase.accessLevel.intValue();
    }
    
    protected POInfo initPO(final Properties ctx) {
        final POInfo poi = POInfo.getPOInfo(ctx, X_KJS_Phase.Table_ID, this.get_TrxName());
        return poi;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer("X_KJS_Phase[").append(this.get_ID()).append("]");
        return sb.toString();
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
    
    public void setKJS_Phase_UU(final String KJS_Phase_UU) {
        this.set_ValueNoCheck("KJS_Phase_UU", (Object)KJS_Phase_UU);
    }
    
    public String getKJS_Phase_UU() {
        return (String)this.get_Value("KJS_Phase_UU");
    }
    
    public void setName(final String Name) {
        this.set_Value("Name", (Object)Name);
    }
    
    public String getName() {
        return (String)this.get_Value("Name");
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
