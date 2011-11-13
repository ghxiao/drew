package org.semanticweb.drew.benchmark;

import java.io.File;
import java.io.IOException;

import org.coode.owlapi.latex.LatexOntologyFormat;
import org.coode.owlapi.latex.LatexOntologyStorer;
import org.junit.Test;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLOntologyStorer;

public class OntologyRendererTest {

	@Test
	public void testRenderer() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
		String ontologyFile = "benchmark/galen/ontology/full-galen.owl";
		// String latexFile = "benchmark/galen/ontology/full-galen.tex";
		String latexFile = ontologyFile.replace(".owl", ".tex");

		OWLOntologyManager manager = org.semanticweb.owlapi.apibinding.OWLManager.createOWLOntologyManager();
		OWLOntology ontology;

		ontology = manager.loadOntologyFromOntologyDocument(new File(ontologyFile));

		OWLOntologyStorer storer = new LatexOntologyStorer();

		OWLOntologyDocumentTarget target = new FileDocumentTarget(new File(latexFile));
		storer.storeOntology(manager, ontology, target, new LatexOntologyFormat());

	}
}
