package org.semanticweb.drew.ldlp.reasoner;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class LoadTest {
	private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	
//	public final static String uri = "http://www.kr.tuwien.ac.at/staff/xiao/ldl/super.ldl";
//
//	public final static String phyUri = "file:kb/super.ldl";

//	public final static String uri = "http://www.kr.tuwien.ac.at/staff/xiao/ldl/role_intersection_and_union.ldl";
//
//	public final static String phyUri = "file:kb/role_intersection_and_union.ldl";

//	public final static String uri = "http://www.kr.tuwien.ac.at/staff/xiao/ldl/role_chain.ldl";
//
//	public final static String phyUri = "file:kb/role_chain.ldl";
//	
//	public final static String uri = "http://www.kr.tuwien.ac.at/staff/xiao/ldl/role_inverse.ldl";

	//public final static String phyUri = "file:kb/role_inverse.ldl";
	
	public final static String uri = "http://www.kr.tuwien.ac.at/staff/xiao/ldl/role_nominal.ldl";

	public final static String phyUri = "file:kb/role_nominal.ldl";

	
	public static void main(String[] args) {
		loadOntology(uri,phyUri);
	}

	private static void loadOntology(String uri,String phyUri) {
		OWLOntology ontology;

		System.out.println();
		System.out.println("------------------------------------------------------");

		System.out.println("Reading file " + uri + "...");
		manager.addIRIMapper(new SimpleIRIMapper(IRI.create(uri), IRI.create(phyUri)));

		try {
			ontology = manager.loadOntology(IRI.create(uri));

			for (OWLAxiom axiom : ontology.getAxioms()) {
				System.out.println(axiom);
			}

			System.out.println(ontology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}
}
