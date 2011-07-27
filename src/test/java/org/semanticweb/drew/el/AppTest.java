package org.semanticweb.drew.el;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.el.profile.SROELProfile;
import org.semanticweb.drew.el.reasoner.DatalogToStringBuilder;
import org.semanticweb.drew.el.reasoner.DReWELManager;
import org.semanticweb.drew.el.reasoner.NamingStrategy;
import org.semanticweb.drew.el.reasoner.SROEL2DatalogRewriter;
import org.semanticweb.drew.elprogram.ELProgramKBCompiler;
import org.semanticweb.drew.elprogram.ELProgramReasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
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
		
		
		DatalogToStringBuilder builder = new DatalogToStringBuilder();
		String strDatalog = builder.toString(datalog);
		
		
		
		System.out.println(strDatalog);
		System.out.println("---------------------------------------");
		
    }
    
    public void testTopBot() throws OWLOntologyCreationException
    {
    	DReWELManager.getInstance().setNamingStrategy(NamingStrategy.IRIFragment);
		final String owlFileName = "testcase/TestTopBot.owl";
		
		File file = new File(owlFileName);
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
		
		SROELProfile sroelProfile = new SROELProfile();
		OWLProfileReport report = sroelProfile.checkOntology(ontology);
		
		System.out.println(report);
    	
		System.out.println("---------------------------------------");
		
		SROEL2DatalogRewriter rewriter = new SROEL2DatalogRewriter();
		DLProgram datalog = rewriter.rewrite(ontology);
		
		DatalogToStringBuilder builder = new DatalogToStringBuilder();
		String strDatalog = builder.toString(datalog);
		
		System.out.println(strDatalog);
		System.out.println("---------------------------------------");
    }

    public void testSubRole() throws OWLOntologyCreationException
    {
    	DReWELManager.getInstance().setNamingStrategy(NamingStrategy.IRIFragment);
		final String owlFileName = "testcase/TestSubRole.owl";
		
		File file = new File(owlFileName);
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
		
		SROELProfile sroelProfile = new SROELProfile();
		OWLProfileReport report = sroelProfile.checkOntology(ontology);
		
		System.out.println(report);
    	
		System.out.println("---------------------------------------");
		
		SROEL2DatalogRewriter rewriter = new SROEL2DatalogRewriter();
		DLProgram datalog = rewriter.rewrite(ontology);
		
		DatalogToStringBuilder builder = new DatalogToStringBuilder();
		String strDatalog = builder.toString(datalog);
		
		System.out.println(strDatalog);
		System.out.println("---------------------------------------");
    }

    public void testNom() throws OWLOntologyCreationException
    {
    	DReWELManager.getInstance().setNamingStrategy(NamingStrategy.IRIFragment);
		final String owlFileName = "testcase/TestNom.owl";
		
		File file = new File(owlFileName);
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
		
		SROELProfile sroelProfile = new SROELProfile();
		OWLProfileReport report = sroelProfile.checkOntology(ontology);
		
		System.out.println(report);
    	
		System.out.println("---------------------------------------");
		
		SROEL2DatalogRewriter rewriter = new SROEL2DatalogRewriter();
		DLProgram datalog = rewriter.rewrite(ontology);
		
		DatalogToStringBuilder builder = new DatalogToStringBuilder();
		String strDatalog = builder.toString(datalog);
		
		System.out.println(strDatalog);
		System.out.println("---------------------------------------");
    }

    public void testSelf() throws OWLOntologyCreationException
    {
    	DReWELManager.getInstance().setNamingStrategy(NamingStrategy.IRIFragment);
		final String owlFileName = "testcase/TestSelf.owl";
		
		File file = new File(owlFileName);
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
		
		SROELProfile sroelProfile = new SROELProfile();
		OWLProfileReport report = sroelProfile.checkOntology(ontology);
		
		System.out.println(report);
    	
		System.out.println("---------------------------------------");
		
		SROEL2DatalogRewriter rewriter = new SROEL2DatalogRewriter();
		DLProgram datalog = rewriter.rewrite(ontology);
		
		DatalogToStringBuilder builder = new DatalogToStringBuilder();
		String strDatalog = builder.toString(datalog);
		
		System.out.println(strDatalog);
    }
    
    public void testRoleChain() throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException
    {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
//		OWLDataFactory factory = man.getOWLDataFactory();
//    	
//		OWLObjectProperty r1 = factory.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/ontologies/Test01.owl#R1"));
//		OWLObjectProperty r2 = factory.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/ontologies/Test01.owl#R2"));
//		OWLObjectProperty r = factory.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/ontologies/Test01.owl#R"));
//		
//		List<OWLObjectPropertyExpression> chain = new ArrayList<OWLObjectPropertyExpression>();
//		chain.add(r1);
//		chain.add(r2);
//		OWLSubPropertyChainOfAxiom axiom = factory.getOWLSubPropertyChainOfAxiom(chain , r);
//		
//		OWLOntology ontology = man.createOntology();
//		man.addAxiom(ontology, axiom);
		
    	DReWELManager.getInstance().setNamingStrategy(NamingStrategy.IRIFragment);
		final String owlFileName = "testcase/testRoleChain.owl";
		File file = new File(owlFileName);
		OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);
		
		man.saveOntology(ontology, new FileOutputStream(owlFileName));
		SROELProfile sroelProfile = new SROELProfile();
		OWLProfileReport report = sroelProfile.checkOntology(ontology);
		
		System.out.println(report);
    	
		System.out.println("---------------------------------------");
		
		SROEL2DatalogRewriter rewriter = new SROEL2DatalogRewriter();
		DLProgram datalog = rewriter.rewrite(ontology);
		
		DatalogToStringBuilder builder = new DatalogToStringBuilder();
		String strDatalog = builder.toString(datalog);
		
		System.out.println(strDatalog);
    }
    
    @Test
    public void testELProgram() throws OWLOntologyCreationException, FileNotFoundException, ParseException
    {
    	DReWELManager.getInstance().setNamingStrategy(NamingStrategy.IRIFragment);
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
		//System.out.println(datalog);
		System.out.println(new DatalogToStringBuilder().toString(datalog));
    }
}
