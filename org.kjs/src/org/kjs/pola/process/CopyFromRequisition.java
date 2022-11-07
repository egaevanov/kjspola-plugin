package org.kjs.pola.process;

import org.compiere.model.MRequisition;
import org.kjs.pola.model.MRequisitionExt;
import org.compiere.process.ProcessInfoParameter;
import java.util.logging.Level;
import java.math.BigDecimal;
import org.compiere.process.SvrProcess;

public class CopyFromRequisition extends SvrProcess
{
    private int p_M_Requisition_ID;
    
    public CopyFromRequisition() {
        this.p_M_Requisition_ID = 0;
    }
    
    protected void prepare() {
        final ProcessInfoParameter[] para = this.getParameter();
        for (int i = 0; i < para.length; ++i) {
            final String name = para[i].getParameterName();
            if (para[i].getParameter() != null) {
                if (name.equals("M_Requisition_ID")) {
                    this.p_M_Requisition_ID = ((BigDecimal)para[i].getParameter()).intValue();
                }
                else {
                    this.log.log(Level.SEVERE, "Unknown Parameter: " + name);
                }
            }
        }
    }
    
    protected String doIt() throws Exception {
        final int To_M_Requisition_ID = this.getRecord_ID();
        if (this.log.isLoggable(Level.INFO)) {
            this.log.info("From M_Requisition_ID=" + this.p_M_Requisition_ID + " to " + To_M_Requisition_ID);
        }
        if (To_M_Requisition_ID == 0) {
            throw new IllegalArgumentException("Target M_Requisition_ID == 0");
        }
        if (this.p_M_Requisition_ID == 0) {
            throw new IllegalArgumentException("Source M_Requisition_ID == 0");
        }
        final MRequisitionExt from = new MRequisitionExt(this.getCtx(), this.p_M_Requisition_ID, this.get_TrxName());
        final MRequisitionExt to = new MRequisitionExt(this.getCtx(), To_M_Requisition_ID, this.get_TrxName());
        final int no = to.copyLinesFrom((MRequisition)from, false);
        return "@Copied@=" + no;
    }
}