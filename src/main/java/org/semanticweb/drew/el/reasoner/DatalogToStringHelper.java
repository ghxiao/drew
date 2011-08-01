package org.semanticweb.drew.el.reasoner;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Types;

import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Constant;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.IRIConstant;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.Predicate;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.owlapi.model.IRI;

public class DatalogToStringHelper {

	StringBuilder sb;

	public DatalogToStringHelper() {

	}

	public void saveToFile(DLProgram program, String file) {
		try {
			FileWriter writer = new FileWriter(file);
			if(DReWELManager.getInstance().getDatalogFormat()==DatalogFormat.XSB){
				writer.write(PInst.strXSBHeader);
			}
			writer.write(toString(program));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public String toString(DLProgram program) {
		sb = new StringBuilder();
		for (Clause r : program.getClauses()) {
			toString(r);
			sb.append("\n");
		}

		return sb.toString();
	}

	private void toString(Clause r) {
		toString(r.getHead());

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
		if (predicate instanceof NormalPredicate) {
			sb.append(((NormalPredicate) predicate).getName());
		} else {
			sb.append(predicate);
		}

		if (lit.getTerms().size() > 0) {

			sb.append("(");
			boolean first = true;
			for (Term t : lit.getTerms()) {
				if (!first) {
					sb.append(", ");
				}
				first = false;
				
				
				if (t instanceof IRIConstant) {
					IRIConstant iriConstant = (IRIConstant)t;
					IRI iri = iriConstant.getIRI();
					switch (DReWELManager.getInstance().getNamingStrategy()) {
					case IntEncoding:
						sb.append(DReWELManager.getInstance().getIRIEncoder().encode(iri));
						break;
					case IRIFragment:
						sb.append("\"").append(iri.getFragment())
								.append("\"");
						break;
					case IRIFull:
						sb.append("\"").append(iri.toString()).append("\"");
						break;
					default:
						break;
					}

				} else if (t instanceof Constant && ((Constant) t).getType() == Types.VARCHAR) {
					sb.append("\"");
					sb.append(t);
					sb.append("\"");
				} else {
					sb.append(t);
				}
			}
			sb.append(")");
		}
	}

}
