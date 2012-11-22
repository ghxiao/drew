package org.semanticweb.drew.rl.cli;

import it.unical.mat.dlv.program.Literal;
import it.unical.mat.wrapper.DLVError;
import it.unical.mat.wrapper.DLVInputProgram;
import it.unical.mat.wrapper.DLVInputProgramImpl;
import it.unical.mat.wrapper.DLVInvocation;
import it.unical.mat.wrapper.DLVInvocationException;
import it.unical.mat.wrapper.DLVWrapper;
import it.unical.mat.wrapper.Model;
import it.unical.mat.wrapper.ModelHandler;
import it.unical.mat.wrapper.ModelResult;
import it.unical.mat.wrapper.Predicate;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.ldlpprogram.reasoner.LDLPProgramQueryResultDecompiler;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class DReWRLCLITest {

	int nModels = 0;

	@Test
	public void testNetwork() throws OWLOntologyCreationException, IOException,
			ParseException, DLVInvocationException {
		DReWRLCLI
				.main("-rl -ontology benchmark/network/ontology/network.owl -dlp benchmark/network/rules/network.dlp -filter connect -dlv /Users/xiao/bin/dlv"
						.split("\\s"));
	}


}
