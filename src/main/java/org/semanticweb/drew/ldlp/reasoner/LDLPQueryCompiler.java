package org.semanticweb.drew.ldlp.reasoner;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Constant;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.dlprogram.model.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LDLPQueryCompiler {

	final static Logger logger = LoggerFactory
			.getLogger(LDLPQueryCompiler.class);

	public Clause compileQuery(Clause query) {

		Clause newQuery = new Clause();

		Literal head = query.getHead();
		Literal newHead = compileHeadLiteral(head);
		newQuery.setHead(newHead);

		for (Literal bodyLiteral : query.getPositiveBody()) {
			Literal newBodyLiteral = compileLiteral(bodyLiteral);
			newQuery.getPositiveBody().add(newBodyLiteral);
		}

		logger.debug("{}\n   ->\n{}", query, newQuery);
		return newQuery;
	}

	// keep the head predicate
	public Literal compileHeadLiteral(Literal literal) {
		NormalPredicate normalLiteral = (NormalPredicate) literal
				.getPredicate();

		List<Term> terms = literal.getTerms();
		List<Term> newTerms = compileTerms(terms);

		Literal newLiteral = new Literal(normalLiteral, newTerms);
		return newLiteral;
	}
	
	public Literal compileLiteral(Literal literal) {
		NormalPredicate normalLiteral = (NormalPredicate) literal
				.getPredicate();
		NormalPredicate newHeadPredicate = compileNormalPredicate(normalLiteral);

		List<Term> terms = literal.getTerms();
		List<Term> newTerms = compileTerms(terms);

		Literal newLiteral = new Literal(newHeadPredicate, newTerms);
		return newLiteral;
	}

	private NormalPredicate compileNormalPredicate(NormalPredicate normalLiteral) {
		String name = normalLiteral.getName();

		String newName = LDLPCompilerManager.getInstance().getPredicate(name);

		String newHeadPredicateName = newName;
		NormalPredicate newHeadPredicate = CacheManager.getInstance()
				.getPredicate(newHeadPredicateName, normalLiteral.getArity());
		return newHeadPredicate;
	}

	private List<Term> compileTerms(final List<Term> terms) {
		List<Term> newHeadTerms = new ArrayList<>();
		for (Term t : terms) {
			if (t instanceof Variable) {
				Variable var = (Variable) t;
				Variable newVar = compileVariable(var);
				newHeadTerms.add(newVar);
			} else if (t instanceof Constant) {
				Constant constant = (Constant) t;
				Constant newConstant = compileConstant(constant);
				newHeadTerms.add(newConstant);
			}
		}
		return newHeadTerms;
	}

	private Variable compileVariable(Variable var) {
		Variable newVar = var;
		return newVar;
	}

	private Constant compileConstant(Constant constant) {
		String newConstantName = LDLPCompilerManager.getInstance().getConstant(
				constant.getName());

		Constant newConstant = CacheManager.getInstance().getConstant(newConstantName);
		
		return newConstant;
	}
}
