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
/** Generated Model - DO NOT CHANGE */
package org.kjs.pola.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for I_KJS_ProductionPlan
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_I_KJS_ProductionPlan extends PO implements I_I_KJS_ProductionPlan, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20220717L;

    /** Standard Constructor */
    public X_I_KJS_ProductionPlan (Properties ctx, int I_KJS_ProductionPlan_ID, String trxName)
    {
      super (ctx, I_KJS_ProductionPlan_ID, trxName);
      /** if (I_KJS_ProductionPlan_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_I_KJS_ProductionPlan (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_I_KJS_ProductionPlan[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set BOM Quantity.
		@param BOMQty 
		Bill of Materials Quantity
	  */
	public void setBOMQty (BigDecimal BOMQty)
	{
		set_Value (COLUMNNAME_BOMQty, BOMQty);
	}

	/** Get BOM Quantity.
		@return Bill of Materials Quantity
	  */
	public BigDecimal getBOMQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BOMQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_ValueNoCheck (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set I_KJS_ProductionPlan_ID.
		@param I_KJS_ProductionPlan_ID I_KJS_ProductionPlan_ID	  */
	public void setI_KJS_ProductionPlan_ID (int I_KJS_ProductionPlan_ID)
	{
		if (I_KJS_ProductionPlan_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_KJS_ProductionPlan_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_KJS_ProductionPlan_ID, Integer.valueOf(I_KJS_ProductionPlan_ID));
	}

	/** Get I_KJS_ProductionPlan_ID.
		@return I_KJS_ProductionPlan_ID	  */
	public int getI_KJS_ProductionPlan_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_KJS_ProductionPlan_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set I_KJS_ProductionPlan_UU.
		@param I_KJS_ProductionPlan_UU I_KJS_ProductionPlan_UU	  */
	public void setI_KJS_ProductionPlan_UU (String I_KJS_ProductionPlan_UU)
	{
		set_ValueNoCheck (COLUMNNAME_I_KJS_ProductionPlan_UU, I_KJS_ProductionPlan_UU);
	}

	/** Get I_KJS_ProductionPlan_UU.
		@return I_KJS_ProductionPlan_UU	  */
	public String getI_KJS_ProductionPlan_UU () 
	{
		return (String)get_Value(COLUMNNAME_I_KJS_ProductionPlan_UU);
	}

	/** Set Cavity.
		@param KJS_Cavity Cavity	  */
	public void setKJS_Cavity (BigDecimal KJS_Cavity)
	{
		set_Value (COLUMNNAME_KJS_Cavity, KJS_Cavity);
	}

	/** Get Cavity.
		@return Cavity	  */
	public BigDecimal getKJS_Cavity () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_Cavity);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Gear & Feed Up.
		@param KJS_CavityGearFeed Gear & Feed Up	  */
	public void setKJS_CavityGearFeed (BigDecimal KJS_CavityGearFeed)
	{
		set_Value (COLUMNNAME_KJS_CavityGearFeed, KJS_CavityGearFeed);
	}

	/** Get Gear & Feed Up.
		@return Gear & Feed Up	  */
	public BigDecimal getKJS_CavityGearFeed () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_CavityGearFeed);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Jumlah Up.
		@param KJS_CavityJumlah Jumlah Up	  */
	public void setKJS_CavityJumlah (BigDecimal KJS_CavityJumlah)
	{
		set_Value (COLUMNNAME_KJS_CavityJumlah, KJS_CavityJumlah);
	}

	/** Get Jumlah Up.
		@return Jumlah Up	  */
	public BigDecimal getKJS_CavityJumlah () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_CavityJumlah);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Lebar Roll & Panjang Kertas.
		@param KJS_CavityRollKertas Lebar Roll & Panjang Kertas	  */
	public void setKJS_CavityRollKertas (BigDecimal KJS_CavityRollKertas)
	{
		set_Value (COLUMNNAME_KJS_CavityRollKertas, KJS_CavityRollKertas);
	}

	/** Get Lebar Roll & Panjang Kertas.
		@return Lebar Roll & Panjang Kertas	  */
	public BigDecimal getKJS_CavityRollKertas () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_CavityRollKertas);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Ukuran Sheet.
		@param KJS_CavityUkuran Ukuran Sheet	  */
	public void setKJS_CavityUkuran (BigDecimal KJS_CavityUkuran)
	{
		set_Value (COLUMNNAME_KJS_CavityUkuran, KJS_CavityUkuran);
	}

	/** Get Ukuran Sheet.
		@return Ukuran Sheet	  */
	public BigDecimal getKJS_CavityUkuran () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_CavityUkuran);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_KJS_Phase getKJS_Phase() throws RuntimeException
    {
		return (I_KJS_Phase)MTable.get(getCtx(), I_KJS_Phase.Table_Name)
			.getPO(getKJS_Phase_ID(), get_TrxName());	}

	/** Set Phase.
		@param KJS_Phase_ID Phase	  */
	public void setKJS_Phase_ID (int KJS_Phase_ID)
	{
		if (KJS_Phase_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_KJS_Phase_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_KJS_Phase_ID, Integer.valueOf(KJS_Phase_ID));
	}

	/** Get Phase.
		@return Phase	  */
	public int getKJS_Phase_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_KJS_Phase_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_KJS_ProductAsset getKJS_ProductAsset() throws RuntimeException
    {
		return (I_KJS_ProductAsset)MTable.get(getCtx(), I_KJS_ProductAsset.Table_Name)
			.getPO(getKJS_ProductAsset_ID(), get_TrxName());	}

	/** Set Product Asset.
		@param KJS_ProductAsset_ID Product Asset	  */
	public void setKJS_ProductAsset_ID (int KJS_ProductAsset_ID)
	{
		if (KJS_ProductAsset_ID < 1) 
			set_Value (COLUMNNAME_KJS_ProductAsset_ID, null);
		else 
			set_Value (COLUMNNAME_KJS_ProductAsset_ID, Integer.valueOf(KJS_ProductAsset_ID));
	}

	/** Get Product Asset.
		@return Product Asset	  */
	public int getKJS_ProductAsset_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_KJS_ProductAsset_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_KJS_ProductionPlan getKJS_ProductionPlan() throws RuntimeException
    {
		return (I_KJS_ProductionPlan)MTable.get(getCtx(), I_KJS_ProductionPlan.Table_Name)
			.getPO(getKJS_ProductionPlan_ID(), get_TrxName());	}

	/** Set SPK Induk.
		@param KJS_ProductionPlan_ID SPK Induk	  */
	public void setKJS_ProductionPlan_ID (int KJS_ProductionPlan_ID)
	{
		if (KJS_ProductionPlan_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_KJS_ProductionPlan_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_KJS_ProductionPlan_ID, Integer.valueOf(KJS_ProductionPlan_ID));
	}

	/** Get SPK Induk.
		@return SPK Induk	  */
	public int getKJS_ProductionPlan_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_KJS_ProductionPlan_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_KJS_ProductionPlanLine getKJS_ProductionPlanLine() throws RuntimeException
    {
		return (I_KJS_ProductionPlanLine)MTable.get(getCtx(), I_KJS_ProductionPlanLine.Table_Name)
			.getPO(getKJS_ProductionPlanLine_ID(), get_TrxName());	}

	/** Set SPK Phase.
		@param KJS_ProductionPlanLine_ID SPK Phase	  */
	public void setKJS_ProductionPlanLine_ID (int KJS_ProductionPlanLine_ID)
	{
		if (KJS_ProductionPlanLine_ID < 1) 
			set_Value (COLUMNNAME_KJS_ProductionPlanLine_ID, null);
		else 
			set_Value (COLUMNNAME_KJS_ProductionPlanLine_ID, Integer.valueOf(KJS_ProductionPlanLine_ID));
	}

	/** Get SPK Phase.
		@return SPK Phase	  */
	public int getKJS_ProductionPlanLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_KJS_ProductionPlanLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Line No.
		@param Line 
		Unique line for this document
	  */
	public void setLine (int Line)
	{
		set_ValueNoCheck (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_Alternate getM_Alternate() throws RuntimeException
    {
		return (I_M_Alternate)MTable.get(getCtx(), I_M_Alternate.Table_Name)
			.getPO(getM_Alternate_ID(), get_TrxName());	}

	/** Set Alternate.
		@param M_Alternate_ID Alternate	  */
	public void setM_Alternate_ID (int M_Alternate_ID)
	{
		if (M_Alternate_ID < 1) 
			set_Value (COLUMNNAME_M_Alternate_ID, null);
		else 
			set_Value (COLUMNNAME_M_Alternate_ID, Integer.valueOf(M_Alternate_ID));
	}

	/** Get Alternate.
		@return Alternate	  */
	public int getM_Alternate_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Alternate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_Alternate getM_AlternateBOM() throws RuntimeException
    {
		return (I_M_Alternate)MTable.get(getCtx(), I_M_Alternate.Table_Name)
			.getPO(getM_AlternateBOM_ID(), get_TrxName());	}

	/** Set M_AlternateBOM_ID.
		@param M_AlternateBOM_ID M_AlternateBOM_ID	  */
	public void setM_AlternateBOM_ID (int M_AlternateBOM_ID)
	{
		if (M_AlternateBOM_ID < 1) 
			set_Value (COLUMNNAME_M_AlternateBOM_ID, null);
		else 
			set_Value (COLUMNNAME_M_AlternateBOM_ID, Integer.valueOf(M_AlternateBOM_ID));
	}

	/** Get M_AlternateBOM_ID.
		@return M_AlternateBOM_ID	  */
	public int getM_AlternateBOM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_AlternateBOM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_Locator getM_Locator() throws RuntimeException
    {
		return (I_M_Locator)MTable.get(getCtx(), I_M_Locator.Table_Name)
			.getPO(getM_Locator_ID(), get_TrxName());	}

	/** Set Locator.
		@param M_Locator_ID 
		Warehouse Locator
	  */
	public void setM_Locator_ID (int M_Locator_ID)
	{
		if (M_Locator_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Locator_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Locator_ID, Integer.valueOf(M_Locator_ID));
	}

	/** Get Locator.
		@return Warehouse Locator
	  */
	public int getM_Locator_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Locator_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_M_Product getProductBOM() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getProductBOM_ID(), get_TrxName());	}

	/** Set ProductBOM_ID.
		@param ProductBOM_ID ProductBOM_ID	  */
	public void setProductBOM_ID (int ProductBOM_ID)
	{
		if (ProductBOM_ID < 1) 
			set_Value (COLUMNNAME_ProductBOM_ID, null);
		else 
			set_Value (COLUMNNAME_ProductBOM_ID, Integer.valueOf(ProductBOM_ID));
	}

	/** Get ProductBOM_ID.
		@return ProductBOM_ID	  */
	public int getProductBOM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ProductBOM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Quantity.
		@param Qty 
		Quantity
	  */
	public void setQty (BigDecimal Qty)
	{
		set_Value (COLUMNNAME_Qty, Qty);
	}

	/** Get Quantity.
		@return Quantity
	  */
	public BigDecimal getQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Qty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Quantity.
		@param QtyEntered 
		The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEntered (BigDecimal QtyEntered)
	{
		set_ValueNoCheck (COLUMNNAME_QtyEntered, QtyEntered);
	}

	/** Get Quantity.
		@return The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEntered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyEntered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}