/*
 * @(#)DlvDatalogReasoner.java 2010-3-28 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.datalog;

import java.util.List;

import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlvwrapper.DLVInvocationException;
import org.semanticweb.drew.dlvwrapper.DLVWrapper;
//import org.semanticweb.drew.helper.OSDetector;
import org.semanticweb.drew.ldlp.reasoner.LDLPAxiomCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//use official DLVWrapper instead
@Deprecated
public class DLVReasoner implements DatalogReasoner {

	final static Logger logger = LoggerFactory.getLogger(LDLPAxiomCompiler.class);

	// /*
	// * (non-Javadoc)
	// *
	// * @see at.ac.tuwien.kr.datalog.DatalogReasoner#query(java.util.List,
	// * edu.stanford.db.lp.ProgramClause)
	// */
	// @Override
	// public boolean query(List<ProgramClause> program, ProgramClause query) {
	//
	// boolean result = false;
	//
	// Program dlvProgram = new Program(); // creates a new program
	// // p.addString(":- a, not b."); // adds a constraint using a String
	// // object
	// // p.addProgramFile(".\\test\\misc\\simple.dlv"); // adds a text file
	// // containing
	//
	// StringBuilder lines = new StringBuilder();
	//
	// for (ProgramClause programClause : program) {
	// logger.debug("Clause added: {}", programClause);
	// lines.append(programClause);
	// }
	//
	// dlvProgram.addString(lines.toString());
	//
	// // build a manager (a Dlv instance)
	//
	// final String dlvPath = ".\\dlv\\dlv.mingw.exe";
	// logger.debug("Using Dlv: {}", dlvPath);
	// DlvHandler dlv = new DlvHandler(dlvPath); // builds a
	// // manager
	// // object and set the
	// // dlv executable full
	// // pathname
	//
	// dlv.setProgram(dlvProgram); // sets input (contained in a Program
	// // object)
	//
	// // set invocation parameters
	//
	// // for positive datalog proram, there is a unique model
	// dlv.setNumberOfModels(2); // sets the number of models to be generated
	// // by DLV
	//
	// final Literal[] queryLiterals = query.getHead();
	// final int n = queryLiterals.length;
	// String[] filters = new String[n];
	// for (int i = 0; i < n; i++) {
	// filters[i] = queryLiterals[i].getPredicateSymbol();
	// logger.debug("Using Filter: {}", filters[i]);
	// }
	//
	// // dlv.setFilter(filters); // sets a -filter=a option
	// dlv.setIncludeFacts(false); // sets -nofacts option
	//
	// BitSet results = new BitSet(n);
	//
	// // call DLV
	// try {
	// logger.debug("run a DLV process");
	// dlv.run(DlvHandler.MODEL_SYNCHRONOUS); // run a DLV process and set
	// // the output handling
	// // method.
	//
	// while (dlv.hasMoreModels()) // waits while DLV outputs a new model
	// {
	// Model m = dlv.nextModel(); // gets a Model object
	//
	// logger.debug("Model: {}", m);
	// if (!m.isNoModel()) {
	//
	// while (m.hasMorePredicates()) {
	// Predicate pr = m.nextPredicate();
	//
	// while(pr.next()){
	//
	// DLV.Predicate.Literal lit = pr.getLiteral();
	// logger.debug("Literal: {}", lit);
	//
	// if(lit.toString().equals(queryLiterals[0]+".")){
	// result = true;
	// }
	// }
	//
	// // while (literals.hasMoreElements()) {
	// // Object object = (Object) literals.nextElement();
	// // logger.debug("Literal: {}", object);
	// // }
	//
	//
	// }
	//
	// // m.
	// //
	// // while (m.hasMorePredicates()) // work with Predicates
	// // // contained in m
	// // {
	// // Predicate pr = m.nextPredicate(); // gets a Predicate
	// // // object
	// // System.out.println(pr);
	// // }
	// }
	//
	// }
	//
	// } catch (DLVException d) {
	// d.printStackTrace();
	// } catch (DLVExceptionUncheked d) {
	// d.printStackTrace();
	// }
	//
	// return result;
	// }

	@Override
	public List<Literal> query(List<ProgramStatement> program, Literal query) {
		
//		for(Clause clause: program){
//			System.out.println(clause);
//		}
		
		DLVWrapper dlv = initialize(program);

		String queryText = query.toString();
		String filter = ((NormalPredicate) query.getPredicate()).getName();
		try {
			return dlv.querySM(queryText, filter);
		} catch (DLVInvocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	private DLVWrapper initialize(List<ProgramStatement> program) {
		DLVWrapper dlv = new DLVWrapper();

		StringBuilder programText = new StringBuilder();
		for (ProgramStatement clause : program) {
			programText.append(clause);
			programText.append("\n");
		}
		dlv.setProgram(programText.toString());

		// FIXME
		// if (OSDetector.isUnix()) {
		// dlv.setDlvPath("./dlv/dlv_magic");

		dlv.setDlvPath("/Users/xiao/bin/dlv");
		// } else {
		// dlv.setDlvPath("./dlv/dlv.mingw.exe");
		// }
		return dlv;
	}

	// @Override
	public boolean query(List<Clause> program, Clause query) {
		DLVWrapper dlv = new DLVWrapper();

		StringBuilder programText = new StringBuilder();
		for (Clause clause : program) {
			programText.append(clause);
			programText.append("\n");
		}
		dlv.setProgram(programText.toString());
		dlv.setDlvPath("./dlv/dlv_magic");

		// StringBuilder queryText =new StringBuilder();

		String queryText = query.toString();
		try {
			dlv.queryWFS(queryText, "");
		} catch (DLVInvocationException e) {
			e.printStackTrace();
		}
		return false;
	}

	// public boolean query(DLProgram program, Clause query) {
	// return query(program.getClauses(), query);
	//
	// }

	@Override
	public List<Literal> query(DLProgram program, Literal query) {
		return query(program.getStatements(), query);
	}

	@Override
	public boolean isEntailed(List<ProgramStatement> program, Literal query) {
		DLVWrapper dlv = initialize(program);
		String queryText = query.toString();
		String filter = ((NormalPredicate) query.getPredicate()).getName();
		try {
			return dlv.isEntailed(queryText, filter);
		} catch (DLVInvocationException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean isEntailed(DLProgram program, Literal query) {
		return isEntailed(program.getStatements(), query);
	}

}
