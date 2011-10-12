/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 * Copyright (C) 2006-2009 Samuel
 */


package org.semanticweb.drew.dlprogram.model;

import org.semanticweb.drew.default_logic.OWLPredicate;

public interface Predicate extends Comparable<Predicate>{

	void setArity(int arity);

	int getArity();
	
	OWLPredicate asOWLPredicate();

}
