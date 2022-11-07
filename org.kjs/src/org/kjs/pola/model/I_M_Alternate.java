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

/** Generated Interface for M_Alternate
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_M_Alternate 
{

    /** TableName=M_Alternate */
    public static final String Table_Name = "M_Alternate";

    /** AD_Table_ID=1000039 */
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

    /** Column name KJS_KebutuhanGearFeed */
    public static final String COLUMNNAME_KJS_KebutuhanGearFeed = "KJS_KebutuhanGearFeed";

	/** Set Kebutuhan RM Gear & Feed Up	  */
	public void setKJS_KebutuhanGearFeed (BigDecimal KJS_KebutuhanGearFeed);

	/** Get Kebutuhan RM Gear & Feed Up	  */
	public BigDecimal getKJS_KebutuhanGearFeed();

    /** Column name KJS_KebutuhanRollKertas */
    public static final String COLUMNNAME_KJS_KebutuhanRollKertas = "KJS_KebutuhanRollKertas";

	/** Set Kebutuhan RM Lebar Roll & Panjang Kertas	  */
	public void setKJS_KebutuhanRollKertas (BigDecimal KJS_KebutuhanRollKertas);

	/** Get Kebutuhan RM Lebar Roll & Panjang Kertas	  */
	public BigDecimal getKJS_KebutuhanRollKertas();

    /** Column name KJS_KebutuhanUkuran */
    public static final String COLUMNNAME_KJS_KebutuhanUkuran = "KJS_KebutuhanUkuran";

	/** Set Kebutuhan RM Ukuran	  */
	public void setKJS_KebutuhanUkuran (BigDecimal KJS_KebutuhanUkuran);

	/** Get Kebutuhan RM Ukuran	  */
	public BigDecimal getKJS_KebutuhanUkuran();

    /** Column name M_Alternate_ID */
    public static final String COLUMNNAME_M_Alternate_ID = "M_Alternate_ID";

	/** Set Alternate	  */
	public void setM_Alternate_ID (int M_Alternate_ID);

	/** Get Alternate	  */
	public int getM_Alternate_ID();

    /** Column name M_Alternate_UU */
    public static final String COLUMNNAME_M_Alternate_UU = "M_Alternate_UU";

	/** Set M_Alternate_UU	  */
	public void setM_Alternate_UU (String M_Alternate_UU);

	/** Get M_Alternate_UU	  */
	public String getM_Alternate_UU();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}
