package org.semanticweb.drew.benchmark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.coode.owlapi.latex.LatexOntologyFormat;
import org.coode.owlapi.latex.LatexOntologyStorer;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.drew.el.profile.SROELProfile;
import org.semanticweb.drew.util.MyLatexRenderer;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLOntologyStorer;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;

import com.google.common.collect.Multimaps;

public class OntologyRendererTest {

	@Ignore
	@Test
	public void testRenderer() throws OWLOntologyCreationException,
			OWLOntologyStorageException, IOException, IllegalArgumentException,
			SecurityException, IllegalAccessException, NoSuchFieldException {
		//String ontologyFile = "benchmark/galen/ontology/full-galen.el.owl";
		
		String ontologyFile = "benchmark/galen/ontology/EL-GALEN.owl";
		String latexFile = ontologyFile.replace(".owl", ".tex");

		OWLOntologyManager manager = org.semanticweb.owlapi.apibinding.OWLManager
				.createOWLOntologyManager();
		OWLOntology ontology;

		MyLatexRenderer renderer = new MyLatexRenderer(manager);
		ontology = manager.loadOntologyFromOntologyDocument(new File(
				ontologyFile));

		System.out.print("Logical Axioms : " + ontology.getLogicalAxiomCount()
				+ "  ");

		System.out.print("individuls : "
				+ ontology.getIndividualsInSignature().size() + "  ");

		System.out.print("classes : " + ontology.getClassesInSignature().size()
				+ "  ");

		System.out.print("object properties : "
				+ ontology.getObjectPropertiesInSignature().size() + "  ");

		System.out.print("data propterties : "
				+ ontology.getDataPropertiesInSignature().size() + "  ");

		SROELProfile profile = new SROELProfile();
		OWLProfileReport report = profile.checkOntology(ontology);

		String reportFile = ontologyFile + ".txt";

		FileWriter writer = new FileWriter(reportFile);

		writer.append(report.toString());

		writer.close();

		List<OWLProfileViolation> violations = report.getViolations();
		Map<AxiomType<?>, Integer> count = new HashMap<>();

		for (OWLProfileViolation v : violations) {
			AxiomType<?> type = v.getAxiom().getAxiomType();
			if (count.containsKey(type)) {
				count.put(type, count.get(type) + 1);
			} else {
				count.put(type, 1);
			}
		}

		for (Entry<AxiomType<?>, Integer> t : count.entrySet()) {
			System.out.println(t.getKey() + " -> " + t.getValue());
		}

		// OWLOntology elOntology = profile.extract(ontology, report);
		// manager.saveOntology(
		// elOntology,
		// new RDFXMLOntologyFormat(),
		// new FileOutputStream(new File(ontologyFile.replace(".owl",
		// ".el.owl"))));

		// System.out.println(report);

		// renderer.render(ontology, new FileWriter(new File(latexFile)));

		// storer.storeOntology(manager, ontology, target, new
		// LatexOntologyFormat());

	}
}
