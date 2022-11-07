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

/** Generated Interface for KJS_QC
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_KJS_QC 
{

    /** TableName=KJS_QC */
    public static final String Table_Name = "KJS_QC";

    /** AD_Table_ID=1000013 */
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

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name Die_QHold */
    public static final String COLUMNNAME_Die_QHold = "Die_QHold";

	/** Set Die_QHold	  */
	public void setDie_QHold (BigDecimal Die_QHold);

	/** Get Die_QHold	  */
	public BigDecimal getDie_QHold();

    /** Column name Die_QPass */
    public static final String COLUMNNAME_Die_QPass = "Die_QPass";

	/** Set Die_QPass	  */
	public void setDie_QPass (BigDecimal Die_QPass);

	/** Get Die_QPass	  */
	public BigDecimal getDie_QPass();

    /** Column name Die_QPrd */
    public static final String COLUMNNAME_Die_QPrd = "Die_QPrd";

	/** Set Die_QPrd	  */
	public void setDie_QPrd (BigDecimal Die_QPrd);

	/** Get Die_QPrd	  */
	public BigDecimal getDie_QPrd();

    /** Column name Die_QRej */
    public static final String COLUMNNAME_Die_QRej = "Die_QRej";

	/** Set Die_QRej	  */
	public void setDie_QRej (BigDecimal Die_QRej);

	/** Get Die_QRej	  */
	public BigDecimal getDie_QRej();

    /** Column name Die_QWaste */
    public static final String COLUMNNAME_Die_QWaste = "Die_QWaste";

	/** Set Die_QWaste	  */
	public void setDie_QWaste (BigDecimal Die_QWaste);

	/** Get Die_QWaste	  */
	public BigDecimal getDie_QWaste();

    /** Column name Dril_QHold */
    public static final String COLUMNNAME_Dril_QHold = "Dril_QHold";

	/** Set Dril_QHold	  */
	public void setDril_QHold (BigDecimal Dril_QHold);

	/** Get Dril_QHold	  */
	public BigDecimal getDril_QHold();

    /** Column name Dril_QPass */
    public static final String COLUMNNAME_Dril_QPass = "Dril_QPass";

	/** Set Dril_QPass	  */
	public void setDril_QPass (BigDecimal Dril_QPass);

	/** Get Dril_QPass	  */
	public BigDecimal getDril_QPass();

    /** Column name Dril_QPrd */
    public static final String COLUMNNAME_Dril_QPrd = "Dril_QPrd";

	/** Set Dril_QPrd	  */
	public void setDril_QPrd (BigDecimal Dril_QPrd);

	/** Get Dril_QPrd	  */
	public BigDecimal getDril_QPrd();

    /** Column name Dril_QRej */
    public static final String COLUMNNAME_Dril_QRej = "Dril_QRej";

	/** Set Dril_QRej	  */
	public void setDril_QRej (BigDecimal Dril_QRej);

	/** Get Dril_QRej	  */
	public BigDecimal getDril_QRej();

    /** Column name Dril_QWaste */
    public static final String COLUMNNAME_Dril_QWaste = "Dril_QWaste";

	/** Set Dril_QWaste	  */
	public void setDril_QWaste (BigDecimal Dril_QWaste);

	/** Get Dril_QWaste	  */
	public BigDecimal getDril_QWaste();

    /** Column name Flex_QHold */
    public static final String COLUMNNAME_Flex_QHold = "Flex_QHold";

	/** Set Flex_QHold	  */
	public void setFlex_QHold (BigDecimal Flex_QHold);

	/** Get Flex_QHold	  */
	public BigDecimal getFlex_QHold();

    /** Column name Flex_QPass */
    public static final String COLUMNNAME_Flex_QPass = "Flex_QPass";

	/** Set Flex_QPass	  */
	public void setFlex_QPass (BigDecimal Flex_QPass);

	/** Get Flex_QPass	  */
	public BigDecimal getFlex_QPass();

    /** Column name Flex_QPrd */
    public static final String COLUMNNAME_Flex_QPrd = "Flex_QPrd";

	/** Set Flex_QPrd	  */
	public void setFlex_QPrd (BigDecimal Flex_QPrd);

	/** Get Flex_QPrd	  */
	public BigDecimal getFlex_QPrd();

    /** Column name Flex_QRej */
    public static final String COLUMNNAME_Flex_QRej = "Flex_QRej";

	/** Set Flex_QRej	  */
	public void setFlex_QRej (BigDecimal Flex_QRej);

	/** Get Flex_QRej	  */
	public BigDecimal getFlex_QRej();

    /** Column name Flex_QWaste */
    public static final String COLUMNNAME_Flex_QWaste = "Flex_QWaste";

	/** Set Flex_QWaste	  */
	public void setFlex_QWaste (BigDecimal Flex_QWaste);

	/** Get Flex_QWaste	  */
	public BigDecimal getFlex_QWaste();

    /** Column name IK_Incom_Description */
    public static final String COLUMNNAME_IK_Incom_Description = "IK_Incom_Description";

	/** Set IK_Incom_Description	  */
	public void setIK_Incom_Description (String IK_Incom_Description);

	/** Get IK_Incom_Description	  */
	public String getIK_Incom_Description();

    /** Column name IK_Incom_Inspector */
    public static final String COLUMNNAME_IK_Incom_Inspector = "IK_Incom_Inspector";

	/** Set IK_Incom_Inspector	  */
	public void setIK_Incom_Inspector (String IK_Incom_Inspector);

	/** Get IK_Incom_Inspector	  */
	public String getIK_Incom_Inspector();

    /** Column name IK_Incom_Is_Bau */
    public static final String COLUMNNAME_IK_Incom_Is_Bau = "IK_Incom_Is_Bau";

	/** Set IK_Incom_Is_Bau	  */
	public void setIK_Incom_Is_Bau (boolean IK_Incom_Is_Bau);

	/** Get IK_Incom_Is_Bau	  */
	public boolean isIK_Incom_Is_Bau();

    /** Column name IK_Incom_Is_Gramature */
    public static final String COLUMNNAME_IK_Incom_Is_Gramature = "IK_Incom_Is_Gramature";

	/** Set IK_Incom_Is_Gramature	  */
	public void setIK_Incom_Is_Gramature (boolean IK_Incom_Is_Gramature);

	/** Get IK_Incom_Is_Gramature	  */
	public boolean isIK_Incom_Is_Gramature();

    /** Column name IK_Incom_Is_Haram */
    public static final String COLUMNNAME_IK_Incom_Is_Haram = "IK_Incom_Is_Haram";

	/** Set IK_Incom_Is_Haram	  */
	public void setIK_Incom_Is_Haram (boolean IK_Incom_Is_Haram);

	/** Get IK_Incom_Is_Haram	  */
	public boolean isIK_Incom_Is_Haram();

    /** Column name IK_Incom_Is_Kebersihan */
    public static final String COLUMNNAME_IK_Incom_Is_Kebersihan = "IK_Incom_Is_Kebersihan";

	/** Set IK_Incom_Is_Kebersihan	  */
	public void setIK_Incom_Is_Kebersihan (boolean IK_Incom_Is_Kebersihan);

	/** Get IK_Incom_Is_Kebersihan	  */
	public boolean isIK_Incom_Is_Kebersihan();

    /** Column name IK_Incom_Is_Ketebalan */
    public static final String COLUMNNAME_IK_Incom_Is_Ketebalan = "IK_Incom_Is_Ketebalan";

	/** Set IK_Incom_Is_Ketebalan	  */
	public void setIK_Incom_Is_Ketebalan (boolean IK_Incom_Is_Ketebalan);

	/** Get IK_Incom_Is_Ketebalan	  */
	public boolean isIK_Incom_Is_Ketebalan();

    /** Column name IK_Incom_Is_Label */
    public static final String COLUMNNAME_IK_Incom_Is_Label = "IK_Incom_Is_Label";

	/** Set IK_Incom_Is_Label	  */
	public void setIK_Incom_Is_Label (boolean IK_Incom_Is_Label);

	/** Get IK_Incom_Is_Label	  */
	public boolean isIK_Incom_Is_Label();

    /** Column name IK_Incom_Is_Packing */
    public static final String COLUMNNAME_IK_Incom_Is_Packing = "IK_Incom_Is_Packing";

	/** Set IK_Incom_Is_Packing	  */
	public void setIK_Incom_Is_Packing (boolean IK_Incom_Is_Packing);

	/** Get IK_Incom_Is_Packing	  */
	public boolean isIK_Incom_Is_Packing();

    /** Column name IK_Incom_Is_Paper_Core */
    public static final String COLUMNNAME_IK_Incom_Is_Paper_Core = "IK_Incom_Is_Paper_Core";

	/** Set IK_Incom_Is_Paper_Core	  */
	public void setIK_Incom_Is_Paper_Core (boolean IK_Incom_Is_Paper_Core);

	/** Get IK_Incom_Is_Paper_Core	  */
	public boolean isIK_Incom_Is_Paper_Core();

    /** Column name IK_Incom_Is_Permukaan */
    public static final String COLUMNNAME_IK_Incom_Is_Permukaan = "IK_Incom_Is_Permukaan";

	/** Set IK_Incom_Is_Permukaan	  */
	public void setIK_Incom_Is_Permukaan (boolean IK_Incom_Is_Permukaan);

	/** Get IK_Incom_Is_Permukaan	  */
	public boolean isIK_Incom_Is_Permukaan();

    /** Column name IK_Incom_Is_Warna */
    public static final String COLUMNNAME_IK_Incom_Is_Warna = "IK_Incom_Is_Warna";

	/** Set IK_Incom_Is_Warna	  */
	public void setIK_Incom_Is_Warna (boolean IK_Incom_Is_Warna);

	/** Get IK_Incom_Is_Warna	  */
	public boolean isIK_Incom_Is_Warna();

    /** Column name IK_Incom_Jml_Dtg */
    public static final String COLUMNNAME_IK_Incom_Jml_Dtg = "IK_Incom_Jml_Dtg";

	/** Set IK_Incom_Jml_Dtg	  */
	public void setIK_Incom_Jml_Dtg (BigDecimal IK_Incom_Jml_Dtg);

	/** Get IK_Incom_Jml_Dtg	  */
	public BigDecimal getIK_Incom_Jml_Dtg();

    /** Column name IK_Incom_Jml_Sampel */
    public static final String COLUMNNAME_IK_Incom_Jml_Sampel = "IK_Incom_Jml_Sampel";

	/** Set IK_Incom_Jml_Sampel	  */
	public void setIK_Incom_Jml_Sampel (BigDecimal IK_Incom_Jml_Sampel);

	/** Get IK_Incom_Jml_Sampel	  */
	public BigDecimal getIK_Incom_Jml_Sampel();

    /** Column name IK_Incom_Lebar_Roll */
    public static final String COLUMNNAME_IK_Incom_Lebar_Roll = "IK_Incom_Lebar_Roll";

	/** Set IK_Incom_Lebar_Roll	  */
	public void setIK_Incom_Lebar_Roll (BigDecimal IK_Incom_Lebar_Roll);

	/** Get IK_Incom_Lebar_Roll	  */
	public BigDecimal getIK_Incom_Lebar_Roll();

    /** Column name IK_Incom_QC_Status */
    public static final String COLUMNNAME_IK_Incom_QC_Status = "IK_Incom_QC_Status";

	/** Set IK_Incom_QC_Status	  */
	public void setIK_Incom_QC_Status (String IK_Incom_QC_Status);

	/** Get IK_Incom_QC_Status	  */
	public String getIK_Incom_QC_Status();

    /** Column name IK_Incom_Quality_Mgr */
    public static final String COLUMNNAME_IK_Incom_Quality_Mgr = "IK_Incom_Quality_Mgr";

	/** Set IK_Incom_Quality_Mgr	  */
	public void setIK_Incom_Quality_Mgr (String IK_Incom_Quality_Mgr);

	/** Get IK_Incom_Quality_Mgr	  */
	public String getIK_Incom_Quality_Mgr();

    /** Column name IK_Incom_Tgl_Dtg */
    public static final String COLUMNNAME_IK_Incom_Tgl_Dtg = "IK_Incom_Tgl_Dtg";

	/** Set IK_Incom_Tgl_Dtg	  */
	public void setIK_Incom_Tgl_Dtg (Timestamp IK_Incom_Tgl_Dtg);

	/** Get IK_Incom_Tgl_Dtg	  */
	public Timestamp getIK_Incom_Tgl_Dtg();

    /** Column name IK_Incom_Tgl_Periksa */
    public static final String COLUMNNAME_IK_Incom_Tgl_Periksa = "IK_Incom_Tgl_Periksa";

	/** Set IK_Incom_Tgl_Periksa	  */
	public void setIK_Incom_Tgl_Periksa (Timestamp IK_Incom_Tgl_Periksa);

	/** Get IK_Incom_Tgl_Periksa	  */
	public Timestamp getIK_Incom_Tgl_Periksa();

    /** Column name IK_Incom_Trans_Is_Ada_Cemaran */
    public static final String COLUMNNAME_IK_Incom_Trans_Is_Ada_Cemaran = "IK_Incom_Trans_Is_Ada_Cemaran";

	/** Set IK_Incom_Trans_Is_Ada_Cemaran	  */
	public void setIK_Incom_Trans_Is_Ada_Cemaran (boolean IK_Incom_Trans_Is_Ada_Cemaran);

	/** Get IK_Incom_Trans_Is_Ada_Cemaran	  */
	public boolean isIK_Incom_Trans_Is_Ada_Cemaran();

    /** Column name IK_Ripple_Description */
    public static final String COLUMNNAME_IK_Ripple_Description = "IK_Ripple_Description";

	/** Set IK_Ripple_Description	  */
	public void setIK_Ripple_Description (String IK_Ripple_Description);

	/** Get IK_Ripple_Description	  */
	public String getIK_Ripple_Description();

    /** Column name IK_Ripple_isBlankDrill */
    public static final String COLUMNNAME_IK_Ripple_isBlankDrill = "IK_Ripple_isBlankDrill";

	/** Set IK_Ripple_isBlankDrill	  */
	public void setIK_Ripple_isBlankDrill (boolean IK_Ripple_isBlankDrill);

	/** Get IK_Ripple_isBlankDrill	  */
	public boolean isIK_Ripple_isBlankDrill();

    /** Column name IK_Ripple_isKontaminasiBiologi */
    public static final String COLUMNNAME_IK_Ripple_isKontaminasiBiologi = "IK_Ripple_isKontaminasiBiologi";

	/** Set IK_Ripple_isKontaminasiBiologi	  */
	public void setIK_Ripple_isKontaminasiBiologi (boolean IK_Ripple_isKontaminasiBiologi);

	/** Get IK_Ripple_isKontaminasiBiologi	  */
	public boolean isIK_Ripple_isKontaminasiBiologi();

    /** Column name IK_Ripple_isKontaminasiFisik */
    public static final String COLUMNNAME_IK_Ripple_isKontaminasiFisik = "IK_Ripple_isKontaminasiFisik";

	/** Set IK_Ripple_isKontaminasiFisik	  */
	public void setIK_Ripple_isKontaminasiFisik (boolean IK_Ripple_isKontaminasiFisik);

	/** Get IK_Ripple_isKontaminasiFisik	  */
	public boolean isIK_Ripple_isKontaminasiFisik();

    /** Column name IK_Ripple_isKontaminasiKimia */
    public static final String COLUMNNAME_IK_Ripple_isKontaminasiKimia = "IK_Ripple_isKontaminasiKimia";

	/** Set IK_Ripple_isKontaminasiKimia	  */
	public void setIK_Ripple_isKontaminasiKimia (boolean IK_Ripple_isKontaminasiKimia);

	/** Get IK_Ripple_isKontaminasiKimia	  */
	public boolean isIK_Ripple_isKontaminasiKimia();

    /** Column name IK_Ripple_isPackingAkhir */
    public static final String COLUMNNAME_IK_Ripple_isPackingAkhir = "IK_Ripple_isPackingAkhir";

	/** Set IK_Ripple_isPackingAkhir	  */
	public void setIK_Ripple_isPackingAkhir (boolean IK_Ripple_isPackingAkhir);

	/** Get IK_Ripple_isPackingAkhir	  */
	public boolean isIK_Ripple_isPackingAkhir();

    /** Column name IK_Ripple_isPengeleman */
    public static final String COLUMNNAME_IK_Ripple_isPengeleman = "IK_Ripple_isPengeleman";

	/** Set IK_Ripple_isPengeleman	  */
	public void setIK_Ripple_isPengeleman (boolean IK_Ripple_isPengeleman);

	/** Get IK_Ripple_isPengeleman	  */
	public boolean isIK_Ripple_isPengeleman();

    /** Column name IK_Ripple_JmlSampling */
    public static final String COLUMNNAME_IK_Ripple_JmlSampling = "IK_Ripple_JmlSampling";

	/** Set IK_Ripple_JmlSampling	  */
	public void setIK_Ripple_JmlSampling (BigDecimal IK_Ripple_JmlSampling);

	/** Get IK_Ripple_JmlSampling	  */
	public BigDecimal getIK_Ripple_JmlSampling();

    /** Column name IK_Ripple_QCStatus */
    public static final String COLUMNNAME_IK_Ripple_QCStatus = "IK_Ripple_QCStatus";

	/** Set IK_Ripple_QCStatus	  */
	public void setIK_Ripple_QCStatus (String IK_Ripple_QCStatus);

	/** Get IK_Ripple_QCStatus	  */
	public String getIK_Ripple_QCStatus();

    /** Column name IK_Ripple_QM */
    public static final String COLUMNNAME_IK_Ripple_QM = "IK_Ripple_QM";

	/** Set IK_Ripple_QM	  */
	public void setIK_Ripple_QM (String IK_Ripple_QM);

	/** Get IK_Ripple_QM	  */
	public String getIK_Ripple_QM();

    /** Column name IK_Ripple_Time */
    public static final String COLUMNNAME_IK_Ripple_Time = "IK_Ripple_Time";

	/** Set IK_Ripple_Time	  */
	public void setIK_Ripple_Time (Timestamp IK_Ripple_Time);

	/** Get IK_Ripple_Time	  */
	public Timestamp getIK_Ripple_Time();

    /** Column name IK_Stripping_Description */
    public static final String COLUMNNAME_IK_Stripping_Description = "IK_Stripping_Description";

	/** Set IK_Stripping_Description	  */
	public void setIK_Stripping_Description (String IK_Stripping_Description);

	/** Get IK_Stripping_Description	  */
	public String getIK_Stripping_Description();

    /** Column name IK_Stripping_isInspeksiCutting */
    public static final String COLUMNNAME_IK_Stripping_isInspeksiCutting = "IK_Stripping_isInspeksiCutting";

	/** Set IK_Stripping_isInspeksiCutting	  */
	public void setIK_Stripping_isInspeksiCutting (boolean IK_Stripping_isInspeksiCutting);

	/** Get IK_Stripping_isInspeksiCutting	  */
	public boolean isIK_Stripping_isInspeksiCutting();

    /** Column name IK_Stripping_isKontaminasiBio */
    public static final String COLUMNNAME_IK_Stripping_isKontaminasiBio = "IK_Stripping_isKontaminasiBio";

	/** Set IK_Stripping_isKontaminasiBio	  */
	public void setIK_Stripping_isKontaminasiBio (boolean IK_Stripping_isKontaminasiBio);

	/** Get IK_Stripping_isKontaminasiBio	  */
	public boolean isIK_Stripping_isKontaminasiBio();

    /** Column name IK_Stripping_isKontaminasiFis */
    public static final String COLUMNNAME_IK_Stripping_isKontaminasiFis = "IK_Stripping_isKontaminasiFis";

	/** Set IK_Stripping_isKontaminasiFis	  */
	public void setIK_Stripping_isKontaminasiFis (boolean IK_Stripping_isKontaminasiFis);

	/** Get IK_Stripping_isKontaminasiFis	  */
	public boolean isIK_Stripping_isKontaminasiFis();

    /** Column name IK_Stripping_isKontaminasiKim */
    public static final String COLUMNNAME_IK_Stripping_isKontaminasiKim = "IK_Stripping_isKontaminasiKim";

	/** Set IK_Stripping_isKontaminasiKim	  */
	public void setIK_Stripping_isKontaminasiKim (boolean IK_Stripping_isKontaminasiKim);

	/** Get IK_Stripping_isKontaminasiKim	  */
	public boolean isIK_Stripping_isKontaminasiKim();

    /** Column name IK_Stripping_QCStatus */
    public static final String COLUMNNAME_IK_Stripping_QCStatus = "IK_Stripping_QCStatus";

	/** Set IK_Stripping_QCStatus	  */
	public void setIK_Stripping_QCStatus (String IK_Stripping_QCStatus);

	/** Get IK_Stripping_QCStatus	  */
	public String getIK_Stripping_QCStatus();

    /** Column name IK_Stripping_QM */
    public static final String COLUMNNAME_IK_Stripping_QM = "IK_Stripping_QM";

	/** Set IK_Stripping_QM	  */
	public void setIK_Stripping_QM (String IK_Stripping_QM);

	/** Get IK_Stripping_QM	  */
	public String getIK_Stripping_QM();

    /** Column name Ins_QHold */
    public static final String COLUMNNAME_Ins_QHold = "Ins_QHold";

	/** Set Ins_QHold	  */
	public void setIns_QHold (BigDecimal Ins_QHold);

	/** Get Ins_QHold	  */
	public BigDecimal getIns_QHold();

    /** Column name Ins_QPass */
    public static final String COLUMNNAME_Ins_QPass = "Ins_QPass";

	/** Set Ins_QPass	  */
	public void setIns_QPass (BigDecimal Ins_QPass);

	/** Get Ins_QPass	  */
	public BigDecimal getIns_QPass();

    /** Column name Ins_QPrd */
    public static final String COLUMNNAME_Ins_QPrd = "Ins_QPrd";

	/** Set Ins_QPrd	  */
	public void setIns_QPrd (BigDecimal Ins_QPrd);

	/** Get Ins_QPrd	  */
	public BigDecimal getIns_QPrd();

    /** Column name Ins_QRej */
    public static final String COLUMNNAME_Ins_QRej = "Ins_QRej";

	/** Set Ins_QRej	  */
	public void setIns_QRej (BigDecimal Ins_QRej);

	/** Get Ins_QRej	  */
	public BigDecimal getIns_QRej();

    /** Column name Ins_QWaste */
    public static final String COLUMNNAME_Ins_QWaste = "Ins_QWaste";

	/** Set Ins_QWaste	  */
	public void setIns_QWaste (BigDecimal Ins_QWaste);

	/** Get Ins_QWaste	  */
	public BigDecimal getIns_QWaste();

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

    /** Column name KJS_Activity */
    public static final String COLUMNNAME_KJS_Activity = "KJS_Activity";

	/** Set QC Activity	  */
	public void setKJS_Activity (String KJS_Activity);

	/** Get QC Activity	  */
	public String getKJS_Activity();

    /** Column name KJS_ActualLength */
    public static final String COLUMNNAME_KJS_ActualLength = "KJS_ActualLength";

	/** Set Dimensi Aktual (Toleransi +5%) Panjang (mm)	  */
	public void setKJS_ActualLength (BigDecimal KJS_ActualLength);

	/** Get Dimensi Aktual (Toleransi +5%) Panjang (mm)	  */
	public BigDecimal getKJS_ActualLength();

    /** Column name KJS_ActualWidth */
    public static final String COLUMNNAME_KJS_ActualWidth = "KJS_ActualWidth";

	/** Set Dimensi Aktual (Toleransi +5%) Lebar (mm)	  */
	public void setKJS_ActualWidth (BigDecimal KJS_ActualWidth);

	/** Get Dimensi Aktual (Toleransi +5%) Lebar (mm)	  */
	public BigDecimal getKJS_ActualWidth();

    /** Column name KJS_BoxNo */
    public static final String COLUMNNAME_KJS_BoxNo = "KJS_BoxNo";

	/** Set Box No	  */
	public void setKJS_BoxNo (String KJS_BoxNo);

	/** Get Box No	  */
	public String getKJS_BoxNo();

    /** Column name KJS_Description */
    public static final String COLUMNNAME_KJS_Description = "KJS_Description";

	/** Set LainLain Description	  */
	public void setKJS_Description (String KJS_Description);

	/** Get LainLain Description	  */
	public String getKJS_Description();

    /** Column name KJS_DimentionLength */
    public static final String COLUMNNAME_KJS_DimentionLength = "KJS_DimentionLength";

	/** Set Dimensi Standar Panjang (mm)	  */
	public void setKJS_DimentionLength (BigDecimal KJS_DimentionLength);

	/** Get Dimensi Standar Panjang (mm)	  */
	public BigDecimal getKJS_DimentionLength();

    /** Column name KJS_DimentionWidth */
    public static final String COLUMNNAME_KJS_DimentionWidth = "KJS_DimentionWidth";

	/** Set Dimensi Standar Lebar (mm)	  */
	public void setKJS_DimentionWidth (BigDecimal KJS_DimentionWidth);

	/** Get Dimensi Standar Lebar (mm)	  */
	public BigDecimal getKJS_DimentionWidth();

    /** Column name KJS_Inspector */
    public static final String COLUMNNAME_KJS_Inspector = "KJS_Inspector";

	/** Set Inspector	  */
	public void setKJS_Inspector (String KJS_Inspector);

	/** Get Inspector	  */
	public String getKJS_Inspector();

    /** Column name KJS_Inspector_Bag */
    public static final String COLUMNNAME_KJS_Inspector_Bag = "KJS_Inspector_Bag";

	/** Set Inspector	  */
	public void setKJS_Inspector_Bag (String KJS_Inspector_Bag);

	/** Get Inspector	  */
	public String getKJS_Inspector_Bag();

    /** Column name KJS_Inspector_Die */
    public static final String COLUMNNAME_KJS_Inspector_Die = "KJS_Inspector_Die";

	/** Set Inspector	  */
	public void setKJS_Inspector_Die (String KJS_Inspector_Die);

	/** Get Inspector	  */
	public String getKJS_Inspector_Die();

    /** Column name KJS_Inspector_Lunch */
    public static final String COLUMNNAME_KJS_Inspector_Lunch = "KJS_Inspector_Lunch";

	/** Set Inspector	  */
	public void setKJS_Inspector_Lunch (String KJS_Inspector_Lunch);

	/** Get Inspector	  */
	public String getKJS_Inspector_Lunch();

    /** Column name KJS_Inspector_Paper */
    public static final String COLUMNNAME_KJS_Inspector_Paper = "KJS_Inspector_Paper";

	/** Set Inspector	  */
	public void setKJS_Inspector_Paper (String KJS_Inspector_Paper);

	/** Get Inspector	  */
	public String getKJS_Inspector_Paper();

    /** Column name KJS_Inspector_Ripple */
    public static final String COLUMNNAME_KJS_Inspector_Ripple = "KJS_Inspector_Ripple";

	/** Set Inspector	  */
	public void setKJS_Inspector_Ripple (String KJS_Inspector_Ripple);

	/** Get Inspector	  */
	public String getKJS_Inspector_Ripple();

    /** Column name KJS_Inspector_Stripping */
    public static final String COLUMNNAME_KJS_Inspector_Stripping = "KJS_Inspector_Stripping";

	/** Set Inspector	  */
	public void setKJS_Inspector_Stripping (String KJS_Inspector_Stripping);

	/** Get Inspector	  */
	public String getKJS_Inspector_Stripping();

    /** Column name KJS_IsAsing */
    public static final String COLUMNNAME_KJS_IsAsing = "KJS_IsAsing";

	/** Set Hasil Inspeksi Benda Asing	  */
	public void setKJS_IsAsing (boolean KJS_IsAsing);

	/** Get Hasil Inspeksi Benda Asing	  */
	public boolean isKJS_IsAsing();

    /** Column name KJS_IsBergaris */
    public static final String COLUMNNAME_KJS_IsBergaris = "KJS_IsBergaris";

	/** Set Visual Bergaris	  */
	public void setKJS_IsBergaris (boolean KJS_IsBergaris);

	/** Get Visual Bergaris	  */
	public boolean isKJS_IsBergaris();

    /** Column name KJS_IsBersihKertas */
    public static final String COLUMNNAME_KJS_IsBersihKertas = "KJS_IsBersihKertas";

	/** Set Kebersihan Kertas	  */
	public void setKJS_IsBersihKertas (boolean KJS_IsBersihKertas);

	/** Get Kebersihan Kertas	  */
	public boolean isKJS_IsBersihKertas();

    /** Column name KJS_IsBersihPrint */
    public static final String COLUMNNAME_KJS_IsBersihPrint = "KJS_IsBersihPrint";

	/** Set Kebersihan Print	  */
	public void setKJS_IsBersihPrint (boolean KJS_IsBersihPrint);

	/** Get Kebersihan Print	  */
	public boolean isKJS_IsBersihPrint();

    /** Column name KJS_IsBody */
    public static final String COLUMNNAME_KJS_IsBody = "KJS_IsBody";

	/** Set Hasil Inspeksi Body	  */
	public void setKJS_IsBody (boolean KJS_IsBody);

	/** Get Hasil Inspeksi Body	  */
	public boolean isKJS_IsBody();

    /** Column name KJS_IsCacat */
    public static final String COLUMNNAME_KJS_IsCacat = "KJS_IsCacat";

	/** Set Visual Cacat	  */
	public void setKJS_IsCacat (boolean KJS_IsCacat);

	/** Get Visual Cacat	  */
	public boolean isKJS_IsCacat();

    /** Column name KJS_IsContaBio */
    public static final String COLUMNNAME_KJS_IsContaBio = "KJS_IsContaBio";

	/** Set Kontaminasi/Benda Asing Biologi	  */
	public void setKJS_IsContaBio (boolean KJS_IsContaBio);

	/** Get Kontaminasi/Benda Asing Biologi	  */
	public boolean isKJS_IsContaBio();

    /** Column name KJS_IsContaChem */
    public static final String COLUMNNAME_KJS_IsContaChem = "KJS_IsContaChem";

	/** Set Kontaminasi/Benda Asing Kimia	  */
	public void setKJS_IsContaChem (boolean KJS_IsContaChem);

	/** Get Kontaminasi/Benda Asing Kimia	  */
	public boolean isKJS_IsContaChem();

    /** Column name KJS_IsContaPhy */
    public static final String COLUMNNAME_KJS_IsContaPhy = "KJS_IsContaPhy";

	/** Set Kontaminasi/Benda Asing Fisik	  */
	public void setKJS_IsContaPhy (boolean KJS_IsContaPhy);

	/** Get Kontaminasi/Benda Asing Fisik	  */
	public boolean isKJS_IsContaPhy();

    /** Column name KJS_IsCrossMark */
    public static final String COLUMNNAME_KJS_IsCrossMark = "KJS_IsCrossMark";

	/** Set Cross Mark	  */
	public void setKJS_IsCrossMark (boolean KJS_IsCrossMark);

	/** Get Cross Mark	  */
	public boolean isKJS_IsCrossMark();

    /** Column name KJS_IsCutting */
    public static final String COLUMNNAME_KJS_IsCutting = "KJS_IsCutting";

	/** Set Hasil Inspeksi Cutting	  */
	public void setKJS_IsCutting (boolean KJS_IsCutting);

	/** Get Hasil Inspeksi Cutting	  */
	public boolean isKJS_IsCutting();

    /** Column name KJS_IsDimensi */
    public static final String COLUMNNAME_KJS_IsDimensi = "KJS_IsDimensi";

	/** Set Hasil Inspeksi Dimensi	  */
	public void setKJS_IsDimensi (boolean KJS_IsDimensi);

	/** Get Hasil Inspeksi Dimensi	  */
	public boolean isKJS_IsDimensi();

    /** Column name KJS_IsDrippingTestBagus */
    public static final String COLUMNNAME_KJS_IsDrippingTestBagus = "KJS_IsDrippingTestBagus";

	/** Set Test Dripping Bagus	  */
	public void setKJS_IsDrippingTestBagus (boolean KJS_IsDrippingTestBagus);

	/** Get Test Dripping Bagus	  */
	public boolean isKJS_IsDrippingTestBagus();

    /** Column name KJS_IsDrippingTestTidak */
    public static final String COLUMNNAME_KJS_IsDrippingTestTidak = "KJS_IsDrippingTestTidak";

	/** Set Test Dripping Tidak	  */
	public void setKJS_IsDrippingTestTidak (boolean KJS_IsDrippingTestTidak);

	/** Get Test Dripping Tidak	  */
	public boolean isKJS_IsDrippingTestTidak();

    /** Column name KJS_IsGarisPond */
    public static final String COLUMNNAME_KJS_IsGarisPond = "KJS_IsGarisPond";

	/** Set Garis Pond	  */
	public void setKJS_IsGarisPond (boolean KJS_IsGarisPond);

	/** Get Garis Pond	  */
	public boolean isKJS_IsGarisPond();

    /** Column name KJS_IsHygiene */
    public static final String COLUMNNAME_KJS_IsHygiene = "KJS_IsHygiene";

	/** Set Hasil Inspeksi Hygiene	  */
	public void setKJS_IsHygiene (boolean KJS_IsHygiene);

	/** Get Hasil Inspeksi Hygiene	  */
	public boolean isKJS_IsHygiene();

    /** Column name KJS_IsInspectionAll */
    public static final String COLUMNNAME_KJS_IsInspectionAll = "KJS_IsInspectionAll";

	/** Set Hasil Inspection Jumlah / Isi / Labeling	  */
	public void setKJS_IsInspectionAll (boolean KJS_IsInspectionAll);

	/** Get Hasil Inspection Jumlah / Isi / Labeling	  */
	public boolean isKJS_IsInspectionAll();

    /** Column name KJS_IsKerut */
    public static final String COLUMNNAME_KJS_IsKerut = "KJS_IsKerut";

	/** Set Visual Kerut	  */
	public void setKJS_IsKerut (boolean KJS_IsKerut);

	/** Get Visual Kerut	  */
	public boolean isKJS_IsKerut();

    /** Column name KJS_IsKodeTanggal */
    public static final String COLUMNNAME_KJS_IsKodeTanggal = "KJS_IsKodeTanggal";

	/** Set Kode Tanggal	  */
	public void setKJS_IsKodeTanggal (boolean KJS_IsKodeTanggal);

	/** Get Kode Tanggal	  */
	public boolean isKJS_IsKodeTanggal();

    /** Column name KJS_IsKotor */
    public static final String COLUMNNAME_KJS_IsKotor = "KJS_IsKotor";

	/** Set Visual Kotor	  */
	public void setKJS_IsKotor (boolean KJS_IsKotor);

	/** Get Visual Kotor	  */
	public boolean isKJS_IsKotor();

    /** Column name KJS_IsLain */
    public static final String COLUMNNAME_KJS_IsLain = "KJS_IsLain";

	/** Set Lain Lain	  */
	public void setKJS_IsLain (boolean KJS_IsLain);

	/** Get Lain Lain	  */
	public boolean isKJS_IsLain();

    /** Column name KJS_IsLem */
    public static final String COLUMNNAME_KJS_IsLem = "KJS_IsLem";

	/** Set Hasil Inspeksi Pengeleman	  */
	public void setKJS_IsLem (boolean KJS_IsLem);

	/** Get Hasil Inspeksi Pengeleman	  */
	public boolean isKJS_IsLem();

    /** Column name KJS_IsLidTestBagus */
    public static final String COLUMNNAME_KJS_IsLidTestBagus = "KJS_IsLidTestBagus";

	/** Set Test Lid Bagus	  */
	public void setKJS_IsLidTestBagus (boolean KJS_IsLidTestBagus);

	/** Get Test Lid Bagus	  */
	public boolean isKJS_IsLidTestBagus();

    /** Column name KJS_IsLidTestTidak */
    public static final String COLUMNNAME_KJS_IsLidTestTidak = "KJS_IsLidTestTidak";

	/** Set Test Lid Tidak	  */
	public void setKJS_IsLidTestTidak (boolean KJS_IsLidTestTidak);

	/** Get Test Lid Tidak	  */
	public boolean isKJS_IsLidTestTidak();

    /** Column name KJS_IsLuntur */
    public static final String COLUMNNAME_KJS_IsLuntur = "KJS_IsLuntur";

	/** Set Kelunturan	  */
	public void setKJS_IsLuntur (boolean KJS_IsLuntur);

	/** Get Kelunturan	  */
	public boolean isKJS_IsLuntur();

    /** Column name KJS_IsMonitoringMesin */
    public static final String COLUMNNAME_KJS_IsMonitoringMesin = "KJS_IsMonitoringMesin";

	/** Set Monitoring Berada dimesin	  */
	public void setKJS_IsMonitoringMesin (boolean KJS_IsMonitoringMesin);

	/** Get Monitoring Berada dimesin	  */
	public boolean isKJS_IsMonitoringMesin();

    /** Column name KJS_IsMonitoringTidakSesuai */
    public static final String COLUMNNAME_KJS_IsMonitoringTidakSesuai = "KJS_IsMonitoringTidakSesuai";

	/** Set Monitoring Adanya Ketidaksesuaian	  */
	public void setKJS_IsMonitoringTidakSesuai (boolean KJS_IsMonitoringTidakSesuai);

	/** Get Monitoring Adanya Ketidaksesuaian	  */
	public boolean isKJS_IsMonitoringTidakSesuai();

    /** Column name KJS_IsPacking */
    public static final String COLUMNNAME_KJS_IsPacking = "KJS_IsPacking";

	/** Set Hasil Inspeksi Packing / Inspeksi Akhir	  */
	public void setKJS_IsPacking (boolean KJS_IsPacking);

	/** Get Hasil Inspeksi Packing / Inspeksi Akhir	  */
	public boolean isKJS_IsPacking();

    /** Column name KJS_IsPotongan */
    public static final String COLUMNNAME_KJS_IsPotongan = "KJS_IsPotongan";

	/** Set Hasil Inspeksi Lipatan & Potongan Atas	  */
	public void setKJS_IsPotongan (boolean KJS_IsPotongan);

	/** Get Hasil Inspeksi Lipatan & Potongan Atas	  */
	public boolean isKJS_IsPotongan();

    /** Column name KJS_IsPrinting */
    public static final String COLUMNNAME_KJS_IsPrinting = "KJS_IsPrinting";

	/** Set Hasil Inspeksi Printing	  */
	public void setKJS_IsPrinting (boolean KJS_IsPrinting);

	/** Get Hasil Inspeksi Printing	  */
	public boolean isKJS_IsPrinting();

    /** Column name KJS_IsRata */
    public static final String COLUMNNAME_KJS_IsRata = "KJS_IsRata";

	/** Set Kerataan Print	  */
	public void setKJS_IsRata (boolean KJS_IsRata);

	/** Get Kerataan Print	  */
	public boolean isKJS_IsRata();

    /** Column name KJS_IsRegistrasi */
    public static final String COLUMNNAME_KJS_IsRegistrasi = "KJS_IsRegistrasi";

	/** Set Registrasi	  */
	public void setKJS_IsRegistrasi (boolean KJS_IsRegistrasi);

	/** Get Registrasi	  */
	public boolean isKJS_IsRegistrasi();

    /** Column name KJS_IsRim */
    public static final String COLUMNNAME_KJS_IsRim = "KJS_IsRim";

	/** Set Hasil Inspeksi Rim	  */
	public void setKJS_IsRim (boolean KJS_IsRim);

	/** Get Hasil Inspeksi Rim	  */
	public boolean isKJS_IsRim();

    /** Column name KJS_IsRoll */
    public static final String COLUMNNAME_KJS_IsRoll = "KJS_IsRoll";

	/** Set Hasil Inspeksi Roll	  */
	public void setKJS_IsRoll (boolean KJS_IsRoll);

	/** Get Hasil Inspeksi Roll	  */
	public boolean isKJS_IsRoll();

    /** Column name KJS_IsSealing */
    public static final String COLUMNNAME_KJS_IsSealing = "KJS_IsSealing";

	/** Set Hasil Inspeksi Sealing	  */
	public void setKJS_IsSealing (boolean KJS_IsSealing);

	/** Get Hasil Inspeksi Sealing	  */
	public boolean isKJS_IsSealing();

    /** Column name KJS_IsSerat */
    public static final String COLUMNNAME_KJS_IsSerat = "KJS_IsSerat";

	/** Set Visual Serat Kertas	  */
	public void setKJS_IsSerat (boolean KJS_IsSerat);

	/** Get Visual Serat Kertas	  */
	public boolean isKJS_IsSerat();

    /** Column name KJS_IsSesuai */
    public static final String COLUMNNAME_KJS_IsSesuai = "KJS_IsSesuai";

	/** Set Kesesuaian Desain	  */
	public void setKJS_IsSesuai (boolean KJS_IsSesuai);

	/** Get Kesesuaian Desain	  */
	public boolean isKJS_IsSesuai();

    /** Column name KJS_IsSolid */
    public static final String COLUMNNAME_KJS_IsSolid = "KJS_IsSolid";

	/** Set Kesolidan	  */
	public void setKJS_IsSolid (boolean KJS_IsSolid);

	/** Get Kesolidan	  */
	public boolean isKJS_IsSolid();

    /** Column name KJS_IsTajam */
    public static final String COLUMNNAME_KJS_IsTajam = "KJS_IsTajam";

	/** Set Ketajaman	  */
	public void setKJS_IsTajam (boolean KJS_IsTajam);

	/** Get Ketajaman	  */
	public boolean isKJS_IsTajam();

    /** Column name KJS_IsToleransi */
    public static final String COLUMNNAME_KJS_IsToleransi = "KJS_IsToleransi";

	/** Set Hasil Inspeksi Toleransi Register Garis Potong 0.10mm	  */
	public void setKJS_IsToleransi (boolean KJS_IsToleransi);

	/** Get Hasil Inspeksi Toleransi Register Garis Potong 0.10mm	  */
	public boolean isKJS_IsToleransi();

    /** Column name KJS_IsUkuran */
    public static final String COLUMNNAME_KJS_IsUkuran = "KJS_IsUkuran";

	/** Set Hasil Inspeksi Ukuran Jadi	  */
	public void setKJS_IsUkuran (boolean KJS_IsUkuran);

	/** Get Hasil Inspeksi Ukuran Jadi	  */
	public boolean isKJS_IsUkuran();

    /** Column name KJS_IsWarna */
    public static final String COLUMNNAME_KJS_IsWarna = "KJS_IsWarna";

	/** Set Kesesuaian Warna	  */
	public void setKJS_IsWarna (boolean KJS_IsWarna);

	/** Get Kesesuaian Warna	  */
	public boolean isKJS_IsWarna();

    /** Column name KJS_JenisKertas */
    public static final String COLUMNNAME_KJS_JenisKertas = "KJS_JenisKertas";

	/** Set Jenis Kertas	  */
	public void setKJS_JenisKertas (String KJS_JenisKertas);

	/** Get Jenis Kertas	  */
	public String getKJS_JenisKertas();

    /** Column name KJS_LeakTestBagus */
    public static final String COLUMNNAME_KJS_LeakTestBagus = "KJS_LeakTestBagus";

	/** Set Hasil Leak Test Bagus	  */
	public void setKJS_LeakTestBagus (BigDecimal KJS_LeakTestBagus);

	/** Get Hasil Leak Test Bagus	  */
	public BigDecimal getKJS_LeakTestBagus();

    /** Column name KJS_LeakTestBocor */
    public static final String COLUMNNAME_KJS_LeakTestBocor = "KJS_LeakTestBocor";

	/** Set Hasil Leak Test Bocor	  */
	public void setKJS_LeakTestBocor (BigDecimal KJS_LeakTestBocor);

	/** Get Hasil Leak Test Bocor	  */
	public BigDecimal getKJS_LeakTestBocor();

    /** Column name KJS_Manager */
    public static final String COLUMNNAME_KJS_Manager = "KJS_Manager";

	/** Set Quality Manager	  */
	public void setKJS_Manager (String KJS_Manager);

	/** Get Quality Manager	  */
	public String getKJS_Manager();

    /** Column name KJS_Material */
    public static final String COLUMNNAME_KJS_Material = "KJS_Material";

	/** Set Material	  */
	public void setKJS_Material (String KJS_Material);

	/** Get Material	  */
	public String getKJS_Material();

    /** Column name KJS_QC_ID */
    public static final String COLUMNNAME_KJS_QC_ID = "KJS_QC_ID";

	/** Set QC	  */
	public void setKJS_QC_ID (int KJS_QC_ID);

	/** Get QC	  */
	public int getKJS_QC_ID();

    /** Column name KJS_QC_UU */
    public static final String COLUMNNAME_KJS_QC_UU = "KJS_QC_UU";

	/** Set KJS_QC_UU	  */
	public void setKJS_QC_UU (String KJS_QC_UU);

	/** Get KJS_QC_UU	  */
	public String getKJS_QC_UU();

    /** Column name KJS_RelFrom */
    public static final String COLUMNNAME_KJS_RelFrom = "KJS_RelFrom";

	/** Set Kedalaman Rel From	  */
	public void setKJS_RelFrom (BigDecimal KJS_RelFrom);

	/** Get Kedalaman Rel From	  */
	public BigDecimal getKJS_RelFrom();

    /** Column name KJS_RelTo */
    public static final String COLUMNNAME_KJS_RelTo = "KJS_RelTo";

	/** Set Kedalaman Rel To	  */
	public void setKJS_RelTo (BigDecimal KJS_RelTo);

	/** Get Kedalaman Rel To	  */
	public BigDecimal getKJS_RelTo();

    /** Column name KJS_SamplingQty */
    public static final String COLUMNNAME_KJS_SamplingQty = "KJS_SamplingQty";

	/** Set Jumlah Sampling	  */
	public void setKJS_SamplingQty (BigDecimal KJS_SamplingQty);

	/** Get Jumlah Sampling	  */
	public BigDecimal getKJS_SamplingQty();

    /** Column name KJS_Size */
    public static final String COLUMNNAME_KJS_Size = "KJS_Size";

	/** Set Size	  */
	public void setKJS_Size (String KJS_Size);

	/** Get Size	  */
	public String getKJS_Size();

    /** Column name KJS_SobekTestBagus */
    public static final String COLUMNNAME_KJS_SobekTestBagus = "KJS_SobekTestBagus";

	/** Set Test Sobek Bagus	  */
	public void setKJS_SobekTestBagus (BigDecimal KJS_SobekTestBagus);

	/** Get Test Sobek Bagus	  */
	public BigDecimal getKJS_SobekTestBagus();

    /** Column name KJS_SobekTestTidak */
    public static final String COLUMNNAME_KJS_SobekTestTidak = "KJS_SobekTestTidak";

	/** Set Test Sobek Tidak	  */
	public void setKJS_SobekTestTidak (BigDecimal KJS_SobekTestTidak);

	/** Get Test Sobek Tidak	  */
	public BigDecimal getKJS_SobekTestTidak();

    /** Column name KJS_SpecBotDepth */
    public static final String COLUMNNAME_KJS_SpecBotDepth = "KJS_SpecBotDepth";

	/** Set Specification Bottom Depth	  */
	public void setKJS_SpecBotDepth (BigDecimal KJS_SpecBotDepth);

	/** Get Specification Bottom Depth	  */
	public BigDecimal getKJS_SpecBotDepth();

    /** Column name KJS_SpecBotDepth_Max */
    public static final String COLUMNNAME_KJS_SpecBotDepth_Max = "KJS_SpecBotDepth_Max";

	/** Set Specification Bottom Depth	  */
	public void setKJS_SpecBotDepth_Max (BigDecimal KJS_SpecBotDepth_Max);

	/** Get Specification Bottom Depth	  */
	public BigDecimal getKJS_SpecBotDepth_Max();

    /** Column name KJS_SpecBotDiam */
    public static final String COLUMNNAME_KJS_SpecBotDiam = "KJS_SpecBotDiam";

	/** Set Specification Bottom Diameter	  */
	public void setKJS_SpecBotDiam (BigDecimal KJS_SpecBotDiam);

	/** Get Specification Bottom Diameter	  */
	public BigDecimal getKJS_SpecBotDiam();

    /** Column name KJS_SpecBotDiam_Max */
    public static final String COLUMNNAME_KJS_SpecBotDiam_Max = "KJS_SpecBotDiam_Max";

	/** Set Specification Bottom Diameter	  */
	public void setKJS_SpecBotDiam_Max (BigDecimal KJS_SpecBotDiam_Max);

	/** Get Specification Bottom Diameter	  */
	public BigDecimal getKJS_SpecBotDiam_Max();

    /** Column name KJS_SpecHeight */
    public static final String COLUMNNAME_KJS_SpecHeight = "KJS_SpecHeight";

	/** Set Specification Height	  */
	public void setKJS_SpecHeight (BigDecimal KJS_SpecHeight);

	/** Get Specification Height	  */
	public BigDecimal getKJS_SpecHeight();

    /** Column name KJS_SpecHeight_Max */
    public static final String COLUMNNAME_KJS_SpecHeight_Max = "KJS_SpecHeight_Max";

	/** Set Specification Height	  */
	public void setKJS_SpecHeight_Max (BigDecimal KJS_SpecHeight_Max);

	/** Get Specification Height	  */
	public BigDecimal getKJS_SpecHeight_Max();

    /** Column name KJS_SpecRimHeight */
    public static final String COLUMNNAME_KJS_SpecRimHeight = "KJS_SpecRimHeight";

	/** Set Specification Rim Height	  */
	public void setKJS_SpecRimHeight (BigDecimal KJS_SpecRimHeight);

	/** Get Specification Rim Height	  */
	public BigDecimal getKJS_SpecRimHeight();

    /** Column name KJS_SpecRimHeight_Max */
    public static final String COLUMNNAME_KJS_SpecRimHeight_Max = "KJS_SpecRimHeight_Max";

	/** Set Specification Rim Height	  */
	public void setKJS_SpecRimHeight_Max (BigDecimal KJS_SpecRimHeight_Max);

	/** Get Specification Rim Height	  */
	public BigDecimal getKJS_SpecRimHeight_Max();

    /** Column name KJS_SpecTopDiam */
    public static final String COLUMNNAME_KJS_SpecTopDiam = "KJS_SpecTopDiam";

	/** Set Specification Top Diameter	  */
	public void setKJS_SpecTopDiam (BigDecimal KJS_SpecTopDiam);

	/** Get Specification Top Diameter	  */
	public BigDecimal getKJS_SpecTopDiam();

    /** Column name KJS_SpecTopDiam_Max */
    public static final String COLUMNNAME_KJS_SpecTopDiam_Max = "KJS_SpecTopDiam_Max";

	/** Set Specification Top Diameter	  */
	public void setKJS_SpecTopDiam_Max (BigDecimal KJS_SpecTopDiam_Max);

	/** Get Specification Top Diameter	  */
	public BigDecimal getKJS_SpecTopDiam_Max();

    /** Column name KJS_SpecWeight */
    public static final String COLUMNNAME_KJS_SpecWeight = "KJS_SpecWeight";

	/** Set Specification Cup Weight	  */
	public void setKJS_SpecWeight (BigDecimal KJS_SpecWeight);

	/** Get Specification Cup Weight	  */
	public BigDecimal getKJS_SpecWeight();

    /** Column name KJS_SpecWeight_Max */
    public static final String COLUMNNAME_KJS_SpecWeight_Max = "KJS_SpecWeight_Max";

	/** Set Specification Cup Weight	  */
	public void setKJS_SpecWeight_Max (BigDecimal KJS_SpecWeight_Max);

	/** Get Specification Cup Weight	  */
	public BigDecimal getKJS_SpecWeight_Max();

    /** Column name KJS_Status */
    public static final String COLUMNNAME_KJS_Status = "KJS_Status";

	/** Set QC Status	  */
	public void setKJS_Status (String KJS_Status);

	/** Get QC Status	  */
	public String getKJS_Status();

    /** Column name KJS_Time */
    public static final String COLUMNNAME_KJS_Time = "KJS_Time";

	/** Set Time	  */
	public void setKJS_Time (Timestamp KJS_Time);

	/** Get Time	  */
	public Timestamp getKJS_Time();

    /** Column name LB_QHold */
    public static final String COLUMNNAME_LB_QHold = "LB_QHold";

	/** Set LB_QHold	  */
	public void setLB_QHold (BigDecimal LB_QHold);

	/** Get LB_QHold	  */
	public BigDecimal getLB_QHold();

    /** Column name LB_QPass */
    public static final String COLUMNNAME_LB_QPass = "LB_QPass";

	/** Set LB_QPass	  */
	public void setLB_QPass (BigDecimal LB_QPass);

	/** Get LB_QPass	  */
	public BigDecimal getLB_QPass();

    /** Column name LB_QPrd */
    public static final String COLUMNNAME_LB_QPrd = "LB_QPrd";

	/** Set LB_QPrd	  */
	public void setLB_QPrd (BigDecimal LB_QPrd);

	/** Get LB_QPrd	  */
	public BigDecimal getLB_QPrd();

    /** Column name LB_QRej */
    public static final String COLUMNNAME_LB_QRej = "LB_QRej";

	/** Set LB_QRej	  */
	public void setLB_QRej (BigDecimal LB_QRej);

	/** Get LB_QRej	  */
	public BigDecimal getLB_QRej();

    /** Column name LB_QWaste */
    public static final String COLUMNNAME_LB_QWaste = "LB_QWaste";

	/** Set LB_QWaste	  */
	public void setLB_QWaste (BigDecimal LB_QWaste);

	/** Get LB_QWaste	  */
	public BigDecimal getLB_QWaste();

    /** Column name Leak_QHold */
    public static final String COLUMNNAME_Leak_QHold = "Leak_QHold";

	/** Set Leak_QHold	  */
	public void setLeak_QHold (BigDecimal Leak_QHold);

	/** Get Leak_QHold	  */
	public BigDecimal getLeak_QHold();

    /** Column name Leak_QPass */
    public static final String COLUMNNAME_Leak_QPass = "Leak_QPass";

	/** Set Leak_QPass	  */
	public void setLeak_QPass (BigDecimal Leak_QPass);

	/** Get Leak_QPass	  */
	public BigDecimal getLeak_QPass();

    /** Column name Leak_QPrd */
    public static final String COLUMNNAME_Leak_QPrd = "Leak_QPrd";

	/** Set Leak_QPrd	  */
	public void setLeak_QPrd (BigDecimal Leak_QPrd);

	/** Get Leak_QPrd	  */
	public BigDecimal getLeak_QPrd();

    /** Column name Leak_QRej */
    public static final String COLUMNNAME_Leak_QRej = "Leak_QRej";

	/** Set Leak_QRej	  */
	public void setLeak_QRej (BigDecimal Leak_QRej);

	/** Get Leak_QRej	  */
	public BigDecimal getLeak_QRej();

    /** Column name Leak_QWaste */
    public static final String COLUMNNAME_Leak_QWaste = "Leak_QWaste";

	/** Set Leak_QWaste	  */
	public void setLeak_QWaste (BigDecimal Leak_QWaste);

	/** Get Leak_QWaste	  */
	public BigDecimal getLeak_QWaste();

    /** Column name M_Production_ID */
    public static final String COLUMNNAME_M_Production_ID = "M_Production_ID";

	/** Set Production.
	  * Plan for producing a product
	  */
	public void setM_Production_ID (int M_Production_ID);

	/** Get Production.
	  * Plan for producing a product
	  */
	public int getM_Production_ID();

	public org.compiere.model.I_M_Production getM_Production() throws RuntimeException;

    /** Column name Paper_QHold */
    public static final String COLUMNNAME_Paper_QHold = "Paper_QHold";

	/** Set Paper_QHold	  */
	public void setPaper_QHold (BigDecimal Paper_QHold);

	/** Get Paper_QHold	  */
	public BigDecimal getPaper_QHold();

    /** Column name Paper_QPass */
    public static final String COLUMNNAME_Paper_QPass = "Paper_QPass";

	/** Set Paper_QPass	  */
	public void setPaper_QPass (BigDecimal Paper_QPass);

	/** Get Paper_QPass	  */
	public BigDecimal getPaper_QPass();

    /** Column name Paper_QPrd */
    public static final String COLUMNNAME_Paper_QPrd = "Paper_QPrd";

	/** Set Paper_QPrd	  */
	public void setPaper_QPrd (BigDecimal Paper_QPrd);

	/** Get Paper_QPrd	  */
	public BigDecimal getPaper_QPrd();

    /** Column name Paper_QRej */
    public static final String COLUMNNAME_Paper_QRej = "Paper_QRej";

	/** Set Paper_QRej	  */
	public void setPaper_QRej (BigDecimal Paper_QRej);

	/** Get Paper_QRej	  */
	public BigDecimal getPaper_QRej();

    /** Column name Paper_QWaste */
    public static final String COLUMNNAME_Paper_QWaste = "Paper_QWaste";

	/** Set Paper_QWaste	  */
	public void setPaper_QWaste (BigDecimal Paper_QWaste);

	/** Get Paper_QWaste	  */
	public BigDecimal getPaper_QWaste();

    /** Column name Rip_QHold */
    public static final String COLUMNNAME_Rip_QHold = "Rip_QHold";

	/** Set Rip_QHold	  */
	public void setRip_QHold (BigDecimal Rip_QHold);

	/** Get Rip_QHold	  */
	public BigDecimal getRip_QHold();

    /** Column name Rip_QPass */
    public static final String COLUMNNAME_Rip_QPass = "Rip_QPass";

	/** Set Rip_QPass	  */
	public void setRip_QPass (BigDecimal Rip_QPass);

	/** Get Rip_QPass	  */
	public BigDecimal getRip_QPass();

    /** Column name Rip_QPrd */
    public static final String COLUMNNAME_Rip_QPrd = "Rip_QPrd";

	/** Set Rip_QPrd	  */
	public void setRip_QPrd (BigDecimal Rip_QPrd);

	/** Get Rip_QPrd	  */
	public BigDecimal getRip_QPrd();

    /** Column name Rip_QRej */
    public static final String COLUMNNAME_Rip_QRej = "Rip_QRej";

	/** Set Rip_QRej	  */
	public void setRip_QRej (BigDecimal Rip_QRej);

	/** Get Rip_QRej	  */
	public BigDecimal getRip_QRej();

    /** Column name Rip_QWaste */
    public static final String COLUMNNAME_Rip_QWaste = "Rip_QWaste";

	/** Set Rip_QWaste	  */
	public void setRip_QWaste (BigDecimal Rip_QWaste);

	/** Get Rip_QWaste	  */
	public BigDecimal getRip_QWaste();

    /** Column name Sheet_QHold */
    public static final String COLUMNNAME_Sheet_QHold = "Sheet_QHold";

	/** Set Sheet_QHold	  */
	public void setSheet_QHold (BigDecimal Sheet_QHold);

	/** Get Sheet_QHold	  */
	public BigDecimal getSheet_QHold();

    /** Column name Sheet_QPass */
    public static final String COLUMNNAME_Sheet_QPass = "Sheet_QPass";

	/** Set Sheet_QPass	  */
	public void setSheet_QPass (BigDecimal Sheet_QPass);

	/** Get Sheet_QPass	  */
	public BigDecimal getSheet_QPass();

    /** Column name Sheet_QPrd */
    public static final String COLUMNNAME_Sheet_QPrd = "Sheet_QPrd";

	/** Set Sheet_QPrd	  */
	public void setSheet_QPrd (BigDecimal Sheet_QPrd);

	/** Get Sheet_QPrd	  */
	public BigDecimal getSheet_QPrd();

    /** Column name Sheet_QRej */
    public static final String COLUMNNAME_Sheet_QRej = "Sheet_QRej";

	/** Set Sheet_QRej	  */
	public void setSheet_QRej (BigDecimal Sheet_QRej);

	/** Get Sheet_QRej	  */
	public BigDecimal getSheet_QRej();

    /** Column name Sheet_QWaste */
    public static final String COLUMNNAME_Sheet_QWaste = "Sheet_QWaste";

	/** Set Sheet_QWaste	  */
	public void setSheet_QWaste (BigDecimal Sheet_QWaste);

	/** Get Sheet_QWaste	  */
	public BigDecimal getSheet_QWaste();

    /** Column name Slit_QHold */
    public static final String COLUMNNAME_Slit_QHold = "Slit_QHold";

	/** Set Slit_QHold	  */
	public void setSlit_QHold (BigDecimal Slit_QHold);

	/** Get Slit_QHold	  */
	public BigDecimal getSlit_QHold();

    /** Column name Slit_QPass */
    public static final String COLUMNNAME_Slit_QPass = "Slit_QPass";

	/** Set Slit_QPass	  */
	public void setSlit_QPass (BigDecimal Slit_QPass);

	/** Get Slit_QPass	  */
	public BigDecimal getSlit_QPass();

    /** Column name Slit_QPrd */
    public static final String COLUMNNAME_Slit_QPrd = "Slit_QPrd";

	/** Set Slit_QPrd	  */
	public void setSlit_QPrd (BigDecimal Slit_QPrd);

	/** Get Slit_QPrd	  */
	public BigDecimal getSlit_QPrd();

    /** Column name Slit_QRej */
    public static final String COLUMNNAME_Slit_QRej = "Slit_QRej";

	/** Set Slit_QRej	  */
	public void setSlit_QRej (BigDecimal Slit_QRej);

	/** Get Slit_QRej	  */
	public BigDecimal getSlit_QRej();

    /** Column name Slit_QWaste */
    public static final String COLUMNNAME_Slit_QWaste = "Slit_QWaste";

	/** Set Slit_QWaste	  */
	public void setSlit_QWaste (BigDecimal Slit_QWaste);

	/** Get Slit_QWaste	  */
	public BigDecimal getSlit_QWaste();

    /** Column name Strip_QHold */
    public static final String COLUMNNAME_Strip_QHold = "Strip_QHold";

	/** Set Strip_QHold	  */
	public void setStrip_QHold (BigDecimal Strip_QHold);

	/** Get Strip_QHold	  */
	public BigDecimal getStrip_QHold();

    /** Column name Strip_QPass */
    public static final String COLUMNNAME_Strip_QPass = "Strip_QPass";

	/** Set Strip_QPass	  */
	public void setStrip_QPass (BigDecimal Strip_QPass);

	/** Get Strip_QPass	  */
	public BigDecimal getStrip_QPass();

    /** Column name Strip_QPrd */
    public static final String COLUMNNAME_Strip_QPrd = "Strip_QPrd";

	/** Set Strip_QPrd	  */
	public void setStrip_QPrd (BigDecimal Strip_QPrd);

	/** Get Strip_QPrd	  */
	public BigDecimal getStrip_QPrd();

    /** Column name Strip_QRej */
    public static final String COLUMNNAME_Strip_QRej = "Strip_QRej";

	/** Set Strip_QRej	  */
	public void setStrip_QRej (BigDecimal Strip_QRej);

	/** Get Strip_QRej	  */
	public BigDecimal getStrip_QRej();

    /** Column name Strip_QWaste */
    public static final String COLUMNNAME_Strip_QWaste = "Strip_QWaste";

	/** Set Strip_QWaste	  */
	public void setStrip_QWaste (BigDecimal Strip_QWaste);

	/** Get Strip_QWaste	  */
	public BigDecimal getStrip_QWaste();

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
