package org.semanticweb.drew.benchmark;

import org.junit.Ignore;
import org.junit.Test;

public class PaperReviewBenchmarkGeneratorTest {

	@Ignore
	@Test
	public void test() {
		PaperReviewBenchmarkGenerator g = new PaperReviewBenchmarkGenerator();
		g.generate("benchmark/review");
	}

	public static void main(String[] args){
		new PaperReviewBenchmarkGeneratorTest().test();
	}
}
