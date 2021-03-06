package org.semanticweb.drew.el.reasoner;

import java.util.HashMap;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

class SuperSomeAxiomEncoder {

	private Map<Integer, OWLSubClassOfAxiom> int2axiom;

	private Map<OWLSubClassOfAxiom, Integer> axiom2int;

	private int maxEncode;

	public SuperSomeAxiomEncoder() {
		maxEncode = -1;
		int2axiom = new HashMap<>();
		axiom2int = new HashMap<>();
	}

	public int encode(OWLSubClassOfAxiom axiom) {
		if (axiom2int.containsKey(axiom)) {
			return axiom2int.get(axiom);
		} else {
			maxEncode++;
			axiom2int.put(axiom, maxEncode);
			int2axiom.put(maxEncode, axiom);
			return maxEncode;
		}
	}

	public OWLSubClassOfAxiom decode(Integer value) {
		return int2axiom.get(value);
	}
}
