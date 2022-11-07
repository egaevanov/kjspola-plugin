package org.kjs.pola.model;

import org.compiere.model.I_M_Product;
import org.compiere.model.I_M_Locator;
import java.sql.Timestamp;
import org.compiere.model.MTable;
import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

public interface I_KJS_ProductionPlanLine
{
    public static final String Table_Name = "KJS_ProductionPlanLine";
    public static final int Table_ID = MTable.getTable_ID("KJS_ProductionPlanLine");
    public static final KeyNamePair Model = new KeyNamePair(I_KJS_ProductionPlanLine.Table_ID, "KJS_ProductionPlanLine");
    public static final BigDecimal accessLevel = BigDecimal.valueOf(3L);
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";
    public static final String COLUMNNAME_BOMQty = "BOMQty";
    public static final String COLUMNNAME_Created = "Created";
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";
    public static final String COLUMNNAME_CreateFrom = "CreateFrom";
    public static final String COLUMNNAME_EndDate = "EndDate";
    public static final String COLUMNNAME_IsActive = "IsActive";
    public static final String COLUMNNAME_KJS_EstEndDate = "KJS_EstEndDate";
    public static final String COLUMNNAME_KJS_EstStartDate = "KJS_EstStartDate";
    public static final String COLUMNNAME_KJS_JumlahProses = "KJS_JumlahProses";
    public static final String COLUMNNAME_KJS_Phase_ID = "KJS_Phase_ID";
    public static final String COLUMNNAME_KJS_ProductAsset_ID = "KJS_ProductAsset_ID";
    public static final String COLUMNNAME_KJS_ProductionPlan_ID = "KJS_ProductionPlan_ID";
    public static final String COLUMNNAME_KJS_ProductionPlanLine_ID = "KJS_ProductionPlanLine_ID";
    public static final String COLUMNNAME_KJS_ProductionPlanLine_UU = "KJS_ProductionPlanLine_UU";
    public static final String COLUMNNAME_Line = "Line";
    public static final String COLUMNNAME_M_Locator_ID = "M_Locator_ID";
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";
    public static final String COLUMNNAME_Processed = "Processed";
    public static final String COLUMNNAME_StartDate = "StartDate";
    public static final String COLUMNNAME_Updated = "Updated";
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";
    
    int getAD_Client_ID();
    
    void setAD_Org_ID(final int p0);
    
    int getAD_Org_ID();
    
    void setBOMQty(final BigDecimal p0);
    
    BigDecimal getBOMQty();
    
    Timestamp getCreated();
    
    int getCreatedBy();
    
    void setCreateFrom(final String p0);
    
    String getCreateFrom();
    
    void setEndDate(final Timestamp p0);
    
    Timestamp getEndDate();
    
    void setIsActive(final boolean p0);
    
    boolean isActive();
    
    void setKJS_EstEndDate(final Timestamp p0);
    
    Timestamp getKJS_EstEndDate();
    
    void setKJS_EstStartDate(final Timestamp p0);
    
    Timestamp getKJS_EstStartDate();
    
    void setKJS_JumlahProses(final BigDecimal p0);
    
    BigDecimal getKJS_JumlahProses();
    
    void setKJS_Phase_ID(final int p0);
    
    int getKJS_Phase_ID();
    
    I_KJS_Phase getKJS_Phase() throws RuntimeException;
    
    void setKJS_ProductAsset_ID(final int p0);
    
    int getKJS_ProductAsset_ID();
    
    I_KJS_ProductAsset getKJS_ProductAsset() throws RuntimeException;
    
    void setKJS_ProductionPlan_ID(final int p0);
    
    int getKJS_ProductionPlan_ID();
    
    I_KJS_ProductionPlan getKJS_ProductionPlan() throws RuntimeException;
    
    void setKJS_ProductionPlanLine_ID(final int p0);
    
    int getKJS_ProductionPlanLine_ID();
    
    void setKJS_ProductionPlanLine_UU(final String p0);
    
    String getKJS_ProductionPlanLine_UU();
    
    void setLine(final int p0);
    
    int getLine();
    
    void setM_Locator_ID(final int p0);
    
    int getM_Locator_ID();
    
    I_M_Locator getM_Locator() throws RuntimeException;
    
    void setM_Product_ID(final int p0);
    
    int getM_Product_ID();
    
    I_M_Product getM_Product() throws RuntimeException;
    
    void setProcessed(final boolean p0);
    
    boolean isProcessed();
    
    void setStartDate(final Timestamp p0);
    
    Timestamp getStartDate();
    
    Timestamp getUpdated();
    
    int getUpdatedBy();
}
