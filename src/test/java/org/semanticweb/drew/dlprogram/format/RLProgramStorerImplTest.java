package org.semanticweb.drew.dlprogram.format;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.ldlp.reasoner.LDLPOntologyCompiler;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class RLProgramStorerImplTest {

	@Test
	public void testRegex() {
		Pattern qIriPattern = Pattern.compile("^<.*#(.*)>$");
		Pattern pdPattern = Pattern.compile("^p\\d+$");

		System.out.println(pdPattern.matcher("p12").find());

		String qiri = "<http://www.kr.tuwien.ac.at/staff/xiao/ontology/network.owl#Node>";

		Matcher matcher = qIriPattern.matcher(qiri);

		if (matcher.find()) {
			String group = matcher.group();

			System.out.println(matcher.group(1));
 
			System.out.println(group);
		} else {
			System.out.println("no match");
		}
		
		

		// System.out.println(qiri.replaceAll(pattern, "$1"));
	}

	@Test
	public void testDatatype() throws OWLOntologyCreationException {
		OWLOntology ontology = OWLManager.createOWLOntologyManager()
				.loadOntologyFromOntologyDocument(
						new File("src/test/resources/testDatatype.owl"));
		LDLPOntologyCompiler rewriter = new LDLPOntologyCompiler();
		List<ProgramStatement> datalog = rewriter.compile(ontology);
		// DLProgramStorer storer = new DLProgramStorerImpl();
		RLProgramStorerImpl storer = new RLProgramStorerImpl();
		storer.setBase(100);
		storer.setOutputingTypeTag(false);
		storer.store(datalog, System.out);
	}

}
