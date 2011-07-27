package org.semanticweb.drew.el;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.el.profile.SROELProfile;
import org.semanticweb.drew.el.reasoner.DLProgramToStringBuilder;
import org.semanticweb.drew.el.reasoner.DReWELManager;
import org.semanticweb.drew.el.reasoner.NamingStrategy;
import org.semanticweb.drew.el.reasoner.SROEL2DatalogRewriter;
import org.semanticweb.drew.elprogram.ELProgramKBCompiler;
import org.semanticweb.drew.elprogram.ELProgramReasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

import org.junit.Test;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static TestSuite suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws OWLOntologyCreationException 
     */
    public void testApp() throws OWLOntologyCreationException
    {
    	DReWELManager.getInstance().setNamingStrategy(NamingStrategy.IRIFragment);
		final String owlFileName = "testcase/Test01.owl";
		
		File file = new File(owlFileName);
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
		
		SROELProfile sroelProfile = new SROELProfile();
		OWLProfileReport report = sroelProfile.checkOntology(ontology);
		
		System.out.println(report);
    	
		System.out.println("---------------------------------------");
		
		SROEL2DatalogRewriter rewriter = new SROEL2DatalogRewriter();
		DLProgram datalog = rewriter.rewrite(ontology);
		
		rewriter.saveToFile("testcase/test01.owl.dl");
		
		
		DLProgramToStringBuilder builder = new DLProgramToStringBuilder();
		String strDatalog = builder.toString(datalog);
		
		
		
		System.out.println(strDatalog);
		System.out.println("---------------------------------------");
		
    }
    
    @Test
    public void testELP() throws OWLOntologyCreationException, FileNotFoundException, ParseException
    {
    	//DReWELManager.getInstance().setNamingStrategy(NamingStrategy.IRIFragment);
		final String owlFileName = "testcase/test02.owl";
		//final String dlpFileName = "testcase/test01.dlp";
		final String dlpFileName = "testcase/test01.dlp";
		File file = new File(owlFileName);
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
		DLProgramParser parser = new DLProgramParser(new FileReader(dlpFileName));
		DLProgram elprogram = parser.program();
		DLProgramKB kb = new DLProgramKB();
		kb.setOntology(ontology);
		kb.setProgram(elprogram);
		
		ELProgramKBCompiler compiler = new ELProgramKBCompiler();
		DLProgram datalog = compiler.compile(kb);
		System.out.println(datalog);
		//System.out.println(new DLProgramToStringBuilder().toString(datalog));
    }
}
