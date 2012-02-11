package org.semanticweb.drew.dlprogram.model;

// the common interface of clause and comments
public interface ProgramStatement {
	boolean isComment();

	boolean isClause();

	ProgramComment asComment();

	Clause asClause();
}
