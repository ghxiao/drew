package org.semanticweb.drew.cli;

import it.unical.mat.wrapper.DLVInputProgram;
import it.unical.mat.wrapper.DLVInvocationException;

import java.io.IOException;

import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.el.cli.DReWELCLI;
import org.semanticweb.drew.rl.cli.DReWRLCLI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public abstract class CommandLine {

	/**
	 * @param args
	 * @throws DLVInvocationException
	 * @throws ParseException
	 * @throws IOException
	 * @throws OWLOntologyCreationException
	 */
	public static void main(String[] args) throws OWLOntologyCreationException,
			IOException, ParseException, DLVInvocationException {
		if (args.length == 0) {
			//printUsage();
			String usage = "Usage: drew [-rl | -el] ...";
			System.err.println(usage);
			System.exit(0);
		}


		if (args[0].equals("-el")) {
			DReWELCLI.main(args);
		} else if (args[0].equals("-rl")) {
			DReWRLCLI.main(args);
		}
	}

	

	public abstract boolean parseArgs(String[] args);

	public abstract void go();

	public abstract void handleDefault(OWLOntology ontology,
			DLVInputProgram inputProgram);

	public abstract void handleDLProgram(OWLOntology ontology,
			DLVInputProgram inputProgram);

	public abstract void handleCQ(OWLOntology ontology,
			DLVInputProgram inputProgram);

}
