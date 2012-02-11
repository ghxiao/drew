package org.semanticweb.drew.dlprogram.format;

import java.io.IOException;

import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.ProgramComment;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;

public class DLProgramStorerBase implements DLProgramStorer {

	private Appendable target;

	@Override
	public void storeDLProgram(DLProgram program, Appendable target)
			throws IOException {

		this.target = target;

		for (ProgramStatement stmt : program.getStatements()) {
			if (stmt.isClause()) {
				Clause clause = stmt.asClause();
				beginWritingClause(clause);
				writeClause(clause);
				endWritingClause(clause);
			} else if (stmt.isComment()) {
				beginWritingComment(stmt.asComment());
				writeComment(stmt.asComment());
				endWritingComment(stmt.asComment());
			}
		}
	}

	protected void beginWritingClause(Clause clause) {

	}

	protected void writeClause(Clause clause) {
		Literal head = clause.getHead();
		writeLiteral(head);
		if (clause.isFact()) {
			write(".");
			return;
		} else {
			
		}
	}

//	private void writeLiteral(Literal head) {
//		beginWritingLiteral(head);
//		writeLiteral(head);
//		endWritingLiteral(head);
//	}

	protected void endWritingClause(Clause clause)
			throws IOException {
		target.append("\n");
	}

	protected void beginWritingLiteral(Literal literal) {

	}

	protected void writeLiteral(Literal literal) {

	}

	protected void endWritingLiteral(Literal literal) {

	}

	protected void endWritingStatement(ProgramStatement stmt) {

	}

	protected void writeStatement(ProgramStatement stmt) {

	}

	protected void beginWritingStatement(ProgramStatement stmt,
			Appendable target) {

	}

	private void endWritingComment(ProgramComment asComment) {

	}

	private void writeComment(ProgramComment asComment) {

	}

	private void beginWritingComment(ProgramComment asComment) {

	}

	private void writeNewLine() {
		write("\n");
	}

	private void write(String string) {
		try {
			target.append(string);
		} catch (IOException e) {
			throw new DLProgramStoreException(e);
		}
	}
}
