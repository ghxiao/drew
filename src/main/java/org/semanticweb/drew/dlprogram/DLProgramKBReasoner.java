package org.semanticweb.drew.dlprogram;

import java.util.List;

import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.model.Literal;

public interface DLProgramKBReasoner {

	public void loadDLProgramKB(DLProgramKB kb);
	
	public List<List<Literal>> getStableModels(String filter);
	
	public List<Literal> getWellFounedModel(String filter);
}
