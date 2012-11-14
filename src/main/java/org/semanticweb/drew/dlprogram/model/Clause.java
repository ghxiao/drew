/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 * Copyright (C) 2006-2009 Samuel
 */

package org.semanticweb.drew.dlprogram.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;



/**
 * The implementation of clause.
 * 
 */
public class Clause implements ProgramStatement, Cloneable, Comparable<Clause> {
	private static final String IMPLY = ":-";

	private Literal head = Literal.FALSE;

	List<Literal> positives = new ArrayList<Literal>();

	List<Literal> negatives = new ArrayList<Literal>();

	public Clause(Literal[] head, Literal[] body) {
		this(head[0], body);
	}

	public Clause(Literal head, Literal[] body) {
		this.head = head;
		this.positives = Arrays.asList(body);
	}

	public Clause(Literal head, List<Literal> positiveBody,
			List<Literal> negativeBody) {
		this.head = head;
		this.positives = positiveBody;
		this.negatives = negativeBody;
	}

	public Clause(Literal head, List<Literal> body) {
		this.head = head;

		this.positives = body;
	}
	
	public Clause() {

	}

	/**
	 * constructor for facts
	 */
	public Clause(Literal fact) {
		this(fact, new Literal[] {});
	}

	/**
	 * Set the head of the clause.
	 * 
	 * @param head
	 *            the head literal
	 */
	public void setHead(Literal head) {
		this.head = head;
	}

	/**
	 * Get the head literal
	 * 
	 * @return head literal
	 */
	public Literal getHead() {
		return head;
	}

	/**
	 * Get positive body literals.
	 * 
	 * @return positive body
	 */
	public List<Literal> getPositiveBody() {
		return positives;
	}

	/**
	 * Get negative body literals.
	 * 
	 * @return negative body
	 */
	public List<Literal> getNegativeBody() {
		return negatives;
	}

	/**
	 * Get predicates in positive literals.
	 * 
	 * @return predicates in positive literals
	 */
	public Set<Predicate> getPositivePredicates() {
		Set<Predicate> result = new HashSet<Predicate>();

		for (Literal literal : positives) {
			result.add(literal.getPredicate());
		}

		return result;
	}

	/**
	 * Get predicates in negative literals.
	 * 
	 * @return predicates in negative literals
	 */
	public Set<Predicate> getNegativePredicates() {
		Set<Predicate> result = new HashSet<Predicate>();

		for (Literal literal : negatives) {
			result.add(literal.getPredicate());
		}

		return result;
	}

	/**
	 * Get all body literals. Positive literals are placed before negative
	 * literals.
	 * 
	 * @return all body literals
	 */
	public Set<Literal> getBody() {
		Set<Literal> result = new TreeSet<Literal>();

		result.addAll(positives);
		result.addAll(negatives);

		return result;
	}

	/**
	 * Get normal body literals, aka, literals without built-in and logical
	 * predicates.
	 * 
	 * update: all literals except DL Atoms
	 * 
	 * @return all body literals
	 */
	public List<Literal> getNormalPositiveBody() {
		List<Literal> result = new ArrayList<Literal>();

		for (Literal literal : positives) {
			if (literal.getPredicate() instanceof NormalPredicate
			// && ((NormalPredicate) literal.getPredicate()).type
			// .equals(PredicateType.NORMAL)
			) {
				result.add(literal);
			}
		}

		return result;
	}

	public List<Literal> getNormalNegativeBody() {
		List<Literal> result = new ArrayList<Literal>();

		for (Literal literal : negatives) {
			if (literal.getPredicate() instanceof NormalPredicate
			// && ((NormalPredicate) literal.getPredicate()).type
			// .equals(PredicateType.NORMAL)
			) {
				result.add(literal);
			}
		}

		return result;
	}

	/**
	 * Get special body literals, aka, literals with built-in or logical
	 * predicates.
	 * 
	 * @return all body literals
	 */
	public Set<Literal> getSpecialPositiveBody() {
		Set<Literal> result = new TreeSet<Literal>();

		for (Literal literal : positives) {
			if (literal.getPredicate() instanceof NormalPredicate
					&& !((NormalPredicate) literal.getPredicate()).type
							.equals(PredicateType.NORMAL)) {
				result.add(literal);
			}
		}

		return result;
	}

	/**
	 * Get clause type: fact, constraint or rule.
	 * 
	 * @return clause type.
	 */
	public ClauseType getType() {
		assert (head != null);

		if ((positives.size() == 0 && negatives.size() == 0)
				|| (1 == positives.size() && positives.iterator().next()
						.equals(Literal.TRUE))) {
			return ClauseType.FACT;
		}

		if (head.equals(Literal.FALSE)) {
			return ClauseType.CONSTRAINT;
		}

		if (!head.equals(Literal.FALSE)
				&& (positives.size() + negatives.size()) > 0) {
			return ClauseType.RULE;
		}

		// should never reach here
		throw new IllegalStateException();
	}

	/**
	 * Get all terms appearing in the clause.
	 * 
	 * @return terms appearing in the clause
	 */
	public Set<Term> getTerms() {
		Set<Term> result = new HashSet<Term>();

		for (Literal literal : positives) {
			result.addAll(literal.getTerms());
		}
		for (Literal literal : negatives) {
			result.addAll(literal.getTerms());
		}

		return result;
	}

