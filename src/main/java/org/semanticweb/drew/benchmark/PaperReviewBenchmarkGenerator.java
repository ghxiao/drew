package org.semanticweb.drew.benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.semanticweb.drew.dlprogram.format.DLProgramStorerImpl;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
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

	private String baseIRI = "http://www.semanticweb.org/ontologies/2011/7/publication.owl";

	private Random rand = new Random();

	private int nAuthors = 100;
	private int nOrganizations = 20;
	private int nAreas = 20;
	private int nKeywords = 80;
	private int nArticleInProceedings = 200;

	private int nSubmissions = 20;

	private List<OWLIndividual> authors = new ArrayList<>();
	private List<OWLIndividual> organizations = new ArrayList<>();

	private List<String> firstnames = new ArrayList<>();
	private List<String> lastnames = new ArrayList<>();
	private OWLClass personClass;
	private OWLClass keywordClass;

	private OWLDataFactory factory;
	private OWLOntologyManager manager;
	private OWLOntology ontology;

	private OWLDataProperty firstnameProperty;
	private OWLDataProperty lastnameProperty;
	private OWLObjectProperty hasAffliationProperty;
	private OWLClass orgnizationClass;
	private OWLClass areaClass;
	private OWLClass articleInProceedingsClass;

	private List<OWLIndividual> areas = new ArrayList<>();

	private List<OWLIndividual> keywords = new ArrayList<>();

	private OWLObjectProperty hasMemberProperty;

	private OWLObjectProperty hasAuthorProperty;

	private OWLObjectProperty hasKeywordProperty;

	private OWLDataProperty hasTitleProperty;

	private OWLClass refereeClass;

	private String rootPath;

	public PaperReviewBenchmarkGenerator() {
		rand.setSeed(12345678L);
	}

	private String elpRules;

	private static String dlpRules;

	public void generate(String rootPath) {
		generate(rootPath, 1);
	}

	void generate(String rootPath, int n) {

		this.rootPath = rootPath;

		nAuthors = 100 * n;
		nOrganizations = 20 * n;
		nAreas = 20 * n;
		nKeywords = 80 * n;
		nArticleInProceedings = 200 * n;
		nSubmissions = 20 * n;
		generateOntology(n);
		try {
			generateDLProgram(n);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void generateDLProgram(int n) throws IOException {
		elpRules = "";
		BufferedReader reader = new BufferedReader(new FileReader(this
				.getClass().getResource("review.elp").getFile()));
		String line;
		while ((line = reader.readLine()) != null) {
			elpRules += line + "\n";
		}
		reader.close();

		StringReader r = new StringReader(elpRules);
		DLProgramParser parser = new DLProgramParser(r);
		try {
			DLProgram program = parser.program();
			// DatalogToStringHelper helper = new DatalogToStringHelper();
			DLProgramStorerImpl helper = new DLProgramStorerImpl();
			helper.setUsingDlvhexFormat(true);
			// FIXME:
			//dlpRules = helper.toString(program);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < nSubmissions; i++) {
			sb.append(String.format("paper(\"<%s#Submission_%d>\").\n",
					baseIRI, i));
			int nKeywordsForThis = rand.nextInt(2) + 2;
			for (int j = 0; j < nKeywordsForThis; j++) {
				sb.append(String.format(
						"kw(\"<%s#Submission_%d>\", \"<%s#Keyword_%d>\").\n",
						baseIRI, i, baseIRI, rand.nextInt(nKeywords)));
			}
		}

		try {
			FileWriter writer = new FileWriter(rootPath + "/reviewers-" + n
					+ ".dlp");
			writer.write(dlpRules);
			writer.write(sb.toString());
			writer.close();

			writer = new FileWriter(rootPath + "/reviewers-" + n + ".elp");
			writer.write(elpRules);
			writer.write(sb.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void generateOntology(int n) {

		try {
			manager = OWLManager.createOWLOntologyManager();
			URL resource = this.getClass().getResource("publication-tbox.owl");
			final OWLOntology schemaOntology = manager.loadOntology(IRI
					.create(resource));
			Set<OWLAxiom> tBoxAxioms = schemaOntology.getTBoxAxioms(false);
			Set<OWLAxiom> rBoxAxioms = schemaOntology.getRBoxAxioms(false);

			manager.removeOntology(schemaOntology);
			ontology = manager.createOntology(IRI.create(baseIRI));
			manager.addAxioms(ontology, tBoxAxioms);
			manager.addAxioms(ontology, rBoxAxioms);
			factory = manager.getOWLDataFactory();

			personClass = factory.getOWLClass(IRI.create(baseIRI + "#Person"));
			refereeClass = factory
					.getOWLClass(IRI.create(baseIRI + "#Referee"));
			orgnizationClass = factory.getOWLClass(IRI.create(baseIRI
					+ "#Organization"));
			keywordClass = factory
					.getOWLClass(IRI.create(baseIRI + "#Keyword"));
			areaClass = factory.getOWLClass(IRI.create(baseIRI + "#Area"));
			firstnameProperty = factory.getOWLDataProperty(IRI.create(baseIRI
					+ "#firstname"));
			lastnameProperty = factory.getOWLDataProperty(IRI.create(baseIRI
					+ "#lastname"));
			hasAffliationProperty = factory.getOWLObjectProperty(IRI
					.create(baseIRI + "#hasAfflication"));
			hasMemberProperty = factory.getOWLObjectProperty(IRI.create(baseIRI
					+ "#hasMember"));
			articleInProceedingsClass = factory.getOWLClass(IRI.create(baseIRI
					+ "#Article_in_Proceedings"));
			hasTitleProperty = factory.getOWLDataProperty(IRI.create(baseIRI
					+ "#hasTitle"));
			hasAuthorProperty = factory.getOWLObjectProperty(IRI.create(baseIRI
					+ "#hasAuthor"));
			hasKeywordProperty = factory.getOWLObjectProperty(IRI
					.create(baseIRI + "#hasKeyword"));

			generateAuthors();
			generateAffliations();
			generateAuthorHasAffliations();
			generateAreas();
			generateKeywords();
			generateHasMembers();
			generateArticlesInProcedings();
			manager.saveOntology(
					ontology,
					IRI.create(new File("benchmark/review/publication-" + n
							+ ".owl")));
		} catch (OWLOntologyCreationException | OWLOntologyStorageException | URISyntaxException e) {
			e.printStackTrace();
		}

    }

	private void generateKeywords() {
		for (int i = 0; i < nKeywords; i++) {
			final OWLNamedIndividual keyword = factory
					.getOWLNamedIndividual(IRI
							.create(baseIRI + "#Keyword_" + i));
			keywords.add(keyword);
			manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(keyword));
			manager.addAxiom(ontology,
					factory.getOWLClassAssertionAxiom(keywordClass, keyword));
		}
	}

	private void generateAreas() {
		for (int i = 0; i < nAreas; i++) {
			final OWLNamedIndividual area = factory.getOWLNamedIndividual(IRI
					.create(baseIRI + "#Area_" + i));
			areas.add(area);
			manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(area));
			manager.addAxiom(ontology,
					factory.getOWLClassAssertionAxiom(areaClass, area));
		}

	}

	private void generateAuthors() {
		for (int i = 0; i < nAuthors; i++) {
			firstnames.add("FirstName" + i);
			lastnames.add("LastName" + i);

			final OWLNamedIndividual author = factory.getOWLNamedIndividual(IRI
					.create(baseIRI + "#Person_" + firstnames.get(i) + "_"
							+ lastnames.get(i)));
			authors.add(author);
			manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(author));
			manager.addAxiom(ontology,
					factory.getOWLClassAssertionAxiom(personClass, author));
			manager.addAxiom(ontology,
					factory.getOWLClassAssertionAxiom(refereeClass, author));

			manager.addAxiom(ontology, factory
					.getOWLDataPropertyAssertionAxiom(firstnameProperty,
							author, firstnames.get(i)));
			manager.addAxiom(ontology, factory
					.getOWLDataPropertyAssertionAxiom(lastnameProperty, author,
							lastnames.get(i)));
		}
	}

	private void generateAffliations() {
		for (int i = 0; i < nOrganizations; i++) {
			final OWLNamedIndividual orgnization = factory
					.getOWLNamedIndividual(IRI.create(baseIRI
							+ "#Organization_" + i));
			organizations.add(orgnization);
			manager.addAxiom(ontology,
					factory.getOWLDeclarationAxiom(orgnization));
			manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(
					orgnizationClass, orgnization));
		}
	}

	private void generateAuthorHasAffliations() {
		for (int i = 0; i < nAuthors; i++) {
			OWLIndividual author = authors.get(i);
			int id = rand.nextInt(nOrganizations);
			OWLIndividual org = organizations.get(id);
			manager.addAxiom(ontology, factory
					.getOWLObjectPropertyAssertionAxiom(hasAffliationProperty,
							author, org));
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
				OWLIndividual keyword = keywords.get((currentKeywordId++)
						% nKeywords);
				manager.addAxiom(ontology, factory
						.getOWLObjectPropertyAssertionAxiom(hasMemberProperty,
								area, keyword));
			}
		}
	}

	private void generateArticlesInProcedings() {
		for (int i = 0; i < nArticleInProceedings; i++) {
			final OWLNamedIndividual article = factory
					.getOWLNamedIndividual(IRI.create(baseIRI
							+ "#Article_In_Proceedings_" + i));
			manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(
					articleInProceedingsClass, article));

			manager.addAxiom(ontology, factory
					.getOWLDataPropertyAssertionAxiom(hasTitleProperty,
							article, "Article_In_Proceedings_" + i));

			// every paper has 1~4 authors
			int nAuthorsForThis = rand.nextInt(4) + 1;
			for (int j = 0; j < nAuthorsForThis; j++) {
				OWLIndividual author = authors.get(rand.nextInt(nAuthors));
				manager.addAxiom(ontology, factory
						.getOWLObjectPropertyAssertionAxiom(hasAuthorProperty,
								article, author));
			}

			// every paper has 2~5 keywords
			int nKeywordsForThis = rand.nextInt(4) + 2;
			for (int j = 0; j < nKeywordsForThis; j++) {
				OWLIndividual keyword = keywords.get(rand.nextInt(nKeywords));
				manager.addAxiom(ontology, factory
						.getOWLObjectPropertyAssertionAxiom(hasKeywordProperty,
								article, keyword));
			}
		}
	}

	public static void main(String[] args) {
		PaperReviewBenchmarkGenerator g = new PaperReviewBenchmarkGenerator();
		for (int i = 1; i <= 3; i++) {
			g.generate("benchmark/review", i);
		}
	}
}
