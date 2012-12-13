/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 */

package org.semanticweb.drew.dlprogram.model;

import org.semanticweb.owlapi.model.OWLLogicalEntity;

/**
 * (S += p) or (S -= p)
 */
public class DLInputOperation {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dlPredicate == null) ? 0 : dlPredicate.hashCode());
		result = prime * result
				+ ((inputPredicate == null) ? 0 : inputPredicate.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DLInputOperation other = (DLInputOperation) obj;
		if (dlPredicate == null) {
			if (other.dlPredicate != null)
				return false;
		} else if (!dlPredicate.equals(other.dlPredicate))
			return false;
		if (inputPredicate == null) {
			if (other.inputPredicate != null)
				return false;
		} else if (!inputPredicate.equals(other.inputPredicate))
			return false;
        return type == other.type;
    }

	// only DL concept or role name allowed
	OWLLogicalEntity dlPredicate;

	NormalPredicate inputPredicate;

	DLInputOperator type;

	/**
	 * @return the dlPredicate
	 */
	public OWLLogicalEntity getDLPredicate() {
		return dlPredicate;
	}

	public DLInputOperation() {
	}

	public DLInputOperation(OWLLogicalEntity dlPredicate, NormalPredicate inputPredicate, DLInputOperator type) {
		super();
		this.dlPredicate = dlPredicate;
		this.inputPredicate = inputPredicate;
		this.type = type;
	}

	public DLInputOperation(OWLLogicalEntity dlPredicate, NormalPredicate inputPredicate) {
		this(dlPredicate, inputPredicate, DLInputOperator.U_PLUS);
	}

	/**
	 * @param dlPredicate
	 *            the dlPredicate to set
	 */
	public void setDLPredicate(OWLLogicalEntity dlPredicate) {
		this.dlPredicate = dlPredicate;
	}

	/**
	 * @return the inputPredicate
	 */
	public NormalPredicate getInputPredicate() {
		return inputPredicate;
	}

	/**
	 * @param inputPredicate
	 *            the inputPredicate to set
	 */
	public void setInputPredicate(NormalPredicate inputPredicate) {
		this.inputPredicate = inputPredicate;
	}

	/**
	 * @return the type
	 */
	public DLInputOperator getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(DLInputOperator type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(dlPredicate.getIRI());
		builder.append(" ");
		builder.append(type);
		builder.append(" ");
		builder.append(inputPredicate.getName());
		return builder.toString();
	}
}
