package org.kjs.pola.model;

import org.compiere.model.I_M_Product;
import java.sql.Timestamp;
import org.compiere.model.MTable;
import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

public interface I_KJS_ProductPhaseLine
{
    public static final String Table_Name = "KJS_ProductPhaseLine";
    public static final int Table_ID = MTable.getTable_ID("KJS_ProductPhaseLine");
    public static final KeyNamePair Model = new KeyNamePair(I_KJS_ProductPhaseLine.Table_ID, "KJS_ProductPhaseLine");
    public static final BigDecimal accessLevel = BigDecimal.valueOf(3L);
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";
    public static final String COLUMNNAME_BOMType = "BOMType";
    public static final String COLUMNNAME_Created = "Created";
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";
    public static final String COLUMNNAME_Duration = "Duration";
    public static final String COLUMNNAME_IsActive = "IsActive";
    public static final String COLUMNNAME_KJS_Phase_ID = "KJS_Phase_ID";
    public static final String COLUMNNAME_KJS_ProductPhase_ID = "KJS_ProductPhase_ID";
    public static final String COLUMNNAME_KJS_ProductPhaseLine_ID = "KJS_ProductPhaseLine_ID";
    public static final String COLUMNNAME_KJS_ProductPhaseLine_UU = "KJS_ProductPhaseLine_UU";
    public static final String COLUMNNAME_Line = "Line";
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";
    public static final String COLUMNNAME_Processed = "Processed";
    public static final String COLUMNNAME_Updated = "Updated";
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";
    
    int getAD_Client_ID();
    
    void setAD_Org_ID(final int p0);
    
    int getAD_Org_ID();
    
    void setBOMType(final String p0);
    
    String getBOMType();
    
    Timestamp getCreated();
    
    int getCreatedBy();
    
    void setDuration(final int p0);
    
    int getDuration();
    
    void setIsActive(final boolean p0);
    
    boolean isActive();
    
    void setKJS_Phase_ID(final int p0);
    
    int getKJS_Phase_ID();
    
    I_KJS_Phase getKJS_Phase() throws RuntimeException;
    
    void setKJS_ProductPhase_ID(final int p0);
    
    int getKJS_ProductPhase_ID();
    
    I_KJS_ProductPhase getKJS_ProductPhase() throws RuntimeException;
    
    void setKJS_ProductPhaseLine_ID(final int p0);
    
    int getKJS_ProductPhaseLine_ID();
    
    void setKJS_ProductPhaseLine_UU(final String p0);
    
    String getKJS_ProductPhaseLine_UU();
    
    void setLine(final int p0);
    
    int getLine();
    
    void setM_Product_ID(final int p0);
    
    int getM_Product_ID();
    
    I_M_Product getM_Product() throws RuntimeException;
    
    void setProcessed(final boolean p0);
    
    boolean isProcessed();
    
    Timestamp getUpdated();
    
    int getUpdatedBy();
}
