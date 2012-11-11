package org.semanticweb.drew.default_logic;

import java.util.List;

import org.semanticweb.owlapi.model.OWLOntology;

public class DefaultLogicKB {
	private OWLOntology ontology;
	private List<DefaultRule> dfRules;

	public DefaultLogicKB(OWLOntology ontology, List<DefaultRule> dfRules) {
		super();
		this.ontology = ontology;
		this.dfRules = dfRules;
	}

	public OWLOntology getOntology() {
		return ontology;
	}

	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}

	public List<DefaultRule> getDfRules() {
		return dfRules;
	}

	public void setDfRules(List<DefaultRule> dfRules) {
		this.dfRules = dfRules;
	}

}