	/**
	 * Get all variables appearing in the clause.
	 * 
	 * @return terms appearing in the clause
	 */
	public Set<Variable> getVariables() {
		Set<Variable> result = new HashSet<Variable>();

		for (Term term : getTerms()) {
			if (term instanceof Variable) {
				result.add((Variable) term);
			}
		}

		return result;
	}

	/**
	 * First compare clause type, then compare clause content.
	 * 
	 * @param that
	 *            the other clause
	 * @return compare result
	 */
	public int compareTo(Clause that) {
		int result;

		result = this.getHead().compareTo(that.getHead());
		if (result != 0) {
			return result;
		}

		result = this.getPositiveBody().toString()
				.compareTo(that.getPositiveBody().toString());
		if (result != 0) {
			return result;
		}

		result = this.getNegativeBody().toString()
				.compareTo(that.getNegativeBody().toString());
		if (result != 0) {
			return result;
		}

		return 0;
	}

	/**
	 * Get the string form of body. This is a pull-up method used in toString().
	 * 
	 * @return string form of body
	 */
	private String bodyToString() {
		StringBuffer result = new StringBuffer();

		for (Literal literal : positives) {
			if (result.length() > 0) {
				result.append(", ");
			}
			result.append(literal);
		}

		for (Literal literal : negatives) {
			if (result.length() > 0) {
				result.append(", ");
			}
			result.append("not ").append(literal);
		}

		return result.toString();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		switch (getType()) {
		case FACT:
			result.append(head).append(".");
			break;
		case CONSTRAINT:
			result.append(IMPLY).append(" ").append(bodyToString()).append(".");
			break;
		case RULE:
			result.append(head).append(" ").append(IMPLY).append(" ")
					.append(bodyToString()).append(".");
			break;
		default:
			throw new IllegalStateException();
		}

		return result.toString();
	}

	@Override
	public Clause clone() {
		try {
			Clause result = (Clause) super.clone();
			result.positives = new ArrayList<Literal>(positives);
			result.negatives = new ArrayList<Literal>(negatives);
			return result;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // cannot happen
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Clause) {
			Clause that = (Clause) obj;
			if (head.equals(that.head) && positives.equals(that.positives)
					&& negatives.equals(that.negatives)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = head.hashCode();
		result = 31 * result + positives.hashCode();
		result = 31 * result + negatives.hashCode();
		return result;
	}

	public Set<DLInputSignature> getDLInputSignatures() {
		Set<DLInputSignature> signatures = new HashSet<DLInputSignature>();
		for (Literal lit : this.getDLAtoms()) {
			if (lit.getPredicate() instanceof DLAtomPredicate) {
				final DLAtomPredicate predicate = (DLAtomPredicate) (lit
						.getPredicate());
				signatures.add(predicate.getInputSignature());
			}
		}
		return signatures;
	}

	public Set<Literal> getDLAtoms() {
		Set<Literal> result = new HashSet<Literal>();

		for (Literal literal : positives) {
			if (!(literal.getPredicate() instanceof NormalPredicate))
			// && ((NormalPredicate) literal.getPredicate()).type
			// .equals(PredicateType.NORMAL))
			{
				result.add(literal);
			}
		}

		for (Literal literal : negatives) {
			if (!(literal.getPredicate() instanceof NormalPredicate))
			// && ((NormalPredicate) literal.getPredicate()).type
			// .equals(PredicateType.NORMAL))
			{
				result.add(literal);
			}
		}
		return result;
	}

	public Set<DLAtomPredicate> getDLAtomPredicates() {
		Set<DLAtomPredicate> result = new HashSet<DLAtomPredicate>();

		for (Literal literal : positives) {
			if (!(literal.getPredicate() instanceof NormalPredicate))
			// && ((NormalPredicate) literal.getPredicate()).type
			// .equals(PredicateType.NORMAL))
			{
				result.add((DLAtomPredicate) literal.getPredicate());
			}
		}

		for (Literal literal : negatives) {
			if (!(literal.getPredicate() instanceof NormalPredicate))
			// && ((NormalPredicate) literal.getPredicate()).type
			// .equals(PredicateType.NORMAL))
			{
				result.add((DLAtomPredicate) literal.getPredicate());
			}
		}
		return result;
	}

	public List<Literal> getPositiveDLAtoms() {
		List<Literal> result = new ArrayList<Literal>();

		for (Literal literal : positives) {
			if (!(literal.getPredicate() instanceof NormalPredicate)) {
				result.add(literal);
			}
		}

		return result;
	}

	public List<Literal> getNegativeDLAtoms() {
		List<Literal> result = new ArrayList<Literal>();

		for (Literal literal : negatives) {
			if (!(literal.getPredicate() instanceof NormalPredicate))
			// && ((NormalPredicate) literal.getPredicate()).type
			// .equals(PredicateType.NORMAL))
			{
				result.add(literal);
			}
		}

		return result;
	}

	@Override
	public boolean isComment() {
		return false;
	}

	@Override
	public boolean isClause() {
		return true;
	}

	@Override
	public ProgramComment asComment() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Clause asClause() {
		return this;
	}

	public boolean isFact() {
		return positives.size() == 0 && negatives.size() == 0;
	}
}