package org.semanticweb.drew.el.reasoner;

import java.io.StringReader;
import java.util.List;

import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;

public class PInst {

	private static String strPInst = "isa(X, X) :- nom(X).\n" + //
			"self(X, V) :- nom(X), rol(V), triple(X, V, X).\n" + //
			// "isa(X, Z) :- top(Z), isa(X, Z1).\n" + //
			// "isa(X, Y) :- bot(Z), isa(U, Z), isa(X, Z1), cls(Y).\n" + //
			"isa(X, Z) :- subClass(Y, Z), isa(X, Y).\n" + //
			"isa(X, Z) :- subConj(Y1, Y2, Z), isa(X, Y1), isa(X, Y2).\n" + //
			"isa(X, Z) :- rol(Y), rol(V), subEx(V, Y, Z), triple(X, V, X1), isa(X1, Y).\n" + //
			"isa(X, Z) :- rol(Y), subEx(V, Y, Z), self(X, V), isa(X, Y).\n" + //
			//"triple(X, V, X1) :-  rol(V), supEx(Y, V, Z, X1), isa(X, Y).\n" + //
			"triple(X, V, X1) :-  supEx(Y, V, Z, X1), isa(X, Y).\n" + //
			"isa(X1, Z) :- rol(V), supEx(Y, V, Z, X1), isa(X, Y).\n" + //
			"isa(X, Z) :- rol(V), subSelf(V, Z), self(X, V).\n" + //
			"self(X, V) :- rol(V), supSelf(Y, V), isa(X, Y).\n" + //
			"triple(X, W, X1) :- rol(V), rol(W), subRole(V, W), triple(X, V, X1).\n" + //
			"self(X, W) :- rol(V), rol(W), subRole(V, W), self(X, V).\n" + //
			"triple(X, W, X11) :- rol(U), rol(V), rol(W), subRChain(U, V, W), triple(X, U, X1), triple(X1, V, X11).\n" + //
			"triple(X, W, X1) :- rol(U), rol(V), rol(W), subRChain(U, V, W), self(X, U), triple(X, V, X1).\n" + //
			"triple(X, W, X1) :- rol(U), rol(V), rol(W), subRChain(U, V, W), triple(X, U, X1), self(X1, V).\n" + //
			"triple(X, W, X) :- rol(U), rol(V), rol(W), subRChain(U, V, W), self(X, U), self(X, V).\n" + //
			"triple(X, W, X1) :- rol(V1), rol(V2), rol(W), subRConj(V1,V2,W), triple(X,V1,X1), triple(X,V2,X1).\n" + //
			"self(X, W) :- rol(V1), rol(V2), rol(W), subRConj(V1, V2, W), self(X, V1), self(X, V2).\n" + //
			// "triple(X, W, X1) :- rol(W), subProd(Y1, Y2, W), isa(X, Y1), isa(X1, Y2).\n"
			// + //
			// "self(X, W) :- rol(W), subProd(Y1, Y2, W), isa(X, Y1), isa(X, Y2).\n"
			// + //
			// "isa(X, Z1) :- rol(V), supProd(V, Z1, Z2), triple(X, V, X1).\n"
			// + //
			// "isa(X, Z1) :-supProd(V, Z1, Z2), self(X, V).\n" + //
			// "isa(X1, Z2) :-supProd(V, Z1, Z2), triple(X, V, X1).\n" + //
			// "isa(X, Z2) :-supProd(V, Z1, Z2), self(X, V).\n" + //
			"isa(Y, Z) :-isa(X, Y), nom(Y), isa(X, Z).\n" + //
			"isa(X, Z) :-isa(X, Y), nom(Y), isa(Y, Z).\n" + //
			"triple(Z, U, Y) :- isa(X, Y), nom(Y), triple(Z, U, X).\n" + //
			"triple(X, V, X) :- nom(X), rol(V), self(X, V).\n" + //
			"isa(Y, R) :- range(V, R), triple(X, V, Y).\n " + //
			"isa(X, R) :- domain(V, R), triple(X, V, Y).\n " + //
			// "isa(X, R) :- transitive(R), triple(X, R, Y).\n " + //
			"triple(X,R,Y) :- inv(R, R1), triple(Y, R1, X), rol(R), rol(R1).\n"+
			"top(\"Thing\").\n" + //
			"bot(\"Nothing\").\n" + //
			"";

	public static String strXSBHeader = //
	":- auto_table.\n" + //
			// ":- table triple/3.\n" + //
			// ":- table isa/2.\n" + //
			// ":- table self/2.\n" + //
			// "\n" + //
			// "subConj(_, _, _) :- fail.\n" + //
			// "subEx(_, _, _) :- fail.\n" + //
			// "subSelf(_, _) :- fail.\n" + //
			// "supSelf(_, _) :- fail.\n" + //
			// "self(_, _) :- fail.\n" + //
			// "subProd(_, _, _) :- fail.\n" + //
			// "supProd(_, _, _) :- fail.\n" + //
			// "subRConj(_, _, _) :- fail.\n" + //
			// "subClass(_, _) :- fail.\n" + //
			// "subRole(_, _) :- fail.\n" + //
			// "isa(_, _) :- fail.\n" + //
			// "triple(_, _, _) :- fail.\n" + //
			// "nom(_) :- fail.\n" + //
			// "rol(_) :- fail.\n" + //
			// "cls(_) :- fail.\n" + //
			"";

	private static String strXSBDeclaration = //
	"subConj(X, Y, Z) :- fail.\n" + //
			"subEx(X, Y, Z) :- fail.\n" + //
			"supEx(X, Y, Z, W) :- fail.\n" + //
			"subSelf(X, Y) :- fail.\n" + //
			"supSelf(X, Y) :- fail.\n" + //
			"self(X, Y) :- fail.\n" + //
			"subProd(X, Y, Z) :- fail.\n" + //
			"supProd(X, Y, Z) :- fail.\n" + //
			"subRConj(X, Y, Z) :- fail.\n" + //
			"subRChain(X, Y, Z) :- fail.\n" + //
			"subClass(X, Y) :- fail.\n" + //
			"subRole(X, Y) :- fail.\n" + //
			"isa(X, Y) :- fail.\n" + //
			"triple(X, Y, Z) :- fail.\n" + //
			"nom(X) :- fail.\n" + //
			"rol(X) :- fail.\n" + //
			"cls(X) :- fail.\n" + //
			"range(X, Y) :- fail.\n" + //
			"domain(X, Y) :- fail.\n" + //
			"domainD(X, Y) :- fail.\n" + //
			"";
	private static List<ProgramStatement> pInst;

	private static List<ProgramStatement> xsbDeclaration;

	static {

		StringReader reader = new StringReader(strPInst);

		DLProgramParser dlProgramParser = new DLProgramParser(reader);
		try {
			pInst = dlProgramParser.program().getStatements();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		reader = new StringReader(strXSBDeclaration);
		dlProgramParser = new DLProgramParser(reader);
		try {
			xsbDeclaration = dlProgramParser.program().getStatements();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static List<ProgramStatement> getPInst() {
		return pInst;
	}

	public static List<ProgramStatement> getXsbDeclaration() {
		return xsbDeclaration;
	}

	public static void setXsbDeclaration(List<ProgramStatement> xsbDeclaration) {
		PInst.xsbDeclaration = xsbDeclaration;
	}

}
