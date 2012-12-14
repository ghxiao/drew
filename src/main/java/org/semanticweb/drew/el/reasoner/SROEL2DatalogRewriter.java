package org.semanticweb.drew.el.reasoner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.semanticweb.drew.dlprogram.format.DLProgramStorerImpl;
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
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntityVisitor;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

public class SROEL2DatalogRewriter extends OWLAxiomVisitorAdapter implements
		OWLEntityVisitor {

    private DLProgram datalog;

    public SROEL2DatalogRewriter() {
        SymbolEncoder<IRI> iriEncoder = DReWELManager.getInstance().getIRIEncoder();
        SymbolEncoder<OWLSubClassOfAxiom> superSomeAxiomEncoder = DReWELManager.getInstance()
                .getSuperSomeAxiomEncoder();
	}

	public DLProgram rewrite(OWLOntology ontology) {
		datalog = new DLProgram();

		ontology = new ELNormalization().normalize(ontology);

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

		// datalog.getClauses().addAll(PInst.getPInst());

		if (DReWELManager.getInstance().getDatalogFormat() == DatalogFormat.XSB) {
			datalog.addAll(PInst.getXsbDeclaration());
		}

		// iriEncoder.report();
		return datalog;
	}

	public void saveToFile(String datalogFile) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					datalogFile));
			writer.write(PInst.getPInst().toString());
			
			new DLProgramStorerImpl().store(datalog, writer);
			
			//writer.write(new DatalogToStringHelper().toString(datalog));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(OWLClassAssertionAxiom axiom) {
		// int c =
		// iriEncoder.encode(axiom.getClassExpression().asOWLClass().getIRI());
		// int a =
		// iriEncoder.encode(axiom.getIndividual().asOWLNamedIndividual().getIRI());
		// addFact(RewritingVocabulary.SUB_CLASS, a, c);
		//

		// TODO: CHECK!!
		// addFact(RewritingVocabulary.SUB_CLASS,//
		// axiom.getIndividual().asOWLNamedIndividual().getIRI(), //
		// axiom.getClassExpression().asOWLClass().getIRI());

		addFact(RewritingVocabulary.ISA,//
				axiom.getIndividual().asOWLNamedIndividual().getIRI(), //
				axiom.getClassExpression().asOWLClass().getIRI());
	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom) {
		// int p =
		// iriEncoder.encode(axiom.getProperty().asOWLObjectProperty().getIRI());
		// int s =
		// iriEncoder.encode(axiom.getSubject().asOWLNamedIndividual().getIRI());
		// int o =
		// iriEncoder.encode(axiom.getObject().asOWLNamedIndividual().getIRI());
		//
		// addFact(RewritingVocabulary.SUP_EX, s, p, o, o);

		// TODO: CHECK!!!
		// addFact(RewritingVocabulary.SUP_EX, //
		// axiom.getSubject().asOWLNamedIndividual().getIRI(), //
		// axiom.getProperty().asOWLObjectProperty().getIRI(), //
		// axiom.getObject().asOWLNamedIndividual().getIRI(), //
		// axiom.getObject().asOWLNamedIndividual().getIRI() //
		// );

		addFact(RewritingVocabulary.TRIPLE, //
				axiom.getSubject().asOWLNamedIndividual().getIRI(), //
				axiom.getProperty().asOWLObjectProperty().getIRI(), //
				axiom.getObject().asOWLNamedIndividual().getIRI() //
		);
	}

	@Override
	public void visit(OWLSubPropertyChainOfAxiom axiom) {
		List<OWLObjectPropertyExpression> chain = axiom.getPropertyChain();
		Iterator<OWLObjectPropertyExpression> iterator = chain.iterator();
		// int r1 =
		// iriEncoder.encode(iterator.next().asOWLObjectProperty().getIRI());
		// int r2 =
		// iriEncoder.encode(iterator.next().asOWLObjectProperty().getIRI());
		// int r =
		// iriEncoder.encode(axiom.getSuperProperty().asOWLObjectProperty().getIRI());
		//
		// addFact(RewritingVocabulary.SUB_R_CHAIN, r1, r2, r);
		addFact(RewritingVocabulary.SUB_R_CHAIN, //
				iterator.next().asOWLObjectProperty().getIRI(),//
				iterator.next().asOWLObjectProperty().getIRI(),//
				axiom.getSuperProperty().asOWLObjectProperty().getIRI()//
		);
	}

	@Override
	public void visit(OWLSubClassOfAxiom axiom) {
		OWLClassExpression subClass = axiom.getSubClass();
		OWLClassExpression superClass = axiom.getSuperClass();

		// simple case: A subclass C
		if ((subClass.getClassExpressionType() == ClassExpressionType.OWL_CLASS)
				&& (superClass.getClassExpressionType() == ClassExpressionType.OWL_CLASS)) {
			// int a =
			// iriEncoder.encode(axiom.getSubClass().asOWLClass().getIRI());
			// int c =
			// iriEncoder.encode(axiom.getSuperClass().asOWLClass().getIRI());

			if (subClass.isOWLThing()) {
				// addFact(RewritingVocabulary.TOP, c);
				addFact(RewritingVocabulary.TOP, axiom.getSuperClass()
						.asOWLClass().getIRI());
			} else if (superClass.isOWLNothing()) {
				// addFact(RewritingVocabulary.BOT, a);
				addFact(RewritingVocabulary.BOT, axiom.getSubClass()
						.asOWLClass().getIRI());
			} else /* (!subClass.isOWLNothing() && !superClass.isOWLThing()) */{
				// addFact(RewritingVocabulary.SUB_CLASS, a, c);
				addFact(RewritingVocabulary.SUB_CLASS, //
						subClass.asOWLClass().getIRI(), superClass.asOWLClass()
								.getIRI());
			}
		}

		// A' subclass C
		else if (superClass.getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
			// and(A', B) subclass C
			// and(B, A') subclass C
			// -> {A' subclass D, and(D,C) subclass B}
			if (subClass.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
				// TODO: dealing with the case when size(ops) != 2
				// OWLObjectIntersectionOf inter = (OWLObjectIntersectionOf)
				// subClass;
				// Set<OWLClassExpression> operands = inter.getOperands();
				// int[] params = new int[3];
				// int i = 0;
				// for (OWLClassExpression op : operands) {
				// params[i++] = iriEncoder.encode(op.asOWLClass().getIRI());
				// }
				// params[2] =
				// iriEncoder.encode(superClass.asOWLClass().getIRI());
				// addFact(RewritingVocabulary.SUB_CONJ, params);
				OWLObjectIntersectionOf inter = (OWLObjectIntersectionOf) subClass;
				Set<OWLClassExpression> operands = inter.getOperands();
				IRI[] params = new IRI[3];
				int i = 0;
				for (OWLClassExpression op : operands) {
					params[i++] = op.asOWLClass().getIRI();
				}
				params[2] = superClass.asOWLClass().getIRI();
				addFact(RewritingVocabulary.SUB_CONJ, params);
			}

			// exist(R,A') subclass B
			else if (subClass.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
				OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) subClass;

				// int r =
				// iriEncoder.encode(some.getProperty().asOWLObjectProperty().getIRI());
				// int a =
				// iriEncoder.encode(some.getFiller().asOWLClass().getIRI());
				// int b = iriEncoder.encode(superClass.asOWLClass().getIRI());
				//
				// addFact(RewritingVocabulary.SUB_EX, r, a, b);
				addFact(RewritingVocabulary.SUB_EX,//
						some.getProperty().asOWLObjectProperty().getIRI(),//
						some.getFiller().asOWLClass().getIRI(),//
						superClass.asOWLClass().getIRI());

			} else if (subClass.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
				throw new IllegalStateException();
			} else if (subClass.getClassExpressionType() == ClassExpressionType.OBJECT_ONE_OF) {
				// {c} ⊑ A -> SubClass(c, A)
				// OWLObjectOneOf oneOf = (OWLObjectOneOf) subClass;
				// int c =
				// iriEncoder.encode(oneOf.getIndividuals().iterator().next().asOWLNamedIndividual().getIRI());
				// int a = iriEncoder.encode(superClass.asOWLClass().getIRI());
				// addFact(RewritingVocabulary.SUB_CLASS, c, a);
				OWLObjectOneOf oneOf = (OWLObjectOneOf) subClass;
				addFact(RewritingVocabulary.SUB_CLASS, //
						oneOf.getIndividuals().iterator().next()
								.asOWLNamedIndividual().getIRI(),//
						superClass.asOWLClass().getIRI());

			} else if (subClass.getClassExpressionType() == ClassExpressionType.OBJECT_HAS_SELF) {
				// OWLObjectHasSelf self = (OWLObjectHasSelf) subClass;
				// int r = DReWELManager.getInstance().getIRIEncoder()
				// .encode(self.getProperty().asOWLObjectProperty().getIRI());
				// int a =
				// DReWELManager.getInstance().getIRIEncoder().encode(superClass.asOWLClass().getIRI());
				// addFact(RewritingVocabulary.SUB_SELF, r, a);
				OWLObjectHasSelf self = (OWLObjectHasSelf) subClass;
				addFact(RewritingVocabulary.SUB_SELF,//
						self.getProperty().asOWLObjectProperty().getIRI(),//
						superClass.asOWLClass().getIRI());
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
				// datalog.getClauses().add(
				// new Clause(new Literal[] { new Literal( //
				// RewritingVocabulary.SUP_EX, //
				// CacheManager.getInstance().getConstant(
				// iriEncoder.encode(subClass.asOWLClass().getIRI())),//
				// CacheManager.getInstance().getConstant(
				// iriEncoder.encode(some.getProperty().asOWLObjectProperty().getIRI())),//
				// CacheManager.getInstance().getConstant(
				// iriEncoder.encode(some.getFiller().asOWLClass().getIRI())),//
				// CacheManager.getInstance().getConstant("e" +
				// superSomeAxiomEncoder.encode(axiom))//
				// ) }, //
				// new Literal[] {}));
				datalog.add(new Clause(new Literal[] { new Literal( //
						RewritingVocabulary.SUP_EX, //
						CacheManager.getInstance().getConstant(
								subClass.asOWLClass().getIRI()),//
						CacheManager.getInstance().getConstant(
								some.getProperty().asOWLObjectProperty()
										.getIRI()),//
						CacheManager.getInstance().getConstant(
								some.getFiller().asOWLClass().getIRI()),//
						// CacheManager.getInstance().getConstant("e" +
						// superSomeAxiomEncoder.encode(axiom))//
						CacheManager.getInstance().getConstant(
								"e^{"
										+ subClass.asOWLClass().getIRI()
												.getFragment()
										+ "->" //
										+ some.getProperty()
												.asOWLObjectProperty().getIRI()
												.getFragment()
										+ "_SOME_" //
										+ some.getFiller().asOWLClass()
												.getIRI().getFragment() + "}"
						// superSomeAxiomEncoder.encode(axiom)
								)//
				) }, //
						new Literal[] {}));

			} else if (superClass.getClassExpressionType() == ClassExpressionType.OBJECT_ONE_OF) {
				// A ⊑ {c} -> SubClass(A, c)
				// OWLObjectOneOf oneOf = (OWLObjectOneOf) superClass;
				// int c =
				// iriEncoder.encode(oneOf.getIndividuals().iterator().next().asOWLNamedIndividual().getIRI());
				// int a = iriEncoder.encode(subClass.asOWLClass().getIRI());
				// addFact(RewritingVocabulary.SUB_CLASS, a, c);
				OWLObjectOneOf oneOf = (OWLObjectOneOf) superClass;
				addFact(RewritingVocabulary.SUB_CLASS, //
						subClass.asOWLClass().getIRI(),//
						oneOf.getIndividuals().iterator().next()
								.asOWLNamedIndividual().getIRI());

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
				// OWLObjectHasSelf self = (OWLObjectHasSelf) superClass;
				// int r = DReWELManager.getInstance().getIRIEncoder()
				// .encode(self.getProperty().asOWLObjectProperty().getIRI());
				// int a =
				// DReWELManager.getInstance().getIRIEncoder().encode(subClass.asOWLClass().getIRI());
				// addFact(RewritingVocabulary.SUP_SELF, a, r);
				OWLObjectHasSelf self = (OWLObjectHasSelf) superClass;
				addFact(RewritingVocabulary.SUP_SELF, //
						subClass.asOWLClass().getIRI(), //
						self.getProperty().asOWLObjectProperty().getIRI());
			} else {
				throw new IllegalStateException();
			}
		}

		super.visit(axiom);
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {
		// int r =
		// iriEncoder.encode(axiom.getSubProperty().asOWLObjectProperty().getIRI());
		// int s =
		// iriEncoder.encode(axiom.getSuperProperty().asOWLObjectProperty().getIRI());
		//
		// addFact(RewritingVocabulary.SUB_ROLE, r, s);
		addFact(RewritingVocabulary.SUB_ROLE, //
				axiom.getSubProperty().asOWLObjectProperty().getIRI(),//
				axiom.getSuperProperty().asOWLObjectProperty().getIRI());
	}

	protected void addFact(NormalPredicate predicate, int... params) {
		List<Term> terms = new ArrayList<>();

		for (int param : params) {
			terms.add(CacheManager.getInstance().getConstant(param));
		}

		datalog.add(new Clause(new Literal[] { new Literal(predicate, terms
                .toArray(new Term[terms.size()])) }, new Literal[] {}));
	}

	@Override
	public void visit(OWLClass cls) {
		// int c = iriEncoder.encode(cls.getIRI());
		// addFact(RewritingVocabulary.CLASS, c);
		addFact(RewritingVocabulary.CLASS, cls.getIRI());
	}

	@Override
	public void visit(OWLObjectProperty property) {
		// int r = iriEncoder.encode(property.getIRI());
		// addFact(RewritingVocabulary.ROLE, r);
		addFact(RewritingVocabulary.ROLE, property.getIRI());
	}

	@Override
	public void visit(OWLDataProperty property) {
		// do nothing
	}

	@Override
	public void visit(OWLNamedIndividual individual) {
		// int i = iriEncoder.encode(individual.getIRI());
		// addFact(RewritingVocabulary.NOM, i);
		addFact(RewritingVocabulary.NOM, individual.getIRI());
	}

	@Override
	public void visit(OWLDatatype datatype) {
		// do nothing
	}

	@Override
	public void visit(OWLAnnotationProperty property) {
		// do nothing
	}

	@Override
	public void visit(OWLDataPropertyDomainAxiom axiom) {
		System.err.println("warning! ignore axiom " + axiom);

		// throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataPropertyRangeAxiom axiom) {
		System.err.println("warning! ignore axiom " + axiom);

		// throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom) {
		// int p =
		// iriEncoder.encode(axiom.getProperty().asOWLObjectProperty().getIRI());
		// int d = iriEncoder.encode(axiom.getDomain().asOWLClass().getIRI());
		// addFact(RewritingVocabulary.DOMAIN, p, d);
		addFact(RewritingVocabulary.DOMAIN, //
				axiom.getProperty().asOWLObjectProperty().getIRI(),//
				axiom.getDomain().asOWLClass().getIRI());

	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		// int p =
		// iriEncoder.encode(axiom.getProperty().asOWLObjectProperty().getIRI());
		// int r = iriEncoder.encode(axiom.getRange().asOWLClass().getIRI());
		// addFact(RewritingVocabulary.RANGE, p, r);
		addFact(RewritingVocabulary.RANGE, //
				axiom.getProperty().asOWLObjectProperty().getIRI(),//
				axiom.getRange().asOWLClass().getIRI());

	}

	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
		System.err.println("warning! ignore axiom " + axiom);

		// throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom) {
		// int p =
		// iriEncoder.encode(axiom.getProperty().asOWLDataProperty().getIRI());
		// int s =
		// iriEncoder.encode(axiom.getSubject().asOWLNamedIndividual().getIRI());
		// OWLLiteral object = axiom.getObject();
		// object.isRDFPlainLiteral();
		//
		// int o = iriEncoder.encode(axiom.getObject());
		//
		// addFact(RewritingVocabulary.SUP_EX, s, p, o, o);

		// System.err.println("warning: ignore axiom " + axiom);

		datalog.add(
				new Clause(new Literal[] { new Literal( //
						RewritingVocabulary.SUP_EX, //
						CacheManager.getInstance().getConstant(
								axiom.getSubject().asOWLNamedIndividual()
										.getIRI()),//
						CacheManager.getInstance().getConstant(
								axiom.getProperty().asOWLDataProperty()
										.getIRI()),//
						CacheManager.getInstance().getConstant(
								axiom.getObject().getLiteral()),//
						CacheManager.getInstance().getConstant(
								axiom.getObject().getLiteral())//
				) }, //
						new Literal[] {}));

	}

	private void addFact(NormalPredicate predicate, IRI... params) {

		List<Term> terms = new ArrayList<>();

		for (IRI param : params) {
			terms.add(CacheManager.getInstance().getConstant(param));
		}

		datalog.add(
				new Clause(new Literal[] { new Literal(predicate, terms
                        .toArray(new Term[terms.size()])) }, new Literal[] {}));
	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		System.err.println("warning: ignore axiom " + axiom);
	}

}
