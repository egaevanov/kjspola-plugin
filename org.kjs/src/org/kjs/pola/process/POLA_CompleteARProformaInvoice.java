package org.kjs.pola.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.kjs.pola.model.X_C_ARProInv;

public class POLA_CompleteARProformaInvoice extends SvrProcess{
	
	private String p_Action = "";
	private int p_C_ARProInv_ID = 0;

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
		
		p_C_ARProInv_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		
		String rs = "";
		
		String DocStatus = "";
		
		if(p_C_ARProInv_ID <= 0) {
			return "";
		}
		
		X_C_ARProInv arpro = new X_C_ARProInv(getCtx(), p_C_ARProInv_ID, get_TrxName());
		DocStatus = arpro.getDocStatus();
		
		
		if(p_Action.equalsIgnoreCase("CO")) {
			 
			if(DocStatus.equalsIgnoreCase("CO")) {
				
				rs = "Cant Process Document - Document Already Completed";
			}
		}if(p_Action.equalsIgnoreCase("RE")) {
			
			if(DocStatus.equalsIgnoreCase("DR")) {				
				rs = "Cant Process Document - Document Already Active";
			}else if(DocStatus.equalsIgnoreCase("VO")) {
				rs = "Cant Process Document - Cant Re-Active Voided Document";
			}
			
		}
		
		if(p_Action.equalsIgnoreCase("CO")) {
			
			arpro.setProcessed(true);
			arpro.setDocStatus("CO");
			arpro.saveEx();
			
		}else if(p_Action.equalsIgnoreCase("VO")) {
			
			arpro.setProcessed(true);
			arpro.setDocStatus("VO");
			arpro.saveEx();
			
		}else if(p_Action.equalsIgnoreCase("RE")) {
			
			arpro.setProcessed(false);
			arpro.setDocStatus("DR");
			arpro.saveEx();
			
		}
		
		return rs;
	}

}
