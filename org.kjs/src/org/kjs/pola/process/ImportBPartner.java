package org.kjs.pola.process;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.adempiere.exceptions.DBException;
import java.sql.Statement;
import org.compiere.model.MContactInterest;
import org.compiere.model.X_C_BPartner;
import org.compiere.model.MUser;
import org.compiere.model.MLocation;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MBPartner;
import org.compiere.model.X_I_BPartner;
import org.compiere.model.PO;
import org.compiere.model.ModelValidationEngine;
import org.compiere.util.DB;
import org.compiere.process.ProcessInfoParameter;
import java.util.logging.Level;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.adempiere.process.ImportProcess;
import org.compiere.process.SvrProcess;

public class ImportBPartner extends SvrProcess implements ImportProcess
{
    private int m_AD_Client_ID;
    private boolean m_deleteOldImported;
    private boolean p_IsValidateOnly;
    private Timestamp m_DateValue;
    
    public ImportBPartner() {
        this.m_AD_Client_ID = 0;
        this.m_deleteOldImported = false;
        this.p_IsValidateOnly = false;
        this.m_DateValue = null;
    }
    
    protected void prepare() {
        final ProcessInfoParameter[] para = this.getParameter();
        for (int i = 0; i < para.length; ++i) {
            final String name = para[i].getParameterName();
            if (name.equals("AD_Client_ID")) {
                this.m_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
            }
            else if (name.equals("DeleteOldImported")) {
                this.m_deleteOldImported = "Y".equals(para[i].getParameter());
            }
            else if (name.equals("IsValidateOnly")) {
                this.p_IsValidateOnly = para[i].getParameterAsBoolean();
            }
            else {
                this.log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
        }
        if (this.m_DateValue == null) {
            this.m_DateValue = new Timestamp(System.currentTimeMillis());
        }
    }
    
    protected String doIt() throws Exception {
        StringBuilder sql = null;
        int no = 0;
        final String clientCheck = this.getWhereClause();
        if (this.m_deleteOldImported) {
            sql = new StringBuilder("DELETE I_BPartner ").append("WHERE I_IsImported='Y'").append(clientCheck);
            no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
            if (this.log.isLoggable(Level.FINE)) {
                this.log.fine("Delete Old Impored =" + no);
            }
        }
        sql = new StringBuilder("UPDATE I_BPartner ").append("SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(this.m_AD_Client_ID).append("),").append(" AD_Org_ID = COALESCE (AD_Org_ID, 0),").append(" IsActive = COALESCE (IsActive, 'Y'),").append(" Created = COALESCE (Created, SysDate),").append(" CreatedBy = COALESCE (CreatedBy, 0),").append(" Updated = COALESCE (Updated, SysDate),").append(" UpdatedBy = COALESCE (UpdatedBy, 0),").append(" I_ErrorMsg = ' ',").append(" I_IsImported = 'N' ").append("WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Reset=" + no);
        }
        ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)null, (PO)null, 10);
        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET GroupValue=(SELECT MAX(Value) FROM C_BP_Group g WHERE g.IsDefault='Y'").append(" AND g.AD_Client_ID=i.AD_Client_ID) ");
        sql.append("WHERE GroupValue IS NULL AND C_BP_Group_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Set Group Default=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET C_BP_Group_ID=(SELECT C_BP_Group_ID FROM C_BP_Group g").append(" WHERE i.GroupValue=g.Value AND g.AD_Client_ID=i.AD_Client_ID) ").append("WHERE C_BP_Group_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Set Group=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Group, ' ").append("WHERE C_BP_Group_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("Invalid Group=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET C_Country_ID=(SELECT C_Country_ID FROM C_Country c").append(" WHERE i.CountryCode=c.CountryCode AND c.AD_Client_ID IN (0, i.AD_Client_ID)) ").append("WHERE C_Country_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Set Country=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Country, ' ").append("WHERE C_Country_ID IS NULL AND (City IS NOT NULL OR Address1 IS NOT NULL)").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("Invalid Country=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("Set RegionName=(SELECT MAX(Name) FROM C_Region r").append(" WHERE r.IsDefault='Y' AND r.C_Country_ID=i.C_Country_ID").append(" AND r.AD_Client_ID IN (0, i.AD_Client_ID)) ");
        sql.append("WHERE RegionName IS NULL AND C_Region_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Set Region Default=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("Set C_Region_ID=(SELECT C_Region_ID FROM C_Region r").append(" WHERE r.Name=i.RegionName AND r.C_Country_ID=i.C_Country_ID").append(" AND r.AD_Client_ID IN (0, i.AD_Client_ID)) ").append("WHERE C_Region_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Set Region=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Region, ' ").append("WHERE C_Region_ID IS NULL ").append(" AND EXISTS (SELECT * FROM C_Country c").append(" WHERE c.C_Country_ID=i.C_Country_ID AND c.HasRegion='Y')").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("Invalid Region=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET C_Greeting_ID=(SELECT C_Greeting_ID FROM C_Greeting g").append(" WHERE i.BPContactGreeting=g.Name AND g.AD_Client_ID IN (0, i.AD_Client_ID)) ").append("WHERE C_Greeting_ID IS NULL AND BPContactGreeting IS NOT NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Set Greeting=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Greeting, ' ").append("WHERE C_Greeting_ID IS NULL AND BPContactGreeting IS NOT NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("Invalid Greeting=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET (C_BPartner_ID,AD_User_ID)=").append("(SELECT C_BPartner_ID,AD_User_ID FROM AD_User u ").append("WHERE i.EMail=u.EMail AND u.AD_Client_ID=i.AD_Client_ID) ").append("WHERE i.EMail IS NOT NULL AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Found EMail User=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner p").append(" WHERE i.Value=p.Value AND p.AD_Client_ID=i.AD_Client_ID) ").append("WHERE C_BPartner_ID IS NULL AND Value IS NOT NULL").append(" AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Found BPartner=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET AD_User_ID=(SELECT AD_User_ID FROM AD_User c").append(" WHERE i.ContactName=c.Name AND i.C_BPartner_ID=c.C_BPartner_ID AND c.AD_Client_ID=i.AD_Client_ID) ").append("WHERE C_BPartner_ID IS NOT NULL AND AD_User_ID IS NULL AND ContactName IS NOT NULL").append(" AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Found Contact=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET C_BPartner_Location_ID=(SELECT C_BPartner_Location_ID").append(" FROM C_BPartner_Location bpl INNER JOIN C_Location l ON (bpl.C_Location_ID=l.C_Location_ID)").append(" WHERE i.C_BPartner_ID=bpl.C_BPartner_ID AND bpl.AD_Client_ID=i.AD_Client_ID").append(" AND (i.Address1=l.Address1 OR (i.Address1 IS NULL AND l.Address1 IS NULL))").append(" AND (i.Address2=l.Address2 OR (i.Address2 IS NULL AND l.Address2 IS NULL))").append(" AND (i.City=l.City OR (i.City IS NULL AND l.City IS NULL))").append(" AND (i.Postal=l.Postal OR (i.Postal IS NULL AND l.Postal IS NULL))").append(" AND (i.Postal_Add=l.Postal_Add OR (l.Postal_Add IS NULL AND l.Postal_Add IS NULL))").append(" AND (i.C_Region_ID=l.C_Region_ID OR (l.C_Region_ID IS NULL AND i.C_Region_ID IS NULL))").append(" AND i.C_Country_ID=l.C_Country_ID) ").append("WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL").append(" AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Found Location=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET R_InterestArea_ID=(SELECT R_InterestArea_ID FROM R_InterestArea ia ").append("WHERE i.InterestAreaName=ia.Name AND ia.AD_Client_ID=i.AD_Client_ID) ").append("WHERE R_InterestArea_ID IS NULL AND InterestAreaName IS NOT NULL").append(" AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Set Interest Area=" + no);
        }
        sql = new StringBuilder("UPDATE I_BPartner ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Value is mandatory, ' ").append("WHERE Value IS NULL ").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("Value is mandatory=" + no);
        }
        ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)null, (PO)null, 20);
        this.commitEx();
        if (this.p_IsValidateOnly) {
            return "Validated";
        }
        int noInsert = 0;
        int noUpdate = 0;
        sql = new StringBuilder("SELECT * FROM I_BPartner ").append("WHERE I_IsImported='N'").append(clientCheck);
        sql.append(" ORDER BY Value, I_BPartner_ID");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), this.get_TrxName());
            rs = pstmt.executeQuery();
            String Old_BPValue = "";
            MBPartner bp = null;
            MBPartnerLocation bpl = null;
            while (rs.next()) {
                final String New_BPValue = rs.getString("Value");
                final X_I_BPartner impBP = new X_I_BPartner(this.getCtx(), rs, this.get_TrxName());
                StringBuilder msglog = new StringBuilder("I_BPartner_ID=").append(impBP.getI_BPartner_ID()).append(", C_BPartner_ID=").append(impBP.getC_BPartner_ID()).append(", C_BPartner_Location_ID=").append(impBP.getC_BPartner_Location_ID()).append(", AD_User_ID=").append(impBP.getAD_User_ID());
                if (this.log.isLoggable(Level.FINE)) {
                    this.log.fine(msglog.toString());
                }
                if (!New_BPValue.equals(Old_BPValue)) {
                    bp = null;
                    if (impBP.getC_BPartner_ID() == 0) {
                        bp = new MBPartner(impBP);
                        ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)impBP, (PO)bp, 40);
                        this.setTypeOfBPartner(impBP, bp);
                        bp.set_ValueNoCheck("KJS_BPCategory", impBP.get_Value("KJS_BPCategory"));
                        bp.set_ValueNoCheck("KJS_Region", impBP.get_Value("KJS_Region"));
                        if (!bp.save()) {
                            sql = new StringBuilder("UPDATE I_BPartner i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append("'Cannot Insert BPartner, ' ").append("WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
                            DB.executeUpdateEx(sql.toString(), this.get_TrxName());
                            continue;
                        }
                        impBP.setC_BPartner_ID(bp.getC_BPartner_ID());
                        msglog = new StringBuilder("Insert BPartner - ").append(bp.getC_BPartner_ID());
                        if (this.log.isLoggable(Level.FINEST)) {
                            this.log.finest(msglog.toString());
                        }
                        ++noInsert;
                    }
                    else {
                        bp = new MBPartner(this.getCtx(), impBP.getC_BPartner_ID(), this.get_TrxName());
                        bp.set_ValueNoCheck("KJS_BPCategory", impBP.get_Value("KJS_BPCategory"));
                        bp.set_ValueNoCheck("KJS_Region", impBP.get_Value("KJS_Region"));
                        if (impBP.getName() != null) {
                            bp.setName(impBP.getName());
                            bp.setName2(impBP.getName2());
                        }
                        if (impBP.getDUNS() != null) {
                            bp.setDUNS(impBP.getDUNS());
                        }
                        if (impBP.getTaxID() != null) {
                            bp.setTaxID(impBP.getTaxID());
                        }
                        if (impBP.getNAICS() != null) {
                            bp.setNAICS(impBP.getNAICS());
                        }
                        if (impBP.getDescription() != null) {
                            bp.setDescription(impBP.getDescription());
                        }
                        if (impBP.getC_BP_Group_ID() != 0) {
                            bp.setC_BP_Group_ID(impBP.getC_BP_Group_ID());
                        }
                        ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)impBP, (PO)bp, 40);
                        this.setTypeOfBPartner(impBP, bp);
                        if (!bp.save()) {
                            sql = new StringBuilder("UPDATE I_BPartner i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append("'Cannot Update BPartner, ' ").append("WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
                            DB.executeUpdateEx(sql.toString(), this.get_TrxName());
                            continue;
                        }
                        msglog = new StringBuilder("Update BPartner - ").append(bp.getC_BPartner_ID());
                        if (this.log.isLoggable(Level.FINEST)) {
                            this.log.finest(msglog.toString());
                        }
                        ++noUpdate;
                    }
                    bpl = null;
                    if (impBP.getC_BPartner_Location_ID() != 0) {
                        bpl = new MBPartnerLocation(this.getCtx(), impBP.getC_BPartner_Location_ID(), this.get_TrxName());
                        final MLocation location = new MLocation(this.getCtx(), bpl.getC_Location_ID(), this.get_TrxName());
                        location.setC_Country_ID(impBP.getC_Country_ID());
                        location.setC_Region_ID(impBP.getC_Region_ID());
                        location.setCity(impBP.getCity());
                        location.setAddress1(impBP.getAddress1());
                        location.setAddress2(impBP.getAddress2());
                        location.setPostal(impBP.getPostal());
                        location.setPostal_Add(impBP.getPostal_Add());
                        if (!location.save()) {
                            this.log.warning("Location not updated");
                        }
                        else {
                            bpl.setC_Location_ID(location.getC_Location_ID());
                        }
                        if (impBP.getPhone() != null) {
                            bpl.setPhone(impBP.getPhone());
                        }
                        if (impBP.getPhone2() != null) {
                            bpl.setPhone2(impBP.getPhone2());
                        }
                        if (impBP.getFax() != null) {
                            bpl.setFax(impBP.getFax());
                        }
                        ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)impBP, (PO)bpl, 40);
                        bpl.saveEx();
                    }
                    else if (impBP.getC_Country_ID() != 0 && impBP.getAddress1() != null && impBP.getCity() != null) {
                        final MLocation location = new MLocation(this.getCtx(), impBP.getC_Country_ID(), impBP.getC_Region_ID(), impBP.getCity(), this.get_TrxName());
                        location.setAddress1(impBP.getAddress1());
                        location.setAddress2(impBP.getAddress2());
                        location.setPostal(impBP.getPostal());
                        location.setPostal_Add(impBP.getPostal_Add());
                        if (!location.save()) {
                            this.rollback();
                            --noInsert;
                            sql = new StringBuilder("UPDATE I_BPartner i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append("'Cannot Insert Location, ' ").append("WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
                            DB.executeUpdateEx(sql.toString(), this.get_TrxName());
                            continue;
                        }
                        msglog = new StringBuilder("Insert Location - ").append(location.getC_Location_ID());
                        if (this.log.isLoggable(Level.FINEST)) {
                            this.log.finest(msglog.toString());
                        }
                        bpl = new MBPartnerLocation(bp);
                        bpl.setC_Location_ID(location.getC_Location_ID());
                        bpl.setPhone(impBP.getPhone());
                        bpl.setPhone2(impBP.getPhone2());
                        bpl.setFax(impBP.getFax());
                        ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)impBP, (PO)bpl, 40);
                        if (!bpl.save()) {
                            this.rollback();
                            --noInsert;
                            sql = new StringBuilder("UPDATE I_BPartner i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append("'Cannot Insert BPLocation, ' ").append("WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
                            DB.executeUpdateEx(sql.toString(), this.get_TrxName());
                            continue;
                        }
                        msglog = new StringBuilder("Insert BP Location - ").append(bpl.getC_BPartner_Location_ID());
                        if (this.log.isLoggable(Level.FINEST)) {
                            this.log.finest(msglog.toString());
                        }
                        impBP.setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
                    }
                }
                Old_BPValue = New_BPValue;
                MUser user = null;
                if (impBP.getAD_User_ID() != 0) {
                    user = new MUser(this.getCtx(), impBP.getAD_User_ID(), this.get_TrxName());
                    if (user.getC_BPartner_ID() == 0) {
                        user.setC_BPartner_ID(bp.getC_BPartner_ID());
                    }
                    else if (user.getC_BPartner_ID() != bp.getC_BPartner_ID()) {
                        this.rollback();
                        --noInsert;
                        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append("'BP of User <> BP, ' ").append("WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
                        DB.executeUpdateEx(sql.toString(), this.get_TrxName());
                        continue;
                    }
                    if (impBP.getC_Greeting_ID() != 0) {
                        user.setC_Greeting_ID(impBP.getC_Greeting_ID());
                    }
                    String name = impBP.getContactName();
                    if (name == null || name.length() == 0) {
                        name = impBP.getEMail();
                    }
                    user.setName(name);
                    if (impBP.getTitle() != null) {
                        user.setTitle(impBP.getTitle());
                    }
                    if (impBP.getContactDescription() != null) {
                        user.setDescription(impBP.getContactDescription());
                    }
                    if (impBP.getComments() != null) {
                        user.setComments(impBP.getComments());
                    }
                    if (impBP.getPhone() != null) {
                        user.setPhone(impBP.getPhone());
                    }
                    if (impBP.getPhone2() != null) {
                        user.setPhone2(impBP.getPhone2());
                    }
                    if (impBP.getFax() != null) {
                        user.setFax(impBP.getFax());
                    }
                    if (impBP.getEMail() != null) {
                        user.setEMail(impBP.getEMail());
                    }
                    if (impBP.getBirthday() != null) {
                        user.setBirthday(impBP.getBirthday());
                    }
                    if (bpl != null) {
                        user.setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
                    }
                    ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)impBP, (PO)user, 40);
                    if (!user.save()) {
                        this.rollback();
                        --noInsert;
                        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append("'Cannot Update BP Contact, ' ").append("WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
                        DB.executeUpdateEx(sql.toString(), this.get_TrxName());
                        continue;
                    }
                    msglog = new StringBuilder("Update BP Contact - ").append(user.getAD_User_ID());
                    if (this.log.isLoggable(Level.FINEST)) {
                        this.log.finest(msglog.toString());
                    }
                }
                else if (impBP.getContactName() != null || impBP.getEMail() != null) {
                    user = new MUser((X_C_BPartner)bp);
                    if (impBP.getC_Greeting_ID() != 0) {
                        user.setC_Greeting_ID(impBP.getC_Greeting_ID());
                    }
                    String name = impBP.getContactName();
                    if (name == null || name.length() == 0) {
                        name = impBP.getEMail();
                    }
                    user.setName(name);
                    user.setTitle(impBP.getTitle());
                    user.setDescription(impBP.getContactDescription());
                    user.setComments(impBP.getComments());
                    user.setPhone(impBP.getPhone());
                    user.setPhone2(impBP.getPhone2());
                    user.setFax(impBP.getFax());
                    user.setEMail(impBP.getEMail());
                    user.setBirthday(impBP.getBirthday());
                    if (bpl != null) {
                        user.setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
                    }
                    ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)impBP, (PO)user, 40);
                    if (!user.save()) {
                        this.rollback();
                        --noInsert;
                        sql = new StringBuilder("UPDATE I_BPartner i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append("'Cannot Insert BPContact, ' ").append("WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
                        DB.executeUpdateEx(sql.toString(), this.get_TrxName());
                        continue;
                    }
                    msglog = new StringBuilder("Insert BP Contact - ").append(user.getAD_User_ID());
                    if (this.log.isLoggable(Level.FINEST)) {
                        this.log.finest(msglog.toString());
                    }
                    impBP.setAD_User_ID(user.getAD_User_ID());
                }
                if (impBP.getR_InterestArea_ID() != 0 && user != null) {
                    final MContactInterest ci = MContactInterest.get(this.getCtx(), impBP.getR_InterestArea_ID(), user.getAD_User_ID(), true, this.get_TrxName());
                    ci.saveEx();
                }
                impBP.setI_IsImported(true);
                impBP.setProcessed(true);
                impBP.setProcessing(false);
                impBP.saveEx();
                this.commitEx();
            }
            DB.close(rs, (Statement)pstmt);
        }
        catch (SQLException e) {
            this.rollback();
            throw new DBException(e, sql.toString());
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
            sql = new StringBuilder("UPDATE I_BPartner ").append("SET I_IsImported='N', Updated=SysDate ").append("WHERE I_IsImported<>'Y'").append(clientCheck);
            no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
            this.addLog(0, (Timestamp)null, new BigDecimal(no), "@Errors@");
            this.addLog(0, (Timestamp)null, new BigDecimal(noInsert), "@C_BPartner_ID@: @Inserted@");
            this.addLog(0, (Timestamp)null, new BigDecimal(noUpdate), "@C_BPartner_ID@: @Updated@");
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        sql = new StringBuilder("UPDATE I_BPartner ").append("SET I_IsImported='N', Updated=SysDate ").append("WHERE I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), this.get_TrxName());
        this.addLog(0, (Timestamp)null, new BigDecimal(no), "@Errors@");
        this.addLog(0, (Timestamp)null, new BigDecimal(noInsert), "@C_BPartner_ID@: @Inserted@");
        this.addLog(0, (Timestamp)null, new BigDecimal(noUpdate), "@C_BPartner_ID@: @Updated@");
        return "";
    }
    
    public String getWhereClause() {
        final StringBuilder msgreturn = new StringBuilder(" AND AD_Client_ID=").append(this.m_AD_Client_ID);
        return msgreturn.toString();
    }
    
    public String getImportTableName() {
        return "I_BPartner";
    }
    
    private void setTypeOfBPartner(final X_I_BPartner impBP, final MBPartner bp) {
        if (impBP.isVendor()) {
            bp.setIsVendor(true);
            bp.setIsCustomer(false);
        }
        if (impBP.isEmployee()) {
            bp.setIsEmployee(true);
            bp.setIsCustomer(false);
        }
        if (impBP.isCustomer()) {
            bp.setIsCustomer(true);
        }
    }
}