package org.semanticweb.drew.util;

import org.coode.owlapi.latex.LatexRenderer;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class MyLatexRenderer extends LatexRenderer {

	public MyLatexRenderer(OWLOntologyManager owlOntologyManager) {
		super(owlOntologyManager);
		super.shortFormProvider  = new LatexSimpleShortFromProvider();
	}
	
}
