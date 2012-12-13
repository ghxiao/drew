package org.semanticweb.drew.dlprogram.parser;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;
import org.semanticweb.drew.benchmark.PaperReviewBenchmarkGenerator;
import org.semanticweb.drew.default_logic.DefaultRule;
import org.semanticweb.drew.dlprogram.model.DLProgram;

public class DLProgramParserTest {

	@Test
	public void test() throws ParseException {
		
		//System.out.println(PaperReviewBenchmarkGenerator.elpRules);
//		DLProgramParser parser = new DLProgramParser(new StringReader(PaperReviewBenchmarkGenerator.elpRules));
//		DLProgram program = parser.program();
//		System.out.println(program);
	}
	
	@Test
	public void testNeg() throws ParseException{
		String s = "p(X) :- q(X), not r(X), not -s(Y), -f(X,Y). ";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		DLProgram program = parser.program();
		System.out.println(program);
    }

	
	
	public static void main(String[] args) throws ParseException{
		new DLProgramParserTest().test();
	}

}
