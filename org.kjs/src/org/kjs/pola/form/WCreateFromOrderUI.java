package org.kjs.pola.form;

import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.ClientInfo;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.apps.form.WCreateFromWindow;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.model.GridTab;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Vlayout;

public class WCreateFromOrderUI extends CreateFromOrder implements EventListener<Event>, ValueChangeListener {

//	private WCreateFromWindow window;
//	private int p_WindowNo;

	public WCreateFromOrderUI(GridTab gridTab) {
		super(gridTab);
		log.info(getGridTab().toString());

		window = new WCreateFromWindow(this, getGridTab().getWindowNo());

		p_WindowNo = getGridTab().getWindowNo();

		try {
			if (!dynInit())
				return;
			zkInit();
			// setInitOK(true);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
			setInitOK(false);
			throw new AdempiereException(e.getMessage());
		}
		AEnv.showWindow(window);
	}

	@Override
	public void onEvent(Event event) throws Exception {

		if (m_actionActive)
			return;
		m_actionActive = true;
		// TODO Auto-generated method stub

	}

	protected Label bPartnerLabel = new Label();
	protected WEditor bPartnerField;

	protected Label orderLabel = new Label();
	protected WEditor orderField;

	protected Label prLabel = new Label();
	protected WEditor prField;

	private Grid parameterStdLayout;

//	private int noOfParameterColumn;

	public boolean dynInit() throws Exception {
		log.config("");

		super.dynInit();

		window.setTitle(getTitle());

		initBPartner();
		initRequisition();
		initOrder();
		bPartnerField.addValueChangeListener((ValueChangeListener) this);
		prField.addValueChangeListener((ValueChangeListener) this);

		return true;
	} // dynInit

	protected void zkInit() throws Exception {

		bPartnerLabel.setText(Msg.getElement(Env.getCtx(), "C_BPartner_ID"));
		orderLabel.setText(Msg.getElement(Env.getCtx(), "C_Order_ID", false));
		prLabel.setText(Msg.getElement(Env.getCtx(), "M_Requisition_ID", false));

		Vlayout vlayout = new Vlayout();
		ZKUpdateUtil.setVflex(vlayout, "min");
		ZKUpdateUtil.setWidth(vlayout, "100%");
		Panel parameterPanel = window.getParameterPanel();
		parameterPanel.appendChild(vlayout);

		parameterStdLayout = GridFactory.newGridLayout();
		vlayout.appendChild(parameterStdLayout);
		ZKUpdateUtil.setVflex(vlayout, "parameterStdLayout");

		setupColumns(parameterStdLayout);

		Rows rows = (Rows) parameterStdLayout.newRows();
		Row row = rows.newRow();

		row.appendChild(orderLabel.rightAlign());
		row.appendChild(orderField.getComponent());
		orderField.setReadWrite(false);

		row.appendChild(bPartnerLabel.rightAlign());
		row.appendChild(bPartnerField.getComponent());
		bPartnerField.fillHorizontal();
		bPartnerField.setReadWrite(false);

		row = rows.newRow();
		row.appendChild(prLabel.rightAlign());
		row.appendChild(prField.getComponent());
		prField.setReadWrite(true);

	}

	private boolean m_actionActive = false;

	protected void setupColumns(Grid parameterGrid) {
//		noOfParameterColumn = ClientInfo.maxWidth((ClientInfo.EXTRA_SMALL_WIDTH+ClientInfo.SMALL_WIDTH)/2) ? 2 : 4;

		Columns columns = new Columns();
		parameterGrid.appendChild(columns);
		if (ClientInfo.maxWidth((ClientInfo.EXTRA_SMALL_WIDTH + ClientInfo.SMALL_WIDTH) / 2)) {
			Column column = new Column();
			ZKUpdateUtil.setWidth(column, "35%");
			columns.appendChild(column);
			column = new Column();
			ZKUpdateUtil.setWidth(column, "65%");
			columns.appendChild(column);
		} else {
			Column column = new Column();
			columns.appendChild(column);
			column = new Column();
			ZKUpdateUtil.setWidth(column, "15%");
			columns.appendChild(column);
			ZKUpdateUtil.setWidth(column, "35%");
			column = new Column();
			ZKUpdateUtil.setWidth(column, "15%");
			columns.appendChild(column);
			column = new Column();
			ZKUpdateUtil.setWidth(column, "35%");
			columns.appendChild(column);
		}
	}

	public void showWindow() {
		window.setVisible(true);

	}

	public void closeWindow() {
		window.dispose();
	}

	@Override
	public Object getWindow() {
		return window;
	}

	@Override
	public void valueChange(ValueChangeEvent evt) {
		if (log.isLoggable(Level.CONFIG))
			log.config(evt.getPropertyName() + "=" + evt.getNewValue());

		// BPartner - load Order/Invoice/Shipment
		if (evt.getPropertyName().equals("M_Requisition_ID")) {
			int M_Requisition_ID = 0;
			if (evt.getNewValue() != null) {
				M_Requisition_ID = ((Integer) evt.getNewValue()).intValue();
				getPRDetails(M_Requisition_ID);
			}

		}
		if (evt.getPropertyName().equals("QtyAvailal")) {

		}
		window.tableChanged(null);
	}

	protected void initBPartner() throws Exception {

		int AD_Column_ID = 3499; // C_Invoice.C_BPartner_ID
		MLookup lookup = MLookupFactory.get(Env.getCtx(), p_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		bPartnerField = new WSearchEditor("C_BPartner_ID", true, false, true, lookup);

		int C_BPartner_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
		bPartnerField.setValue(Integer.valueOf(C_BPartner_ID));

	}

	protected void initRequisition() throws Exception {

		int AD_Column_ID = 11477; // C_Invoice.C_BPartner_ID
		MLookup lookup = MLookupFactory.get(Env.getCtx(), p_WindowNo, 0, AD_Column_ID, DisplayType.Search);

		int M_Requisition_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "M_Requisition_ID");

		prField = new WSearchEditor("M_Requisition_ID", true, false, true, lookup);

		if (M_Requisition_ID > 0) {
			prField.setValue(M_Requisition_ID);
			getPRDetails( M_Requisition_ID); 
		} else {
			prField.setValue(null);
		}

	}

	protected void initOrder() {
		int AD_Column_ID = 2161;
		MLookup lookup = MLookupFactory.get(Env.getCtx(), p_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		orderField = new WSearchEditor("C_Order_ID", true, false, true, lookup);
		int C_Order_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_Order_ID");
		orderField.setValue(Integer.valueOf(C_Order_ID));

	}

	protected void getPRDetails(int M_Requisition_ID) {

		loadTableOIS(getPRDetailData(M_Requisition_ID));
	}

}
