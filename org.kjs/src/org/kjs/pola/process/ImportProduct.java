package org.kjs.pola.process;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.compiere.model.MProductPrice;
import org.compiere.model.MProduct;
import org.compiere.model.X_I_Product;
import java.sql.Statement;
import java.sql.SQLException;
import org.compiere.model.PO;
import org.compiere.model.ModelValidationEngine;
import org.compiere.util.DB;
import org.compiere.process.ProcessInfoParameter;
import java.util.logging.Level;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.adempiere.process.ImportProcess;
import org.compiere.process.SvrProcess;

public class ImportProduct extends SvrProcess implements ImportProcess
{
    private int m_AD_Client_ID;
    private boolean m_deleteOldImported;
    private Timestamp m_DateValue;
    private int p_M_PriceList_Version_ID;
    private String[] strFieldsToCopy;
    
    public ImportProduct() {
        this.m_AD_Client_ID = 0;
        this.m_deleteOldImported = false;
        this.m_DateValue = null;
        this.p_M_PriceList_Version_ID = 0;
        this.strFieldsToCopy = new String[] { "Value", "Name", "Description", "DocumentNote", "Help", "UPC", "SKU", "Classification", "ProductType", "Discontinued", "DiscontinuedBy", "DiscontinuedAt", "ImageURL", "DescriptionURL" };
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
            else if (name.equals("M_PriceList_Version_ID")) {
                this.p_M_PriceList_Version_ID = para[i].getParameterAsInt();
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
            sql = new StringBuilder("DELETE I_Product ").append("WHERE I_IsImported='Y'").append(clientCheck);
            no = DB.executeUpdate(sql.toString(), this.get_TrxName());
            if (this.log.isLoggable(Level.INFO)) {
                this.log.info("Delete Old Imported =" + no);
            }
        }
        sql = new StringBuilder("UPDATE I_Product ").append("SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(this.m_AD_Client_ID).append("),").append(" AD_Org_ID = COALESCE (AD_Org_ID, 0),").append(" IsActive = COALESCE (IsActive, 'Y'),").append(" Created = COALESCE (Created, SysDate),").append(" CreatedBy = COALESCE (CreatedBy, 0),").append(" Updated = COALESCE (Updated, SysDate),").append(" UpdatedBy = COALESCE (UpdatedBy, 0),").append(" ProductType = COALESCE (ProductType, 'I'),").append(" I_ErrorMsg = ' ',").append(" I_IsImported = 'N' ").append("WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("Reset=" + no);
        }
        ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)null, (PO)null, 10);
        sql = new StringBuilder("UPDATE I_Product i ").append("SET C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner p").append(" WHERE i.BPartner_Value=p.Value AND i.AD_Client_ID=p.AD_Client_ID) ").append("WHERE C_BPartner_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("BPartner=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid BPartner,' ").append("WHERE C_BPartner_ID IS NULL AND BPartner_Value IS NOT NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (no != 0) {
            this.log.warning("Invalid BPartner=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET M_Product_ID=(SELECT M_Product_ID FROM M_Product p").append(" WHERE i.UPC=p.UPC AND i.AD_Client_ID=p.AD_Client_ID) ").append("WHERE M_Product_ID IS NULL").append(" AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("Product Existing UPC=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET M_Product_ID=(SELECT M_Product_ID FROM M_Product p").append(" WHERE i.Value=p.Value AND i.AD_Client_ID=p.AD_Client_ID) ").append("WHERE M_Product_ID IS NULL").append(" AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("Product Existing Value=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET M_Product_ID=(SELECT M_Product_ID FROM M_Product_po p").append(" WHERE i.C_BPartner_ID=p.C_BPartner_ID").append(" AND i.VendorProductNo=p.VendorProductNo AND i.AD_Client_ID=p.AD_Client_ID) ").append("WHERE M_Product_ID IS NULL").append(" AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("Product Existing Vendor ProductNo=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product ").append("SET ProductCategory_Value=(SELECT MAX(Value) FROM M_Product_Category").append(" WHERE IsDefault='Y' AND AD_Client_ID=").append(this.m_AD_Client_ID).append(") ").append("WHERE ProductCategory_Value IS NULL AND M_Product_Category_ID IS NULL").append(" AND M_Product_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Set Category Default Value=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET M_Product_Category_ID=(SELECT M_Product_Category_ID FROM M_Product_Category c").append(" WHERE i.ProductCategory_Value=c.Value AND i.AD_Client_ID=c.AD_Client_ID) ").append("WHERE ProductCategory_Value IS NOT NULL AND M_Product_Category_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("Set Category=" + no);
        }
        for (int i = 0; i < this.strFieldsToCopy.length; ++i) {
            sql = new StringBuilder("UPDATE I_Product i ").append("SET ").append(this.strFieldsToCopy[i]).append(" = (SELECT ").append(this.strFieldsToCopy[i]).append(" FROM M_Product p").append(" WHERE i.M_Product_ID=p.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID) ").append("WHERE M_Product_ID IS NOT NULL").append(" AND ").append(this.strFieldsToCopy[i]).append(" IS NULL").append(" AND I_IsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString(), this.get_TrxName());
            if (no != 0 && this.log.isLoggable(Level.FINE)) {
                this.log.fine(String.valueOf(this.strFieldsToCopy[i]) + " - default from existing Product=" + no);
            }
        }
        final String[] numFields = { "C_UOM_ID", "M_Product_Category_ID", "Volume", "Weight", "ShelfWidth", "ShelfHeight", "ShelfDepth", "UnitsPerPallet" };
        for (int j = 0; j < numFields.length; ++j) {
            sql = new StringBuilder("UPDATE I_PRODUCT i ").append("SET ").append(numFields[j]).append(" = (SELECT ").append(numFields[j]).append(" FROM M_Product p").append(" WHERE i.M_Product_ID=p.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID) ").append("WHERE M_Product_ID IS NOT NULL").append(" AND (").append(numFields[j]).append(" IS NULL OR ").append(numFields[j]).append("=0)").append(" AND I_IsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString(), this.get_TrxName());
            if (no != 0 && this.log.isLoggable(Level.FINE)) {
                this.log.fine(String.valueOf(numFields[j]) + " default from existing Product=" + no);
            }
        }
        final String[] strFieldsPO = { "UPC", "PriceEffective", "VendorProductNo", "VendorCategory", "Manufacturer", "Discontinued", "DiscontinuedBy", "DiscontinuedAt" };
        for (int k = 0; k < strFieldsPO.length; ++k) {
            sql = new StringBuilder("UPDATE I_PRODUCT i ").append("SET ").append(strFieldsPO[k]).append(" = (SELECT ").append(strFieldsPO[k]).append(" FROM M_Product_PO p").append(" WHERE i.M_Product_ID=p.M_Product_ID AND i.C_BPartner_ID=p.C_BPartner_ID AND i.AD_Client_ID=p.AD_Client_ID) ").append("WHERE M_Product_ID IS NOT NULL AND C_BPartner_ID IS NOT NULL").append(" AND ").append(strFieldsPO[k]).append(" IS NULL").append(" AND I_IsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString(), this.get_TrxName());
            if (no != 0 && this.log.isLoggable(Level.FINE)) {
                this.log.fine(String.valueOf(strFieldsPO[k]) + " default from existing Product PO=" + no);
            }
        }
        final String[] numFieldsPO = { "C_UOM_ID", "C_Currency_ID", "PriceList", "PricePO", "RoyaltyAmt", "Order_Min", "Order_Pack", "CostPerOrder", "DeliveryTime_Promised" };
        for (int l = 0; l < numFieldsPO.length; ++l) {
            sql = new StringBuilder("UPDATE I_PRODUCT i ").append("SET ").append(numFieldsPO[l]).append(" = (SELECT ").append(numFieldsPO[l]).append(" FROM M_Product_PO p").append(" WHERE i.M_Product_ID=p.M_Product_ID AND i.C_BPartner_ID=p.C_BPartner_ID AND i.AD_Client_ID=p.AD_Client_ID) ").append("WHERE M_Product_ID IS NOT NULL AND C_BPartner_ID IS NOT NULL").append(" AND (").append(numFieldsPO[l]).append(" IS NULL OR ").append(numFieldsPO[l]).append("=0)").append(" AND I_IsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString(), this.get_TrxName());
            if (no != 0 && this.log.isLoggable(Level.FINE)) {
                this.log.fine(String.valueOf(numFieldsPO[l]) + " default from existing Product PO=" + no);
            }
        }
        sql = new StringBuilder("UPDATE I_Product ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid ProdCategory,' ").append("WHERE M_Product_Category_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (no != 0) {
            this.log.warning("Invalid Category=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET X12DE355 = ").append("(SELECT MAX(X12DE355) FROM C_UOM u WHERE u.IsDefault='Y' AND u.AD_Client_ID IN (0,i.AD_Client_ID)) ").append("WHERE X12DE355 IS NULL AND C_UOM_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Set UOM Default=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET C_UOM_ID = (SELECT C_UOM_ID FROM C_UOM u WHERE u.X12DE355=i.X12DE355 AND u.AD_Client_ID IN (0,i.AD_Client_ID)) ").append("WHERE C_UOM_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("Set UOM=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid UOM, ' ").append("WHERE C_UOM_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (no != 0) {
            this.log.warning("Invalid UOM=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET ISO_Code=(SELECT ISO_Code FROM C_Currency c").append(" INNER JOIN C_AcctSchema a ON (a.C_Currency_ID=c.C_Currency_ID)").append(" INNER JOIN AD_ClientInfo ci ON (a.C_AcctSchema_ID=ci.C_AcctSchema1_ID)").append(" WHERE ci.AD_Client_ID=i.AD_Client_ID) ").append("WHERE C_Currency_ID IS NULL AND ISO_Code IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("Set Currency Default=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET C_Currency_ID=(SELECT C_Currency_ID FROM C_Currency c").append(" WHERE i.ISO_Code=c.ISO_Code AND c.AD_Client_ID IN (0,i.AD_Client_ID)) ").append("WHERE C_Currency_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("doIt- Set Currency=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Currency,' ").append("WHERE C_Currency_ID IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (no != 0) {
            this.log.warning("Invalid Currency=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid ProductType,' ").append("WHERE ProductType NOT IN ('E','I','R','S','A')").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (no != 0) {
            this.log.warning("Invalid ProductType=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Value not unique,' ").append("WHERE I_IsImported<>'Y'").append(" AND Value IN (SELECT Value FROM I_Product ii WHERE i.AD_Client_ID=ii.AD_Client_ID GROUP BY Value HAVING COUNT(*) > 1)").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (no != 0) {
            this.log.warning("Not Unique Value=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=UPC not unique,' ").append("WHERE I_IsImported<>'Y'").append(" AND UPC IN (SELECT UPC FROM I_Product ii WHERE i.AD_Client_ID=ii.AD_Client_ID GROUP BY UPC HAVING COUNT(*) > 1)").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (no != 0) {
            this.log.warning("Not Unique UPC=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No Mandatory Value,' ").append("WHERE Value IS NULL").append(" AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (no != 0) {
            this.log.warning("No Mandatory Value=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product ").append("SET VendorProductNo=Value ").append("WHERE C_BPartner_ID IS NOT NULL AND VendorProductNo IS NULL").append(" AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("VendorProductNo Set to Value=" + no);
        }
        sql = new StringBuilder("UPDATE I_Product i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=VendorProductNo not unique,' ").append("WHERE I_IsImported<>'Y'").append(" AND C_BPartner_ID IS NOT NULL").append(" AND (C_BPartner_ID, VendorProductNo) IN ").append(" (SELECT C_BPartner_ID, VendorProductNo FROM I_Product ii WHERE i.AD_Client_ID=ii.AD_Client_ID GROUP BY C_BPartner_ID, VendorProductNo HAVING COUNT(*) > 1)").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        if (no != 0) {
            this.log.warning("Not Unique VendorProductNo=" + no);
        }
        int C_TaxCategory_ID = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            final StringBuilder dbpst = new StringBuilder("SELECT C_TaxCategory_ID FROM C_TaxCategory WHERE IsDefault='Y'").append(clientCheck);
            pstmt = (PreparedStatement)DB.prepareStatement(dbpst.toString(), this.get_TrxName());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                C_TaxCategory_ID = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            throw new Exception("TaxCategory", e);
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        if (this.log.isLoggable(Level.FINE)) {
            this.log.fine("C_TaxCategory_ID=" + C_TaxCategory_ID);
        }
        ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)null, (PO)null, 20);
        this.commitEx();
        int noInsert = 0;
        int noUpdate = 0;
        int noInsertPO = 0;
        int noUpdatePO = 0;
        this.log.fine("start inserting/updating ...");
        sql = new StringBuilder("SELECT * FROM I_Product WHERE I_IsImported='N'").append(clientCheck);
        PreparedStatement pstmt_setImported = null;
        PreparedStatement pstmt_insertProductPO = null;
        Label_4728: {
            try {
                pstmt_insertProductPO = (PreparedStatement)DB.prepareStatement("INSERT INTO M_Product_PO (M_Product_ID,C_BPartner_ID, AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,IsCurrentVendor,C_UOM_ID,C_Currency_ID,UPC,PriceList,PricePO,RoyaltyAmt,PriceEffective,VendorProductNo,VendorCategory,Manufacturer,Discontinued,DiscontinuedBy, DiscontinuedAt, Order_Min,Order_Pack,CostPerOrder,DeliveryTime_Promised) SELECT ?,?, AD_Client_ID,AD_Org_ID,'Y',SysDate,CreatedBy,SysDate,UpdatedBy,'Y',C_UOM_ID,C_Currency_ID,UPC,PriceList,PricePO,RoyaltyAmt,PriceEffective,VendorProductNo,VendorCategory,Manufacturer,Discontinued,DiscontinuedBy, DiscontinuedAt, Order_Min,Order_Pack,CostPerOrder,DeliveryTime_Promised FROM I_Product WHERE I_Product_ID=?", this.get_TrxName());
                pstmt_setImported = (PreparedStatement)DB.prepareStatement("UPDATE I_Product SET I_IsImported='Y', M_Product_ID=?, Updated=SysDate, Processed='Y' WHERE I_Product_ID=?", this.get_TrxName());
                pstmt = (PreparedStatement)DB.prepareStatement(sql.toString(), this.get_TrxName());
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    final X_I_Product imp = new X_I_Product(this.getCtx(), rs, this.get_TrxName());
                    final int I_Product_ID = imp.getI_Product_ID();
                    int M_Product_ID = imp.getM_Product_ID();
                    final int C_BPartner_ID = imp.getC_BPartner_ID();
                    final boolean newProduct = M_Product_ID == 0;
                    if (this.log.isLoggable(Level.FINE)) {
                        this.log.fine("I_Product_ID=" + I_Product_ID + ", M_Product_ID=" + M_Product_ID + ", C_BPartner_ID=" + C_BPartner_ID);
                    }
                    if (newProduct) {
                        final MProduct product = new MProduct(imp);
                        product.setC_TaxCategory_ID(C_TaxCategory_ID);
                        product.set_ValueOfColumn("KJS_BodyType", imp.get_Value("KJS_BodyType"));
                        product.set_ValueOfColumn("KJS_BodySubs", imp.get_Value("KJS_BodySubs"));
                        product.set_ValueOfColumn("KJS_BodyPE", imp.get_Value("KJS_BodyPE"));
                        product.set_ValueOfColumn("KJS_BodyVendor", imp.get_Value("KJS_BodyVendor"));
                        product.set_ValueOfColumn("KJS_BottomRW", imp.get_Value("KJS_BottomRW"));
                        product.set_ValueOfColumn("KJS_BottomSubs", imp.get_Value("KJS_BottomSubs"));
                        product.set_ValueOfColumn("KJS_BottomPE", imp.get_Value("KJS_BottomPE"));
                        product.set_ValueOfColumn("KJS_OffsetMaterialWidth", imp.get_Value("KJS_OffsetMaterialWidth"));
                        product.set_ValueOfColumn("KJS_OffsetMaterialHeight", imp.get_Value("KJS_OffsetMaterialHeight"));
                        product.set_ValueOfColumn("KJS_OffsetCavityWidth", imp.get_Value("KJS_OffsetCavityWidth"));
                        product.set_ValueOfColumn("KJS_OffsetCavityHeight", imp.get_Value("KJS_OffsetCavityHeight"));
                        product.set_ValueOfColumn("KJS_OffsetCavityUp", imp.get_Value("KJS_OffsetCavityUp"));
                        product.set_ValueOfColumn("KJS_FlexoMaterialWidth", imp.get_Value("KJS_FlexoMaterialWidth"));
                        product.set_ValueOfColumn("KJS_FlexoMaterialHeight", imp.get_Value("KJS_FlexoMaterialHeight"));
                        product.set_ValueOfColumn("KJS_FlexoGear", imp.get_Value("KJS_FlexoGear"));
                        product.set_ValueOfColumn("KJS_FlexoCavityWidth", imp.get_Value("KJS_FlexoCavityWidth"));
                        product.set_ValueOfColumn("KJS_FlexoCavityHeight", imp.get_Value("KJS_FlexoCavityHeight"));
                        product.set_ValueOfColumn("KJS_FlexoCavityUp", imp.get_Value("KJS_FlexoCavityUp"));
                        product.set_ValueOfColumn("KJS_KonsumsiOffset", imp.get_Value("KJS_KonsumsiOffset"));
                        product.set_ValueOfColumn("KJS_KonsumsiFlexo", imp.get_Value("KJS_KonsumsiFlexo"));
                        product.set_ValueOfColumn("KJS_KonsumsiBottom", imp.get_Value("KJS_KonsumsiBottom"));
                        product.set_ValueOfColumn("KJS_Brand", imp.get_Value("KJS_Brand"));
                        product.set_ValueOfColumn("KJS_SubCategory", imp.get_Value("KJS_SubCategory"));
                        ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)imp, (PO)product, 40);
                        if (!product.save()) {
                            final StringBuilder sql2 = new StringBuilder("UPDATE I_Product i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert Product failed")).append("WHERE I_Product_ID=").append(I_Product_ID);
                            DB.executeUpdate(sql2.toString(), this.get_TrxName());
                            continue;
                        }
                        M_Product_ID = product.getM_Product_ID();
                        this.log.finer("Insert Product");
                        ++noInsert;
                    }
                    else {
                        final StringBuilder sqlt = new StringBuilder("UPDATE M_PRODUCT ").append("SET (Value,Name,Description,DocumentNote,Help,").append("UPC,SKU,C_UOM_ID,M_Product_Category_ID,Classification,ProductType,").append("Volume,Weight,ShelfWidth,ShelfHeight,ShelfDepth,UnitsPerPallet,").append("Discontinued,DiscontinuedBy, DiscontinuedAt, Updated,UpdatedBy,").append("KJS_BodyType,KJS_BodySubs,KJS_BodyPE,KJS_BodyVendor,KJS_BottomRW,").append("KJS_BottomSubs,KJS_BottomPE,KJS_OffsetMaterialWidth,KJS_OffsetMaterialHeight,").append("KJS_OffsetCavityWidth,KJS_OffsetCavityHeight,KJS_OffsetCavityUp,").append("KJS_FlexoMaterialWidth,KJS_FlexoMaterialHeight,KJS_FlexoGear,").append("KJS_FlexoCavityWidth,KJS_FlexoCavityHeight,KJS_FlexoCavityUp,").append("KJS_KonsumsiOffset,KJS_KonsumsiFlexo,KJS_KonsumsiBottom,").append("KJS_Brand,KJS_SubCategory)= ").append("(SELECT Value,Name,Description,DocumentNote,Help,").append("UPC,SKU,C_UOM_ID,M_Product_Category_ID,Classification,ProductType,").append("Volume,Weight,ShelfWidth,ShelfHeight,ShelfDepth,UnitsPerPallet,").append("Discontinued,DiscontinuedBy, DiscontinuedAt, SysDate,UpdatedBy,").append("KJS_BodyType,KJS_BodySubs,KJS_BodyPE,KJS_BodyVendor,KJS_BottomRW,").append("KJS_BottomSubs,KJS_BottomPE,KJS_OffsetMaterialWidth,KJS_OffsetMaterialHeight,").append("KJS_OffsetCavityWidth,KJS_OffsetCavityHeight,KJS_OffsetCavityUp,").append("KJS_FlexoMaterialWidth,KJS_FlexoMaterialHeight,KJS_FlexoGear,").append("KJS_FlexoCavityWidth,KJS_FlexoCavityHeight,KJS_FlexoCavityUp,").append("KJS_KonsumsiOffset,KJS_KonsumsiFlexo,KJS_KonsumsiBottom,").append("KJS_Brand,KJS_SubCategory").append(" FROM I_Product WHERE I_Product_ID=").append(I_Product_ID).append(") ").append("WHERE M_Product_ID=").append(M_Product_ID);
                        PreparedStatement pstmt_updateProduct = (PreparedStatement)DB.prepareStatement(sqlt.toString(), this.get_TrxName());
                        try {
                            no = pstmt_updateProduct.executeUpdate();
                            if (this.log.isLoggable(Level.FINER)) {
                                this.log.finer("Update Product = " + no);
                            }
                            ++noUpdate;
                        }
                        catch (SQLException ex) {
                            this.log.warning("Update Product - " + ex.toString());
                            final StringBuilder sql3 = new StringBuilder("UPDATE I_Product i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Update Product: " + ex.toString())).append("WHERE I_Product_ID=").append(I_Product_ID);
                            DB.executeUpdate(sql3.toString(), this.get_TrxName());
                            continue;
                        }
                        finally {
                            DB.close((Statement)pstmt_updateProduct);
                            pstmt_updateProduct = null;
                        }
                        DB.close((Statement)pstmt_updateProduct);
                        pstmt_updateProduct = null;
                    }
                    if (C_BPartner_ID != 0) {
                        no = 0;
                        if (!newProduct) {
                            final StringBuilder sqlt = new StringBuilder("UPDATE M_Product_PO ").append("SET (IsCurrentVendor,C_UOM_ID,C_Currency_ID,UPC,").append("PriceList,PricePO,RoyaltyAmt,PriceEffective,").append("VendorProductNo,VendorCategory,Manufacturer,").append("Discontinued,DiscontinuedBy, DiscontinuedAt, Order_Min,Order_Pack,").append("CostPerOrder,DeliveryTime_Promised,Updated,UpdatedBy)= ").append("(SELECT CAST('Y' AS CHAR),C_UOM_ID,C_Currency_ID,UPC,").append("PriceList,PricePO,RoyaltyAmt,PriceEffective,").append("VendorProductNo,VendorCategory,Manufacturer,").append("Discontinued,DiscontinuedBy, DiscontinuedAt, Order_Min,Order_Pack,").append("CostPerOrder,DeliveryTime_Promised,SysDate,UpdatedBy").append(" FROM I_Product").append(" WHERE I_Product_ID=").append(I_Product_ID).append(") ").append("WHERE M_Product_ID=").append(M_Product_ID).append(" AND C_BPartner_ID=").append(C_BPartner_ID);
                            PreparedStatement pstmt_updateProductPO = (PreparedStatement)DB.prepareStatement(sqlt.toString(), this.get_TrxName());
                            try {
                                no = pstmt_updateProductPO.executeUpdate();
                                if (this.log.isLoggable(Level.FINER)) {
                                    this.log.finer("Update Product_PO = " + no);
                                }
                                ++noUpdatePO;
                            }
                            catch (SQLException ex) {
                                this.log.warning("Update Product_PO - " + ex.toString());
                                --noUpdate;
                                this.rollback();
                                final StringBuilder sql3 = new StringBuilder("UPDATE I_Product i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Update Product_PO: " + ex.toString())).append("WHERE I_Product_ID=").append(I_Product_ID);
                                DB.executeUpdate(sql3.toString(), this.get_TrxName());
                                continue;
                            }
                            finally {
                                DB.close((Statement)pstmt_updateProductPO);
                                pstmt_updateProductPO = null;
                            }
                            DB.close((Statement)pstmt_updateProductPO);
                            pstmt_updateProductPO = null;
                        }
                        if (no == 0) {
                            pstmt_insertProductPO.setInt(1, M_Product_ID);
                            pstmt_insertProductPO.setInt(2, C_BPartner_ID);
                            pstmt_insertProductPO.setInt(3, I_Product_ID);
                            try {
                                no = pstmt_insertProductPO.executeUpdate();
                                if (this.log.isLoggable(Level.FINER)) {
                                    this.log.finer("Insert Product_PO = " + no);
                                }
                                ++noInsertPO;
                            }
                            catch (SQLException ex2) {
                                this.log.warning("Insert Product_PO - " + ex2.toString());
                                --noInsert;
                                this.rollback();
                                final StringBuilder sql2 = new StringBuilder("UPDATE I_Product i ").append("SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert Product_PO: " + ex2.toString())).append("WHERE I_Product_ID=").append(I_Product_ID);
                                DB.executeUpdate(sql2.toString(), this.get_TrxName());
                                continue;
                            }
                        }
                    }
                    if (this.p_M_PriceList_Version_ID != 0) {
                        final BigDecimal PriceList = imp.getPriceList();
                        final BigDecimal PriceStd = imp.getPriceStd();
                        final BigDecimal PriceLimit = imp.getPriceLimit();
                        if (PriceStd.signum() != 0 || PriceLimit.signum() != 0 || PriceList.signum() != 0) {
                            MProductPrice pp = MProductPrice.get(this.getCtx(), this.p_M_PriceList_Version_ID, M_Product_ID, this.get_TrxName());
                            if (pp == null) {
                                pp = new MProductPrice(this.getCtx(), this.p_M_PriceList_Version_ID, M_Product_ID, this.get_TrxName());
                            }
                            pp.setPrices(PriceList, PriceStd, PriceLimit);
                            ModelValidationEngine.get().fireImportValidate((ImportProcess)this, (PO)imp, (PO)pp, 40);
                            pp.saveEx();
                        }
                    }
                    pstmt_setImported.setInt(1, M_Product_ID);
                    pstmt_setImported.setInt(2, I_Product_ID);
                    no = pstmt_setImported.executeUpdate();
                    this.commitEx();
                }
            }
            catch (SQLException ex3) {
                break Label_4728;
            }
            finally {
                DB.close(rs, (Statement)pstmt);
                rs = null;
                pstmt = null;
                DB.close((Statement)pstmt_insertProductPO);
                pstmt_insertProductPO = null;
                DB.close((Statement)pstmt_setImported);
                pstmt_setImported = null;
            }
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
            DB.close((Statement)pstmt_insertProductPO);
            pstmt_insertProductPO = null;
            DB.close((Statement)pstmt_setImported);
            pstmt_setImported = null;
        }
        sql = new StringBuilder("UPDATE I_Product ").append("SET I_IsImported='N', Updated=SysDate ").append("WHERE I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), this.get_TrxName());
        this.addLog(0, (Timestamp)null, new BigDecimal(no), "@Errors@");
        this.addLog(0, (Timestamp)null, new BigDecimal(noInsert), "@M_Product_ID@: @Inserted@");
        this.addLog(0, (Timestamp)null, new BigDecimal(noUpdate), "@M_Product_ID@: @Updated@");
        this.addLog(0, (Timestamp)null, new BigDecimal(noInsertPO), "@M_Product_ID@ @Purchase@: @Inserted@");
        this.addLog(0, (Timestamp)null, new BigDecimal(noUpdatePO), "@M_Product_ID@ @Purchase@: @Updated@");
        return "";
    }
    
    public String getImportTableName() {
        return "I_Product";
    }
    
    public String getWhereClause() {
        final StringBuilder msgreturn = new StringBuilder(" AND AD_Client_ID=").append(this.m_AD_Client_ID);
        return msgreturn.toString();
    }
}