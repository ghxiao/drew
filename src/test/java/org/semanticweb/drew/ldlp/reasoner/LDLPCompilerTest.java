/*
 * @(#)LDLPCompilerTest.java 2010-3-19 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.ldlp.reasoner;

import java.util.List;

import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.ldlp.reasoner.LDLPOntologyCompiler;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;


public class LDLPCompilerTest {
	private final static String uri = "http://www.kr.tuwien.ac.at/staff/xiao/ldl/role_inverse.ldl";

	private final static String phyUri = "file:kb/role_inverse.ldl";

	private static OWLOntologyManager manager = OWLManager
			.createOWLOntologyManager();

	public static void main(String[] args) {
		loadOntology(uri, phyUri);
	}

	private static void loadOntology(String uri, String phyUri) {
		OWLOntology ontology;

		System.out.println();
		System.out
				.println("------------------------------------------------------");

		System.out.println("Reading file " + uri + "...");
		manager.addIRIMapper(new SimpleIRIMapper(IRI.create(uri), IRI
				.create(phyUri)));

		try {
			ontology = manager.loadOntology(IRI.create(uri));

			for (OWLAxiom axiom : ontology.getAxioms()) {
				System.out.println(axiom);
			}

			System.out.println(ontology);

			System.out
					.println("------------------------------------------------------");

			System.out.println("Compiled:");

			LDLPOntologyCompiler compiler = new LDLPOntologyCompiler();
			final List<ProgramStatement> datalogClauses = compiler.compile(ontology);

			for (ProgramStatement clause : datalogClauses) {
				System.out.println(clause);
			}
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}
}
