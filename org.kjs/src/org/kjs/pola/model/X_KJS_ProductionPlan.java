package org.kjs.pola.model;

import org.compiere.util.Env;
import java.math.BigDecimal;
import org.compiere.model.I_M_RequisitionLine;
import org.compiere.model.I_M_Requisition;
import org.compiere.model.I_M_Product;
import org.compiere.util.KeyNamePair;
import java.sql.Timestamp;
import org.compiere.model.I_C_OrderLine;
import org.compiere.model.MTable;
import org.compiere.model.I_C_Order;
import org.compiere.model.POInfo;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.PO;

public class X_KJS_ProductionPlan extends PO implements I_KJS_ProductionPlan, I_Persistent
{
    private static final long serialVersionUID = 20190411L;
    public static final int DOCACTION_AD_Reference_ID = 135;
    public static final String DOCACTION_Complete = "CO";
    public static final String DOCACTION_Approve = "AP";
    public static final String DOCACTION_Reject = "RJ";
    public static final String DOCACTION_Post = "PO";
    public static final String DOCACTION_Void = "VO";
    public static final String DOCACTION_Close = "CL";
    public static final String DOCACTION_Reverse_Correct = "RC";
    public static final String DOCACTION_Reverse_Accrual = "RA";
    public static final String DOCACTION_Invalidate = "IN";
    public static final String DOCACTION_Re_Activate = "RE";
    public static final String DOCACTION_None = "--";
    public static final String DOCACTION_Prepare = "PR";
    public static final String DOCACTION_Unlock = "XL";
    public static final String DOCACTION_WaitComplete = "WC";
    public static final int DOCSTATUS_AD_Reference_ID = 131;
    public static final String DOCSTATUS_Drafted = "DR";
    public static final String DOCSTATUS_Completed = "CO";
    public static final String DOCSTATUS_Approved = "AP";
    public static final String DOCSTATUS_NotApproved = "NA";
    public static final String DOCSTATUS_Voided = "VO";
    public static final String DOCSTATUS_Invalid = "IN";
    public static final String DOCSTATUS_Reversed = "RE";
    public static final String DOCSTATUS_Closed = "CL";
    public static final String DOCSTATUS_Unknown = "??";
    public static final String DOCSTATUS_InProgress = "IP";
    public static final String DOCSTATUS_WaitingPayment = "WP";
    public static final String DOCSTATUS_WaitingConfirmation = "WC";
    
    public X_KJS_ProductionPlan(final Properties ctx, final int KJS_ProductionPlan_ID, final String trxName) {
        super(ctx, KJS_ProductionPlan_ID, trxName);
    }
    
    public X_KJS_ProductionPlan(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
    }
    
    protected int get_AccessLevel() {
        return X_KJS_ProductionPlan.accessLevel.intValue();
    }
    
    protected POInfo initPO(final Properties ctx) {
        final POInfo poi = POInfo.getPOInfo(ctx, X_KJS_ProductionPlan.Table_ID, this.get_TrxName());
        return poi;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer("X_KJS_ProductionPlan[").append(this.get_ID()).append("]");
        return sb.toString();
    }
    
    public I_C_Order getC_Order() throws RuntimeException {
        return (I_C_Order)MTable.get(this.getCtx(), "C_Order").getPO(this.getC_Order_ID(), this.get_TrxName());
    }
    
