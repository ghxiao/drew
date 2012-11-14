package org.semanticweb.drew.ldlp.profile;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitorEx;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;

class LDLPEquivalentClassExpressionChecker implements OWLClassExpressionVisitorEx<Boolean> {

	//TODO: Need more work here
	
    public Boolean visit(OWLClass desc) {
        return !desc.isOWLThing();
    }

    public Boolean visit(OWLObjectIntersectionOf desc) {
        for(OWLClassExpression ce : desc.getOperands()) {
            if(!ce.accept(this)) {
                return false;
            }
        }
        return true;
    }

    public Boolean visit(OWLObjectUnionOf desc) {
        return false;
    }

    public Boolean visit(OWLObjectComplementOf desc) {
        return false;
    }

    public Boolean visit(OWLObjectSomeValuesFrom desc) {
        return false;
    }

    public Boolean visit(OWLObjectAllValuesFrom desc) {
        return false;
    }

    public Boolean visit(OWLObjectHasValue desc) {
        return true;
    }

    public Boolean visit(OWLObjectMinCardinality desc) {
        return false;
    }

    public Boolean visit(OWLObjectExactCardinality desc) {
        return false;
    }

    public Boolean visit(OWLObjectMaxCardinality desc) {
        return false;
    }

    public Boolean visit(OWLObjectHasSelf desc) {
        return false;
    }

    public Boolean visit(OWLObjectOneOf desc) {
        return false;
    }

    public Boolean visit(OWLDataSomeValuesFrom desc) {
        return false;
    }

    public Boolean visit(OWLDataAllValuesFrom desc) {
        return false;
    }

    public Boolean visit(OWLDataHasValue desc) {
        return true;
    }

    public Boolean visit(OWLDataMinCardinality desc) {
        return false;
    }

    public Boolean visit(OWLDataExactCardinality desc) {
        return false;
    }

    public Boolean visit(OWLDataMaxCardinality desc) {
        return false;
    }
}