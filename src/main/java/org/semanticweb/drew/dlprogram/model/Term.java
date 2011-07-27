/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 * Copyright (C) 2006-2009 Samuel
 */

package org.semanticweb.drew.dlprogram.model;

/**
 * The implementation of term.
 * 
 */
public interface Term extends Cloneable {
	/**
	 * Get the name of the term.
	 * 
	 * @return name of the term
	 */
	public String getName();

	/**
	 * Clone the term.
	 * 
	 * @return a deep copy of the term
	 */
	public Term clone();
}
