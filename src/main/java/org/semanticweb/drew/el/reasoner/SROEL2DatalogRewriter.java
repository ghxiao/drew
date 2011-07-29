package org.semanticweb.drew.el.reasoner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.el.SymbolEncoder;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntityVisitor;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

public class SROEL2DatalogRewriter extends OWLAxiomVisitorAdapter implements OWLEntityVisitor {

	private SymbolEncoder<IRI> iriEncoder;

	private DLProgram datalog;
	private SymbolEncoder<OWLSubClassOfAxiom> superSomeAxiomEncoder;

	public SROEL2DatalogRewriter() {
		this.iriEncoder = DReWELManager.getInstance().getIriEncoder();
		this.superSomeAxiomEncoder = DReWELManager.getInstance().getSuperSomeAxiomEncoder();
	}

	public DLProgram rewrite(OWLOntology ontology) {
		datalog = new DLProgram();

		ontology = new ELNormalizer().normalize(ontology);

		for (OWLLogicalAxiom ax : ontology.getLogicalAxioms()) {
			ax.accept(this);
		}

		for (OWLNamedIndividual i : ontology.getIndividualsInSignature()) {
			i.accept(this);
		}

		for (OWLObjectProperty p : ontology.getObjectPropertiesInSignature()) {
			p.accept(this);
		}

		for (OWLClass cls : ontology.getClassesInSignature()) {
			cls.accept(this);
		}

		datalog.getClauses().addAll(PInst.getPInst().getClauses());

		// iriEncoder.report();
		return datalog;
	}

