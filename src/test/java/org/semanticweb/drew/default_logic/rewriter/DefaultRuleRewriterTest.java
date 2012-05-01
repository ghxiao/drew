package org.semanticweb.drew.default_logic.rewriter;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.semanticweb.drew.default_logic.DefaultRule;
import org.semanticweb.drew.default_logic.DefaultRuleTest;
import org.semanticweb.drew.dlprogram.format.DLProgramStorer;
import org.semanticweb.drew.dlprogram.format.DLProgramStorerImpl;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class DefaultRuleRewriterTest {

	@Test
	public void test() {
		// fail("Not yet implemented");
	}

	@Test
	public void testRewriteRule() throws ParseException,
			OWLOntologyCreationException, IOException {
		InputStream owlStream = DefaultRuleTest.class
				.getResourceAsStream("res/bird.owl");
		OWLOntology ontology = OWLManager.createOWLOntologyManager()
				.loadOntologyFromOntologyDocument(owlStream);

		InputStream stream = DefaultRuleTest.class
				.getResourceAsStream("res/bird.df");
		DLProgramParser dfParser = new DLProgramParser(stream);
		dfParser.setOntology(ontology);

		List<DefaultRule> defaultRules = dfParser.defaultRules();
		System.out.println(defaultRules);

		DefaultRuleRewriter rewriter = new DefaultRuleRewriter();
		// List<Clause> result = rewriter.rewrite(defaultRule);
		List<ProgramStatement> result = rewriter.rewriteDefaultLogicKB(
				ontology, defaultRules);

		DLProgramStorer storer = new DLProgramStorerImpl();
		storer.storeProgramStatements(result, System.out);
		FileWriter writer = null;
		try {
			writer = new FileWriter("tmp.dlv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		storer.storeProgramStatements(result, writer);
		writer.close();
		//System.out.println(result);

		//System.out.println(rewriter.commonRules);
	}

	@Test
	public void testDefaultLogicCommonRules() throws FileNotFoundException,
			ParseException {
		InputStream stream = DefaultRuleRewriterTest.class
				.getResourceAsStream("default.dl");
		DLProgramParser parser = new DLProgramParser(stream);
		DLProgram program = parser.program();
		
		
		
		
		System.out.println(program);
	}

	public static void main(String[] args) throws OWLOntologyCreationException, ParseException, IOException {
		new DefaultRuleRewriterTest().testRewriteRule();
	}

}
