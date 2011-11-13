package org.semanticweb.drew.default_logic;

import org.semanticweb.drew.dlprogram.model.Predicate;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLogicalEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * a wrapper for OWLLogicEntity (class or role), which is used as predicate in
 * default rules
 * 
 * @author xiao
 * 
 */

public class OWLPredicate implements Predicate {

	private OWLLogicalEntity logicalEntity;

	public OWLPredicate(OWLLogicalEntity logicalEntity) {
		this.logicalEntity = logicalEntity;
	}

	@Override
	public int compareTo(Predicate o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setArity(int arity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getArity() {
		if (logicalEntity instanceof OWLClass) {
			return 1;
		} else if (logicalEntity instanceof OWLObjectProperty) {
			return 2;
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public String toString() {
		return logicalEntity.getIRI().toString();
	}

	@Override
	public OWLPredicate asOWLPredicate() {
		return this;
	}

	public OWLLogicalEntity getLogicalEntity() {
		return logicalEntity;
	}

	public void setLogicalEntity(OWLLogicalEntity logicalEntity) {
		this.logicalEntity = logicalEntity;
	}

}
