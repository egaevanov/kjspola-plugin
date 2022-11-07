package org.kjs.pola.model;

import org.compiere.model.I_M_Product;
import java.sql.Timestamp;
import org.compiere.model.MTable;
import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

public interface I_I_ProductPhase
{
    public static final String Table_Name = "I_ProductPhase";
    public static final int Table_ID = MTable.getTable_ID("I_ProductPhase");
    public static final KeyNamePair Model = new KeyNamePair(I_I_ProductPhase.Table_ID, "I_ProductPhase");
    public static final BigDecimal accessLevel = BigDecimal.valueOf(3L);
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";
    public static final String COLUMNNAME_BOMType = "BOMType";
    public static final String COLUMNNAME_Created = "Created";
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";
    public static final String COLUMNNAME_I_ErrorMsg = "I_ErrorMsg";
    public static final String COLUMNNAME_I_IsImported = "I_IsImported";
    public static final String COLUMNNAME_I_ProductPhase_ID = "I_ProductPhase_ID";
    public static final String COLUMNNAME_I_ProductPhase_UU = "I_ProductPhase_UU";
    public static final String COLUMNNAME_IsActive = "IsActive";
    public static final String COLUMNNAME_KJS_Phase_ID = "KJS_Phase_ID";
    public static final String COLUMNNAME_KJS_ProductPhase_ID = "KJS_ProductPhase_ID";
    public static final String COLUMNNAME_Line = "Line";
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";
    public static final String COLUMNNAME_M_ProductPhaseLine_ID = "M_ProductPhaseLine_ID";
    public static final String COLUMNNAME_Name = "Name";
    public static final String COLUMNNAME_Processed = "Processed";
    public static final String COLUMNNAME_Processing = "Processing";
    public static final String COLUMNNAME_ProductPhaseLineValue = "ProductPhaseLineValue";
    public static final String COLUMNNAME_Updated = "Updated";
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";
    public static final String COLUMNNAME_Value = "Value";
    
    int getAD_Client_ID();
    
    void setAD_Org_ID(final int p0);
    
    int getAD_Org_ID();
    
    void setBOMType(final String p0);
    
    String getBOMType();
    
    Timestamp getCreated();
    
    int getCreatedBy();
    
    void setI_ErrorMsg(final String p0);
    
    String getI_ErrorMsg();
    
    void setI_IsImported(final boolean p0);
    
    boolean isI_IsImported();
    
    void setI_ProductPhase_ID(final int p0);
    
    int getI_ProductPhase_ID();
    
    void setI_ProductPhase_UU(final String p0);
    
    String getI_ProductPhase_UU();
    
    void setIsActive(final boolean p0);
    
    boolean isActive();
    
    void setKJS_Phase_ID(final int p0);
    
    int getKJS_Phase_ID();
    
    I_KJS_Phase getKJS_Phase() throws RuntimeException;
    
    void setKJS_ProductPhase_ID(final int p0);
    
    int getKJS_ProductPhase_ID();
    
    I_KJS_ProductPhase getKJS_ProductPhase() throws RuntimeException;
    
    void setLine(final int p0);
    
    int getLine();
    
    void setM_Product_ID(final int p0);
    
    int getM_Product_ID();
    
    I_M_Product getM_Product() throws RuntimeException;
    
    void setM_ProductPhaseLine_ID(final int p0);
    
    int getM_ProductPhaseLine_ID();
    
    I_M_Product getM_ProductPhaseLine() throws RuntimeException;
    
    void setName(final String p0);
    
    String getName();
    
    void setProcessed(final boolean p0);
    
    boolean isProcessed();
    
    void setProcessing(final boolean p0);
    
    boolean isProcessing();
    
    void setProductPhaseLineValue(final String p0);
    
    String getProductPhaseLineValue();
    
    Timestamp getUpdated();
    
    int getUpdatedBy();
    
    void setValue(final String p0);
    
    String getValue();
}
