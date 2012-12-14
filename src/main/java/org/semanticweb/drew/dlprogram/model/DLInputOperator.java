/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 * Copyright (C) 2006-2009 Samuel
 */

package org.semanticweb.drew.dlprogram.model;

/**
 * DL Input Operator: $\\uplus$ or $\\uminus$
 */
public enum DLInputOperator {
	U_PLUS("+="), U_MINUS("-=");
	
	private String symbol;
	
	DLInputOperator(String symbol){
		this.symbol = symbol;
	}
	
	@Override
	public String toString(){
		return symbol;
	}
}
