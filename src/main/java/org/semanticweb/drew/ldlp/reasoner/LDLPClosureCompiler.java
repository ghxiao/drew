package org.semanticweb.drew.ldlp.reasoner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.dlprogram.model.Variable;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualVisitor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import edu.stanford.db.lp.Literal;
//import edu.stanford.db.lp.ProgramClause;
//import edu.stanford.db.lp.StringTerm;
//import edu.stanford.db.lp.Term;
//import edu.stanford.db.lp.VariableTerm;

public class LDLPClosureCompiler implements OWLClassExpressionVisitor,
		OWLPropertyExpressionVisitor, OWLIndividualVisitor {

	private final static Logger logger = LoggerFactory
			.getLogger(LDLPClosureCompiler.class);

	private LDLPCompilerManager manager = LDLPCompilerManager.getInstance();

	private Variable X = CacheManager.getInstance().getVariable("X");
	private Variable Y = CacheManager.getInstance().getVariable("Y");
	Variable Z = CacheManager.getInstance().getVariable("Z");

	private String TOP1 = manager.getTop1();
	private String TOP2 = manager.getTop2();
	// String NOTEQUAL = manager.getNotEqual();

	private List<Clause> clauses;

	public LDLPClosureCompiler() {
		this.clauses = new ArrayList<>();
	}

	public List<Clause> compile(LDLPClosure closure) {

		for (OWLClass cls : closure.getNamedClasses()) {
			cls.accept(this);
		}

		for (OWLClassExpression cls : closure.getComplexClassExpressions()) {
			cls.accept(this);
		}

		for (OWLObjectProperty prop : closure.getNamedProperties()) {
			prop.accept(this);
		}

		for (OWLObjectPropertyExpression prop : closure
				.getComplexPropertyExpressions()) {
			prop.accept(this);
		}

		return clauses;
	}

	/**
	 * <pre>
	 * 	top(X):-A(X).
	 * </pre>
	 */
	@Override
	public void visit(OWLClass ce) {
		if (ce.isTopEntity()) {
			return;
		}

		Literal[] head = new Literal[1];
		head[0] = new Literal(TOP1, X);
		Literal[] body = new Literal[1];
		String predicate = manager.getPredicate(ce);
		body[0] = new Literal(predicate, X);
		Clause clause = new Clause(head, body);
		logger.debug("{}\n\t->\n{}", ce, clause);
		clauses.add(clause);
	}

	/**
	 * <pre>
	 * (A and B and C)(X):-A(X),B(X),C(X).
	 * </pre>
	 */
	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		final Set<OWLClassExpression> operands = ce.getOperands();
		int n = operands.size();
		Literal[] head = new Literal[1];
		head[0] = new Literal(manager.getPredicate(ce), X);
		Literal[] body = new Literal[n];

		int i = 0;
		for (OWLClassExpression operand : operands) {
			body[i] = new Literal(manager.getPredicate(operand), X);
			i++;
		}

		final Clause clause = new Clause(head, body);

		logger.debug("{}\n\t->\n{}", ce, clause);

		clauses.add(clause);

	}

	/**
	 * <pre>
	 * 	(A or B or C)(X):-A(X).
	 * 	(A or B or C)(X):-B(X).
	 * 	(A or B or C)(X):-C(X).
	 * </pre>
	 * 
	 */
	@Override
	public void visit(OWLObjectUnionOf ce) {
		final Set<OWLClassExpression> operands = ce.getOperands();

		int i = 0;
		for (OWLClassExpression operand : operands) {
			Literal[] head = new Literal[1];
			head[0] = new Literal(manager.getPredicate(ce), X);
			Literal[] body = new Literal[1];

			body[0] = new Literal(manager.getPredicate(operand), X);

			final Clause clause = new Clause(head, body);
			clauses.add(clause);
			logger.debug("{}\n\t->\n{}", ce, clause);
			i++;
		}

	}

	@Override
	public void visit(OWLObjectComplementOf ce) {
		throw new UnsupportedOperationException();

	}

	/**
	 * <pre>
	 * (E some A)(X):-E(X,Y),A(Y).
	 * </pre>
	 */
	@Override
	public void visit(OWLObjectSomeValuesFrom ce) {
		Literal[] head = new Literal[1];
		head[0] = new Literal(manager.getPredicate(ce), X);
		Literal[] body = new Literal[2];
		body[0] = new Literal(manager.getPredicate(ce.getProperty()), X, Y);
		body[1] = new Literal(manager.getPredicate(ce.getFiller()), Y);
		final Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", ce, clause);
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		// TODO: non normal form?
		throw new UnsupportedOperationException();

	}

	/**
	 * <pre>
	 * (R value o)(X) :- R(X, o).
	 * R(X, o) :- (R value o)(X).
	 * </pre>
	 */

	@Override
	public void visit(OWLObjectHasValue ce) {
		// TODO: Check the completeness
		OWLObjectPropertyExpression property = ce.getProperty();
		OWLIndividual value = ce.getValue();
		Literal[] head = new Literal[1];
		head[0] = new Literal(manager.getPredicate(ce), X);
		Literal[] body = new Literal[1];
		body[0] = new Literal(manager.getPredicate(property), X, CacheManager
				.getInstance().getConstant(manager.getConstant(value)));

		Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", ce, clause);

		clause = new Clause(body, head);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", ce, clause);

		// throw new UnsupportedOperationException();
	}

	/**
	 * <pre>
	 * (E min n D)(X):- E(X,Y1),D(Y1),...,E(X,Yn),D(Yn), 
	 * 					Y1 != Y2, Y1 != Y3, ..., Yn-1 != Yn
	 * </pre>
	 */
	@Override
	public void visit(OWLObjectMinCardinality ce) {
		// Xiao: Take care of it!!!!

		final OWLObjectPropertyExpression E = ce.getProperty();
		final int n = ce.getCardinality();
		final OWLClassExpression D = ce.getFiller();
		Literal[] head = new Literal[1];
		head[0] = new Literal(manager.getPredicate(ce), X);
		List<Literal> bodyLiterals = new ArrayList<>();
		Variable[] Ys = new Variable[n];
		for (int i = 0; i < Ys.length; i++) {
			Ys[i] = CacheManager.getInstance().getVariable("Y" + (i + 1));
		}

		final String pE = manager.getPredicate(E);
		final String pD = manager.getPredicate(D);

		for (int i = 0; i < n; i++) {
			bodyLiterals.add(new Literal(pE, X, Ys[i]));
			bodyLiterals.add(new Literal(pD, Ys[i]));
		}

		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				bodyLiterals.add(new Literal(NormalPredicate.NOTEQUAL, Ys[i],
						Ys[j]));
			}
		}
		Literal[] body = new Literal[0];
		body = bodyLiterals.toArray(body);
		final Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", ce, clause);
	}

	@Override
	public void visit(OWLObjectExactCardinality ce) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void visit(OWLObjectMaxCardinality ce) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLObjectHasSelf ce) {
		throw new UnsupportedOperationException();

	}

	/**
	 * <pre>
	 * 	{o1,o2}
	 * 
	 * {o1,o2)(o1).
	 * {o1,o2}(o2).
	 * </pre>
	 * 
	 * @see org.semanticweb.owlapi.model.OWLClassExpressionVisitor#visit(org.semanticweb.owlapi.model.OWLObjectOneOf)
	 * 
	 */
	@Override
	public void visit(OWLObjectOneOf ce) {
		final Set<OWLIndividual> individuals = ce.getIndividuals();
		final String predicate = manager.getPredicate(ce);
		for (OWLIndividual ind : individuals) {
			Literal[] head = new Literal[1];
			final String constant = manager.getConstant(ind);
			head[0] = new Literal(predicate, CacheManager.getInstance()
					.getConstant(constant));
			Literal[] body = new Literal[0];
			final Clause clause = new Clause(head, body);
			clauses.add(clause);
			logger.debug("{}\n\t->\n{}", ce, clause);
		}
	}

	/**
	 * <pre>
	 * (E some A)(X):-E(X,Y),A(Y).
	 * </pre>
	 */
	@Override
	public void visit(OWLDataSomeValuesFrom ce) {
		Literal[] head = new Literal[1];
		head[0] = new Literal(manager.getPredicate(ce), X);
		Literal[] body = new Literal[2];
		body[0] = new Literal(manager.getPredicate(ce.getProperty()), X, Y);
		body[1] = new Literal(manager.getPredicate(ce.getFiller()), Y);
		final Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", ce, clause);

	}

	@Override
	public void visit(OWLDataAllValuesFrom ce) {
		throw new UnsupportedOperationException();

	}

	// TODO check
	/**
	 * (R value o)(X) :- R(X, o)
	 */
	@Override
	public void visit(OWLDataHasValue ce) {
		Literal[] head = new Literal[1];
		head[0] = new Literal(manager.getPredicate(ce), X);
		Literal[] body = new Literal[1];
		body[0] = new Literal(manager.getPredicate(ce.getProperty()), X,
				CacheManager.getInstance().getConstant(
						manager.getConstant(ce.getValue().toString())));

		final Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", ce, clause);

	}

	@Override
	public void visit(OWLDataMinCardinality ce) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLDataExactCardinality ce) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLDataMaxCardinality ce) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLObjectProperty property) {
		if (property.isTopEntity()) {
			return;
		}

		Literal[] head = new Literal[1];
		head[0] = new Literal(TOP2, X, Y);
		Literal[] body = new Literal[1];
		String predicate = manager.getPredicate(property);
		body[0] = new Literal(predicate, X, Y);
		Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", property, clause);
	}

	// R^-(X,Y) :- R(Y,X).
	// R(X,Y) :- R^-(Y,X).
	@Override
	public void visit(OWLObjectInverseOf property) {

		Literal[] head1 = new Literal[1];
		final OWLObjectPropertyExpression inverse1 = property.getInverse();
		head1[0] = new Literal(manager.getPredicate(inverse1), X, Y);
		Literal[] body1 = new Literal[1];
		String predicate1 = manager.getPredicate(property);
		body1[0] = new Literal(predicate1, Y, X);
		Clause clause1 = new Clause(head1, body1);

		Clause clause2 = new Clause(body1, head1);

		clauses.add(clause1);
		clauses.add(clause2);

		logger.debug("{}\n\t->\n{}", property, clause1);
		logger.debug("{}\n\t->\n{}", property, clause2);
	}

	@Override
	public void visit(OWLDataProperty property) {
		throw new UnsupportedOperationException();
	}

	// @Override
	// public void visit(LDLObjectPropertyIntersectionOf property) {
	// final Set<OWLObjectPropertyExpression> operands = property
	// .getOperands();
	// int n = operands.size();
	// Literal[] head = new Literal[1];
	// head[0] = new Literal(manager.getPredicate(property),
	// new Term[] { X, Y });
	// Literal[] body = new Literal[n];
	//
	// int i = 0;
	// for (OWLObjectPropertyExpression operand : operands) {
	// body[i] = new Literal(manager.getPredicate(operand), new Term[] {
	// X, Y });
	// i++;
	// }
	//
	// final Clause clause = new Clause(head, body);
	// clauses.add(clause);
	// logger.debug("{}\n\t->\n{}", property, clause);
	// }

	// @Override
	// public void visit(LDLObjectPropertyUnionOf property) {
	// final Set<OWLObjectPropertyExpression> operands = property
	// .getOperands();
	//
	// for (OWLObjectPropertyExpression operand : operands) {
	// Literal[] head = new Literal[1];
	// head[0] = new Literal(manager.getPredicate(property), new Term[] {
	// X, Y });
	// Literal[] body = new Literal[1];
	//
	// body[0] = new Literal(manager.getPredicate(operand), new Term[] {
	// X, Y });
	//
	// final Clause clause = new Clause(head, body);
	// clauses.add(clause);
	// logger.debug("{}\n\t->\n{}", property, clause);
	// }
	//
	// }

	// trans(E)(X,Y):-E(X,Y)
	// trans(E)(X,Z):-E(X,Y),trans(Y,Z).
	// @Override
	// public void visit(LDLObjectPropertyTransitiveClosureOf property) {
	// final OWLObjectPropertyExpression operand = property.getOperand();
	//
	// Literal[] head1 = new Literal[1];
	// head1[0] = new Literal(manager.getPredicate(property), new Term[] { X,
	// Y });
	// Literal[] body1 = new Literal[1];
	// body1[0] = new Literal(manager.getPredicate(operand),
	// new Term[] { X, Y });
	// final Clause clause1 = new Clause(head1, body1);
	// logger.debug("{}\n\t->\n{}", property, clause1);
	// clauses.add(clause1);
	// Literal[] head2 = new Literal[1];
	// head2[0] = new Literal(manager.getPredicate(property), new Term[] { X,
	// Z });
	// Literal[] body2 = new Literal[2];
	// body2[0] = new Literal(manager.getPredicate(operand),
	// new Term[] { X, Y });
	// body2[1] = new Literal(manager.getPredicate(property), new Term[] { Y,
	// Z });
	// final Clause clause2 = new Clause(head2, body2);
	// clauses.add(clause2);
	// logger.debug("{}\n\t->\n{}", property, clause2);
	// }

	// compose(E1, E2, ... En)(X1,Xn+1):- E1(X1,X2), E2(X2,X3), ... ,
	// En(Xn,Xn+1)
	// @Override
	// public void visit(LDLObjectPropertyChainOf property) {
	// final Set<OWLObjectPropertyExpression> operands = property
	// .getOperands();
	// int n = operands.size();
	// Variable[] Xs = new Variable[n + 1];
	// for (int i = 0; i < n + 1; i++) {
	// Xs[i] = CacheManager.getInstance().getVariable("X" + (i + 1));
	// }
	//
	// Literal[] head = new Literal[1];
	// head[0] = new Literal(manager.getPredicate(property), new Term[] {
	// Xs[0], Xs[n] });
	// Literal[] body = new Literal[n];
	// int i = 0;
	// for (OWLObjectPropertyExpression operand : operands) {
	// body[i] = new Literal(manager.getPredicate(operand), new Term[] {
	// Xs[i], Xs[i + 1] });
	// i++;
	// }
	// final Clause clause = new Clause(head, body);
	// clauses.add(clause);
	// logger.debug("{}\n\t->\n{}", property, clause);
	//
	// }

	@Override
	public void visit(OWLNamedIndividual individual) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void visit(OWLAnonymousIndividual individual) {
		throw new UnsupportedOperationException();

	}

	// /**
	// * <pre>
	// * {<o11,o12>,<o21,o22>)
	// *
	// * {<o11,o12>,<o21,o22>)(o11,o12).
	// * {<o11,o12>,<o21,o22>)(o21,o22).
	// * </pre>
	// *
	// */
	// @Override
	// public void visit(LDLObjectPropertyOneOf property) {
	// final Set<LDLIndividualPair> individualPairs = property.getOperands();
	// final String predicate = manager.getPredicate(property);
	// for (LDLIndividualPair pair : individualPairs) {
	// Literal[] head = new Literal[1];
	// final String constant1 = manager.getConstant(pair.getFirst());
	// final String constant2 = manager.getConstant(pair.getSecond());
	// head[0] = new Literal(predicate, CacheManager.getInstance()
	// .getConstant(constant1), CacheManager.getInstance()
	// .getConstant(constant2));
	// Literal[] body = new Literal[0];
	// final Clause clause = new Clause(head, body);
	// clauses.add(clause);
	// logger.debug("{}\n\t->\n{}", property, clause);
	// }
	//
	// }

}
