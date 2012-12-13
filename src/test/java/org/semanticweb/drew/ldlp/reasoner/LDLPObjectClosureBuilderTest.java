package org.semanticweb.drew.ldlp.reasoner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.drew.ldlp.reasoner.LDLPClosure;
import org.semanticweb.drew.ldlp.reasoner.LDLPClosureBuilder;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyIntersectionOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyTransitiveClosureOf;
//
//import edu.stanford.db.lp.ProgramClause;

public class LDLPObjectClosureBuilderTest {

	//private List<ProgramClause> clauses;
	
	private OWLOntologyManager manager;
	private OWLDataFactory factory;
	private OWLIndividual a;
	private OWLIndividual b;
	private OWLIndividual c;
	private OWLClass A;
	private OWLClass B;
	private OWLClass C;
	private OWLObjectProperty E;
	private OWLObjectProperty F;
	LDLPClosureBuilder builder;
	OWLOntology ontology;

	@Before
	public void setUp() {
		//clauses = new ArrayList<ProgramClause>();
		
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		A = factory.getOWLClass(IRI.create("A"));
		B = factory.getOWLClass(IRI.create("B"));
		C = factory.getOWLClass(IRI.create("C"));
		a = factory.getOWLNamedIndividual(IRI.create("a"));
		b = factory.getOWLNamedIndividual(IRI.create("b"));
		c = factory.getOWLNamedIndividual(IRI.create("c"));
		E = factory.getOWLObjectProperty(IRI.create("E"));
		F = factory.getOWLObjectProperty(IRI.create("F"));
		builder = new LDLPClosureBuilder();
	}

	// a:A
	@Test
	public void testVisitOWLClassAssertionAxiom1() throws OWLOntologyCreationException {
		final OWLClassAssertionAxiom a_is_A = factory.getOWLClassAssertionAxiom(A, a);
		Set<OWLAxiom> axioms = new HashSet<>();
		axioms.add(a_is_A);
		ontology = manager.createOntology(axioms);
		final LDLPClosure closure = builder.build(ontology);
		assertEquals(1, closure.getNamedClasses().size());
		assertTrue(closure.getNamedClasses().contains(A));
		assertEquals(0, closure.getComplexClassExpressions().size());
		assertEquals(0, closure.getComplexPropertyExpressions().size());
		assertEquals(0, closure.getNamedProperties().size());
		assertEquals(1, closure.getNamedIndividuals().size());
	}

	// a:A and B and C
	@Test
	public void testVisitOWLClassAssertionAxiom2() throws OWLOntologyCreationException {
		final OWLObjectIntersectionOf A_and_B_and_C = factory.getOWLObjectIntersectionOf(A, B, C);
		final OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(A_and_B_and_C, a);
		Set<OWLAxiom> axioms = new HashSet<>();
		axioms.add(axiom);
		ontology = manager.createOntology(axioms);
		final LDLPClosure closure = builder.build(ontology);

		assertEquals(3, closure.getNamedClasses().size());
		assertTrue(closure.getNamedClasses().contains(A));
		assertTrue(closure.getNamedClasses().contains(B));
		assertTrue(closure.getNamedClasses().contains(B));

		assertEquals(1, closure.getComplexClassExpressions().size());
		assertTrue(closure.getComplexClassExpressions().contains(A_and_B_and_C));

		assertEquals(0, closure.getComplexPropertyExpressions().size());
		assertEquals(0, closure.getNamedProperties().size());
		assertEquals(1, closure.getNamedIndividuals().size());
	}

//	@Test
//	public void testVisitOWLObjectPropertyAssertionAxiom() throws OWLOntologyCreationException {
//		final LDLObjectPropertyIntersectionOf property = factory.getLDLObjectPropertyIntersectionOf(E, F);
//		final OWLAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(property, a, b);
//		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
//		axioms.add(axiom);
//		ontology = manager.createOntology(axioms);
//		final LDLPClosure closure = builder.build(ontology);
//		assertEquals(0, closure.getNamedClasses().size());
//
//		assertEquals(0, closure.getComplexClassExpressions().size());
//
//		assertEquals(1, closure.getComplexPropertyExpressions().size());
//		assertTrue(closure.getComplexPropertyExpressions().contains(property));
//
//		assertEquals(2, closure.getNamedProperties().size());
//		assertEquals(2, closure.getNamedIndividuals().size());
//	}