    public void setC_Order_ID(final int C_Order_ID) {
        if (C_Order_ID < 1) {
            this.set_ValueNoCheck("C_Order_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("C_Order_ID", (Object)C_Order_ID);
        }
    }
    
    public int getC_Order_ID() {
        final Integer ii = (Integer)this.get_Value("C_Order_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public I_C_OrderLine getC_OrderLine() throws RuntimeException {
        return (I_C_OrderLine)MTable.get(this.getCtx(), "C_OrderLine").getPO(this.getC_OrderLine_ID(), this.get_TrxName());
    }
    
    public void setC_OrderLine_ID(final int C_OrderLine_ID) {
        if (C_OrderLine_ID < 1) {
            this.set_ValueNoCheck("C_OrderLine_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("C_OrderLine_ID", (Object)C_OrderLine_ID);
        }
    }
    
    public int getC_OrderLine_ID() {
        final Integer ii = (Integer)this.get_Value("C_OrderLine_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setCreateFrom(final String CreateFrom) {
        this.set_Value("CreateFrom", (Object)CreateFrom);
    }
    
    public String getCreateFrom() {
        return (String)this.get_Value("CreateFrom");
    }
    
    public void setDateDoc(final Timestamp DateDoc) {
        this.set_Value("DateDoc", (Object)DateDoc);
    }
    
    public Timestamp getDateDoc() {
        return (Timestamp)this.get_Value("DateDoc");
    }
    
    public void setDocAction(final String DocAction) {
        this.set_Value("DocAction", (Object)DocAction);
    }
    
    public String getDocAction() {
        return (String)this.get_Value("DocAction");
    }
    
    public void setDocStatus(final String DocStatus) {
        this.set_Value("DocStatus", (Object)DocStatus);
    }
    
    public String getDocStatus() {
        return (String)this.get_Value("DocStatus");
    }
    
    public void setDocumentNo(final String DocumentNo) {
        this.set_ValueNoCheck("DocumentNo", (Object)DocumentNo);
    }
    
    public String getDocumentNo() {
        return (String)this.get_Value("DocumentNo");
    }
    
    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(this.get_ID(), this.getDocumentNo());
    }
    
    public void setKJS_ProductionPlan_ID(final int KJS_ProductionPlan_ID) {
        if (KJS_ProductionPlan_ID < 1) {
            this.set_ValueNoCheck("KJS_ProductionPlan_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("KJS_ProductionPlan_ID", (Object)KJS_ProductionPlan_ID);
        }
    }
    
    public int getKJS_ProductionPlan_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_ProductionPlan_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setKJS_ProductionPlan_UU(final String KJS_ProductionPlan_UU) {
        this.set_ValueNoCheck("KJS_ProductionPlan_UU", (Object)KJS_ProductionPlan_UU);
    }
    
    public String getKJS_ProductionPlan_UU() {
        return (String)this.get_Value("KJS_ProductionPlan_UU");
    }
    
    public I_M_Product getM_Product() throws RuntimeException {
        return (I_M_Product)MTable.get(this.getCtx(), "M_Product").getPO(this.getM_Product_ID(), this.get_TrxName());
    }
    
    public void setM_Product_ID(final int M_Product_ID) {
        if (M_Product_ID < 1) {
            this.set_ValueNoCheck("M_Product_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("M_Product_ID", (Object)M_Product_ID);
        }
    }
    
    public int getM_Product_ID() {
        final Integer ii = (Integer)this.get_Value("M_Product_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public I_M_Requisition getM_Requisition() throws RuntimeException {
        return (I_M_Requisition)MTable.get(this.getCtx(), "M_Requisition").getPO(this.getM_Requisition_ID(), this.get_TrxName());
    }
    
    public void setM_Requisition_ID(final int M_Requisition_ID) {
        if (M_Requisition_ID < 1) {
            this.set_Value("M_Requisition_ID", (Object)null);
        }
        else {
            this.set_Value("M_Requisition_ID", (Object)M_Requisition_ID);
        }
    }
    
    public int getM_Requisition_ID() {
        final Integer ii = (Integer)this.get_Value("M_Requisition_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public I_M_RequisitionLine getM_RequisitionLine() throws RuntimeException {
        return (I_M_RequisitionLine)MTable.get(this.getCtx(), "M_RequisitionLine").getPO(this.getM_RequisitionLine_ID(), this.get_TrxName());
    }
    
    public void setM_RequisitionLine_ID(final int M_RequisitionLine_ID) {
        if (M_RequisitionLine_ID < 1) {
            this.set_Value("M_RequisitionLine_ID", (Object)null);
        }
        else {
            this.set_Value("M_RequisitionLine_ID", (Object)M_RequisitionLine_ID);
        }
    }
    
    public int getM_RequisitionLine_ID() {
        final Integer ii = (Integer)this.get_Value("M_RequisitionLine_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setProcessed(final boolean Processed) {
        this.set_ValueNoCheck("Processed", (Object)Processed);
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
    
    public void setQtyEntered(final BigDecimal QtyEntered) {
        this.set_ValueNoCheck("QtyEntered", (Object)QtyEntered);
    }
    
    public BigDecimal getQtyEntered() {
        final BigDecimal bd = (BigDecimal)this.get_Value("QtyEntered");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }
    
    public void setQtyOrdered(final BigDecimal QtyOrdered) {
        throw new IllegalArgumentException("QtyOrdered is virtual column");
    }
    
    public BigDecimal getQtyOrdered() {
        final BigDecimal bd = (BigDecimal)this.get_Value("QtyOrdered");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }
}
