package org.semanticweb.drew.hermit;

import java.io.File;


import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.Reasoner;
import com.google.common.base.Stopwatch;

public class PelletEvaluation {

	/**
	 * @param args
	 * @throws OWLOntologyCreationException
	 */
	public static void main(String[] args) throws OWLOntologyCreationException {
		Stopwatch sw = new Stopwatch();
		sw.start();
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		OWLOntology o = m.loadOntologyFromOntologyDocument(new File(
				"benchmark/galen/ontology/full-galen.el.owl"));
		
		OWLReasoner pellet = new PelletReasoner(o, BufferingMode.BUFFERING);
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass human = factory.getOWLClass(IRI
				.create("http://www.co-ode.org/ontologies/galen#Human"));
		NodeSet<OWLClass> superClasses = pellet.getSuperClasses(human, false);
		for (Node<OWLClass> cls : superClasses) {
			System.out.println(cls);
		}
		sw.stop();
		System.out.println(sw.elapsedMillis());
	}
}
