package org.semanticweb.drew.ldlp.reasoner;

import java.util.List;
import java.util.Set;

import org.semanticweb.drew.datalog.DLVReasoner;
import org.semanticweb.drew.datalog.DatalogReasoner;
//import org.semanticweb.drew.datalog.XSBDatalogReasoner;
import org.semanticweb.drew.datalog.DatalogReasoner.TYPE;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.rl.sparql.SparqlCompiler;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.AxiomNotInProfileException;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.FreshEntitiesException;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.TimeOutException;
import org.semanticweb.owlapi.reasoner.UnsupportedEntailmentTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.Query;

class LDLPReasoner extends OWLReasonerAdapter {
	private final static Logger logger = LoggerFactory
			.getLogger(LDLPClosureCompiler.class);

	private List<ProgramStatement> program;

	private boolean compiled;

	private LDLPOntologyCompiler ontologyCompiler;

	private LDLPAxiomCompiler axiomCompiler = new LDLPAxiomCompiler();

	private DatalogReasoner datalogReasoner;

	private LDLPReasoner(OWLOntology rootOntology,
                         OWLReasonerConfiguration configuration, BufferingMode bufferingMode) {
		super(rootOntology, configuration, bufferingMode);
		ontologyCompiler = new LDLPOntologyCompiler();
		// datalogReasoner = new XSBDatalogReasoner();
	}

	public LDLPReasoner(OWLOntology rootOntology) {
		this(rootOntology, TYPE.DLV);
	}

	private LDLPReasoner(OWLOntology rootOntology, DatalogReasoner.TYPE type) {

		this(rootOntology, new SimpleConfiguration(), null);
		//if (type == TYPE.DLV) {
			datalogReasoner = new DLVReasoner();
//		} else if (type == TYPE.XSB) {
//			datalogReasoner = new XSBDatalogReasoner();
//		}
	}

	// public OWLIndividual query(OWLClass cls) {
	// program = compiler.complile(this.getRootOntology());
	// final String predicate =
	// LDLPCompilerManager.getInstance().getPredicate(cls);
	//
	//
	// }

	@Override
	public boolean isEntailed(OWLAxiom axiom)
			throws ReasonerInterruptedException,
			UnsupportedEntailmentTypeException, TimeOutException,
			AxiomNotInProfileException, FreshEntitiesException {

		if (!(axiom instanceof OWLClassAssertionAxiom))
			throw new UnsupportedOperationException();

		OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) axiom;

		final OWLIndividual individual = classAssertionAxiom.getIndividual();

		final OWLOntology rootOntology = super.getRootOntology();

		final OWLOntologyManager manager = rootOntology.getOWLOntologyManager();

		if (!compiled) {
			logger.debug("Program Compiling Started");
			program = ontologyCompiler.complile(this.getRootOntology());
			logger.debug("Program Compiling Finished");
			compiled = true;
		}

		Clause query = (Clause)axiomCompiler.compileOWLAxiom(axiom);
		// compiler.compileClassAssertionAxiom(classAssertionAxiom);

		return datalogReasoner.isEntailed(program, query.getHead());

	}

	/**
	 * Conjunctive query <br/>
	 * eg. ans(X):-A(X,c), B(X,Y).
	 * 
	 * @param q
	 *            The conjunctive query
	 * @return
	 */
	public List<Literal> query(Clause q) {
		program = ontologyCompiler.complile(this.getRootOntology());

		LDLPQueryCompiler queryComiler = new LDLPQueryCompiler();
		Clause query = queryComiler.compileQuery(q);

		program.add(query);

		List<Literal> queryResult = datalogReasoner.query(program,
				query.getHead());

		LDLPQueryResultDecompiler decompiler = new LDLPQueryResultDecompiler();

		List<Literal> result = decompiler.decompileLiterals(queryResult);
		return result;
	}

	@Override
	public void precomputeInferences(InferenceType... inferenceTypes)
			throws ReasonerInterruptedException, TimeOutException,
			InconsistentOntologyException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPrecomputed(InferenceType inferenceType) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<InferenceType> getPrecomputableInferenceTypes() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public NodeSet<OWLClass> getDisjointClasses(OWLClassExpression ce)
			throws ReasonerInterruptedException, TimeOutException,
			FreshEntitiesException, InconsistentOntologyException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public NodeSet<OWLObjectPropertyExpression> getDisjointObjectProperties(
			OWLObjectPropertyExpression pe)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public NodeSet<OWLDataProperty> getDisjointDataProperties(
			OWLDataPropertyExpression pe) throws InconsistentOntologyException,
			FreshEntitiesException, ReasonerInterruptedException,
			TimeOutException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Node<OWLObjectPropertyExpression> getTopObjectPropertyNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLObjectPropertyExpression> getSubObjectProperties(
			OWLObjectPropertyExpression pe, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeSet<OWLObjectPropertyExpression> getSuperObjectProperties(
			OWLObjectPropertyExpression pe, boolean direct)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLObjectPropertyExpression> getEquivalentObjectProperties(
			OWLObjectPropertyExpression pe)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node<OWLObjectPropertyExpression> getInverseObjectProperties(
			OWLObjectPropertyExpression pe)
			throws InconsistentOntologyException, FreshEntitiesException,
			ReasonerInterruptedException, TimeOutException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * answer SPARQL queries
	 * 
	 * @param query
	 * @return
	 */
	public List<Literal> executeQuery(Query query) {
		if (!compiled) {
			logger.debug("Program Compiling Started");
			program = ontologyCompiler.complile(this.getRootOntology());
			logger.debug("Program Compiling Finished");
			compiled = true;
		}

		SparqlCompiler sparqlCompiler = new SparqlCompiler();
		Clause datalogQuery = sparqlCompiler.compileQuery(query);
		System.out.println(datalogQuery);
		return query(datalogQuery);
	}

}
