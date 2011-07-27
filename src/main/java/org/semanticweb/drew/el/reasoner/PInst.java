package org.semanticweb.drew.el.reasoner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;

import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKBLoader;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;

public class PInst {

	static String strPInst = "inst(X, X) :- nom(X).\n" + //
			"self(X, V) :- nom(X), triple(X, V, X).\n" + //
			"inst(X, Z) :- top(Z), inst(X, Z1).\n" + //
			"inst(X, Y) :- bot(Z), inst(U, Z), inst(X, Z1), cls(Y).\n" + //
			"inst(X, Z) :- subClass(Y, Z), inst(X, Y).\n" + //
			"inst(X, Z) :-subConj(Y1, Y2, Z), inst(X, Y1), inst(X, Y2).\n" + //
			"inst(X, Z) :-subEx(V, Y, Z), triple(X, V, X1), inst(X1, Y).\n" + //
			"inst(X, Z) :-subEx(V, Y, Z), self(X, V), inst(X, Y).\n" + //
			"triple(X, V, X1) :-  supEx(Y, V, Z, X1), inst(X, Y).\n" + //
			"inst(X1, Z) :-supEx(Y, V, Z, X1), inst(X, Y).\n" + //
			"inst(X, Z) :-subSelf(V, Z), self(X, V).\n" + //
			"self(X, V) :-supSelf(Y, V), inst(X, Y).\n" + //
			"triple(X, W, X1) :-subRole(V, W), triple(X, V, X1).\n" + //
			"self(X, W) :- subRole(V, W), self(X, V).\n" + //
			"triple(X, W, X11) :- subRChain(U, V, W), triple(X, U, X1), triple(X1, V, X11).\n" + //
			"triple(X, W,X1) :-subRChain(U, V, W), self(X, U), triple(X, V, X1).\n" + //
			"triple(X, W, X1) :-subRChain(U, V, W), triple(X, U, X1), self(X1, V).\n" + //
			"triple(X, W, X) :-subRChain(U, V, W), self(X, U), self(X, V).\n" + //
			"triple(X,W,X1) :-subRConj(V1,V2,W), triple(X,V1,X1), triple(X,V2,X1).\n" + //
			"self(X, W) :-subRConj(V1, V2, W), self(X, V1), self(X, V2).\n" + //
			"triple(X, W, X1) :-subProd(Y1, Y2, W), inst(X, Y1), inst(X1, Y2).\n" + //
			"self(X, W) :-subProd(Y1, Y2, W), inst(X, Y1), inst(X, Y2).\n" + //
			"inst(X, Z1) :-supProd(V, Z1, Z2), triple(X, V, X1).\n" + //
			"inst(X, Z1) :-supProd(V, Z1, Z2), self(X, V).\n" + //
			"inst(X1, Z2) :-supProd(V, Z1, Z2), triple(X, V, X1).\n" + //
			"inst(X, Z2) :-supProd(V, Z1, Z2), self(X, V).\n" + //
			"inst(Y, Z) :-inst(X, Y), nom(Y), inst(X, Z).\n" + //
			"inst(X, Z) :-inst(X, Y), nom(Y), inst(Y, Z).\n" + //
			"triple(Z, U, Y) :- inst(X, Y), nom(Y), triple(Z, U, X).\n" + //
			"";

	static DLProgram pInst;

	static {

		StringReader reader = new StringReader(strPInst);

		DLProgramParser dlProgramParser = new DLProgramParser(reader);
		try {
			pInst = dlProgramParser.program();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static DLProgram getPInst() {
		return pInst;
	}
}
