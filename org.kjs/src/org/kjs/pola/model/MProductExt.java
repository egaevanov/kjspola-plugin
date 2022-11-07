package org.kjs.pola.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAssetGroup;
import org.compiere.model.MAttribute;
import org.compiere.model.MAttributeInstance;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MClient;
import org.compiere.model.MCost;
import org.compiere.model.MCostElement;
import org.compiere.model.MExpenseType;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;
import org.compiere.model.MProductCategoryAcct;
import org.compiere.model.MProductDownload;
import org.compiere.model.MResource;
import org.compiere.model.MResourceType;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.MStorageReservation;
import org.compiere.model.MTable;
import org.compiere.model.MUOM;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_I_Product;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;

public class MProductExt extends MProduct
{
    private static final long serialVersionUID = -2387223874488597523L;
    private static CCache<Integer, MProduct> s_cache;
    private MProductDownload[] m_downloads;
    private Integer m_precision;
    
    static {
        MProductExt.s_cache = (CCache<Integer, MProduct>)new CCache("M_Product", 40, 5);
    }
    
    public static MProduct get(final Properties ctx, final int M_Product_ID) {
        if (M_Product_ID <= 0) {
            return null;
        }
        final Integer key = new Integer(M_Product_ID);
        MProduct retValue = (MProduct)MProductExt.s_cache.get((Object)key);
        if (retValue != null) {
            return retValue;
        }
        retValue = new MProduct(ctx, M_Product_ID, (String)null);
        if (retValue.get_ID() != 0) {
            MProductExt.s_cache.put(key, retValue);
        }
        return retValue;
    }
    
    public static MProduct[] get(final Properties ctx, final String whereClause, final String trxName) {
        final List<MProduct> list = new Query(ctx, "M_Product", whereClause, trxName).setClient_ID().list();
        return list.toArray(new MProduct[list.size()]);
    }
    
    public static List<MProduct> getByUPC(final Properties ctx, final String upc, final String trxName) {
        final Query q = new Query(ctx, "M_Product", "UPC=?", trxName);
        q.setParameters(new Object[] { upc }).setClient_ID();
        return (q.list());
    }
    
    @Deprecated
    public static MProduct forS_Resource_ID(final Properties ctx, final int S_Resource_ID) {
        return forS_Resource_ID(ctx, S_Resource_ID, null);
    }
    
    public static MProduct forS_Resource_ID(final Properties ctx, final int S_Resource_ID, final String trxName) {
        if (S_Resource_ID <= 0) {
            return null;
        }
        if (trxName == null) {
            for (final MProduct p : MProductExt.s_cache.values()) {
                if (p.getS_Resource_ID() == S_Resource_ID) {
                    return p;
                }
            }
        }
        MProduct p = (MProduct)new Query(ctx, "M_Product", "S_Resource_ID=?", trxName).setParameters(new Object[] { S_Resource_ID }).firstOnly();
        if (p != null && trxName == null) {
            MProductExt.s_cache.put(p.getM_Product_ID(), p);
        }
        return p;
    }
    
    public static boolean isProductStocked(final Properties ctx, final int M_Product_ID) {
        final MProduct product = get(ctx, M_Product_ID);
        return product.isStocked();
    }
    
    public MProductExt(final Properties ctx, final int M_Product_ID, final String trxName) {
        super(ctx, M_Product_ID, trxName);
        this.m_downloads = null;
        this.m_precision = null;
        if (M_Product_ID == 0) {
            this.setProductType("I");
            this.setIsBOM(false);
            this.setIsInvoicePrintDetails(false);
            this.setIsPickListPrintDetails(false);
            this.setIsPurchased(true);
            this.setIsSold(true);
            this.setIsStocked(true);
            this.setIsSummary(false);
            this.setIsVerified(false);
            this.setIsWebStoreFeatured(false);
            this.setIsSelfService(true);
            this.setIsExcludeAutoDelivery(false);
            this.setProcessing(false);
            this.setLowLevel(0);
        }
    }
    
    public MProductExt(final Properties ctx, final ResultSet rs, final String trxName) {
        super(ctx, rs, trxName);
        this.m_downloads = null;
        this.m_precision = null;
    }
    
    public MProductExt(final MExpenseType et) {
        this(et.getCtx(), 0, et.get_TrxName());
        this.setProductType("E");
        this.setExpenseType(et);
    }
    
    public MProductExt(final MResource resource, final MResourceType resourceType) {
        this(resource.getCtx(), 0, resource.get_TrxName());
        this.setAD_Org_ID(resource.getAD_Org_ID());
        this.setProductType("R");
        this.setResource(resource);
        this.setResource(resourceType);
    }
    
