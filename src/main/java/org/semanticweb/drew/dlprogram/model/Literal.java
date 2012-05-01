/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 * Copyright (C) 2006-2009 Samuel
 */

package org.semanticweb.drew.dlprogram.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.semanticweb.drew.default_logic.OWLPredicate;

/**
 * A literal has a predicate as well as a list of terms. A literal is called
 * schematic if it contains variables, grounded if it contains constants only.
 * There are two special literals, TRUE and FALSE, represent logically true or
 * false.
 * 
 */
public class Literal implements Cloneable, Comparable<Literal> {
	public static final Literal TRUE = new Literal(NormalPredicate.TRUE, (List<Term>) null);

	public static final Literal FALSE = new Literal(NormalPredicate.FALSE, (List<Term>) null);

	private Predicate predicate;

	private List<Term> terms = new ArrayList<Term>();

	/*
	 * classical negation / "true" negation
	 */
	private boolean negative;

	/**
	 * Default empty constructor.
	 */
	public Literal() {

	}

	/**
	 * Constructor with initialization.
	 * 
	 * @param predicate
	 * @param terms
	 */
	public Literal(Predicate predicate, List<Term> terms) {
		this.predicate = predicate;

		if (terms != null) {
			this.terms.addAll(terms);
		}
	}

	public Literal(Predicate predicate, Term... terms) {
		
		if(predicate.getArity() != terms.length){
			throw new IllegalArgumentException(String.format("predicate.arity != term.size"));
		}
		
		this.predicate = predicate;

		this.terms = Arrays.asList(terms);
	}

	public Literal(String predicate, Term... terms) {
		this.predicate = CacheManager.getInstance().getPredicate(predicate, terms.length);

		this.terms = Arrays.asList(terms);
	}

	/**
	 * Set predicate.
	 * 
	 * @param predicate
	 */
	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	/**
	 * Get predicate.
	 * 
	 * @return
	 */
	public Predicate getPredicate() {
		return predicate;
	}

	/**
	 * Get the term list.
	 * 
	 * @return
	 */
	public List<Term> getTerms() {
		return terms;
	}

	/**
	 * Compare two literal by alphabetic order.
	 * 
	 * @param that
	 *            another literal
	 * @return compare result
	 */
	@Override
	public int compareTo(Literal that) {
		// literals with fewer functions go first
		int nThisTerms = 0;
		for (Term term : terms) {
			if (term instanceof Function) {
				nThisTerms++;
			}
		}

		int nThatTerms = 0;
		for (Term term : that.terms) {
			if (term instanceof Function) {
				nThatTerms++;
			}
		}

		if (nThisTerms != nThatTerms) {
			return nThisTerms - nThatTerms;
		}

		// literals with small predicates go first
		int diff = predicate.compareTo(that.predicate);
		if (diff != 0) {
			return diff;
		}

		return toString().compareTo(that.toString());
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		if (predicate instanceof NormalPredicate) {

			NormalPredicate normalPredicate = (NormalPredicate) predicate;

			switch (normalPredicate.type) {
			case BUILTIN:
				result.append(terms.get(0));
				result.append(" ").append(normalPredicate.name).append(" ");
				result.append(terms.get(1));
				return result.toString();
			case LOGIC:
				return normalPredicate.name;
			case NORMAL:
				if (isNegative()) {
					result.append("-");
				}
				result.append(normalPredicate.name);
				if (terms.size() > 0) {
					result.append("(");
					for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
						result.append(iter.next());
						if (iter.hasNext()) {
							result.append(", ");
						}
					}
					result.append(")");
				}
				return result.toString();
			default:
				throw new IllegalStateException();
			}
		} else if (predicate instanceof DLAtomPredicate) {

			DLAtomPredicate dlAtomPredicate = (DLAtomPredicate) predicate;
			result.append(dlAtomPredicate.toString());
			if (terms.size() > 0) {
				result.append("(");
				for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
					result.append(iter.next());
					if (iter.hasNext()) {
						result.append(", ");
					}
				}
				result.append(")");
			}
			return result.toString();
		} else if (predicate instanceof OWLPredicate) {
			OWLPredicate owlPredicate = (OWLPredicate) predicate;
			result.append(owlPredicate.toString());
			if (terms.size() > 0) {
				result.append("(");
				for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
					result.append(iter.next());
					if (iter.hasNext()) {
						result.append(", ");
					}
				}
				result.append(")");
			}
			return result.toString();
		}
		throw new IllegalStateException();

	}

	@Override
	public Literal clone() {
		try {
			Literal result = (Literal) super.clone();
			result.terms = new ArrayList<Term>(terms);
			return result;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // cannot happen
		}
	}

	@Override
	public boolean equals(Object obj) {
		// this is a shortcut for TRUE and FALSE
		if (this == obj) {
			return true;
		}

		if (obj instanceof Literal) {
			Literal that = (Literal) obj;
			if (predicate.equals(that.predicate) && terms.equals(that.terms)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + predicate.hashCode();
		result = 31 * result + terms.hashCode();
		return result;
	}

	public boolean isNegative() {
		return negative;
	}

	public void setNegative(boolean negative) {
		this.negative = negative;
	}

}