package org.semanticweb.drew.benchmark;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class PaperReviewBenchmarkGenerator {

	String baseIRI = "http://www.semanticweb.org/ontologies/2011/7/publication.owl";

	Random rand = new Random();

	int nAuthors = 100;
	int nOrganizations = 20;
	private int nAreas = 20;
	private int nKeywords = 80;
	private int nArticleInProceedings = 200;

	private List<OWLIndividual> authors = new ArrayList<OWLIndividual>();
	private List<OWLIndividual> organizations = new ArrayList<OWLIndividual>();

	private List<String> firstnames = new ArrayList<String>();
	private List<String> lastnames = new ArrayList<String>();
	private OWLClass personClass;
	private OWLClass keywordClass;

	private OWLOntology ontology;

	private OWLDataFactory factory;

	private OWLOntologyManager manager;

	private OWLDataProperty firstnameProperty;
	private OWLDataProperty lastnameProperty;

	private OWLObjectProperty hasAffliationProperty;

	private OWLClass orgnizationClass;

	private OWLClass areaClass;

	private OWLClass articleInProceedingsClass;

	private List<OWLIndividual> areas = new ArrayList<OWLIndividual>();

	private List<OWLIndividual> keywords = new ArrayList<OWLIndividual>();

	private OWLObjectPropertyExpression hasMemberProperty;

	private OWLDataPropertyExpression hasTitleProperty;

	private OWLObjectPropertyExpression hasAuthorProperty;

	private OWLObjectPropertyExpression hasKeywordProperty;

	public PaperReviewBenchmarkGenerator() {
		rand.setSeed(12345678L);
	}

	public OWLOntology generate() {

		try {
			manager = OWLManager.createOWLOntologyManager();
			URL resource = this.getClass().getResource("publication-tbox.owl");
			final OWLOntology schemaOntology = manager.loadOntology(IRI.create(resource));
			Set<OWLAxiom> tBoxAxioms = schemaOntology.getTBoxAxioms(false);
			Set<OWLAxiom> rBoxAxioms = schemaOntology.getRBoxAxioms(false);

			manager.removeOntology(schemaOntology);
			ontology = manager.createOntology(IRI.create(baseIRI));
			manager.addAxioms(ontology, tBoxAxioms);
			manager.addAxioms(ontology, rBoxAxioms);
			factory = manager.getOWLDataFactory();

			personClass = factory.getOWLClass(IRI.create(baseIRI + "#Person"));
			orgnizationClass = factory.getOWLClass(IRI.create(baseIRI + "#Organization"));
			keywordClass = factory.getOWLClass(IRI.create(baseIRI + "#Keyword"));
			areaClass = factory.getOWLClass(IRI.create(baseIRI + "#Area"));
			firstnameProperty = factory.getOWLDataProperty(IRI.create(baseIRI + "#firstname"));
			lastnameProperty = factory.getOWLDataProperty(IRI.create(baseIRI + "#lastname"));
			hasAffliationProperty = factory.getOWLObjectProperty(IRI.create(baseIRI + "#hasAfflication"));
			hasMemberProperty = factory.getOWLObjectProperty(IRI.create(baseIRI + "#hasMember"));
			articleInProceedingsClass = factory.getOWLClass(IRI.create(baseIRI + "#Article_in_Proceedings"));
			hasTitleProperty = factory.getOWLDataProperty(IRI.create(baseIRI + "#hasTitle"));
			hasAuthorProperty = factory.getOWLObjectProperty(IRI.create(baseIRI + "#hasAuthor"));
			hasKeywordProperty = factory.getOWLObjectProperty(IRI.create(baseIRI + "#hasKeyword"));

			generateAuthors();
			generateAffliations();
			generateAuthorHasAffliations();
			generateAreas();
			generateKeywords();
			generateHasMembers();
			generateArticlesInProcedings();
			manager.saveOntology(ontology, IRI.create(new File("benchmark/publication.owl")));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

		return null;

	}

	private void generateKeywords() {
		for (int i = 0; i < nKeywords; i++) {
			final OWLNamedIndividual keyword = factory.getOWLNamedIndividual(IRI.create(baseIRI + "#Keyword_" + i));
			keywords.add(keyword);
			manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(keyword));
			manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(keywordClass, keyword));
		}
	}

	private void generateAreas() {
		for (int i = 0; i < nAreas; i++) {
			final OWLNamedIndividual area = factory.getOWLNamedIndividual(IRI.create(baseIRI + "#Area_" + i));
			areas.add(area);
			manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(area));
			manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(areaClass, area));
		}

	}

	private void generateAuthors() {
		for (int i = 0; i < nAuthors; i++) {
			firstnames.add("FirstName" + i);
			lastnames.add("LastName" + i);

			final OWLNamedIndividual author = factory.getOWLNamedIndividual(IRI.create(baseIRI + "#Person_"
					+ firstnames.get(i) + "_" + lastnames.get(i)));
			authors.add(author);
			manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(author));
			manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(personClass, author));
			manager.addAxiom(ontology,
					factory.getOWLDataPropertyAssertionAxiom(firstnameProperty, author, firstnames.get(i)));
			manager.addAxiom(ontology,
					factory.getOWLDataPropertyAssertionAxiom(lastnameProperty, author, lastnames.get(i)));

		}
	}

	private void generateAffliations() {
		for (int i = 0; i < nOrganizations; i++) {
			final OWLNamedIndividual orgnization = factory.getOWLNamedIndividual(IRI.create(baseIRI + "#Organization_"
					+ i));
			organizations.add(orgnization);
			manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(orgnization));
			manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(orgnizationClass, orgnization));
		}
	}

	private void generateAuthorHasAffliations() {
		for (int i = 0; i < nAuthors; i++) {
			OWLIndividual author = authors.get(i);
			int id = rand.nextInt(nOrganizations);
			OWLIndividual org = organizations.get(id);
			manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(hasAffliationProperty, author, org));
		}
	}

	private void generateHasMembers() {
		int currentKeywordId = 0;

		for (int i = 0; i < nAreas; i++) {
			OWLIndividual area = areas.get(i);

			// every area has 3~6 keywords
			int n = rand.nextInt(4) + 3;

			for (int j = 0; j < n; j++) {
				// int id = rand.nextInt(nKeywords);
				OWLIndividual keyword = keywords.get((currentKeywordId++) % nKeywords);
				manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(hasMemberProperty, area, keyword));
			}
		}
	}

	private void generateArticlesInProcedings() {
		for (int i = 0; i < nArticleInProceedings; i++) {
			final OWLNamedIndividual article = factory.getOWLNamedIndividual(IRI.create(baseIRI
					+ "#Article_In_Proceedings_" + i));
			manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(articleInProceedingsClass, article));

			manager.addAxiom(ontology,
					factory.getOWLDataPropertyAssertionAxiom(hasTitleProperty, article, "Article_In_Proceedings_" + i));

			// every paper has 1~4 authors
			int nAuthorsForThis = rand.nextInt(4) + 1;
			for (int j = 0; j < nAuthorsForThis; j++) {
				OWLIndividual author = authors.get(rand.nextInt(nAuthors));
				manager.addAxiom(ontology,
						factory.getOWLObjectPropertyAssertionAxiom(hasAuthorProperty, article, author));
			}

			// every paper has 2~5 keywords
			int nKeywordsForThis = rand.nextInt(4) + 2;
			for (int j = 0; j < nKeywordsForThis; j++) {
				OWLIndividual keyword = keywords.get(rand.nextInt(nKeywords));
				manager.addAxiom(ontology,
						factory.getOWLObjectPropertyAssertionAxiom(hasKeywordProperty, article, keyword));
			}
		}
	}

}
