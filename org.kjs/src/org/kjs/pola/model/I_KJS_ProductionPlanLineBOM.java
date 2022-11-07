package org.kjs.pola.model;

import org.compiere.model.I_M_Product;
import org.compiere.model.I_M_AttributeSetInstance;
import java.sql.Timestamp;
import org.compiere.model.MTable;
import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

public interface I_KJS_ProductionPlanLineBOM
{
    public static final String Table_Name = "KJS_ProductionPlanLineBOM";
    public static final int Table_ID = MTable.getTable_ID("KJS_ProductionPlanLineBOM");
    public static final KeyNamePair Model = new KeyNamePair(I_KJS_ProductionPlanLineBOM.Table_ID, "KJS_ProductionPlanLineBOM");
    public static final BigDecimal accessLevel = BigDecimal.valueOf(3L);
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";
    public static final String COLUMNNAME_Created = "Created";
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";
    public static final String COLUMNNAME_KJS_ProductionPlanLine_ID = "KJS_ProductionPlanLine_ID";
    public static final String COLUMNNAME_KJS_ProductionPlanLineBOM_ID = "KJS_ProductionPlanLineBOM_ID";
    public static final String COLUMNNAME_KJS_ProductionPlanLineBOM_UU = "KJS_ProductionPlanLineBOM_UU";
    public static final String COLUMNNAME_Line = "Line";
    public static final String COLUMNNAME_M_AttributeSetInstance_ID = "M_AttributeSetInstance_ID";
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";
    public static final String COLUMNNAME_Qty = "Qty";
    public static final String COLUMNNAME_Updated = "Updated";
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";
    
    int getAD_Client_ID();
    
    void setAD_Org_ID(final int p0);
    
    int getAD_Org_ID();
    
    Timestamp getCreated();
    
    int getCreatedBy();
    
    void setKJS_ProductionPlanLine_ID(final int p0);
    
    int getKJS_ProductionPlanLine_ID();
    
    I_KJS_ProductionPlanLine getKJS_ProductionPlanLine() throws RuntimeException;
    
    void setKJS_ProductionPlanLineBOM_ID(final int p0);
    
    int getKJS_ProductionPlanLineBOM_ID();
    
    void setKJS_ProductionPlanLineBOM_UU(final String p0);
    
    String getKJS_ProductionPlanLineBOM_UU();
    
    void setLine(final int p0);
    
    int getLine();
    
    void setM_AttributeSetInstance_ID(final int p0);
    
    int getM_AttributeSetInstance_ID();
    
    I_M_AttributeSetInstance getM_AttributeSetInstance() throws RuntimeException;
    
    void setM_Product_ID(final int p0);
    
    int getM_Product_ID();
    
    I_M_Product getM_Product() throws RuntimeException;
    
    void setQty(final BigDecimal p0);
    
    BigDecimal getQty();
    
    Timestamp getUpdated();
    
    int getUpdatedBy();
}