	@Test
	public void testVisitOWLSubClassOfAxiom() throws OWLOntologyCreationException {

		final OWLAxiom axiom = factory.getOWLSubClassOfAxiom(A, B);
		Set<OWLAxiom> axioms = new HashSet<>();
		axioms.add(axiom);
		ontology = manager.createOntology(axioms);
		final LDLPClosure closure = builder.build(ontology);
		assertEquals(2, closure.getNamedClasses().size());

		assertEquals(0, closure.getComplexClassExpressions().size());

		assertEquals(0, closure.getComplexPropertyExpressions().size());

		assertEquals(0, closure.getNamedProperties().size());
		assertEquals(0, closure.getNamedIndividuals().size());
	}

	@Test
	public void testVisitOWLSubObjectPropertyOfAxiom() throws OWLOntologyCreationException {
		final OWLAxiom axiom = factory.getOWLSubObjectPropertyOfAxiom(E, F);
		Set<OWLAxiom> axioms = new HashSet<>();
		axioms.add(axiom);
		ontology = manager.createOntology(axioms);
		final LDLPClosure closure = builder.build(ontology);
		assertEquals(0, closure.getNamedClasses().size());

		assertEquals(0, closure.getComplexClassExpressions().size());

		assertEquals(0, closure.getComplexPropertyExpressions().size());

		assertEquals(2, closure.getNamedProperties().size());
		assertEquals(0, closure.getNamedIndividuals().size());
	}

	// A or B or C subClassOf E some A
	@Test
	public void testVisitOWLObjectUnionOf() throws OWLOntologyCreationException {
		final OWLObjectUnionOf sub = factory.getOWLObjectUnionOf(A, B, C);
		final OWLObjectSomeValuesFrom sup = factory.getOWLObjectSomeValuesFrom(E, A);

		final OWLAxiom axiom = factory.getOWLSubClassOfAxiom(sub, sup);
		Set<OWLAxiom> axioms = new HashSet<>();
		axioms.add(axiom);
		ontology = manager.createOntology(axioms);
		final LDLPClosure closure = builder.build(ontology);
		assertEquals(3, closure.getNamedClasses().size());

		assertEquals(2, closure.getComplexClassExpressions().size());

		assertEquals(0, closure.getComplexPropertyExpressions().size());

		assertEquals(1, closure.getNamedProperties().size());
		assertEquals(0, closure.getNamedIndividuals().size());
	}

	//a:(E and E and F) some (E^+ all A) 

//	@Test
//	public void testVisitOWLObjectAllValuesFrom() throws OWLOntologyCreationException {
//		final LDLObjectPropertyIntersectionOf E_and_E_and_F = factory.getLDLObjectPropertyIntersectionOf(E,E,F);
//		final LDLObjectPropertyTransitiveClosureOf Et = factory.getLDLObjectPropertyTransitiveClosureOf(E);
//		final OWLObjectAllValuesFrom Ep_all_A = factory.getOWLObjectAllValuesFrom(Et, A);
//		final OWLObjectSomeValuesFrom cls = factory.getOWLObjectSomeValuesFrom(E_and_E_and_F, Ep_all_A);
//		final OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(cls, a);
//		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
//		axioms.add(axiom);
//		ontology = manager.createOntology(axioms);
//		final LDLPClosure closure = builder.build(ontology);
//		assertEquals(1, closure.getNamedClasses().size());
//
//		assertEquals(2, closure.getComplexClassExpressions().size());
//
//		assertEquals(2, closure.getComplexPropertyExpressions().size());
//
//		assertEquals(2, closure.getNamedProperties().size());
//		assertEquals(1, closure.getNamedIndividuals().size());
//	}

	@Test
	public void testVisitOWLObjectHasValue() {

	}

	@Test
	public void testVisitOWLObjectMinCardinality() {

	}

	@Test
	public void testVisitOWLObjectExactCardinality() {

	}

	@Test
	
	public void testVisitOWLObjectOneOf() {
	
	}


	@Test
	public void testVisitOWLObjectInverseOf() {

	}

	@Test
	public void testVisitOWLDataProperty() {

	}

	@Test
	public void testVisitLDLObjectPropertyIntersectionOf() {

	}

	@Test
	public void testVisitLDLObjectPropertyUnionOf() {

	}



	@Test
	public void testVisitLDLObjectPropertyChainOf() {

	}

	@Test
	public void testVisitOWLNamedIndividual() {

	}

	@Test
	public void testVisitOWLAnonymousIndividual() {

	}

}
