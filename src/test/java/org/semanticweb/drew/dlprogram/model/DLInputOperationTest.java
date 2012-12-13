/*
 * @(#)DLInputAddOperationTest.java 2010-4-1 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.dlprogram.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.DLInputOperation;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLogicalEntity;

/**
 * TODO describe this class please.
 */
public class DLInputOperationTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link at.ac.tuwien.kr.dlprogram.DLInputAddOperation#toString()}.
	 */
	@Test
	public void testToString() {
		OWLLogicalEntity S1 = OWLManager.getOWLDataFactory().getOWLClass(IRI.create("S1"));
		NormalPredicate p1 = CacheManager.getInstance().getPredicate("p1", 1);
        DLInputOperation S1_uplus_p1 = new DLInputOperation(S1, p1);
		assertEquals("S1 += p1", S1_uplus_p1.toString());
	}

}
