package org.semanticweb.drew.dlprogram.format;

import java.io.IOException;

import org.semanticweb.drew.dlprogram.model.DLProgram;

public interface DLProgramStorer {

	/**
	 * store the dl-program to the target
	 * 
	 * @param program
	 *            DL-Program
	 * @param target
	 *            The storage target which implements
	 *            {@link java.lang.Appendable} , e.g.,
	 *            {@link java.lang.StringBuilder}, or {@link java.io.FileWriter}
	 * @throws IOException
	 */
	public void storeDLProgram(DLProgram program, Appendable target);
	
	
}
