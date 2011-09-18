package org.semanticweb.drew.elprogram.incremental;

import java.io.StringReader;
import java.util.List;

import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;

public class PInstStar {
	static String strPInstStar = "isa_p(X, X, I) :- nom(X), input(I).\n"
			+ "self_p(X, V, I) :- nom(X), triple_p(X, V, X, I).\n"
			+ "isa_p(X, Z, I) :- top(Z), isa_p(X, Z1, I).  \n"
			+ "isa_p(X, Z, I) :- top(Z), isa_p(X, Z1, I), subset(I1,I).  \n"
			+ "isa_p(X, Z, I) :- top(Z), isa_p(X, Z1, I1), subset(I1,I).  \n"
			+ "isa_p(X, Y, I) :- bot(Z), isa_p(U, Z, I), isa_p(X, Z1, I), cls(Y). \n"
			+ "isa_p(X, Y, I) :- bot(Z), isa_p(U, Z, I), isa_p(X, Z1, I), cls(Y), subclass(I1, I). \n"
			+ "isa_p(X, Y, I) :- bot(Z), isa_p(U, Z, I1), isa_p(X, Z1, I), cls(Y) , subclass(I1, I).\n"
			+ "isa_p(X, Y, I) :- bot(Z), isa_p(U, Z, I), isa_p(X, Z1, I1), cls(Y) , subset(I1, I).\n"
			+ "isa_p(X, Z, I) :- subClass(Y, Z),  isa_p(X, Y, I).  \n"
			+ "isa_p(X, Z, I) :- subConj(Y_1, Y_2, Z), isa_p(X, Y_1, I), isa_p(X, Y_2, I).\n"
			+ "isa_p(X, Z, I) :- subConj(Y_1, Y_2, Z), isa_p(X, Y_1, I1), isa_p(X, Y_2, I), subset(I1, I).\n"
			+ "isa_p(X, Z, I) :- subEx(V, Y, Z),  triple_p(X, V, X1, I),  isa_p(X1, Y, I).\n"
			+ "isa_p(X, Z, I) :- subEx(V, Y, Z),  triple_p(X, V, X1, I1),  isa_p(X1, Y, I),  subset(I1, I).\n"
			+ "isa_p(X, Z, I) :- subEx(V, Y, Z),  triple_p(X, V, X1, I),  isa_p(X1, Y, I1), subset(I1, I).\n"
			+ "isa_p(X, Z, I) :- subEx(V, Y, Z),  self_p(X, V, I),  isa_p(X, Y, I).\n"
			+ "isa_p(X, Z, I) :- subEx(V, Y, Z),  self_p(X, V, I1),  isa_p(X, Y, I), subset(I1, I).\n"
			+ "isa_p(X, Z, I) :- subEx(V, Y, Z),  self_p(X, V, I),  isa_p(X, Y, I1), subset(I1, I).\n"
			+ "triple_p(X, V, X1, I) :-supEx(Y, V, Z, X1),  isa_p(X, Y, I).\n"
			+ "isa_p(X1, Z, I) :- supEx(Y, V, Z, X1),  isa_p(X, Y, I).  \n"
			+ "isa_p(X, Z, I) :- subSelf(V, Z),  self_p(X, V, I).\n"
			+ "self_p(X, V, I) :- supSelf(Y, V),  isa_p(X, Y, I).\n"
			+ "triple_p(X, W, X1, I) :- subRole(V, W),  triple_p(X, V, X1, I). \n"
			+ "self_p(X, W, I) :- subRole(V, W),  self_p(X, V, I).\n"
			+ "triple_p(X, W, X2, I) :- subRChain(U, V, W), triple_p(X, U, X1, I), triple_p(X1, V, X2, I).\n"
			+ "triple_p(X, W, X2, I) :- subRChain(U, V, W), triple_p(X, U, X1, I1), triple_p(X1, V, X2, I), subset(I1,I).\n"
			+ "triple_p(X, W, X1, I) :- subRChain(U, V, W), self_p(X, U, I), triple_p(X, V, X1, I). \n"
			+ "triple_p(X, W, X1, I) :- subRChain(U, V, W), self_p(X, U, I1), triple_p(X, V, X1, I), subset(I1,I). \n"
			+ "triple_p(X, W, X1, I) :- subRChain(U, V, W), self_p(X, U, I), triple_p(X, V, X1, I1), subset(I1, I). \n"
			+ "triple_p(X, W, X1, I) :- subRChain(U, V, W), triple_p(X, U, X1, I),  self_p(X1, V, I). \n"
			+ "triple_p(X, W, X1, I) :- subRChain(U, V, W), triple_p(X, U, X1, I1),  self_p(X1, V, I), subset(I1,\n"
			+ "  I). \n"
			+ "triple_p(X, W, X1, I) :- subRChain(U, V, W), triple_p(X, U, X1, I),  self_p(X1, V, I1), subset(I1, I). \n"
			+ "triple_p(X, W, X, I) :- subRChain(U, V, W),  self_p(X, U, I),  self_p(X, V, I).\n"
			+ "triple_p(X, W, X, I) :- subRChain(U, V, W),  self_p(X, U, I1),  self_p(X, V, I), subset(I1,I).\n"
			+ "triple_p(X, W, X, I) :- subRChain(U, V, W),  self_p(X, U, I),  self_p(X, V, I1), subset(I1,I).\n"
			+ "isa_p(Y, Z, I) :- isa_p(X, Y, I),  nom(Y),  isa_p(X, Z, I). \n"
			+ "isa_p(Y, Z, I) :- isa_p(X, Y, I1),  nom(Y),  isa_p(X, Z, I), subset(I1,I). \n"
			+ "triple_p(Z, U, Y, I) :- isa_p(X, Y, I), nom(Y), triple_p(Z, U, X, I). \n"
			// FIXME:
			//+ "triple_p(Z, U, Y, I) :- isa_p(X, Y, I), nom(Y), triple_p(Z, U, X, I1), subset(I1, I). \n"
			+ "triple_p(Z, U, Y, I) :- isa_p(X, Y, I1), nom(Y), triple_p(Z, U, X, I), subset(I1, I). \n"
			+ "" //
			+ "isa_p(X, Y, 0) :- isa(X, Y)."
			+ "triple_p(X, R, Y, 0) :- triple(X, R, Y)."
			+ "";
	
	static List<Clause> pInstStar;

	static {

		StringReader reader = new StringReader(strPInstStar);

		DLProgramParser dlProgramParser = new DLProgramParser(reader);
		try {
			pInstStar = dlProgramParser.program().getClauses();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Clause> getPInstStar(){
		return pInstStar;
	}
}
