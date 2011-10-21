package org.semanticweb.drew.dlprogram.format;

import java.io.IOException;

import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.Literal;

public class DLProgramStorerBase implements DLProgramStorer {

	
	
	
	public void beginWritingClause(Clause clause, Appendable target){
		
	}
	
	public void writeClause(Clause clause, Appendable target){
		beginWritingLiteral(clause.getHead(), target);
		writeLiteral(clause.getHead(), target);
		endWritingLiteral(clause.getHead(), target);
		
		
	}
	
	public void endWritingClause(Clause clause, Appendable target) throws IOException{
			target.append("\n");
	}
	
	public void beginWritingLiteral(Literal literal, Appendable target){
		
	}
	
	public void writeLiteral(Literal literal, Appendable target){
		
	}
	
	public void endWritingLiteral(Literal literal, Appendable target){
		
	}

	@Override
	public void storeDLProgram(DLProgram program, Appendable target) throws IOException {
		for(Clause clause : program.getClauses()){
			beginWritingClause(clause, target);
			writeClause(clause, target);
			endWritingClause(clause, target);
		}
	}
	
}
