/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 * Copyright (C) 2006-2009 Samuel
 */

package org.semanticweb.drew.dlprogram.model;

/**
 * Functor is used by function to represent an arithmetic operator or a functor as abs, sqrt, etc.
 * 
 */
public class Functor implements Cloneable, Comparable<Functor> {
	public static final String PLUS = "+";

	public static final String MINUS = "-";

	public static final String MULTIPLY = "*";

	public static final String DEVIDE = "/";

	private static final String[] operators = { PLUS, MINUS, MULTIPLY, DEVIDE };

	private String name;

	public Functor() {
	}

	public Functor(String name) {
		this.name = name;
	}

	/**
	 * Is the functor an arithmetic operator?
	 * 
	 * @return whether the functor is an arithmetic operator
	 */
	public boolean isOperator() {
		for (String operator : operators) {
			if (name.equals(operator))
				return true;
		}
		return false;
	}

	/**
	 * Set the name of the functor.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the name of the functor.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Functors are sorted in alphabetic order.
	 */
	public int compareTo(Functor that) {
		return name.compareTo(that.name);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public Functor clone() {
		try {
			return (Functor) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // cannot happen
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Functor) {
			Functor that = (Functor) obj;
			if (name.equals(that.name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}