package org.semanticweb.drew.dlprogram.format;

import java.io.IOException;

import org.semanticweb.drew.dlprogram.model.DLProgram;

public interface DLProgramStorer {
	
	/**
	 * store the dl-program to the target
	 * @param program
	 * @param target
	 * @throws IOException 
	 */
	public void storeDLProgram(DLProgram program, Appendable target) throws IOException;
}
