package org.semanticweb.drew.default_logic.rewritter;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.drew.default_logic.DefaultRule;
import org.semanticweb.drew.default_logic.OWLPredicate;
import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLogicalEntity;

public class DefaultRuleRewriter {
	static final String POSTFIX_IN = "_in";
	static final String POSTFIX_OUT = "_out";

	NormalPredicate in = CacheManager.getInstance().getPredicate("in", 2); 
	NormalPredicate out = CacheManager.getInstance().getPredicate("out", 2); 
	NormalPredicate im = CacheManager.getInstance().getPredicate("im", 2);
	
	
	// FIXME: in general, for a default ([pre : just1, ..., justm] / [concl] ),
	// every pre/just_i, concl can be a conjunction of literals. For simplicity,
	// we only consider the case that each conjunction only has one literal
	List<Clause> rewrite(DefaultRule df) {
		List<Clause> result = new ArrayList<Clause>();
		Literal pre = df.getPrerequisite().get(0);
		Literal conc = df.getConclusion().get(0);

		final OWLPredicate concPredicate = conc.getPredicate().asOWLPredicate();
		
		//in(X, Y) :- not out(X, Y), nom(X), concl(Y), q(X)
		
		
		OWLPredicate concInPredicate = addPostfix(concPredicate, POSTFIX_IN);
		OWLPredicate concOutPredicate = addPostfix(concPredicate, POSTFIX_OUT);
		Literal concIn = new Literal(concInPredicate, conc.getTerms());
		Literal concOut = new Literal(concOutPredicate, conc.getTerms());

		Clause choiceRule1 = new Clause();
		choiceRule1.setHead(concIn);
//		choiceRule1.getPositiveBody().add;
		choiceRule1.getNegativeBody().add(concOut);
		result.add(choiceRule1);
		
		Clause choiceRule2 = new Clause();
		choiceRule1.setHead(concOut);
//		choiceRule1.getPositiveBody().add;
		choiceRule1.getNegativeBody().add(concIn);
		result.add(choiceRule2);
		
		
		
		return result;
	}

	protected OWLPredicate addPostfix(OWLPredicate p, String postfix) {
		IRI iri = p.getLogicalEntity().getIRI();
		IRI new_iri = IRI.create(iri.toString() + postfix);
		OWLLogicalEntity e = null;
		if (p.getArity() == 1) {
			e = OWLManager.getOWLDataFactory().getOWLClass(new_iri);
		} else if (p.getArity() == 2) {
			e = OWLManager.getOWLDataFactory().getOWLObjectProperty(new_iri);
		} else {
			throw new IllegalStateException();
		}
		return new OWLPredicate(e);

	}
}
