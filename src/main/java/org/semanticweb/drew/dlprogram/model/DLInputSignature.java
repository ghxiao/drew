/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 */

package org.semanticweb.drew.dlprogram.model;

import java.util.ArrayList;
import java.util.List;

/**
 * [S1 op1 p1, ..., Sn op_n p_n]
 */
public class DLInputSignature {

	List<DLInputOperation> operations = new ArrayList<>();

	public final static DLInputSignature EMPTY = new DLInputSignature();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;

		for (DLInputOperation op : operations) {
			if (!first)
				sb.append(",");
			sb.append(op);
			first = false;
		}

		return sb.toString();
	}

	/**
	 * @return the operations
	 */
	public List<DLInputOperation> getOperations() {
		return operations;
	}

	/**
	 * @param operations
	 *            the operations to set
	 */
	public void setOperations(List<DLInputOperation> operations) {
		this.operations = operations;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((operations == null) ? 0 : operations.hashCode());
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
		DLInputSignature other = (DLInputSignature) obj;
		if (operations == null) {
			if (other.operations != null)
				return false;
		} else if (!operations.equals(other.operations))
			return false;
		return true;
	}

}
