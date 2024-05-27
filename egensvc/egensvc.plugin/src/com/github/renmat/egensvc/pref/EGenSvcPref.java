package com.github.renmat.egensvc.pref;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.renmat.egensvc.EGenSvcActivator;

public class EGenSvcPref extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String P_KEY_EXEC_GITOPS_PATH = "P_KEY_EXEC_GITOPS_PATH";
	public static final String P_KEY_ENABLE_GITOPS_JOB = "P_KEY_ENABLE_GITOPS_JOB";
	public static final String P_KEY_ENABLE_GITOPS_JOB_DELAY = "P_KEY_ENABLE_GITOPS_JOB_DELAY";
	public static final String P_KEY_LOCAL_SERVER_PORT = "P_KEY_LOCAL_SERVER_PORT";
	public static final String P_KEY_EXEC_LAUNCH_CHOICE = "P_KEY_EXEC_LAUNCH_CHOICE";
	public static final String P_KEY_COMMIT_PREFIX = "P_KEY_COMMIT_PREFIX";
	public static final String P_KEY_EXEC_TEMP_GITOPS = "P_KEY_EXEC_TEMP_GITOPS";
	public static final String P_KEY_EXT_LAUNCH_BUTTON = "P_KEY_EXT_LAUNCH_BUTTON";

	public EGenSvcPref() {
		super(GRID);
		IPreferenceStore scopedPreferenceStore = EGenSvcActivator.getDefault().getPreferenceStore();
		setPreferenceStore(scopedPreferenceStore);
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		addField(new FileFieldEditor(P_KEY_EXEC_GITOPS_PATH, "Exec&utable gitops", getFieldEditorParent()));		
		addField(new BooleanFieldEditor(P_KEY_ENABLE_GITOPS_JOB, "Enable &periodic gitops", getFieldEditorParent()));
		addField(new IntegerFieldEditor(P_KEY_ENABLE_GITOPS_JOB_DELAY, "&Gitops job delay", getFieldEditorParent()));		
		addField(new RadioGroupFieldEditor(P_KEY_EXEC_LAUNCH_CHOICE, "Executable &jar launch mode", 1,
				new String[][] { { "&Within eclipse", "execWithinEclipse" }, { "E&xternal process", "execForkProcess" } },
				getFieldEditorParent()));
		
		addField(new StringFieldEditor(P_KEY_COMMIT_PREFIX, "&Commit prefix:", getFieldEditorParent()));
		List<String[]> entryNamesAndValues = new ArrayList<>();
		entryNamesAndValues.add(new String[] {"",""});
		try {
			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfiguration[] launchConfigurations = manager.getLaunchConfigurations();
			for(ILaunchConfiguration launchConfiguration:launchConfigurations) {
				if("org.eclipse.ui.externaltools".equals(launchConfiguration.getCategory())) {
					entryNamesAndValues.add(new String[] {launchConfiguration.getName(),launchConfiguration.getName()});
				}
			}
		} catch (Exception e) {
			EGenSvcActivator.getDefault().getLog().error("error getting launch config", e);
		}
		addField(new ComboFieldEditor(P_KEY_EXT_LAUNCH_BUTTON, "External launc&her",
				entryNamesAndValues.toArray(new String[entryNamesAndValues.size()][]), getFieldEditorParent()));
		addField(new IntegerFieldEditor(P_KEY_LOCAL_SERVER_PORT, "&Local server port", getFieldEditorParent()));
		
	}

}
