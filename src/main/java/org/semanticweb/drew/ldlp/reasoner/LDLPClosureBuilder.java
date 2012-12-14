/*
 * @(#)ClosureBuilder.java 2010-3-24 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.ldlp.reasoner;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDataVisitor;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualVisitor;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
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
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitor;
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

//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyChainOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyIntersectionOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyOneOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyTransitiveClosureOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyUnionOf;

/**
 * TODO describe this class please.
 */

class LDLPClosureBuilder extends OWLAxiomVisitorAdapter implements OWLClassExpressionVisitor,
		OWLPropertyExpressionVisitor, OWLIndividualVisitor, OWLDataVisitor {

	private LDLPClosure closure = new LDLPClosure();

	public LDLPClosureBuilder() {

	}

	public LDLPClosure build(OWLOntology ontology) {
		final Set<OWLAxiom> axioms = ontology.getAxioms();
		return build(axioms);
	}

	public LDLPClosure build(final Set<OWLAxiom> axioms) {
		for (OWLAxiom axiom : axioms) {
			axiom.accept(this);
		}
		return closure;
	}

	public LDLPClosure build(final OWLAxiom... axioms) {
		for (OWLAxiom axiom : axioms) {
			axiom.accept(this);
		}
		return closure;
	}

	@Override
	public void visit(OWLClassAssertionAxiom axiom) {
		final OWLClassExpression cls = axiom.getClassExpression();
		final OWLIndividual individual = axiom.getIndividual();
		cls.accept(this);
		individual.accept(this);
	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom) {
		final OWLObjectPropertyExpression property = axiom.getProperty();
		final OWLIndividual subject = axiom.getSubject();
		final OWLIndividual object = axiom.getObject();
		property.accept(this);
		subject.accept(this);
		object.accept(this);
	}

	@Override
	public void visit(OWLSubClassOfAxiom axiom) {
		final OWLClassExpression subClass = axiom.getSubClass();
		final OWLClassExpression superClass = axiom.getSuperClass();

		if (!(superClass instanceof OWLObjectAllValuesFrom)) {
			superClass.accept(this);
			subClass.accept(this);
		} else {
			OWLObjectAllValuesFrom E_only_A = (OWLObjectAllValuesFrom) superClass;
			final OWLClassExpression A = E_only_A.getFiller();
			final OWLObjectPropertyExpression E = E_only_A.getProperty();
			A.accept(this);
			E.accept(this);
			subClass.accept(this);
		}
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {
		final OWLObjectPropertyExpression subProperty = axiom.getSubProperty();
		final OWLObjectPropertyExpression superProperty = axiom.getSuperProperty();
		subProperty.accept(this);
		superProperty.accept(this);
	}

	@Override
	public void visit(OWLClass ce) {
		closure.addNamedClasses(ce);
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		closure.addComplexClass(ce);
		for (OWLClassExpression op : ce.getOperands()) {
			op.accept(this);
		}
	}

	@Override
	public void visit(OWLObjectUnionOf ce) {
		closure.addComplexClass(ce);
		for (OWLClassExpression op : ce.getOperands()) {
			op.accept(this);
		}
	}

	@Override
	public void visit(OWLObjectComplementOf ce) {
		// this is not possible for LDLP
		closure.addComplexClass(ce);
		ce.getOperand().accept(this);

	}

	@Override
	public void visit(OWLObjectSomeValuesFrom ce) {
		closure.addComplexClass(ce);
		ce.getProperty().accept(this);
		ce.getFiller().accept(this);
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		// TODO: Fix me if the expression is not in normal form
		closure.addComplexClass(ce);
		ce.getProperty().accept(this);
		ce.getFiller().accept(this);
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		closure.addComplexClass(ce);
		ce.getProperty().accept(this);
		ce.getValue().accept(this);
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {
		closure.addComplexClass(ce);
		ce.getProperty().accept(this);
		ce.getFiller().accept(this);
	}

	@Override
	public void visit(OWLObjectExactCardinality ce) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLObjectMaxCardinality ce) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLObjectHasSelf ce) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLObjectOneOf ce) {
		closure.addComplexClass(ce);
	}

	@Override
	public void visit(OWLDataSomeValuesFrom ce) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLDataAllValuesFrom ce) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLDataHasValue ce) {
		closure.addComplexClass(ce);
		ce.getProperty().accept(this);
		ce.getValue().accept(this);
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
		closure.addNamedProperty(property);
	}

	@Override
	public void visit(OWLObjectInverseOf property) {
		closure.addComplexProperty(property);
		property.getInverse().accept(this);
	}

	@Override
	public void visit(OWLDataProperty property) {
		closure.addNamedDataProperty(property);
	}

	// @Override
	// public void visit(LDLObjectPropertyIntersectionOf property) {
	// closure.addComplexProperty(property);
	// for (OWLObjectPropertyExpression operand : property.getOperands()) {
	// operand.accept(this);
	// }
	// }
	//
	// @Override
	// public void visit(LDLObjectPropertyUnionOf property) {
	// closure.addComplexProperty(property);
	// for (OWLObjectPropertyExpression operand : property.getOperands()) {
	// operand.accept(this);
	// }
	// }
	//
	// @Override
	// public void visit(LDLObjectPropertyTransitiveClosureOf property) {
	// closure.addComplexProperty(property);
	// property.getOperand().accept(this);
	//
	// }
	//
	// @Override
	// public void visit(LDLObjectPropertyChainOf property) {
	// closure.addComplexProperty(property);
	// for (OWLObjectPropertyExpression operand : property.getOperands()) {
	// operand.accept(this);
	// }
	// }

	@Override
	public void visit(OWLNamedIndividual individual) {
		closure.addNamedIndividual(individual);
		// OWLObjectOneOf oneOf = dataFactory.getOWLObjectOneOf(individual);

	}

	@Override
	public void visit(OWLAnonymousIndividual individual) {
		throw new UnsupportedOperationException();
	}

	// @Override
	// public void visit(LDLObjectPropertyOneOf property) {
	// closure.addComplexProperty(property);
	//
	// }

	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
		axiom.getProperty().accept(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLAnnotationAssertionAxiom)
	 */
	@Override
	public void visit(OWLAnnotationAssertionAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLAnnotationPropertyDomainAxiom)
	 */
	@Override
	public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLAnnotationPropertyRangeAxiom)
	 */
	@Override
	public void visit(OWLAnnotationPropertyRangeAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLAsymmetricObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLDataPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLDataPropertyDomainAxiom)
	 */
	@Override
	public void visit(OWLDataPropertyDomainAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLDataPropertyRangeAxiom)
	 */
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
	 * .owlapi.model.OWLDatatypeDefinitionAxiom)
	 */
	@Override
	public void visit(OWLDatatypeDefinitionAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLDeclarationAxiom)
	 */
	@Override
	public void visit(OWLDeclarationAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLDifferentIndividualsAxiom)
	 */
	@Override
	public void visit(OWLDifferentIndividualsAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLDisjointClassesAxiom)
	 */
	@Override
	public void visit(OWLDisjointClassesAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLDisjointDataPropertiesAxiom)
	 */
	@Override
	public void visit(OWLDisjointDataPropertiesAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLDisjointObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLDisjointUnionAxiom)
	 */
	@Override
	public void visit(OWLDisjointUnionAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLEquivalentClassesAxiom)
	 */
	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		// FIXME: More cases should be considered
		for (OWLClassExpression cls : axiom.getClassExpressions()) {
			cls.accept(this);
		}
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLEquivalentDataPropertiesAxiom)
	 */
	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLEquivalentObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLFunctionalDataPropertyAxiom)
	 */
	@Override
	public void visit(OWLFunctionalDataPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLFunctionalObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLHasKeyAxiom)
	 */
	@Override
	public void visit(OWLHasKeyAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLInverseFunctionalObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLInverseObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLIrreflexiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLNegativeDataPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLNegativeObjectPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLObjectPropertyDomainAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLObjectPropertyRangeAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLReflexiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLSameIndividualAxiom)
	 */
	@Override
	public void visit(OWLSameIndividualAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLSubAnnotationPropertyOfAxiom)
	 */
	@Override
	public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLSubDataPropertyOfAxiom)
	 */
	@Override
	public void visit(OWLSubDataPropertyOfAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLSubPropertyChainOfAxiom)
	 */
	@Override
	public void visit(OWLSubPropertyChainOfAxiom axiom) {
		// TODO Auto-generated method stub
		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.OWLSymmetricObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
		axiom.getProperty().accept(this);

		super.visit(axiom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter#visit(org.semanticweb
	 * .owlapi.model.SWRLRule)
	 */
	@Override
	public void visit(SWRLRule rule) {
		// TODO Auto-generated method stub
		super.visit(rule);
	}

	@Override
	public void visit(OWLDatatype node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLDataOneOf node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLDataComplementOf node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLDataIntersectionOf node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLDataUnionOf node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLDatatypeRestriction node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(OWLLiteral node) {
		closure.addLiteral(node);
	}

	@Override
	public void visit(OWLFacetRestriction node) {
		throw new UnsupportedOperationException();
	}
}
