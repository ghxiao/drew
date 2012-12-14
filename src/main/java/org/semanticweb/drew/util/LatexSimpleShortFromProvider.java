package org.semanticweb.drew.util;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

class LatexSimpleShortFromProvider extends SimpleShortFormProvider {

	@Override
	public String getShortForm(OWLEntity entity) {
		return super.getShortForm(entity).replace("_", "\\_").replace("#", "\\#");
	}

}
