/*
 * @(#)LDLDataFactoryHelper.java 2010-3-26 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.helper;

import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.ldlp.reasoner.LDLPCompilerManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;


//import edu.stanford.db.lp.Literal;
//import edu.stanford.db.lp.ProgramClause;
//import edu.stanford.db.lp.StringTerm;
//import edu.stanford.db.lp.Term;

/**
 * TODO describe this class please.
 */
public class LDLHelper {

	private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	private static OWLDataFactory factory = manager.getOWLDataFactory();

	public static OWLClass cls(String iri) {
		return factory.getOWLClass(IRI.create(iri));
	}

	public static OWLObjectProperty prop(String iri) {
		return factory.getOWLObjectProperty(IRI.create(iri));
	}

	public static OWLNamedIndividual ind(String iri) {
		return factory.getOWLNamedIndividual(IRI.create(iri));
	}

	public static OWLClass topClass() {
		return factory.getOWLThing();
	}

	public static OWLSubClassOfAxiom sub(OWLClassExpression sub, OWLClassExpression sup) {
		return factory.getOWLSubClassOfAxiom(sub, sup);
	}

	public static OWLSubObjectPropertyOfAxiom sub(OWLObjectPropertyExpression sub, OWLObjectPropertyExpression sup) {
		return factory.getOWLSubObjectPropertyOfAxiom(sub, sup);
	}

	public static OWLClassExpression and(OWLClassExpression... operands) {
		return factory.getOWLObjectIntersectionOf(operands);
	}

//	public static OWLObjectPropertyExpression and(OWLObjectPropertyExpression... operands) {
//		return factory.getLDLObjectPropertyIntersectionOf(operands);
//	}

	public static OWLClassExpression or(OWLClassExpression... operands) {
		return factory.getOWLObjectUnionOf(operands);
	}

//	public static OWLObjectPropertyExpression or(OWLObjectPropertyExpression... operands) {
//		return factory.getLDLObjectPropertyUnionOf(operands);
//	}

	public static OWLClassAssertionAxiom assert$(OWLClassExpression cls, OWLIndividual ind) {
		return factory.getOWLClassAssertionAxiom(cls, ind);
	}

	public static OWLObjectPropertyAssertionAxiom assert$(OWLObjectPropertyExpression prop, OWLIndividual a, OWLIndividual b) {
		return factory.getOWLObjectPropertyAssertionAxiom(prop, a, b);
	}

	public static OWLObjectSomeValuesFrom some(OWLObjectPropertyExpression prop, OWLClassExpression cls) {
		return factory.getOWLObjectSomeValuesFrom(prop, cls);
	}

	public static OWLObjectAllValuesFrom all(OWLObjectPropertyExpression prop, OWLClassExpression cls) {
		return factory.getOWLObjectAllValuesFrom(prop, cls);
	}

	public static OWLObjectInverseOf inv(OWLObjectPropertyExpression prop) {
		return factory.getOWLObjectInverseOf(prop);
	}

//	public static LDLObjectPropertyTransitiveClosureOf trans(OWLObjectPropertyExpression prop) {
//		return factory.getLDLObjectPropertyTransitiveClosureOf(prop);
//	}
//
//	public static LDLObjectPropertyChainOf compose(OWLObjectPropertyExpression... props) {
//		return factory.getLDLObjectPropertyChainOf(props);
//	}

	public static OWLObjectMinCardinality min(int n, OWLObjectPropertyExpression property, OWLClassExpression cls) {
		return factory.getOWLObjectMinCardinality(n, property, cls);
	}

	public static OWLObjectOneOf oneOf(OWLIndividual... individuals) {
		return factory.getOWLObjectOneOf(individuals);
	}
	
//	public static LDLObjectPropertyOneOf oneOf(LDLIndividualPair... pairs) {
//		return factory.getLDLObjectPropertyOneOf(pairs);
//	}
//	
//	public static LDLIndividualPair pair(OWLIndividual first, OWLIndividual second){
//		return factory.getLDLIndiviualPair(first, second);
//	}

	public static Clause clause(Literal[] head, Literal[] body) {
		return new Clause(head, body);
	}

	public static Literal[] head(Literal... literals) {
		return literals;
	}

	public static Literal[] body(Literal... literals) {
		return literals;
	}

	public static Literal literal(String prediate, Term... terms) {
		return new Literal(prediate, terms);
	}

	public static String p(OWLObject owlObject) {
		LDLPCompilerManager factory = LDLPCompilerManager.getInstance();
		return factory.getPredicate(owlObject);
	}

	public static Term term(OWLIndividual individual) {
		LDLPCompilerManager factory = LDLPCompilerManager.getInstance();
		final String const1 = factory.getConstant(individual);
		return CacheManager.getInstance().getConstant(const1);
	}

}
