package com.github.renmat.egensvc.egit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.renmat.egensvc.EGenSvcActivator;
import com.github.renmat.egensvc.action.GitopsHandler;
import com.github.renmat.egensvc.pref.EGenSvcPref;

public class PeriodicGitopsJob extends Job {

	private int jobRepeatDelay = 60000;
	
	public PeriodicGitopsJob() {
		super("PeriodicGitopsJob");
		try {
			IPreferenceStore store = EGenSvcActivator.getDefault().getPreferenceStore();
			int jobDelay = store.getInt(EGenSvcPref.P_KEY_ENABLE_GITOPS_JOB_DELAY);
			if(jobDelay>0) {
				jobRepeatDelay = jobDelay;
			}
		} catch (Exception e) {
			EGenSvcActivator.getDefault().getLog().error("error loading url", e);
		}	
	}

	private boolean enabled = true;

	protected IStatus run(IProgressMonitor monitor) {
		try {
			GitopsHandler.runExternalJar(GitopsHandler.getCurrentProjectPath(null), monitor, "gitops", null);
			scheduleJob();
		} catch (Exception e) {
			EGenSvcActivator.getDefault().getLog().error("error in job", e);
		}		
		return Status.OK_STATUS;
	}
	
	

	public boolean shouldSchedule() {
		return enabled;
	}

	public void stop() {
		enabled = false;
	}



	public void scheduleJob() {
		if (enabled) {
			schedule(jobRepeatDelay);
		}
	}

}
