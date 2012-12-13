/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 * Copyright (C) 2006-2009 Samuel
 */


package org.semanticweb.drew.dlprogram.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Function represents math expression, e.g. X * (Y - Z), as well as math function, e.g. abs(X - Y). Function adopts composite
 * pattern to store the data. Internally X * (Y - Z) is stored as *(X, -(Y, Z)).
 * 
 */
public class Function implements Term {
	private Functor functor;

	private ArrayList<Term> terms = new ArrayList<>();

	/**
	 * Get the functor.
	 * 
	 * @return the functor of the function
	 */
	public Functor getFunctor() {
		return functor;
	}

	/**
	 * Set the functor of the function.
	 * 
	 * @param functor functor of the function
	 */
	public void setFunctor(Functor functor) {
		this.functor = functor;
	}

	/**
	 * Get the terms of the function.
	 * 
	 * @return terms of the function
	 */
	public List<Term> getTerms() {
		return terms;
	}

	/**
	 * This is a dummy function since function does not have a name.
	 * 
	 * @param name name of the term
	 */
	public void setName(String name) {
		// no effect at all
	}

	/**
	 * This always returns null since function does not have a name.
	 * 
	 * @return name of the term
	 */
	@Override
	public String getName() {
		return null;
	}

	/**
	 * Decide if a given term needs brackets as a left or right sub-term of the operator. For example, when operator is - and term
	 * is X + Y, then it needs a bracket. For another example, if operator is / and term is abs(Z), then it doesn't need a
	 * bracket.
	 * 
	 * @param term sub-term who needs a bracket or not
	 * @return true if term needs a bracket
	 */
	private boolean needBracket(Term term) {
		assert (functor.isOperator() && terms.size() == 2);

		boolean left = term == terms.get(0);

		if (term instanceof Function) {
			Function expr = (Function) term;
			String subop = expr.getFunctor().getName();

			if (functor.getName().equals(Functor.MINUS)) {
				// bracket right term if it is plus expression
				if (!left && subop.equals(Functor.PLUS)) {
					return true;
				}
			} else if (functor.getName().equals(Functor.MULTIPLY)) {
				// bracket left and right term if it is plus or minus expression
				if (subop.equals(Functor.PLUS) || subop.equals(Functor.MINUS)) {
					return true;
				}
			} else if (functor.getName().equals(Functor.DEVIDE)) {
				// bracket left term if it is plus or minus expression
				if (left && (subop.equals(Functor.PLUS) || subop.equals(Functor.MINUS))) {
					return true;
				}

				// bracket right term anyhow
				if (!left) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		if (functor.isOperator()) {
			if (needBracket(terms.get(0))) {
				result.append("(").append(terms.get(0)).append(")");
			} else {
				result.append(terms.get(0).toString());
			}

			result.append(" ").append(functor.getName()).append(" ");

			if (needBracket(terms.get(1))) {
				result.append("(").append(terms.get(1)).append(")");
			} else {
				result.append(terms.get(1));
			}
		} else {
			result.append(functor.getName());
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
		}

		return result.toString();
	}

	@Override
	public Function clone() {
		try {
			Function result = (Function) super.clone();
			result.terms = new ArrayList<>(terms);
			return result;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // cannot happen
		}
	}
}