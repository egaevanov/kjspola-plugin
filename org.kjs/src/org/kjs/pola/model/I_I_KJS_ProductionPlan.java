/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.kjs.pola.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for I_KJS_ProductionPlan
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_I_KJS_ProductionPlan 
{

    /** TableName=I_KJS_ProductionPlan */
    public static final String Table_Name = "I_KJS_ProductionPlan";

    /** AD_Table_ID=1000048 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name BOMQty */
    public static final String COLUMNNAME_BOMQty = "BOMQty";

	/** Set BOM Quantity.
	  * Bill of Materials Quantity
	  */
	public void setBOMQty (BigDecimal BOMQty);

	/** Get BOM Quantity.
	  * Bill of Materials Quantity
	  */
	public BigDecimal getBOMQty();

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DateDoc */
    public static final String COLUMNNAME_DateDoc = "DateDoc";

	/** Set Document Date.
	  * Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc);

	/** Get Document Date.
	  * Date of the Document
	  */
	public Timestamp getDateDoc();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name I_KJS_ProductionPlan_ID */
    public static final String COLUMNNAME_I_KJS_ProductionPlan_ID = "I_KJS_ProductionPlan_ID";

	/** Set I_KJS_ProductionPlan_ID	  */
	public void setI_KJS_ProductionPlan_ID (int I_KJS_ProductionPlan_ID);

	/** Get I_KJS_ProductionPlan_ID	  */
	public int getI_KJS_ProductionPlan_ID();

    /** Column name I_KJS_ProductionPlan_UU */
    public static final String COLUMNNAME_I_KJS_ProductionPlan_UU = "I_KJS_ProductionPlan_UU";

	/** Set I_KJS_ProductionPlan_UU	  */
	public void setI_KJS_ProductionPlan_UU (String I_KJS_ProductionPlan_UU);

	/** Get I_KJS_ProductionPlan_UU	  */
	public String getI_KJS_ProductionPlan_UU();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name KJS_Cavity */
    public static final String COLUMNNAME_KJS_Cavity = "KJS_Cavity";

	/** Set Cavity	  */
	public void setKJS_Cavity (BigDecimal KJS_Cavity);

	/** Get Cavity	  */
	public BigDecimal getKJS_Cavity();

    /** Column name KJS_CavityGearFeed */
    public static final String COLUMNNAME_KJS_CavityGearFeed = "KJS_CavityGearFeed";

	/** Set Gear & Feed Up	  */
	public void setKJS_CavityGearFeed (BigDecimal KJS_CavityGearFeed);

	/** Get Gear & Feed Up	  */
	public BigDecimal getKJS_CavityGearFeed();

    /** Column name KJS_CavityJumlah */
    public static final String COLUMNNAME_KJS_CavityJumlah = "KJS_CavityJumlah";

	/** Set Jumlah Up	  */
	public void setKJS_CavityJumlah (BigDecimal KJS_CavityJumlah);

	/** Get Jumlah Up	  */
	public BigDecimal getKJS_CavityJumlah();

    /** Column name KJS_CavityRollKertas */
    public static final String COLUMNNAME_KJS_CavityRollKertas = "KJS_CavityRollKertas";

	/** Set Lebar Roll & Panjang Kertas	  */
	public void setKJS_CavityRollKertas (BigDecimal KJS_CavityRollKertas);

	/** Get Lebar Roll & Panjang Kertas	  */
	public BigDecimal getKJS_CavityRollKertas();

    /** Column name KJS_CavityUkuran */
    public static final String COLUMNNAME_KJS_CavityUkuran = "KJS_CavityUkuran";

	/** Set Ukuran Sheet	  */
	public void setKJS_CavityUkuran (BigDecimal KJS_CavityUkuran);

	/** Get Ukuran Sheet	  */
	public BigDecimal getKJS_CavityUkuran();

    /** Column name KJS_Phase_ID */
    public static final String COLUMNNAME_KJS_Phase_ID = "KJS_Phase_ID";

	/** Set Phase	  */
	public void setKJS_Phase_ID (int KJS_Phase_ID);

	/** Get Phase	  */
	public int getKJS_Phase_ID();

	public I_KJS_Phase getKJS_Phase() throws RuntimeException;

    /** Column name KJS_ProductAsset_ID */
    public static final String COLUMNNAME_KJS_ProductAsset_ID = "KJS_ProductAsset_ID";

	/** Set Product Asset	  */
	public void setKJS_ProductAsset_ID (int KJS_ProductAsset_ID);

	/** Get Product Asset	  */
	public int getKJS_ProductAsset_ID();

	public I_KJS_ProductAsset getKJS_ProductAsset() throws RuntimeException;

    /** Column name KJS_ProductionPlan_ID */
    public static final String COLUMNNAME_KJS_ProductionPlan_ID = "KJS_ProductionPlan_ID";

	/** Set SPK Induk	  */
	public void setKJS_ProductionPlan_ID (int KJS_ProductionPlan_ID);

	/** Get SPK Induk	  */
	public int getKJS_ProductionPlan_ID();

	public I_KJS_ProductionPlan getKJS_ProductionPlan() throws RuntimeException;

    /** Column name KJS_ProductionPlanLine_ID */
    public static final String COLUMNNAME_KJS_ProductionPlanLine_ID = "KJS_ProductionPlanLine_ID";

	/** Set SPK Phase	  */
	public void setKJS_ProductionPlanLine_ID (int KJS_ProductionPlanLine_ID);

	/** Get SPK Phase	  */
	public int getKJS_ProductionPlanLine_ID();

	public I_KJS_ProductionPlanLine getKJS_ProductionPlanLine() throws RuntimeException;

    /** Column name Line */
    public static final String COLUMNNAME_Line = "Line";

	/** Set Line No.
	  * Unique line for this document
	  */
	public void setLine (int Line);

	/** Get Line No.
	  * Unique line for this document
	  */
	public int getLine();

    /** Column name M_Alternate_ID */
    public static final String COLUMNNAME_M_Alternate_ID = "M_Alternate_ID";

	/** Set Alternate	  */
	public void setM_Alternate_ID (int M_Alternate_ID);

	/** Get Alternate	  */
	public int getM_Alternate_ID();

	public I_M_Alternate getM_Alternate() throws RuntimeException;

    /** Column name M_AlternateBOM_ID */
    public static final String COLUMNNAME_M_AlternateBOM_ID = "M_AlternateBOM_ID";

	/** Set M_AlternateBOM_ID	  */
	public void setM_AlternateBOM_ID (int M_AlternateBOM_ID);

	/** Get M_AlternateBOM_ID	  */
	public int getM_AlternateBOM_ID();

	public I_M_Alternate getM_AlternateBOM() throws RuntimeException;

    /** Column name M_Locator_ID */
    public static final String COLUMNNAME_M_Locator_ID = "M_Locator_ID";

	/** Set Locator.
	  * Warehouse Locator
	  */
	public void setM_Locator_ID (int M_Locator_ID);

	/** Get Locator.
	  * Warehouse Locator
	  */
	public int getM_Locator_ID();

	public I_M_Locator getM_Locator() throws RuntimeException;

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException;

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name ProductBOM_ID */
    public static final String COLUMNNAME_ProductBOM_ID = "ProductBOM_ID";

	/** Set ProductBOM_ID	  */
	public void setProductBOM_ID (int ProductBOM_ID);

	/** Get ProductBOM_ID	  */
	public int getProductBOM_ID();

	public org.compiere.model.I_M_Product getProductBOM() throws RuntimeException;

    /** Column name Qty */
    public static final String COLUMNNAME_Qty = "Qty";

	/** Set Quantity.
	  * Quantity
	  */
	public void setQty (BigDecimal Qty);

	/** Get Quantity.
	  * Quantity
	  */
	public BigDecimal getQty();

    /** Column name QtyEntered */
    public static final String COLUMNNAME_QtyEntered = "QtyEntered";

	/** Set Quantity.
	  * The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEntered (BigDecimal QtyEntered);

	/** Get Quantity.
	  * The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEntered();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
