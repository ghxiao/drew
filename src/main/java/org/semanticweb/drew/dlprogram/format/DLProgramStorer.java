package org.semanticweb.drew.dlprogram.format;

import org.semanticweb.drew.dlprogram.model.DLProgram;

public interface DLProgramStorer {
	
	/**
	 * store the dl-program to the target
	 * @param program
	 * @param target
	 */
	public void storeDLProgram(DLProgram program, Appendable target);
}
