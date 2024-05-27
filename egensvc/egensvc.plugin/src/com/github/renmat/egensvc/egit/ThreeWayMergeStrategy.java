package com.github.renmat.egensvc.egit;

import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.StrategyRecursive;
import org.eclipse.jgit.merge.ThreeWayMerger;

public class ThreeWayMergeStrategy extends StrategyRecursive {
	

	@Override
	public ThreeWayMerger newMerger(Repository db) {
		return newMerger(db, false);
	}

	@Override
	public ThreeWayMerger newMerger(Repository db, boolean inCore) {
		ThreeWayMerger threeWayMerger = super.newMerger(db, inCore);
		return threeWayMerger;
	}

	@Override
	public ThreeWayMerger newMerger(ObjectInserter inserter, Config config) {
		ThreeWayMerger threeWayMerger = super.newMerger(inserter, config);
		return threeWayMerger;
	}

	@Override
	public String getName() {
		return "egensvcrecursive";
	}

}
