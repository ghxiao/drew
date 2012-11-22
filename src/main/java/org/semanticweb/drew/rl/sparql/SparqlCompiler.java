package org.semanticweb.drew.rl.sparql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.sparql.core.TriplePath;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.syntax.Element;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;

public class SparqlCompiler {

	public SparqlCompiler() {

	}

	/**
	 * @param query
	 */
	public Clause compileQuery(Query query) {

		List<Literal> body = new ArrayList<Literal>();
		List<Term> ansVars = new ArrayList<Term>();
		List<Var> projectVars = query.getProjectVars();
		for (Var var : projectVars) {
			ansVars.add(compileTerm(var));
		}

		NormalPredicate ans = CacheManager.getInstance().getPredicate("ans",
				ansVars.size());

		Literal head = new Literal(ans, ansVars);

		Element queryPattern = query.getQueryPattern();

		if (queryPattern instanceof ElementGroup) {
			ElementGroup group = (ElementGroup) queryPattern;
			List<Element> elements = group.getElements();
			for (Element ele : elements) {
				// System.out.println(ele.getClass());
				if (ele instanceof ElementPathBlock) {
					ElementPathBlock block = (ElementPathBlock) ele;
					Iterator<TriplePath> patternElts = block.patternElts();
					while (patternElts.hasNext()) {
						TriplePath triplePath = patternElts.next();
						Triple triple = triplePath.asTriple();
						final Literal lit = complileTriple(triple);
						//System.out.println(triple + " ==> " + lit);
						body.add(lit);
					}
					return new Clause(head, body);
				} else {
					throw new UnsupportedOperationException();
				}

			}

		} else {
			throw new UnsupportedOperationException();
		}
		return null;
	}

	private Literal complileTriple(Triple triple) {

		if (triple.getPredicate().getURI()
				.equals(OWLRDFVocabulary.RDF_TYPE.toString())) {
			return new Literal(compilePredicate(triple.getObject(), 1), //
					compileTerm(triple.getSubject()));
		} else {
			return new Literal(compilePredicate(triple.getPredicate(), 2), //
					compileTerm(triple.getSubject()), //
					compileTerm(triple.getObject()));
		}

	}

	/**
	 * @param predicate
	 * @return
	 */
	private NormalPredicate compilePredicate(Node predicate, int arity) {

		if (predicate.isURI()) {
			String uri = predicate.getURI();
			return CacheManager.getInstance().getPredicate(quote(uri), arity);
		} else {
			throw new IllegalArgumentException();
		}
	}

	private Term compileTerm(Node node) {
		if (node.isURI()) {
			String uri = node.getURI();
			return CacheManager.getInstance().getConstant(quote(uri));
		} else if (node.isLiteral()) {
			String uri = node.getLiteralDatatypeURI();
			if (uri != null)
				return CacheManager.getInstance().getConstant(quote(uri));
			else
				return CacheManager.getInstance().getConstant(node.toString());
		} else if (node.isVariable()) {
			String name = node.getName();
			return CacheManager.getInstance().getVariable(name);
		} else {
			throw new IllegalArgumentException(node.toString());
		}
	}

	/**
	 * @param uri
	 * @return
	 */
	private String quote(String uri) {
		final String quotedIRI = "<" + uri + ">";
		return quotedIRI;
	}

}
