package com.github.renmat.egensvc.egit;

import java.util.Arrays;

import org.eclipse.core.resources.IResource;
import org.eclipse.egit.ui.CommitMessageWithCaretPosition;
import org.eclipse.egit.ui.ICommitMessageProvider2;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.renmat.egensvc.EGenSvcActivator;
import com.github.renmat.egensvc.pref.EGenSvcPref;

@SuppressWarnings("restriction")
public class CommitMessageProvider implements ICommitMessageProvider2 {

	@Override
	public String getMessage(IResource[] arg0) {
		EGenSvcActivator.logInfo("CommitMessageProvider "+Arrays.toString(arg0));
		try {
			IPreferenceStore store = EGenSvcActivator.getDefault().getPreferenceStore();
			String commitPrefix = store.getString(EGenSvcPref.P_KEY_COMMIT_PREFIX);
			if(commitPrefix!=null && commitPrefix.trim().length()>0) {				
				return commitPrefix;
			}
		} catch (Exception e) {
			EGenSvcActivator.getDefault().getLog().error("error loading url", e);
		}
		return "ticket-1234-id ";
	}

	@Override
	public CommitMessageWithCaretPosition getCommitMessageWithPosition(IResource[] arg0) {
		String commitMessage = getMessage(arg0);
		return new CommitMessageWithCaretPosition(commitMessage,commitMessage.length());
	}

}
