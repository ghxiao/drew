package org.semanticweb.drew.ldlp.reasoner;

import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.AxiomNotInProfileException;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.ClassExpressionNotInProfileException;
import org.semanticweb.owlapi.reasoner.FreshEntitiesException;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;
import org.semanticweb.owlapi.reasoner.UnsupportedEntailmentTypeException;
import org.semanticweb.owlapi.reasoner.impl.OWLReasonerBase;
import org.semanticweb.owlapi.util.Version;

public abstract class OWLReasonerAdapter extends OWLReasonerBase {

	public OWLReasonerAdapter(OWLOntology rootOntology, OWLReasonerConfiguration configuration,
			BufferingMode bufferingMode) {
		super(rootOntology, configuration, bufferingMode);
	}

	@Override
	protected void handleChanges(Set<OWLAxiom> addAxioms, Set<OWLAxiom> removeAxioms) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Node<OWLClass> getBottomClassNode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node<OWLDataProperty> getBottomDataPropertyNode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node<OWLObjectPropertyExpression> getBottomObjectPropertyNode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public NodeSet<OWLClass> getDataPropertyDomains(OWLDataProperty pe, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<OWLLiteral> getDataPropertyValues(OWLNamedIndividual ind, OWLDataProperty pe)
			throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		throw new UnsupportedOperationException();
	}

	@Override
	public NodeSet<OWLNamedIndividual> getDifferentIndividuals(OWLNamedIndividual ind)
			throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node<OWLClass> getEquivalentClasses(OWLClassExpression ce) throws InconsistentOntologyException,
			ClassExpressionNotInProfileException, FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Node<OWLDataProperty> getEquivalentDataProperties(OWLDataProperty pe) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NodeSet<OWLNamedIndividual> getInstances(OWLClassExpression ce, boolean direct)
			throws InconsistentOntologyException, ClassExpressionNotInProfileException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NodeSet<OWLClass> getObjectPropertyDomains(OWLObjectPropertyExpression pe, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NodeSet<OWLClass> getObjectPropertyRanges(OWLObjectPropertyExpression pe, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NodeSet<OWLNamedIndividual> getObjectPropertyValues(OWLNamedIndividual ind, OWLObjectPropertyExpression pe)
			throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getReasonerName() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Version getReasonerVersion() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Node<OWLNamedIndividual> getSameIndividuals(OWLNamedIndividual ind) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NodeSet<OWLClass> getSubClasses(OWLClassExpression ce, boolean direct) {
		throw new UnsupportedOperationException();

	}

	@Override
	public NodeSet<OWLDataProperty> getSubDataProperties(OWLDataProperty pe, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NodeSet<OWLClass> getSuperClasses(OWLClassExpression ce, boolean direct)
			throws InconsistentOntologyException, ClassExpressionNotInProfileException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NodeSet<OWLDataProperty> getSuperDataProperties(OWLDataProperty pe, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Node<OWLClass> getTopClassNode() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Node<OWLDataProperty> getTopDataPropertyNode() {
		throw new UnsupportedOperationException();

	}

	@Override
	public NodeSet<OWLClass> getTypes(OWLNamedIndividual ind, boolean direct) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Node<OWLClass> getUnsatisfiableClasses() throws ReasonerInterruptedException, TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void interrupt() {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isConsistent() throws ReasonerInterruptedException, TimeOutException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isEntailed(Set<? extends OWLAxiom> axioms) throws ReasonerInterruptedException,
			UnsupportedEntailmentTypeException, TimeOutException, AxiomNotInProfileException, FreshEntitiesException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isEntailmentCheckingSupported(AxiomType<?> axiomType) {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isSatisfiable(OWLClassExpression classExpression) throws ReasonerInterruptedException,
			TimeOutException, ClassExpressionNotInProfileException, FreshEntitiesException,
			InconsistentOntologyException {
		throw new UnsupportedOperationException();

	}

}