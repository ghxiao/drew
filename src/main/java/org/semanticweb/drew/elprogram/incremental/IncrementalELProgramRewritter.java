package org.semanticweb.drew.elprogram.incremental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Constant;
import org.semanticweb.drew.dlprogram.model.DLAtomPredicate;
import org.semanticweb.drew.dlprogram.model.DLInputOperation;
import org.semanticweb.drew.dlprogram.model.DLInputSignature;
import org.semanticweb.drew.dlprogram.model.IRIConstant;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.dlprogram.model.Variable;
import org.semanticweb.drew.el.SymbolEncoder;
import org.semanticweb.drew.el.reasoner.DReWELManager;
import org.semanticweb.drew.el.reasoner.RewritingVocabulary;
import org.semanticweb.owlapi.model.OWLLogicalEntity;

public class IncrementalELProgramRewritter {

	Variable X = CacheManager.getInstance().getVariable("X");
	Variable Y = CacheManager.getInstance().getVariable("Y");
	Variable I = CacheManager.getInstance().getVariable("I");

	public List<Clause> rewriteDLInputSignatures(
			Collection<DLInputSignature> signatures) {
		SymbolEncoder<DLInputSignature> signatureEncoder = DReWELManager
				.getInstance().getDlInputSignatureEncoder();

		// Variable X = CacheManager.getInstance().getVariable("X");
		// Variable Y = CacheManager.getInstance().getVariable("Y");

		List<Clause> clauses = new ArrayList<Clause>();

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
					List<DLInputOperation> delta = new ArrayList<DLInputOperation>(
							inputs2.getOperations());
					delta.removeAll(inputs1.getOperations());
					for (DLInputOperation input : delta) {
						NormalPredicate p = input.getInputPredicate();
						OWLLogicalEntity s = input.getDLPredicate();

						IRIConstant dlp = new IRIConstant(s.getIRI());

						Clause transferRule = new Clause();

						if (p.getArity() == 1) {
							transferRule.setHead(new Literal(
									RewritingVocabulary.INST_P, X, dlp,
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
		List<Clause> clauses = new ArrayList<Clause>();

		int arity = dlAtomPredicate.getArity();
		DLInputSignature input = dlAtomPredicate.getInputSignature();
		int q = DReWELManager.getInstance().getIRIEncoder()
				.encode(dlAtomPredicate.getQuery().getIRI());

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
							Y, ei));

			clause2.getPositiveBody().add(new Literal(RewritingVocabulary.SUBSET, I, ei));
			
		} else if (arity == 1) {
			clause1.setHead(new Literal(newPredicate, X));

			clause1.getPositiveBody()
					.add(new Literal(
							RewritingVocabulary.INST_P,
							X,
							new IRIConstant(dlAtomPredicate.getQuery().getIRI()),
							ei));

			clause1.setHead(new Literal(newPredicate, X));
			clause1.getPositiveBody()
					.add(new Literal(
							RewritingVocabulary.INST_P,
							X,
							new IRIConstant(dlAtomPredicate.getQuery().getIRI()),
							ei));

			clause2.setHead(new Literal(newPredicate, X));
			clause2.getPositiveBody()
					.add(new Literal(
							RewritingVocabulary.INST_P,
							X,
							new IRIConstant(dlAtomPredicate.getQuery().getIRI()),
							ei));

			clause2.getPositiveBody().add(new Literal(RewritingVocabulary.SUBSET, I, ei));
			
		}
		
		clauses.add(clause1);
		clauses.add(clause2);
		
		return clauses;

	}
}
