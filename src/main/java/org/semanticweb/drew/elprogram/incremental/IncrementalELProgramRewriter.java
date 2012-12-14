package org.semanticweb.drew.elprogram.incremental;

import java.util.ArrayList;
import java.util.Collection;
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
import org.semanticweb.drew.dlprogram.model.IRIConstant;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.dlprogram.model.Variable;
import org.semanticweb.drew.el.SymbolEncoder;
import org.semanticweb.drew.el.reasoner.DReWELManager;
import org.semanticweb.drew.el.reasoner.RewritingVocabulary;
import org.semanticweb.drew.el.reasoner.SROEL2DatalogRewriter;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLogicalEntity;
import org.semanticweb.owlapi.model.OWLOntology;

public class IncrementalELProgramRewriter implements
		DLProgramKB2DatalogRewriter {

	private Variable X = CacheManager.getInstance().getVariable("X");
	private Variable Y = CacheManager.getInstance().getVariable("Y");
	private Variable I = CacheManager.getInstance().getVariable("I");

	private SymbolEncoder<DLInputSignature> dlInputSignatureEncoder;

	private SymbolEncoder<IRI> iriEncoder;

	public IncrementalELProgramRewriter() {
		dlInputSignatureEncoder = DReWELManager.getInstance()
				.getDlInputSignatureEncoder();
		iriEncoder = DReWELManager.getInstance().getIRIEncoder();
	}

	public List<ProgramStatement> rewriteELProgram(DLProgram program) {
		List<ProgramStatement> result = new ArrayList<>();

		final Set<DLInputSignature> dlInputSignatures = program
				.getDLInputSignatures(true);

		result.addAll(rewriteDLInputSignatures(dlInputSignatures));

		Set<DLAtomPredicate> dlAtomPredicates = program.getDLAtomPredicates();
		for (DLAtomPredicate dlAtomPredicate : dlAtomPredicates) {
			result.addAll(rewriteDLAtomPredicate(dlAtomPredicate));
		}

		result.addAll(compileProgram(program));
		return result;
	}

	public List<ProgramStatement> rewriteELProgramKB(DLProgramKB kb) {

		List<ProgramStatement> result = new ArrayList<>();

		final OWLOntology ontology = kb.getOntology();
		SROEL2DatalogRewriter ldlpCompiler = new SROEL2DatalogRewriter();
		// final List<Clause> compiledOntology = ldlpCompiler.rewrite(ontology)
		// .getClauses();
		final List<ProgramStatement> compiledOntology = ldlpCompiler.rewrite(ontology)
				.getStatements();

		final DLProgram program = kb.getProgram();

		result = rewriteELProgram(program);

		DLProgram resultProgram = new DLProgram();

		//resultProgram.getClauses().addAll(result);
		resultProgram.addAll(result);

		resultProgram.addAll(compiledOntology);
		resultProgram.addAll(PInstStar.pInstStar);

		return resultProgram.getStatements();
	}

	List<ProgramStatement> compileProgram(DLProgram program) {
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

	Clause compileClause(Clause clause) {
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

	Literal compileNormalLiteral(Literal lit) {
		List<Term> terms = lit.getTerms();
		List<Term> newTerms = compileTerms(terms);
		Literal newLit = new Literal(lit.getPredicate(), newTerms);
		return newLit;
	}

	Literal compileDLAtom(Literal lit) {
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

	List<Term> compileTerms(List<Term> terms) {
		List<Term> newTerms = new ArrayList<>();
		for (Term term : terms) {
			newTerms.add(complileTerm(term));

		}

		return newTerms;
	}

	Term complileTerm(Term term) {
		return term;
	}

	public Collection<Clause> rewriteDLInputSignatures(
			Collection<DLInputSignature> signatures) {
		SymbolEncoder<DLInputSignature> signatureEncoder = DReWELManager
				.getInstance().getDlInputSignatureEncoder();

		// Variable X = CacheManager.getInstance().getVariable("X");
		// Variable Y = CacheManager.getInstance().getVariable("Y");

		List<Clause> clauses = new ArrayList<>();

		// signatures.add(DLInputSignature.EMPTY);

		for (DLInputSignature inputs : signatures) {
			Constant input = CacheManager.getInstance().getConstant(
					signatureEncoder.encode(inputs));
			Clause inputFact = new Clause();
			inputFact.setHead(new Literal(RewritingVocabulary.INPUT, input));
			clauses.add(inputFact);
		}

		for (DLInputSignature inputs1 : signatures) {
			for (DLInputSignature inputs2 : signatures) {
				if (inputs2.getOperations().size() > inputs1.getOperations()
						.size() //
						&& inputs2.getOperations().containsAll(
								inputs1.getOperations())) {
					Clause subsetClause = new Clause();
					subsetClause.setHead(new Literal(
							RewritingVocabulary.SUBSET, //
							CacheManager.getInstance().getConstant(
									signatureEncoder.encode(inputs1)),
							CacheManager.getInstance().getConstant(
									signatureEncoder.encode(inputs2))));
					clauses.add(subsetClause);
					List<DLInputOperation> delta = new ArrayList<>(
							inputs2.getOperations());
					delta.removeAll(inputs1.getOperations());
					for (DLInputOperation input : delta) {
						NormalPredicate p = input.getInputPredicate();
						OWLLogicalEntity s = input.getDLPredicate();

						IRIConstant dlp = new IRIConstant(s.getIRI());

						Clause transferRule = new Clause();

						if (p.getArity() == 1) {
							transferRule.setHead(new Literal(
									RewritingVocabulary.ISA_P, X, dlp,
									CacheManager.getInstance().getConstant(
											signatureEncoder.encode(inputs2))));
							transferRule.getPositiveBody().add(
									new Literal(p, X));
						} else if (p.getArity() == 2) {
							transferRule.setHead(new Literal(
									RewritingVocabulary.TRIPLE_P, X, dlp, Y,
									CacheManager.getInstance().getConstant(
											signatureEncoder.encode(inputs2))));
							transferRule.getPositiveBody().add(
									new Literal(p, X, Y));
						} else {
							throw new IllegalStateException();
						}
						clauses.add(transferRule);
					}
				}
			}

		}
		return clauses;
	}

	public List<Clause> rewriteDLAtomPredicate(DLAtomPredicate dlAtomPredicate) {
		List<Clause> clauses = new ArrayList<>();

		int arity = dlAtomPredicate.getArity();
		DLInputSignature input = dlAtomPredicate.getInputSignature();
		int q = DReWELManager.getInstance().getIRIEncoder()
				.encode(dlAtomPredicate.getQuery().getIRI());

		// String Q;
		//
		// switch (DReWELManager.getInstance().getNamingStrategy()) {
		// case IntEncoding:
		// Q = String.valueOf(q);
		// break;
		//
		// default:
		// Q = dlAtomPredicate.getQuery().getIRI().getFragment();
		// if (Q 0)
		// break;
		// }

		// String sub =
		// KBCompilerManager.getInstance().getSubscript(inputSigature);

		String sub = String.valueOf(DReWELManager.getInstance()
				.getDlInputSignatureEncoder().encode(input));

		NormalPredicate newPredicate = CacheManager.getInstance().getPredicate(
				RewritingVocabulary.DL_ATOM + "_" + q + "_" + sub,
				dlAtomPredicate.getArity());

		Clause clause1 = new Clause();
		Clause clause2 = new Clause();

		Constant ei = CacheManager.getInstance().getConstant(
				DReWELManager.getInstance().getDlInputSignatureEncoder()
						.encode(input));

		if (arity == 2) {
			clause1.setHead(new Literal(newPredicate, X, Y));

			clause1.getPositiveBody()
					.add(new Literal(
							RewritingVocabulary.TRIPLE_P,
							X,
							new IRIConstant(dlAtomPredicate.getQuery().getIRI()),
							Y, ei));

			clause2.setHead(new Literal(newPredicate, X, Y));
			clause2.getPositiveBody()
					.add(new Literal(
							RewritingVocabulary.TRIPLE_P,
							X,
							new IRIConstant(dlAtomPredicate.getQuery().getIRI()),
							Y, I));

			clause2.getPositiveBody().add(
					new Literal(RewritingVocabulary.SUBSET, I, ei));

		} else if (arity == 1) {
			clause1.setHead(new Literal(newPredicate, X));

			clause1.getPositiveBody().add(
					new Literal(RewritingVocabulary.ISA_P, X, new IRIConstant(
							dlAtomPredicate.getQuery().getIRI()), ei));

			clause2.setHead(new Literal(newPredicate, X));
			clause2.getPositiveBody().add(
					new Literal(RewritingVocabulary.ISA_P, X, new IRIConstant(
							dlAtomPredicate.getQuery().getIRI()), I));

			clause2.getPositiveBody().add(
					new Literal(RewritingVocabulary.SUBSET, I, ei));

		}

		clauses.add(clause1);
		clauses.add(clause2);

		return clauses;

	}

	@Override
	public DLProgram rewrite(DLProgramKB kb) {
		List<ProgramStatement> rules = rewriteELProgramKB(kb);
		DLProgram result = new DLProgram();
		result.addAll(rules);
		return result;
	}
}
