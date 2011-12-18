package org.semanticweb.drew.el.reasoner;

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
import org.semanticweb.drew.dlprogram.model.ProgramComment;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.owlapi.model.IRI;

public class DatalogToStringHelper {

	StringBuilder sb;

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

	public DatalogToStringHelper() {

	}

	public void saveToFile(DLProgram program, String file) {
		// try {
		// FileWriter writer = new FileWriter(file);
		// if (DReWELManager.getInstance().getDatalogFormat() ==
		// DatalogFormat.XSB) {
		// writer.write(PInst.strXSBHeader);
		// }
		// writer.write(toString(program));
		// writer.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		saveToFile(program.getStatements(), file);
	}

	public void saveToFile(List<ProgramStatement> program, String file) {
		try {
			FileWriter writer = new FileWriter(file);
			if (DReWELManager.getInstance().getDatalogFormat() == DatalogFormat.XSB) {
				writer.write(PInst.strXSBHeader);
			}
			writer.write(toString(program));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String toString(DLProgram program) {
		// sb = new StringBuilder();
		return toString(program.getStatements());
	}

	public String toString(Collection<ProgramStatement> statements) {
		sb = new StringBuilder();

		for (ProgramStatement r : statements) {
			toString(r);
			
		}
		return sb.toString();
	}

	private void toString(ProgramStatement r) {
		if (r instanceof ProgramComment) {
			sb.append(r);
		} else if (r instanceof Clause) {
			toString(((Clause) r));
			sb.append("\n");
		}
	}

	private void toString(Clause r) {
		if (!(r.getHead().equals(Literal.FALSE))) {
			toString(r.getHead());
		}

		if (r.getBody().size() > 0) {
			sb.append(" :- ");
			boolean firstLiteral = true;
			for (Literal literal : r.getPositiveBody()) {
				if (!firstLiteral) {
					sb.append(", ");
				}
				firstLiteral = false;
				toString(literal);
			}

			for (Literal literal : r.getNegativeBody()) {
				if (!firstLiteral) {
					sb.append(", ");
				}
				firstLiteral = false;
				sb.append("not ");
				toString(literal);
			}
		}

		sb.append(".");
	}

	private void toString(Literal lit) {
		final Predicate predicate = lit.getPredicate();
		List<Term> terms = lit.getTerms();
		if (predicate instanceof NormalPredicate) {
			// sb.append(((NormalPredicate) predicate).getName());

			NormalPredicate normalPredicate = (NormalPredicate) predicate;
			switch (normalPredicate.getType()) {
			case BUILTIN:
				// sb.append(terms.get(0));
				toString(terms.get(0));
				sb.append(" ").append(normalPredicate.getName()).append(" ");
				// sb.append(terms.get(1));
				toString(terms.get(1));
				// return result.toString();
				break;
			case LOGIC:
				sb.append(normalPredicate.getName());
				// return normalPredicate.name;
				break;
			case NORMAL:
				sb.append(normalPredicate.getName());
				if (terms.size() > 0) {
					sb.append("(");
					for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
						// sb.append(iter.next());
						toString(iter.next());
						if (iter.hasNext()) {
							sb.append(", ");
						}
					}
					sb.append(")");
				}
				// return result.toString();
				break;
			default:
				throw new IllegalStateException();
			}
		} else if (predicate instanceof DLAtomPredicate) {

			sb.append(toString((DLAtomPredicate) predicate));
			if (terms.size() > 0) {
				sb.append("(");
				for (Iterator<Term> iter = terms.iterator(); iter.hasNext();) {
					// sb.append(iter.next());
					toString(iter.next());
					if (iter.hasNext()) {
						sb.append(", ");
					}
				}
				sb.append(")");
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

	private void toString(Term t) {
		if (t instanceof IRIConstant) {
			IRIConstant iriConstant = (IRIConstant) t;
			IRI iri = iriConstant.getIRI();
			switch (DReWELManager.getInstance().getNamingStrategy()) {
			case IntEncoding:
				sb.append(DReWELManager.getInstance().getIRIEncoder()
						.encode(iri));
				break;
			case IRIFragment:
				String fragment = iri.getFragment();
				if (fragment == null) {
					fragment = iri.toString();
				}
				sb.append("\"").append(fragment).append("\"");
				break;
			case IRIFull:
				sb.append("\"<").append(iri.toString()).append(">\"");
				break;
			default:
				break;
			}

		} else if (t instanceof Constant
				&& ((Constant) t).getType() == Types.VARCHAR) {
			sb.append("\"");
			sb.append(t);
			sb.append("\"");
		} else {
			sb.append(t);
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

}
