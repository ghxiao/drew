/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 * Copyright (C) 2006-2009 Samuel
 */


package org.semanticweb.drew.dlprogram.model;

/**
 * Functor manager is used to cache functors so that the type information can be fetched globally.
 * 
 */
public class FunctorManager {
	//private Map<Functor, FunctorDefinition> mapping = new HashMap<Functor, FunctorDefinition>();

	private static FunctorManager manager;

	/**
	 * Private constructor.
	 */
	private FunctorManager() {
		init();
	}

	/**
	 * Initialize all supported functors.
	 */
	private void init() {
//		mapping.put(new Functor("abs"), new FunctorDefinition(Types.INTEGER, new int[] { Types.INTEGER }));
//		mapping.put(new Functor("floor"), new FunctorDefinition(Types.INTEGER, new int[] { Types.INTEGER }));
//		mapping.put(new Functor("mod"), new FunctorDefinition(Types.INTEGER, new int[] { Types.INTEGER, Types.INTEGER }));
	}

	/**
	 * Factory method to get the functor manager instance.
	 * 
	 * @return functor manager instance
	 */
	public static FunctorManager getInstance() {
		if (manager == null) {
			manager = new FunctorManager();
		}
		return manager;
	}

	/**
	 * Get the definition of a functor.
	 * 
	 * @param functor
	 * @return the definition of a functor
	 */
//	public FunctorDefinition getType(Functor functor) {
//		return mapping.get(functor);
//	}
}
