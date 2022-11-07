package org.kjs.pola.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.kjs.pola.model.X_I_KJS_ProductionPlan;
import org.kjs.pola.model.X_KJS_ProductionPlan;
import org.kjs.pola.model.X_KJS_ProductionPlanLine;

public class POLA_Process_Import_JOB extends SvrProcess {

	/** Delete old Imported */
	private boolean p_deleteOldImported = false;

	/** Effective */
	private Timestamp p_DateValue = null;

	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (name.equals("DeleteOldImported"))
				p_deleteOldImported = para[i].getParameterAsBoolean();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

		if (p_DateValue == null)
			p_DateValue = new Timestamp(System.currentTimeMillis());

	}

	@Override
	protected String doIt() throws Exception {

		try {

			int no = 0;

			String ErrorMsg = "";

			ArrayList<Integer> jobList_ID = new ArrayList<Integer>();

			if (p_deleteOldImported) {

				StringBuilder delete = new StringBuilder();
				delete.append("DELETE FROM I_KJS_ProductionPLan");
				delete.append(" WHERE AD_Client_ID =" + Env.getAD_Client_ID(getCtx()));
				delete.append(" AND I_IsImported ='Y'");

				no = DB.executeUpdate(delete.toString(), get_TrxName());

			}

			// Select All document
			StringBuilder QueryAllDocument = new StringBuilder();
			QueryAllDocument.append("SELECT I_KJS_ProductionPLan_ID");
			QueryAllDocument.append(" FROM I_KJS_PRODUCTIONPLAN");
			QueryAllDocument.append(" WHERE AD_Client_ID =" + Env.getAD_Client_ID(getCtx()));
			QueryAllDocument.append(" AND I_IsImported ='N'");
			QueryAllDocument.append(" ORDER BY DocumentNo,Line ASC");

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = (PreparedStatement) DB.prepareStatement(QueryAllDocument.toString(), (String) null);

				rs = pstmt.executeQuery();
				while (rs.next()) {

					jobList_ID.add(rs.getInt(1));

				}
			} catch (SQLException err) {
				this.log.log(Level.SEVERE, QueryAllDocument.toString(), (Throwable) err);
				rollback();
			} finally {
				DB.close(rs, (Statement) pstmt);
				rs = null;
				pstmt = null;
			}

			// loop document
			for (int i = 0; i < jobList_ID.size(); i++) {
				ErrorMsg = "";
				boolean readyProcess = true;
				X_I_KJS_ProductionPlan I_JOB = new X_I_KJS_ProductionPlan(getCtx(), jobList_ID.get(i), get_TrxName());
				X_I_KJS_ProductionPlan I_JOB_Before = null;
				if (i > 0) {

					I_JOB_Before = new X_I_KJS_ProductionPlan(getCtx(), jobList_ID.get(i - 1), get_TrxName());

				}
				int KJS_ProductionPlan_ID = 0;
				// check
				if (I_JOB.getDocumentNo() == null || I_JOB.getDocumentNo().isEmpty() || I_JOB.getDocumentNo() == "") {
					ErrorMsg = ErrorMsg + "\n DocumentNo Is Empty";
					readyProcess = false;
				}

				if (I_JOB.getM_Product_ID() <= 0) {
					System.out.println(I_JOB.getM_Product_ID());
					ErrorMsg = ErrorMsg + "\n Product Is Empty";
					readyProcess = false;
				}

				if (I_JOB.getProductBOM_ID() <= 0) {
					ErrorMsg = ErrorMsg + "\n Product BOM Is Empty";
					readyProcess = false;
				}

				if (I_JOB.getKJS_ProductAsset_ID() <= 0) {
					ErrorMsg = ErrorMsg + "\n Product Asset Is Empty";
					readyProcess = false;
				}

				if (I_JOB.getM_Alternate_ID() <= 0) {
					ErrorMsg = ErrorMsg + "\n Alternate Is Empty";
					readyProcess = false;
				}

				if (I_JOB.getKJS_Phase_ID() <= 0) {
					ErrorMsg = ErrorMsg + "\n Phase Is Empty";
					readyProcess = false;
				}

				I_JOB.set_CustomColumn("I_ErrorMsg", ErrorMsg);
				I_JOB.set_CustomColumnReturningBoolean("I_IsImported", false);
				I_JOB.saveEx();

				if (!readyProcess)
					continue;

				StringBuilder SQLGetJOB = new StringBuilder();
				SQLGetJOB.append("SELECT KJS_ProductionPLan_ID");
				SQLGetJOB.append(" FROM KJS_ProductionPLan");
				SQLGetJOB.append(" WHERE AD_Client_ID = " + Env.getAD_Client_ID(getCtx()));
				SQLGetJOB.append(" AND DocumentNo= '" + I_JOB.getDocumentNo() + "'");

				KJS_ProductionPlan_ID = DB.getSQLValueEx(get_TrxName(), SQLGetJOB.toString());

				if (KJS_ProductionPlan_ID < 0)
					KJS_ProductionPlan_ID = 0;

				if (KJS_ProductionPlan_ID == 0) {

					if (I_JOB_Before != null) {

						if (I_JOB_Before.getDocumentNo().equalsIgnoreCase(I_JOB.getDocumentNo())) {

							KJS_ProductionPlan_ID = I_JOB_Before.getKJS_ProductionPlan_ID();

						}

					} else {
						KJS_ProductionPlan_ID = 0;
					}
				}

				X_KJS_ProductionPlan JOB = new X_KJS_ProductionPlan(getCtx(), KJS_ProductionPlan_ID, get_TrxName());

				if (KJS_ProductionPlan_ID > 0) {

					X_KJS_ProductionPlanLine JOBLine = new X_KJS_ProductionPlanLine(getCtx(), 0, get_TrxName());
					JOBLine.setKJS_ProductionPlan_ID(KJS_ProductionPlan_ID);
					JOBLine.setAD_Org_ID(I_JOB.getAD_Org_ID());
					JOBLine.setLine(I_JOB.getLine());
					JOBLine.setM_Product_ID(I_JOB.getProductBOM_ID());
					JOBLine.setBOMQty(I_JOB.getBOMQty());
					JOBLine.set_ValueOfColumn("M_Alternate_ID", I_JOB.getM_AlternateBOM_ID());
					JOBLine.setKJS_ProductAsset_ID(I_JOB.getKJS_ProductAsset_ID());
					JOBLine.setM_Locator_ID(I_JOB.getM_Locator_ID());
					JOBLine.setKJS_Phase_ID(I_JOB.getKJS_Phase_ID());
					JOBLine.saveEx();

					I_JOB.setKJS_ProductionPlan_ID(JOB.getKJS_ProductionPlan_ID());
					I_JOB.setKJS_ProductionPlanLine_ID(JOBLine.getKJS_ProductionPlanLine_ID());
					I_JOB.setProcessed(true);
					I_JOB.set_CustomColumnReturningBoolean("I_IsImported", true);
					I_JOB.saveEx();

				} else {

					JOB.setAD_Org_ID(I_JOB.getAD_Org_ID());
					JOB.set_CustomColumn("C_DocType_ID", I_JOB.getC_DocType_ID());
					JOB.setDocumentNo(I_JOB.getDocumentNo());
					JOB.setDateDoc(I_JOB.getDateDoc());
					JOB.setM_Product_ID(I_JOB.getM_Product_ID());
					JOB.setQtyEntered(I_JOB.getQtyEntered());
					JOB.set_ValueOfColumn("Qty", I_JOB.getQty());
					JOB.set_ValueOfColumn("M_Alternate_ID", I_JOB.getM_Alternate_ID());
					JOB.set_ValueOfColumn("KJS_CavityRollKertas", I_JOB.getKJS_CavityRollKertas());
					JOB.set_ValueOfColumn("KJS_CavityGearFeed", I_JOB.getKJS_CavityGearFeed());
					JOB.set_ValueOfColumn("KJS_CavityUkuran", I_JOB.getKJS_CavityUkuran());
					JOB.set_ValueOfColumn("KJS_CavityJumlah", I_JOB.getKJS_CavityJumlah());
					JOB.set_ValueOfColumn("KJS_Cavity", I_JOB.getKJS_Cavity());
					JOB.saveEx();

					X_KJS_ProductionPlanLine JOBLine = new X_KJS_ProductionPlanLine(getCtx(), 0, get_TrxName());
					JOBLine.setKJS_ProductionPlan_ID(JOB.getKJS_ProductionPlan_ID());
					JOBLine.setAD_Org_ID(I_JOB.getAD_Org_ID());
					JOBLine.setLine(I_JOB.getLine());
					JOBLine.setM_Product_ID(I_JOB.getProductBOM_ID());
					JOBLine.setBOMQty(I_JOB.getBOMQty());
					JOBLine.set_ValueOfColumn("M_Alternate_ID", I_JOB.getM_AlternateBOM_ID());
					JOBLine.setKJS_ProductAsset_ID(I_JOB.getKJS_ProductAsset_ID());
					JOBLine.setM_Locator_ID(I_JOB.getM_Locator_ID());
					JOBLine.setKJS_Phase_ID(I_JOB.getKJS_Phase_ID());
					JOBLine.saveEx();

					I_JOB.setKJS_ProductionPlan_ID(JOB.getKJS_ProductionPlan_ID());
					I_JOB.setKJS_ProductionPlanLine_ID(JOBLine.getKJS_ProductionPlanLine_ID());
					I_JOB.setProcessed(true);
					I_JOB.set_CustomColumnReturningBoolean("I_IsImported", true);
					I_JOB.saveEx();

				}

			}

		} catch (Exception e) {
			rollback();
		}

		return "";
	}

}