	public void saveToFile(String datalogFile) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(datalogFile));
			writer.write(PInst.getPInst().toString());
			writer.write(new DatalogToStringHelper().toString(datalog));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(OWLClassAssertionAxiom axiom) {
		int c = iriEncoder.encode(axiom.getClassExpression().asOWLClass().getIRI());
		int a = iriEncoder.encode(axiom.getIndividual().asOWLNamedIndividual().getIRI());
		addFact(RewritingVocabulary.SUB_CLASS, a, c);
	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom) {
		int p = iriEncoder.encode(axiom.getProperty().asOWLObjectProperty().getIRI());
		int s = iriEncoder.encode(axiom.getSubject().asOWLNamedIndividual().getIRI());
		int o = iriEncoder.encode(axiom.getObject().asOWLNamedIndividual().getIRI());

		addFact(RewritingVocabulary.SUP_EX, s, p, o, o);
	}

	@Override
	public void visit(OWLSubPropertyChainOfAxiom axiom) {
		List<OWLObjectPropertyExpression> chain = axiom.getPropertyChain();
		Iterator<OWLObjectPropertyExpression> iterator = chain.iterator();
		int r1 = iriEncoder.encode(iterator.next().asOWLObjectProperty().getIRI());
		int r2 = iriEncoder.encode(iterator.next().asOWLObjectProperty().getIRI());
		int r = iriEncoder.encode(axiom.getSuperProperty().asOWLObjectProperty().getIRI());
		
		addFact(RewritingVocabulary.SUB_R_CHAIN, r1, r2, r);
		
		super.visit(axiom);
	}

	@Override
	public void visit(OWLSubClassOfAxiom axiom) {
		OWLClassExpression subClass = axiom.getSubClass();
		OWLClassExpression superClass = axiom.getSuperClass();

		// simple case: A subclass C
		if ((subClass.getClassExpressionType() == ClassExpressionType.OWL_CLASS)
				&& (superClass.getClassExpressionType() == ClassExpressionType.OWL_CLASS)) {
			int a = iriEncoder.encode(axiom.getSubClass().asOWLClass().getIRI());
			int c = iriEncoder.encode(axiom.getSuperClass().asOWLClass().getIRI());

			if (subClass.isOWLThing()) {
				addFact(RewritingVocabulary.TOP, c);
			} else if (superClass.isOWLNothing()) {
				addFact(RewritingVocabulary.BOT, a);
			} else /* (!subClass.isOWLNothing() && !superClass.isOWLThing()) */{
				addFact(RewritingVocabulary.SUB_CLASS, a, c);
			}
		}

		// A' subclass C
		else if (superClass.getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
			// and(A', B) subclass C
			// and(B, A') subclass C
			// -> {A' subclass D, and(D,C) subclass B}
			if (subClass.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
				// TODO: dealing with the case when size(ops) != 2
				OWLObjectIntersectionOf inter = (OWLObjectIntersectionOf) subClass;
				Set<OWLClassExpression> operands = inter.getOperands();
				int[] params = new int[3];
				int i = 0;
				for (OWLClassExpression op : operands) {
					params[i++] = iriEncoder.encode(op.asOWLClass().getIRI());
				}
				params[2] = iriEncoder.encode(superClass.asOWLClass().getIRI());
				addFact(RewritingVocabulary.SUB_CONJ, params);
			}

			// exist(R,A') subclass B
			else if (subClass.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
				OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) subClass;

				int r = iriEncoder.encode(some.getProperty().asOWLObjectProperty().getIRI());
				int a = iriEncoder.encode(some.getFiller().asOWLClass().getIRI());
				int b = iriEncoder.encode(superClass.asOWLClass().getIRI());

				addFact(RewritingVocabulary.SUB_EX, r, a, b);
			} else if (subClass.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
				throw new IllegalStateException();
			} else if (subClass.getClassExpressionType() == ClassExpressionType.OBJECT_ONE_OF) {
				// {c} ⊑ A -> SubClass(c, A)
				OWLObjectOneOf oneOf = (OWLObjectOneOf) subClass;
				int c = iriEncoder.encode(oneOf.getIndividuals().iterator().next().asOWLNamedIndividual().getIRI());
				int a = iriEncoder.encode(superClass.asOWLClass().getIRI());
				addFact(RewritingVocabulary.SUB_CLASS, c, a);
			} else if (subClass.getClassExpressionType() == ClassExpressionType.OBJECT_HAS_SELF) {
				OWLObjectHasSelf self = (OWLObjectHasSelf) subClass;
				int r = DReWELManager.getInstance().getIriEncoder()
						.encode(self.getProperty().asOWLObjectProperty().getIRI());
				int a = DReWELManager.getInstance().getIriEncoder().encode(superClass.asOWLClass().getIRI());
				addFact(RewritingVocabulary.SUB_SELF, r, a);
			} else {
				throw new IllegalStateException();
			}

		}
		// A subclass C'
		else if (subClass.getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
			// (A -> or(B, C)) ~>
			if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
				throw new IllegalStateException();
			}

			// A subclass and(B, C)
			else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
				throw new IllegalStateException();
			}
			// A subclass exists(R, C') -> {A subclass some(R,D), D subclass C'}
			else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
				OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) superClass;
				// A ⊑ ∃R.B → {supEx(A, R, B, auxA⊑∃R.B )}
				datalog.getClauses().add(
						new Clause(new Literal[] { new Literal( //
								RewritingVocabulary.SUP_EX, //
								CacheManager.getInstance().getConstant(
										iriEncoder.encode(subClass.asOWLClass().getIRI())),//
								CacheManager.getInstance().getConstant(
										iriEncoder.encode(some.getProperty().asOWLObjectProperty().getIRI())),//
								CacheManager.getInstance().getConstant(
										iriEncoder.encode(some.getFiller().asOWLClass().getIRI())),//
								CacheManager.getInstance().getConstant("e" + superSomeAxiomEncoder.encode(axiom))//
						) }, //
								new Literal[] {}));

			} else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_ONE_OF) {
				// A ⊑ {c} -> SubClass(A, c)
				OWLObjectOneOf oneOf = (OWLObjectOneOf) superClass;
				int c = iriEncoder.encode(oneOf.getIndividuals().iterator().next().asOWLNamedIndividual().getIRI());
				int a = iriEncoder.encode(subClass.asOWLClass().getIRI());
				addFact(RewritingVocabulary.SUB_CLASS, a, c);
			}
			// A subclass all(R, C')
			else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_ALL_VALUES_FROM) {
				throw new IllegalStateException();
			}
			// A subclass max(R, 1, B')
			else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_MAX_CARDINALITY) {
				throw new IllegalStateException();
			} else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_MIN_CARDINALITY) {
				throw new IllegalStateException();
			}

			// A subclass not(B)
			else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_COMPLEMENT_OF) {
				throw new IllegalStateException();

			} else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_HAS_SELF) {
				OWLObjectHasSelf self = (OWLObjectHasSelf) superClass;
				int r = DReWELManager.getInstance().getIriEncoder()
						.encode(self.getProperty().asOWLObjectProperty().getIRI());
				int a = DReWELManager.getInstance().getIriEncoder().encode(subClass.asOWLClass().getIRI());
				addFact(RewritingVocabulary.SUP_SELF, a, r);
			} else {
				throw new IllegalStateException();
			}
		}

		super.visit(axiom);
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {
		int r = iriEncoder.encode(axiom.getSubProperty().asOWLObjectProperty().getIRI());
		int s = iriEncoder.encode(axiom.getSuperProperty().asOWLObjectProperty().getIRI());

		addFact(RewritingVocabulary.SUB_ROLE, r, s);
	}

	protected boolean addFact(NormalPredicate predicate, int... params) {
		List<Term> terms = new ArrayList<Term>();

		for (int param : params) {
			terms.add(CacheManager.getInstance().getConstant(param));
		}

		return datalog.getClauses().add(
				new Clause(new Literal[] { new Literal(predicate, terms.toArray(new Term[0])) }, new Literal[] {}));
	}

	@Override
	public void visit(OWLClass cls) {
		int c = iriEncoder.encode(cls.getIRI());
		addFact(RewritingVocabulary.CLASS, c);
	}

	@Override
	public void visit(OWLObjectProperty property) {
		int r = iriEncoder.encode(property.getIRI());
		addFact(RewritingVocabulary.ROLE, r);
	}

	@Override
	public void visit(OWLDataProperty property) {

	}

	@Override
	public void visit(OWLNamedIndividual individual) {
		int i = iriEncoder.encode(individual.getIRI());
		addFact(RewritingVocabulary.NOM, i);
	}

	@Override
	public void visit(OWLDatatype datatype) {

	}

	@Override
	public void visit(OWLAnnotationProperty property) {

	}

}
