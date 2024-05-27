package com.github.renmat.egensvc.pref;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.renmat.egensvc.EGenSvcActivator;

public class EGenSvcPrefInit extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = EGenSvcActivator.getDefault().getPreferenceStore();
		store.setDefault(EGenSvcPref.P_KEY_ENABLE_GITOPS_JOB, false);
		store.setDefault(EGenSvcPref.P_KEY_EXEC_LAUNCH_CHOICE, "execForkProcess");
		
		store.setDefault(EGenSvcPref.P_KEY_LOCAL_SERVER_PORT, 8080);
		store.setDefault(EGenSvcPref.P_KEY_ENABLE_GITOPS_JOB_DELAY, 60000);
	}

}