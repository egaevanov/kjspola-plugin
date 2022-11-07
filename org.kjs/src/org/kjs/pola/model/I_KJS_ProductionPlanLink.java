package org.kjs.pola.model;

import org.compiere.model.I_M_RequisitionLine;
import org.compiere.model.I_M_Product;
import java.sql.Timestamp;
import org.compiere.model.I_C_OrderLine;
import org.compiere.model.MTable;
import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

public interface I_KJS_ProductionPlanLink
{
    public static final String Table_Name = "KJS_ProductionPlanLink";
    public static final int Table_ID = MTable.getTable_ID("KJS_ProductionPlanLink");
    public static final KeyNamePair Model = new KeyNamePair(I_KJS_ProductionPlanLink.Table_ID, "KJS_ProductionPlanLink");
    public static final BigDecimal accessLevel = BigDecimal.valueOf(3L);
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";
    public static final String COLUMNNAME_C_OrderLine_ID = "C_OrderLine_ID";
    public static final String COLUMNNAME_Created = "Created";
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";
    public static final String COLUMNNAME_KJS_ProductionPlan_ID = "KJS_ProductionPlan_ID";
    public static final String COLUMNNAME_KJS_ProductionPlanLink_ID = "KJS_ProductionPlanLink_ID";
    public static final String COLUMNNAME_KJS_ProductionPlanLink_UU = "KJS_ProductionPlanLink_UU";
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";
    public static final String COLUMNNAME_M_RequisitionLine_ID = "M_RequisitionLine_ID";
    public static final String COLUMNNAME_Qty = "Qty";
    public static final String COLUMNNAME_Updated = "Updated";
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";
    
    int getAD_Client_ID();
    
    void setAD_Org_ID(final int p0);
    
    int getAD_Org_ID();
    
    void setC_OrderLine_ID(final int p0);
    
    int getC_OrderLine_ID();
    
    I_C_OrderLine getC_OrderLine() throws RuntimeException;
    
    Timestamp getCreated();
    
    int getCreatedBy();
    
    void setKJS_ProductionPlan_ID(final int p0);
    
    int getKJS_ProductionPlan_ID();
    
    I_KJS_ProductionPlan getKJS_ProductionPlan() throws RuntimeException;
    
    void setKJS_ProductionPlanLink_ID(final int p0);
    
    int getKJS_ProductionPlanLink_ID();
    
    void setKJS_ProductionPlanLink_UU(final String p0);
    
    String getKJS_ProductionPlanLink_UU();
    
    void setM_Product_ID(final int p0);
    
    int getM_Product_ID();
    
    I_M_Product getM_Product() throws RuntimeException;
    
    void setM_RequisitionLine_ID(final int p0);
    
    int getM_RequisitionLine_ID();
    
    I_M_RequisitionLine getM_RequisitionLine() throws RuntimeException;
    
    void setQty(final BigDecimal p0);
    
    BigDecimal getQty();
    
    Timestamp getUpdated();
    
    int getUpdatedBy();
}
