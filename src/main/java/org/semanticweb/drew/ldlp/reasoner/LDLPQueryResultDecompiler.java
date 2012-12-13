package org.semanticweb.drew.ldlp.reasoner;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Constant;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.Predicate;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.dlprogram.model.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LDLPQueryResultDecompiler {
	
	final static Logger logger = LoggerFactory
	.getLogger(LDLPQueryResultDecompiler.class);
	
	public List<Literal> decompileLiterals(List<Literal> lits) {
		List<Literal> newLits = new ArrayList<>();
		for(Literal lit:lits){
			Literal newLit = decompileLiteral(lit);
			newLits.add(newLit);
		}
		return newLits;

	}

	public Literal decompileLiteral(Literal lit) {
		Predicate predicate = lit.getPredicate();
		Predicate decompiledPredicate = decompilePredicate(predicate);
		
		List<Term> terms = lit.getTerms();
		
		List<Term> decompiledTerms = decompileTerms(terms);
		
		return new Literal(decompiledPredicate, decompiledTerms);
	}

	public	List<Term> decompileTerms(List<Term> terms) {

		List<Term> decompiledTerms = new ArrayList<>();
		
		for(Term term: terms){
			Term decompiledTerm = decompileTerm(term);
			decompiledTerms.add(decompiledTerm);
		}
		
		return decompiledTerms;
	}

	public Term decompileTerm(Term term) {
		if(term instanceof Variable){
			throw new IllegalStateException("Query Results should not contain Variable");
		}else if(term instanceof Constant){
			Constant constant = (Constant)term;
			Constant decomiledConstant = decompileConstant(constant);
			return decomiledConstant;
		}
		
		throw new IllegalStateException();
	}

	public Constant decompileConstant(Constant constant) {
		String name = constant.getName();
		
		String decompiledConstantName = LDLPCompilerManager.getInstance().decompile(name);
		
		Constant decompiledConstant = CacheManager.getInstance().getConstant(decompiledConstantName);
		
		return decompiledConstant;
	}

	public Predicate decompilePredicate(Predicate predicate) {
		NormalPredicate normalPredicate = (NormalPredicate)predicate;
		String name = normalPredicate.getName();
		int arity = normalPredicate.getArity();
		
		String decompiledName = LDLPCompilerManager.getInstance().decompile(name);
		
		NormalPredicate decompiledPredicate = CacheManager.getInstance().getPredicate(decompiledName, arity);
		return decompiledPredicate;
	}
}
