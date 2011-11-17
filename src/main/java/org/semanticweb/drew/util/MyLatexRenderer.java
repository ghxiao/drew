package org.semanticweb.drew.util;

import java.lang.reflect.Field;

import org.coode.owlapi.latex.LatexRenderer;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class MyLatexRenderer extends LatexRenderer {

	public MyLatexRenderer(OWLOntologyManager owlOntologyManager)
			throws IllegalArgumentException, IllegalAccessException,
			SecurityException, NoSuchFieldException {
		super(owlOntologyManager);
		
		
		
		Field shortFormProviderFiled = LatexRenderer.class.getDeclaredField("shortFormProvider");
		shortFormProviderFiled.setAccessible(true);
		shortFormProviderFiled.set((LatexRenderer)this, new LatexSimpleShortFromProvider());
	}
}
