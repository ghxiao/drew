package org.semanticweb.drew.rl.sparql;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.format.DLProgramStorer;
import org.semanticweb.drew.dlprogram.format.DLProgramStorerImpl;
import org.semanticweb.drew.dlprogram.format.RLProgramStorerImpl;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.el.reasoner.DReWELManager;
import org.semanticweb.drew.el.reasoner.NamingStrategy;
import org.semanticweb.drew.ldlp.reasoner.LDLPQueryCompiler;
import org.semanticweb.drew.rl.sparql.SparqlCompiler;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.sparql.expr.ExprAggregator;

public class SparqlCompilerTest {

	@Test
	public void test() {
		String prefixRDF = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String queryText = prefixRDF + "PREFIX : <#>" + "SELECT ?X ?Y "
				+ "WHERE {"
				+ "?X rdf:type :preOnt1____ORDERS_LineItemQuantity ."
				+ "?X :preOnt1____hasValue ?Y " + "}";

		Query query = QueryFactory.create(queryText, Syntax.syntaxARQ);
		SparqlCompiler sparqlCompiler = new SparqlCompiler();

		Clause drewQuery = sparqlCompiler.compileQuery(query);
		LDLPQueryCompiler queryCompiler = new LDLPQueryCompiler();
		Clause drewRLQuery = queryCompiler.compileQuery(drewQuery);
		DLProgramStorer storer = new RLProgramStorerImpl();
		storer.store(drewQuery, System.out);
		storer.store(drewRLQuery, System.out);
	}

	@Test
	public void testAggregation() {
		String queryText = "PREFIX ex: <http://example.org/> SELECT (Count(?Item) AS ?C) WHERE { ?Item ex:price ?Pr }";
		
		Query query = QueryFactory.create(queryText, Syntax.syntaxARQ);
		
		List<ExprAggregator> aggregators = query.getAggregators();
		
		for(ExprAggregator agg: aggregators){
			System.out.println(agg);
		}
		
		SparqlCompiler sparqlCompiler = new SparqlCompiler();
		Clause drewQuery = sparqlCompiler.compileQuery(query);
		LDLPQueryCompiler queryCompiler = new LDLPQueryCompiler();
		Clause drewRLQuery = queryCompiler.compileQuery(drewQuery);
		DLProgramStorer storer = new RLProgramStorerImpl();
		storer.store(drewQuery, System.out);
		storer.store(drewRLQuery, System.out);
	}

}
