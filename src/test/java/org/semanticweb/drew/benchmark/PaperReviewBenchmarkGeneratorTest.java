package org.semanticweb.drew.benchmark;

import org.junit.Test;

public class PaperReviewBenchmarkGeneratorTest {

	@Test
	public void test() {
		PaperReviewBenchmarkGenerator g = new PaperReviewBenchmarkGenerator();
		g.generate("benchmark/review");
	}

	public static void main(String[] args){
		new PaperReviewBenchmarkGeneratorTest().test();
	}
}
