package org.semanticweb.drew.ldlp.profile;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitorEx;

//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyChainOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyIntersectionOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyOneOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyTransitiveClosureOf;
//import at.ac.tuwien.kr.owlapi.model.ldl.LDLObjectPropertyUnionOf;

public class LDLPSuperPropertyExpressionChecker implements OWLPropertyExpressionVisitorEx<Boolean> {

	@Override
	public Boolean visit(OWLObjectProperty property) {
		return true;

	}

	@Override
	public Boolean visit(OWLObjectInverseOf property) {
		
		return property.getInverse().accept(this);
		//return true;

	}

	@Override
	public Boolean visit(OWLDataProperty property) {
		return true;
		//return false;

	}

//	@Override
//	public Boolean visit(LDLObjectPropertyIntersectionOf property) {
//		for(OWLObjectPropertyExpression op:property.getOperands()){
//			if(!op.accept(this)){
//				return false;
//			}
//		}
//		return true;
//	}
//
//	@Override
//	public Boolean visit(LDLObjectPropertyUnionOf property) {
//		return false;
//	}
//
//	@Override
//	public Boolean visit(LDLObjectPropertyTransitiveClosureOf property) {
//		return false;
//	}
//
//	@Override
//	public Boolean visit(LDLObjectPropertyChainOf ldlObjectPropertyChainOfImpl) {
//		return false;
//	}
//
//	@Override
//	public Boolean visit(LDLObjectPropertyOneOf property) {
//		return false;
//	}

}
