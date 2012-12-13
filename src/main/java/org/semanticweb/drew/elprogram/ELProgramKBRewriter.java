/*
 * @(#)DLProgramCompiler.java 2010-4-3 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.elprogram;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.drew.dlprogram.DLProgramKB2DatalogRewriter;
import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Constant;
import org.semanticweb.drew.dlprogram.model.DLAtomPredicate;
import org.semanticweb.drew.dlprogram.model.DLInputOperation;
import org.semanticweb.drew.dlprogram.model.DLInputSignature;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.ProgramComment;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.dlprogram.model.Variable;
import org.semanticweb.drew.el.SymbolEncoder;
import org.semanticweb.drew.el.reasoner.DReWELManager;
import org.semanticweb.drew.el.reasoner.PInst;
import org.semanticweb.drew.el.reasoner.RewritingVocabulary;
import org.semanticweb.drew.el.reasoner.SROEL2DatalogRewriter;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLogicalEntity;
import org.semanticweb.owlapi.model.OWLOntology;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * KBCompiler: compile the el-program KB to a datalog^n program.
 */
public class ELProgramKBRewriter implements DLProgramKB2DatalogRewriter {

	// final static Logger logger =
	// LoggerFactory.getLogger(ELProgramKBCompiler.class);

	Variable X = CacheManager.getInstance().getVariable("X");
	Variable Y = CacheManager.getInstance().getVariable("Y");

	SymbolEncoder<DLInputSignature> dlInputSignatureEncoder;

	private SymbolEncoder<IRI> iriEncoder;

	public ELProgramKBRewriter() {
		dlInputSignatureEncoder = DReWELManager.getInstance()
				.getDlInputSignatureEncoder();
		iriEncoder = DReWELManager.getInstance().getIRIEncoder();
	}

	@Override
	public DLProgram rewrite(DLProgramKB kb) {
		List<ProgramStatement> result = new ArrayList<>();

		final OWLOntology ontology = kb.getOntology();
		SROEL2DatalogRewriter elCompiler = new SROEL2DatalogRewriter();
		final List<ProgramStatement> compiledOntology = elCompiler.rewrite(
				ontology).getStatements();
		final DLProgram program = kb.getProgram();
		final Set<DLInputSignature> dlInputSignatures = program
				.getDLInputSignatures();
		Set<DLAtomPredicate> dlAtomPredicates = program.getDLAtomPredicates();

		DLAtomPredicatesCompiler dlAtomPredicatesCompiler = new DLAtomPredicatesCompiler();

		result.add(new ProgramComment("---P^{ord}--start--- "));
		
		result.addAll(compileProgram(program));
		result.add(new ProgramComment("---P^{ord}--end--- "));
		result.add(new ProgramComment(""));
		
		
		result.addAll(dlAtomPredicatesCompiler.compile(dlAtomPredicates));
		List<ProgramStatement> pInst = PInst.getPInst();

		for (DLInputSignature signature : dlInputSignatures) {
			result.add(new ProgramComment("-- Phi(L) with input signature [" + signature.toString() + "] --start -- "));
			
			result.addAll(subscript(pInst, signature));
			result.addAll(subscript(compiledOntology, signature));
			result.add(new ProgramComment("-- Phi(L) with input signature [" + signature.toString() + "] --end-- "));
			result.add(new ProgramComment(""));
		}


//		for (DLInputSignature signature : dlInputSignatures) {
//			result.add(new ProgramComment(signature.toString()));
//		}

		
		result.add(new ProgramComment("--rho(Lambda)--start"));
		for (DLInputSignature signature : dlInputSignatures) {
			result.add(new ProgramComment(signature.toString()));
			result.addAll(compileSignature(signature));
		}
		result.add(new ProgramComment("--rho(Lambda)--end"));
		// result.addAll(PInstStar.getPInstStar());

		DLProgram resultProgram = new DLProgram();
		resultProgram.addAll(result);

		return resultProgram;
	}

	/**
	 * P -> P^{o} replace all the DLAtom with a ordinary atom
	 * 
	 */
	public List<ProgramStatement> compileProgram(DLProgram program) {

		List<ProgramStatement> result = new ArrayList<>();

		for (ProgramStatement ps : program.getStatements()) {
			if (ps instanceof Clause) {
				Clause clause = (Clause)ps;
				Clause newClause = compileClause(clause);

				result.add(newClause);
			}else{
				result.add(ps);
			}
		}

		return result;
	}

	public Clause compileClause(Clause clause) {
		Clause newClause = new Clause();
		Literal head = clause.getHead();
		Literal newHead = compileNormalLiteral(head);
		newClause.setHead(newHead);

		for (Literal lit : clause.getNormalPositiveBody()) {
			Literal newLit = compileNormalLiteral(lit);
			newClause.getPositiveBody().add(newLit);
		}

		for (Literal lit : clause.getNormalNegativeBody()) {
			Literal newLit = compileNormalLiteral(lit);
			newClause.getNegativeBody().add(newLit);
		}

		for (Literal lit : clause.getPositiveDLAtoms()) {
			Literal newLit = compileDLAtom(lit);
			newClause.getPositiveBody().add(newLit);
		}

		for (Literal lit : clause.getNegativeDLAtoms()) {
			Literal newLit = compileDLAtom(lit);
			newClause.getNegativeBody().add(newLit);
		}

		// logger.debug("{}\n   -> \n{}", clause, newClause);

		return newClause;
	}

	public Literal compileNormalLiteral(Literal lit) {
		List<Term> terms = lit.getTerms();
		List<Term> newTerms = compileTerms(terms);
		Literal newLit = new Literal(lit.getPredicate(), newTerms);
		return newLit;
	}

