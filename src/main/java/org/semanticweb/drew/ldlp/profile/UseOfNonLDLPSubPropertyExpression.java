/*
 * @(#)UseOfNonSubObjectPropertyExpression.java 2010-3-17 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.ldlp.profile;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;

/**
 * TODO describe this class please.
 */
class UseOfNonLDLPSubPropertyExpression extends OWLProfileViolation {

	@SuppressWarnings("unchecked")
	private OWLPropertyExpression propertyExpression;

	@SuppressWarnings("unchecked")
	public UseOfNonLDLPSubPropertyExpression(OWLOntology ontology, OWLAxiom axiom, OWLPropertyExpression propertyExpression) {
		super(ontology, axiom);
		this.propertyExpression = propertyExpression;
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Use of non-subproperty expression in position that requires a subproperty expression: ");
        sb.append(propertyExpression);
        sb.append(" [");
        sb.append(getAxiom());
        sb.append(" in ");
        sb.append(getOntologyID());
        sb.append("]");
        return sb.toString();
    }
	
}
