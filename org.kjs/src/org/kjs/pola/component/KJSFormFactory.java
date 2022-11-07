package org.kjs.pola.component;

import java.util.logging.Level;

import org.adempiere.webui.factory.IFormFactory;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.IFormController;
import org.compiere.util.CLogger;

public class KJSFormFactory implements IFormFactory
{
    private static final CLogger log;
    
    static {
        log = CLogger.getCLogger(KJSFormFactory.class);
    }
    
    public ADForm newFormInstance(final String formName) {
        if (formName.startsWith("org.kjs.jembo.form")) {
            Object form = null;
            Class<?> clazz = null;
            final ClassLoader loader = this.getClass().getClassLoader();
            try {
                clazz = loader.loadClass(formName);
            }
            catch (Exception e) {
                KJSFormFactory.log.log(Level.FINE, "Load form class failed in org.kjs.jembo.form", (Throwable)e);
            }
            if (clazz != null) {
                try {
                	 form = clazz.getDeclaredConstructor().newInstance();
                }
                catch (Exception e) {
                    KJSFormFactory.log.log(Level.FINE, "Form Class Initiated failed in  org.kjs.jembo.form", (Throwable)e);
                }
            }
            if (form != null) {
                if (form instanceof ADForm) {
                    return (ADForm)form;
                }
                if (form instanceof IFormController) {
                    final IFormController controller = (IFormController)form;
                    final ADForm adForm = controller.getForm();
                    adForm.setICustomForm(controller);
                    return adForm;
                }
            }
        }
        return null;
    }
}
