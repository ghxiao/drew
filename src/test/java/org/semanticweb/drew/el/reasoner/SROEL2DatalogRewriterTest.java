package org.semanticweb.drew.el.reasoner;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.format.DLProgramStorer;
import org.semanticweb.drew.dlprogram.format.DLProgramStorerImpl;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.el.profile.SROELProfile;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class SROEL2DatalogRewriterTest {

	@Test
	public void test() throws OWLOntologyCreationException {
		SROELProfile profile = new SROELProfile();
		//String ontologyFile= "testcase/policy.owl";
		String ontologyFile= "benchmark/galen/ontology/EL-GALEN.owl";
		File file = new File(ontologyFile);
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
		
		SROEL2DatalogRewriter elCompiler = new SROEL2DatalogRewriter();
		final List<ProgramStatement> compiledOntology = elCompiler.rewrite(ontology)
				.getStatements();
		DReWELManager.getInstance().setDatalogFormat(DatalogFormat.DLV);
		DReWELManager.getInstance().setNamingStrategy(NamingStrategy.IRIFragment);
		
		//DatalogToStringHelper f = new DatalogToStringHelper(); 
		DLProgramStorer storer =new DLProgramStorerImpl();
		
		//storer.storeDLProgram(program, target)
		
		f.saveToFile(compiledOntology, "benchmark/galen/ontology/el-galen.dl");
	
	}

}