    public MProductExt(final X_I_Product impP) {
        this(impP.getCtx(), 0, impP.get_TrxName());
        this.setClientOrg((PO)impP);
        this.setUpdatedBy(impP.getUpdatedBy());
        this.setValue(impP.getValue());
        this.setName(impP.getName());
        this.setDescription(impP.getDescription());
        this.setDocumentNote(impP.getDocumentNote());
        this.setHelp(impP.getHelp());
        this.setUPC(impP.getUPC());
        this.setSKU(impP.getSKU());
        this.setC_UOM_ID(impP.getC_UOM_ID());
        this.setM_Product_Category_ID(impP.getM_Product_Category_ID());
        this.setProductType(impP.getProductType());
        this.setImageURL(impP.getImageURL());
        this.setDescriptionURL(impP.getDescriptionURL());
        this.setVolume(impP.getVolume());
        this.setWeight(impP.getWeight());
    }
    
    public boolean setExpenseType(final MExpenseType parent) {
        boolean changed = false;
        if (!"E".equals(this.getProductType())) {
            this.setProductType("E");
            changed = true;
        }
        if (parent.getS_ExpenseType_ID() != this.getS_ExpenseType_ID()) {
            this.setS_ExpenseType_ID(parent.getS_ExpenseType_ID());
            changed = true;
        }
        if (parent.isActive() != this.isActive()) {
            this.setIsActive(parent.isActive());
            changed = true;
        }
        if (!parent.getValue().equals(this.getValue())) {
            this.setValue(parent.getValue());
            changed = true;
        }
        if (!parent.getName().equals(this.getName())) {
            this.setName(parent.getName());
            changed = true;
        }
        if ((parent.getDescription() == null && this.getDescription() != null) || (parent.getDescription() != null && !parent.getDescription().equals(this.getDescription()))) {
            this.setDescription(parent.getDescription());
            changed = true;
        }
        if (parent.getC_UOM_ID() != this.getC_UOM_ID()) {
            this.setC_UOM_ID(parent.getC_UOM_ID());
            changed = true;
        }
        if (parent.getM_Product_Category_ID() != this.getM_Product_Category_ID()) {
            this.setM_Product_Category_ID(parent.getM_Product_Category_ID());
            changed = true;
        }
        if (parent.getC_TaxCategory_ID() != this.getC_TaxCategory_ID()) {
            this.setC_TaxCategory_ID(parent.getC_TaxCategory_ID());
            changed = true;
        }
        return changed;
    }
    
    public boolean setResource(final MResource parent) {
        boolean changed = false;
        if (!"R".equals(this.getProductType())) {
            this.setProductType("R");
            changed = true;
        }
        if (parent.getS_Resource_ID() != this.getS_Resource_ID()) {
            this.setS_Resource_ID(parent.getS_Resource_ID());
            changed = true;
        }
        if (parent.isActive() != this.isActive()) {
            this.setIsActive(parent.isActive());
            changed = true;
        }
        if (!parent.getValue().equals(this.getValue())) {
            this.setValue(parent.getValue());
            changed = true;
        }
        if (!parent.getName().equals(this.getName())) {
            this.setName(parent.getName());
            changed = true;
        }
        if ((parent.getDescription() == null && this.getDescription() != null) || (parent.getDescription() != null && !parent.getDescription().equals(this.getDescription()))) {
            this.setDescription(parent.getDescription());
            changed = true;
        }
        return changed;
    }
    
    public boolean setResource(final MResourceType parent) {
        boolean changed = false;
        if ("R".equals(this.getProductType())) {
            this.setProductType("R");
            changed = true;
        }
        if (parent.getC_UOM_ID() != this.getC_UOM_ID()) {
            this.setC_UOM_ID(parent.getC_UOM_ID());
            changed = true;
        }
        if (parent.getM_Product_Category_ID() != this.getM_Product_Category_ID()) {
            this.setM_Product_Category_ID(parent.getM_Product_Category_ID());
            changed = true;
        }
        if (parent.getC_TaxCategory_ID() != this.getC_TaxCategory_ID()) {
            this.setC_TaxCategory_ID(parent.getC_TaxCategory_ID());
            changed = true;
        }
        return changed;
    }
    
    public int getUOMPrecision() {
        if (this.m_precision == null) {
            final int C_UOM_ID = this.getC_UOM_ID();
            if (C_UOM_ID == 0) {
                return 0;
            }
            this.m_precision = new Integer(MUOM.getPrecision(this.getCtx(), C_UOM_ID));
        }
        return this.m_precision;
    }
    
