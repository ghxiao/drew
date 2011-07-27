package org.semanticweb.drew.el.reasoner;

import org.semanticweb.drew.dlprogram.model.DLInputSignature;
import org.semanticweb.drew.el.SymbolEncoder;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class DReWELManager {
	static private DReWELManager instance = new DReWELManager();

	// private SymbolEncoder<OWLClass> owlClassEncoder;
	// private SymbolEncoder<OWLObjectPropertyExpression>
	// owlObjectPropertyExpressionEncoder;
	// private SymbolEncoder<OWLDataProperty> owlDataPropertyEncoder;
	// private SymbolEncoder<OWLIndividual> owlIndividualEncoder;
	private int thing;
	private int nothing;
	private int topProperty;
	private int bottomProperty;

	private int verboseLevel;

	private NamingStrategy namingStrategy;

	private SymbolEncoder<IRI> iriEncoder;

	private SymbolEncoder<OWLSubClassOfAxiom> superSomeAxiomEncoder;

	private SymbolEncoder<DLInputSignature> dlInputSignatureEncoder;

	public SymbolEncoder<OWLSubClassOfAxiom> getSuperSomeAxiomEncoder() {
		return superSomeAxiomEncoder;
	}

	public void setSuperSomeAxiomEncoder(SymbolEncoder<OWLSubClassOfAxiom> superSomeAxiomEncoder) {
		this.superSomeAxiomEncoder = superSomeAxiomEncoder;
	}

	private DReWELManager() {
		this.iriEncoder = new SymbolEncoder<IRI>();
		this.superSomeAxiomEncoder = new SymbolEncoder<OWLSubClassOfAxiom>();
		this.dlInputSignatureEncoder = new SymbolEncoder<DLInputSignature>();
		this.namingStrategy = NamingStrategy.IntEncoding;
		// this.owlClassEncoder = new SymbolEncoder<OWLClass>(OWLClass.class);
		// this.owlObjectPropertyExpressionEncoder = new
		// SymbolEncoder<OWLObjectPropertyExpression>(
		// OWLObjectPropertyExpression.class);
		// this.owlDataPropertyEncoder = new
		// SymbolEncoder<OWLDataProperty>(OWLDataProperty.class);
		// this.owlIndividualEncoder = new
		// SymbolEncoder<OWLIndividual>(OWLIndividual.class);
		// this.thing =
		// owlClassEncoder.getValueBySymbol(OWLManager.getOWLDataFactory().getOWLThing());
		// this.nothing =
		// owlClassEncoder.getValueBySymbol(OWLManager.getOWLDataFactory().getOWLNothing());
		// this.topProperty =
		// owlObjectPropertyExpressionEncoder.getValueBySymbol(OWLManager.getOWLDataFactory()
		// .getOWLTopObjectProperty());
		// this.bottomProperty =
		// owlObjectPropertyExpressionEncoder.getValueBySymbol(OWLManager.getOWLDataFactory()
		// .getOWLBottomObjectProperty());
	}

	public static DReWELManager getInstance() {
		return instance;
	}

	public int getThing() {
		return thing;
	}

	public int getNothing() {
		return nothing;
	}

	public int getTopProperty() {
		return topProperty;
	}

	public int getBottomProperty() {
		return bottomProperty;
	}

	public int getVerboseLevel() {
		return verboseLevel;
	}

	public void setVerboseLevel(int verboseLevel) {
		this.verboseLevel = verboseLevel;
	}

	public NamingStrategy getNamingStrategy() {
		return namingStrategy;
	}

	public void setNamingStrategy(NamingStrategy namingStrategy) {
		this.namingStrategy = namingStrategy;
	}

	public SymbolEncoder<IRI> getIriEncoder() {
		return iriEncoder;
	}

	public void setIriEncoder(SymbolEncoder<IRI> iriEncoder) {
		this.iriEncoder = iriEncoder;
	}

	public SymbolEncoder<DLInputSignature> getDlInputSignatureEncoder() {
		return dlInputSignatureEncoder;
	}

	public void setDlInputSignatureEncoder(SymbolEncoder<DLInputSignature> dlInputSignatureEncoder) {
		this.dlInputSignatureEncoder = dlInputSignatureEncoder;
	}

}
