package org.semanticweb.drew.ldlp.reasoner;

import static org.junit.Assert.*;
import static org.semanticweb.drew.helper.LDLHelper.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Variable;
import org.semanticweb.drew.ldlp.reasoner.LDLPClosure;
import org.semanticweb.drew.ldlp.reasoner.LDLPClosureBuilder;
import org.semanticweb.drew.ldlp.reasoner.LDLPClosureCompiler;
import org.semanticweb.drew.ldlp.reasoner.LDLPCompilerManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyChainOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyOneOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyTransitiveClosureOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLIndividualPair;



public class ClosureCompilerTest {
	Variable X = CacheManager.getInstance().getVariable("X");
	Variable Y = CacheManager.getInstance().getVariable("Y");
	Variable Z = CacheManager.getInstance().getVariable("Z");
	Variable X1 = CacheManager.getInstance().getVariable("X1");
	Variable X2 = CacheManager.getInstance().getVariable("X2");
	Variable X3 = CacheManager.getInstance().getVariable("X3");
	Variable Y1 = CacheManager.getInstance().getVariable("Y1");
	Variable Y2 = CacheManager.getInstance().getVariable("Y2");
	Variable Y3 = CacheManager.getInstance().getVariable("Y3");
	
	
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
	public LDLPClosureCompiler closureCompiler;
	LDLPClosure closure;
	private String TOP1 = LDLPCompilerManager.getInstance().getTop1();
	private String TOP2 = LDLPCompilerManager.getInstance().getTop2();
	private String NOTEQUAL = LDLPCompilerManager.getInstance().getNotEqual();

	@Before
	public void setUp() {

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
		closureCompiler = new LDLPClosureCompiler();

	}

	@Test
	public void testClosureCompiler() {

	}

	@Test
	public void testCompile() {

	}

	// a:A
	@Test
	public void testVisitOWLClass() throws OWLOntologyCreationException {
		final OWLClassAssertionAxiom a_is_A = factory.getOWLClassAssertionAxiom(A, a);
		Set<OWLAxiom> axioms = new HashSet<>();
		axioms.add(a_is_A);
		ontology = manager.createOntology(axioms);
		final LDLPClosure closure = builder.build(ontology);
		final List<Clause> clauses = closureCompiler.compile(closure);

		assertEquals(1, clauses.size());
		assertEquals("top1(X) :- p0(X).", clauses.get(0).toString());

	}

	// a:A and B and C
	// A(a).
	// (A and B and C)(X):-A(X),B(X),C(X)
	@Test
	public void testVisitOWLObjectIntersectionOf() {
		final OWLClassExpression A_and_B_and_C = and(A, B, C);
		closure = builder.build(assert$(A_and_B_and_C, a));
		final List<Clause> clauses = closureCompiler.compile(closure);
		assertTrue(clauses.contains(clause(head(literal(TOP1, X)), body(literal(p(A), X)))));
		assertTrue(clauses.contains(clause(head(literal(p(A_and_B_and_C), X)), body(literal(p(A), X), literal(p(B), X), literal(p(C), X)))));
	}

	// a:A or B or C
	// (A or B or C)(X):-A(X).
	// (A or B or C)(X):-B(X).
	// (A or B or C)(X):-C(X).
	@Test
	public void testVisitOWLObjectUnionOf() {
		final OWLClassExpression A_or_B_or_C = or(A, B, C);
		closure = builder.build(assert$(A_or_B_or_C, a));
		final List<Clause> clauses = closureCompiler.compile(closure);
		assertTrue(clauses.contains(clause(head(literal(TOP1, X)), body(literal(p(A), X)))));
		assertTrue(clauses.contains(clause(head(literal(p(A_or_B_or_C), X)), body(literal(p(A), X)))));
	}

	// a:E some A
	// (E some A)(X):-E(X,Y),A(Y).
	@Test
	public void testVisitOWLObjectSomeValuesFrom() {
		final OWLClassExpression E_some_A = some(E, A);
		closure = builder.build(assert$(E_some_A, a));
		final List<Clause> clauses = closureCompiler.compile(closure);
		assertTrue(clauses.contains(clause(head(literal(TOP1, X)), body(literal(p(A), X)))));
		assertTrue(clauses.contains(clause(head(literal(p(E_some_A), X)), body(literal(p(E), X, Y), literal(p(A), Y)))));
	}

	@Test
	public void testVisitOWLObjectAllValuesFrom() {

	}

	/**
	 * <pre>
	 * (E min n D)(X):- E(X,Y1),D(Y1),...,E(X,Yn),D(Yn), 
	 * 					Y1 != Y2, Y1 != Y3, ..., Yn-1 != Yn
	 * </pre>
	 */
	@Test
	public void testVisitOWLObjectMinCardinality() {
		final OWLObjectMinCardinality E_min_2_C = min(2, E, C);
		closure = builder.build(assert$(E_min_2_C, a));
		final List<Clause> clauses = closureCompiler.compile(closure);
		assertTrue(clauses.contains(clause(head(literal(p(E_min_2_C), X)), //
				body(literal(p(E), X, Y1), literal(p(C), Y1), literal(p(E), X, Y2), //
						literal(p(C), Y2), literal(NOTEQUAL, Y1, Y2)))));
	}

