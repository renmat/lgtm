package com.github.renmat.egensvc.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.github.renmat.egensvc.EGenSvcActivator;
import com.github.renmat.egensvc.pref.EGenSvcPref;



public class EGenSvcBrowserView extends ViewPart implements ISelectionListener {

	public static final String ID = "com.github.renmat.egensvc.views.eGenSvcBrowserView";

	private Action action1 = makeAction1();
	private Action action2 = makeAction2();

	private Browser fBrowser;

	@Override
	public void createPartControl(Composite parent) {
		fBrowser = new Browser(parent, SWT.WEBKIT);
		loadUrl();
		makeActions();
		contributeToActionBars(getViewSite());
		getSite().getPage().addSelectionListener(this);
	}

	private void contributeToActionBars(IViewSite viewSite) {
		IActionBars bars = viewSite.getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		makeAction1();
		makeAction2();
	}
	
	private void loadUrl() {
		try {
			IPreferenceStore store = EGenSvcActivator.getDefault().getPreferenceStore();
			int localServerPort = store.getInt(EGenSvcPref.P_KEY_LOCAL_SERVER_PORT);
			if(localServerPort>0) {
				fBrowser.setUrl("http://localhost:"+localServerPort);
			}
		} catch (Exception e) {
			EGenSvcActivator.getDefault().getLog().error("error loading url", e);
		}		
	}

	private Action makeAction1() {
		Action action = new Action() {
			public void run() {
				loadUrl();
			}
		};
		action.setText("Home");
		action.setToolTipText("Go to landing page");
		action.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		return action;
	}

	private Action makeAction2() {
		Action action = new Action() {
			public void run() {
				fBrowser.refresh();
			}
		};
		action.setText("Refresh");
		action.setToolTipText("Refresh the page");
		action.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
		return action;
	}

	@Override
	public void setFocus() {
		fBrowser.setFocus();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection.isEmpty()) {
			return;
		}
		if (selection instanceof IStructuredSelection) {
			
		} else {
			
		}
	}

	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(this);
		super.dispose();
	}
}
