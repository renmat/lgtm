package com.github.renmat.egensvc.action;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.github.renmat.egensvc.EGenSvcActivator;
import com.github.renmat.egensvc.pref.EGenSvcPref;

public class GitopsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		runExternalJar(getCurrentProjectPath(event), null, "gitops", null);
		return null;
	}

	public static String getCurrentProjectPath(ExecutionEvent event)  {
		try {
			IWorkbenchWindow window = event != null ? HandlerUtil.getActiveWorkbenchWindow(event)
					: PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage activePage = window.getActivePage();
			ISelection selection = activePage.getSelection();
			if (selection != null) {
				if (selection instanceof IStructuredSelection) {
					if (selection instanceof ITreeSelection) {
						TreeSelection treeSelection = (TreeSelection) selection;
						TreePath[] treePaths = treeSelection.getPaths();
						TreePath treePath = treePaths[0];
						Object lastSegmentObj = treePath.getLastSegment();
						Class currClass = lastSegmentObj.getClass();
						while (currClass != null) {
							Class[] interfaces = currClass.getInterfaces();
							currClass = currClass.getSuperclass();
						}
						if (lastSegmentObj instanceof IAdaptable) {
							IFile file = (IFile) ((IAdaptable) lastSegmentObj).getAdapter(IFile.class);
							if (file != null) {
								String path = file.getRawLocation().toOSString();
								return path;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			EGenSvcActivator.getDefault().getLog().error("error resolving project", e);
		}
		return null;
	}

	public static void runExternalJar(String currentProjectPath, IProgressMonitor monitor, String command,
			List<String> arguments) {
		IPreferenceStore store = EGenSvcActivator.getDefault().getPreferenceStore();
		boolean fork = "execForkProcess".equals(store.getString(EGenSvcPref.P_KEY_EXEC_LAUNCH_CHOICE));
		if (fork) {
			ExtLauncherHandler.runExternalLaunchConfiguration();
		} else {
			try {
				String jarLocation = store.getString(EGenSvcPref.P_KEY_EXEC_GITOPS_PATH);
				if (jarLocation != null && new File(jarLocation).exists()) {
					List<String> args = new ArrayList<String>();
					args.add(command);
					args.add(currentProjectPath);
					if (arguments != null) {
						args.addAll(arguments);
					}
					runExternalJarCommand(jarLocation, "main", arguments.toArray(new String[arguments.size()]));
					IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
					workspaceRoot.refreshLocal(IResource.DEPTH_INFINITE, monitor);
				}
			} catch (Exception e) {
				EGenSvcActivator.getDefault().getLog().error("error exec jar", e);
			}
		}
	}

	public static String runExternalJarCommand(String jarLocation, String method, String[] args)
			throws IOException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		try (JarFile jarFile = new JarFile(jarLocation)) {
			Manifest manifest = jarFile.getManifest();
			Attributes attributes = manifest.getMainAttributes();
			String mainClassName = attributes.getValue("Main-Class");
			Class<?> mainClass = Class.forName(mainClassName);
			Object result = mainClass.getMethod("method", String[].class).invoke(mainClass, (String[]) args);
			if (result != null) {
				return result.toString();
			}
			return null;
		}
	}
}
