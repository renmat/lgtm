package com.github.renmat.egensvc.egit;

import org.eclipse.egit.ui.IBranchNameProvider;

@SuppressWarnings("restriction")
public class BranchNameProvider implements IBranchNameProvider {

	@Override
	public String getBranchNameSuggestion() {
		return "ticket-1234-id-branch";
	}

}
