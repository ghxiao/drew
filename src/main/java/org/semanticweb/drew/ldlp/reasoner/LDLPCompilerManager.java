/*
 * @(#)PredicateManager.java 2010-3-25 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.ldlp.reasoner;


import org.semanticweb.drew.el.SymbolEncoder;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LDLPCompilerManager
 */
public class LDLPCompilerManager {

	private static LDLPCompilerManager instance = new LDLPCompilerManager();

	final static Logger logger = LoggerFactory.getLogger(LDLPCompilerManager.class);

	public static LDLPCompilerManager getInstance() {
		return instance;
	}

	public LDLPCompilerManager() {

	}

	SymbolEncoder<String> predicates = new SymbolEncoder<String>();

	SymbolEncoder<String> constants = new SymbolEncoder<String>();

	final String TOP = "top";

	public String getTop1() {
		return TOP + "1";
	}

	public String getTop2() {
		return TOP + "2";
	}

	public String getEqual() {
		return "=";
	}

	public String getNotEqual() {
		return "!=";
	}

	public String getPredicate(OWLObject owlObject) {

		String predicate;

		if (owlObject instanceof OWLClass && ((OWLEntity) owlObject).isTopEntity()) {
			predicate = getTop1();
		} else if (owlObject instanceof OWLObjectProperty && ((OWLEntity) owlObject).isTopEntity()) {
			predicate = getTop2();
		} else {
			final String iri = owlObject.toString();
			predicate = "p" + predicates.encode(iri);
		}
		logger.debug("{}  ->  {}", owlObject, predicate);

		return predicate;

	}

	public String getConstant(OWLIndividual individual) {

		// OWLIndividual individual = OWLManager.getOWLDataFactory()
		// .getOWLNamedIndividual(IRI.create(name));

		String iri = individual.asOWLNamedIndividual().toString();

		return getConstant(iri);

		// return constant;
	}

	public String getConstant(OWLLiteral literal) {
		String iri = literal.toString();

		return getConstant(iri);
	}

	public String getConstant(String iri) {

		String constant = "o" + constants.encode(iri);

		logger.debug("{}  ->  {}", iri, constant);

		return constant;
	}

	public String getPredicate(String name) {
		String predicate = "p" + predicates.encode(name);
		logger.debug("{}  ->  {}", name, predicate);

		return predicate;
	}

	public String decompile(String name) {

		int index = Integer.parseInt(name.substring(1));

		if (name.startsWith("p")) { // predicate
			return predicates.decode(index);
		} else if (name.startsWith("o")) { // constant
			return constants.decode(index);
		} else {
			throw new IllegalStateException();
		}

	}

	public void reset() {
		predicates = new SymbolEncoder<String>();
		constants = new SymbolEncoder<String>();
	}

	public void dump() {
		//predicates.dump();
		//constants.dump();

	}
}
