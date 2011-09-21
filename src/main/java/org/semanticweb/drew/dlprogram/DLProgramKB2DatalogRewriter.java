package org.semanticweb.drew.dlprogram;

import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;

public interface DLProgramKB2DatalogRewriter {
	public DLProgram rewrite(DLProgramKB kb);

}
