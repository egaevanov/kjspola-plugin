package org.kjs.pola.form;

import org.compiere.minigrid.IMiniTable;
import java.util.List;
import java.util.Collection;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.event.WTableModelListener;
import java.util.Vector;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.GridFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Vlayout;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.apps.AEnv;
import java.util.logging.Level;
import org.compiere.grid.CreateFrom;
import org.compiere.model.GridTab;
import org.adempiere.webui.apps.form.WCreateFromWindow;
import org.adempiere.webui.event.ValueChangeListener;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class WCreateFromProductionPlanUI extends CreateFromProductionPlan implements EventListener<Event>, ValueChangeListener
{
    private WCreateFromWindow window;
    private int p_WindowNo;
    private boolean m_actionActive;
    
    public WCreateFromProductionPlanUI(final GridTab gridTab) {
        super(gridTab);
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
        this.loadData();
        return true;
    }
    
    protected void zkInit() throws Exception {
        final Vlayout vlayout = new Vlayout();
        vlayout.setVflex("1");
        vlayout.setWidth("100%");
        final Panel parameterPanel = this.window.getParameterPanel();
        parameterPanel.appendChild((Component)vlayout);
        final Grid parameterStdLayout = GridFactory.newGridLayout();
        vlayout.appendChild((Component)parameterStdLayout);
    }
    
    public void onEvent(final Event e) throws Exception {
        if (this.m_actionActive) {
            return;
        }
        this.m_actionActive = true;
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
        this.loadTableOIS(this.getProductPhaseData(this.M_Product_ID));
    }
    
    private void loadTableOIS(final Vector<Vector<Object>> data) {
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
    
    @Override
    public Object getWindow() {
        return this.window;
    }
}
