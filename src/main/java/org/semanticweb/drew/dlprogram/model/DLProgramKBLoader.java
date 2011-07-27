/* 
 * Copyright (C) 2010, 2011 Guohui Xiao
 */
 

package org.semanticweb.drew.dlprogram.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DLProgramKBLoader {

	final static Logger logger = LoggerFactory
			.getLogger(DLProgramKBLoader.class);

	/**
	 * Load a dl-program kb from two files
	 * 
	 * @param path
	 *            the prifex of kb path. <br/>
	 *            The file path.ldl is an OWL ontology and path.dlp is a
	 *            dl-program,
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */
	public DLProgramKB load(String path) throws FileNotFoundException,
			ParseException {

		DLProgramKB kb = new DLProgramKB();

		OWLOntology ontology = loadOntology(path);

		kb.ontology = ontology;

		DLProgram program = loadDLProgram(path);
		kb.program = program;
		return kb;
	}

	private DLProgram loadDLProgram(String path) throws FileNotFoundException,
			ParseException {

		BufferedReader reader = new BufferedReader(
				new FileReader(path + ".dlp"));

		DLProgramParser dlProgramParser = new DLProgramParser(reader);
		DLProgram program = dlProgramParser.program();
		return program;
	}

	private OWLOntology loadOntology(String path) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		OWLOntology ontology = null;

		String uri = "file:" + path + ".ldl";

		try {
			ontology = manager.loadOntology(IRI.create(uri));

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		return ontology;
	}
}
