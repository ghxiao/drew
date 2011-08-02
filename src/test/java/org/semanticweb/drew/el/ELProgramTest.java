package org.semanticweb.drew.el;

import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class ELProgramTest {

	@Test
	public void test() throws IOException {
		String[] queries = {
				// queries[0]
						"#namespace(\"ub\",\"http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#\").\n"
								+ "f (X, Y) :- DL[;ub:Faculty](X), DL[;ub:Faculty](Y), D1 = D2 , U1 != U2 ,\n"
								+ "DL[;ub:doctoralDegreeFrom](X, U1), DL[;ub:worksFor](X, D1 ),\n"
								+ "DL[;ub:doctoralDegreeFrom](Y, U2), DL[;ub:worksFor](Y, D2 ).\n",

						// queries[1]
						"#namespace(\"ub\",\"http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#\").\n"
								+ "f(X, Y) :-  DL[;ub:GraduateStudent](X), DL[;ub:takesCourse](X, Y), \n"
								+ "Y=\"<http://www.Department0.University0.edu/GraduateCourse0>\".",

						// queries[2]
						"#namespace(\"ub\",\"http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#\").\n"
								+ "f(X, Y, Z) :-  DL[;ub:GraduateStudent](X), DL[;ub:University](Y), \n"
								+ "DL[;ub:Department](Z), DL[;ub:memberOf](X,Z), DL[;ub:subOrganizationOf](Z,Y), "
								+ "DL[;ub:undergraduateDegreeFrom](X,Y).\n",

						// queries[3]
						"#namespace(\"ub\",\"http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#\").\n"
								+ "f(X, Y) :-  DL[;ub:Publication](X), DL[;ub:publicationAuthor](X,Y), \n"
								+ "Y=\"<http://www.Department0.University0.edu/AssistantProfessor0>\".",

						// queries[4]
						"#namespace(\"ub\",\"http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#\").\n"
								+ "f(X, Y1, Y2, Y3) :-  DL[;ub:Professor](X), DL[;ub:worksFor](X,Z), \n"
								+ "Z=\"<http://www.Department0.University0.edu>\",\n"
								+ "DL[;ub:name](X,Y1),\n"
								+ "DL[;ub:emailAddress](X,Y2),\n"
								+ "DL[;ub:telephone](X,Y3).\n",

						// queries[5]
						"#namespace(\"ub\",\"http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#\").\n"
								+ "publication(a).\n"
								+ "myPublicationAuthor(a,\"<http://www.Department0.University0.edu/AssistantProfessor0>\")."
								+ "f(X, Y) :-  DL[ub:Publication += publication;ub:Publication](X), "
								+ "DL[ub:publicationAuthor += myPublicationAuthor;ub:publicationAuthor](X,Y), \n"
								+ "Y=\"<http://www.Department0.University0.edu/AssistantProfessor0>\".",

						// queries[6]
						"#namespace(\"ub\",\"http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#\").\n"
								+ "graduateStudent(a).\n"
								+ "university(b).\n"
								+ "department(c).\n"
								+ "myMemberOf(a,c).\n"
								+ "mySubOrganizationOf(c,b).\n"
								+ "myUndergraduateDegreeFrom(a,b)."
								+ "f(X, Y, Z) :-  "
								+ "DL[ub:GraduateStudent+=graduateStudent;ub:GraduateStudent](X), "
								+ "DL[ub:University+=university;ub:University](Y), \n"
								+ "DL[ub:Department+=department;ub:Department](Z), "
								+ "DL[ub:memberOf+=myMemberOf;ub:memberOf](X,Z), "
								+ "DL[ub:subOrganizationOf+=mySubOrganizationOf;ub:subOrganizationOf](Z,Y), "
								+ "DL[ub:undergraduateDegreeFrom+=myUndergraduateDegreeFrom;ub:undergraduateDegreeFrom](X,Y).\n",

						// queries[7]
						"#namespace(\"ub\",\"http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#\").\n"
								+ "graduateStudent(a).\n"
								+ "myTakesCourse(a,b).\n"
								+ "f(X, Y) :-  DL[ub:GraduateStudent+=graduateStudent;ub:GraduateStudent](X), \n"
								+ "DL[ub:takesCourse+=myTakesCourse;ub:takesCourse](X, Y), \n"
								+ "Y=\"<http://www.Department0.University0.edu/GraduateCourse0>\". "

				};
		
		for (int i = 0; i < queries.length; i++){
			String file = "testcase/elprogram_" + i + ".dlp";
			FileWriter writer = new FileWriter(file);
			writer.write(queries[i]);
			writer.close();
		}
	}

}
