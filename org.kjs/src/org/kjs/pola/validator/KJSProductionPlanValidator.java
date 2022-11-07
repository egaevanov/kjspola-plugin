package org.kjs.pola.validator;

import java.math.BigDecimal;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.kjs.pola.model.X_KJS_ProductionPlan;
import org.osgi.service.event.Event;

public class KJSProductionPlanValidator {
	
	
	
public static CLogger log = CLogger.getCLogger(KJSProductionPlanValidator.class);

	public static String executeKJSProductionPlan(Event event, PO po) {
		
		String msgProd = "";
		X_KJS_ProductionPlan KJSPP = (X_KJS_ProductionPlan) po;
		
		if (event.getTopic().equals(IEventTopics.PO_BEFORE_DELETE)) {
			msgProd = KJSProdPlanBeforeDelete(KJSPP);	
		}
		
	return msgProd;
	
	}
	
	
	public static String KJSProdPlanBeforeDelete(X_KJS_ProductionPlan KJSProdPlan) {
		
		String rslt = "";
		
		BigDecimal CurrentQtyJob = Env.ZERO;
		CurrentQtyJob = (BigDecimal) KJSProdPlan.get_Value("Qty");
		
		if(CurrentQtyJob == null) {
			CurrentQtyJob = Env.ZERO;
		}
		
	
		StringBuilder SQLUpdate = new StringBuilder();
		SQLUpdate.append("UPDATE M_RequisitionLine ");
		SQLUpdate.append(" SET QtyJob = QtyJob -"+CurrentQtyJob);
		SQLUpdate.append(" WHERE M_RequisitionLine_ID ="+KJSProdPlan.getM_RequisitionLine_ID());
		DB.executeUpdate(SQLUpdate.toString(), KJSProdPlan.get_TrxName());


		return rslt;
		
	}
	
}
