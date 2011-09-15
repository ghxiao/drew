package org.semanticweb.drew.elprogram.incremental;

import java.util.List;

import org.semanticweb.drew.dlprogram.DLProgramKBReasoner;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.model.Literal;

public class IncrementalELProgramReasoner implements DLProgramKBReasoner{

	@Override
	public void loadDLProgramKB(DLProgramKB kb) {
		
	}

	@Override
	public List<List<Literal>> getStableModels(String filter) {
		return null;
	}

	@Override
	public List<Literal> getWellFounedModel(String filter) {
		return null;
	}

}
