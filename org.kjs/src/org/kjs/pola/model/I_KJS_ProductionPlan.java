package org.kjs.pola.model;

import org.compiere.model.I_M_RequisitionLine;
import org.compiere.model.I_M_Requisition;
import org.compiere.model.I_M_Product;
import java.sql.Timestamp;
import org.compiere.model.I_C_OrderLine;
import org.compiere.model.I_C_Order;
import org.compiere.model.MTable;
import java.math.BigDecimal;
import org.compiere.util.KeyNamePair;

public interface I_KJS_ProductionPlan
{
    public static final String Table_Name = "KJS_ProductionPlan";
    public static final int Table_ID = MTable.getTable_ID("KJS_ProductionPlan");
    public static final KeyNamePair Model = new KeyNamePair(I_KJS_ProductionPlan.Table_ID, "KJS_ProductionPlan");
    public static final BigDecimal accessLevel = BigDecimal.valueOf(3L);
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";
    public static final String COLUMNNAME_C_Order_ID = "C_Order_ID";
    public static final String COLUMNNAME_C_OrderLine_ID = "C_OrderLine_ID";
    public static final String COLUMNNAME_Created = "Created";
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";
    public static final String COLUMNNAME_CreateFrom = "CreateFrom";
    public static final String COLUMNNAME_DateDoc = "DateDoc";
    public static final String COLUMNNAME_DocAction = "DocAction";
    public static final String COLUMNNAME_DocStatus = "DocStatus";
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";
    public static final String COLUMNNAME_IsActive = "IsActive";
    public static final String COLUMNNAME_KJS_ProductionPlan_ID = "KJS_ProductionPlan_ID";
    public static final String COLUMNNAME_KJS_ProductionPlan_UU = "KJS_ProductionPlan_UU";
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";
    public static final String COLUMNNAME_M_Requisition_ID = "M_Requisition_ID";
    public static final String COLUMNNAME_M_RequisitionLine_ID = "M_RequisitionLine_ID";
    public static final String COLUMNNAME_Processed = "Processed";
    public static final String COLUMNNAME_Processing = "Processing";
    public static final String COLUMNNAME_QtyEntered = "QtyEntered";
    public static final String COLUMNNAME_QtyOrdered = "QtyOrdered";
    public static final String COLUMNNAME_Updated = "Updated";
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";
    
    int getAD_Client_ID();
    
    void setAD_Org_ID(final int p0);
    
    int getAD_Org_ID();
    
    void setC_Order_ID(final int p0);
    
    int getC_Order_ID();
    
    I_C_Order getC_Order() throws RuntimeException;
    
    void setC_OrderLine_ID(final int p0);
    
    int getC_OrderLine_ID();
    
    I_C_OrderLine getC_OrderLine() throws RuntimeException;
    
    Timestamp getCreated();
    
    int getCreatedBy();
    
    void setCreateFrom(final String p0);
    
    String getCreateFrom();
    
    void setDateDoc(final Timestamp p0);
    
    Timestamp getDateDoc();
    
    void setDocAction(final String p0);
    
    String getDocAction();
    
    void setDocStatus(final String p0);
    
    String getDocStatus();
    
    void setDocumentNo(final String p0);
    
    String getDocumentNo();
    
    void setIsActive(final boolean p0);
    
    boolean isActive();
    
    void setKJS_ProductionPlan_ID(final int p0);
    
    int getKJS_ProductionPlan_ID();
    
    void setKJS_ProductionPlan_UU(final String p0);
    
    String getKJS_ProductionPlan_UU();
    
    void setM_Product_ID(final int p0);
    
    int getM_Product_ID();
    
    I_M_Product getM_Product() throws RuntimeException;
    
    void setM_Requisition_ID(final int p0);
    
    int getM_Requisition_ID();
    
    I_M_Requisition getM_Requisition() throws RuntimeException;
    
    void setM_RequisitionLine_ID(final int p0);
    
    int getM_RequisitionLine_ID();
    
    I_M_RequisitionLine getM_RequisitionLine() throws RuntimeException;
    
    void setProcessed(final boolean p0);
    
    boolean isProcessed();
    
    void setProcessing(final boolean p0);
    
    boolean isProcessing();
    
    void setQtyEntered(final BigDecimal p0);
    
    BigDecimal getQtyEntered();
    
    void setQtyOrdered(final BigDecimal p0);
    
    BigDecimal getQtyOrdered();
    
    Timestamp getUpdated();
    
    int getUpdatedBy();
}
