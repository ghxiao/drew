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

	public RLProgramStorerImpl() {
		ldlpCompilerManager = LDLPCompilerManager.getInstance();
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
		boolean isIRI = false;
		String name = t.getName();
		String newp = name;
		try {
			if (name.matches("^o\\d+$")) {
				String d = ldlpCompilerManager.decompile(name);
				Matcher matcher = qIriPattern.matcher(d);
				if (matcher.find()) {
					newp = matcher.group(1);
					isIRI = true;
				} else {
					// datatype
					// newp = d;
					newp = d.replace('\"', '\'');
				}
			}

			// if (isIRI)
			write("\"", target);
			target.append(newp);
			// if (isIRI)
			write("\"", target);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
