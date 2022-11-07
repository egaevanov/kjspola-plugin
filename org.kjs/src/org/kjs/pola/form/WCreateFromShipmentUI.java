package org.kjs.pola.form;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.apps.form.WCreateFromWindow;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WLocatorEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WStringEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.Lookup;
import org.compiere.model.MLocatorLookup;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MProduct;
import org.compiere.model.MWindow;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Vlayout;

public class WCreateFromShipmentUI extends CreateFromShipment implements EventListener<Event>, ValueChangeListener
{
    private WCreateFromWindow window;
    private int p_WindowNo;
    private CLogger log;
    protected Label bPartnerLabel;
    protected WEditor bPartnerField;
    protected Label orderLabel;
    protected Listbox orderField;
    protected Label rmaLabel;
    protected Listbox rmaField;
    protected Label invoiceLabel;
    protected Listbox invoiceField;
    protected Checkbox sameWarehouseCb;
    protected Label locatorLabel;
    protected WLocatorEditor locatorField;
    protected Label upcLabel;
    protected WStringEditor upcField;
    private boolean m_actionActive;
    
    public WCreateFromShipmentUI(final GridTab tab) {
        super(tab);
        this.log = CLogger.getCLogger((Class)this.getClass());
        this.bPartnerLabel = new Label();
        this.orderLabel = new Label();
        this.orderField = ListboxFactory.newDropdownListbox();
        this.rmaLabel = new Label();
        this.rmaField = ListboxFactory.newDropdownListbox();
        this.invoiceLabel = new Label();
        this.invoiceField = ListboxFactory.newDropdownListbox();
        this.sameWarehouseCb = new Checkbox();
        this.locatorLabel = new Label();
        this.locatorField = new WLocatorEditor();
        this.upcLabel = new Label();
        this.upcField = new WStringEditor();
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
            throw new AdempiereException(e.getMessage());
        }
        AEnv.showWindow((Window)this.window);
    }
    
    @Override
    public boolean dynInit() throws Exception {
        this.log.config("");
        super.dynInit();
        this.window.setTitle(this.getTitle());
        this.sameWarehouseCb.setSelected(false);
        this.sameWarehouseCb.addActionListener((EventListener)this);
        final MLocatorLookup locator = new MLocatorLookup(Env.getCtx(), this.p_WindowNo);
        this.locatorField = new WLocatorEditor("M_Locator_ID", true, false, true, locator, this.p_WindowNo);
        this.initBPartner(false);
        this.bPartnerField.addValueChangeListener((ValueChangeListener)this);
        this.locatorLabel.setMandatory(true);
        this.upcField = new WStringEditor("UPC", false, false, true, 10, 30, (String)null, (String)null);
        this.upcField.getComponent().addEventListener("onChange", (EventListener)this);
        return true;
    }
    
    protected void zkInit() throws Exception {
        final boolean isRMAWindow = this.getGridTab().getAD_Window_ID() == 53098 || this.getGridTab().getAD_Window_ID() == 53097;
        this.bPartnerLabel.setText(Msg.getElement(Env.getCtx(), "C_BPartner_ID"));
        final boolean isSOTrx = new MWindow(Env.getCtx(), this.getGridTab().getAD_Window_ID(), (String)null).isSOTrx();
        this.orderLabel.setText(Msg.getElement(Env.getCtx(), "C_Order_ID", isSOTrx));
        final Vlayout vlayout = new Vlayout();
        vlayout.setVflex("1");
        vlayout.setWidth("100%");
        final Panel parameterPanel = this.window.getParameterPanel();
        parameterPanel.appendChild((Component)vlayout);
        final Grid parameterStdLayout = GridFactory.newGridLayout();
        vlayout.appendChild((Component)parameterStdLayout);
        final Rows rows = parameterStdLayout.newRows();
        final Row row = rows.newRow();
        row.appendChild(this.bPartnerLabel.rightAlign());
        if (this.bPartnerField != null) {
            row.appendChild(this.bPartnerField.getComponent());
            this.bPartnerField.fillHorizontal();
        }
        if (!isRMAWindow) {
            row.appendChild(this.orderLabel.rightAlign());
            row.appendChild((Component)this.orderField);
            this.orderField.setHflex("1");
        }
    }
    
    public void onEvent(final Event e) throws Exception {
        if (this.m_actionActive) {
            return;
        }
        this.m_actionActive = true;
        if (e.getTarget().equals(this.orderField)) {
            final KeyNamePair pp = this.orderField.getSelectedItem().toKeyNamePair();
            if (pp != null) {
                if (pp.getKey() != 0) {
                    final int C_Order_ID = pp.getKey();
                    this.invoiceField.setSelectedIndex(-1);
                    this.rmaField.setSelectedIndex(-1);
                    this.loadOrder(C_Order_ID, false, (this.locatorField.getValue() != null) ? ((int)this.locatorField.getValue()) : 0);
                }
            }
        }
        else if (e.getTarget().equals(this.invoiceField)) {
            final KeyNamePair pp = this.invoiceField.getSelectedItem().toKeyNamePair();
            if (pp != null) {
                if (pp.getKey() != 0) {
                    final int C_Invoice_ID = pp.getKey();
                    this.orderField.setSelectedIndex(-1);
                    this.rmaField.setSelectedIndex(-1);
                    this.loadInvoice(C_Invoice_ID, (this.locatorField.getValue() != null) ? ((int)this.locatorField.getValue()) : 0);
                }
            }
        }
        else if (e.getTarget().equals(this.rmaField)) {
            final KeyNamePair pp = this.rmaField.getSelectedItem().toKeyNamePair();
            if (pp != null) {
                if (pp.getKey() != 0) {
                    final int M_RMA_ID = pp.getKey();
                    this.orderField.setSelectedIndex(-1);
                    this.invoiceField.setSelectedIndex(-1);
                    this.loadRMA(M_RMA_ID, (this.locatorField.getValue() != null) ? ((int)this.locatorField.getValue()) : 0);
                }
            }
        }
        else if (e.getTarget().equals(this.sameWarehouseCb)) {
            final int bpId = (int)((this.bPartnerField.getValue() == null) ? 0 : this.bPartnerField.getValue());
            this.initBPOrderDetails(bpId, false);
        }
        else if (e.getTarget().equals(this.upcField.getComponent())) {
            this.checkProductUsingUPC();
        }
        this.m_actionActive = false;
    }
    
    private void checkProductUsingUPC() {
        final String upc = this.upcField.getDisplay();
        final ListModelTable model = this.window.getWListbox().getModel();
        final List<MProduct> products = (List<MProduct>)MProduct.getByUPC(Env.getCtx(), upc, (String)null);
        for (final MProduct product : products) {
            final int row = this.findProductRow(product.get_ID());
            if (row >= 0) {
                final BigDecimal qty = (BigDecimal)model.getValueAt(row, 1);
                model.setValueAt((Object)qty, row, 1);
                model.setValueAt((Object)Boolean.TRUE, row, 0);
                model.updateComponent(row, row);
            }
        }
        this.upcField.setValue((Object)"");
    }
    
    private int findProductRow(final int M_Product_ID) {
        final ListModelTable model = this.window.getWListbox().getModel();
        for (int i = 0; i < model.getRowCount(); ++i) {
            final KeyNamePair kp = (KeyNamePair)model.getValueAt(i, 4);
            if (kp.getKey() == M_Product_ID) {
                return i;
            }
        }
        return -1;
    }
    
    public void valueChange(final ValueChangeEvent e) {
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config(String.valueOf(e.getPropertyName()) + "=" + e.getNewValue());
        }
        if (e.getPropertyName().equals("C_BPartner_ID")) {
            int C_BPartner_ID = 0;
            if (e.getNewValue() != null) {
                C_BPartner_ID = (int)e.getNewValue();
            }
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
    
    private void initBPInvoiceDetails(final int C_BPartner_ID) {
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("C_BPartner_ID" + C_BPartner_ID);
        }
        this.invoiceField.removeActionListener((EventListener)this);
        this.invoiceField.removeAllItems();
        final KeyNamePair pp = new KeyNamePair(0, "");
        this.invoiceField.addItem(pp);
        final ArrayList<KeyNamePair> list = this.loadInvoiceData(C_BPartner_ID);
        for (final KeyNamePair knp : list) {
            this.invoiceField.addItem(knp);
        }
        this.invoiceField.setSelectedIndex(0);
        this.invoiceField.addActionListener((EventListener)this);
        this.upcField.addValueChangeListener((ValueChangeListener)this);
    }
    
    protected void initBPOrderDetails(final int C_BPartner_ID, final boolean forInvoice) {
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config("C_BPartner_ID=" + C_BPartner_ID);
        }
        final KeyNamePair pp = new KeyNamePair(0, "");
        this.orderField.removeActionListener((EventListener)this);
        this.orderField.removeAllItems();
        this.orderField.addItem(pp);
        final ArrayList<KeyNamePair> list = this.loadOrderData(C_BPartner_ID, forInvoice, this.sameWarehouseCb.isSelected());
        for (final KeyNamePair knp : list) {
            this.orderField.addItem(knp);
        }
        this.orderField.setSelectedIndex(0);
        this.orderField.addActionListener((EventListener)this);
        this.initBPDetails(C_BPartner_ID);
    }
    
    public void initBPDetails(final int C_BPartner_ID) {
        this.initBPInvoiceDetails(C_BPartner_ID);
        this.initBPRMADetails(C_BPartner_ID);
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
    
    protected void loadOrder(final int C_Order_ID, final boolean forInvoice, final int M_Locator_ID) {
        this.loadTableOIS(this.getOrderData(C_Order_ID, forInvoice, M_Locator_ID));
    }
    
    protected void loadRMA(final int M_RMA_ID, final int M_Locator_ID) {
        this.loadTableOIS(this.getRMAData(M_RMA_ID, M_Locator_ID));
    }
    
    protected void loadInvoice(final int C_Invoice_ID, final int M_Locator_ID) {
        this.loadTableOIS(this.getInvoiceData(C_Invoice_ID, M_Locator_ID));
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