	public Literal compileDLAtom(Literal lit) {
		DLAtomPredicate p = (DLAtomPredicate) (lit.getPredicate());
		DLInputSignature inputSigature = p.getInputSignature();
		OWLLogicalEntity query = p.getQuery();

		// String predicate =
		// LDLPCompilerManager.getInstance().getPredicate(query);

		int q = iriEncoder.encode(query.getIRI());

		// String sub =
		// KBCompilerManager.getInstance().getSubscript(inputSigature);

		String sub = String.valueOf(dlInputSignatureEncoder
				.encode(inputSigature));

		NormalPredicate newPredicate = CacheManager.getInstance()
				.getPredicate(
						RewritingVocabulary.DL_ATOM + "_" + q + "_" + sub,
						p.getArity());
		List<Term> terms = lit.getTerms();
		List<Term> newTerms = compileTerms(terms);
		Literal newLit = new Literal(newPredicate, newTerms);
		return newLit;
	}

	public List<Term> compileTerms(List<Term> terms) {
		List<Term> newTerms = new ArrayList<>();
		for (Term term : terms) {
			newTerms.add(complileTerm(term));

		}

		return newTerms;
	}

	public Term complileTerm(Term term) {
		if (term instanceof Constant) {
			// Constant constant = (Constant) term;
			// String name = constant.getName();
			// //String newName =
			// LDLPCompilerManager.getInstance().getConstant(name);
			// //Constant newConstant = new Constant(newName);
			// return newConstant;
			// TODO:
			return term;
		} else {
			return term;
		}
	}

	public List<Clause> compileSignature(DLInputSignature signature) {
		// String sub = KBCompilerManager.getInstance().getSubscript(signature);

		int sub = dlInputSignatureEncoder.encode(signature);

		List<Clause> clauses = new ArrayList<>();
		for (DLInputOperation op : signature.getOperations()) {
			NormalPredicate inputPredicate = op.getInputPredicate();
			int arity = inputPredicate.getArity();

			String name = null;
			if (arity == 1) {
				name = RewritingVocabulary.ISA.getName() + "_" + sub;
			} else if (arity == 2) {
				name = RewritingVocabulary.TRIPLE.getName() + "_" + sub;
			}

			NormalPredicate predicate = CacheManager.getInstance()
					.getPredicate(name, arity + 1); //reify

			// Constant cP =
			// CacheManager.getInstance().getConstant(iriEncoder.encode(op.getDLPredicate().getIRI()));
			Constant cP = CacheManager.getInstance().getConstant(
					op.getDLPredicate().getIRI());

			Literal head = null;
			Literal body = null;
			if (arity == 1) {
				head = new Literal(predicate, X, cP);
				body = new Literal(inputPredicate, X);
			} else if (arity == 2) {
				head = new Literal(predicate, X, cP, Y);
				body = new Literal(inputPredicate, X, Y);
			} else {
				throw new IllegalArgumentException();
			}

			Clause clause = new Clause();
			clause.setHead(head);
			clause.getPositiveBody().add(body);
			clauses.add(clause);
			// logger.debug("{} -> {}", signature, clause);
		}

		return clauses;
	}

	List<ProgramStatement> subscript(List<ProgramStatement> pInst,
			DLInputSignature signature) {

		List<ProgramStatement> newStatements = new ArrayList<>();

		String sub = String.valueOf(dlInputSignatureEncoder.encode(signature));
		// getSubscript(signature);

		for (ProgramStatement st : pInst) {

			if (st instanceof Clause) {
				Clause clause = (Clause) st;
				Literal head = clause.getHead();
				List<Literal> body = clause.getPositiveBody();
				// Note: In LDLp, we only have positive body
				Literal newHead = subscript(head, sub);
				Clause newClause = new Clause();
				newClause.setHead(newHead);
				for (Literal lit : body) {
					Literal newLit = subscript(lit, sub);
					newClause.getPositiveBody().add(newLit);
				}
				newStatements.add(newClause);
			} else if (st instanceof ProgramComment) {
				newStatements.add(st);
			}
			// logger.debug("{} / [{}]\n  ->\n{}", new Object[] { clause,
			// signature, newClause });
		}

		return newStatements;
	}

	private Literal subscript(Literal literal, String sub) {
		NormalPredicate predicate = (NormalPredicate) literal.getPredicate();

		switch (predicate.getType()) {
		case BUILTIN:
		case LOGIC:
			return literal;
		case NORMAL:
			int arity = predicate.getArity();
			String name = predicate.getName();
			String newName = name + "_" + sub;
			NormalPredicate newPredicate = CacheManager.getInstance()
					.getPredicate(newName, arity);
			Literal newLiteral = new Literal(newPredicate, literal.getTerms());

			return newLiteral;
		default:
			throw new IllegalStateException();
		}
	}

	// public Literal compile(OWLClassAssertionAxiom axiom) {
	//
	// OWLClassExpression classExpression = axiom.getClassExpression();
	//
	// OWLClass cls = (OWLClass) classExpression;
	//
	// String predicateName =
	// LDLPCompilerManager.getInstance().getPredicate(cls);
	//
	// NormalPredicate predicate =
	// CacheManager.getInstance().getPredicate(predicateName, 1);
	//
	// OWLIndividual individual = axiom.getIndividual();
	//
	// String const1 =
	// LDLPCompilerManager.getInstance().getConstant(individual);
	//
	// Term t = new Constant(const1);
	//
	// Literal literal = new Literal(predicate, t);
	//
	// return literal;
	// }

}
