package org.kjs.pola.process;

import java.util.logging.Level;

import org.compiere.model.MOrder;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.kjs.pola.model.X_C_Contract;

public class POLA_Process_CompleteContract extends SvrProcess {

	private String p_Action = "";
	private int p_C_Contract_ID = 0;
	private String p_DocStatus = "";

	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("Action"))
				p_Action = para[i].getParameterAsString();

			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_Contract_ID = getRecord_ID();

	}

	@Override
	protected String doIt() throws Exception {

		if (p_C_Contract_ID <= 0)
			return "";

		X_C_Contract contract = new X_C_Contract(getCtx(), p_C_Contract_ID, get_TrxName());
		p_DocStatus = contract.getDocStatus();

		StringBuilder SQLCheckLine = new StringBuilder();
		SQLCheckLine.append("SELECT Count(C_ContractLine_ID)");
		SQLCheckLine.append(" FROM C_ContractLine ");
		SQLCheckLine.append(" WHERE AD_Client_ID = ?");
		SQLCheckLine.append(" AND C_Contract_ID = ?");

		int jmlhLine = DB.getSQLValueEx(get_TrxName(), SQLCheckLine.toString(),
				new Object[] { contract.getAD_Client_ID(), contract.getC_Contract_ID() });

		if (p_Action.toUpperCase().equals("CO")) {

			if (p_DocStatus.toUpperCase().equals("DR") || p_DocStatus.toUpperCase().equals("IP")) {

				if (jmlhLine <= 0)
					return "Process Complete Failed - Contract No Have Details";

				contract.setDocStatus("CO");
				contract.setProcessed(true);
				contract.saveEx();
			}

		} else if (p_Action.toUpperCase().equals("VO")) {

			if (p_DocStatus.toUpperCase().equals("CO") || p_DocStatus.toUpperCase().equals("IP")) {

				StringBuilder SQLCheckHaveSO = new StringBuilder();
				SQLCheckHaveSO.append("SELECT C_Order_ID");
				SQLCheckHaveSO.append(" FROM C_Order ");
				SQLCheckHaveSO.append(" WHERE AD_Client_ID = ?");
				SQLCheckHaveSO.append(" AND C_Contract_ID = ?");

				int C_Order_ID = DB.getSQLValueEx(get_TrxName(), SQLCheckHaveSO.toString(),
						new Object[] { contract.getAD_Client_ID(), contract.getC_Contract_ID() });

				if (C_Order_ID > 0) {
					MOrder Ord = new MOrder(getCtx(), C_Order_ID, get_TrxName());
					return "Process Void Failed - Contract Already Generated to Sales Order Document "
							+ Ord.getDocumentNo();
				}

				contract.setDocStatus("VO");
				contract.setProcessed(true);
				contract.saveEx();
			}

		} 
//		else if (p_Action.toUpperCase().equals("RE")) {
//
//			if (p_DocStatus.toUpperCase().equals("CO")) {
//
//				StringBuilder SQLCheckHaveSO = new StringBuilder();
//				SQLCheckHaveSO.append("SELECT C_Order_ID");
//				SQLCheckHaveSO.append(" FROM C_Order ");
//				SQLCheckHaveSO.append(" WHERE AD_Client_ID = ?");
//				SQLCheckHaveSO.append(" AND C_Quotation_ID = ?");
//
//				int C_Order_ID = DB.getSQLValueEx(get_TrxName(), SQLCheckHaveSO.toString(),
//						new Object[] { contract.getAD_Client_ID(), contract.getC_Contract_ID() });
//
//				if (C_Order_ID > 0) {
//					MOrder Ord = new MOrder(getCtx(), C_Order_ID, get_TrxName());
//					return "Process Re-Active Failed - Contract Already Generated to Sales Order Document "
//							+ Ord.getDocumentNo();
//				}
//
//				contract.setDocStatus("IP");
//				contract.setProcessed(false);
//				contract.saveEx();
//
//			}
//
//		}

		return "";
	}

}
