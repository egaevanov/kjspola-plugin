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
import org.kjs.pola.model.X_C_ARProInv;
import org.kjs.pola.model.X_C_ARProInvLine;
import org.osgi.service.event.Event;

public class POLA_ProformaLineValidator {

	public static CLogger log = CLogger.getCLogger(POLA_QuotationLineValidator.class);

	public static String executeProformaEvent(Event event, PO po) {

		String msgQuo = "";
		X_C_ARProInvLine ProformaLine = (X_C_ARProInvLine) po;

		if (event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE)
				|| event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
			msgQuo = ProformaBeforeSave(ProformaLine);
		} else if (event.getTopic().equals(IEventTopics.PO_BEFORE_DELETE)) {
			msgQuo = ProformaBeforeDelete(ProformaLine);
		}

		return msgQuo;

	}

	public static String ProformaBeforeSave(X_C_ARProInvLine ProformaLine) {

		String rslt = "";

		BigDecimal taxBaseAmt = Env.ZERO;
		BigDecimal taxAmt = Env.ZERO;

		X_C_ARProInv proforma = new X_C_ARProInv(ProformaLine.getCtx(), ProformaLine.getC_ARProInv_ID(),ProformaLine.get_TrxName());
		MPriceList priceList = new MPriceList(ProformaLine.getCtx(), proforma.getM_PriceList_ID(),ProformaLine.get_TrxName());

		int C_Tax_ID = ProformaLine.getC_Tax_ID();
		MTax tax = new MTax(ProformaLine.getCtx(), C_Tax_ID, ProformaLine.get_TrxName());
		//
		String sql = "SELECT LineNetAmt FROM C_ARProInvLine WHERE C_ARProInv_ID=? AND C_Tax_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, ProformaLine.get_TrxName());
			pstmt.setInt(1, ProformaLine.getC_ARProInv_ID());
			pstmt.setInt(2, ProformaLine.getC_Tax_ID());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BigDecimal baseAmt = rs.getBigDecimal(1);
				taxBaseAmt = taxBaseAmt.add(baseAmt);

				// calculate line tax
				taxAmt = taxAmt.add(tax.calculateTax(baseAmt, priceList.isTaxIncluded(), priceList.getPricePrecision()));
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, ProformaLine.get_TrxName(), e);
			taxBaseAmt = null;
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//
		if (taxBaseAmt == null)
			return "";

		// Calculate Tax
		taxAmt = tax.calculateTax(taxBaseAmt, priceList.isTaxIncluded(), priceList.getPricePrecision());
		StringBuilder updateTax = new StringBuilder();
		updateTax.append("UPDATE C_ARProInv");
		updateTax.append(" SET TaxAmt = " + taxAmt);

		// Set Base
		if (priceList.isTaxIncluded()) {

			updateTax.append(",TaxBaseAmt = " + taxBaseAmt.subtract(taxAmt));
			updateTax.append(",TotalLines = " + taxBaseAmt);
			updateTax.append(",GrandTotal = " + taxBaseAmt);

		} else {

			updateTax.append(",TaxBaseAmt = " + taxBaseAmt);
			updateTax.append(",TotalLines = " + taxBaseAmt);
			updateTax.append(",GrandTotal = " + taxBaseAmt.add(taxAmt));

		}

		DB.executeUpdate(updateTax.toString(), null);

		return rslt;

	}

	public static String ProformaBeforeDelete(X_C_ARProInvLine ProformaLine) {

		String rslt = "";

		BigDecimal taxBaseAmt = Env.ZERO;
		BigDecimal taxAmt = Env.ZERO;

		X_C_ARProInv proforma = new X_C_ARProInv(ProformaLine.getCtx(), ProformaLine.getC_ARProInv_ID(),ProformaLine.get_TrxName());
		MPriceList priceList = new MPriceList(ProformaLine.getCtx(), proforma.getM_PriceList_ID(),ProformaLine.get_TrxName());

		int C_Tax_ID = ProformaLine.getC_Tax_ID();
		MTax tax = new MTax(ProformaLine.getCtx(), C_Tax_ID, ProformaLine.get_TrxName());
		
		String sql = "SELECT LineNetAmt FROM C_ARProInvLine WHERE C_ARProInv_ID=? AND C_Tax_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, ProformaLine.get_TrxName());
			pstmt.setInt(1, ProformaLine.getC_ARProInv_ID());
			pstmt.setInt(2, ProformaLine.getC_Tax_ID());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BigDecimal baseAmt = rs.getBigDecimal(1);
				taxBaseAmt = taxBaseAmt.add(baseAmt);

				// calculate line tax
				taxAmt = taxAmt.add(tax.calculateTax(baseAmt, priceList.isTaxIncluded(), priceList.getPricePrecision()));
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, ProformaLine.get_TrxName(), e);
			taxBaseAmt = null;
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//
		if (taxBaseAmt == null)
			return "";
		
		
		// Calculate Tax
		taxAmt = tax.calculateTax(taxBaseAmt, priceList.isTaxIncluded(), priceList.getPricePrecision());
		StringBuilder updateTax = new StringBuilder();
		updateTax.append("UPDATE C_ARProInv");
		updateTax.append(" SET TaxAmt = TaxAmt-" + taxAmt);

		// Set Base
		if (priceList.isTaxIncluded()) {

			updateTax.append(",TaxBaseAmt = TaxBaseAmt-" + taxBaseAmt.subtract(taxAmt));
			updateTax.append(",TotalLines = TotalLines-" + taxBaseAmt);
			updateTax.append(",GrandTotal = GrandTotal-" + taxBaseAmt);

		} else {

			updateTax.append(",TaxBaseAmt = TaxBaseAmt-" + taxBaseAmt);
			updateTax.append(",TotalLines = TotalLines-" + taxBaseAmt);
			updateTax.append(",GrandTotal = GrandTotal-" + taxBaseAmt.add(taxAmt));

		}

		DB.executeUpdate(updateTax.toString(), null);

		return rslt;

	}

}
