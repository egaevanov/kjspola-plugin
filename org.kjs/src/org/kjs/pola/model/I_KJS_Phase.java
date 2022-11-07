package org.kjs.pola.model;

import java.sql.Timestamp;
import org.compiere.model.MTable;
import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

public interface I_KJS_Phase
{
    public static final String Table_Name = "KJS_Phase";
    public static final int Table_ID = MTable.getTable_ID("KJS_Phase");
    public static final KeyNamePair Model = new KeyNamePair(I_KJS_Phase.Table_ID, "KJS_Phase");
    public static final BigDecimal accessLevel = BigDecimal.valueOf(3L);
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";
    public static final String COLUMNNAME_Created = "Created";
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";
    public static final String COLUMNNAME_IsActive = "IsActive";
    public static final String COLUMNNAME_KJS_Phase_ID = "KJS_Phase_ID";
    public static final String COLUMNNAME_KJS_Phase_UU = "KJS_Phase_UU";
    public static final String COLUMNNAME_Name = "Name";
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
    
    void setKJS_Phase_UU(final String p0);
    
    String getKJS_Phase_UU();
    
    void setName(final String p0);
    
    String getName();
    
    void setProcessed(final boolean p0);
    
    boolean isProcessed();
    
    Timestamp getUpdated();
    
    int getUpdatedBy();
}
