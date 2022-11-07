package org.kjs.pola.form;

import org.compiere.model.MAllocationHdr;
import org.compiere.util.TrxRunnable;
import org.kjs.pola.form.WAllocation;

class WAllocation$1 implements TrxRunnable {

   // $FF: synthetic field
   final WAllocation this$0;
   // $FF: synthetic field
   private final MAllocationHdr[] val$allocation;


   WAllocation$1(WAllocation var1, MAllocationHdr[] var2) {
      this.this$0 = var1;
      this.val$allocation = var2;
   }

   public void run(String trxName) {
      WAllocation.access$0(this.this$0).getChildren().clear();
      this.val$allocation[0] = this.this$0.saveData(WAllocation.access$1(this.this$0).getWindowNo(), WAllocation.access$2(this.this$0).getValue(), WAllocation.access$3(this.this$0), WAllocation.access$4(this.this$0), trxName);
   }
}