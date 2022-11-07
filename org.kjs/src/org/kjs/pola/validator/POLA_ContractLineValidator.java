package org.kjs.pola.validator;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.kjs.pola.model.X_C_ContractLine;
import org.osgi.service.event.Event;

public class POLA_ContractLineValidator {
	
	public static CLogger log = CLogger.getCLogger(POLA_ContractLineValidator.class);

	public static String executeContractEvent(Event event, PO po) {
		
		String msgContract = "";
		X_C_ContractLine contractLine = (X_C_ContractLine) po;
		
		if (event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE)||event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
			msgContract = ContractBeforeSave(contractLine);	
		}else if (event.getTopic().equals(IEventTopics.PO_BEFORE_DELETE)) {
			msgContract = ContractBeforeDelete(contractLine);	
		}
		
	return msgContract;
	
	}

	
	
public static String ContractBeforeSave(X_C_ContractLine contLine) {
		
		String rslt = "";
		
		BigDecimal taxBaseAmt = Env.ZERO;
		String Trx_Name = contLine.get_TrxName();
				
		//
		String sql = "SELECT LineNetAmt FROM C_ContractLine WHERE C_Contract_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, Trx_Name);
			pstmt.setInt (1, contLine.getC_Contract_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				BigDecimal baseAmt = rs.getBigDecimal(1);
				taxBaseAmt = taxBaseAmt.add(baseAmt);
				
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, Trx_Name, e);
			taxBaseAmt = null;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		if (taxBaseAmt == null)
			return "";
		
	
		StringBuilder updateGrandTotal = new StringBuilder();
		updateGrandTotal.append("UPDATE C_Contract");
		updateGrandTotal.append(" SET GrandTotal = "+taxBaseAmt);
		updateGrandTotal.append(" ,TotalLines = "+taxBaseAmt);

		DB.executeUpdate(updateGrandTotal.toString(), null);
		
		return rslt;
		
	
		
	}
	
	public static String ContractBeforeDelete(X_C_ContractLine contLine) {
		
		String rslt = "";
		
		BigDecimal taxBaseAmt = Env.ZERO;
		String Trx_Name = contLine.get_TrxName();

		String sql = "SELECT LineNetAmt FROM C_ContractLine WHERE C_Contract_ID=? AND C_ContractLine_ID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, Trx_Name);
			pstmt.setInt (1, contLine.getC_Contract_ID());
			pstmt.setInt (2, contLine.getC_ContractLine_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				BigDecimal baseAmt = rs.getBigDecimal(1);
				taxBaseAmt = taxBaseAmt.add(baseAmt);
				
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, Trx_Name, e);
			taxBaseAmt = null;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		if (taxBaseAmt == null)
			return "";
		
		StringBuilder updateGrandTotal = new StringBuilder();
		updateGrandTotal.append("UPDATE C_Contract");
		updateGrandTotal.append(" SET GrandTotal = GrandTotal-"+taxBaseAmt);	
		updateGrandTotal.append(" ,TotalLines = TotalLines-"+taxBaseAmt);	

		DB.executeUpdate(updateGrandTotal.toString(), null);
		
		return rslt;
		
	}
}
