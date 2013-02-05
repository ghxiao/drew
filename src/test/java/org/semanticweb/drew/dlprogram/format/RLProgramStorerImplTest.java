package org.semanticweb.drew.dlprogram.format;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.semanticweb.drew.dlprogram.model.Term;

public class RLProgramStorerImplTest {

	@Test
	public void testRegex() {
		Pattern qIriPattern = Pattern.compile("^<.*#(.*)>$");
		Pattern pdPattern = Pattern.compile("^p\\d+$");

		System.out.println(pdPattern.matcher("p12").find());

		String qiri = "<http://www.kr.tuwien.ac.at/staff/xiao/ontology/network.owl#Node>";

		Matcher matcher = qIriPattern.matcher(qiri);

		if (matcher.find()) {
			String group = matcher.group();

			System.out.println(matcher.group(1));

			System.out.println(group);
		} else {
			System.out.println("no match");
		}

		// System.out.println(qiri.replaceAll(pattern, "$1"));
	}

	

}