	/**
	 * <pre>
	 * 	{o1,o2}
	 * 
	 * {o1,o2)(o1).
	 * {o1,o2}(o2).
	 * </pre>
	 */
	@Test
	public void testVisitOWLObjectOneOf() {
		final OWLObjectOneOf cls = oneOf(a, b);
		final OWLSubClassOfAxiom axiom = sub(cls, A);
		closure = builder.build(axiom);
		final List<Clause> clauses = closureCompiler.compile(closure);
		Clause clause1 = clause(head(literal(p(cls), term(a))), body());
		assertTrue(clauses.contains(clause1));
		Clause clause2 = clause(head(literal(p(cls), term(b))), body());
		assertTrue(clauses.contains(clause2));
	}

	// E(X,Y):-inv(E)(Y,X).
	@Test
	public void testVisitOWLObjectInverseOf() {
		final OWLObjectInverseOf inv_E = inv(E);
		final OWLObjectPropertyAssertionAxiom axiom = assert$(inv_E, a, b);
		closure = builder.build(axiom);
		final List<Clause> clauses = closureCompiler.compile(closure);
		assertTrue(clauses.contains(clause(head(literal(p(E), X, Y)), body(literal(p(inv_E), Y, X)))));
	}

//	// and(E,F)(X,Y):-E(X,Y),F(X,Y).
//	@Test
//	public void testVisitLDLObjectPropertyIntersectionOf() {
//		final OWLObjectPropertyExpression E_and_F = and(E, F);
//		closure = builder.build(assert$(E_and_F, a, b));
//		final List<Clause> clauses = closureCompiler.compile(closure);
//		assertTrue(clauses.contains(clause(head(literal(TOP2, X, Y)), body(literal(p(E), X, Y)))));
//		assertTrue(clauses.contains(clause(head(literal(p(E_and_F), X, Y)), body(literal(p(E), X, Y), literal(p(F), X, Y)))));
//	}
//
//	@Test
//	public void testVisitLDLObjectPropertyUnionOf() {
//		final OWLObjectPropertyExpression E_or_F = or(E, F);
//		closure = builder.build(assert$(E_or_F, a, b));
//		final List<Clause> clauses = closureCompiler.compile(closure);
//		assertTrue(clauses.contains(clause(head(literal(TOP2, X, Y)), body(literal(p(E), X, Y)))));
//		assertTrue(clauses.contains(clause(head(literal(p(E_or_F), X, Y)), body(literal(p(E), X, Y)))));
//	}

//	// trans(E)(X,Y):-E(X,Y)
//	// trans(E)(X,Z):-E(X,Y),trans(Y,Z).
//	@Test
//	public void testVisitLDLObjectPropertyTransitiveClosureOf() {
//		final LDLObjectPropertyTransitiveClosureOf trans_E = trans(E);
//		final OWLObjectPropertyAssertionAxiom axiom = assert$(trans_E, a, b);
//		closure = builder.build(axiom);
//		final List<Clause> clauses = closureCompiler.compile(closure);
//		assertTrue(clauses.contains(clause(head(literal(p(trans_E), X, Y)), body(literal(p(E), X, Y)))));
//		assertTrue(clauses.contains(clause(head(literal(p(trans_E), X, Z)), body(literal(p(E), X, Y), literal(p(trans_E), Y, Z)))));
//	}
//
//	// compose(E1, E2, ... En)(X1,Xn+1):- E1(X1,X2), E2(X2,X3), ... ,
//	// En(Xn,Xn+1)
//	@Test
//	public void testVisitLDLObjectPropertyChainOf() {
//		final LDLObjectPropertyChainOf E_o_F = compose(E, F);
//		final OWLObjectPropertyAssertionAxiom axiom = assert$(E_o_F, a, b);
//		closure = builder.build(axiom);
//		final List<Clause> clauses = closureCompiler.compile(closure);
//		assertTrue(clauses.contains(clause(head(literal(p(E_o_F), X1, X3)), body(literal(p(E), X1, X2), literal(p(F), X2, X3)))));
//	}
//
//	/**
//	 * <pre>
//	 * 	{<o11,o12>,<o21,o22>)
//	 * 
//	 * {<o11,o12>,<o21,o22>)(o11,o12).
//	 * {<o11,o12>,<o21,o22>)(o21,o22).
//	 * </pre>
//	 * 
//	 */
//	@Test
//	public void testVisitOWLObjectPropertyOneOf() {
//		LDLIndividualPair pair1 = pair(a, b);
//		LDLIndividualPair pair2 = pair(b, c);
//		final LDLObjectPropertyOneOf prop = oneOf(pair1, pair2);
//		final OWLSubObjectPropertyOfAxiom axiom = sub(prop, E);
//		closure = builder.build(axiom);
//		final List<Clause> clauses = closureCompiler.compile(closure);
//		assertTrue(clauses.contains(clause(head(literal(p(prop), term(a), term(b))), body())));
//		assertTrue(clauses.contains(clause(head(literal(p(prop), term(b), term(c))), body())));
//	}

}
