package com.github.renmat.egensvc;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.github.renmat.egensvc.egit.PeriodicGitopsJob;
import com.github.renmat.egensvc.pref.EGenSvcPref;

public class EGenSvcActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.github.renmat.egensvc.plugin"; //$NON-NLS-1$
	private static EGenSvcActivator plugin;

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	    IPreferenceStore store = getPreferenceStore();
	    boolean gitopsBooleanPref = store.getBoolean(EGenSvcPref.P_KEY_ENABLE_GITOPS_JOB);
	    if(gitopsBooleanPref) {
			PeriodicGitopsJob gf = new PeriodicGitopsJob();
			gf.scheduleJob();
	    }
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static EGenSvcActivator getDefault() {
		return plugin;
	}

	public static void logInfo(String message) {
		getDefault().getLog().info(message);
	}
}
