package org.semanticweb.drew.dlprogram.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import org.junit.Test;
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

	@Test
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
	public void testDLInputOperators() throws OWLOntologyCreationException, ParseException{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.createOntology();
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLObjectProperty property = factory
				.getOWLObjectProperty(IRI
						.create("http://www.kr.tuwien.ac.at/staff/xiao/ontology/graph#arc"));
		manager.addAxiom(ontology,  factory.getOWLDeclarationAxiom(property));
		
		String s1 = "DL[arc+=tc;arc](X, Y)";
		DLProgramParser parser1 = new DLProgramParser(new StringReader(s1));
		parser1.setOntology(ontology);
		Literal lit1 = parser1.literal();
		System.out.println(lit1);
		
		String s2 = "DL[arc-=n_tc;arc](X, Y)";
		DLProgramParser parser2 = new DLProgramParser(new StringReader(s2));
		parser2.setOntology(ontology);
		Literal lit2 = parser2.literal();
		System.out.println(lit2);
		
		String s3 = "DL[arc~=poss_arc;arc](X, Y)";
		DLProgramParser parser3 = new DLProgramParser(new StringReader(s3));
		parser3.setOntology(ontology);
		Literal lit3 = parser3.literal();
		System.out.println(lit3);
	}
	
	@Test
	public void testDLPredicate() throws ParseException, OWLOntologyCreationException {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.createOntology();
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLObjectProperty property = factory
				.getOWLObjectProperty(IRI
						.create("http://www.kr.tuwien.ac.at/staff/xiao/ontology/graph#arc"));
		manager.addAxiom(ontology,  factory.getOWLDeclarationAxiom(property));

		String s1 = "DL[\"http://www.kr.tuwien.ac.at/staff/xiao/ontology/graph#arc\"](X, Y)";
		DLProgramParser parser1 = new DLProgramParser(new StringReader(s1));
		parser1.setOntology(ontology);
		Literal lit1 = parser1.literal();
		System.out.println(lit1);
		
		String s2 = "DL[arc](X, Y)";
		DLProgramParser parser2 = new DLProgramParser(new StringReader(s2));
		parser2.setOntology(ontology);
		Literal lit2 = parser2.literal();
		System.out.println(lit2);
		
		String s3 = "DL[\"arc\"](X, Y)";
		DLProgramParser parser3 = new DLProgramParser(new StringReader(s3));
		parser3.setOntology(ontology);
		Literal lit3 = parser3.literal();
		System.out.println(lit3);
		
		String s4 = "DL[arc+=tc;arc](X, Y)";
		DLProgramParser parser4 = new DLProgramParser(new StringReader(s4));
		parser4.setOntology(ontology);
		Literal lit4 = parser4.literal();
		System.out.println(lit4);
		
		String s5 = "DL[;arc](X, Y)";
		DLProgramParser parser5 = new DLProgramParser(new StringReader(s5));
		parser5.setOntology(ontology);
		Literal lit5 = parser5.literal();
		System.out.println(lit5);
		
	}
	
	@Test
	public void testConstants() throws ParseException{
		String s1 = "p(X, \"123\")";
		DLProgramParser parser1 = new DLProgramParser(new StringReader(s1));
		//parser5.setOntology(ontology);
		Literal lit1 = parser1.literal();
		System.out.println(lit1);
		
		String s2 = "p(X, 123)";
		DLProgramParser parser2 = new DLProgramParser(new StringReader(s2));
		//parser5.setOntology(ontology);
		Literal lit2 = parser2.literal();
		System.out.println(lit2);
		
		String s3 = "p(X, \"<http://a.b.com/123>\")";
		DLProgramParser parser3 = new DLProgramParser(new StringReader(s3));
		//parser5.setOntology(ontology);
		Literal lit3 = parser3.literal();
		System.out.println(lit3);
	}
	
//	@Test
//	public void testXSDTypes() throws ParseException{
//		String s5 = "p(X, \"abc\"^^xsd:string)";
//		DLProgramParser parser5 = new DLProgramParser(new StringReader(s5));
//		//parser5.setOntology(ontology);
//		Literal lit5 = parser5.literal();
//		System.out.println(lit5);
//	}
	
	@Test
	public void testInt() throws ParseException{
		String s5 = "#int(X)";
		DLProgramParser parser5 = new DLProgramParser(new StringReader(s5));
		//parser5.setOntology(ontology);
		Literal lit5 = parser5.literal();
		assertEquals(s5, lit5.toString());
		System.out.println(lit5);
	}
	public static void main(String[] args) throws ParseException,
			FileNotFoundException, OWLOntologyCreationException {
		new DLProgramParserTest().testDLInputOperators();
	}

}
