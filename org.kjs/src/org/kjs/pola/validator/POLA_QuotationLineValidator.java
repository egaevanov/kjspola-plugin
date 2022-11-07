package org.kjs.pola.validator;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MPriceList;
import org.compiere.model.MTax;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.kjs.pola.model.X_C_Quotation;
import org.kjs.pola.model.X_C_QuotationLine;
import org.osgi.service.event.Event;

public class POLA_QuotationLineValidator {

	public static CLogger log = CLogger.getCLogger(POLA_QuotationLineValidator.class);

	public static String executeQuotationEvent(Event event, PO po) {
		
		String msgQuo = "";
		X_C_QuotationLine quoLine = (X_C_QuotationLine) po;
		
		if (event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE)||event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
			msgQuo = QuotationBeforeSave(quoLine);	
		}else if (event.getTopic().equals(IEventTopics.PO_BEFORE_DELETE)) {
			msgQuo = QuotationBeforeDelete(quoLine);	
		}
		
	return msgQuo;
	
	}
	
	
	public static String QuotationBeforeSave(X_C_QuotationLine quoLine) {
		
		String rslt = "";
		
		BigDecimal taxBaseAmt = Env.ZERO;
		BigDecimal taxAmt = Env.ZERO;
		
		X_C_Quotation quo = new X_C_Quotation(quoLine.getCtx(), quoLine.getC_Quotation_ID(), quoLine.get_TrxName());
		MPriceList priceList = new MPriceList(quoLine.getCtx(), quo.getM_PriceList_ID(), quoLine.get_TrxName());
		
		int C_Tax_ID = quoLine.getC_Tax_ID();
		MTax tax = new MTax(quoLine.getCtx(), C_Tax_ID, quoLine.get_TrxName());
		//
		String sql = "SELECT LineNetAmt FROM C_QuotationLine WHERE C_Quotation_ID=? AND C_Tax_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, quoLine.get_TrxName());
			pstmt.setInt (1, quoLine.getC_Quotation_ID());
			pstmt.setInt (2, quoLine.getC_Tax_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				BigDecimal baseAmt = rs.getBigDecimal(1);
				taxBaseAmt = taxBaseAmt.add(baseAmt);
				
				// calculate line tax
//				taxAmt = taxAmt.add(tax.calculateTax(baseAmt, priceList.isTaxIncluded(), priceList.getPricePrecision()));
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, quoLine.get_TrxName(), e);
			taxBaseAmt = null;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//
		if (taxBaseAmt == null)
			return "";
		
		//	Calculate Tax
		taxAmt = tax.calculateTax(taxBaseAmt,priceList.isTaxIncluded(), priceList.getPricePrecision());
		StringBuilder updateTax = new StringBuilder();
		updateTax.append("UPDATE C_Quotation");
		updateTax.append(" SET TaxAmt = "+taxAmt);
		
		//	Set Base
		if (priceList.isTaxIncluded()) {
			
			updateTax.append(",TaxBaseAmt = "+taxBaseAmt.subtract(taxAmt));
			updateTax.append(",TotalLines = "+taxBaseAmt);
			updateTax.append(",GrandTotal = "+taxBaseAmt);

		}else {
			
			updateTax.append(",TaxBaseAmt = "+taxBaseAmt);
			updateTax.append(",TotalLines = "+taxBaseAmt);
			updateTax.append(",GrandTotal = "+taxBaseAmt.add(taxAmt));

		}
		
		DB.executeUpdate(updateTax.toString(), null);
		
		return rslt;
		
	
		
	}
	
	public static String QuotationBeforeDelete(X_C_QuotationLine quoLine) {
		
		String rslt = "";
		
		BigDecimal taxBaseAmt = Env.ZERO;
		BigDecimal taxAmt = Env.ZERO;
		
		X_C_Quotation quo = new X_C_Quotation(quoLine.getCtx(), quoLine.getC_Quotation_ID(), quoLine.get_TrxName());
		MPriceList priceList = new MPriceList(quoLine.getCtx(), quo.getM_PriceList_ID(), quoLine.get_TrxName());
		
		int C_Tax_ID = quoLine.getC_Tax_ID();
		MTax tax = new MTax(quoLine.getCtx(), C_Tax_ID, quoLine.get_TrxName());
		//
		String sql = "SELECT LineNetAmt FROM C_QuotationLine WHERE C_Quotation_ID=? AND C_Tax_ID=? AND C_QuotationLine_ID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, quoLine.get_TrxName());
			pstmt.setInt (1, quoLine.getC_Quotation_ID());
			pstmt.setInt (2, quoLine.getC_Tax_ID());
			pstmt.setInt (3, quoLine.getC_QuotationLine_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				BigDecimal baseAmt = rs.getBigDecimal(1);
				taxBaseAmt = taxBaseAmt.add(baseAmt);
				
				// calculate line tax
//				taxAmt = taxAmt.add(tax.calculateTax(baseAmt, priceList.isTaxIncluded(), priceList.getPricePrecision()));
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, quoLine.get_TrxName(), e);
			taxBaseAmt = null;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//
		if (taxBaseAmt == null)
			return "";
		
		//	Calculate Tax
		taxAmt = tax.calculateTax(taxBaseAmt,priceList.isTaxIncluded(), priceList.getPricePrecision());
		StringBuilder updateTax = new StringBuilder();
		updateTax.append("UPDATE C_Quotation");
		updateTax.append(" SET TaxAmt = TaxAmt-"+taxAmt);
		
		//	Set Base
		if (priceList.isTaxIncluded()) {
			
			updateTax.append(",TaxBaseAmt = TaxBaseAmt-"+taxBaseAmt.subtract(taxAmt));
			updateTax.append(",TotalLines = TotalLines-"+taxBaseAmt);
			updateTax.append(",GrandTotal = GrandTotal-"+taxBaseAmt);

		}else {
			
			updateTax.append(",TaxBaseAmt = TaxBaseAmt-"+taxBaseAmt);
			updateTax.append(",TotalLines = TotalLines-"+taxBaseAmt);
			updateTax.append(",GrandTotal = GrandTotal-"+taxBaseAmt.add(taxAmt));

		}
		
		DB.executeUpdate(updateTax.toString(), null);
		
		return rslt;
		
	}
	
	
}
