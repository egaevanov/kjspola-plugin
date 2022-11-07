package org.kjs.pola.component;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;

/**
 * 
 * @author Tegar N
 *
 */

public class KJSProcessFactory implements IProcessFactory {

	@Override
	public ProcessCall newProcessInstance(String className) {
		ProcessCall process = null;
		try {
			Class<?> clazz = getClass().getClassLoader().loadClass(className);
			process = (ProcessCall) clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
		}
		return process;
	}

}
