package org.semanticweb.drew.dlprogram.format;

import java.io.IOException;
import java.sql.Types;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

	//Appendable target;

	/*
	 * In dlvhex format, prefix is not allowed
	 */
	boolean usingDlvhexFormat;

	public boolean isUsingDlvhexFormat() {
		return usingDlvhexFormat;
	}

	public void setUsingDlvhexFormat(boolean usingDlvhexFormat) {
		this.usingDlvhexFormat = usingDlvhexFormat;
	}

	public DLProgramStorerImpl() {

	}

//	public void saveToFile(DLProgram program, String file) {
//		saveToFile(program.getStatements(), file);
//	}

//	public void saveToFile(List<ProgramStatement> program, String file) {
//		try {
//			FileWriter writer = new FileWriter(file);
//			// if (DReWELManager.getInstance().getDatalogFormat() ==
//			// DatalogFormat.XSB) {
//			// writer.write(PInst.strXSBHeader);
//			// }
//			writer.write(writeStatements(program));
//			writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	void writeDLProgram(DLProgram program, Appendable target) {
		// sb = new StringBuilder();
		List<ProgramStatement> stmts = program.getStatements();
		writeStatements(stmts, target);
	}

	public void writeStatements(Collection<ProgramStatement> statements, Appendable target) {
		// target = new StringBuilder();

		for (ProgramStatement r : statements) {
			if (r.isClause()) {
				writeCluase(r.asClause(), target);
				write("\n", target);
			} else if (r.isComment()) {
				write(r.asComment().toString(), target);
			} else {
				throw new IllegalStateException();
			}
		}
		//return target.toString();
	}

	void writeCluase(Clause r, Appendable target) {
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
			// sb.append(((NormalPredicate) predicate).getName());

			NormalPredicate normalPredicate = (NormalPredicate) predicate;
			switch (normalPredicate.getType()) {
			case BUILTIN:
				// sb.append(terms.get(0));
				writeTerm(terms.get(0), target);
				write(" ", target);
				write(normalPredicate.getName(), target);
				write(" ", target);
				// sb.append(terms.get(1));
				writeTerm(terms.get(1), target);
				// return result.toString();
				break;
			case LOGIC:
				write(normalPredicate.getName(), target);
				// return normalPredicate.name;
				break;
			case NORMAL:
				if (lit.isNegative()) {
					write("-", target);
				}

				write(normalPredicate.getName(), target);
				if (terms.size() > 0) {
					write("(", target);
					for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
						// sb.append(iter.next());
						writeTerm(iter.next(), target);
						if (iter.hasNext()) {
							write(", ", target);
						}
					}
					write(")", target);
				}
				// return result.toString();
				break;
			default:
				throw new IllegalStateException();
			}
		} else if (predicate instanceof DLAtomPredicate) {

			write(((DLAtomPredicate) predicate).toString(), target);
			if (terms.size() > 0) {
				write("(", target);
				for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
					// sb.append(iter.next());
					writeTerm(iter.next(), target);
					if (iter.hasNext()) {
						write(", ", target);
					}
				}
				write(")", target);
			}
		}

		//
		// if (predicate instanceof NormalPredicate) {
		//
		// NormalPredicate normalPredicate = (NormalPredicate) predicate;
		//
		// switch (normalPredicate.type) {
		// case BUILTIN:
		// result.append(terms.get(0));
		// result.append(" ").append(normalPredicate.name).append(" ");
		// result.append(terms.get(1));
		// return result.toString();
		// case LOGIC:
		// return normalPredicate.name;
		// case NORMAL:
		// result.append(normalPredicate.name);
		// if (terms.size() > 0) {
		// result.append("(");
		// for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
		// result.append(iter.next());
		// if (iter.hasNext()) {
		// result.append(", ");
		// }
		// }
		// result.append(")");
		// }
		// return result.toString();
		// default:
		// throw new IllegalStateException();
		// }
		// } else if (predicate instanceof DLAtomPredicate) {
		//
		// DLAtomPredicate dlAtomPredicate = (DLAtomPredicate) predicate;
		// result.append(dlAtomPredicate.toString());
		// if (terms.size() > 0) {
		// result.append("(");
		// for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
		// result.append(iter.next());
		// if (iter.hasNext()) {
		// result.append(", ");
		// }
		// }
		// result.append(")");
		// }
		// return result.toString();
		// }
		//
		//
		// NormalPredicate np = (NormalPredicate) predicate;
		// np.getType() ==
		//
		// if (lit.getTerms().size() > 0) {
		//
		// sb.append("(");
		// boolean first = true;
		// for (Term t : lit.getTerms()) {
		// if (!first) {
		// sb.append(", ");
		// }
		// first = false;
		//
		// toString(t);
		// }
		// sb.append(")");
		// }
	}

	void writeTerm(Term t, Appendable target) {
		if (t instanceof IRIConstant) {
			IRIConstant iriConstant = (IRIConstant) t;
			IRI iri = iriConstant.getIRI();
			switch (DReWELManager.getInstance().getNamingStrategy()) {
			case IntEncoding:
				write(DReWELManager.getInstance().getIRIEncoder().encode(iri), target);
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
			write("\"", target);
			write(t.toString(), target);
			write("\"", target);
		} else {
			write(t.toString(), target);
		}
	}

	private String toString(DLAtomPredicate predicate, Appendable target) {
		StringBuilder result = new StringBuilder();
		result.append("DL[");
		boolean first = true;
		for (DLInputOperation op : predicate.getInputSignature()
				.getOperations()) {
			if (!first)
				result.append(",");
			first = false;
			result.append(op.getDLPredicate().getIRI().getFragment());
			result.append(op.getType());
			result.append(op.getInputPredicate().getName());
			// 1st difference
		}
		// 2nd diff
		if (predicate.getInputSignature().getOperations().size() > 0)
			result.append(";");

		// 3rd diff
		result.append(predicate.getQuery().getIRI().getFragment());

		result.append("]");

		return result.toString();
	}

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
	public void storeDLProgram(DLProgram program, Appendable target) {
		//this.target = target;
		writeDLProgram(program, target);
	}

	@Override
	public void storeProgramStatements(Collection<ProgramStatement> statements,
			Appendable target) {
		writeStatements(statements, target);
	}
	
	
}
