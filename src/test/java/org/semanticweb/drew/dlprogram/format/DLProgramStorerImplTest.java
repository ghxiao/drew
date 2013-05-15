package org.semanticweb.drew.dlprogram.format;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;

public class DLProgramStorerImplTest {

	@Test
	public void test1() throws ParseException, IOException {
		String s = "p(X) :- q(X), not r(X), not -s(Y), -f(X,Y).";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		DLProgram program = parser.program();
		DLProgramStorer storer = new DLProgramStorerImpl();
		StringBuilder sb = new StringBuilder();
		storer.store(program, System.out);
		storer.store(program, sb);
		//assertEquals(s, sb.toString());
		
	}
	
	@Test
	public void test2() throws ParseException, IOException {
		String s = "p(X) :-  not r(X), not -s(Y), -f(X,Y). ";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		DLProgram program = parser.program();
		DLProgramStorer storer = new DLProgramStorerImpl();
		storer.store(program, System.out);
		//System.out.println(program);
	}
	
	@Test
	public void test3() throws ParseException, IOException {
		String s = "p(X) :-  not r(X), not -s(Y). ";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		DLProgram program = parser.program();
		DLProgramStorer storer = new DLProgramStorerImpl();
		storer.store(program, System.out);
		//System.out.println(program);
	}
	
	@Test
	public void test4() throws ParseException {
		String s = "p(X) :- q(X). ";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		DLProgram program = parser.program();
		DLProgramStorer storer = new DLProgramStorerImpl();
		storer.store(program, System.out);
		//System.out.println(program);
	}
	
	@Test
	public void test4_prefix() throws ParseException {
		String s = "p(X) :- q(X). ";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		DLProgram program = parser.program();
		DLProgramStorer storer = new DLProgramStorerImpl();
		storer.setPrefix("preOnt____");
		storer.store(program, System.out);
		//System.out.println(program);
	}
	
	@Test
	public void test4_prefix_RL() throws ParseException {
		String s = "p(X) :- q(X). ";
		DLProgramParser parser = new DLProgramParser(new StringReader(s));
		DLProgram program = parser.program();
		DLProgramStorer storer = new RLProgramStorerImpl();
		storer.setPrefix("preOnt____");
		storer.store(program, System.out);
		//System.out.println(program);
	}

}
