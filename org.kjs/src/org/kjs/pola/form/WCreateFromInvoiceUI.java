package org.kjs.pola.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.apps.form.WCreateFromWindow;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListItem;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.Lookup;
import org.compiere.model.MDocType;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Space;

public class WCreateFromInvoiceUI extends CreateFromInvoice implements EventListener<Event>, ValueChangeListener
{
    private WCreateFromWindow window;
    private int p_WindowNo;
    private CLogger log;
    protected Label bPartnerLabel;
    protected WEditor bPartnerField;
    protected Label orderLabel;
    protected Listbox orderField;
    protected Label shipmentLabel;
    protected Listbox shipmentField;
    protected Label rmaLabel;
    protected Listbox rmaField;
    private boolean m_actionActive;
    
    public WCreateFromInvoiceUI(final GridTab tab) {
        super(tab);
        this.log = CLogger.getCLogger((Class)this.getClass());
        this.bPartnerLabel = new Label();
        this.orderLabel = new Label();
        this.orderField = ListboxFactory.newDropdownListbox();
        this.shipmentLabel = new Label();
        this.shipmentField = ListboxFactory.newDropdownListbox();
        this.rmaLabel = new Label();
        this.rmaField = ListboxFactory.newDropdownListbox();
        this.m_actionActive = false;
        this.log.info(this.getGridTab().toString());
        this.window = new WCreateFromWindow((CreateFrom)this, this.getGridTab().getWindowNo());
        this.p_WindowNo = this.getGridTab().getWindowNo();
        try {
            if (!this.dynInit()) {
                return;
            }
            this.zkInit();
            this.setInitOK(true);
        }
        catch (Exception e) {
            this.log.log(Level.SEVERE, "", (Throwable)e);
            this.setInitOK(false);
        }
        AEnv.showWindow((Window)this.window);
    }
    
    @Override
    public boolean dynInit() throws Exception {
        this.log.config("");
        super.dynInit();
        this.window.setTitle(this.getTitle());
        final Integer docTypeId = (Integer)this.getGridTab().getValue("C_DocTypeTarget_ID");
        final MDocType docType = MDocType.get(Env.getCtx(), (int)docTypeId);
        if (!"APC".equals(docType.getDocBaseType())) {
            this.rmaLabel.setVisible(false);
            this.rmaField.setVisible(false);
        }
        this.initBPartner(true);
        this.bPartnerField.addValueChangeListener((ValueChangeListener)this);
        return true;
    }
    
    protected void zkInit() throws Exception {
        this.bPartnerLabel.setText(Msg.getElement(Env.getCtx(), "C_BPartner_ID"));
        this.orderLabel.setText(Msg.getElement(Env.getCtx(), "C_Order_ID", this.isSOTrx));
        this.shipmentLabel.setText(Msg.getElement(Env.getCtx(), "M_InOut_ID", this.isSOTrx));
        this.rmaLabel.setText(Msg.translate(Env.getCtx(), "M_RMA_ID"));
        final Borderlayout parameterLayout = new Borderlayout();
        parameterLayout.setHeight("110px");
        parameterLayout.setWidth("100%");
        final Panel parameterPanel = this.window.getParameterPanel();
        parameterPanel.appendChild((Component)parameterLayout);
        final Grid parameterStdLayout = GridFactory.newGridLayout();
        final Panel parameterStdPanel = new Panel();
        parameterStdPanel.appendChild((Component)parameterStdLayout);
        final Center center = new Center();
        parameterLayout.appendChild((Component)center);
        center.appendChild((Component)parameterStdPanel);
        final Rows rows = parameterStdLayout.newRows();
        Row row = rows.newRow();
        row.appendChild(this.bPartnerLabel.rightAlign());
        if (this.bPartnerField != null) {
            row.appendChild(this.bPartnerField.getComponent());
        }
        row.appendChild(this.orderLabel.rightAlign());
        this.orderField.setHflex("1");
        row.appendChild((Component)this.orderField);
        row = rows.newRow();
        row.appendChild((Component)new Space());
        row.appendChild((Component)new Space());
        row.appendChild(this.shipmentLabel.rightAlign());
        this.shipmentField.setHflex("1");
        row.appendChild((Component)this.shipmentField);
        row = rows.newRow();
        row.appendChild((Component)new Space());
        row.appendChild((Component)new Space());
        row.appendChild(this.rmaLabel.rightAlign());
        this.rmaField.setHflex("1");
        row.appendChild((Component)this.rmaField);
    }
    
