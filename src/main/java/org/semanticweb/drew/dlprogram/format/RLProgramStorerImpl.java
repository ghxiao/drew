package org.semanticweb.drew.dlprogram.format;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.ldlp.reasoner.LDLPCompilerManager;

/**
 * Formatting DL-Programs over RL ontology
 * 
 * Predicate pi -> Fragment
 * 
 * @author xiao
 * 
 */

public class RLProgramStorerImpl extends DLProgramStorerImpl {

	LDLPCompilerManager ldlpCompilerManager;
	Pattern qIriPattern = Pattern.compile("^<.*#(.*)>$");
	//Pattern dataypeTagPattern = Pattern.compile("\\^\\^.*$");
	
	private int base = 0;
	private boolean outputtingTypeTag = true;

	public RLProgramStorerImpl() {
		ldlpCompilerManager = LDLPCompilerManager.getInstance();
	}

	public void setBase(int base) {
		this.base = base;
	}
	
	/**
	 * if outputtingTypeTag is set. the tag (e.g. ^^xsd:sting) will be output;
	 * otherwise, it will be dropped
	 * Default value: true
	 * 
	 * @param outputtingTypeTag
	 */
	public void setOutputingTypeTag(boolean outputtingTypeTag){
		this.outputtingTypeTag = outputtingTypeTag;
	}

	@Override
	void writeNormalPredicate(NormalPredicate predicate, Appendable target) {

		// Pattern pdPattern = Pattern.compile("^p\\d+$");
		try {
			if (getPrefix() != null) {
				target.append(getPrefix());
			}
			String name = predicate.getName();
			String newp = name;
			if (name.matches("^p\\d+$")) {
				String d = ldlpCompilerManager.decompile(name);
				Matcher matcher = qIriPattern.matcher(d);
				if (matcher.find()) {
					newp = matcher.group(1);
				}
			}
			target.append(newp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	void writeStringConstant(Term t, Appendable target) {
		boolean unquote = false;
		String name = t.getName();
		String newp = name;
		try {
			if (name.matches("^o\\d+$")) {
				String d = ldlpCompilerManager.decompile(name);
				Matcher matcher = qIriPattern.matcher(d);
				if (matcher.find()) {
					newp = matcher.group(1);
					// isIRI = true;
				} else {
					// datatype
					// newp = d;
					newp = d.replace('\"', '\'');
					
					if(!outputtingTypeTag){
						newp = newp.replaceAll("\\^\\^(.*)$", "");
					}
				}
			} else {
				// t is a numerical
				if (base != 0) {
					unquote = true;
					newp = String
							.valueOf((int) (Double.parseDouble(name) * base));
				}
			}

			if (!unquote) {
				write("\"", target);
			}
			target.append(newp);
			if (!unquote) {
				write("\"", target);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
