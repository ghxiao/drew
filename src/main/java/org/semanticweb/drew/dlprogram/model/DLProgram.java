/* 
 * Copyright (C) 2011 Guohui Xiao
 * Copyright (C) 2006-2009 Samuel
 */

package org.semanticweb.drew.dlprogram.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The implementation of DL-Programs.
 * 
 * 
 */
public class DLProgram implements Cloneable {

	public DLProgram() {

	}

	private List<ProgramStatement> statements = new ArrayList<ProgramStatement>();

	private List<Clause> clauses = new ArrayList<Clause>();

	/**
	 * Return the clauses contained in the program.
	 * 
	 * @return
	 */
	// public List<Clause> getClauses() {
	// return clauses;
	// }

	/**
	 * Get all the clauses with the given head predicate name and given clause
	 * type.
	 * 
	 * @param predicate
	 *            the interested predicate
	 * @param type
	 *            the type of interested clauses
	 * @return set of clauses of the given type about the predicate
	 */
	public List<Clause> getClausesAboutPredicate(NormalPredicate predicate,
			ClauseType type) {
		List<Clause> result = new ArrayList<Clause>();

		for (Clause clause : clauses) {
			if (clause.getHead().getPredicate().equals(predicate)
					&& clause.getType() == type) {
				result.add(clause);
			}
		}

		return result;
	}

	/**
	 * Normalizes the program to canonical form. There will be no duplicate
	 * positive or negative body literals and no duplicate clauses in the
	 * canonical form. Also the literals and clauses are sorted.
	 */
	public void normalize() {
		for (Clause clause : clauses) {
			Set<Literal> pos = new TreeSet<Literal>();
			Set<Literal> neg = new TreeSet<Literal>();

			pos.addAll(clause.getPositiveBody());
			neg.addAll(clause.getNegativeBody());

			clause.positives = new ArrayList<Literal>(pos);
			clause.negatives = new ArrayList<Literal>(neg);
		}

		Set<Clause> result = new TreeSet<Clause>();
		result.addAll(clauses);
		clauses = new ArrayList<Clause>(result);
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();

		for (Clause clause : clauses) {
			result.append(clause).append("\n");
		}

		return result.toString();
	}

	@Override
	public DLProgram clone() {
		try {
			DLProgram result = (DLProgram) super.clone();
			result.clauses = new ArrayList<Clause>(clauses);
			return result;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // cannot happen
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DLProgram) {
			DLProgram that = (DLProgram) obj;

			if (clauses.equals(that.clauses)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		int result = 0;

		for (Clause clause : clauses) {
			result += clause.hashCode();
		}

		return result;
	}

	public Set<DLInputSignature> getDLInputSignatures(boolean withEmpty) {
		Set<DLInputSignature> signatures = new HashSet<DLInputSignature>();

		if (withEmpty) {
			signatures.add(DLInputSignature.EMPTY);
		}
		for (ProgramStatement s : this.getStatements()) {
			if (s instanceof Clause) {
				Clause clause = (Clause)s;
				signatures.addAll(clause.getDLInputSignatures());
			}
		}
//		for (Clause clause : this.getClauses()) {
//			signatures.addAll(clause.getDLInputSignatures());
//		}
		return signatures;
	}

	public Set<DLInputSignature> getDLInputSignatures() {
		return getDLInputSignatures(false);
		// Set<DLInputSignature> signatures = new HashSet<DLInputSignature>();
		// // signatures.add(DLInputSignature.EMPTY);
		//
		// for (Clause clause : this.getClauses()) {
		// signatures.addAll(clause.getDLInputSignatures());
		// }
		// return signatures;
	}

	public Set<DLAtomPredicate> getDLAtomPredicates() {
		Set<DLAtomPredicate> dlAtoms = new HashSet<DLAtomPredicate>();

		for (ProgramStatement s : this.getStatements()) {
			if (s instanceof Clause) {
				Clause clause = (Clause)s;
				dlAtoms.addAll(clause.getDLAtomPredicates());
			}
		}

		// for (Clause clause : this.getClauses()) {
		// dlAtoms.addAll(clause.getDLAtomPredicates());
		// }

		return dlAtoms;
	}

	public void addAll(List<ProgramStatement> statements) {
		this.statements.addAll(statements);
	}

	public void add(ProgramStatement s) {
		this.statements.add(s);
	}

	public List<ProgramStatement> getStatements() {
		return statements;
	}

}