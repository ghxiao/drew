/*
 * @(#)LDLPObjectClosure 2010-3-26 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.ldlp.reasoner;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concepts, properites, indviduals in the ontology
 */
public class LDLPClosure {

	private final static Logger logger = LoggerFactory.getLogger(LDLPClosure.class);

	private Set<OWLIndividual> namedIndividuals;
	private Set<OWLClass> namedClasses;
	private Set<OWLObjectProperty> namedProperties;
	private Set<OWLClassExpression> complexClassExpressions;
	private Set<OWLObjectPropertyExpression> complexPropertyExpressions;
	private Set<OWLDataProperty> namedDataProperties;
	private Set<OWLLiteral> literals;

	public LDLPClosure() {
		namedClasses = new HashSet<>();
		namedIndividuals = new HashSet<>();
		namedProperties = new HashSet<>();
		namedDataProperties = new HashSet<>();
		literals = new HashSet<>();
		complexClassExpressions = new HashSet<>();
		complexPropertyExpressions = new HashSet<>();
		
	}

	/**
	 * @return the namedIndividuals
	 */
	public Set<OWLIndividual> getNamedIndividuals() {
		return namedIndividuals;
	}

	/**
	 * @return the namedClasses
	 */
	public Set<OWLClass> getNamedClasses() {
		return namedClasses;
	}

	/**
	 * @return the namedProperties
	 */
	public Set<OWLObjectProperty> getNamedProperties() {
		return namedProperties;
	}

	/**
	 * @return the complexClassExpressions
	 */
	public Set<OWLClassExpression> getComplexClassExpressions() {
		return complexClassExpressions;
	}

	/**
	 * @return the complexPropertyExpressions
	 */
	public Set<OWLObjectPropertyExpression> getComplexPropertyExpressions() {
		return complexPropertyExpressions;
	}

	public LDLPClosure addNamedClasses(OWLClass cls) {
		this.namedClasses.add(cls);
		logger.debug("Class {} added to the closure", cls);
		return this;
	}

	public LDLPClosure addNamedProperty(OWLObjectProperty property) {
		this.namedProperties.add(property);
		logger.debug("Property {} added to the closure", property);
		return this;
	}

	public LDLPClosure addNamedIndividual(OWLIndividual individual) {
		this.namedIndividuals.add(individual);
		logger.debug("Individual {} added to the closure", individual);
		return this;
	}

	public LDLPClosure addComplexClass(OWLClassExpression classExpression) {
		this.complexClassExpressions.add(classExpression);
		logger.debug("classExpression {} added to the closure", classExpression);
		return this;
	}

	public LDLPClosure addComplexProperty(OWLObjectPropertyExpression propertyExpression) {
		this.complexPropertyExpressions.add(propertyExpression);
		logger.debug("propertyExpression {} added to the closure", propertyExpression);
		return this;
	}

	public LDLPClosure addNamedDataProperty(OWLDataProperty property) {
		this.namedDataProperties.add(property);
		logger.debug("Property {} added to the closure", property);
		return this;
	}

	public LDLPClosure addLiteral(OWLLiteral literal) {
		this.literals.add(literal);
		logger.debug("Literal {} added to the closure", literal);
		return this;
	}
}
