package org.semanticweb.drew.default_logic.rewriter;

import java.util.List;

import org.semanticweb.drew.default_logic.DefaultLogicKB;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;

class DefaultLogicKBReasoner {

	private DefaultLogicKB kb;

	private DefaultLogicKBRewriter rewriter;
	
	public DefaultLogicKBReasoner(DefaultLogicKB kb) {
		this.kb = kb;
		this.rewriter = new DefaultLogicKBRewriter();
	}

	public List<List<Literal>> computeExtensions() {
		List<ProgramStatement> rewrittenRules = rewriter.rewriteDefaultLogicKB(kb);
		
		// TODO
		
		return null;
		
	}

}
