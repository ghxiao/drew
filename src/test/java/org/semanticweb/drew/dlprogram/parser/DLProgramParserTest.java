package org.semanticweb.drew.dlprogram.parser;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import org.junit.Test;
import org.semanticweb.drew.benchmark.PaperReviewBenchmarkGenerator;
import org.semanticweb.drew.default_logic.DefaultRule;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.Literal;

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
	
	@Test
	public void testFullURI() throws ParseException, FileNotFoundException{
		//String s = "p(X) :- q(X), not r(X), not -s(Y), -f(X,Y). ";
		DLProgramParser parser = new DLProgramParser(new FileReader("sample_data/graph.dlp"));
		DLProgram program = parser.program();
		System.out.println(program);
    }

	// expect a failure because of DL[;..]
	@Test(expected=ParseException.class)
	public void testSemiColon1() throws ParseException{
		String s = "DL[;\"http://www.kr.tuwien.ac.at/staff/xiao/ontology/graph#arc\"](X, Y)";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		Literal program = parser.literal();
		System.out.println(program);
	}
	
	@Test
	public void testSemiColon2() throws ParseException{
		String s = "DL[\"http://www.kr.tuwien.ac.at/staff/xiao/ontology/graph#arc\"](X, Y)";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		Literal program = parser.literal();
		System.out.println(program);
	}
	
	public static void main(String[] args) throws ParseException, FileNotFoundException{
		new DLProgramParserTest().testFullURI();
	}

}
