/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 * Copyright (C) 2006-2009 Samuel
 */


package org.semanticweb.drew.dlprogram.model;

/**
 * Predicate is an implementation of predicates used in logic programs. Predicate is instance atomic. Customer code cannot create
 * Predicate directly. Instead, Predicate instance can be created and managed by {@link CacheManager}.
 * 
 * @author Samuel
 */
public class NormalPredicate implements Predicate, Cloneable {
	public static final NormalPredicate GREATER = new NormalPredicate(">", 2, PredicateType.BUILTIN);

	public static final NormalPredicate GREATEREQUAL = new NormalPredicate(">=", 2, PredicateType.BUILTIN);

	public static final NormalPredicate EQUAL = new NormalPredicate("=", 2, PredicateType.BUILTIN);

	public static final NormalPredicate LESSEQUAL = new NormalPredicate("<=", 2, PredicateType.BUILTIN);

	public static final NormalPredicate LESS = new NormalPredicate("<", 2, PredicateType.BUILTIN);

	public static final NormalPredicate NOTEQUAL = new NormalPredicate("!=", 2, PredicateType.BUILTIN);

	public static final NormalPredicate TRUE = new NormalPredicate("true", 0, PredicateType.LOGIC);

	public static final NormalPredicate FALSE = new NormalPredicate("false", 0, PredicateType.LOGIC);

	static final NormalPredicate[] builtins = { GREATER, GREATEREQUAL, EQUAL, LESSEQUAL, LESS, NOTEQUAL };

	static final NormalPredicate[] logics = { TRUE, FALSE };

	/**
	 * Check whether a predicate is a built in one.
	 * 
	 * @param predicate the candidate predicate
	 * @return whether a predicate is a built in one
	 */
	public static boolean isBuiltIn(NormalPredicate predicate) {
		for (NormalPredicate builtin : builtins) {
			if (builtin == predicate) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether a predicate is a logic one.
	 * 
	 * @param predicate the candidate predicate
	 * @return whether a predicate is a logic one
	 */
	public static boolean isLogic(NormalPredicate predicate) {
		for (NormalPredicate logic : logics) {
			if (logic == predicate) {
				return true;
			}
		}
		return false;
	}

	String name;

	int arity;

	PredicateType type;

	String string;

	int hash;

	/**
	 * Constructor for build-in predicate. It is privately used by the global singletons.
	 */
	private NormalPredicate(String name, int arity, PredicateType type) {
		if (name == null || arity < 0 || type == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.arity = arity;
		this.type = type;

		update();
	}

	/**
	 * Constructor for normal predicate. Customer code should use {@link CacheManager} to create process unique predicates.
	 * 
	 * @param name the name of the predicate
	 * @param arity the arity of the predicate
	 */
	NormalPredicate(String name, int arity) {
		this.name = name;
		this.arity = arity;
		this.type = name == null ? PredicateType.UNKNOWN : PredicateType.NORMAL;

		update();
	}

	/**
	 * Updates the string presentation and hash code of the predicate in advance.
	 */
	private void update() {
		// calculate string representation
		if (type == PredicateType.LOGIC) {
			string = name;
		} else {
			StringBuilder result = new StringBuilder();
			result.append(name).append("/").append(arity);
			string = result.toString();
		}

		// calculate the hash code
		hash = name.hashCode() + arity;
	}

	/**
	 * Get the name of the predicate.
	 * 
	 * @return predicate name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the arity of the predicate.
	 * 
	 * @return predicate arity
	 */
	public int getArity() {
		return arity;
	}

	/**
	 * Get the type of the predicate.
	 * 
	 * @return the type of the predicate
	 */
	public PredicateType getType() {
		return type;
	}

	/**
	 * Predicates are compared by alphabetic ordering if they are both strings or comparison operators. If only one predicate is a
	 * comparison operator, then it stays behind.
	 * 
	 * @param that another predicate
	 * @return positive if greater in order
	 */
	public int compareTo(NormalPredicate that) {
		int result = type.ordinal() - that.type.ordinal();
		if (result == 0) {
			return name.compareTo(that.name);
		} else {
			return result;
		}
	}

	@Override
	public String toString() {
		return string;
	}

	@Override
	public Object clone() {
		try {
			return (NormalPredicate) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // cannot happen
		}
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

	@Override
	public int hashCode() {
		return hash;
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
}