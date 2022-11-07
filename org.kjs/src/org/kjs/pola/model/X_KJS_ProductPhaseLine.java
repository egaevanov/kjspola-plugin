package org.kjs.pola.model;

import org.compiere.model.I_M_Product;
import org.compiere.model.MTable;
import org.compiere.model.POInfo;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.PO;

public class X_KJS_ProductPhaseLine extends PO implements I_KJS_ProductPhaseLine, I_Persistent
{
    private static final long serialVersionUID = 20190624L;
    public static final int BOMTYPE_AD_Reference_ID = 279;
    public static final String BOMTYPE_StandardPart = "P";
    public static final String BOMTYPE_OptionalPart = "O";
    public static final String BOMTYPE_InAlternativeGroup1 = "1";
    public static final String BOMTYPE_InAlternativeGroup2 = "2";
    public static final String BOMTYPE_InAlternaltveGroup3 = "3";
    public static final String BOMTYPE_InAlternativeGroup4 = "4";
    public static final String BOMTYPE_InAlternativeGroup5 = "5";
    public static final String BOMTYPE_InAlternativeGroup6 = "6";
    public static final String BOMTYPE_InAlternativeGroup7 = "7";
    public static final String BOMTYPE_InAlternativeGroup8 = "8";
    public static final String BOMTYPE_InAlternativeGroup9 = "9";
    
    public X_KJS_ProductPhaseLine(final Properties ctx, final int KJS_ProductPhaseLine_ID, final String trxName) {
        super(ctx, KJS_ProductPhaseLine_ID, trxName);
    }
    
    public X_KJS_ProductPhaseLine(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
    }
    
    protected int get_AccessLevel() {
        return X_KJS_ProductPhaseLine.accessLevel.intValue();
    }
    
    protected POInfo initPO(final Properties ctx) {
        final POInfo poi = POInfo.getPOInfo(ctx, X_KJS_ProductPhaseLine.Table_ID, this.get_TrxName());
        return poi;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer("X_KJS_ProductPhaseLine[").append(this.get_ID()).append("]");
        return sb.toString();
    }
    
    public void setBOMType(final String BOMType) {
        this.set_Value("BOMType", (Object)BOMType);
    }
    
    public String getBOMType() {
        return (String)this.get_Value("BOMType");
    }
    
    public void setDuration(final int Duration) {
        this.set_Value("Duration", (Object)Duration);
    }
    
    public int getDuration() {
        final Integer ii = (Integer)this.get_Value("Duration");
        if (ii == null) {
            return 0;
        }
        return ii;
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
    
    public I_KJS_ProductPhase getKJS_ProductPhase() throws RuntimeException {
        return (I_KJS_ProductPhase)MTable.get(this.getCtx(), "KJS_ProductPhase").getPO(this.getKJS_ProductPhase_ID(), this.get_TrxName());
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
    
    public void setKJS_ProductPhaseLine_ID(final int KJS_ProductPhaseLine_ID) {
        if (KJS_ProductPhaseLine_ID < 1) {
            this.set_ValueNoCheck("KJS_ProductPhaseLine_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("KJS_ProductPhaseLine_ID", (Object)KJS_ProductPhaseLine_ID);
        }
    }
    
    public int getKJS_ProductPhaseLine_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_ProductPhaseLine_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setKJS_ProductPhaseLine_UU(final String KJS_ProductPhaseLine_UU) {
        this.set_ValueNoCheck("KJS_ProductPhaseLine_UU", (Object)KJS_ProductPhaseLine_UU);
    }
    
    public String getKJS_ProductPhaseLine_UU() {
        return (String)this.get_Value("KJS_ProductPhaseLine_UU");
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
