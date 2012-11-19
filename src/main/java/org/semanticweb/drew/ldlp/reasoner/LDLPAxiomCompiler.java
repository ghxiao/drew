/*
 * @(#)AxiomCompiler.java 2010-3-24 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.ldlp.reasoner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.dlprogram.model.Variable;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LDLPAxiomCompiler extends OWLAxiomVisitorAdapter {

	LDLPCompilerManager ldlpCompierManager = LDLPCompilerManager
			.getInstance();
	final static Logger logger = LoggerFactory
			.getLogger(LDLPAxiomCompiler.class);
	Variable X = CacheManager.getInstance().getVariable("X");
	Variable Y = CacheManager.getInstance().getVariable("Y");
	Variable Z = CacheManager.getInstance().getVariable("Z");

	private List<ProgramStatement> clauses;

	public List<ProgramStatement> getClauses() {
		return clauses;
	}

	public LDLPAxiomCompiler() {
		this.clauses = new ArrayList<ProgramStatement>();
	}

	public ProgramStatement compileOWLAxiom(OWLAxiom axiom) {
		this.clauses = new ArrayList<ProgramStatement>();

		axiom.accept(this);

		return clauses.get(0);
	}

	public List<ProgramStatement> compile(OWLAxiom... axioms) {
		this.clauses = new ArrayList<ProgramStatement>();
		for (OWLAxiom owlAxiom : axioms) {
			owlAxiom.accept(this);
		}
		return clauses;
	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		final OWLIndividual subject = axiom.getSubject();
		final OWLIndividual object = axiom.getObject();
		Literal[] head = null;
		Literal[] body = null;
		head = new Literal[1];

		String predicate = ldlpCompierManager.getPredicate(property);
		String a = ldlpCompierManager.getConstant(subject);
		String b = ldlpCompierManager.getConstant(object);

		head[0] = new Literal(predicate, new Term[] {
				CacheManager.getInstance().getConstant(a),
				CacheManager.getInstance().getConstant(b) });
		body = new Literal[0];
		Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", axiom, clause);
	}

	@Override
	public void visit(OWLClassAssertionAxiom axiom) {
		OWLClassExpression cls = axiom.getClassExpression();
		OWLIndividual individual = axiom.getIndividual();
		Literal[] head = new Literal[1];
		Literal[] body = null;

		String predicate = ldlpCompierManager.getPredicate(cls);
		String a = ldlpCompierManager.getConstant(individual);
		head[0] = new Literal(predicate, new Term[] { CacheManager
				.getInstance().getConstant(a) });
		body = new Literal[0];
		Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", axiom, clause);
	}

	@Override
	public void visit(OWLSubClassOfAxiom axiom) {
		final OWLClassExpression subClass = axiom.getSubClass();
		final OWLClassExpression superClass = axiom.getSuperClass();
		Literal[] head = null;
		Literal[] body = null;

		if (!(superClass instanceof OWLObjectAllValuesFrom)) {
			head = new Literal[1];
			head[0] = new Literal(
					ldlpCompierManager.getPredicate(superClass),
					new Term[] { X });
			body = new Literal[1];
			body[0] = new Literal(ldlpCompierManager.getPredicate(subClass),
					new Term[] { X });
		} else {
			OWLObjectAllValuesFrom E_only_A = (OWLObjectAllValuesFrom) superClass;
			final OWLClassExpression A = E_only_A.getFiller();
			final OWLObjectPropertyExpression E = E_only_A.getProperty();
			head = new Literal[1];
			head[0] = new Literal(ldlpCompierManager.getPredicate(A),
					new Term[] { Y });
			body = new Literal[2];
			body[0] = new Literal(ldlpCompierManager.getPredicate(subClass),
					new Term[] { X });
			body[1] = new Literal(ldlpCompierManager.getPredicate(E),
					new Term[] { X, Y });
		}

		Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", axiom, clause);
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {
		final OWLObjectPropertyExpression subProperty = axiom.getSubProperty();
		final OWLObjectPropertyExpression superProperty = axiom
				.getSuperProperty();
		Literal[] head = new Literal[1];
		head[0] = new Literal(ldlpCompierManager.getPredicate(superProperty),
				new Term[] { X, Y });
		Literal[] body = new Literal[1];
		body[0] = new Literal(ldlpCompierManager.getPredicate(subProperty),
				new Term[] { X, Y });
		Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", axiom, clause);
	}

	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		OWLClassExpression domain = axiom.getDomain();

		Literal[] head = new Literal[1];
		head[0] = new Literal(ldlpCompierManager.getPredicate(domain),
				new Term[] { X });
		Literal[] body = new Literal[1];
		body[0] = new Literal(ldlpCompierManager.getPredicate(property),
				new Term[] { X, Y });

		Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", axiom, clause);
	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		OWLClassExpression range = axiom.getRange();

		Literal[] head = new Literal[1];
		head[0] = new Literal(ldlpCompierManager.getPredicate(range),
				new Term[] { Y });
		Literal[] body = new Literal[1];
		body[0] = new Literal(ldlpCompierManager.getPredicate(property),
				new Term[] { X, Y });

		Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", axiom, clause);
	}

	public List<ProgramStatement> compile(Set<OWLAxiom> axioms) {
		this.clauses = new ArrayList<ProgramStatement>();
		for (OWLAxiom owlAxiom : axioms) {
			owlAxiom.accept(this);
		}
		return clauses;

	}

	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom) {
		OWLDataPropertyExpression property = axiom.getProperty();
		final OWLIndividual subject = axiom.getSubject();

		final OWLLiteral object = axiom.getObject();
		Literal[] head = null;
		Literal[] body = null;
		head = new Literal[1];

		String predicate = ldlpCompierManager.getPredicate(property);
		String a = ldlpCompierManager.getConstant(subject);
		String b = ldlpCompierManager.getConstant(object);

		head[0] = new Literal(predicate, new Term[] {
				CacheManager.getInstance().getConstant(a),
				CacheManager.getInstance().getConstant(b) });
		body = new Literal[0];
		Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", axiom, clause);

	}

	@Override
	public void visit(OWLDataPropertyDomainAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	@Override
	public void visit(OWLDataPropertyRangeAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLTransitiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
		final OWLObjectPropertyExpression property = axiom.getProperty();
		String predicate = ldlpCompierManager.getPredicate(property);
		Literal[] head = null;
		Literal[] body = null;
		head = new Literal[1];
		head[0] = new Literal(predicate, X, Z);
		body = new Literal[2];
		body[0] = new Literal(predicate, X, Y);
		body[1] = new Literal(predicate, Y, Z);
		Clause clause = new Clause(head, body);
		clauses.add(clause);
		logger.debug("{}\n\t->\n{}", axiom, clause);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom)
	 */
	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		final Set<OWLClassExpression> classExpressions = axiom.getClassExpressions();
		if(classExpressions.size() != 2) {
			throw new IllegalStateException("Now we only support OWLEquivalentClassesAxiom with 2 classes");
		}
		
		final Iterator<OWLClassExpression> iterator = classExpressions.iterator();
		final OWLClassExpression left = iterator.next();
		final OWLClassExpression right = iterator.next();
		
		final OWLDataFactory owlDataFactory = OWLManager.getOWLDataFactory();
		
		final OWLSubClassOfAxiom left2right = owlDataFactory.getOWLSubClassOfAxiom(left,right);
		final OWLSubClassOfAxiom right2left = owlDataFactory.getOWLSubClassOfAxiom(right,left);
		
		left2right.accept(this);
		right2left.accept(this);
		
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom)
	 */
	@Override
	public void visit(OWLAnnotationAssertionAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom)
	 */
	@Override
	public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom)
	 */
	@Override
	public void visit(OWLAnnotationPropertyRangeAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom)
	 */
	@Override
	public void visit(OWLDatatypeDefinitionAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLDeclarationAxiom)
	 */
	@Override
	public void visit(OWLDeclarationAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom)
	 */
	@Override
	public void visit(OWLDifferentIndividualsAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLDisjointClassesAxiom)
	 */
	@Override
	public void visit(OWLDisjointClassesAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom)
	 */
	@Override
	public void visit(OWLDisjointDataPropertiesAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLDisjointUnionAxiom)
	 */
	@Override
	public void visit(OWLDisjointUnionAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom)
	 */
	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom)
	 */
	@Override
	public void visit(OWLFunctionalDataPropertyAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLHasKeyAxiom)
	 */
	@Override
	public void visit(OWLHasKeyAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		final OWLObjectPropertyExpression firstProperty = axiom.getFirstProperty();
		final OWLObjectPropertyExpression secondProperty = axiom.getSecondProperty();
		String first = ldlpCompierManager.getPredicate(firstProperty);
		String second = ldlpCompierManager.getPredicate(secondProperty);
		Literal[] head = null;
		Literal[] body = null;
		head = new Literal[1];
		head[0] = new Literal(first, X, Y);
		body = new Literal[1];
		body[0] = new Literal(second, Y, X);
		
		Clause clause1 = new Clause(head, body);
		clauses.add(clause1);
		logger.debug("{}\n\t->\n{}", axiom, clause1);
		
		Clause clause2 = new Clause(body, head);
		clauses.add(clause2);
		logger.debug("{}\n\t->\n{}", axiom, clause2);
		
		
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLSameIndividualAxiom)
	 */
	@Override
	public void visit(OWLSameIndividualAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom)
	 */
	@Override
	public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom)
	 */
	@Override
	public void visit(OWLSubDataPropertyOfAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom)
	 */
	@Override
	public void visit(OWLSubPropertyChainOfAxiom axiom) {
		logger.debug("Skip Axiom: " + axiom);
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
		final OWLObjectPropertyExpression property = axiom.getProperty();
		

		String p = ldlpCompierManager.getPredicate(property);
		
		Literal[] head = null;
		Literal[] body = null;
		head = new Literal[1];
		head[0] = new Literal(p, X, Y);
		body = new Literal[2];
		body[0] = new Literal(p, Y, X);
//		
//		Clause clause1 = new Clause(head, body);
//		clauses.add(clause1);
//		logger.debug("{}\n\t->\n{}", axiom, clause1);
//		
		Clause clause2 = new Clause(body, head);
		clauses.add(clause2);
		logger.debug("{}\n\t->\n{}", axiom, clause2);
		
		super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb.owlapi.model.SWRLRule)
	 */
	@Override
	public void visit(SWRLRule rule) {
		logger.debug("Skip Axiom: " + rule);
		super.visit(rule);
	}
}
