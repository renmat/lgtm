package com.github.renmat.egensvc.action;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.renmat.egensvc.EGenSvcActivator;
import com.github.renmat.egensvc.pref.EGenSvcPref;

public class ExtLauncherHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		runExternalLaunchConfiguration();
		return null;
	}
	
	public static void runExternalLaunchConfiguration() {
		IPreferenceStore store = EGenSvcActivator.getDefault().getPreferenceStore();
	    String extLaunchName = store.getString(EGenSvcPref.P_KEY_EXT_LAUNCH_BUTTON);
	    if(extLaunchName!=null && extLaunchName.trim().length()>0) {
	    	try {
				runExternalLaunchConfiguration(extLaunchName);
			} catch (CoreException e) {
				EGenSvcActivator.getDefault().getLog().error("error running "+extLaunchName, e);
			}
	    }
	}

	public static void runExternalLaunchConfiguration(String configurationName) throws CoreException {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfiguration[] launchConfigurations = manager.getLaunchConfigurations();
		for(ILaunchConfiguration launchConfiguration:launchConfigurations) {
			if("org.eclipse.ui.externaltools".equals(launchConfiguration.getCategory()) && launchConfiguration.getName().equals(configurationName)) {
				ILaunchConfigurationWorkingCopy t = launchConfiguration.getWorkingCopy();
				ILaunchConfiguration config = t.doSave();
				if (config != null) {
					DebugUITools.launch(config, ILaunchManager.RUN_MODE);
				}
			}
		}
	}

}
