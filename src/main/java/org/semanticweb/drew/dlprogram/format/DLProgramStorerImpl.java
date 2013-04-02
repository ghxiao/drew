package org.semanticweb.drew.dlprogram.format;

import java.io.IOException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Constant;
import org.semanticweb.drew.dlprogram.model.DLAtomPredicate;
import org.semanticweb.drew.dlprogram.model.DLInputOperation;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.IRIConstant;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.Predicate;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.el.reasoner.DReWELManager;
import org.semanticweb.owlapi.model.IRI;

public class DLProgramStorerImpl implements DLProgramStorer {

	/*
	 * In dlvhex format, prefix is not allowed
	 */
	private boolean usingDlvhexFormat;

	//private Map<String, String> prefixMap = new HashMap<>();
	private String prefix;
	
	public boolean isUsingDlvhexFormat() {
		return usingDlvhexFormat;
	}

	public void setUsingDlvhexFormat(boolean usingDlvhexFormat) {
		this.usingDlvhexFormat = usingDlvhexFormat;
	}

	public DLProgramStorerImpl() {

	}

	void writeDLProgram(DLProgram program, Appendable target) {
		List<ProgramStatement> stmts = program.getStatements();
		writeStatements(stmts, target);
	}

	void writeStatements(Collection<ProgramStatement> statements,
			Appendable target) {

		for (ProgramStatement r : statements) {
			writeStatement(r, target);
		}
	}

	private void writeStatement(ProgramStatement stmt, Appendable target) {
		if (stmt.isClause()) {
			writeClause(stmt.asClause(), target);
			write("\n", target);
		} else if (stmt.isComment()) {
			write(stmt.asComment().toString(), target);
		} else {
			throw new IllegalStateException();
		}
	}

	void writeClause(Clause r, Appendable target) {
		if (!(r.getHead().equals(Literal.FALSE))) {
			writeLiteral(r.getHead(), target);
		}

		if (r.getBody().size() > 0) {
			write(" :- ", target);
			boolean firstLiteral = true;
			for (Literal literal : r.getPositiveBody()) {
				if (!firstLiteral) {
					write(", ", target);
				}
				firstLiteral = false;
				writeLiteral(literal, target);
			}

			for (Literal literal : r.getNegativeBody()) {
				if (!firstLiteral) {
					write(", ", target);
				}
				firstLiteral = false;
				write("not ", target);
				writeLiteral(literal, target);
			}
		}

		write(".", target);
	}

	void writeLiteral(Literal lit, Appendable target) {
		final Predicate predicate = lit.getPredicate();
		List<Term> terms = lit.getTerms();
		if (predicate instanceof NormalPredicate) {

			NormalPredicate normalPredicate = (NormalPredicate) predicate;
			switch (normalPredicate.getType()) {
			case BUILTIN:
				writeTerm(terms.get(0), target);
				write(" ", target);
				write(normalPredicate.getName(), target);
				write(" ", target);
				writeTerm(terms.get(1), target);
				break;
			case LOGIC:
				write(normalPredicate.getName(), target);
				break;
			case NORMAL:
				if (lit.isNegative()) {
					write("-", target);
				}

				// write(normalPredicate.getName(), target);
				writeNormalPredicate(normalPredicate, target);
				if (terms.size() > 0) {
					write("(", target);
					for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
						writeTerm(iter.next(), target);
						if (iter.hasNext()) {
							write(", ", target);
						}
					}
					write(")", target);
				}
				break;
			default:
				throw new IllegalStateException();
			}
		} else if (predicate instanceof DLAtomPredicate) {

			write(predicate.toString(), target);
			if (terms.size() > 0) {
				write("(", target);
				for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
					writeTerm(iter.next(), target);
					if (iter.hasNext()) {
						write(", ", target);
					}
				}
				write(")", target);
			}
		}
	}

	void writeNormalPredicate(NormalPredicate logicPredicate, Appendable target) {
		try {
			if(prefix != null){
				target.append(prefix);
			}
			target.append(logicPredicate.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void writeTerm(Term t, Appendable target) {
		if (t instanceof IRIConstant) {
			IRIConstant iriConstant = (IRIConstant) t;
			IRI iri = iriConstant.getIRI();
			switch (DReWELManager.getInstance().getNamingStrategy()) {
			case IntEncoding:
				write(DReWELManager.getInstance().getIRIEncoder().encode(iri),
						target);
				break;
			case IRIFragment:
				String fragment = iri.getFragment();
				if (fragment == null) {
					fragment = iri.toString();
				}
				write("\"", target);
				write(fragment, target);
				write("\"", target);
				break;
			case IRIFull:
				write("\"<", target);
				write(iri.toString(), target);
				write(">\"", target);
				break;
			default:
				break;
			}

		} else if (t instanceof Constant
				&& ((Constant) t).getType() == Types.VARCHAR) {
			writeStringConstant(t, target);
		} else {
			write(t.toString(), target);
		}
	}

	void writeStringConstant(Term t, Appendable target) {
		write("\"", target);
		write(t.toString(), target);
		write("\"", target);
	}

	// private String toString(DLAtomPredicate predicate, Appendable target) {
	// StringBuilder result = new StringBuilder();
	// result.append("DL[");
	// boolean first = true;
	// for (DLInputOperation op : predicate.getInputSignature()
	// .getOperations()) {
	// if (!first)
	// result.append(",");
	// first = false;
	// result.append(op.getDLPredicate().getIRI().getFragment());
	// result.append(op.getType());
	// result.append(op.getInputPredicate().getName());
	// // 1st difference
	// }
	// // 2nd diff
	// if (predicate.getInputSignature().getOperations().size() > 0)
	// result.append(";");
	//
	// // 3rd diff
	// result.append(predicate.getQuery().getIRI().getFragment());
	//
	// result.append("]");
	//
	// return result.toString();
	// }

	void write(String string, Appendable target) {
		try {
			target.append(string);
		} catch (IOException e) {
			throw new DLProgramStoreException(e);
		}
	}

	void write(int number, Appendable target) {
		try {
			target.append(String.valueOf(number));
		} catch (IOException e) {
			throw new DLProgramStoreException(e);
		}
	}

	@Override
	public void store(DLProgram program, Appendable target) {
		writeDLProgram(program, target);
	}

	@Override
	public void store(Collection<ProgramStatement> statements, Appendable target) {
		writeStatements(statements, target);
	}

	@Override
	public void store(ProgramStatement statement, Appendable target) {
		writeStatement(statement, target);
	}

	public String getPrefix() {
		return prefix;
	}

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

//	public Map<String, String> getPrefixMap() {
//		return prefixMap;
//	}
//
//	public void setPrefixMap(Map<String, String> prefixMap) {
//		this.prefixMap = prefixMap;
//	}
}
