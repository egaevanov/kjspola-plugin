package org.kjs.pola.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MDocType;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPeriod;
import org.compiere.model.MPriceList;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MUser;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MRequisitionExt extends MRequisition
{
    private static final long serialVersionUID = 1L;
    private MRequisitionLine[] m_lines;
    private String m_processMsg;
    private boolean m_justPrepared;
    
    public MRequisitionExt(final Properties ctx, final int M_Requisition_ID, final String trxName) {
        super(ctx, M_Requisition_ID, trxName);
        this.m_lines = null;
        this.m_processMsg = null;
        this.m_justPrepared = false;
        if (M_Requisition_ID == 0) {
            this.setDateDoc(new Timestamp(System.currentTimeMillis()));
            this.setDateRequired(new Timestamp(System.currentTimeMillis()));
            this.setDocAction("CO");
            this.setDocStatus("DR");
            this.setPriorityRule("5");
            this.setTotalLines(Env.ZERO);
            this.setIsApproved(false);
            this.setPosted(false);
            this.setProcessed(false);
        }
    }
    
    public MRequisitionExt(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
        this.m_lines = null;
        this.m_processMsg = null;
        this.m_justPrepared = false;
    }
    
    public MRequisitionLine[] getLines() {
        if (this.m_lines != null) {
            set_TrxName((PO[])this.m_lines, this.get_TrxName());
            return this.m_lines;
        }
        final List<MRequisitionLine> list = new Query(this.getCtx(), "M_RequisitionLine", "M_Requisition_ID=?", this.get_TrxName()).setParameters(new Object[] { this.get_ID() }).setOrderBy("Line").list();
        list.toArray(this.m_lines = new MRequisitionLine[list.size()]);
        return this.m_lines;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder("MRequisition[");
        sb.append(this.get_ID()).append("-").append(this.getDocumentNo()).append(",Status=").append(this.getDocStatus()).append(",Action=").append(this.getDocAction()).append("]");
        return sb.toString();
    }
    
    public String getDocumentInfo() {
        return String.valueOf(Msg.getElement(this.getCtx(), "M_Requisition_ID")) + " " + this.getDocumentNo();
    }
    
    public File createPDF() {
        try {
            final File temp = File.createTempFile(String.valueOf(this.get_TableName()) + this.get_ID() + "_", ".pdf");
            return this.createPDF(temp);
        }
        catch (Exception e) {
            this.log.severe("Could not create PDF - " + e.getMessage());
            return null;
        }
    }
    
    public File createPDF(final File file) {
        return null;
    }
    
    public void setM_PriceList_ID() {
        MPriceList defaultPL = MPriceList.getDefault(this.getCtx(), false);
        if (defaultPL == null) {
            defaultPL = MPriceList.getDefault(this.getCtx(), true);
        }
        if (defaultPL != null) {
            this.setM_PriceList_ID(defaultPL.getM_PriceList_ID());
        }
    }
    
    protected boolean beforeSave(final boolean newRecord) {
        if (this.getM_PriceList_ID() == 0) {
            this.setM_PriceList_ID();
        }
        return true;
    }
    
    protected boolean beforeDelete() {
        MRequisitionLine[] lines;
        for (int length = (lines = this.getLines()).length, i = 0; i < length; ++i) {
            final MRequisitionLine line = lines[i];
            line.deleteEx(true);
        }
        return true;
    }
    
    public boolean processIt(final String processAction) {
        this.m_processMsg = null;
        final DocumentEngine engine = new DocumentEngine((DocAction)this, this.getDocStatus());
        return engine.processIt(processAction, this.getDocAction());
    }
    
    public boolean unlockIt() {
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("unlockIt - " + this.toString());
        }
        this.setProcessing(false);
        return true;
    }
    
    public boolean invalidateIt() {
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("invalidateIt - " + this.toString());
        }
        return true;
    }
    
    public String prepareIt() {
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info(this.toString());
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 1);
        if (this.m_processMsg != null) {
            return "IN";
        }
        final MRequisitionLine[] lines = this.getLines();
        if (this.getAD_User_ID() == 0 || this.getM_PriceList_ID() == 0 || this.getM_Warehouse_ID() == 0) {
            return "IN";
        }
        if (lines.length == 0) {
            throw new AdempiereException("@NoLines@");
        }
        MPeriod.testPeriodOpen(this.getCtx(), this.getDateDoc(), "POR", this.getAD_Org_ID());
        final int precision = MPriceList.getStandardPrecision(this.getCtx(), this.getM_PriceList_ID());
        BigDecimal totalLines = Env.ZERO;
        for (int i = 0; i < lines.length; ++i) {
            final MRequisitionLine line = lines[i];
            BigDecimal lineNet = line.getQty().multiply(line.getPriceActual());
            lineNet = lineNet.setScale(precision, 4);
            if (lineNet.compareTo(line.getLineNetAmt()) != 0) {
                line.setLineNetAmt(lineNet);
                line.saveEx();
            }
            totalLines = totalLines.add(line.getLineNetAmt());
        }
        if (totalLines.compareTo(this.getTotalLines()) != 0) {
            this.setTotalLines(totalLines);
            this.saveEx();
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 8);
        if (this.m_processMsg != null) {
            return "IN";
        }
        this.m_justPrepared = true;
        return "IP";
    }
    
    public boolean approveIt() {
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("approveIt - " + this.toString());
        }
        this.setIsApproved(true);
        return true;
    }
    
    public boolean rejectIt() {
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("rejectIt - " + this.toString());
        }
        this.setIsApproved(false);
        return true;
    }
    
    public String completeIt() {
        if (!this.m_justPrepared) {
            final String status = this.prepareIt();
            this.m_justPrepared = false;
            if (!"IP".equals(status)) {
                return status;
            }
        }
        this.setDefiniteDocumentNo();
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 7);
        if (this.m_processMsg != null) {
            return "IN";
        }
        if (!this.isApproved()) {
            this.approveIt();
        }
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info(this.toString());
        }
        final String valid = ModelValidationEngine.get().fireDocValidate((PO)this, 9);
        if (valid != null) {
            this.m_processMsg = valid;
            return "IN";
        }
        this.setProcessed(true);
        this.setDocAction("CL");
        return "CO";
    }
    
    private void setDefiniteDocumentNo() {
        final MDocType dt = MDocType.get(this.getCtx(), this.getC_DocType_ID());
        if (dt.isOverwriteDateOnComplete()) {
            this.setDateDoc(new Timestamp(System.currentTimeMillis()));
            MPeriod.testPeriodOpen(this.getCtx(), this.getDateDoc(), "POR", this.getAD_Org_ID());
        }
        if (dt.isOverwriteSeqOnComplete()) {
            final String value = DB.getDocumentNo(this.getC_DocType_ID(), this.get_TrxName(), true, (PO)this);
            if (value != null) {
                this.setDocumentNo(value);
            }
        }
    }
    
    public boolean voidIt() {
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("voidIt - " + this.toString());
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 2);
        if (this.m_processMsg != null) {
            return false;
        }
        if (!this.closeIt()) {
            return false;
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 10);
        return this.m_processMsg == null;
    }
    
    public boolean closeIt() {
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("closeIt - " + this.toString());
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 3);
        if (this.m_processMsg != null) {
            return false;
        }
        final MRequisitionLine[] lines = this.getLines();
        BigDecimal totalLines = Env.ZERO;
        for (int i = 0; i < lines.length; ++i) {
            final MRequisitionLine line = lines[i];
            BigDecimal finalQty = line.getQty();
            if (line.getC_OrderLine_ID() == 0) {
                finalQty = Env.ZERO;
            }
            else {
                final MOrderLine ol = new MOrderLine(this.getCtx(), line.getC_OrderLine_ID(), this.get_TrxName());
                finalQty = ol.getQtyOrdered();
            }
            if (finalQty.compareTo(line.getQty()) != 0) {
                String description = line.getDescription();
                if (description == null) {
                    description = "";
                }
                description = String.valueOf(description) + " [" + line.getQty() + "]";
                line.setDescription(description);
                line.setQty(finalQty);
                line.setLineNetAmt();
                line.saveEx();
            }
            totalLines = totalLines.add(line.getLineNetAmt());
        }
        if (totalLines.compareTo(this.getTotalLines()) != 0) {
            this.setTotalLines(totalLines);
            this.saveEx();
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 11);
        return this.m_processMsg == null;
    }
    
    public boolean reverseCorrectIt() {
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("reverseCorrectIt - " + this.toString());
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 5);
        if (this.m_processMsg != null) {
            return false;
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 13);
        return this.m_processMsg != null && false;
    }
    
    public boolean reverseAccrualIt() {
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("reverseAccrualIt - " + this.toString());
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 6);
        if (this.m_processMsg != null) {
            return false;
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 14);
        return this.m_processMsg != null && false;
    }
    
    public boolean reActivateIt() {
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("reActivateIt - " + this.toString());
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 4);
        if (this.m_processMsg != null) {
            return false;
        }
        if (!this.reverseCorrectIt()) {
            return false;
        }
        this.m_processMsg = ModelValidationEngine.get().fireDocValidate((PO)this, 12);
        return this.m_processMsg == null;
    }
    
    public String getSummary() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getDocumentNo());
        sb.append(" - ").append(this.getUserName());
        sb.append(": ").append(Msg.translate(this.getCtx(), "TotalLines")).append("=").append(this.getTotalLines()).append(" (#").append(this.getLines().length).append(")");
        if (this.getDescription() != null && this.getDescription().length() > 0) {
            sb.append(" - ").append(this.getDescription());
        }
        return sb.toString();
    }
    
    public String getProcessMsg() {
        return this.m_processMsg;
    }
    
    public int getDoc_User_ID() {
        return this.getAD_User_ID();
    }
    
    public int getC_Currency_ID() {
        final MPriceList pl = MPriceList.get(this.getCtx(), this.getM_PriceList_ID(), this.get_TrxName());
        return pl.getC_Currency_ID();
    }
    
    public BigDecimal getApprovalAmt() {
        return this.getTotalLines();
    }
    
    public String getUserName() {
        return MUser.get(this.getCtx(), this.getAD_User_ID()).getName();
    }
    
    public boolean isComplete() {
        final String ds = this.getDocStatus();
        return "CO".equals(ds) || "CL".equals(ds) || "RE".equals(ds);
    }
    
    public int copyLinesFrom(final MRequisition otherRequisition, final boolean counter) {
        if (this.isProcessed() || this.isPosted() || otherRequisition == null) {
            return 0;
        }
        final MRequisitionLine[] fromLines = otherRequisition.getLines();
        int count = 0;
        for (int i = 0; i < fromLines.length; ++i) {
            final MRequisitionLine line = new MRequisitionLine((MRequisition)this);
            PO.copyValues((PO)fromLines[i], (PO)line, this.getAD_Client_ID(), this.getAD_Org_ID());
            line.setM_Requisition_ID(this.getM_Requisition_ID());
            if (line.save(this.get_TrxName())) {
                ++count;
            }
        }
        if (fromLines.length != count) {
            this.log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
        }
        return count;
    }
}
