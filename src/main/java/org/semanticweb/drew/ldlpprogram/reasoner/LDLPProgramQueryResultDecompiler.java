package org.semanticweb.drew.ldlpprogram.reasoner;

import org.semanticweb.drew.dlprogram.model.Predicate;
import org.semanticweb.drew.ldlp.reasoner.LDLPQueryResultDecompiler;

public class LDLPProgramQueryResultDecompiler extends LDLPQueryResultDecompiler {

	@Override
	public Predicate decompilePredicate(Predicate predicate) {
		return predicate;
	}

}
