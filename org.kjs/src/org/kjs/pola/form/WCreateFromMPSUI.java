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
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Vlayout;

public class WCreateFromMPSUI extends CreateFromMPS implements EventListener<Event>, ValueChangeListener
{
    private WCreateFromWindow window;
    private int p_WindowNo;
    protected Label orderLabel;
    protected Label requisitionLabel;
    protected Listbox orderField;
    protected Listbox requisitionField;
    private boolean m_actionActive;
    private int C_Order_ID;
    private int M_Requisition_ID;
    
    public WCreateFromMPSUI(final GridTab gridTab) {
        super(gridTab);
        this.orderLabel = new Label();
        this.requisitionLabel = new Label();
        this.orderField = ListboxFactory.newDropdownListbox();
        this.requisitionField = ListboxFactory.newDropdownListbox();
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
        this.initOrderDetails();
        this.initRequisitionDetails();
        this.loadData();
        return true;
    }
    
    protected void zkInit() throws Exception {
        this.orderLabel.setText(Msg.translate(Env.getCtx(), "C_Order_ID"));
        this.requisitionLabel.setText(Msg.translate(Env.getCtx(), "M_Requisition_ID"));
        final Vlayout vlayout = new Vlayout();
        vlayout.setVflex("1");
        vlayout.setWidth("100%");
        final Panel parameterPanel = this.window.getParameterPanel();
        parameterPanel.appendChild((Component)vlayout);
        final Grid parameterStdLayout = GridFactory.newGridLayout();
        vlayout.appendChild((Component)parameterStdLayout);
        final Rows rows = parameterStdLayout.newRows();
        Row row = rows.newRow();
        row.appendChild(this.orderLabel.rightAlign());
        row.appendChild((Component)this.orderField);
        this.orderField.setHflex("1");
        row = rows.newRow();
        row.appendChild(this.requisitionLabel.rightAlign());
        row.appendChild((Component)this.requisitionField);
        this.requisitionField.setHflex("1");
    }
    
    public void onEvent(final Event e) throws Exception {
        if (this.m_actionActive) {
            return;
        }
        this.m_actionActive = true;
        if (e.getTarget().equals(this.orderField)) {
            final KeyNamePair pp = this.orderField.getSelectedItem().toKeyNamePair();
            if (pp != null) {
                this.C_Order_ID = pp.getKey();
                this.loadData();
            }
        }
        else if (e.getTarget().equals(this.requisitionField)) {
            final KeyNamePair pp = this.requisitionField.getSelectedItem().toKeyNamePair();
            if (pp != null) {
                this.M_Requisition_ID = pp.getKey();
                this.loadData();
            }
        }
        else if (e.getTarget().equals(this.orderField)) {
            final KeyNamePair pp = this.orderField.getSelectedItem().toKeyNamePair();
            if (pp != null) {
                this.loadData();
            }
        }
        this.m_actionActive = false;
    }
    
    public void valueChange(final ValueChangeEvent e) {
        if (this.log.isLoggable(Level.CONFIG)) {
            this.log.config(String.valueOf(e.getPropertyName()) + "=" + e.getNewValue());
        }
        
        if (e.getPropertyName().equals("M_Product_ID") && e.getNewValue() != null) { 
            e.getNewValue();
        }
        
        this.window.tableChanged((WTableModelEvent)null);
    }
    
    protected void loadData() {
        this.loadTableOIS(this.getProductPhaseData(this.C_Order_ID, this.M_Requisition_ID));
    }
    
    private void loadTableOIS(final Vector<?> data) {
        this.window.getWListbox().clear();
        this.window.getWListbox().getModel().removeTableModelListener((WTableModelListener)this.window);
        final ListModelTable model = new ListModelTable((Collection)data);
        model.addTableModelListener((WTableModelListener)this.window);
        this.window.getWListbox().setData(model, (List)this.getOISColumnNames());
        this.configureMiniTable((IMiniTable)this.window.getWListbox());
    }
    
    private void initOrderDetails() {
        this.orderField.removeActionListener((EventListener)this);
        this.orderField.removeAllItems();
        final KeyNamePair pp = new KeyNamePair(0, "");
        this.orderField.addItem(pp);
        final ArrayList<KeyNamePair> list = this.loadOrder();
        for (final KeyNamePair knp : list) {
            this.orderField.addItem(knp);
        }
        this.orderField.setSelectedIndex(0);
        this.orderField.addActionListener((EventListener)this);
    }
    
    private void initRequisitionDetails() {
        this.requisitionField.removeActionListener((EventListener)this);
        this.requisitionField.removeAllItems();
        final KeyNamePair pp = new KeyNamePair(0, "");
        this.requisitionField.addItem(pp);
        final ArrayList<KeyNamePair> list = this.loadRequisition();
        for (final KeyNamePair knp : list) {
            this.requisitionField.addItem(knp);
        }
        this.requisitionField.setSelectedIndex(0);
        this.requisitionField.addActionListener((EventListener)this);
    }
    
    public void showWindow() {
        this.window.setVisible(true);
    }
    
    public void closeWindow() {
        this.window.dispose();
    }
    
    @Override
    public Object getWindow() {
        return this.window;
    }
}
