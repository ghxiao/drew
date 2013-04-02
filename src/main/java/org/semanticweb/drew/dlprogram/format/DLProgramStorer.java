package org.semanticweb.drew.dlprogram.format;

import java.io.IOException;
import java.util.Collection;

import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;

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
	public void store(DLProgram program, Appendable target);
	
	public void store(Collection<ProgramStatement> program, Appendable target);
	
	public void store(ProgramStatement stmt, Appendable target);
	
	public void setPrefix(String prefix);
	
}
