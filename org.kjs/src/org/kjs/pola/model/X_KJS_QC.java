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

/** Generated Model for KJS_QC
 *  @author iDempiere (generated) 
 *  @version Release 6.2 - $Id$ */
public class X_KJS_QC extends PO implements I_KJS_QC, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20220419L;

    /** Standard Constructor */
    public X_KJS_QC (Properties ctx, int KJS_QC_ID, String trxName)
    {
      super (ctx, KJS_QC_ID, trxName);
      /** if (KJS_QC_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_KJS_QC (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_KJS_QC[")
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

	/** Set Die_QHold.
		@param Die_QHold Die_QHold	  */
	public void setDie_QHold (BigDecimal Die_QHold)
	{
		set_Value (COLUMNNAME_Die_QHold, Die_QHold);
	}

	/** Get Die_QHold.
		@return Die_QHold	  */
	public BigDecimal getDie_QHold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Die_QHold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Die_QPass.
		@param Die_QPass Die_QPass	  */
	public void setDie_QPass (BigDecimal Die_QPass)
	{
		set_Value (COLUMNNAME_Die_QPass, Die_QPass);
	}

	/** Get Die_QPass.
		@return Die_QPass	  */
	public BigDecimal getDie_QPass () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Die_QPass);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Die_QPrd.
		@param Die_QPrd Die_QPrd	  */
	public void setDie_QPrd (BigDecimal Die_QPrd)
	{
		set_Value (COLUMNNAME_Die_QPrd, Die_QPrd);
	}

	/** Get Die_QPrd.
		@return Die_QPrd	  */
	public BigDecimal getDie_QPrd () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Die_QPrd);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Die_QRej.
		@param Die_QRej Die_QRej	  */
	public void setDie_QRej (BigDecimal Die_QRej)
	{
		set_Value (COLUMNNAME_Die_QRej, Die_QRej);
	}

	/** Get Die_QRej.
		@return Die_QRej	  */
	public BigDecimal getDie_QRej () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Die_QRej);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Die_QWaste.
		@param Die_QWaste Die_QWaste	  */
	public void setDie_QWaste (BigDecimal Die_QWaste)
	{
		set_Value (COLUMNNAME_Die_QWaste, Die_QWaste);
	}

	/** Get Die_QWaste.
		@return Die_QWaste	  */
	public BigDecimal getDie_QWaste () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Die_QWaste);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Dril_QHold.
		@param Dril_QHold Dril_QHold	  */
	public void setDril_QHold (BigDecimal Dril_QHold)
	{
		set_Value (COLUMNNAME_Dril_QHold, Dril_QHold);
	}

	/** Get Dril_QHold.
		@return Dril_QHold	  */
	public BigDecimal getDril_QHold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Dril_QHold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Dril_QPass.
		@param Dril_QPass Dril_QPass	  */
	public void setDril_QPass (BigDecimal Dril_QPass)
	{
		set_Value (COLUMNNAME_Dril_QPass, Dril_QPass);
	}

	/** Get Dril_QPass.
		@return Dril_QPass	  */
	public BigDecimal getDril_QPass () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Dril_QPass);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Dril_QPrd.
		@param Dril_QPrd Dril_QPrd	  */
	public void setDril_QPrd (BigDecimal Dril_QPrd)
	{
		set_Value (COLUMNNAME_Dril_QPrd, Dril_QPrd);
	}

	/** Get Dril_QPrd.
		@return Dril_QPrd	  */
	public BigDecimal getDril_QPrd () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Dril_QPrd);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Dril_QRej.
		@param Dril_QRej Dril_QRej	  */
	public void setDril_QRej (BigDecimal Dril_QRej)
	{
		set_Value (COLUMNNAME_Dril_QRej, Dril_QRej);
	}

	/** Get Dril_QRej.
		@return Dril_QRej	  */
	public BigDecimal getDril_QRej () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Dril_QRej);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Dril_QWaste.
		@param Dril_QWaste Dril_QWaste	  */
	public void setDril_QWaste (BigDecimal Dril_QWaste)
	{
		set_Value (COLUMNNAME_Dril_QWaste, Dril_QWaste);
	}

	/** Get Dril_QWaste.
		@return Dril_QWaste	  */
	public BigDecimal getDril_QWaste () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Dril_QWaste);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Flex_QHold.
		@param Flex_QHold Flex_QHold	  */
	public void setFlex_QHold (BigDecimal Flex_QHold)
	{
		set_Value (COLUMNNAME_Flex_QHold, Flex_QHold);
	}

	/** Get Flex_QHold.
		@return Flex_QHold	  */
	public BigDecimal getFlex_QHold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Flex_QHold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Flex_QPass.
		@param Flex_QPass Flex_QPass	  */
	public void setFlex_QPass (BigDecimal Flex_QPass)
	{
		set_Value (COLUMNNAME_Flex_QPass, Flex_QPass);
	}

	/** Get Flex_QPass.
		@return Flex_QPass	  */
	public BigDecimal getFlex_QPass () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Flex_QPass);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Flex_QPrd.
		@param Flex_QPrd Flex_QPrd	  */
	public void setFlex_QPrd (BigDecimal Flex_QPrd)
	{
		set_Value (COLUMNNAME_Flex_QPrd, Flex_QPrd);
	}

	/** Get Flex_QPrd.
		@return Flex_QPrd	  */
	public BigDecimal getFlex_QPrd () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Flex_QPrd);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Flex_QRej.
		@param Flex_QRej Flex_QRej	  */
	public void setFlex_QRej (BigDecimal Flex_QRej)
	{
		set_Value (COLUMNNAME_Flex_QRej, Flex_QRej);
	}

	/** Get Flex_QRej.
		@return Flex_QRej	  */
	public BigDecimal getFlex_QRej () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Flex_QRej);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Flex_QWaste.
		@param Flex_QWaste Flex_QWaste	  */
	public void setFlex_QWaste (BigDecimal Flex_QWaste)
	{
		set_Value (COLUMNNAME_Flex_QWaste, Flex_QWaste);
	}

	/** Get Flex_QWaste.
		@return Flex_QWaste	  */
	public BigDecimal getFlex_QWaste () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Flex_QWaste);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set IK_Incom_Description.
		@param IK_Incom_Description IK_Incom_Description	  */
	public void setIK_Incom_Description (String IK_Incom_Description)
	{
		set_Value (COLUMNNAME_IK_Incom_Description, IK_Incom_Description);
	}

	/** Get IK_Incom_Description.
		@return IK_Incom_Description	  */
	public String getIK_Incom_Description () 
	{
		return (String)get_Value(COLUMNNAME_IK_Incom_Description);
	}

	/** Set IK_Incom_Inspector.
		@param IK_Incom_Inspector IK_Incom_Inspector	  */
	public void setIK_Incom_Inspector (String IK_Incom_Inspector)
	{
		set_Value (COLUMNNAME_IK_Incom_Inspector, IK_Incom_Inspector);
	}

	/** Get IK_Incom_Inspector.
		@return IK_Incom_Inspector	  */
	public String getIK_Incom_Inspector () 
	{
		return (String)get_Value(COLUMNNAME_IK_Incom_Inspector);
	}

	/** Set IK_Incom_Is_Bau.
		@param IK_Incom_Is_Bau IK_Incom_Is_Bau	  */
	public void setIK_Incom_Is_Bau (boolean IK_Incom_Is_Bau)
	{
		set_Value (COLUMNNAME_IK_Incom_Is_Bau, Boolean.valueOf(IK_Incom_Is_Bau));
	}

	/** Get IK_Incom_Is_Bau.
		@return IK_Incom_Is_Bau	  */
	public boolean isIK_Incom_Is_Bau () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Incom_Is_Bau);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Incom_Is_Gramature.
		@param IK_Incom_Is_Gramature IK_Incom_Is_Gramature	  */
	public void setIK_Incom_Is_Gramature (boolean IK_Incom_Is_Gramature)
	{
		set_Value (COLUMNNAME_IK_Incom_Is_Gramature, Boolean.valueOf(IK_Incom_Is_Gramature));
	}

	/** Get IK_Incom_Is_Gramature.
		@return IK_Incom_Is_Gramature	  */
	public boolean isIK_Incom_Is_Gramature () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Incom_Is_Gramature);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Incom_Is_Haram.
		@param IK_Incom_Is_Haram IK_Incom_Is_Haram	  */
	public void setIK_Incom_Is_Haram (boolean IK_Incom_Is_Haram)
	{
		set_Value (COLUMNNAME_IK_Incom_Is_Haram, Boolean.valueOf(IK_Incom_Is_Haram));
	}

	/** Get IK_Incom_Is_Haram.
		@return IK_Incom_Is_Haram	  */
	public boolean isIK_Incom_Is_Haram () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Incom_Is_Haram);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Incom_Is_Kebersihan.
		@param IK_Incom_Is_Kebersihan IK_Incom_Is_Kebersihan	  */
	public void setIK_Incom_Is_Kebersihan (boolean IK_Incom_Is_Kebersihan)
	{
		set_Value (COLUMNNAME_IK_Incom_Is_Kebersihan, Boolean.valueOf(IK_Incom_Is_Kebersihan));
	}

	/** Get IK_Incom_Is_Kebersihan.
		@return IK_Incom_Is_Kebersihan	  */
	public boolean isIK_Incom_Is_Kebersihan () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Incom_Is_Kebersihan);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Incom_Is_Ketebalan.
		@param IK_Incom_Is_Ketebalan IK_Incom_Is_Ketebalan	  */
	public void setIK_Incom_Is_Ketebalan (boolean IK_Incom_Is_Ketebalan)
	{
		set_Value (COLUMNNAME_IK_Incom_Is_Ketebalan, Boolean.valueOf(IK_Incom_Is_Ketebalan));
	}

	/** Get IK_Incom_Is_Ketebalan.
		@return IK_Incom_Is_Ketebalan	  */
	public boolean isIK_Incom_Is_Ketebalan () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Incom_Is_Ketebalan);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Incom_Is_Label.
		@param IK_Incom_Is_Label IK_Incom_Is_Label	  */
	public void setIK_Incom_Is_Label (boolean IK_Incom_Is_Label)
	{
		set_Value (COLUMNNAME_IK_Incom_Is_Label, Boolean.valueOf(IK_Incom_Is_Label));
	}

	/** Get IK_Incom_Is_Label.
		@return IK_Incom_Is_Label	  */
	public boolean isIK_Incom_Is_Label () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Incom_Is_Label);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Incom_Is_Packing.
		@param IK_Incom_Is_Packing IK_Incom_Is_Packing	  */
	public void setIK_Incom_Is_Packing (boolean IK_Incom_Is_Packing)
	{
		set_Value (COLUMNNAME_IK_Incom_Is_Packing, Boolean.valueOf(IK_Incom_Is_Packing));
	}

	/** Get IK_Incom_Is_Packing.
		@return IK_Incom_Is_Packing	  */
	public boolean isIK_Incom_Is_Packing () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Incom_Is_Packing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Incom_Is_Paper_Core.
		@param IK_Incom_Is_Paper_Core IK_Incom_Is_Paper_Core	  */
	public void setIK_Incom_Is_Paper_Core (boolean IK_Incom_Is_Paper_Core)
	{
		set_Value (COLUMNNAME_IK_Incom_Is_Paper_Core, Boolean.valueOf(IK_Incom_Is_Paper_Core));
	}

	/** Get IK_Incom_Is_Paper_Core.
		@return IK_Incom_Is_Paper_Core	  */
	public boolean isIK_Incom_Is_Paper_Core () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Incom_Is_Paper_Core);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Incom_Is_Permukaan.
		@param IK_Incom_Is_Permukaan IK_Incom_Is_Permukaan	  */
	public void setIK_Incom_Is_Permukaan (boolean IK_Incom_Is_Permukaan)
	{
		set_Value (COLUMNNAME_IK_Incom_Is_Permukaan, Boolean.valueOf(IK_Incom_Is_Permukaan));
	}

	/** Get IK_Incom_Is_Permukaan.
		@return IK_Incom_Is_Permukaan	  */
	public boolean isIK_Incom_Is_Permukaan () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Incom_Is_Permukaan);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Incom_Is_Warna.
		@param IK_Incom_Is_Warna IK_Incom_Is_Warna	  */
	public void setIK_Incom_Is_Warna (boolean IK_Incom_Is_Warna)
	{
		set_Value (COLUMNNAME_IK_Incom_Is_Warna, Boolean.valueOf(IK_Incom_Is_Warna));
	}

	/** Get IK_Incom_Is_Warna.
		@return IK_Incom_Is_Warna	  */
	public boolean isIK_Incom_Is_Warna () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Incom_Is_Warna);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Incom_Jml_Dtg.
		@param IK_Incom_Jml_Dtg IK_Incom_Jml_Dtg	  */
	public void setIK_Incom_Jml_Dtg (BigDecimal IK_Incom_Jml_Dtg)
	{
		set_Value (COLUMNNAME_IK_Incom_Jml_Dtg, IK_Incom_Jml_Dtg);
	}

	/** Get IK_Incom_Jml_Dtg.
		@return IK_Incom_Jml_Dtg	  */
	public BigDecimal getIK_Incom_Jml_Dtg () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_IK_Incom_Jml_Dtg);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set IK_Incom_Jml_Sampel.
		@param IK_Incom_Jml_Sampel IK_Incom_Jml_Sampel	  */
	public void setIK_Incom_Jml_Sampel (BigDecimal IK_Incom_Jml_Sampel)
	{
		set_Value (COLUMNNAME_IK_Incom_Jml_Sampel, IK_Incom_Jml_Sampel);
	}

	/** Get IK_Incom_Jml_Sampel.
		@return IK_Incom_Jml_Sampel	  */
	public BigDecimal getIK_Incom_Jml_Sampel () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_IK_Incom_Jml_Sampel);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set IK_Incom_Lebar_Roll.
		@param IK_Incom_Lebar_Roll IK_Incom_Lebar_Roll	  */
	public void setIK_Incom_Lebar_Roll (BigDecimal IK_Incom_Lebar_Roll)
	{
		set_Value (COLUMNNAME_IK_Incom_Lebar_Roll, IK_Incom_Lebar_Roll);
	}

	/** Get IK_Incom_Lebar_Roll.
		@return IK_Incom_Lebar_Roll	  */
	public BigDecimal getIK_Incom_Lebar_Roll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_IK_Incom_Lebar_Roll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set IK_Incom_QC_Status.
		@param IK_Incom_QC_Status IK_Incom_QC_Status	  */
	public void setIK_Incom_QC_Status (String IK_Incom_QC_Status)
	{
		set_Value (COLUMNNAME_IK_Incom_QC_Status, IK_Incom_QC_Status);
	}

	/** Get IK_Incom_QC_Status.
		@return IK_Incom_QC_Status	  */
	public String getIK_Incom_QC_Status () 
	{
		return (String)get_Value(COLUMNNAME_IK_Incom_QC_Status);
	}

	/** Set IK_Incom_Quality_Mgr.
		@param IK_Incom_Quality_Mgr IK_Incom_Quality_Mgr	  */
	public void setIK_Incom_Quality_Mgr (String IK_Incom_Quality_Mgr)
	{
		set_Value (COLUMNNAME_IK_Incom_Quality_Mgr, IK_Incom_Quality_Mgr);
	}

	/** Get IK_Incom_Quality_Mgr.
		@return IK_Incom_Quality_Mgr	  */
	public String getIK_Incom_Quality_Mgr () 
	{
		return (String)get_Value(COLUMNNAME_IK_Incom_Quality_Mgr);
	}

	/** Set IK_Incom_Tgl_Dtg.
		@param IK_Incom_Tgl_Dtg IK_Incom_Tgl_Dtg	  */
	public void setIK_Incom_Tgl_Dtg (Timestamp IK_Incom_Tgl_Dtg)
	{
		set_Value (COLUMNNAME_IK_Incom_Tgl_Dtg, IK_Incom_Tgl_Dtg);
	}

	/** Get IK_Incom_Tgl_Dtg.
		@return IK_Incom_Tgl_Dtg	  */
	public Timestamp getIK_Incom_Tgl_Dtg () 
	{
		return (Timestamp)get_Value(COLUMNNAME_IK_Incom_Tgl_Dtg);
	}

	/** Set IK_Incom_Tgl_Periksa.
		@param IK_Incom_Tgl_Periksa IK_Incom_Tgl_Periksa	  */
	public void setIK_Incom_Tgl_Periksa (Timestamp IK_Incom_Tgl_Periksa)
	{
		set_Value (COLUMNNAME_IK_Incom_Tgl_Periksa, IK_Incom_Tgl_Periksa);
	}

	/** Get IK_Incom_Tgl_Periksa.
		@return IK_Incom_Tgl_Periksa	  */
	public Timestamp getIK_Incom_Tgl_Periksa () 
	{
		return (Timestamp)get_Value(COLUMNNAME_IK_Incom_Tgl_Periksa);
	}

	/** Set IK_Incom_Trans_Is_Ada_Cemaran.
		@param IK_Incom_Trans_Is_Ada_Cemaran IK_Incom_Trans_Is_Ada_Cemaran	  */
	public void setIK_Incom_Trans_Is_Ada_Cemaran (boolean IK_Incom_Trans_Is_Ada_Cemaran)
	{
		set_Value (COLUMNNAME_IK_Incom_Trans_Is_Ada_Cemaran, Boolean.valueOf(IK_Incom_Trans_Is_Ada_Cemaran));
	}

	/** Get IK_Incom_Trans_Is_Ada_Cemaran.
		@return IK_Incom_Trans_Is_Ada_Cemaran	  */
	public boolean isIK_Incom_Trans_Is_Ada_Cemaran () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Incom_Trans_Is_Ada_Cemaran);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Ripple_Description.
		@param IK_Ripple_Description IK_Ripple_Description	  */
	public void setIK_Ripple_Description (String IK_Ripple_Description)
	{
		set_Value (COLUMNNAME_IK_Ripple_Description, IK_Ripple_Description);
	}

	/** Get IK_Ripple_Description.
		@return IK_Ripple_Description	  */
	public String getIK_Ripple_Description () 
	{
		return (String)get_Value(COLUMNNAME_IK_Ripple_Description);
	}

	/** Set IK_Ripple_isBlankDrill.
		@param IK_Ripple_isBlankDrill IK_Ripple_isBlankDrill	  */
	public void setIK_Ripple_isBlankDrill (boolean IK_Ripple_isBlankDrill)
	{
		set_Value (COLUMNNAME_IK_Ripple_isBlankDrill, Boolean.valueOf(IK_Ripple_isBlankDrill));
	}

	/** Get IK_Ripple_isBlankDrill.
		@return IK_Ripple_isBlankDrill	  */
	public boolean isIK_Ripple_isBlankDrill () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Ripple_isBlankDrill);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Ripple_isKontaminasiBiologi.
		@param IK_Ripple_isKontaminasiBiologi IK_Ripple_isKontaminasiBiologi	  */
	public void setIK_Ripple_isKontaminasiBiologi (boolean IK_Ripple_isKontaminasiBiologi)
	{
		set_Value (COLUMNNAME_IK_Ripple_isKontaminasiBiologi, Boolean.valueOf(IK_Ripple_isKontaminasiBiologi));
	}

	/** Get IK_Ripple_isKontaminasiBiologi.
		@return IK_Ripple_isKontaminasiBiologi	  */
	public boolean isIK_Ripple_isKontaminasiBiologi () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Ripple_isKontaminasiBiologi);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Ripple_isKontaminasiFisik.
		@param IK_Ripple_isKontaminasiFisik IK_Ripple_isKontaminasiFisik	  */
	public void setIK_Ripple_isKontaminasiFisik (boolean IK_Ripple_isKontaminasiFisik)
	{
		set_Value (COLUMNNAME_IK_Ripple_isKontaminasiFisik, Boolean.valueOf(IK_Ripple_isKontaminasiFisik));
	}

	/** Get IK_Ripple_isKontaminasiFisik.
		@return IK_Ripple_isKontaminasiFisik	  */
	public boolean isIK_Ripple_isKontaminasiFisik () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Ripple_isKontaminasiFisik);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Ripple_isKontaminasiKimia.
		@param IK_Ripple_isKontaminasiKimia IK_Ripple_isKontaminasiKimia	  */
	public void setIK_Ripple_isKontaminasiKimia (boolean IK_Ripple_isKontaminasiKimia)
	{
		set_Value (COLUMNNAME_IK_Ripple_isKontaminasiKimia, Boolean.valueOf(IK_Ripple_isKontaminasiKimia));
	}

	/** Get IK_Ripple_isKontaminasiKimia.
		@return IK_Ripple_isKontaminasiKimia	  */
	public boolean isIK_Ripple_isKontaminasiKimia () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Ripple_isKontaminasiKimia);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Ripple_isPackingAkhir.
		@param IK_Ripple_isPackingAkhir IK_Ripple_isPackingAkhir	  */
	public void setIK_Ripple_isPackingAkhir (boolean IK_Ripple_isPackingAkhir)
	{
		set_Value (COLUMNNAME_IK_Ripple_isPackingAkhir, Boolean.valueOf(IK_Ripple_isPackingAkhir));
	}

	/** Get IK_Ripple_isPackingAkhir.
		@return IK_Ripple_isPackingAkhir	  */
	public boolean isIK_Ripple_isPackingAkhir () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Ripple_isPackingAkhir);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Ripple_isPengeleman.
		@param IK_Ripple_isPengeleman IK_Ripple_isPengeleman	  */
	public void setIK_Ripple_isPengeleman (boolean IK_Ripple_isPengeleman)
	{
		set_Value (COLUMNNAME_IK_Ripple_isPengeleman, Boolean.valueOf(IK_Ripple_isPengeleman));
	}

	/** Get IK_Ripple_isPengeleman.
		@return IK_Ripple_isPengeleman	  */
	public boolean isIK_Ripple_isPengeleman () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Ripple_isPengeleman);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Ripple_JmlSampling.
		@param IK_Ripple_JmlSampling IK_Ripple_JmlSampling	  */
	public void setIK_Ripple_JmlSampling (BigDecimal IK_Ripple_JmlSampling)
	{
		set_Value (COLUMNNAME_IK_Ripple_JmlSampling, IK_Ripple_JmlSampling);
	}

	/** Get IK_Ripple_JmlSampling.
		@return IK_Ripple_JmlSampling	  */
	public BigDecimal getIK_Ripple_JmlSampling () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_IK_Ripple_JmlSampling);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set IK_Ripple_QCStatus.
		@param IK_Ripple_QCStatus IK_Ripple_QCStatus	  */
	public void setIK_Ripple_QCStatus (String IK_Ripple_QCStatus)
	{
		set_Value (COLUMNNAME_IK_Ripple_QCStatus, IK_Ripple_QCStatus);
	}

	/** Get IK_Ripple_QCStatus.
		@return IK_Ripple_QCStatus	  */
	public String getIK_Ripple_QCStatus () 
	{
		return (String)get_Value(COLUMNNAME_IK_Ripple_QCStatus);
	}

	/** Set IK_Ripple_QM.
		@param IK_Ripple_QM IK_Ripple_QM	  */
	public void setIK_Ripple_QM (String IK_Ripple_QM)
	{
		set_Value (COLUMNNAME_IK_Ripple_QM, IK_Ripple_QM);
	}

	/** Get IK_Ripple_QM.
		@return IK_Ripple_QM	  */
	public String getIK_Ripple_QM () 
	{
		return (String)get_Value(COLUMNNAME_IK_Ripple_QM);
	}

	/** Set IK_Ripple_Time.
		@param IK_Ripple_Time IK_Ripple_Time	  */
	public void setIK_Ripple_Time (Timestamp IK_Ripple_Time)
	{
		set_Value (COLUMNNAME_IK_Ripple_Time, IK_Ripple_Time);
	}

	/** Get IK_Ripple_Time.
		@return IK_Ripple_Time	  */
	public Timestamp getIK_Ripple_Time () 
	{
		return (Timestamp)get_Value(COLUMNNAME_IK_Ripple_Time);
	}

	/** Set IK_Stripping_Description.
		@param IK_Stripping_Description IK_Stripping_Description	  */
	public void setIK_Stripping_Description (String IK_Stripping_Description)
	{
		set_Value (COLUMNNAME_IK_Stripping_Description, IK_Stripping_Description);
	}

	/** Get IK_Stripping_Description.
		@return IK_Stripping_Description	  */
	public String getIK_Stripping_Description () 
	{
		return (String)get_Value(COLUMNNAME_IK_Stripping_Description);
	}

	/** Set IK_Stripping_isInspeksiCutting.
		@param IK_Stripping_isInspeksiCutting IK_Stripping_isInspeksiCutting	  */
	public void setIK_Stripping_isInspeksiCutting (boolean IK_Stripping_isInspeksiCutting)
	{
		set_Value (COLUMNNAME_IK_Stripping_isInspeksiCutting, Boolean.valueOf(IK_Stripping_isInspeksiCutting));
	}

	/** Get IK_Stripping_isInspeksiCutting.
		@return IK_Stripping_isInspeksiCutting	  */
	public boolean isIK_Stripping_isInspeksiCutting () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Stripping_isInspeksiCutting);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Stripping_isKontaminasiBio.
		@param IK_Stripping_isKontaminasiBio IK_Stripping_isKontaminasiBio	  */
	public void setIK_Stripping_isKontaminasiBio (boolean IK_Stripping_isKontaminasiBio)
	{
		set_Value (COLUMNNAME_IK_Stripping_isKontaminasiBio, Boolean.valueOf(IK_Stripping_isKontaminasiBio));
	}

	/** Get IK_Stripping_isKontaminasiBio.
		@return IK_Stripping_isKontaminasiBio	  */
	public boolean isIK_Stripping_isKontaminasiBio () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Stripping_isKontaminasiBio);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Stripping_isKontaminasiFis.
		@param IK_Stripping_isKontaminasiFis IK_Stripping_isKontaminasiFis	  */
	public void setIK_Stripping_isKontaminasiFis (boolean IK_Stripping_isKontaminasiFis)
	{
		set_Value (COLUMNNAME_IK_Stripping_isKontaminasiFis, Boolean.valueOf(IK_Stripping_isKontaminasiFis));
	}

	/** Get IK_Stripping_isKontaminasiFis.
		@return IK_Stripping_isKontaminasiFis	  */
	public boolean isIK_Stripping_isKontaminasiFis () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Stripping_isKontaminasiFis);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Stripping_isKontaminasiKim.
		@param IK_Stripping_isKontaminasiKim IK_Stripping_isKontaminasiKim	  */
	public void setIK_Stripping_isKontaminasiKim (boolean IK_Stripping_isKontaminasiKim)
	{
		set_Value (COLUMNNAME_IK_Stripping_isKontaminasiKim, Boolean.valueOf(IK_Stripping_isKontaminasiKim));
	}

	/** Get IK_Stripping_isKontaminasiKim.
		@return IK_Stripping_isKontaminasiKim	  */
	public boolean isIK_Stripping_isKontaminasiKim () 
	{
		Object oo = get_Value(COLUMNNAME_IK_Stripping_isKontaminasiKim);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IK_Stripping_QCStatus.
		@param IK_Stripping_QCStatus IK_Stripping_QCStatus	  */
	public void setIK_Stripping_QCStatus (String IK_Stripping_QCStatus)
	{
		set_Value (COLUMNNAME_IK_Stripping_QCStatus, IK_Stripping_QCStatus);
	}

	/** Get IK_Stripping_QCStatus.
		@return IK_Stripping_QCStatus	  */
	public String getIK_Stripping_QCStatus () 
	{
		return (String)get_Value(COLUMNNAME_IK_Stripping_QCStatus);
	}

	/** Set IK_Stripping_QM.
		@param IK_Stripping_QM IK_Stripping_QM	  */
	public void setIK_Stripping_QM (String IK_Stripping_QM)
	{
		set_Value (COLUMNNAME_IK_Stripping_QM, IK_Stripping_QM);
	}

	/** Get IK_Stripping_QM.
		@return IK_Stripping_QM	  */
	public String getIK_Stripping_QM () 
	{
		return (String)get_Value(COLUMNNAME_IK_Stripping_QM);
	}

	/** Set Ins_QHold.
		@param Ins_QHold Ins_QHold	  */
	public void setIns_QHold (BigDecimal Ins_QHold)
	{
		set_Value (COLUMNNAME_Ins_QHold, Ins_QHold);
	}

	/** Get Ins_QHold.
		@return Ins_QHold	  */
	public BigDecimal getIns_QHold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Ins_QHold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Ins_QPass.
		@param Ins_QPass Ins_QPass	  */
	public void setIns_QPass (BigDecimal Ins_QPass)
	{
		set_Value (COLUMNNAME_Ins_QPass, Ins_QPass);
	}

	/** Get Ins_QPass.
		@return Ins_QPass	  */
	public BigDecimal getIns_QPass () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Ins_QPass);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Ins_QPrd.
		@param Ins_QPrd Ins_QPrd	  */
	public void setIns_QPrd (BigDecimal Ins_QPrd)
	{
		set_Value (COLUMNNAME_Ins_QPrd, Ins_QPrd);
	}

	/** Get Ins_QPrd.
		@return Ins_QPrd	  */
	public BigDecimal getIns_QPrd () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Ins_QPrd);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Ins_QRej.
		@param Ins_QRej Ins_QRej	  */
	public void setIns_QRej (BigDecimal Ins_QRej)
	{
		set_Value (COLUMNNAME_Ins_QRej, Ins_QRej);
	}

	/** Get Ins_QRej.
		@return Ins_QRej	  */
	public BigDecimal getIns_QRej () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Ins_QRej);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Ins_QWaste.
		@param Ins_QWaste Ins_QWaste	  */
	public void setIns_QWaste (BigDecimal Ins_QWaste)
	{
		set_Value (COLUMNNAME_Ins_QWaste, Ins_QWaste);
	}

	/** Get Ins_QWaste.
		@return Ins_QWaste	  */
	public BigDecimal getIns_QWaste () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Ins_QWaste);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set QC Activity.
		@param KJS_Activity QC Activity	  */
	public void setKJS_Activity (String KJS_Activity)
	{
		set_Value (COLUMNNAME_KJS_Activity, KJS_Activity);
	}

	/** Get QC Activity.
		@return QC Activity	  */
	public String getKJS_Activity () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Activity);
	}

	/** Set Dimensi Aktual (Toleransi +5%) Panjang (mm).
		@param KJS_ActualLength Dimensi Aktual (Toleransi +5%) Panjang (mm)	  */
	public void setKJS_ActualLength (BigDecimal KJS_ActualLength)
	{
		set_Value (COLUMNNAME_KJS_ActualLength, KJS_ActualLength);
	}

	/** Get Dimensi Aktual (Toleransi +5%) Panjang (mm).
		@return Dimensi Aktual (Toleransi +5%) Panjang (mm)	  */
	public BigDecimal getKJS_ActualLength () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_ActualLength);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Dimensi Aktual (Toleransi +5%) Lebar (mm).
		@param KJS_ActualWidth Dimensi Aktual (Toleransi +5%) Lebar (mm)	  */
	public void setKJS_ActualWidth (BigDecimal KJS_ActualWidth)
	{
		set_Value (COLUMNNAME_KJS_ActualWidth, KJS_ActualWidth);
	}

	/** Get Dimensi Aktual (Toleransi +5%) Lebar (mm).
		@return Dimensi Aktual (Toleransi +5%) Lebar (mm)	  */
	public BigDecimal getKJS_ActualWidth () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_ActualWidth);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Box No.
		@param KJS_BoxNo Box No	  */
	public void setKJS_BoxNo (String KJS_BoxNo)
	{
		set_Value (COLUMNNAME_KJS_BoxNo, KJS_BoxNo);
	}

	/** Get Box No.
		@return Box No	  */
	public String getKJS_BoxNo () 
	{
		return (String)get_Value(COLUMNNAME_KJS_BoxNo);
	}

	/** Set LainLain Description.
		@param KJS_Description LainLain Description	  */
	public void setKJS_Description (String KJS_Description)
	{
		set_Value (COLUMNNAME_KJS_Description, KJS_Description);
	}

	/** Get LainLain Description.
		@return LainLain Description	  */
	public String getKJS_Description () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Description);
	}

	/** Set Dimensi Standar Panjang (mm).
		@param KJS_DimentionLength Dimensi Standar Panjang (mm)	  */
	public void setKJS_DimentionLength (BigDecimal KJS_DimentionLength)
	{
		set_Value (COLUMNNAME_KJS_DimentionLength, KJS_DimentionLength);
	}

	/** Get Dimensi Standar Panjang (mm).
		@return Dimensi Standar Panjang (mm)	  */
	public BigDecimal getKJS_DimentionLength () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_DimentionLength);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Dimensi Standar Lebar (mm).
		@param KJS_DimentionWidth Dimensi Standar Lebar (mm)	  */
	public void setKJS_DimentionWidth (BigDecimal KJS_DimentionWidth)
	{
		set_Value (COLUMNNAME_KJS_DimentionWidth, KJS_DimentionWidth);
	}

	/** Get Dimensi Standar Lebar (mm).
		@return Dimensi Standar Lebar (mm)	  */
	public BigDecimal getKJS_DimentionWidth () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_DimentionWidth);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Inspector.
		@param KJS_Inspector Inspector	  */
	public void setKJS_Inspector (String KJS_Inspector)
	{
		set_Value (COLUMNNAME_KJS_Inspector, KJS_Inspector);
	}

	/** Get Inspector.
		@return Inspector	  */
	public String getKJS_Inspector () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Inspector);
	}

	/** Set Inspector.
		@param KJS_Inspector_Bag Inspector	  */
	public void setKJS_Inspector_Bag (String KJS_Inspector_Bag)
	{
		set_Value (COLUMNNAME_KJS_Inspector_Bag, KJS_Inspector_Bag);
	}

	/** Get Inspector.
		@return Inspector	  */
	public String getKJS_Inspector_Bag () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Inspector_Bag);
	}

	/** Set Inspector.
		@param KJS_Inspector_Die Inspector	  */
	public void setKJS_Inspector_Die (String KJS_Inspector_Die)
	{
		set_Value (COLUMNNAME_KJS_Inspector_Die, KJS_Inspector_Die);
	}

	/** Get Inspector.
		@return Inspector	  */
	public String getKJS_Inspector_Die () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Inspector_Die);
	}

	/** Set Inspector.
		@param KJS_Inspector_Lunch Inspector	  */
	public void setKJS_Inspector_Lunch (String KJS_Inspector_Lunch)
	{
		set_Value (COLUMNNAME_KJS_Inspector_Lunch, KJS_Inspector_Lunch);
	}

	/** Get Inspector.
		@return Inspector	  */
	public String getKJS_Inspector_Lunch () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Inspector_Lunch);
	}

	/** Set Inspector.
		@param KJS_Inspector_Paper Inspector	  */
	public void setKJS_Inspector_Paper (String KJS_Inspector_Paper)
	{
		set_Value (COLUMNNAME_KJS_Inspector_Paper, KJS_Inspector_Paper);
	}

	/** Get Inspector.
		@return Inspector	  */
	public String getKJS_Inspector_Paper () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Inspector_Paper);
	}

	/** Set Inspector.
		@param KJS_Inspector_Ripple Inspector	  */
	public void setKJS_Inspector_Ripple (String KJS_Inspector_Ripple)
	{
		set_Value (COLUMNNAME_KJS_Inspector_Ripple, KJS_Inspector_Ripple);
	}

	/** Get Inspector.
		@return Inspector	  */
	public String getKJS_Inspector_Ripple () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Inspector_Ripple);
	}

	/** Set Inspector.
		@param KJS_Inspector_Stripping Inspector	  */
	public void setKJS_Inspector_Stripping (String KJS_Inspector_Stripping)
	{
		set_Value (COLUMNNAME_KJS_Inspector_Stripping, KJS_Inspector_Stripping);
	}

	/** Get Inspector.
		@return Inspector	  */
	public String getKJS_Inspector_Stripping () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Inspector_Stripping);
	}

	/** Set Hasil Inspeksi Benda Asing.
		@param KJS_IsAsing Hasil Inspeksi Benda Asing	  */
	public void setKJS_IsAsing (boolean KJS_IsAsing)
	{
		set_Value (COLUMNNAME_KJS_IsAsing, Boolean.valueOf(KJS_IsAsing));
	}

	/** Get Hasil Inspeksi Benda Asing.
		@return Hasil Inspeksi Benda Asing	  */
	public boolean isKJS_IsAsing () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsAsing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Visual Bergaris.
		@param KJS_IsBergaris Visual Bergaris	  */
	public void setKJS_IsBergaris (boolean KJS_IsBergaris)
	{
		set_Value (COLUMNNAME_KJS_IsBergaris, Boolean.valueOf(KJS_IsBergaris));
	}

	/** Get Visual Bergaris.
		@return Visual Bergaris	  */
	public boolean isKJS_IsBergaris () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsBergaris);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Kebersihan Kertas.
		@param KJS_IsBersihKertas Kebersihan Kertas	  */
	public void setKJS_IsBersihKertas (boolean KJS_IsBersihKertas)
	{
		set_Value (COLUMNNAME_KJS_IsBersihKertas, Boolean.valueOf(KJS_IsBersihKertas));
	}

	/** Get Kebersihan Kertas.
		@return Kebersihan Kertas	  */
	public boolean isKJS_IsBersihKertas () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsBersihKertas);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Kebersihan Print.
		@param KJS_IsBersihPrint Kebersihan Print	  */
	public void setKJS_IsBersihPrint (boolean KJS_IsBersihPrint)
	{
		set_Value (COLUMNNAME_KJS_IsBersihPrint, Boolean.valueOf(KJS_IsBersihPrint));
	}

	/** Get Kebersihan Print.
		@return Kebersihan Print	  */
	public boolean isKJS_IsBersihPrint () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsBersihPrint);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Body.
		@param KJS_IsBody Hasil Inspeksi Body	  */
	public void setKJS_IsBody (boolean KJS_IsBody)
	{
		set_Value (COLUMNNAME_KJS_IsBody, Boolean.valueOf(KJS_IsBody));
	}

	/** Get Hasil Inspeksi Body.
		@return Hasil Inspeksi Body	  */
	public boolean isKJS_IsBody () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsBody);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Visual Cacat.
		@param KJS_IsCacat Visual Cacat	  */
	public void setKJS_IsCacat (boolean KJS_IsCacat)
	{
		set_Value (COLUMNNAME_KJS_IsCacat, Boolean.valueOf(KJS_IsCacat));
	}

	/** Get Visual Cacat.
		@return Visual Cacat	  */
	public boolean isKJS_IsCacat () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsCacat);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Kontaminasi/Benda Asing Biologi.
		@param KJS_IsContaBio Kontaminasi/Benda Asing Biologi	  */
	public void setKJS_IsContaBio (boolean KJS_IsContaBio)
	{
		set_Value (COLUMNNAME_KJS_IsContaBio, Boolean.valueOf(KJS_IsContaBio));
	}

	/** Get Kontaminasi/Benda Asing Biologi.
		@return Kontaminasi/Benda Asing Biologi	  */
	public boolean isKJS_IsContaBio () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsContaBio);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Kontaminasi/Benda Asing Kimia.
		@param KJS_IsContaChem Kontaminasi/Benda Asing Kimia	  */
	public void setKJS_IsContaChem (boolean KJS_IsContaChem)
	{
		set_Value (COLUMNNAME_KJS_IsContaChem, Boolean.valueOf(KJS_IsContaChem));
	}

	/** Get Kontaminasi/Benda Asing Kimia.
		@return Kontaminasi/Benda Asing Kimia	  */
	public boolean isKJS_IsContaChem () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsContaChem);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Kontaminasi/Benda Asing Fisik.
		@param KJS_IsContaPhy Kontaminasi/Benda Asing Fisik	  */
	public void setKJS_IsContaPhy (boolean KJS_IsContaPhy)
	{
		set_Value (COLUMNNAME_KJS_IsContaPhy, Boolean.valueOf(KJS_IsContaPhy));
	}

	/** Get Kontaminasi/Benda Asing Fisik.
		@return Kontaminasi/Benda Asing Fisik	  */
	public boolean isKJS_IsContaPhy () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsContaPhy);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Cross Mark.
		@param KJS_IsCrossMark Cross Mark	  */
	public void setKJS_IsCrossMark (boolean KJS_IsCrossMark)
	{
		set_Value (COLUMNNAME_KJS_IsCrossMark, Boolean.valueOf(KJS_IsCrossMark));
	}

	/** Get Cross Mark.
		@return Cross Mark	  */
	public boolean isKJS_IsCrossMark () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsCrossMark);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Cutting.
		@param KJS_IsCutting Hasil Inspeksi Cutting	  */
	public void setKJS_IsCutting (boolean KJS_IsCutting)
	{
		set_Value (COLUMNNAME_KJS_IsCutting, Boolean.valueOf(KJS_IsCutting));
	}

	/** Get Hasil Inspeksi Cutting.
		@return Hasil Inspeksi Cutting	  */
	public boolean isKJS_IsCutting () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsCutting);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Dimensi.
		@param KJS_IsDimensi Hasil Inspeksi Dimensi	  */
	public void setKJS_IsDimensi (boolean KJS_IsDimensi)
	{
		set_Value (COLUMNNAME_KJS_IsDimensi, Boolean.valueOf(KJS_IsDimensi));
	}

	/** Get Hasil Inspeksi Dimensi.
		@return Hasil Inspeksi Dimensi	  */
	public boolean isKJS_IsDimensi () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsDimensi);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Test Dripping Bagus.
		@param KJS_IsDrippingTestBagus Test Dripping Bagus	  */
	public void setKJS_IsDrippingTestBagus (boolean KJS_IsDrippingTestBagus)
	{
		set_Value (COLUMNNAME_KJS_IsDrippingTestBagus, Boolean.valueOf(KJS_IsDrippingTestBagus));
	}

	/** Get Test Dripping Bagus.
		@return Test Dripping Bagus	  */
	public boolean isKJS_IsDrippingTestBagus () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsDrippingTestBagus);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Test Dripping Tidak.
		@param KJS_IsDrippingTestTidak Test Dripping Tidak	  */
	public void setKJS_IsDrippingTestTidak (boolean KJS_IsDrippingTestTidak)
	{
		set_Value (COLUMNNAME_KJS_IsDrippingTestTidak, Boolean.valueOf(KJS_IsDrippingTestTidak));
	}

	/** Get Test Dripping Tidak.
		@return Test Dripping Tidak	  */
	public boolean isKJS_IsDrippingTestTidak () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsDrippingTestTidak);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Garis Pond.
		@param KJS_IsGarisPond Garis Pond	  */
	public void setKJS_IsGarisPond (boolean KJS_IsGarisPond)
	{
		set_Value (COLUMNNAME_KJS_IsGarisPond, Boolean.valueOf(KJS_IsGarisPond));
	}

	/** Get Garis Pond.
		@return Garis Pond	  */
	public boolean isKJS_IsGarisPond () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsGarisPond);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Hygiene.
		@param KJS_IsHygiene Hasil Inspeksi Hygiene	  */
	public void setKJS_IsHygiene (boolean KJS_IsHygiene)
	{
		set_Value (COLUMNNAME_KJS_IsHygiene, Boolean.valueOf(KJS_IsHygiene));
	}

	/** Get Hasil Inspeksi Hygiene.
		@return Hasil Inspeksi Hygiene	  */
	public boolean isKJS_IsHygiene () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsHygiene);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspection Jumlah / Isi / Labeling.
		@param KJS_IsInspectionAll Hasil Inspection Jumlah / Isi / Labeling	  */
	public void setKJS_IsInspectionAll (boolean KJS_IsInspectionAll)
	{
		set_Value (COLUMNNAME_KJS_IsInspectionAll, Boolean.valueOf(KJS_IsInspectionAll));
	}

	/** Get Hasil Inspection Jumlah / Isi / Labeling.
		@return Hasil Inspection Jumlah / Isi / Labeling	  */
	public boolean isKJS_IsInspectionAll () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsInspectionAll);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Visual Kerut.
		@param KJS_IsKerut Visual Kerut	  */
	public void setKJS_IsKerut (boolean KJS_IsKerut)
	{
		set_Value (COLUMNNAME_KJS_IsKerut, Boolean.valueOf(KJS_IsKerut));
	}

	/** Get Visual Kerut.
		@return Visual Kerut	  */
	public boolean isKJS_IsKerut () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsKerut);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Kode Tanggal.
		@param KJS_IsKodeTanggal Kode Tanggal	  */
	public void setKJS_IsKodeTanggal (boolean KJS_IsKodeTanggal)
	{
		set_Value (COLUMNNAME_KJS_IsKodeTanggal, Boolean.valueOf(KJS_IsKodeTanggal));
	}

	/** Get Kode Tanggal.
		@return Kode Tanggal	  */
	public boolean isKJS_IsKodeTanggal () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsKodeTanggal);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Visual Kotor.
		@param KJS_IsKotor Visual Kotor	  */
	public void setKJS_IsKotor (boolean KJS_IsKotor)
	{
		set_Value (COLUMNNAME_KJS_IsKotor, Boolean.valueOf(KJS_IsKotor));
	}

	/** Get Visual Kotor.
		@return Visual Kotor	  */
	public boolean isKJS_IsKotor () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsKotor);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Lain Lain.
		@param KJS_IsLain Lain Lain	  */
	public void setKJS_IsLain (boolean KJS_IsLain)
	{
		set_Value (COLUMNNAME_KJS_IsLain, Boolean.valueOf(KJS_IsLain));
	}

	/** Get Lain Lain.
		@return Lain Lain	  */
	public boolean isKJS_IsLain () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsLain);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Pengeleman.
		@param KJS_IsLem Hasil Inspeksi Pengeleman	  */
	public void setKJS_IsLem (boolean KJS_IsLem)
	{
		set_Value (COLUMNNAME_KJS_IsLem, Boolean.valueOf(KJS_IsLem));
	}

	/** Get Hasil Inspeksi Pengeleman.
		@return Hasil Inspeksi Pengeleman	  */
	public boolean isKJS_IsLem () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsLem);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Test Lid Bagus.
		@param KJS_IsLidTestBagus Test Lid Bagus	  */
	public void setKJS_IsLidTestBagus (boolean KJS_IsLidTestBagus)
	{
		set_Value (COLUMNNAME_KJS_IsLidTestBagus, Boolean.valueOf(KJS_IsLidTestBagus));
	}

	/** Get Test Lid Bagus.
		@return Test Lid Bagus	  */
	public boolean isKJS_IsLidTestBagus () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsLidTestBagus);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Test Lid Tidak.
		@param KJS_IsLidTestTidak Test Lid Tidak	  */
	public void setKJS_IsLidTestTidak (boolean KJS_IsLidTestTidak)
	{
		set_Value (COLUMNNAME_KJS_IsLidTestTidak, Boolean.valueOf(KJS_IsLidTestTidak));
	}

	/** Get Test Lid Tidak.
		@return Test Lid Tidak	  */
	public boolean isKJS_IsLidTestTidak () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsLidTestTidak);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Kelunturan.
		@param KJS_IsLuntur Kelunturan	  */
	public void setKJS_IsLuntur (boolean KJS_IsLuntur)
	{
		set_Value (COLUMNNAME_KJS_IsLuntur, Boolean.valueOf(KJS_IsLuntur));
	}

	/** Get Kelunturan.
		@return Kelunturan	  */
	public boolean isKJS_IsLuntur () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsLuntur);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Monitoring Berada dimesin.
		@param KJS_IsMonitoringMesin Monitoring Berada dimesin	  */
	public void setKJS_IsMonitoringMesin (boolean KJS_IsMonitoringMesin)
	{
		set_Value (COLUMNNAME_KJS_IsMonitoringMesin, Boolean.valueOf(KJS_IsMonitoringMesin));
	}

	/** Get Monitoring Berada dimesin.
		@return Monitoring Berada dimesin	  */
	public boolean isKJS_IsMonitoringMesin () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsMonitoringMesin);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Monitoring Adanya Ketidaksesuaian.
		@param KJS_IsMonitoringTidakSesuai Monitoring Adanya Ketidaksesuaian	  */
	public void setKJS_IsMonitoringTidakSesuai (boolean KJS_IsMonitoringTidakSesuai)
	{
		set_Value (COLUMNNAME_KJS_IsMonitoringTidakSesuai, Boolean.valueOf(KJS_IsMonitoringTidakSesuai));
	}

	/** Get Monitoring Adanya Ketidaksesuaian.
		@return Monitoring Adanya Ketidaksesuaian	  */
	public boolean isKJS_IsMonitoringTidakSesuai () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsMonitoringTidakSesuai);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Packing / Inspeksi Akhir.
		@param KJS_IsPacking Hasil Inspeksi Packing / Inspeksi Akhir	  */
	public void setKJS_IsPacking (boolean KJS_IsPacking)
	{
		set_Value (COLUMNNAME_KJS_IsPacking, Boolean.valueOf(KJS_IsPacking));
	}

	/** Get Hasil Inspeksi Packing / Inspeksi Akhir.
		@return Hasil Inspeksi Packing / Inspeksi Akhir	  */
	public boolean isKJS_IsPacking () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsPacking);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Lipatan & Potongan Atas.
		@param KJS_IsPotongan Hasil Inspeksi Lipatan & Potongan Atas	  */
	public void setKJS_IsPotongan (boolean KJS_IsPotongan)
	{
		set_Value (COLUMNNAME_KJS_IsPotongan, Boolean.valueOf(KJS_IsPotongan));
	}

	/** Get Hasil Inspeksi Lipatan & Potongan Atas.
		@return Hasil Inspeksi Lipatan & Potongan Atas	  */
	public boolean isKJS_IsPotongan () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsPotongan);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Printing.
		@param KJS_IsPrinting Hasil Inspeksi Printing	  */
	public void setKJS_IsPrinting (boolean KJS_IsPrinting)
	{
		set_Value (COLUMNNAME_KJS_IsPrinting, Boolean.valueOf(KJS_IsPrinting));
	}

	/** Get Hasil Inspeksi Printing.
		@return Hasil Inspeksi Printing	  */
	public boolean isKJS_IsPrinting () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsPrinting);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Kerataan Print.
		@param KJS_IsRata Kerataan Print	  */
	public void setKJS_IsRata (boolean KJS_IsRata)
	{
		set_Value (COLUMNNAME_KJS_IsRata, Boolean.valueOf(KJS_IsRata));
	}

	/** Get Kerataan Print.
		@return Kerataan Print	  */
	public boolean isKJS_IsRata () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsRata);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Registrasi.
		@param KJS_IsRegistrasi Registrasi	  */
	public void setKJS_IsRegistrasi (boolean KJS_IsRegistrasi)
	{
		set_Value (COLUMNNAME_KJS_IsRegistrasi, Boolean.valueOf(KJS_IsRegistrasi));
	}

	/** Get Registrasi.
		@return Registrasi	  */
	public boolean isKJS_IsRegistrasi () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsRegistrasi);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Rim.
		@param KJS_IsRim Hasil Inspeksi Rim	  */
	public void setKJS_IsRim (boolean KJS_IsRim)
	{
		set_Value (COLUMNNAME_KJS_IsRim, Boolean.valueOf(KJS_IsRim));
	}

	/** Get Hasil Inspeksi Rim.
		@return Hasil Inspeksi Rim	  */
	public boolean isKJS_IsRim () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsRim);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Roll.
		@param KJS_IsRoll Hasil Inspeksi Roll	  */
	public void setKJS_IsRoll (boolean KJS_IsRoll)
	{
		set_Value (COLUMNNAME_KJS_IsRoll, Boolean.valueOf(KJS_IsRoll));
	}

	/** Get Hasil Inspeksi Roll.
		@return Hasil Inspeksi Roll	  */
	public boolean isKJS_IsRoll () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsRoll);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Sealing.
		@param KJS_IsSealing Hasil Inspeksi Sealing	  */
	public void setKJS_IsSealing (boolean KJS_IsSealing)
	{
		set_Value (COLUMNNAME_KJS_IsSealing, Boolean.valueOf(KJS_IsSealing));
	}

	/** Get Hasil Inspeksi Sealing.
		@return Hasil Inspeksi Sealing	  */
	public boolean isKJS_IsSealing () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsSealing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Visual Serat Kertas.
		@param KJS_IsSerat Visual Serat Kertas	  */
	public void setKJS_IsSerat (boolean KJS_IsSerat)
	{
		set_Value (COLUMNNAME_KJS_IsSerat, Boolean.valueOf(KJS_IsSerat));
	}

	/** Get Visual Serat Kertas.
		@return Visual Serat Kertas	  */
	public boolean isKJS_IsSerat () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsSerat);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Kesesuaian Desain.
		@param KJS_IsSesuai Kesesuaian Desain	  */
	public void setKJS_IsSesuai (boolean KJS_IsSesuai)
	{
		set_Value (COLUMNNAME_KJS_IsSesuai, Boolean.valueOf(KJS_IsSesuai));
	}

	/** Get Kesesuaian Desain.
		@return Kesesuaian Desain	  */
	public boolean isKJS_IsSesuai () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsSesuai);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Kesolidan.
		@param KJS_IsSolid Kesolidan	  */
	public void setKJS_IsSolid (boolean KJS_IsSolid)
	{
		set_Value (COLUMNNAME_KJS_IsSolid, Boolean.valueOf(KJS_IsSolid));
	}

	/** Get Kesolidan.
		@return Kesolidan	  */
	public boolean isKJS_IsSolid () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsSolid);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Ketajaman.
		@param KJS_IsTajam Ketajaman	  */
	public void setKJS_IsTajam (boolean KJS_IsTajam)
	{
		set_Value (COLUMNNAME_KJS_IsTajam, Boolean.valueOf(KJS_IsTajam));
	}

	/** Get Ketajaman.
		@return Ketajaman	  */
	public boolean isKJS_IsTajam () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsTajam);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Toleransi Register Garis Potong 0.10mm.
		@param KJS_IsToleransi Hasil Inspeksi Toleransi Register Garis Potong 0.10mm	  */
	public void setKJS_IsToleransi (boolean KJS_IsToleransi)
	{
		set_Value (COLUMNNAME_KJS_IsToleransi, Boolean.valueOf(KJS_IsToleransi));
	}

	/** Get Hasil Inspeksi Toleransi Register Garis Potong 0.10mm.
		@return Hasil Inspeksi Toleransi Register Garis Potong 0.10mm	  */
	public boolean isKJS_IsToleransi () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsToleransi);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Hasil Inspeksi Ukuran Jadi.
		@param KJS_IsUkuran Hasil Inspeksi Ukuran Jadi	  */
	public void setKJS_IsUkuran (boolean KJS_IsUkuran)
	{
		set_Value (COLUMNNAME_KJS_IsUkuran, Boolean.valueOf(KJS_IsUkuran));
	}

	/** Get Hasil Inspeksi Ukuran Jadi.
		@return Hasil Inspeksi Ukuran Jadi	  */
	public boolean isKJS_IsUkuran () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsUkuran);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Kesesuaian Warna.
		@param KJS_IsWarna Kesesuaian Warna	  */
	public void setKJS_IsWarna (boolean KJS_IsWarna)
	{
		set_Value (COLUMNNAME_KJS_IsWarna, Boolean.valueOf(KJS_IsWarna));
	}

	/** Get Kesesuaian Warna.
		@return Kesesuaian Warna	  */
	public boolean isKJS_IsWarna () 
	{
		Object oo = get_Value(COLUMNNAME_KJS_IsWarna);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Jenis Kertas.
		@param KJS_JenisKertas Jenis Kertas	  */
	public void setKJS_JenisKertas (String KJS_JenisKertas)
	{
		set_Value (COLUMNNAME_KJS_JenisKertas, KJS_JenisKertas);
	}

	/** Get Jenis Kertas.
		@return Jenis Kertas	  */
	public String getKJS_JenisKertas () 
	{
		return (String)get_Value(COLUMNNAME_KJS_JenisKertas);
	}

	/** Set Hasil Leak Test Bagus.
		@param KJS_LeakTestBagus Hasil Leak Test Bagus	  */
	public void setKJS_LeakTestBagus (BigDecimal KJS_LeakTestBagus)
	{
		set_Value (COLUMNNAME_KJS_LeakTestBagus, KJS_LeakTestBagus);
	}

	/** Get Hasil Leak Test Bagus.
		@return Hasil Leak Test Bagus	  */
	public BigDecimal getKJS_LeakTestBagus () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_LeakTestBagus);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Hasil Leak Test Bocor.
		@param KJS_LeakTestBocor Hasil Leak Test Bocor	  */
	public void setKJS_LeakTestBocor (BigDecimal KJS_LeakTestBocor)
	{
		set_Value (COLUMNNAME_KJS_LeakTestBocor, KJS_LeakTestBocor);
	}

	/** Get Hasil Leak Test Bocor.
		@return Hasil Leak Test Bocor	  */
	public BigDecimal getKJS_LeakTestBocor () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_LeakTestBocor);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Quality Manager.
		@param KJS_Manager Quality Manager	  */
	public void setKJS_Manager (String KJS_Manager)
	{
		set_Value (COLUMNNAME_KJS_Manager, KJS_Manager);
	}

	/** Get Quality Manager.
		@return Quality Manager	  */
	public String getKJS_Manager () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Manager);
	}

	/** Set Material.
		@param KJS_Material Material	  */
	public void setKJS_Material (String KJS_Material)
	{
		set_Value (COLUMNNAME_KJS_Material, KJS_Material);
	}

	/** Get Material.
		@return Material	  */
	public String getKJS_Material () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Material);
	}

	/** Set QC.
		@param KJS_QC_ID QC	  */
	public void setKJS_QC_ID (int KJS_QC_ID)
	{
		if (KJS_QC_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_KJS_QC_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_KJS_QC_ID, Integer.valueOf(KJS_QC_ID));
	}

	/** Get QC.
		@return QC	  */
	public int getKJS_QC_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_KJS_QC_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set KJS_QC_UU.
		@param KJS_QC_UU KJS_QC_UU	  */
	public void setKJS_QC_UU (String KJS_QC_UU)
	{
		set_ValueNoCheck (COLUMNNAME_KJS_QC_UU, KJS_QC_UU);
	}

	/** Get KJS_QC_UU.
		@return KJS_QC_UU	  */
	public String getKJS_QC_UU () 
	{
		return (String)get_Value(COLUMNNAME_KJS_QC_UU);
	}

	/** Set Kedalaman Rel From.
		@param KJS_RelFrom Kedalaman Rel From	  */
	public void setKJS_RelFrom (BigDecimal KJS_RelFrom)
	{
		set_Value (COLUMNNAME_KJS_RelFrom, KJS_RelFrom);
	}

	/** Get Kedalaman Rel From.
		@return Kedalaman Rel From	  */
	public BigDecimal getKJS_RelFrom () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_RelFrom);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Kedalaman Rel To.
		@param KJS_RelTo Kedalaman Rel To	  */
	public void setKJS_RelTo (BigDecimal KJS_RelTo)
	{
		set_Value (COLUMNNAME_KJS_RelTo, KJS_RelTo);
	}

	/** Get Kedalaman Rel To.
		@return Kedalaman Rel To	  */
	public BigDecimal getKJS_RelTo () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_RelTo);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Jumlah Sampling.
		@param KJS_SamplingQty Jumlah Sampling	  */
	public void setKJS_SamplingQty (BigDecimal KJS_SamplingQty)
	{
		set_Value (COLUMNNAME_KJS_SamplingQty, KJS_SamplingQty);
	}

	/** Get Jumlah Sampling.
		@return Jumlah Sampling	  */
	public BigDecimal getKJS_SamplingQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SamplingQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Size.
		@param KJS_Size Size	  */
	public void setKJS_Size (String KJS_Size)
	{
		set_Value (COLUMNNAME_KJS_Size, KJS_Size);
	}

	/** Get Size.
		@return Size	  */
	public String getKJS_Size () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Size);
	}

	/** Set Test Sobek Bagus.
		@param KJS_SobekTestBagus Test Sobek Bagus	  */
	public void setKJS_SobekTestBagus (BigDecimal KJS_SobekTestBagus)
	{
		set_Value (COLUMNNAME_KJS_SobekTestBagus, KJS_SobekTestBagus);
	}

	/** Get Test Sobek Bagus.
		@return Test Sobek Bagus	  */
	public BigDecimal getKJS_SobekTestBagus () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SobekTestBagus);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Test Sobek Tidak.
		@param KJS_SobekTestTidak Test Sobek Tidak	  */
	public void setKJS_SobekTestTidak (BigDecimal KJS_SobekTestTidak)
	{
		set_Value (COLUMNNAME_KJS_SobekTestTidak, KJS_SobekTestTidak);
	}

	/** Get Test Sobek Tidak.
		@return Test Sobek Tidak	  */
	public BigDecimal getKJS_SobekTestTidak () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SobekTestTidak);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Bottom Depth.
		@param KJS_SpecBotDepth Specification Bottom Depth	  */
	public void setKJS_SpecBotDepth (BigDecimal KJS_SpecBotDepth)
	{
		set_Value (COLUMNNAME_KJS_SpecBotDepth, KJS_SpecBotDepth);
	}

	/** Get Specification Bottom Depth.
		@return Specification Bottom Depth	  */
	public BigDecimal getKJS_SpecBotDepth () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecBotDepth);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Bottom Depth.
		@param KJS_SpecBotDepth_Max Specification Bottom Depth	  */
	public void setKJS_SpecBotDepth_Max (BigDecimal KJS_SpecBotDepth_Max)
	{
		set_Value (COLUMNNAME_KJS_SpecBotDepth_Max, KJS_SpecBotDepth_Max);
	}

	/** Get Specification Bottom Depth.
		@return Specification Bottom Depth	  */
	public BigDecimal getKJS_SpecBotDepth_Max () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecBotDepth_Max);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Bottom Diameter.
		@param KJS_SpecBotDiam Specification Bottom Diameter	  */
	public void setKJS_SpecBotDiam (BigDecimal KJS_SpecBotDiam)
	{
		set_Value (COLUMNNAME_KJS_SpecBotDiam, KJS_SpecBotDiam);
	}

	/** Get Specification Bottom Diameter.
		@return Specification Bottom Diameter	  */
	public BigDecimal getKJS_SpecBotDiam () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecBotDiam);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Bottom Diameter.
		@param KJS_SpecBotDiam_Max Specification Bottom Diameter	  */
	public void setKJS_SpecBotDiam_Max (BigDecimal KJS_SpecBotDiam_Max)
	{
		set_Value (COLUMNNAME_KJS_SpecBotDiam_Max, KJS_SpecBotDiam_Max);
	}

	/** Get Specification Bottom Diameter.
		@return Specification Bottom Diameter	  */
	public BigDecimal getKJS_SpecBotDiam_Max () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecBotDiam_Max);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Height.
		@param KJS_SpecHeight Specification Height	  */
	public void setKJS_SpecHeight (BigDecimal KJS_SpecHeight)
	{
		set_Value (COLUMNNAME_KJS_SpecHeight, KJS_SpecHeight);
	}

	/** Get Specification Height.
		@return Specification Height	  */
	public BigDecimal getKJS_SpecHeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecHeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Height.
		@param KJS_SpecHeight_Max Specification Height	  */
	public void setKJS_SpecHeight_Max (BigDecimal KJS_SpecHeight_Max)
	{
		set_Value (COLUMNNAME_KJS_SpecHeight_Max, KJS_SpecHeight_Max);
	}

	/** Get Specification Height.
		@return Specification Height	  */
	public BigDecimal getKJS_SpecHeight_Max () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecHeight_Max);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Rim Height.
		@param KJS_SpecRimHeight Specification Rim Height	  */
	public void setKJS_SpecRimHeight (BigDecimal KJS_SpecRimHeight)
	{
		set_Value (COLUMNNAME_KJS_SpecRimHeight, KJS_SpecRimHeight);
	}

	/** Get Specification Rim Height.
		@return Specification Rim Height	  */
	public BigDecimal getKJS_SpecRimHeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecRimHeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Rim Height.
		@param KJS_SpecRimHeight_Max Specification Rim Height	  */
	public void setKJS_SpecRimHeight_Max (BigDecimal KJS_SpecRimHeight_Max)
	{
		set_Value (COLUMNNAME_KJS_SpecRimHeight_Max, KJS_SpecRimHeight_Max);
	}

	/** Get Specification Rim Height.
		@return Specification Rim Height	  */
	public BigDecimal getKJS_SpecRimHeight_Max () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecRimHeight_Max);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Top Diameter.
		@param KJS_SpecTopDiam Specification Top Diameter	  */
	public void setKJS_SpecTopDiam (BigDecimal KJS_SpecTopDiam)
	{
		set_Value (COLUMNNAME_KJS_SpecTopDiam, KJS_SpecTopDiam);
	}

	/** Get Specification Top Diameter.
		@return Specification Top Diameter	  */
	public BigDecimal getKJS_SpecTopDiam () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecTopDiam);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Top Diameter.
		@param KJS_SpecTopDiam_Max Specification Top Diameter	  */
	public void setKJS_SpecTopDiam_Max (BigDecimal KJS_SpecTopDiam_Max)
	{
		set_Value (COLUMNNAME_KJS_SpecTopDiam_Max, KJS_SpecTopDiam_Max);
	}

	/** Get Specification Top Diameter.
		@return Specification Top Diameter	  */
	public BigDecimal getKJS_SpecTopDiam_Max () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecTopDiam_Max);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Cup Weight.
		@param KJS_SpecWeight Specification Cup Weight	  */
	public void setKJS_SpecWeight (BigDecimal KJS_SpecWeight)
	{
		set_Value (COLUMNNAME_KJS_SpecWeight, KJS_SpecWeight);
	}

	/** Get Specification Cup Weight.
		@return Specification Cup Weight	  */
	public BigDecimal getKJS_SpecWeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecWeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Specification Cup Weight.
		@param KJS_SpecWeight_Max Specification Cup Weight	  */
	public void setKJS_SpecWeight_Max (BigDecimal KJS_SpecWeight_Max)
	{
		set_Value (COLUMNNAME_KJS_SpecWeight_Max, KJS_SpecWeight_Max);
	}

	/** Get Specification Cup Weight.
		@return Specification Cup Weight	  */
	public BigDecimal getKJS_SpecWeight_Max () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_KJS_SpecWeight_Max);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set QC Status.
		@param KJS_Status QC Status	  */
	public void setKJS_Status (String KJS_Status)
	{
		set_Value (COLUMNNAME_KJS_Status, KJS_Status);
	}

	/** Get QC Status.
		@return QC Status	  */
	public String getKJS_Status () 
	{
		return (String)get_Value(COLUMNNAME_KJS_Status);
	}

	/** Set Time.
		@param KJS_Time Time	  */
	public void setKJS_Time (Timestamp KJS_Time)
	{
		set_Value (COLUMNNAME_KJS_Time, KJS_Time);
	}

	/** Get Time.
		@return Time	  */
	public Timestamp getKJS_Time () 
	{
		return (Timestamp)get_Value(COLUMNNAME_KJS_Time);
	}

	/** Set LB_QHold.
		@param LB_QHold LB_QHold	  */
	public void setLB_QHold (BigDecimal LB_QHold)
	{
		set_Value (COLUMNNAME_LB_QHold, LB_QHold);
	}

	/** Get LB_QHold.
		@return LB_QHold	  */
	public BigDecimal getLB_QHold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LB_QHold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set LB_QPass.
		@param LB_QPass LB_QPass	  */
	public void setLB_QPass (BigDecimal LB_QPass)
	{
		set_Value (COLUMNNAME_LB_QPass, LB_QPass);
	}

	/** Get LB_QPass.
		@return LB_QPass	  */
	public BigDecimal getLB_QPass () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LB_QPass);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set LB_QPrd.
		@param LB_QPrd LB_QPrd	  */
	public void setLB_QPrd (BigDecimal LB_QPrd)
	{
		set_Value (COLUMNNAME_LB_QPrd, LB_QPrd);
	}

	/** Get LB_QPrd.
		@return LB_QPrd	  */
	public BigDecimal getLB_QPrd () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LB_QPrd);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set LB_QRej.
		@param LB_QRej LB_QRej	  */
	public void setLB_QRej (BigDecimal LB_QRej)
	{
		set_Value (COLUMNNAME_LB_QRej, LB_QRej);
	}

	/** Get LB_QRej.
		@return LB_QRej	  */
	public BigDecimal getLB_QRej () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LB_QRej);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set LB_QWaste.
		@param LB_QWaste LB_QWaste	  */
	public void setLB_QWaste (BigDecimal LB_QWaste)
	{
		set_Value (COLUMNNAME_LB_QWaste, LB_QWaste);
	}

	/** Get LB_QWaste.
		@return LB_QWaste	  */
	public BigDecimal getLB_QWaste () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LB_QWaste);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Leak_QHold.
		@param Leak_QHold Leak_QHold	  */
	public void setLeak_QHold (BigDecimal Leak_QHold)
	{
		set_Value (COLUMNNAME_Leak_QHold, Leak_QHold);
	}

	/** Get Leak_QHold.
		@return Leak_QHold	  */
	public BigDecimal getLeak_QHold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Leak_QHold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Leak_QPass.
		@param Leak_QPass Leak_QPass	  */
	public void setLeak_QPass (BigDecimal Leak_QPass)
	{
		set_Value (COLUMNNAME_Leak_QPass, Leak_QPass);
	}

	/** Get Leak_QPass.
		@return Leak_QPass	  */
	public BigDecimal getLeak_QPass () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Leak_QPass);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Leak_QPrd.
		@param Leak_QPrd Leak_QPrd	  */
	public void setLeak_QPrd (BigDecimal Leak_QPrd)
	{
		set_Value (COLUMNNAME_Leak_QPrd, Leak_QPrd);
	}

	/** Get Leak_QPrd.
		@return Leak_QPrd	  */
	public BigDecimal getLeak_QPrd () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Leak_QPrd);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Leak_QRej.
		@param Leak_QRej Leak_QRej	  */
	public void setLeak_QRej (BigDecimal Leak_QRej)
	{
		set_Value (COLUMNNAME_Leak_QRej, Leak_QRej);
	}

	/** Get Leak_QRej.
		@return Leak_QRej	  */
	public BigDecimal getLeak_QRej () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Leak_QRej);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Leak_QWaste.
		@param Leak_QWaste Leak_QWaste	  */
	public void setLeak_QWaste (BigDecimal Leak_QWaste)
	{
		set_Value (COLUMNNAME_Leak_QWaste, Leak_QWaste);
	}

	/** Get Leak_QWaste.
		@return Leak_QWaste	  */
	public BigDecimal getLeak_QWaste () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Leak_QWaste);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_M_Production getM_Production() throws RuntimeException
    {
		return (org.compiere.model.I_M_Production)MTable.get(getCtx(), org.compiere.model.I_M_Production.Table_Name)
			.getPO(getM_Production_ID(), get_TrxName());	}

	/** Set Production.
		@param M_Production_ID 
		Plan for producing a product
	  */
	public void setM_Production_ID (int M_Production_ID)
	{
		if (M_Production_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Production_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Production_ID, Integer.valueOf(M_Production_ID));
	}

	/** Get Production.
		@return Plan for producing a product
	  */
	public int getM_Production_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Production_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Paper_QHold.
		@param Paper_QHold Paper_QHold	  */
	public void setPaper_QHold (BigDecimal Paper_QHold)
	{
		set_Value (COLUMNNAME_Paper_QHold, Paper_QHold);
	}

	/** Get Paper_QHold.
		@return Paper_QHold	  */
	public BigDecimal getPaper_QHold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Paper_QHold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Paper_QPass.
		@param Paper_QPass Paper_QPass	  */
	public void setPaper_QPass (BigDecimal Paper_QPass)
	{
		set_Value (COLUMNNAME_Paper_QPass, Paper_QPass);
	}

	/** Get Paper_QPass.
		@return Paper_QPass	  */
	public BigDecimal getPaper_QPass () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Paper_QPass);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Paper_QPrd.
		@param Paper_QPrd Paper_QPrd	  */
	public void setPaper_QPrd (BigDecimal Paper_QPrd)
	{
		set_Value (COLUMNNAME_Paper_QPrd, Paper_QPrd);
	}

	/** Get Paper_QPrd.
		@return Paper_QPrd	  */
	public BigDecimal getPaper_QPrd () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Paper_QPrd);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Paper_QRej.
		@param Paper_QRej Paper_QRej	  */
	public void setPaper_QRej (BigDecimal Paper_QRej)
	{
		set_Value (COLUMNNAME_Paper_QRej, Paper_QRej);
	}

	/** Get Paper_QRej.
		@return Paper_QRej	  */
	public BigDecimal getPaper_QRej () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Paper_QRej);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Paper_QWaste.
		@param Paper_QWaste Paper_QWaste	  */
	public void setPaper_QWaste (BigDecimal Paper_QWaste)
	{
		set_Value (COLUMNNAME_Paper_QWaste, Paper_QWaste);
	}

	/** Get Paper_QWaste.
		@return Paper_QWaste	  */
	public BigDecimal getPaper_QWaste () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Paper_QWaste);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Rip_QHold.
		@param Rip_QHold Rip_QHold	  */
	public void setRip_QHold (BigDecimal Rip_QHold)
	{
		set_Value (COLUMNNAME_Rip_QHold, Rip_QHold);
	}

	/** Get Rip_QHold.
		@return Rip_QHold	  */
	public BigDecimal getRip_QHold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rip_QHold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Rip_QPass.
		@param Rip_QPass Rip_QPass	  */
	public void setRip_QPass (BigDecimal Rip_QPass)
	{
		set_Value (COLUMNNAME_Rip_QPass, Rip_QPass);
	}

	/** Get Rip_QPass.
		@return Rip_QPass	  */
	public BigDecimal getRip_QPass () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rip_QPass);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Rip_QPrd.
		@param Rip_QPrd Rip_QPrd	  */
	public void setRip_QPrd (BigDecimal Rip_QPrd)
	{
		set_Value (COLUMNNAME_Rip_QPrd, Rip_QPrd);
	}

	/** Get Rip_QPrd.
		@return Rip_QPrd	  */
	public BigDecimal getRip_QPrd () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rip_QPrd);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Rip_QRej.
		@param Rip_QRej Rip_QRej	  */
	public void setRip_QRej (BigDecimal Rip_QRej)
	{
		set_Value (COLUMNNAME_Rip_QRej, Rip_QRej);
	}

	/** Get Rip_QRej.
		@return Rip_QRej	  */
	public BigDecimal getRip_QRej () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rip_QRej);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Rip_QWaste.
		@param Rip_QWaste Rip_QWaste	  */
	public void setRip_QWaste (BigDecimal Rip_QWaste)
	{
		set_Value (COLUMNNAME_Rip_QWaste, Rip_QWaste);
	}

	/** Get Rip_QWaste.
		@return Rip_QWaste	  */
	public BigDecimal getRip_QWaste () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rip_QWaste);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sheet_QHold.
		@param Sheet_QHold Sheet_QHold	  */
	public void setSheet_QHold (BigDecimal Sheet_QHold)
	{
		set_Value (COLUMNNAME_Sheet_QHold, Sheet_QHold);
	}

	/** Get Sheet_QHold.
		@return Sheet_QHold	  */
	public BigDecimal getSheet_QHold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Sheet_QHold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sheet_QPass.
		@param Sheet_QPass Sheet_QPass	  */
	public void setSheet_QPass (BigDecimal Sheet_QPass)
	{
		set_Value (COLUMNNAME_Sheet_QPass, Sheet_QPass);
	}

	/** Get Sheet_QPass.
		@return Sheet_QPass	  */
	public BigDecimal getSheet_QPass () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Sheet_QPass);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sheet_QPrd.
		@param Sheet_QPrd Sheet_QPrd	  */
	public void setSheet_QPrd (BigDecimal Sheet_QPrd)
	{
		set_Value (COLUMNNAME_Sheet_QPrd, Sheet_QPrd);
	}

	/** Get Sheet_QPrd.
		@return Sheet_QPrd	  */
	public BigDecimal getSheet_QPrd () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Sheet_QPrd);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sheet_QRej.
		@param Sheet_QRej Sheet_QRej	  */
	public void setSheet_QRej (BigDecimal Sheet_QRej)
	{
		set_Value (COLUMNNAME_Sheet_QRej, Sheet_QRej);
	}

	/** Get Sheet_QRej.
		@return Sheet_QRej	  */
	public BigDecimal getSheet_QRej () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Sheet_QRej);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sheet_QWaste.
		@param Sheet_QWaste Sheet_QWaste	  */
	public void setSheet_QWaste (BigDecimal Sheet_QWaste)
	{
		set_Value (COLUMNNAME_Sheet_QWaste, Sheet_QWaste);
	}

	/** Get Sheet_QWaste.
		@return Sheet_QWaste	  */
	public BigDecimal getSheet_QWaste () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Sheet_QWaste);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Slit_QHold.
		@param Slit_QHold Slit_QHold	  */
	public void setSlit_QHold (BigDecimal Slit_QHold)
	{
		set_Value (COLUMNNAME_Slit_QHold, Slit_QHold);
	}

	/** Get Slit_QHold.
		@return Slit_QHold	  */
	public BigDecimal getSlit_QHold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Slit_QHold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Slit_QPass.
		@param Slit_QPass Slit_QPass	  */
	public void setSlit_QPass (BigDecimal Slit_QPass)
	{
		set_Value (COLUMNNAME_Slit_QPass, Slit_QPass);
	}

	/** Get Slit_QPass.
		@return Slit_QPass	  */
	public BigDecimal getSlit_QPass () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Slit_QPass);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Slit_QPrd.
		@param Slit_QPrd Slit_QPrd	  */
	public void setSlit_QPrd (BigDecimal Slit_QPrd)
	{
		set_Value (COLUMNNAME_Slit_QPrd, Slit_QPrd);
	}

	/** Get Slit_QPrd.
		@return Slit_QPrd	  */
	public BigDecimal getSlit_QPrd () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Slit_QPrd);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Slit_QRej.
		@param Slit_QRej Slit_QRej	  */
	public void setSlit_QRej (BigDecimal Slit_QRej)
	{
		set_Value (COLUMNNAME_Slit_QRej, Slit_QRej);
	}

	/** Get Slit_QRej.
		@return Slit_QRej	  */
	public BigDecimal getSlit_QRej () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Slit_QRej);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Slit_QWaste.
		@param Slit_QWaste Slit_QWaste	  */
	public void setSlit_QWaste (BigDecimal Slit_QWaste)
	{
		set_Value (COLUMNNAME_Slit_QWaste, Slit_QWaste);
	}

	/** Get Slit_QWaste.
		@return Slit_QWaste	  */
	public BigDecimal getSlit_QWaste () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Slit_QWaste);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Strip_QHold.
		@param Strip_QHold Strip_QHold	  */
	public void setStrip_QHold (BigDecimal Strip_QHold)
	{
		set_Value (COLUMNNAME_Strip_QHold, Strip_QHold);
	}

	/** Get Strip_QHold.
		@return Strip_QHold	  */
	public BigDecimal getStrip_QHold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Strip_QHold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Strip_QPass.
		@param Strip_QPass Strip_QPass	  */
	public void setStrip_QPass (BigDecimal Strip_QPass)
	{
		set_Value (COLUMNNAME_Strip_QPass, Strip_QPass);
	}

	/** Get Strip_QPass.
		@return Strip_QPass	  */
	public BigDecimal getStrip_QPass () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Strip_QPass);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Strip_QPrd.
		@param Strip_QPrd Strip_QPrd	  */
	public void setStrip_QPrd (BigDecimal Strip_QPrd)
	{
		set_Value (COLUMNNAME_Strip_QPrd, Strip_QPrd);
	}

	/** Get Strip_QPrd.
		@return Strip_QPrd	  */
	public BigDecimal getStrip_QPrd () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Strip_QPrd);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Strip_QRej.
		@param Strip_QRej Strip_QRej	  */
	public void setStrip_QRej (BigDecimal Strip_QRej)
	{
		set_Value (COLUMNNAME_Strip_QRej, Strip_QRej);
	}

	/** Get Strip_QRej.
		@return Strip_QRej	  */
	public BigDecimal getStrip_QRej () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Strip_QRej);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Strip_QWaste.
		@param Strip_QWaste Strip_QWaste	  */
	public void setStrip_QWaste (BigDecimal Strip_QWaste)
	{
		set_Value (COLUMNNAME_Strip_QWaste, Strip_QWaste);
	}

	/** Get Strip_QWaste.
		@return Strip_QWaste	  */
	public BigDecimal getStrip_QWaste () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Strip_QWaste);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}