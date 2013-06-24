package org.semanticweb.drew.rl.cli;

import it.unical.mat.wrapper.DLVInvocationException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class DReWRLCLITest {

	public String findDLV() {

		String home = System.getenv("HOME");

		String[] candidatesDLVPaths = { home + "/usr/bin/dlv",
				home + "/bin/dlv", home + "/usr/local/dlv", "/usr/bin/dlv",
				"/bin/dlv", "/usr/local/dlv", };

		for (String p : candidatesDLVPaths) {
			if (Files.exists(FileSystems.getDefault().getPath(p))) {
				return p;
			}
		}

		throw new IllegalStateException("can not find dlv");
	}

	@Test
	public void testNetworkDLP() throws OWLOntologyCreationException,
			IOException, ParseException, DLVInvocationException {
		DReWRLCLI.main("-rl", "-ontology",
				"benchmark/network/ontology/network.owl", "-dlp",
				"benchmark/network/rules/network.dlp", "-filter", "connect",
				"-dlv", findDLV());
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
		DReWRLCLI.main("-rl", "-ontology", "sample_data/graph.owl", "-dlp",
				"sample_data/graph.dlp", "-dlv", findDLV());
	}

	//@Test
	public void testEDIOntology() throws OWLOntologyCreationException,
			IOException, ParseException, DLVInvocationException {
		DReWRLCLI
				.main("-rl", "--rewriting-only", "-ontology",
						"/Users/xiao/Dropbox/krrepos/xiao/benchmark/data/ontologies/edi/testBI5.owl");
	}

	@Test
	public void testSparql() throws OWLOntologyCreationException, IOException,
			ParseException, DLVInvocationException {
		DReWRLCLI
				.main(("-rl -ontology sample_data/U0.owl -sparql sample_data/lubm_q1.sparql -dlv " + findDLV())
						.split("\\s"));
		
		DReWRLCLI
		.main(("-rl -ontology sample_data/U0.owl -sparql sample_data/lubm_q2.sparql -dlv " + findDLV())
				.split("\\s"));
	}
	
	@Test
	public void testDatatype() throws OWLOntologyCreationException, IOException,
			ParseException, DLVInvocationException {
		DReWRLCLI
				.main(("-rl -ontology src/test/resources/testDatatype.owl -dlp src/test/resources/testDatatype.dlp -filter q -dlv " + findDLV())
						.split("\\s"));
		
	}
	
	@Test
	public void testArithmetic() throws OWLOntologyCreationException, IOException,
			ParseException, DLVInvocationException {
		// expected output "{ q(1) q(3) q(5) }"
		DReWRLCLI
				.main(("-rl -ontology src/test/resources/testArithmetic.owl -dlp src/test/resources/testArithmetic.dlp -filter q -N 10 -dlv " + findDLV())
						.split("\\s"));
		
	}

}
