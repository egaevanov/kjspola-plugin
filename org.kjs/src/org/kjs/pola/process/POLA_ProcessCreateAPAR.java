package org.kjs.pola.process;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.compiere.model.MPayment;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class POLA_ProcessCreateAPAR extends SvrProcess{
	
	private int p_C_ChargeAdm_ID = 0;
	private int p_C_ChargePPN_ID = 0;
	private BigDecimal p_AdmAmt = Env.ZERO;
	private BigDecimal p_PPnAmt = Env.ZERO;
	private int p_C_Payment_ID = 0;
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_ChargeAdm_ID"))
				p_C_ChargeAdm_ID = para[i].getParameterAsInt();
			else if (name.equals("AdmAmt"))
				p_AdmAmt = para[i].getParameterAsBigDecimal();
			else if (name.equals("C_ChargePPN_ID"))
				p_C_ChargePPN_ID = para[i].getParameterAsInt();
			else if (name.equals("PPnAmt"))
				p_PPnAmt = para[i].getParameterAsBigDecimal();

			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		p_C_Payment_ID = getRecord_ID();
		
	}

	@Override
	protected String doIt() throws Exception {

		MPayment payment = new MPayment(getCtx(), p_C_Payment_ID, get_TrxName());
		int C_DocType_ID = 0;
		
		StringBuilder SQLGetPayDocType = new StringBuilder();
		SQLGetPayDocType.append("SELECT C_DocType_ID");
		SQLGetPayDocType.append(" FROM C_DocType");
		SQLGetPayDocType.append(" WHERE AD_Client_ID = "+Env.getAD_Client_ID(getCtx()));
		
		if(p_C_ChargePPN_ID > 0 && p_PPnAmt.compareTo(Env.ZERO)>0) {
			MPayment payPPh = new MPayment(getCtx(), 0, get_TrxName());
			payPPh.setAD_Org_ID(payment.getAD_Org_ID());
			if(payment.isReceipt()) {
				payPPh.setIsReceipt(false);
				
				SQLGetPayDocType.append(" AND DocBaseType = 'APP'");
				SQLGetPayDocType.append(" AND IsSoTrx = 'N' ");
				C_DocType_ID = DB.getSQLValueEx(get_TrxName(), SQLGetPayDocType.toString());
				
				payPPh.setC_DocType_ID(C_DocType_ID);
				payPPh.setDescription("Auto Generate From AR Payment :"+payment.getDocumentNo());

			}else {
				payPPh.setIsReceipt(true);
				SQLGetPayDocType.append(" AND DocBaseType = 'ARR'");
				SQLGetPayDocType.append(" AND IsSoTrx = 'Y' ");
				C_DocType_ID = DB.getSQLValueEx(get_TrxName(), SQLGetPayDocType.toString());
				payPPh.setC_DocType_ID(C_DocType_ID);
				payPPh.setDescription("Auto Generate From AP Payment :"+payment.getDocumentNo());

			}
			
			payPPh.setC_BPartner_ID(payment.getC_BPartner_ID());
			payPPh.setDateTrx(payment.getDateTrx());
			payPPh.setDateAcct(payment.getDateAcct());
			payPPh.setC_BankAccount_ID(payment.getC_BankAccount_ID());
			payPPh.setTenderType(MPayment.TENDERTYPE_Cash);
			payPPh.setPayAmt(p_PPnAmt);
			payPPh.setC_Currency_ID(payment.getC_Currency_ID());
			payPPh.setC_Charge_ID(p_C_ChargePPN_ID);
			payPPh.saveEx();			
		}
		
		
		if(p_C_ChargeAdm_ID > 0 && p_AdmAmt.compareTo(Env.ZERO)>0) {
			MPayment payAdm = new MPayment(getCtx(), 0, get_TrxName());
			payAdm.setAD_Org_ID(payment.getAD_Org_ID());
			if(payment.isReceipt()) {
				payAdm.setIsReceipt(false);
				
				SQLGetPayDocType.append(" AND DocBaseType = 'APP'");
				SQLGetPayDocType.append(" AND IsSoTrx = 'N' ");
				C_DocType_ID = DB.getSQLValueEx(get_TrxName(), SQLGetPayDocType.toString());
				
				payAdm.setC_DocType_ID(C_DocType_ID);
			}else {
				payAdm.setIsReceipt(true);
				SQLGetPayDocType.append(" AND DocBaseType = 'ARR'");
				SQLGetPayDocType.append(" AND IsSoTrx = 'Y' ");
				C_DocType_ID = DB.getSQLValueEx(get_TrxName(), SQLGetPayDocType.toString());
				payAdm.setC_DocType_ID(C_DocType_ID);
			}
			
			payAdm.setC_BPartner_ID(payment.getC_BPartner_ID());
			payAdm.setDescription("");
			payAdm.setDateTrx(payment.getDateTrx());
			payAdm.setDateAcct(payment.getDateAcct());
			payAdm.setC_BankAccount_ID(payment.getC_BankAccount_ID());
			payAdm.setTenderType(MPayment.TENDERTYPE_Cash);
			payAdm.setPayAmt(p_AdmAmt);
			payAdm.setC_Currency_ID(payment.getC_Currency_ID());
			payAdm.setC_Charge_ID(p_C_ChargeAdm_ID);
			payAdm.saveEx();			
		}
		
		return "";
	}

}
