package org.kjs.pola.model;

import org.compiere.model.I_M_Product;
import java.sql.Timestamp;
import org.compiere.model.MTable;
import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

public interface I_KJS_ProductAsset
{
    public static final String Table_Name = "KJS_ProductAsset";
    public static final int Table_ID = MTable.getTable_ID("KJS_ProductAsset");
    public static final KeyNamePair Model = new KeyNamePair(I_KJS_ProductAsset.Table_ID, "KJS_ProductAsset");
    public static final BigDecimal accessLevel = BigDecimal.valueOf(3L);
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";
    public static final String COLUMNNAME_Created = "Created";
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";
    public static final String COLUMNNAME_IsActive = "IsActive";
    public static final String COLUMNNAME_KJS_Phase_ID = "KJS_Phase_ID";
    public static final String COLUMNNAME_KJS_ProductAsset_ID = "KJS_ProductAsset_ID";
    public static final String COLUMNNAME_KJS_ProductAsset_UU = "KJS_ProductAsset_UU";
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";
    public static final String COLUMNNAME_Processed = "Processed";
    public static final String COLUMNNAME_Updated = "Updated";
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";
    
    int getAD_Client_ID();
    
    void setAD_Org_ID(final int p0);
    
    int getAD_Org_ID();
    
    Timestamp getCreated();
    
    int getCreatedBy();
    
    void setIsActive(final boolean p0);
    
    boolean isActive();
    
    void setKJS_Phase_ID(final int p0);
    
    int getKJS_Phase_ID();
    
    I_KJS_Phase getKJS_Phase() throws RuntimeException;
    
    void setKJS_ProductAsset_ID(final int p0);
    
    int getKJS_ProductAsset_ID();
    
    void setKJS_ProductAsset_UU(final String p0);
    
    String getKJS_ProductAsset_UU();
    
    void setM_Product_ID(final int p0);
    
    int getM_Product_ID();
    
    I_M_Product getM_Product() throws RuntimeException;
    
    void setProcessed(final boolean p0);
    
    boolean isProcessed();
    
    Timestamp getUpdated();
    
    int getUpdatedBy();
}
