package org.semanticweb.drew.ldlp.reasoner;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;

public class LDLPCompilerManagerTest {

	@Test
	public void testConstants_Int() {
		LDLPCompilerManager manager = LDLPCompilerManager.getInstance();
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLLiteral intLiteral = factory.getOWLLiteral(123);
		String s1 = manager.getConstant(intLiteral);
		System.out.println(s1);
		assertEquals("123", s1);
	}
	
	@Test
	public void testConstants_Double() {
		LDLPCompilerManager manager = LDLPCompilerManager.getInstance();
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLLiteral literal = factory.getOWLLiteral(123.00);
		String s1 = manager.getConstant(literal);
		System.out.println(s1);
		assertEquals("123.0", s1);
	}

	@Test
	public void testConstants_String() {
		LDLPCompilerManager manager = LDLPCompilerManager.getInstance();
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLLiteral literal = factory.getOWLLiteral("123.00");
		String s1 = manager.getConstant(literal);
		System.out.println(s1);
		//assertEquals("123.00", s1);
	}
	
	@Test
	public void testConstants_OWLIndividual() {
		LDLPCompilerManager manager = LDLPCompilerManager.getInstance();
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLLiteral literal = factory.getOWLLiteral("123.00");
		String s1 = manager.getConstant(literal);
		System.out.println(s1);
		assertEquals("123.00", s1);
	}
}
