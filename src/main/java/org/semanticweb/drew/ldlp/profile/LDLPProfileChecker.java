/*
 * @(#)LDLPProfileChecker.java 2010-3-17 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.ldlp.profile;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.profiles.UseOfIllegalAxiom;
import org.semanticweb.owlapi.profiles.UseOfIllegalClassExpression;
import org.semanticweb.owlapi.profiles.UseOfIllegalDataRange;
import org.semanticweb.owlapi.profiles.UseOfNonEquivalentClassExpression;
import org.semanticweb.owlapi.profiles.UseOfNonSubClassExpression;
import org.semanticweb.owlapi.profiles.UseOfNonSuperClassExpression;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

/**
 * LDLPProfileChecker
 */

class LDLPProfileChecker extends OWLOntologyWalkerVisitor {


	private Set<IRI> allowedDatatypes = new HashSet<>();

	private Set<OWLProfileViolation> profileViolations = new HashSet<>();

	private LDLPSubClassExpressionChecker subClassExpressionChecker = new LDLPSubClassExpressionChecker();

	private boolean isLDLPSubClassExpression(OWLClassExpression ce) {
		return ce.accept(subClassExpressionChecker);
	}

	private LDLPSuperClassExpressionChecker superClassExpressionChecker = new LDLPSuperClassExpressionChecker();

	public boolean isLDLPSuperClassExpression(OWLClassExpression ce) {
		return ce.accept(superClassExpressionChecker);
	}

	private LDLPEquivalentClassExpressionChecker equivalentClassExpressionChecker = new LDLPEquivalentClassExpressionChecker();

	public boolean isLDLPEquivalentClassExpression(OWLClassExpression ce) {
		return ce.accept(equivalentClassExpressionChecker);
	}

	private LDLPSubPropertyExpressionChecker subPropertyExpressionChecker = new LDLPSubPropertyExpressionChecker();

	boolean isLDLPSubObjectPropertyExpression(OWLObjectPropertyExpression ope) {
		return ope.accept(subPropertyExpressionChecker);
	}

	private boolean isLDLPSubDataPropertyExpression(OWLDataPropertyExpression property) {
		return property.accept(subPropertyExpressionChecker);

	}

	private LDLPSuperPropertyExpressionChecker superObjectPropertyExpressionChecker = new LDLPSuperPropertyExpressionChecker();

	boolean isLDLPSuperObjectPropertyExpression(OWLObjectPropertyExpression ope) {
		return ope.accept(superObjectPropertyExpressionChecker);
	}

	LDLPProfileChecker(OWLOntologyWalker walker) {

		super(walker);
		allowedDatatypes.add(OWLRDFVocabulary.RDF_PLAIN_LITERAL.getIRI());
        allowedDatatypes.add(OWLRDFVocabulary.RDF_XML_LITERAL.getIRI());
        allowedDatatypes.add(OWLRDFVocabulary.RDFS_LITERAL.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_DECIMAL.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_INTEGER.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_NON_NEGATIVE_INTEGER.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_NON_POSITIVE_INTEGER.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_POSITIVE_INTEGER.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_NEGATIVE_INTEGER.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_LONG.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_INT.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_SHORT.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_BYTE.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_UNSIGNED_LONG.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_UNSIGNED_BYTE.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_FLOAT.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_DOUBLE.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_STRING.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_NORMALIZED_STRING.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_TOKEN.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_LANGUAGE.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_NAME.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_NCNAME.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_NMTOKEN.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_BOOLEAN.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_HEX_BINARY.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_BASE_64_BINARY.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_ANY_URI.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_DATE_TIME.getIRI());
        allowedDatatypes.add(OWL2Datatype.XSD_DATE_TIME_STAMP.getIRI());
		
	}

	public Set<OWLProfileViolation> getProfileViolations() {
		return new HashSet<>(profileViolations);
	}

	public Object visit(OWLClassAssertionAxiom axiom) {
		if (!isLDLPSuperClassExpression(axiom.getClassExpression())) {
			profileViolations.add(new UseOfNonSuperClassExpression(getCurrentOntology(), axiom, axiom.getClassExpression()));
			
		}

		return null;
	}

	public Object visit(OWLDataPropertyDomainAxiom axiom) {

		
		OWLDataPropertyExpression property = axiom.getProperty();
		if (!isLDLPSubDataPropertyExpression(property)) {
			profileViolations.add(new UseOfNonLDLPSubPropertyExpression(getCurrentOntology(), axiom, property));
			
		}

		OWLClassExpression domain = axiom.getDomain();
		if (!isLDLPSuperClassExpression(domain)) {
			profileViolations.add(new UseOfNonSuperClassExpression(getCurrentOntology(), axiom, domain));
			
		}

		
		return null;
	}

	public Object visit(OWLDisjointClassesAxiom axiom) {
		profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
		
		return null;
	}

	public Object visit(OWLDisjointDataPropertiesAxiom axiom) {
		profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
		
		return null;
	}

