/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 */

package org.semanticweb.drew.dlprogram.model;

import org.semanticweb.drew.default_logic.OWLPredicate;
import org.semanticweb.owlapi.model.OWLLogicalEntity;

/**
 * DL Atom predicate in form of DL[S1+=p1, ..., Sn+=pn; Q]
 */
public class DLAtomPredicate implements Predicate {

	private DLInputSignature inputSignature;

	// only DL concept or role name allowed
	OWLLogicalEntity query;

	private int arity;

	public DLAtomPredicate(DLInputSignature inputSignature, OWLLogicalEntity query) {
		super();
		this.inputSignature = inputSignature;
		this.query = query;
	}

	public DLAtomPredicate() {

	}


	/**
	 * @return the query
	 */
	public OWLLogicalEntity getQuery() {
		return query;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(OWLLogicalEntity query) {
		this.query = query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DL[");
		builder.append(inputSignature);
		builder.append(";");
		builder.append(query.getIRI());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Predicate o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setArity(int arity) {
		this.arity = arity;
	}

	public int getArity() {
		return arity;
	}

	public DLInputSignature getInputSignature() {
		return inputSignature;
	}

	public void setInputSignature(DLInputSignature inputSignature) {
		this.inputSignature = inputSignature;
	}

	@Override
	public OWLPredicate asOWLPredicate() {
		throw new UnsupportedOperationException();
	}

}
