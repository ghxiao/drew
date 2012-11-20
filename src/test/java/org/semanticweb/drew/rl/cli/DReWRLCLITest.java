package org.semanticweb.drew.rl.cli;

import static org.junit.Assert.*;
import it.unical.mat.wrapper.DLVInvocationException;

import java.io.IOException;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.el.cli.DReWELCLI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class DReWRLCLITest {

	@Test
	public void testNetwork() throws OWLOntologyCreationException, IOException, ParseException, DLVInvocationException {
		DReWRLCLI.main("-ontology benchmark/network/ontology/network.owl -dlp benchmark/network/rules/network.dlp -dlv /Users/xiao/bin/dlv".split("\\s"));
	}

}
