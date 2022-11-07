package org.kjs.pola.model;

import org.compiere.model.I_M_Product;
import org.compiere.model.MTable;
import org.compiere.model.POInfo;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.PO;

public class X_I_ProductPhase extends PO implements I_I_ProductPhase, I_Persistent
{
    private static final long serialVersionUID = 20190625L;
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
    
    public X_I_ProductPhase(final Properties ctx, final int I_ProductPhase_ID, final String trxName) {
        super(ctx, I_ProductPhase_ID, trxName);
    }
    
    public X_I_ProductPhase(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
    }
    
    protected int get_AccessLevel() {
        return X_I_ProductPhase.accessLevel.intValue();
    }
    
    protected POInfo initPO(final Properties ctx) {
        final POInfo poi = POInfo.getPOInfo(ctx, X_I_ProductPhase.Table_ID, this.get_TrxName());
        return poi;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer("X_I_ProductPhase[").append(this.get_ID()).append("]");
        return sb.toString();
    }
    
    public void setBOMType(final String BOMType) {
        this.set_Value("BOMType", (Object)BOMType);
    }
    
    public String getBOMType() {
        return (String)this.get_Value("BOMType");
    }
    
    public void setI_ErrorMsg(final String I_ErrorMsg) {
        this.set_Value("I_ErrorMsg", (Object)I_ErrorMsg);
    }
    
    public String getI_ErrorMsg() {
        return (String)this.get_Value("I_ErrorMsg");
    }
    
    public void setI_IsImported(final boolean I_IsImported) {
        this.set_Value("I_IsImported", (Object)I_IsImported);
    }
    
    public boolean isI_IsImported() {
        final Object oo = this.get_Value("I_IsImported");
        if (oo == null) {
            return false;
        }
        if (oo instanceof Boolean) {
            return (boolean)oo;
        }
        return "Y".equals(oo);
    }
    
    public void setI_ProductPhase_ID(final int I_ProductPhase_ID) {
        if (I_ProductPhase_ID < 1) {
            this.set_ValueNoCheck("I_ProductPhase_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("I_ProductPhase_ID", (Object)I_ProductPhase_ID);
        }
    }
    
    public int getI_ProductPhase_ID() {
        final Integer ii = (Integer)this.get_Value("I_ProductPhase_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setI_ProductPhase_UU(final String I_ProductPhase_UU) {
        this.set_ValueNoCheck("I_ProductPhase_UU", (Object)I_ProductPhase_UU);
    }
    
    public String getI_ProductPhase_UU() {
        return (String)this.get_Value("I_ProductPhase_UU");
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
    
    public I_M_Product getM_ProductPhaseLine() throws RuntimeException {
        return (I_M_Product)MTable.get(this.getCtx(), "M_Product").getPO(this.getM_ProductPhaseLine_ID(), this.get_TrxName());
    }
    
    public void setM_ProductPhaseLine_ID(final int M_ProductPhaseLine_ID) {
        if (M_ProductPhaseLine_ID < 1) {
            this.set_Value("M_ProductPhaseLine_ID", (Object)null);
        }
        else {
            this.set_Value("M_ProductPhaseLine_ID", (Object)M_ProductPhaseLine_ID);
        }
    }
    
    public int getM_ProductPhaseLine_ID() {
        final Integer ii = (Integer)this.get_Value("M_ProductPhaseLine_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
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
    
    public void setProcessing(final boolean Processing) {
        this.set_Value("Processing", (Object)Processing);
    }
    
    public boolean isProcessing() {
        final Object oo = this.get_Value("Processing");
        if (oo == null) {
            return false;
        }
        if (oo instanceof Boolean) {
            return (boolean)oo;
        }
        return "Y".equals(oo);
    }
    
    public void setProductPhaseLineValue(final String ProductPhaseLineValue) {
        this.set_Value("ProductPhaseLineValue", (Object)ProductPhaseLineValue);
    }
    
    public String getProductPhaseLineValue() {
        return (String)this.get_Value("ProductPhaseLineValue");
    }
    
    public void setValue(final String Value) {
        this.set_Value("Value", (Object)Value);
    }
    
    public String getValue() {
        return (String)this.get_Value("Value");
    }
}
