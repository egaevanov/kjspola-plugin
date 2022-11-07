package org.kjs.pola.model;

import org.compiere.model.I_M_Product;
import org.compiere.model.I_M_Locator;
import org.compiere.util.KeyNamePair;
import org.compiere.model.MTable;
import java.sql.Timestamp;
import org.compiere.util.Env;
import java.math.BigDecimal;
import org.compiere.model.POInfo;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.PO;

public class X_KJS_ProductionPlanLine extends PO implements I_KJS_ProductionPlanLine, I_Persistent
{
    private static final long serialVersionUID = 20190626L;
    
    public X_KJS_ProductionPlanLine(final Properties ctx, final int KJS_ProductionPlanLine_ID, final String trxName) {
        super(ctx, KJS_ProductionPlanLine_ID, trxName);
    }
    
    public X_KJS_ProductionPlanLine(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
    }
    
    protected int get_AccessLevel() {
        return X_KJS_ProductionPlanLine.accessLevel.intValue();
    }
    
    protected POInfo initPO(final Properties ctx) {
        final POInfo poi = POInfo.getPOInfo(ctx, X_KJS_ProductionPlanLine.Table_ID, this.get_TrxName());
        return poi;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer("X_KJS_ProductionPlanLine[").append(this.get_ID()).append("]");
        return sb.toString();
    }
    
    public void setBOMQty(final BigDecimal BOMQty) {
        this.set_Value("BOMQty", (Object)BOMQty);
    }
    
    public BigDecimal getBOMQty() {
        final BigDecimal bd = (BigDecimal)this.get_Value("BOMQty");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }
    
    public void setCreateFrom(final String CreateFrom) {
        this.set_Value("CreateFrom", (Object)CreateFrom);
    }
    
    public String getCreateFrom() {
        return (String)this.get_Value("CreateFrom");
    }
    
    public void setEndDate(final Timestamp EndDate) {
        this.set_Value("EndDate", (Object)EndDate);
    }
    
    public Timestamp getEndDate() {
        return (Timestamp)this.get_Value("EndDate");
    }
    
    public void setKJS_EstEndDate(final Timestamp KJS_EstEndDate) {
        this.set_Value("KJS_EstEndDate", (Object)KJS_EstEndDate);
    }
    
    public Timestamp getKJS_EstEndDate() {
        return (Timestamp)this.get_Value("KJS_EstEndDate");
    }
    
    public void setKJS_EstStartDate(final Timestamp KJS_EstStartDate) {
        this.set_Value("KJS_EstStartDate", (Object)KJS_EstStartDate);
    }
    
    public Timestamp getKJS_EstStartDate() {
        return (Timestamp)this.get_Value("KJS_EstStartDate");
    }
    
    public void setKJS_JumlahProses(final BigDecimal KJS_JumlahProses) {
        this.set_Value("KJS_JumlahProses", (Object)KJS_JumlahProses);
    }
    
    public BigDecimal getKJS_JumlahProses() {
        final BigDecimal bd = (BigDecimal)this.get_Value("KJS_JumlahProses");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }
    
    public I_KJS_Phase getKJS_Phase() throws RuntimeException {
        return (I_KJS_Phase)MTable.get(this.getCtx(), "KJS_Phase").getPO(this.getKJS_Phase_ID(), this.get_TrxName());
    }
    
    public void setKJS_Phase_ID(final int KJS_Phase_ID) {
        if (KJS_Phase_ID < 1) {
            this.set_ValueNoCheck("KJS_Phase_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("KJS_Phase_ID", (Object)KJS_Phase_ID);
        }
    }
    
    public int getKJS_Phase_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_Phase_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(this.get_ID(), String.valueOf(this.getKJS_Phase_ID()));
    }
    
    public I_KJS_ProductAsset getKJS_ProductAsset() throws RuntimeException {
        return (I_KJS_ProductAsset)MTable.get(this.getCtx(), "KJS_ProductAsset").getPO(this.getKJS_ProductAsset_ID(), this.get_TrxName());
    }
    
    public void setKJS_ProductAsset_ID(final int KJS_ProductAsset_ID) {
        if (KJS_ProductAsset_ID < 1) {
            this.set_Value("KJS_ProductAsset_ID", (Object)null);
        }
        else {
            this.set_Value("KJS_ProductAsset_ID", (Object)KJS_ProductAsset_ID);
        }
    }
    
    public int getKJS_ProductAsset_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_ProductAsset_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public I_KJS_ProductionPlan getKJS_ProductionPlan() throws RuntimeException {
        return (I_KJS_ProductionPlan)MTable.get(this.getCtx(), "KJS_ProductionPlan").getPO(this.getKJS_ProductionPlan_ID(), this.get_TrxName());
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
    
    public void setKJS_ProductionPlanLine_ID(final int KJS_ProductionPlanLine_ID) {
        if (KJS_ProductionPlanLine_ID < 1) {
            this.set_ValueNoCheck("KJS_ProductionPlanLine_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("KJS_ProductionPlanLine_ID", (Object)KJS_ProductionPlanLine_ID);
        }
    }
    
    public int getKJS_ProductionPlanLine_ID() {
        final Integer ii = (Integer)this.get_Value("KJS_ProductionPlanLine_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setKJS_ProductionPlanLine_UU(final String KJS_ProductionPlanLine_UU) {
        this.set_ValueNoCheck("KJS_ProductionPlanLine_UU", (Object)KJS_ProductionPlanLine_UU);
    }
    
    public String getKJS_ProductionPlanLine_UU() {
        return (String)this.get_Value("KJS_ProductionPlanLine_UU");
    }
    
    public void setLine(final int Line) {
        this.set_ValueNoCheck("Line", (Object)Line);
    }
    
    public int getLine() {
        final Integer ii = (Integer)this.get_Value("Line");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public I_M_Locator getM_Locator() throws RuntimeException {
        return (I_M_Locator)MTable.get(this.getCtx(), "M_Locator").getPO(this.getM_Locator_ID(), this.get_TrxName());
    }
    
    public void setM_Locator_ID(final int M_Locator_ID) {
        if (M_Locator_ID < 1) {
            this.set_ValueNoCheck("M_Locator_ID", (Object)null);
        }
        else {
            this.set_ValueNoCheck("M_Locator_ID", (Object)M_Locator_ID);
        }
    }
    
    public int getM_Locator_ID() {
        final Integer ii = (Integer)this.get_Value("M_Locator_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public I_M_Product getM_Product() throws RuntimeException {
        return (I_M_Product)MTable.get(this.getCtx(), "M_Product").getPO(this.getM_Product_ID(), this.get_TrxName());
    }
    
    public void setM_Product_ID(final int M_Product_ID) {
        if (M_Product_ID < 1) {
            this.set_Value("M_Product_ID", (Object)null);
        }
        else {
            this.set_Value("M_Product_ID", (Object)M_Product_ID);
        }
    }
    
    public int getM_Product_ID() {
        final Integer ii = (Integer)this.get_Value("M_Product_ID");
        if (ii == null) {
            return 0;
        }
        return ii;
    }
    
    public void setProcessed(final boolean Processed) {
        this.set_Value("Processed", (Object)Processed);
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
    
    public void setStartDate(final Timestamp StartDate) {
        this.set_Value("StartDate", (Object)StartDate);
    }
    
    public Timestamp getStartDate() {
        return (Timestamp)this.get_Value("StartDate");
    }
}
