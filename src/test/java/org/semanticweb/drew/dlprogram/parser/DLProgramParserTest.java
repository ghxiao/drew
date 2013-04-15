package org.semanticweb.drew.dlprogram.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import org.junit.Test;
import org.semanticweb.drew.benchmark.PaperReviewBenchmarkGenerator;
import org.semanticweb.drew.default_logic.DefaultRule;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class DLProgramParserTest {

	@Test
	public void test() throws ParseException {

		// System.out.println(PaperReviewBenchmarkGenerator.elpRules);
		// DLProgramParser parser = new DLProgramParser(new
		// StringReader(PaperReviewBenchmarkGenerator.elpRules));
		// DLProgram program = parser.program();
		// System.out.println(program);
	}

	@Test
	public void testNeg() throws ParseException {
		String s = "p(X) :- q(X), not r(X), not -s(Y), -f(X,Y). ";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		DLProgram program = parser.program();
		System.out.println(program);
	}

	@Test
	public void testFullURI() throws ParseException, FileNotFoundException,
			OWLOntologyCreationException {
		// String s = "p(X) :- q(X), not r(X), not -s(Y), -f(X,Y). ";
		DLProgramParser parser = new DLProgramParser(new FileReader(
				"sample_data/graph.dlp"));

		OWLOntology ontology = OWLManager.createOWLOntologyManager()
				.loadOntologyFromOntologyDocument(
						new File("sample_data/graph.owl"));

		parser.setOntology(ontology);

		DLProgram program = parser.program();
		System.out.println(program);
	}

	// expect a failure because of DL[;..]
	@Test(expected = ParseException.class)
	public void testSemiColon1() throws ParseException, OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.createOntology();
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLObjectProperty property = factory
				.getOWLObjectProperty(IRI
						.create("http://www.kr.tuwien.ac.at/staff/xiao/ontology/graph#arc"));
		manager.addAxiom(ontology,  factory.getOWLDeclarationAxiom(property));
		String s = "DL[;\"http://www.kr.tuwien.ac.at/staff/xiao/ontology/graph#arc\"](X, Y)";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		parser.setOntology(ontology);
		Literal program = parser.literal();
		System.out.println(program);
	}

	@Test
	public void testSemiColon2() throws ParseException, OWLOntologyCreationException {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.createOntology();
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLObjectProperty property = factory
				.getOWLObjectProperty(IRI
						.create("http://www.kr.tuwien.ac.at/staff/xiao/ontology/graph#arc"));
		manager.addAxiom(ontology,  factory.getOWLDeclarationAxiom(property));

		String s = "DL[\"http://www.kr.tuwien.ac.at/staff/xiao/ontology/graph#arc\"](X, Y)";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		parser.setOntology(ontology);
		Literal program = parser.literal();
		System.out.println(program);
	}

	public static void main(String[] args) throws ParseException,
			FileNotFoundException, OWLOntologyCreationException {
		new DLProgramParserTest().testFullURI();
	}

}
