package org.semanticweb.drew.ldlp.reasoner;

import static org.semanticweb.drew.helper.LDLHelper.assert$;
import static org.semanticweb.drew.helper.LDLHelper.cls;
import static org.semanticweb.drew.helper.LDLHelper.ind;
import static org.semanticweb.drew.helper.LDLHelper.prop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.dlprogram.model.Variable;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;

public class LDLPReasonerTest {

	static final Logger logger = LoggerFactory
			.getLogger(LDLPReasonerTest.class);

	@Test
	public void test001() throws OWLOntologyCreationException {
		final OWLClass A = cls("A");
		final OWLClass B = cls("B");
		final OWLObjectProperty P = prop("P");

		final OWLNamedIndividual a = ind("a");
		final OWLNamedIndividual b = ind("b");

		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		axioms.add(assert$(A, a));
		axioms.add(assert$(A, b));
		axioms.add(assert$(B, b));
		axioms.add(assert$(P, a, b));

		System.out.println("Axioms in Ontology");
		for (OWLAxiom axiom : axioms) {
			System.out.println(axiom);
		}

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		OWLOntology ontology = manager.createOntology(axioms);

		LDLPReasoner reasoner = new LDLPReasoner(ontology);

		Clause query = new Clause();

		NormalPredicate ans = CacheManager.getInstance().getPredicate("ans", 1);
		Variable X = CacheManager.getInstance().getVariable("X");
		Variable Y = CacheManager.getInstance().getVariable("Y");

		Literal head = new Literal(ans, Y);

		NormalPredicate pP = CacheManager.getInstance().getPredicate(
				P.toString(), 2);

		// System.out.println(pP);

		NormalPredicate pA = CacheManager.getInstance().getPredicate(
				A.toString(), 1);

		// System.out.println(pA);

		Term cb = CacheManager.getInstance().getConstant(b.toString());

		query.setHead(head);

		Literal body1 = new Literal(pP, X, Y);

		Literal body2 = new Literal(pA, X);

		Literal body3 = new Literal(pA, cb);

		query.getPositiveBody().add(body1);
		query.getPositiveBody().add(body2);
		query.getPositiveBody().add(body3);

		// ans(Y):- P(X,Y), A(X), A(b).

		System.out.println("query is : " + query);

		reasoner.query(query);

		// OWLOntology ontology = OWLOntology
	}

	@Test
	public void testSparqlQuery() throws OWLOntologyCreationException,
			IOException {
		// The ontology loaded as dataset
		InputStream ontStream = LDLPReasonerTest.class
				.getResourceAsStream("/data/university0-0.owl");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(ontStream);

		InputStream queryStream = LDLPReasonerTest.class
				.getResourceAsStream("/data/lubm-query4.sparql");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				queryStream));
		String line;
		String queryText = "";
		while ((line = reader.readLine()) != null) {
			queryText = queryText + line + "\n";
		}

		Query query = QueryFactory.create(queryText, Syntax.syntaxARQ);
		LDLPReasoner reasoner = new LDLPReasoner(ontology);
		List<Literal> results = reasoner.executeQuery(query);
		System.out.println(results.size() + " answers :");
		for (Literal result : results) {
			System.out.println(result);
		}
		LDLPCompilerManager m = LDLPCompilerManager.getInstance();

		// m.dump();

	}
	//
	// @Test
	// public void test002() {
	// final OWLClass B = cls("A");
	// final OWLObjectProperty P = prop("P");
	// System.out.println(B);
	// System.out.println(P);
	// }
}
