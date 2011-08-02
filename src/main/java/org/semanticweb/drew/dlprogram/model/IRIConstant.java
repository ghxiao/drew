package org.semanticweb.drew.dlprogram.model;

import org.semanticweb.owlapi.model.IRI;

public class IRIConstant extends Constant{

	private IRI iri;

	public IRIConstant(String iriString) {
		super(iriString);
		this.iri = IRI.create(iriString);
	}
	
	public IRIConstant(IRI iri) {
		super(iri.toString());
		this.iri = iri;
	}

	public IRI getIRI() {
		return iri;
	}

	public void setIRI(IRI iri) {
		this.iri = iri;
	}

	
}
