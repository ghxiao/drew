/*
 * @(#)Reasoner.java 2010-4-3 
 *
 * Author: Guohui Xiao
 * Technical University of Vienna
 * KBS Group
 */
package org.semanticweb.drew.ldlpprogram.reasoner;

import java.util.List;

import org.semanticweb.drew.datalog.DLVReasoner;
import org.semanticweb.drew.datalog.DatalogReasoner;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.ldlp.reasoner.LDLPQueryResultDecompiler;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;


/**
 * Reasoner for DLProgram KB
 */
public class KBReasoner {

	private RLProgramKBCompiler compiler = new RLProgramKBCompiler();

	private DatalogReasoner datalogReasoner = new DLVReasoner();

	private DLProgram compiledClauses;

	public KBReasoner(DLProgramKB kb) {
		compiledClauses = compiler.rewrite(kb);
	}

	public boolean isEntailed(OWLClassAssertionAxiom axiom) {
		Literal query = compiler.compile(axiom);
		return isEntailed(query);
	}

	boolean isEntailed(Literal query) {
		Literal newQuery = compiler.compileNormalLiteral(query);
		return datalogReasoner.isEntailed(compiledClauses, newQuery);
	}

	public List<Literal> query(Literal q) {

		//LDLPQueryCompiler queryComiler = new LDLPQueryCompiler();
		//Literal query = queryComiler.compileLiteral(q);

		
		List<Literal> result = datalogReasoner.query(compiledClauses, q);
		
		LDLPProgramQueryResultDecompiler decompiler = new LDLPProgramQueryResultDecompiler();
		
		result = decompiler.decompileLiterals(result);
				
		return result;


	}
}
