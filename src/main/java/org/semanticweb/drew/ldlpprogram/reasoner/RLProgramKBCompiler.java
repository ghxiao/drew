/*
 * @(#)DLProgramCompiler.java 2010-4-3 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.ldlpprogram.reasoner;

import java.sql.Types;
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
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.dlprogram.model.Variable;

import org.semanticweb.drew.ldlp.reasoner.LDLPCompilerManager;
import org.semanticweb.drew.ldlp.reasoner.LDLPOntologyCompiler;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLogicalEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KBCompiler: compile the dl-program KB to a datalog^n program.
 */
public class RLProgramKBCompiler implements DLProgramKB2DatalogRewriter{

	private final static Logger logger = LoggerFactory.getLogger(RLProgramKBCompiler.class);

	private Variable X = CacheManager.getInstance().getVariable("X");
	private Variable Y = CacheManager.getInstance().getVariable("Y");

	@Override
	public DLProgram rewrite(DLProgramKB kb) {
		List<ProgramStatement> result = new ArrayList<>();

		final OWLOntology ontology = kb.getOntology();
		LDLPOntologyCompiler ldlpCompiler = new LDLPOntologyCompiler();
		final List<ProgramStatement> compiledOntology = ldlpCompiler
				.compile(ontology);
		final DLProgram program = kb.getProgram();
		final Set<DLInputSignature> dlInputSignatures = program
				.getDLInputSignatures();
		for (DLInputSignature signature : dlInputSignatures) {
			result.addAll(subscript(compiledOntology, signature));
		}

		for (DLInputSignature signature : dlInputSignatures) {
			result.addAll(compileSignature(signature));
		}

		result.addAll(compileProgram(program));

		DLProgram resultProgram = new DLProgram();
		resultProgram.addAll(result);
		// resultProgram.getClauses().addAll(result);

		return resultProgram;
	}

	/**
	 * P -> P^{ord} replace all the DLAtom with a ordinary atom
	 * 
	 */
    List<ProgramStatement> compileProgram(DLProgram program) {

		List<ProgramStatement> result = new ArrayList<>();

		for (ProgramStatement stmt : program.getStatements()) {

			if (stmt.isClause()) {

				Clause newClause = compileClause(stmt.asClause());

				result.add(newClause);
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

		logger.debug("{}\n   -> \n{}", clause, newClause);

		return newClause;
	}

	public Literal compileNormalLiteral(Literal lit) {
		List<Term> terms = lit.getTerms();
		List<Term> newTerms = compileTerms(terms);
		Literal newLit = new Literal(lit.getPredicate(), newTerms);
		return newLit;
	}

	Literal compileDLAtom(Literal lit) {
		DLAtomPredicate p = (DLAtomPredicate) (lit.getPredicate());
		DLInputSignature inputSigature = p.getInputSignature();
		OWLLogicalEntity query = p.getQuery();
		String predicate = LDLPCompilerManager.getInstance()
				.getPredicate(query);
		String sub = KBCompilerManager.getInstance()
				.getSubscript(inputSigature);
		NormalPredicate newPredicate = CacheManager.getInstance().getPredicate(
				predicate + "_" + sub, p.getArity());
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
		if (term instanceof Constant && ((Constant)term).getType() != Types.INTEGER ) {
			Constant constant = (Constant) term;
			String name = constant.getName();
			String newName = LDLPCompilerManager.getInstance()
					.getConstant(name);
			Constant newConstant = new Constant(newName);
			return newConstant;
		} else {
			return term;
		}
	}

	List<Clause> compileSignature(DLInputSignature signature) {
		String sub = KBCompilerManager.getInstance().getSubscript(signature);
		List<Clause> clauses = new ArrayList<>();
		for (DLInputOperation op : signature.getOperations()) {
			String name = LDLPCompilerManager.getInstance().getPredicate(
					op.getDLPredicate())
					+ "_" + sub;
			NormalPredicate inputPredicate = op.getInputPredicate();
			int arity = inputPredicate.getArity();
			NormalPredicate predicate = CacheManager.getInstance()
					.getPredicate(name, arity);
			Literal head = null;
			Literal body = null;
			if (arity == 1) {
				head = new Literal(predicate, X);
				body = new Literal(inputPredicate, X);
			} else if (arity == 2) {
				head = new Literal(predicate, X, Y);
				body = new Literal(inputPredicate, X, Y);
			} else {
				throw new IllegalArgumentException();
			}

			Clause clause = new Clause();
			clause.setHead(head);
			clause.getPositiveBody().add(body);
			clauses.add(clause);
			logger.debug("{} -> {}", signature, clause);
		}

		return clauses;
	}

	List<ProgramStatement> subscript(List<ProgramStatement> stmts,
			DLInputSignature signature) {

		List<ProgramStatement> newStmts = new ArrayList<>();
		String sub = KBCompilerManager.getInstance().getSubscript(signature);

		for (ProgramStatement stmt : stmts) {
			if (stmt.isClause()) {
				Clause clause = stmt.asClause();
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
				newStmts.add(newClause);

				logger.debug("{} / [{}]\n  ->\n{}", new Object[] { stmt,
						signature, newClause });
			}
		}

		return newStmts;
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

	public Literal compile(OWLClassAssertionAxiom axiom) {

		OWLClassExpression classExpression = axiom.getClassExpression();

		OWLClass cls = (OWLClass) classExpression;

		String predicateName = LDLPCompilerManager.getInstance().getPredicate(
				cls);

		NormalPredicate predicate = CacheManager.getInstance().getPredicate(
				predicateName, 1);

		OWLIndividual individual = axiom.getIndividual();

		String const1 = LDLPCompilerManager.getInstance().getConstant(
				individual);

		Term t = new Constant(const1);

		Literal literal = new Literal(predicate, t);

		return literal;
	}

}