	public Object visit(OWLDisjointUnionAxiom axiom) {
		profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
		
		return null;
	}

	public Object visit(OWLEquivalentClassesAxiom axiom) {
		for (OWLClassExpression ce : axiom.getClassExpressions()) {
			if (!isLDLPEquivalentClassExpression(ce)) {
				profileViolations.add(new UseOfNonEquivalentClassExpression(getCurrentOntology(), axiom, ce));
			}
		}
		return null;
	}

	public Object visit(OWLEquivalentDataPropertiesAxiom axiom) {
		profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
		return null;
	}

	public Object visit(OWLFunctionalDataPropertyAxiom axiom) {
		profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
		return null;
	}

	public Object visit(OWLHasKeyAxiom axiom) {
		profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
		return null;
	}

	public Object visit(OWLObjectPropertyDomainAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		if (!isLDLPSubObjectPropertyExpression(property)) {
			profileViolations.add(new UseOfNonLDLPSubPropertyExpression(getCurrentOntology(), axiom, property));
		}

		OWLClassExpression domain = axiom.getDomain();
		if (!isLDLPSuperClassExpression(domain)) {
			profileViolations.add(new UseOfNonSuperClassExpression(getCurrentOntology(), axiom, domain));
		}

		return null;
	}

	public Object visit(OWLObjectPropertyRangeAxiom axiom) {
		OWLObjectPropertyExpression property = axiom.getProperty();
		if (!isLDLPSubObjectPropertyExpression(property)) {
			profileViolations.add(new UseOfNonLDLPSubPropertyExpression(getCurrentOntology(), axiom, property));
		}

		OWLClassExpression range = axiom.getRange();
		if (!isLDLPSuperClassExpression(range)) {
			profileViolations.add(new UseOfNonSuperClassExpression(getCurrentOntology(), axiom, range));
		}

		return null;
	}

	public Object visit(OWLSubClassOfAxiom axiom) {
		if (!isLDLPSubClassExpression(axiom.getSubClass())) {
			profileViolations.add(new UseOfNonSubClassExpression(getCurrentOntology(), axiom, axiom.getSubClass()));
		}
		if (!isLDLPSuperClassExpression(axiom.getSuperClass())) {
			profileViolations.add(new UseOfNonSuperClassExpression(getCurrentOntology(), axiom, axiom.getSuperClass()));
		}
		return null;
	}

	@Override
	public Object visit(OWLSubObjectPropertyOfAxiom axiom) {
		if (!isLDLPSubObjectPropertyExpression(axiom.getSubProperty())) {
			profileViolations.add(new UseOfNonLDLPSubPropertyExpression(getCurrentOntology(), axiom, axiom.getSubProperty()));
		}
		if (!isLDLPSuperObjectPropertyExpression(axiom.getSuperProperty())) {
			profileViolations.add(new UseOfNonLDLPSuperObjectPropertyExpression(getCurrentOntology(), axiom, axiom.getSuperProperty()));
		}
		return null;
	}

	public Object visit(OWLSubDataPropertyOfAxiom axiom) {
		profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
		return null;
	}

	public Object visit(SWRLRule rule) {
		profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), rule));
		return super.visit(rule);
	}

	public Object visit(OWLDataComplementOf node) {
		profileViolations.add(new UseOfIllegalDataRange(getCurrentOntology(),getCurrentAxiom() ,node));
		return null;
	}

	public Object visit(OWLDataIntersectionOf node) {
		profileViolations.add(new UseOfIllegalDataRange(getCurrentOntology(), getCurrentAxiom(), node));
		return null;
	}

	public Object visit(OWLDataOneOf node) {
		profileViolations.add(new UseOfIllegalDataRange(getCurrentOntology(), getCurrentAxiom(), node));
		return null;
	}

	public Object visit(OWLDatatype node) {
		if (!allowedDatatypes.contains(node.getIRI())) {
			profileViolations.add(new UseOfIllegalDataRange(getCurrentOntology(), getCurrentAxiom(), node));
		}
		return null;
	}

	public Object visit(OWLDatatypeRestriction node) {
		profileViolations.add(new UseOfIllegalDataRange(getCurrentOntology(), getCurrentAxiom(), node));
		return null;
	}

	public Object visit(OWLDataUnionOf node) {
		profileViolations.add(new UseOfIllegalDataRange(getCurrentOntology(), getCurrentAxiom(), node));
		return null;
	}

	public Object visit(OWLDatatypeDefinitionAxiom axiom) {
		profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), getCurrentAxiom()));
		return null;
	}

	
	@Override
	public Object visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), getCurrentAxiom()));
		return null;
	}


	@Override
	public Object visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), getCurrentAxiom()));
		return null;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter#visit(org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom)
	 */
	@Override
	public Object visit(OWLSymmetricObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		return super.visit(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter#visit(org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom)
	 */
	@Override
	public Object visit(OWLTransitiveObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		return super.visit(axiom);
	}
}
