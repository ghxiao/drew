package org.semanticweb.drew.elprogram.incremental;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.format.DLProgramStorer;
import org.semanticweb.drew.dlprogram.format.DLProgramStorerImpl;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.DLAtomPredicate;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.model.DLProgramKBLoader;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.el.reasoner.DReWELManager;
//import org.semanticweb.drew.el.reasoner.DatalogToStringHelper;
import org.semanticweb.drew.el.reasoner.NamingStrategy;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.google.common.base.Joiner;

public class IncrementalELProgramRewritterTest {

	@Test
	public void testRewriteDLInputSignatures() throws ParseException,
			IOException {
		DReWELManager.getInstance().setNamingStrategy(
				NamingStrategy.IRIFragment);
		FileReader reader = new FileReader("testcase/ex4.dlp");
		DLProgramParser parser = new DLProgramParser(reader);
		DLProgram result = parser.program();
		System.out.println(result);

		System.out.println("--------------------------------");

		IncrementalELProgramRewriter rewritter = new IncrementalELProgramRewriter();
		Collection<Clause> rules = rewritter.rewriteDLInputSignatures(result
				.getDLInputSignatures());

		Joiner.on("\n").appendTo(System.out, rules);
		
//		DLProgramStorer storer = new DLProgramStorerImpl();
//		
//		DatalogToStringHelper datalogToStringHelper = new DatalogToStringHelper();
//
//		System.out.println(datalogToStringHelper.writeDLProgram(rules));
		// assertEquals("DL[S1 += p1,S1 -= p1;Q](X)", result.toString());

	}

	@Test
	public void testRewriteDLAtomPredicate001() throws ParseException, IOException {
		DReWELManager.getInstance().setNamingStrategy(
				NamingStrategy.IRIFragment);

		String s = "DL[S1 += p1,S1 -= p1;Q](X,Y)";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		Literal literal = parser.literal();

		DLAtomPredicate predicate = (DLAtomPredicate) literal.getPredicate();

		IncrementalELProgramRewriter rewritter = new IncrementalELProgramRewriter();
		List<Clause> rules = rewritter.rewriteDLAtomPredicate(predicate);

		Joiner.on("\n").appendTo(System.out, rules);
		
//		DatalogToStringHelper datalogToStringHelper = new DatalogToStringHelper();
//
//		System.out.println(datalogToStringHelper.writeDLProgram(rules));
	}

	@Test
	public void testRewriteDLAtomPredicate002() throws ParseException, IOException {
		DReWELManager.getInstance().setNamingStrategy(
				NamingStrategy.IRIFragment);

		String s = "DL[S1 += p1,S1 -= p1;Q](X)";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		Literal literal = parser.literal();

		DLAtomPredicate predicate = (DLAtomPredicate) literal.getPredicate();

		IncrementalELProgramRewriter rewritter = new IncrementalELProgramRewriter();
		List<Clause> rules = rewritter.rewriteDLAtomPredicate(predicate);

		Joiner.on("\n").appendTo(System.out, rules);
		
//		DatalogToStringHelper datalogToStringHelper = new DatalogToStringHelper();
//
//		System.out.println(datalogToStringHelper.writeDLProgram(rules));
	}

	@Test
	public void testRewriteDLProgram() throws ParseException, IOException {
		DReWELManager.getInstance().setNamingStrategy(
				NamingStrategy.IRIFragment);

		String s = "r1(a,b).\n"
				+ "r2(b,c).\n"
				+ "q :- not DL[R1 += r1; R](a,c), DL[R1+=r1, R2 += r2; R](a,c).";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		DLProgram program = parser.program();

		IncrementalELProgramRewriter rewritter = new IncrementalELProgramRewriter();
		List<ProgramStatement> rules = rewritter.rewriteELProgram(program);

		Joiner.on("\n").appendTo(System.out, rules);
		
//		DatalogToStringHelper datalogToStringHelper = new DatalogToStringHelper();
//
//		System.out.println(datalogToStringHelper.writeDLProgram(rules));
	}

	@Test
	public void testRewriteDLProgramKB() throws ParseException,
			OWLOntologyCreationException, IOException {

		DReWELManager.getInstance().setNamingStrategy(
				NamingStrategy.IRIFragment);
		
		BufferedReader reader = new BufferedReader(new FileReader(
				"testcase/ex4.dlp"));

		DLProgramParser dlProgramParser = new DLProgramParser(reader);
		DLProgram program = dlProgramParser.program();

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(new File("testcase/ex4.owl"));

		DLProgramKB kb = new DLProgramKB();
		kb.setOntology(ontology);
		kb.setProgram(program);

		IncrementalELProgramRewriter rewriter = new IncrementalELProgramRewriter();
		
		List<ProgramStatement> rules = rewriter.rewriteELProgramKB(kb);

		DLProgramStorer storer =new DLProgramStorerImpl();
		
		Joiner.on("\n").appendTo(System.out, rules);
		
		//storer.storeProgramStatements(rules, new FileWriter("testcase/ex4.dl"));
		
		
//		DatalogToStringHelper datalogToStringHelper = new DatalogToStringHelper();
//
//		datalogToStringHelper.saveToFile(rules, "testcase/ex4.dl");
		
		//System.out.println(datalogToStringHelper.writeDLProgram(rules));
	}

	public static void main(String[] args) throws ParseException, IOException {

		new IncrementalELProgramRewritterTest().testRewriteDLInputSignatures();
	}

}
