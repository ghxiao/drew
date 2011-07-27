/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 * Copyright (C) 2006-2009 Samuel
 */

package org.semanticweb.drew.dlprogram.model;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class CacheManager {
	private static final CacheManager instance = new CacheManager();

	private Map<String, NormalPredicate> predicates = new HashMap<String, NormalPredicate>();

	private Map<String, Variable> vars = new HashMap<String, Variable>();

	private Map<String, Constant> integers = new HashMap<String, Constant>();

	private Map<String, Constant> varchars = new HashMap<String, Constant>();

	private CacheManager() {
		reset();
	}

	public static final CacheManager getInstance() {
		return instance;
	}

	public int getArity(String predicateName) {
		String key;
		int arity = 1;
		key = predicateName + "/" + arity;
		if (predicates.containsKey(key)) {
			return arity;
		}
		arity = 2;
		key = predicateName + "/" + arity;
		if (predicates.containsKey(key)) {
			return arity;
		}
		throw new UnsupportedOperationException(
				"Now we only support arity 1 or 2!");
	}

	public NormalPredicate getPredicate(String name, int arity) {
		String key = name + "/" + arity;

		NormalPredicate result = predicates.get(key);
		if (result != null) {
			return result;
		} else {
			NormalPredicate predicate = new NormalPredicate(name, arity);
			predicates.put(key, predicate);
			return predicate;
		}
	}

	public Constant getConstant(String name) {
		return getConstant(name, Types.VARCHAR);
	}

	public Constant getConstant(int value){
		return getConstant(String.valueOf(value), Types.INTEGER);
	}
	
	public Constant getConstant(String name, int type) {
		Constant result;

		switch (type) {
		case Types.VARCHAR:
			result = varchars.get(name);
			if (result != null) {
				return (Constant) result;
			} else {
				Constant constant = new Constant(name, type);
				varchars.put(name, constant);
				return constant;
			}
		case Types.INTEGER:
			result = integers.get(name);
			if (result != null) {
				return (Constant) result;
			} else {
				Constant constant = new Constant(name, type);
				integers.put(name, constant);
				return constant;
			}
		default:
			throw new IllegalStateException();
		}
	}

	public Variable getVariable(String name) {
		Term result = vars.get(name);
		if (result != null) {
			return (Variable) result;
		} else {
			Variable variable = new Variable(name);
			vars.put(name, variable);
			return variable;
		}
	}

	/**
	 * Reset the predicate manager by cleaning all existing ones and append
	 * builtin ones.
	 */
	public void reset() {
		// clear predicate map
		predicates.clear();

		for (NormalPredicate predicate : NormalPredicate.logics) {
			predicates.put(predicate.toString(), predicate);
		}

		for (NormalPredicate predicate : NormalPredicate.builtins) {
			predicates.put(predicate.toString(), predicate);
		}

		// clear term map
		vars.clear();
	}

}