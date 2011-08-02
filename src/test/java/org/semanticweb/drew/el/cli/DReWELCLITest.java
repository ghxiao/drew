package org.semanticweb.drew.el.cli;

import static org.junit.Assert.*;
import it.unical.mat.wrapper.DLVInvocationException;

import java.io.IOException;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class DReWELCLITest {

	@Test
	public void test() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWELCLI.main("-ontology testcase/U0_0.owl -cq testcase/lubm_q1.cq -dlv /Users/xiao/usr/bin/dlv".split("\\s"));
	}

}