    public void onEvent(final Event e) throws Exception {
        if (this.m_actionActive) {
            return;
        }
        this.m_actionActive = true;
        if (e.getTarget().equals(this.orderField)) {
            final ListItem li = this.orderField.getSelectedItem();
            int C_Order_ID = 0;
            if (li != null && li.getValue() != null) {
                C_Order_ID = (int)li.getValue();
            }
            this.rmaField.setSelectedIndex(-1);
            this.shipmentField.setSelectedIndex(-1);
            this.loadOrder(C_Order_ID, true);
        }
        else if (e.getTarget().equals(this.shipmentField)) {
            final ListItem li = this.shipmentField.getSelectedItem();
            int M_InOut_ID = 0;
            if (li != null && li.getValue() != null) {
                M_InOut_ID = (int)li.getValue();
            }
            this.orderField.setSelectedIndex(-1);
            this.rmaField.setSelectedIndex(-1);
            this.loadShipment(M_InOut_ID);
        }
        else if (e.getTarget().equals(this.rmaField)) {
            final ListItem li = this.rmaField.getSelectedItem();
            int M_RMA_ID = 0;
            if (li != null && li.getValue() != null) {
                M_RMA_ID = (int)li.getValue();
            }
            this.orderField.setSelectedIndex(-1);
            this.shipmentField.setSelectedIndex(-1);
            this.loadRMA(M_RMA_ID);
        }
        this.m_actionActive = false;
    }
    
    public void valueChange(final ValueChangeEvent e) {
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config(String.valueOf(e.getPropertyName()) + "=" + e.getNewValue());
        }
        if (e.getPropertyName().equals("C_BPartner_ID")) {
            final int C_BPartner_ID = (int)e.getNewValue();
            this.initBPOrderDetails(C_BPartner_ID, true);
        }
        this.window.tableChanged((WTableModelEvent)null);
    }
    
    protected void initBPartner(final boolean forInvoice) throws Exception {
        final int AD_Column_ID = 3499;
        final MLookup lookup = MLookupFactory.get(Env.getCtx(), this.p_WindowNo, 0, AD_Column_ID, 30);
        this.bPartnerField = (WEditor)new WSearchEditor("C_BPartner_ID", true, false, true, (Lookup)lookup);
        final int C_BPartner_ID = Env.getContextAsInt(Env.getCtx(), this.p_WindowNo, "C_BPartner_ID");
        this.bPartnerField.setValue((Object)new Integer(C_BPartner_ID));
        this.initBPOrderDetails(C_BPartner_ID, forInvoice);
    }
    
    protected void initBPOrderDetails(final int C_BPartner_ID, final boolean forInvoice) {
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("C_BPartner_ID=" + C_BPartner_ID);
        }
        final KeyNamePair pp = new KeyNamePair(0, "");
        this.orderField.removeActionListener((EventListener)this);
        this.orderField.removeAllItems();
        this.orderField.addItem(pp);
        final ArrayList<KeyNamePair> list = this.loadOrderData(C_BPartner_ID, forInvoice, false);
        for (final KeyNamePair knp : list) {
            this.orderField.addItem(knp);
        }
        this.orderField.setSelectedIndex(0);
        this.orderField.addActionListener((EventListener)this);
        this.initBPDetails(C_BPartner_ID);
    }
    
    public void initBPDetails(final int C_BPartner_ID) {
        this.initBPShipmentDetails(C_BPartner_ID);
        this.initBPRMADetails(C_BPartner_ID);
    }
    
    private void initBPShipmentDetails(final int C_BPartner_ID) {
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("C_BPartner_ID" + C_BPartner_ID);
        }
        this.shipmentField.removeActionListener((EventListener)this);
        this.shipmentField.removeAllItems();
        final KeyNamePair pp = new KeyNamePair(0, "");
        this.shipmentField.addItem(pp);
        final ArrayList<KeyNamePair> list = this.loadShipmentData(C_BPartner_ID);
        for (final KeyNamePair knp : list) {
            this.shipmentField.addItem(knp);
        }
        this.shipmentField.setSelectedIndex(0);
        this.shipmentField.addActionListener((EventListener)this);
    }
    
    private void initBPRMADetails(final int C_BPartner_ID) {
        this.rmaField.removeActionListener((EventListener)this);
        this.rmaField.removeAllItems();
        final KeyNamePair pp = new KeyNamePair(0, "");
        this.rmaField.addItem(pp);
        final ArrayList<KeyNamePair> list = this.loadRMAData(C_BPartner_ID);
        for (final KeyNamePair knp : list) {
            this.rmaField.addItem(knp);
        }
        this.rmaField.setSelectedIndex(0);
        this.rmaField.addActionListener((EventListener)this);
    }
    
    protected void loadOrder(final int C_Order_ID, final boolean forInvoice) {
        this.loadTableOIS(this.getOrderData(C_Order_ID, forInvoice));
    }
    
    protected void loadRMA(final int M_RMA_ID) {
        this.loadTableOIS(this.getRMAData(M_RMA_ID));
    }
    
    protected void loadShipment(final int M_InOut_ID) {
        this.loadTableOIS(this.getShipmentData(M_InOut_ID));
    }
    
    protected void loadTableOIS(final Vector<?> data) {
        this.window.getWListbox().clear();
        this.window.getWListbox().getModel().removeTableModelListener((WTableModelListener)this.window);
        final ListModelTable model = new ListModelTable((Collection)data);
        model.addTableModelListener((WTableModelListener)this.window);
        this.window.getWListbox().setData(model, (List)this.getOISColumnNames());
        this.configureMiniTable((IMiniTable)this.window.getWListbox());
    }
    
    public void showWindow() {
        this.window.setVisible(true);
    }
    
    public void closeWindow() {
        this.window.dispose();
    }
    
    public Object getWindow() {
        return this.window;
    }
}
