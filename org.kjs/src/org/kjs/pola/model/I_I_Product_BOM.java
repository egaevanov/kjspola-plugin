package org.kjs.pola.model;

import org.compiere.model.I_M_Product;
import java.sql.Timestamp;
import org.compiere.model.MTable;
import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

public interface I_I_Product_BOM
{
    public static final String Table_Name = "I_Product_BOM";
    public static final int Table_ID = MTable.getTable_ID("I_Product_BOM");
    public static final KeyNamePair Model = new KeyNamePair(I_I_Product_BOM.Table_ID, "I_Product_BOM");
    public static final BigDecimal accessLevel = BigDecimal.valueOf(3L);
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";
    public static final String COLUMNNAME_BOMQty = "BOMQty";
    public static final String COLUMNNAME_BOMType = "BOMType";
    public static final String COLUMNNAME_BOMValue = "BOMValue";
    public static final String COLUMNNAME_Created = "Created";
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";
    public static final String COLUMNNAME_Description = "Description";
    public static final String COLUMNNAME_I_ErrorMsg = "I_ErrorMsg";
    public static final String COLUMNNAME_I_IsImported = "I_IsImported";
    public static final String COLUMNNAME_I_Product_BOM_ID = "I_Product_BOM_ID";
    public static final String COLUMNNAME_I_Product_BOM_UU = "I_Product_BOM_UU";
    public static final String COLUMNNAME_IsActive = "IsActive";
    public static final String COLUMNNAME_Line = "Line";
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";
    public static final String COLUMNNAME_M_ProductBOM_ID = "M_ProductBOM_ID";
    public static final String COLUMNNAME_Processed = "Processed";
    public static final String COLUMNNAME_Processing = "Processing";
    public static final String COLUMNNAME_Updated = "Updated";
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";
    public static final String COLUMNNAME_Value = "Value";
    
    int getAD_Client_ID();
    
    void setAD_Org_ID(final int p0);
    
    int getAD_Org_ID();
    
    void setBOMQty(final BigDecimal p0);
    
    BigDecimal getBOMQty();
    
    void setBOMType(final String p0);
    
    String getBOMType();
    
    void setBOMValue(final String p0);
    
    String getBOMValue();
    
    Timestamp getCreated();
    
    int getCreatedBy();
    
    void setDescription(final String p0);
    
    String getDescription();
    
    void setI_ErrorMsg(final String p0);
    
    String getI_ErrorMsg();
    
    void setI_IsImported(final boolean p0);
    
    boolean isI_IsImported();
    
    void setI_Product_BOM_ID(final int p0);
    
    int getI_Product_BOM_ID();
    
    void setI_Product_BOM_UU(final String p0);
    
    String getI_Product_BOM_UU();
    
    void setIsActive(final boolean p0);
    
    boolean isActive();
    
    void setLine(final int p0);
    
    int getLine();
    
    void setM_Product_ID(final int p0);
    
    int getM_Product_ID();
    
    I_M_Product getM_Product() throws RuntimeException;
    
    void setM_ProductBOM_ID(final int p0);
    
    int getM_ProductBOM_ID();
    
    I_M_Product getM_ProductBOM() throws RuntimeException;
    
    void setProcessed(final boolean p0);
    
    boolean isProcessed();
    
    void setProcessing(final boolean p0);
    
    boolean isProcessing();
    
    Timestamp getUpdated();
    
    int getUpdatedBy();
    
    void setValue(final String p0);
    
    String getValue();
}