    public int getA_Asset_Group_ID() {
        final MProductCategory pc = MProductCategory.get(this.getCtx(), this.getM_Product_Category_ID());
        return pc.getA_Asset_Group_ID();
    }
    
    public boolean isCreateAsset() {
        final MProductCategory pc = MProductCategory.get(this.getCtx(), this.getM_Product_Category_ID());
        return pc.getA_Asset_Group_ID() != 0;
    }
    
    public MAttributeSet getAttributeSet() {
        if (this.getM_AttributeSet_ID() != 0) {
            return MAttributeSet.get(this.getCtx(), this.getM_AttributeSet_ID());
        }
        return null;
    }
    
    public boolean isInstanceAttribute() {
        if (this.getM_AttributeSet_ID() == 0) {
            return false;
        }
        final MAttributeSet mas = MAttributeSet.get(this.getCtx(), this.getM_AttributeSet_ID());
        return mas.isInstanceAttribute();
    }
    
    public boolean isOneAssetPerUOM() {
        final MProductCategory pc = MProductCategory.get(this.getCtx(), this.getM_Product_Category_ID());
        if (pc.getA_Asset_Group_ID() == 0) {
            return false;
        }
        final MAssetGroup ag = MAssetGroup.get(this.getCtx(), pc.getA_Asset_Group_ID());
        return ag.isOneAssetPerUOM();
    }
    
    public boolean isItem() {
        return "I".equals(this.getProductType());
    }
    
    public boolean isStocked() {
        return super.isStocked() && this.isItem();
    }
    
    public boolean isService() {
        return !this.isItem();
    }
    
    public String getUOMSymbol() {
        final int C_UOM_ID = this.getC_UOM_ID();
        if (C_UOM_ID == 0) {
            return "";
        }
        return MUOM.get(this.getCtx(), C_UOM_ID).getUOMSymbol();
    }
    
    public MProductDownload[] getProductDownloads(final boolean requery) {
        if (this.m_downloads != null && !requery) {
            return this.m_downloads;
        }
        final List<MProductDownload> list = new Query(this.getCtx(), "M_ProductDownload", "M_Product_ID=?", this.get_TrxName()).setOnlyActiveRecords(true).setOrderBy("Name").setParameters(new Object[] { this.get_ID() }).list();
        return this.m_downloads = list.toArray(new MProductDownload[list.size()]);
    }
    
    public boolean hasDownloads() {
        return this.getProductDownloads(false).length > 0;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder("MProduct[");
        sb.append(this.get_ID()).append("-").append(this.getValue()).append("]");
        return sb.toString();
    }
    
