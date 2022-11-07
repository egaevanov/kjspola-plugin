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

public class WCreateFromProformaInv extends CreateFromProformaInv implements EventListener<Event>, ValueChangeListener {

	public WCreateFromProformaInv(GridTab gridTab) {
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
	
	
	
	protected Label bPartnerLabel = new Label();
	protected WEditor bPartnerField;
	protected Label ProInvLabel = new Label();
	protected WEditor ProInvField;
	protected Label SalesLabel = new Label();
	protected WEditor SalesField;
	private Grid parameterStdLayout;
	private boolean m_actionActive = false;	
	
	@Override
	public void onEvent(Event event) throws Exception {
		
		if (m_actionActive)
			return;
		m_actionActive = true;
		
	}
	
	public boolean dynInit() throws Exception {
		log.config("");

		super.dynInit();

		window.setTitle(getTitle());

		initBPartner();
		initSalesOrder();
		initProInv();
		bPartnerField.addValueChangeListener((ValueChangeListener) this);
		SalesField.addValueChangeListener((ValueChangeListener) this);

		return true;
	} // dynInit
	
	protected void zkInit() throws Exception {

		bPartnerLabel.setText(Msg.getElement(Env.getCtx(), "C_BPartner_ID"));
		ProInvLabel.setText("Invoice Proforma");
		SalesLabel.setText("Sales Order");

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

		row.appendChild(ProInvLabel.rightAlign());
		row.appendChild(ProInvField.getComponent());
		ProInvField.setReadWrite(false);

		row.appendChild(bPartnerLabel.rightAlign());
		row.appendChild(bPartnerField.getComponent());
		bPartnerField.fillHorizontal();
		bPartnerField.setReadWrite(false);

		row = rows.newRow();
		row.appendChild(SalesLabel.rightAlign());
		row.appendChild(SalesField.getComponent());
		SalesField.setReadWrite(true);

	}
	
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

	@Override
	public void valueChange(ValueChangeEvent evt) {

		if (log.isLoggable(Level.CONFIG))
			log.config(evt.getPropertyName() + "=" + evt.getNewValue());

		// BPartner - load Order/Invoice/Shipment
		if (evt.getPropertyName().equals("C_Order_ID")) {
			int C_Order_ID = 0;
			if (evt.getNewValue() != null) {
				C_Order_ID = ((Integer) evt.getNewValue()).intValue();
				getSODetails( C_Order_ID); 
			}

		}
		
	}
	
	protected void initBPartner() throws Exception {

		int AD_Column_ID = 1004161; // C_Invoice.C_BPartner_ID
		MLookup lookup = MLookupFactory.get(Env.getCtx(), p_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		bPartnerField = new WSearchEditor("C_BPartner_ID", true, false, true, lookup);

		int C_BPartner_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
		bPartnerField.setValue(Integer.valueOf(C_BPartner_ID));

	}

	protected void initSalesOrder() throws Exception {

		int AD_Column_ID = 1004166; 
		MLookup lookup = MLookupFactory.get(Env.getCtx(), p_WindowNo, 0, AD_Column_ID, DisplayType.Search);

		int C_Order_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_Order_ID");

		SalesField = new WSearchEditor("C_Order_ID", true, false, true, lookup);

		if (C_Order_ID > 0) {
			SalesField.setValue(C_Order_ID);
			getSODetails( C_Order_ID); 
		} else {
			SalesField.setValue(null);
		}

	}

	protected void initProInv() {
		int AD_Column_ID = 1004155;
		MLookup lookup = MLookupFactory.get(Env.getCtx(), p_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		ProInvField = new WSearchEditor("C_ARProInv_ID", true, false, true, lookup);
		int C_ARProInv_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_ARProInv_ID");
		ProInvField.setValue(Integer.valueOf(C_ARProInv_ID));

	}

	protected void getSODetails(int C_Order_ID) {

		loadTableOIS(getSODetailData(C_Order_ID));
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


	

}
