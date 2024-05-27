package com.github.renmat.egensvc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.renmat.egensvc.EGenSvcActivator;

public class ActivatorTest {

	@Test
	public void testActivatorId() {
		Assertions.assertNotNull(EGenSvcActivator.PLUGIN_ID);
	}

}
