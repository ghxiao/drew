package org.semanticweb.drew.cli;

import it.unical.mat.wrapper.DLVInvocationException;

import java.io.IOException;
import java.util.Arrays;

import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.el.cli.DReWELCLI;
import org.semanticweb.drew.rl.cli.DReWRLCLI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.google.common.base.Joiner;

public class CommandLine {

	/**
	 * @param args
	 * @throws DLVInvocationException
	 * @throws ParseException
	 * @throws IOException
	 * @throws OWLOntologyCreationException
	 */
	public static void main(String[] args) throws OWLOntologyCreationException,
			IOException, ParseException, DLVInvocationException {
		if(args.length == 0){
			printUsage();
			System.exit(0);
		}
		
		String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
		
		Joiner.on(", ").appendTo(System.out, subArgs);
		
		if (args[0].equals("-el")) {
			DReWELCLI.main(subArgs);
		} else if (args[0].equals("-rl")) {
			DReWRLCLI.main(subArgs);
		}
	}

	private static void printUsage() {
		String usage = "Usage: drew [-rl | -el] ...";
		System.err.println(usage);
	}

}
