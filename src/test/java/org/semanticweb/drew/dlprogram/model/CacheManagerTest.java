package org.semanticweb.drew.dlprogram.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.model.CacheManager;


public class CacheManagerTest {

	@Test
	public void testGetArity001() {
		String name = "p";
		int arity = 1;
		CacheManager.getInstance().getPredicate(name, arity);
		assertEquals(1,CacheManager.getInstance().getArity(name));
	}

	@Test
	public void testGetArity002() {
		String name = "r";
		int arity = 3;
		CacheManager.getInstance().getPredicate(name, arity);
		assertEquals(-1,CacheManager.getInstance().getArity(name));
	}

}
