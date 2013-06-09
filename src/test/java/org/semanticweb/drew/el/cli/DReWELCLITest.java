package org.semanticweb.drew.el.cli;

import it.unical.mat.wrapper.DLVInvocationException;

import java.io.IOException;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class DReWELCLITest {


	@Test
	public void testRewriteOntology() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main("-r -ontology sample_data/U0.owl".split("\\s"));
	}
	
	@Test
	public void testDLP() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main("-ontology sample_data/U0.owl -dlp sample_data/lubm_0.elp -dlv /Users/xiao/bin/dlv -filter f -v".split("\\s"));
	}
	
	@Test
	public void test1() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main("-ontology sample_data/U0.owl -cq testcase/lubm_q1.cq -dlv /Users/xiao/bin/dlv".split("\\s"));
	}

	@Test
	public void test2() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main("-ontology sample_data/U0.owl -dlp testcase/lubm_5.elp -filter f -dlv /Users/xiao/bin/dlv".split("\\s"));
	}
	
	@Test
	public void testNetwork() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main("-ontology benchmark/network/ontology/network.owl -dlp benchmark/network/rules/network.dlp -dlv /Users/xiao/bin/dlv".split("\\s"));
	}
	
	@Test
	public void testNixon() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main("-ontology testcase/nixon_pos.owl -default testcase/nixon_pos.df -dlv /Users/xiao/bin/dlv".split("\\s"));
	}
	
	@Test
	public void testNixonWF() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main("-wf -ontology testcase/nixon_pos.owl -default testcase/nixon_pos.df -dlv /Users/xiao/bin/dlv".split("\\s"));
	}
	
	@Test
	public void testPR() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main(
				"-ontology benchmark/publication.owl -dlp benchmark/reviewers-1.elp -filter a -dlv /usr/bin/dlv".split("\\s"));
	}
	
	@Test
	public void testConstraint() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main(
				"-ontology testcase/ex4.owl -dlp testcase/constraint.dlp -filter a -dlv /usr/bin/dlv".split("\\s"));
	}
	
	@Test
	public void testLUBM47() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main(
				"-ontology benchmark/lubm/ontology/lubm4.owl -dlp benchmark/lubm/rules/lubm_7.elp -dlv /usr/bin/dlv -rewriting inc".split("\\s"));
	}
	

	@Test
	public void testPolicy() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main(
				"-ontology testcase/policy.owl -dlp testcase/empty.dlp --rewriting-only -dlv /usr/bin/dlv".split("\\s"));
	}
	
	@Test
	public void testDefault() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main(
				"-ontology testcase/bird.owl -default testcase/bird.df -dlv /Users/xiao/bin/dlv".split("\\s"));
	}
	
	public static void main(String[] args) throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException{
		new DReWELCLITest().testNetwork();
	}
}
