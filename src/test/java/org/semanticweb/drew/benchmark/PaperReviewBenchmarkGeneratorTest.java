package org.semanticweb.drew.benchmark;

import static org.junit.Assert.*;

import org.junit.Test;

public class PaperReviewBenchmarkGeneratorTest {

	@Test
	public void test() {
		PaperReviewBenchmarkGenerator g = new PaperReviewBenchmarkGenerator();
		g.generate();
	}

	public static void main(String[] args){
		new PaperReviewBenchmarkGeneratorTest().test();
	}
}
