package org.semanticweb.drew.rl.cli;

import it.unical.mat.wrapper.DLVInvocationException;

import java.io.IOException;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class DReWRLCLITest {

	@Test
	public void testNetworkDLP() throws OWLOntologyCreationException,
			IOException, ParseException, DLVInvocationException {
		DReWRLCLI.main("-rl", "-ontology",
				"benchmark/network/ontology/network.owl", "-dlp",
				"benchmark/network/rules/network.dlp", "-filter", "connect",
				"-dlv", "/Users/xiao/bin/dlv");
	}

	@Test
	public void testNetworkOntology() throws OWLOntologyCreationException,
			IOException, ParseException, DLVInvocationException {
		DReWRLCLI.main("-rl", "--rewriting-only", "-ontology",
				"benchmark/network/ontology/network.owl");
	}
	
	@Test
	public void testGraphOntology() throws OWLOntologyCreationException,
			IOException, ParseException, DLVInvocationException {
		DReWRLCLI.main("-rl", "-ontology",
				"sample_data/graph.owl", "-dlp", "sample_data/graph.dlp", "-dlv", "/Users/xiao/bin/dlv");
	}
	
	@Test
	public void testEDIOntology() throws OWLOntologyCreationException,
			IOException, ParseException, DLVInvocationException {
		DReWRLCLI.main("-rl", "--rewriting-only", "-ontology",
				"/Users/xiao/Dropbox/krrepos/xiao/benchmark/data/ontologies/edi/testBI5.owl");
	}

	@Test
	public void testSparql() throws OWLOntologyCreationException, IOException,
			ParseException, DLVInvocationException {
		DReWRLCLI
				.main("-rl -ontology sample_data/U0.owl -sparql sample_data/lubm_q1.sparql -dlv /Users/xiao/bin/dlv"
						.split("\\s"));
	}

}
