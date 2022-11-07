package org.kjs.pola.model;

import java.math.BigDecimal;
import java.io.File;
import org.compiere.util.DB;
import org.compiere.process.DocumentEngine;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.process.DocAction;

public class MProductionPlanKJS extends X_KJS_ProductionPlan implements DocAction
{
    private static final long serialVersionUID = 8106460316172293611L;
    private String m_processMsg;
    private boolean m_justPrepared;
    
    public MProductionPlanKJS(final Properties ctx, final int KJS_ProductionPlan_ID, final String trxName) {
        super(ctx, KJS_ProductionPlan_ID, trxName);
        this.m_processMsg = null;
        this.m_justPrepared = false;
    }
    
    public MProductionPlanKJS(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
        this.m_processMsg = null;
        this.m_justPrepared = false;
    }
    
    public boolean processIt(final String action) throws Exception {
        this.m_processMsg = null;
        final DocumentEngine engine = new DocumentEngine((DocAction)this, this.getDocStatus());
        return engine.processIt(action, this.getDocAction());
    }
    
    public boolean unlockIt() {
        return true;
    }
    
    public boolean invalidateIt() {
        return true;
    }
    
    public String prepareIt() {
        this.m_justPrepared = true;
        return "IP";
    }
    
    public boolean approveIt() {
        return true;
    }
    
    public boolean rejectIt() {
        return true;
    }
    
    public String completeIt() {
        final String set = "UPDATE KJS_ProductionPlanLine SET Processed='Y' WHERE KJS_ProductionPlan_ID=?";
        DB.executeUpdate(set, this.getKJS_ProductionPlan_ID(), this.get_TrxName());
        this.setProcessed(true);
        this.setDocAction("CL");
        return "CO";
    }
    
    public boolean voidIt() {
        final String set = "UPDATE KJS_ProductionPlanLine SET Processed='Y' WHERE KJS_ProductionPlan_ID=?";
        DB.executeUpdate(set, this.getKJS_ProductionPlan_ID(), this.get_TrxName());
        this.setProcessed(true);
        return true;
    }
    
    public boolean closeIt() {
        return true;
    }
    
    public boolean reverseCorrectIt() {
        return true;
    }
    
    public boolean reverseAccrualIt() {
        return true;
    }
    
    public boolean reActivateIt() {
        return true;
    }
    
    public String getSummary() {
        return null;
    }
    
    public String getDocumentInfo() {
        return null;
    }
    
    public File createPDF() {
        return null;
    }
    
    public String getProcessMsg() {
        return null;
    }
    
    public int getDoc_User_ID() {
        return 0;
    }
    
    public int getC_Currency_ID() {
        return 0;
    }
    
    public BigDecimal getApprovalAmt() {
        return null;
    }
}