    protected boolean beforeSave(final boolean newRecord) {
        if (!newRecord && ((this.is_ValueChanged("IsActive") && !this.isActive()) || (this.is_ValueChanged("IsStocked") && !this.isStocked()) || (this.is_ValueChanged("ProductType") && "I".equals(this.get_ValueOld("ProductType"))))) {
            final String errMsg = this.verifyStorage();
            if (!Util.isEmpty(errMsg)) {
                this.log.saveError("Error", Msg.parseTranslation(this.getCtx(), errMsg));
                return false;
            }
        }
        if (!newRecord && this.is_ValueChanged("C_UOM_ID") && this.hasInventoryOrCost()) {
            this.log.saveError("Error", Msg.getMsg(this.getCtx(), "SaveUomError"));
            return false;
        }
        if (!"I".equals(this.getProductType())) {
            this.setIsStocked(false);
        }
        if (this.m_precision != null && this.is_ValueChanged("C_UOM_ID")) {
            this.m_precision = null;
        }
        if (this.getM_AttributeSetInstance_ID() > 0 && this.is_ValueChanged("M_AttributeSet_ID")) {
            final MAttributeSetInstance asi = new MAttributeSetInstance(this.getCtx(), this.getM_AttributeSetInstance_ID(), this.get_TrxName());
            if (asi.getM_AttributeSet_ID() != this.getM_AttributeSet_ID()) {
                this.setM_AttributeSetInstance_ID(0);
            }
        }
        if (!newRecord && this.is_ValueChanged("M_AttributeSetInstance_ID")) {
            final int oldasiid = this.get_ValueOldAsInt("M_AttributeSetInstance_ID");
            if (oldasiid > 0) {
                final MAttributeSetInstance oldasi = new MAttributeSetInstance(this.getCtx(), this.get_ValueOldAsInt("M_AttributeSetInstance_ID"), this.get_TrxName());
                final int cnt = DB.getSQLValueEx(this.get_TrxName(), "SELECT COUNT(*) FROM M_Product WHERE M_AttributeSetInstance_ID=?", new Object[] { oldasi.getM_AttributeSetInstance_ID() });
                if (cnt == 1) {
                    try {
                        oldasi.deleteEx(true, this.get_TrxName());
                    }
                    catch (AdempiereException ex) {
                        this.log.saveError("Error", "Error deleting the AttributeSetInstance");
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private String verifyStorage() {
        BigDecimal qtyOnHand = Env.ZERO;
        BigDecimal qtyOrdered = Env.ZERO;
        BigDecimal qtyReserved = Env.ZERO;
        MStorageOnHand[] ofProduct;
        for (int length = (ofProduct = MStorageOnHand.getOfProduct(this.getCtx(), this.get_ID(), this.get_TrxName())).length, i = 0; i < length; ++i) {
            final MStorageOnHand ohs = ofProduct[i];
            qtyOnHand = qtyOnHand.add(ohs.getQtyOnHand());
        }
        MStorageReservation[] ofProduct2;
        for (int length2 = (ofProduct2 = MStorageReservation.getOfProduct(this.getCtx(), this.get_ID(), this.get_TrxName())).length, j = 0; j < length2; ++j) {
            final MStorageReservation rs = ofProduct2[j];
            if (rs.isSOTrx()) {
                qtyReserved = qtyReserved.add(rs.getQty());
            }
            else {
                qtyOrdered = qtyOrdered.add(rs.getQty());
            }
        }
        final StringBuilder errMsg = new StringBuilder();
        if (qtyOnHand.signum() != 0) {
            errMsg.append("@QtyOnHand@ = ").append(qtyOnHand);
        }
        if (qtyOrdered.signum() != 0) {
            errMsg.append(" - @QtyOrdered@ = ").append(qtyOrdered);
        }
        if (qtyReserved.signum() != 0) {
            errMsg.append(" - @QtyReserved@").append(qtyReserved);
        }
        return errMsg.toString();
    }
    
    protected boolean hasInventoryOrCost() {
        final boolean hasTrx = new Query(this.getCtx(), "M_Transaction", "M_Product_ID=?", this.get_TrxName()).setOnlyActiveRecords(true).setParameters(new Object[] { this.get_ID() }).match();
        if (hasTrx) {
            return true;
        }
        final boolean hasCosts = new Query(this.getCtx(), "M_CostDetail", "M_Product_ID=?", this.get_TrxName()).setOnlyActiveRecords(true).setParameters(new Object[] { this.get_ID() }).match();
        return hasCosts;
    }
    
    protected boolean afterSave(final boolean newRecord, final boolean success) {
        if (!success) {
            return success;
        }
        if (!newRecord && (this.is_ValueChanged("Value") || this.is_ValueChanged("Name"))) {
            MAccount.updateValueDescription(this.getCtx(), "M_Product_ID=" + this.getM_Product_ID(), this.get_TrxName());
        }
        if (!newRecord && (this.is_ValueChanged("Name") || this.is_ValueChanged("Description"))) {
            final String sql = "UPDATE A_Asset a SET (Name, Description)=(SELECT SUBSTR((SELECT bp.Name FROM C_BPartner bp WHERE bp.C_BPartner_ID=a.C_BPartner_ID) || ' - ' || p.Name,1,60), p.Description FROM M_Product p WHERE p.M_Product_ID=a.M_Product_ID) WHERE IsActive='Y'  AND M_Product_ID=" + this.getM_Product_ID();
            final int no = DB.executeUpdate(sql, this.get_TrxName());
            if (this.log.isLoggable(Level.FINE)) {
                this.log.fine("Asset Description updated #" + no);
            }
        }
        if (newRecord) {
            this.insert_Accounting("M_Product_Acct", "M_Product_Category_Acct", "p.M_Product_Category_ID=" + this.getM_Product_Category_ID());
            this.insert_Tree("PR");
        }
        if (newRecord || this.is_ValueChanged("Value")) {
            this.update_Tree("PR");
        }
        if (newRecord || this.is_ValueChanged("M_Product_Category_ID")) {
            MCostExt.create((MProduct)this);
        }
        return success;
    }
    
    protected boolean beforeDelete() {
        if ("R".equals(this.getProductType()) && this.getS_Resource_ID() > 0) {
            throw new AdempiereException("@S_Resource_ID@<>0");
        }
        if (this.isStocked() || "I".equals(this.getProductType())) {
            final String errMsg = this.verifyStorage();
            if (!Util.isEmpty(errMsg)) {
                this.log.saveError("Error", Msg.parseTranslation(this.getCtx(), errMsg));
                return false;
            }
        }
        MCostExt.delete((MProduct)this);
        return true;
    }
    
    protected boolean afterDelete(final boolean success) {
        if (success) {
            this.delete_Tree("PR");
        }
        return success;
    }
    
    public MAttributeInstance getAttributeInstance(final String name, final String trxName) {
        MAttributeInstance instance = null;
        MTable table = MTable.get(Env.getCtx(), 562);
        final MAttribute attribute = (MAttribute)table.getPO("Name = ?", new Object[] { name }, trxName);
        if (attribute == null) {
            return null;
        }
        table = MTable.get(Env.getCtx(), 561);
        instance = (MAttributeInstance)table.getPO("M_AttributeSetInstance_ID=? and M_Attribute_ID=?", new Object[] { this.getM_AttributeSetInstance_ID(), attribute.getM_Attribute_ID() }, trxName);
        return instance;
    }
    
    public String getMMPolicy() {
        final MProductCategory pc = MProductCategory.get(this.getCtx(), this.getM_Product_Category_ID());
        String MMPolicy = pc.getMMPolicy();
        if (MMPolicy == null || MMPolicy.length() == 0) {
            MMPolicy = MClient.get(this.getCtx()).getMMPolicy();
        }
        return MMPolicy;
    }
    
    public boolean isUseGuaranteeDateForMPolicy() {
        final MAttributeSet as = this.getAttributeSet();
        return as != null && as.isGuaranteeDate() && as.isUseGuaranteeDateForMPolicy();
    }
    
    public boolean isASIMandatory(final boolean isSOTrx) {
        final MAcctSchema[] mass = MAcctSchema.getClientAcctSchema(this.getCtx(), this.getAD_Client_ID(), this.get_TrxName());
        MAcctSchema[] array;
        for (int length = (array = mass).length, i = 0; i < length; ++i) {
            final MAcctSchema as = array[i];
            final String cl = this.getCostingLevel(as);
            if ("B".equals(cl)) {
                return true;
            }
        }
        final int M_AttributeSet_ID = this.getM_AttributeSet_ID();
        if (M_AttributeSet_ID == 0) {
            return false;
        }
        final MAttributeSet mas = MAttributeSet.get(this.getCtx(), M_AttributeSet_ID);
        if (mas == null || !mas.isInstanceAttribute()) {
            return false;
        }
        if (isSOTrx) {
            return mas.isMandatory();
        }
        return mas.isMandatoryAlways();
    }
    
    public String getCostingLevel(final MAcctSchema as) {
        final MProductCategoryAcct pca = MProductCategoryAcct.get(this.getCtx(), this.getM_Product_Category_ID(), as.get_ID(), this.get_TrxName());
        String costingLevel = pca.getCostingLevel();
        if (costingLevel == null) {
            costingLevel = as.getCostingLevel();
        }
        return costingLevel;
    }
    
    public String getCostingMethod(final MAcctSchema as) {
        final MProductCategoryAcct pca = MProductCategoryAcct.get(this.getCtx(), this.getM_Product_Category_ID(), as.get_ID(), this.get_TrxName());
        String costingMethod = pca.getCostingMethod();
        if (costingMethod == null) {
            costingMethod = as.getCostingMethod();
        }
        return costingMethod;
    }
    
    public MCost getCostingRecord(final MAcctSchema as, final int AD_Org_ID, final int M_ASI_ID) {
        return this.getCostingRecord(as, AD_Org_ID, M_ASI_ID, this.getCostingMethod(as));
    }
    
    public MCost getCostingRecord(final MAcctSchema as, int AD_Org_ID, int M_ASI_ID, final String costingMethod) {
        final String costingLevel = this.getCostingLevel(as);
        if ("C".equals(costingLevel)) {
            AD_Org_ID = 0;
            M_ASI_ID = 0;
        }
        else if ("O".equals(costingLevel)) {
            M_ASI_ID = 0;
        }
        else if ("B".equals(costingLevel)) {
            AD_Org_ID = 0;
            if (M_ASI_ID == 0) {
                return null;
            }
        }
        final MCostElement ce = MCostElement.getMaterialCostElement(this.getCtx(), costingMethod, AD_Org_ID);
        if (ce == null) {
            return null;
        }
        final MCost cost = MCost.get((MProduct)this, M_ASI_ID, as, AD_Org_ID, ce.getM_CostElement_ID(), this.get_TrxName());
        return cost.is_new() ? null : cost;
    }
}
