package org.kjs.pola.model;

import org.compiere.model.MTable;
import org.compiere.model.I_M_Product;
import org.compiere.util.Env;
import java.math.BigDecimal;
import org.compiere.model.POInfo;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.PO;

public class X_I_Product_BOM extends PO implements I_I_Product_BOM, I_Persistent
{
    private static final long serialVersionUID = 20190110L;
    public static final int BOMTYPE_AD_Reference_ID = 347;
    public static final String BOMTYPE_CurrentActive = "A";
    public static final String BOMTYPE_Make_To_Order = "O";
    public static final String BOMTYPE_Previous = "P";
    public static final String BOMTYPE_PreviousSpare = "S";
    public static final String BOMTYPE_Future = "F";
    public static final String BOMTYPE_Maintenance = "M";
    public static final String BOMTYPE_Repair = "R";
    public static final String BOMTYPE_ProductConfigure = "C";
    public static final String BOMTYPE_Make_To_Kit = "K";
    
    public X_I_Product_BOM(final Properties ctx, final int I_Product_BOM_ID, final String trxName) {
        super(ctx, I_Product_BOM_ID, trxName);
    }
    
    public X_I_Product_BOM(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
    }
    
    protected int get_AccessLevel() {
        return X_I_Product_BOM.accessLevel.intValue();
    }
    
    protected POInfo initPO(final Properties ctx) {
        final POInfo poi = POInfo.getPOInfo(ctx, X_I_Product_BOM.Table_ID, this.get_TrxName());
        return poi;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer("X_I_Product_BOM[").append(this.get_ID()).append("]");
        return sb.toString();
    }
    
    public void setBOMQty(final BigDecimal BOMQty) {
        this.set_Value("BOMQty", (Object)BOMQty);
    }
    
    public BigDecimal getBOMQty() {
        final BigDecimal bd = (BigDecimal)this.get_Value("BOMQty");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }
    
    public void setBOMType(final String BOMType) {
        this.set_Value("BOMType", (Object)BOMType);
    }
    
    public String getBOMType() {
        return (String)this.get_Value("BOMType");
    }
    
    public void setBOMValue(final String BOMValue) {
        this.set_Value("BOMValue", (Object)BOMValue);
    }
    
    public String getBOMValue() {
        return (String)this.get_Value("BOMValue");
    }
    
    public void setDescription(final String Description) {
        this.set_Value("Description", (Object)Description);
    }
    
    public String getDescription() {
        return (String)this.get_Value("Description");
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
    
    public void setI_Product_BOM_ID(final int I_Product_BOM_ID) {
        if (I_Product_BOM_ID < 1) {
            this.set_ValueNoCheck("I_Product_BOM_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("I_Product_BOM_ID", (Object)I_Product_BOM_ID);
        }
    }
    
    public int getI_Product_BOM_ID() {
        final Integer ii = (Integer)this.get_Value("I_Product_BOM_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setI_Product_BOM_UU(final String I_Product_BOM_UU) {
        this.set_ValueNoCheck("I_Product_BOM_UU", (Object)I_Product_BOM_UU);
    }
    
    public String getI_Product_BOM_UU() {
        return (String)this.get_Value("I_Product_BOM_UU");
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
    
    public I_M_Product getM_ProductBOM() throws RuntimeException {
        return (I_M_Product)MTable.get(this.getCtx(), "M_Product").getPO(this.getM_ProductBOM_ID(), this.get_TrxName());
    }
    
    public void setM_ProductBOM_ID(final int M_ProductBOM_ID) {
        if (M_ProductBOM_ID < 1) {
            this.set_Value("M_ProductBOM_ID", (Object)null);
        }
        else {
            this.set_Value("M_ProductBOM_ID", (Object)M_ProductBOM_ID);
        }
    }
    
    public int getM_ProductBOM_ID() {
        final Integer ii = (Integer)this.get_Value("M_ProductBOM_ID");
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
    
    public void setValue(final String Value) {
        this.set_Value("Value", (Object)Value);
    }
    
    public String getValue() {
        return (String)this.get_Value("Value");
    }
}
