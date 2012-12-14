package org.semanticweb.drew.ldlp.reasoner;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;


public class AxiomCompilerTest {

	private List<ProgramStatement> clauses;
	private LDLPAxiomCompiler axiomCompiler;
    private OWLDataFactory factory;
	private OWLIndividual a;
	private OWLIndividual b;
    private OWLClass A;
	private OWLClass B;
    private OWLObjectProperty E;
	private OWLObjectProperty F;

	@Before
	public void setUp() {

		CacheManager.getInstance().reset();
		LDLPCompilerManager.getInstance().reset();
		axiomCompiler = new LDLPAxiomCompiler();

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		A = factory.getOWLClass(IRI.create("A"));
		B = factory.getOWLClass(IRI.create("B"));
        OWLClass c1 = factory.getOWLClass(IRI.create("C"));
		a = factory.getOWLNamedIndividual(IRI.create("a"));
		b = factory.getOWLNamedIndividual(IRI.create("b"));
        OWLIndividual c = factory.getOWLNamedIndividual(IRI.create("c"));
		E = factory.getOWLObjectProperty(IRI.create("E"));
		F = factory.getOWLObjectProperty(IRI.create("F"));
	}

	// A -> p1
	// a -> o0
	@Test
	public void testVisitOWLClassAssertionAxiom1() {
		final OWLClassAssertionAxiom a_is_A = factory
				.getOWLClassAssertionAxiom(A, a);
		a_is_A.accept(axiomCompiler);
		clauses = axiomCompiler.getClauses();
		assertEquals(1, clauses.size());
		assertEquals("p0(o0).", clauses.get(0).toString());
	}

	// A -> p1
	// A_and_B -> p2
	// a -> o1
	@Test
	public void testVisitOWLClassAssertionAxiom2() {
		final OWLObjectIntersectionOf A_and_B = factory
				.getOWLObjectIntersectionOf(A, B);
		final OWLClassAssertionAxiom a_is_A_and_B = factory
				.getOWLClassAssertionAxiom(A_and_B, a);
		a_is_A_and_B.accept(axiomCompiler);
		clauses = axiomCompiler.getClauses();
		assertEquals(1, clauses.size());
		assertEquals("p2(o1).", clauses.get(0).toString());
	}

	// A -> p1
	// a -> o1
	@Test
	public void testVisitOWLClassAssertionAxiom3() {
		final OWLClassAssertionAxiom a_is_A = factory
				.getOWLClassAssertionAxiom(A, a);
		a_is_A.accept(axiomCompiler);
		clauses = axiomCompiler.getClauses();
		assertEquals(1, clauses.size());
		assertEquals("p1(o1).", clauses.get(0).toString());
	}

	// E -> p3
	// a -> o1
	// b -> o2
	@Test
	public void testVisitOWLObjectPropertyAssertionAxiom() {
		final OWLObjectPropertyAssertionAxiom a_E_b = factory
				.getOWLObjectPropertyAssertionAxiom(E, a, b);
		a_E_b.accept(axiomCompiler);
		clauses = axiomCompiler.getClauses();
		assertEquals(1, clauses.size());
		assertEquals("p3(o1, o2).", clauses.get(0).toString());
	}

	// A -> p1
	// B -> p4
	@Test
	public void testVisitOWLSubClassOfAxiom() {
		final OWLSubClassOfAxiom subClassOf = factory.getOWLSubClassOfAxiom(A,
				B);
		subClassOf.accept(axiomCompiler);
		clauses = axiomCompiler.getClauses();
		assertEquals(1, clauses.size());
		assertEquals("p4(X) :- p1(X).", clauses.get(0).toString());
	}

	// E -> p3
	// F -> p5
	@Test
	public void testVisitOWLSubObjectPropertyOfAxiom() {
		final OWLSubObjectPropertyOfAxiom subPropertyOf = factory
				.getOWLSubObjectPropertyOfAxiom(E, F);
		subPropertyOf.accept(axiomCompiler);
		clauses = axiomCompiler.getClauses();
		assertEquals(1, clauses.size());
		assertEquals("p5(X, Y) :- p3(X, Y).", clauses.get(0).toString());
	}
	
	// E -> p3
	// A -> p1
	@Test
	public void testVisitOWLObjectPropertyDomainAxiom() {
		OWLAxiom axiom = factory.getOWLObjectPropertyDomainAxiom(E, A);
		axiom.accept(axiomCompiler);
		clauses = axiomCompiler.getClauses();
		assertEquals(1, clauses.size());
		assertEquals("p1(X) :- p3(X, Y).", clauses.get(0).toString());
	}
	
	// E -> p3
	// A -> p1
	@Test
	public void testVisitOWLObjectPropertyRangeAxiom() {
		OWLAxiom axiom = factory.getOWLObjectPropertyRangeAxiom(E, A);
		axiom.accept(axiomCompiler);
		clauses = axiomCompiler.getClauses();
		assertEquals(1, clauses.size());
		assertEquals("p1(Y) :- p3(X, Y).", clauses.get(0).toString());
	}
}
