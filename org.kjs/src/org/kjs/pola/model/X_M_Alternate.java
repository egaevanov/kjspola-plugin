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
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for M_Alternate
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_M_Alternate extends PO implements I_M_Alternate, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20220419L;

    /** Standard Constructor */
    public X_M_Alternate (Properties ctx, int M_Alternate_ID, String trxName)
    {
      super (ctx, M_Alternate_ID, trxName);
      /** if (M_Alternate_ID == 0)
        {
			setKJS_CavityGearFeed (Env.ZERO);
			setKJS_CavityJumlah (Env.ZERO);
			setKJS_CavityRollKertas (Env.ZERO);
			setKJS_CavityUkuran (Env.ZERO);
			setKJS_KebutuhanGearFeed (Env.ZERO);
			setKJS_KebutuhanRollKertas (Env.ZERO);
			setKJS_KebutuhanUkuran (Env.ZERO);
			setM_Alternate_ID (0);
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_M_Alternate (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_M_Alternate[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
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

	/** Set Kebutuhan RM Gear & Feed Up.
		@param KJS_KebutuhanGearFeed Kebutuhan RM Gear & Feed Up	  */
	public void setKJS_KebutuhanGearFeed (BigDecimal KJS_KebutuhanGearFeed)
	{
		set_Value (COLUMNNAME_KJS_KebutuhanGearFeed, KJS_KebutuhanGearFeed);
	}

	/** Get Kebutuhan RM Gear & Feed Up.
		@return Kebutuhan RM Gear & Feed Up	  */
	public BigDecimal getKJS_KebutuhanGearFeed () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_KebutuhanGearFeed);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Kebutuhan RM Lebar Roll & Panjang Kertas.
		@param KJS_KebutuhanRollKertas Kebutuhan RM Lebar Roll & Panjang Kertas	  */
	public void setKJS_KebutuhanRollKertas (BigDecimal KJS_KebutuhanRollKertas)
	{
		set_Value (COLUMNNAME_KJS_KebutuhanRollKertas, KJS_KebutuhanRollKertas);
	}

	/** Get Kebutuhan RM Lebar Roll & Panjang Kertas.
		@return Kebutuhan RM Lebar Roll & Panjang Kertas	  */
	public BigDecimal getKJS_KebutuhanRollKertas () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_KebutuhanRollKertas);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Kebutuhan RM Ukuran.
		@param KJS_KebutuhanUkuran Kebutuhan RM Ukuran	  */
	public void setKJS_KebutuhanUkuran (BigDecimal KJS_KebutuhanUkuran)
	{
		set_Value (COLUMNNAME_KJS_KebutuhanUkuran, KJS_KebutuhanUkuran);
	}

	/** Get Kebutuhan RM Ukuran.
		@return Kebutuhan RM Ukuran	  */
	public BigDecimal getKJS_KebutuhanUkuran () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_KebutuhanUkuran);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Alternate.
		@param M_Alternate_ID Alternate	  */
	public void setM_Alternate_ID (int M_Alternate_ID)
	{
		if (M_Alternate_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Alternate_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Alternate_ID, Integer.valueOf(M_Alternate_ID));
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

	/** Set M_Alternate_UU.
		@param M_Alternate_UU M_Alternate_UU	  */
	public void setM_Alternate_UU (String M_Alternate_UU)
	{
		set_ValueNoCheck (COLUMNNAME_M_Alternate_UU, M_Alternate_UU);
	}

	/** Get M_Alternate_UU.
		@return M_Alternate_UU	  */
	public String getM_Alternate_UU () 
	{
		return (String)get_Value(COLUMNNAME_M_Alternate_UU);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}