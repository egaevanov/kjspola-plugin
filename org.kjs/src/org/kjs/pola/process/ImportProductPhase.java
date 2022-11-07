package org.kjs.pola.process;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Statement;
import java.sql.SQLException;
import org.kjs.pola.model.X_KJS_ProductPhase;
import org.kjs.pola.model.X_KJS_ProductPhaseLine;
import org.kjs.pola.model.X_I_ProductPhase;
import org.compiere.model.PO;
import org.compiere.model.ModelValidationEngine;
import org.compiere.util.DB;
import org.compiere.process.ProcessInfoParameter;
import java.util.logging.Level;
import java.math.BigDecimal;
import org.adempiere.process.ImportProcess;
import org.compiere.process.SvrProcess;

public class ImportProductPhase extends SvrProcess implements ImportProcess
{
    private int m_AD_Client_ID;
    
    public ImportProductPhase() {
        this.m_AD_Client_ID = 0;
    }
    
    protected void prepare() {
        final ProcessInfoParameter[] para = this.getParameter();
        for (int i = 0; i < para.length; ++i) {
            final String name = para[i].getParameterName();
            if (name.equals("AD_Client_ID")) {
                this.m_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
            }
            else {
                this.log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
        }
    }
    
    protected String doIt() throws Exception {
        StringBuilder sql = null;
        int no = 0;
        final String clientCheck = this.getWhereClause();
        sql = new StringBuilder("UPDATE I_ProductPhase ").append("SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(this.m_AD_Client_ID).append("),").append(" AD_Org_ID = COALESCE (AD_Org_ID, 0),").append(" IsActive = COALESCE (IsActive, 'Y'),").append(" Created = COALESCE (Created, SysDate),").append(" CreatedBy = COALESCE (CreatedBy, 0),").append(" Updated = COALESCE (Updated, SysDate),").append(" UpdatedBy = COALESCE (UpdatedBy, 0),").append(" I_ErrorMsg = ' ',").append(" I_IsImported = 'N' ").append("WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("Reset=" + no);
        }
        ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)null, (PO)null, 10);
        sql = new StringBuilder("UPDATE I_ProductPhase i ").append("SET M_Product_ID=(SELECT M_Product_ID FROM M_Product p").append(" WHERE i.Value=p.Value AND i.AD_Client_ID=p.AD_Client_ID) ").append("WHERE M_Product_ID IS NULL").append(" AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("Product Phase Existing Value=" + no);
        }
        sql = new StringBuilder("UPDATE I_ProductPhase i ").append("SET M_ProductPhaseLine_ID=(SELECT M_Product_ID FROM M_Product p").append(" WHERE i.ProductPhaseLineValue=p.Value AND i.AD_Client_ID=p.AD_Client_ID) ").append("WHERE M_ProductPhaseLine_ID IS NULL").append(" AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("Product Phase Line Existing Value=" + no);
        }
        sql = new StringBuilder("UPDATE I_ProductPhase i ").append("SET KJS_Phase_ID=(SELECT KJS_Phase_ID FROM KJS_Phase p").append(" WHERE i.Name=p.Name AND i.AD_Client_ID=p.AD_Client_ID) ").append("WHERE KJS_Phase_ID IS NULL").append(" AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("Phase Existing Value=" + no);
        }
        this.commitEx();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sqlimport = null;
        sqlimport = new StringBuilder("SELECT * FROM I_ProductPhase WHERE I_IsImported='N'").append(clientCheck);
        PreparedStatement pstmt_setImported = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sqlimport.toString(), this.get_TrxName());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final X_I_ProductPhase imp = new X_I_ProductPhase(this.getCtx(), rs, this.get_TrxName());
                final int I_ProductPhase_ID = imp.getI_ProductPhase_ID();
                final int M_Product_ID = imp.getM_Product_ID();
                final String check = "SELECT COUNT(*) FROM KJS_ProductPhase WHERE M_Product_ID=?";
                PreparedStatement pstmtcheck = null;
                ResultSet rscheck = null;
                try {
                    pstmtcheck = (PreparedStatement)DB.prepareStatement(check.toString(), (String)null);
                    pstmtcheck.setInt(1, M_Product_ID);
                    rscheck = pstmtcheck.executeQuery();
                    while (rscheck.next()) {
                        if (rscheck.getInt(1) == 0) {
                            final X_KJS_ProductPhaseLine ppl = new X_KJS_ProductPhaseLine(this.getCtx(), rs, this.get_TrxName());
                            final X_KJS_ProductPhase pp = new X_KJS_ProductPhase(this.getCtx(), rs, this.get_TrxName());
                            pp.setM_Product_ID(imp.getM_Product_ID());
                            pp.setAD_Org_ID(imp.getAD_Org_ID());
                            pp.set_ValueNoCheck("CreatedBy", (Object)imp.getCreatedBy());
                            pp.set_ValueNoCheck("UpdatedBy", (Object)imp.getUpdatedBy());
                            pp.saveEx(this.get_TrxName());
                            ppl.setKJS_ProductPhase_ID(pp.getKJS_ProductPhase_ID());
                            ppl.setLine(imp.getLine());
                            ppl.setBOMType(imp.getBOMType());
                            ppl.setM_Product_ID(imp.getM_ProductPhaseLine_ID());
                            ppl.setKJS_Phase_ID(imp.getKJS_Phase_ID());
                            ppl.setAD_Org_ID(imp.getAD_Org_ID());
                            ppl.set_ValueNoCheck("CreatedBy", (Object)imp.getCreatedBy());
                            ppl.set_ValueNoCheck("UpdatedBy", (Object)imp.getUpdatedBy());
                            ppl.saveEx(this.get_TrxName());
                            imp.setKJS_ProductPhase_ID(pp.getKJS_ProductPhase_ID());
                            imp.saveEx(this.get_TrxName());
                            pstmt_setImported = (PreparedStatement)DB.prepareStatement("UPDATE I_ProductPhase SET I_IsImported='Y', M_Product_ID=?, Updated=SysDate, Processed='Y' WHERE I_ProductPhase_ID=?", this.get_TrxName());
                            pstmt_setImported.setInt(1, M_Product_ID);
                            pstmt_setImported.setInt(2, I_ProductPhase_ID);
                            no = pstmt_setImported.executeUpdate();
                            this.commitEx();
                        }
                        else {
                            final String ppID = "SELECT KJS_ProductPhase_ID FROM KJS_ProductPhase WHERE M_Product_ID=?";
                            PreparedStatement pstmtppID = null;
                            ResultSet rsppID = null;
                            try {
                                pstmtppID = (PreparedStatement)DB.prepareStatement(ppID.toString(), (String)null);
                                pstmtppID.setInt(1, M_Product_ID);
                                rsppID = pstmtppID.executeQuery();
                                while (rsppID.next()) {
                                    final X_KJS_ProductPhaseLine ppl2 = new X_KJS_ProductPhaseLine(this.getCtx(), rs, this.get_TrxName());
                                    ppl2.setKJS_ProductPhase_ID(rsppID.getInt(1));
                                    ppl2.setLine(imp.getLine());
                                    ppl2.setBOMType(imp.getBOMType());
                                    ppl2.setM_Product_ID(imp.getM_ProductPhaseLine_ID());
                                    ppl2.setKJS_Phase_ID(imp.getKJS_Phase_ID());
                                    ppl2.setAD_Org_ID(imp.getAD_Org_ID());
                                    ppl2.set_ValueNoCheck("CreatedBy", (Object)imp.getCreatedBy());
                                    ppl2.set_ValueNoCheck("UpdatedBy", (Object)imp.getUpdatedBy());
                                    ppl2.saveEx(this.get_TrxName());
                                    imp.setKJS_ProductPhase_ID(rsppID.getInt(1));
                                    imp.saveEx(this.get_TrxName());
                                    pstmt_setImported = (PreparedStatement)DB.prepareStatement("UPDATE I_ProductPhase SET I_IsImported='Y', M_Product_ID=?, Updated=SysDate, Processed='Y' WHERE I_ProductPhase_ID=?", this.get_TrxName());
                                    pstmt_setImported.setInt(1, M_Product_ID);
                                    pstmt_setImported.setInt(2, I_ProductPhase_ID);
                                    no = pstmt_setImported.executeUpdate();
                                    this.commitEx();
                                }
                            }
                            catch (SQLException e) {
                                this.log.log(Level.SEVERE, ppID.toString(), (Throwable)e);
                                continue;
                            }
                            finally {
                                DB.close(rsppID, (Statement)pstmtppID);
                                rsppID = null;
                                pstmtppID = null;
                            }
                            DB.close(rsppID, (Statement)pstmtppID);
                            rsppID = null;
                            pstmtppID = null;
                        }
                    }
                }
                catch (SQLException e2) {
                    this.log.log(Level.SEVERE, check.toString(), (Throwable)e2);
                    continue;
                }
                finally {
                    DB.close(rscheck, (Statement)pstmtcheck);
                    rscheck = null;
                    pstmtcheck = null;
                }
                DB.close(rscheck, (Statement)pstmtcheck);
                rscheck = null;
                pstmtcheck = null;
            }
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
            DB.close((Statement)pstmt_setImported);
            pstmt_setImported = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        DB.close((Statement)pstmt_setImported);
        pstmt_setImported = null;
        sql = new StringBuilder("UPDATE I_ProductPhase ").append("SET I_IsImported='N', Updated=SysDate ").append("WHERE I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        this.addLog(0, (Timestamp)null, new BigDecimal(no), "@Errors@");
        return "";
    }
    
    public String getImportTableName() {
        return "I_ProductPhase";
    }
    
    public String getWhereClause() {
        final StringBuilder msgreturn = new StringBuilder(" AND AD_Client_ID=").append(this.m_AD_Client_ID);
        return msgreturn.toString();
    }
}