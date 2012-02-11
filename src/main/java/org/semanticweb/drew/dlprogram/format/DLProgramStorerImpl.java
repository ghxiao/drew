package org.semanticweb.drew.dlprogram.format;

import java.io.FileWriter;
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
import org.semanticweb.drew.el.reasoner.DatalogFormat;
import org.semanticweb.drew.el.reasoner.PInst;
import org.semanticweb.owlapi.model.IRI;

public class DLProgramStorerImpl implements DLProgramStorer {

	Appendable target;

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

	public void saveToFile(DLProgram program, String file) {
		saveToFile(program.getStatements(), file);
	}

	public void saveToFile(List<ProgramStatement> program, String file) {
		try {
			FileWriter writer = new FileWriter(file);
			// if (DReWELManager.getInstance().getDatalogFormat() ==
			// DatalogFormat.XSB) {
			// writer.write(PInst.strXSBHeader);
			// }
			writer.write(writeStatements(program));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String writeDLProgram(DLProgram program) {
		// sb = new StringBuilder();
		List<ProgramStatement> stmts = program.getStatements();
		return writeStatements(stmts);
	}

	public String writeStatements(Collection<ProgramStatement> statements) {
		// target = new StringBuilder();

		for (ProgramStatement r : statements) {
			if (r.isClause()) {
				writeCluase(r.asClause());
				write("\n");
			} else if (r.isComment()) {
				write(r.asComment().toString());
			} else {
				throw new IllegalStateException();
			}
		}
		return target.toString();
	}

	void writeCluase(Clause r) {
		if (!(r.getHead().equals(Literal.FALSE))) {
			writeLiteral(r.getHead());
		}

		if (r.getBody().size() > 0) {
			write(" :- ");
			boolean firstLiteral = true;
			for (Literal literal : r.getPositiveBody()) {
				if (!firstLiteral) {
					write(", ");
				}
				firstLiteral = false;
				writeLiteral(literal);
			}

			for (Literal literal : r.getNegativeBody()) {
				if (!firstLiteral) {
					write(", ");
				}
				firstLiteral = false;
				write("not ");
				writeLiteral(literal);
			}
		}

		write(".");
	}

	void writeLiteral(Literal lit) {
		final Predicate predicate = lit.getPredicate();
		List<Term> terms = lit.getTerms();
		if (predicate instanceof NormalPredicate) {
			// sb.append(((NormalPredicate) predicate).getName());

			NormalPredicate normalPredicate = (NormalPredicate) predicate;
			switch (normalPredicate.getType()) {
			case BUILTIN:
				// sb.append(terms.get(0));
				writeTerm(terms.get(0));
				write(" ");
				write(normalPredicate.getName());
				write(" ");
				// sb.append(terms.get(1));
				writeTerm(terms.get(1));
				// return result.toString();
				break;
			case LOGIC:
				write(normalPredicate.getName());
				// return normalPredicate.name;
				break;
			case NORMAL:
				write(normalPredicate.getName());
				if (terms.size() > 0) {
					write("(");
					for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
						// sb.append(iter.next());
						writeTerm(iter.next());
						if (iter.hasNext()) {
							write(", ");
						}
					}
					write(")");
				}
				// return result.toString();
				break;
			default:
				throw new IllegalStateException();
			}
		} else if (predicate instanceof DLAtomPredicate) {

			write(toString((DLAtomPredicate) predicate));
			if (terms.size() > 0) {
				write("(");
				for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
					// sb.append(iter.next());
					writeTerm(iter.next());
					if (iter.hasNext()) {
						write(", ");
					}
				}
				write(")");
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

	void writeTerm(Term t) {
		if (t instanceof IRIConstant) {
			IRIConstant iriConstant = (IRIConstant) t;
			IRI iri = iriConstant.getIRI();
			switch (DReWELManager.getInstance().getNamingStrategy()) {
			case IntEncoding:
				write(DReWELManager.getInstance().getIRIEncoder().encode(iri));
				break;
			case IRIFragment:
				String fragment = iri.getFragment();
				if (fragment == null) {
					fragment = iri.toString();
				}
				write("\"");
				write(fragment);
				write("\"");
				break;
			case IRIFull:
				write("\"<");
				write(iri.toString());
				write(">\"");
				break;
			default:
				break;
			}

		} else if (t instanceof Constant
				&& ((Constant) t).getType() == Types.VARCHAR) {
			write("\"");
			write(t.toString());
			write("\"");
		} else {
			write(t.toString());
		}
	}

	private String toString(DLAtomPredicate predicate) {
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

	private Appendable write(String string) {
		try {
			target.append(string);
			return target;
		} catch (IOException e) {
			throw new DLProgramStoreException(e);
		}
	}

	private Appendable write(int string) {
		try {
			target.append(String.valueOf(string));
			return target;
		} catch (IOException e) {
			throw new DLProgramStoreException(e);
		}
	}

	@Override
	public void storeDLProgram(DLProgram program, Appendable target)
		throws IOException {
		this.target = target;
	}
}
