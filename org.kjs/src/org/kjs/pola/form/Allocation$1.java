package org.kjs.pola.form;

import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.util.Env;
import org.kjs.pola.form.Allocation;

class Allocation$1 extends Thread {

   // $FF: synthetic field
   final Allocation this$0;


   Allocation$1(Allocation var1) {
      this.this$0 = var1;
   }

   public void run() {
      MPayment.setIsAllocated(Env.getCtx(), this.this$0.m_C_BPartner_ID, (String)null);
      MInvoice.setIsPaid(Env.getCtx(), this.this$0.m_C_BPartner_ID, (String)null);
   }
}