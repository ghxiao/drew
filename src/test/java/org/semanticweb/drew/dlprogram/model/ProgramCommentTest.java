package org.semanticweb.drew.dlprogram.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProgramCommentTest {

	@Test
	public void test1() {
		ProgramComment comment = new ProgramComment("abc");
		System.out.println(comment);
	}

	@Test
	public void test2() {
		ProgramComment comment = new ProgramComment("abc\nbcd");
		System.out.println(comment);
	}
	
	@Test
	public void test3() {
		ProgramComment comment = new ProgramComment("");
		System.out.println(comment);
	}
}
