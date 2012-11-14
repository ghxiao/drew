package org.semanticweb.drew.ldlpprogram.eval;

import java.io.StringReader;
import java.util.List;

import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.ldlpprogram.reasoner.KBReasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;


public class LUBMQuery {
	public static void main(String[] args) throws OWLOntologyCreationException,
			ParseException {
		System.setProperty("entityExpansionLimit", "512000");

		long t0 = System.currentTimeMillis();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(IRI
		 .create("file:benchmark/uba/University0_0.owl"));
			//	.create("file:benchmark/uba/full-lubm.owl"));

		// String text =
		// "p(a). s(a). s(b). q:-DL[C+=s;D](a), not DL[C+=p;D](b).";

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

		int queryID = 0;
		if (args.length > 0) {
			queryID = Integer.parseInt(args[0]);
		}

		DLProgramParser parser = new DLProgramParser(new StringReader(
				queries[queryID]));

		DLProgram program = parser.program();

		DLProgramKB kb = new DLProgramKB();
		kb.setOntology(ontology);
		kb.setProgram(program);

		System.out.println("Query:");

		System.out.println(program);

		KBReasoner reasoner = new KBReasoner(kb);

		String queryText = "f(X,Y)";

		Literal query = new DLProgramParser(new StringReader(queryText))
				.literal();

		List<Literal> results = reasoner.query(query);

		System.out.println(results.size() + " Query Results");
		for (Literal result : results) {
			System.out.println(result);
		}
		System.out.println(results.size() + " Query Results");
		long t1 = System.currentTimeMillis();
		long dt = t1 - t0;
		System.out.println("Time: " + dt + " ms");

		//
		// boolean entailed = reasoner.isEntailed(query);
		//
		// assertTrue(entailed);
	}
	//
	// private OWLOntology loadOntology(String uri, String phyUri) {
	//
	// OWLOntology ontology = null;
	//
	// System.out.println();
	// System.out
	// .println("------------------------------------------------------");
	//
	// logger.info("Reading file " + uri + "...");
	//
	// manager.addIRIMapper(new SimpleIRIMapper(IRI.create(uri), IRI
	// .create(phyUri)));
	//
	// try {
	// ontology = manager.loadOntology(IRI.create(uri));
	//
	// // for (OWLAxiom axiom : ontology.getAxioms()) {
	// // System.out.println(axiom);
	// // }
	// logger.info("Loading complete");
	// System.out.println(ontology);
	// } catch (OWLOntologyCreationException e) {
	// e.printStackTrace();
	// }
	//
	// LDLPProfile profile = new LDLPProfile();
	//
	// OWLProfileReport report = profile.checkOntology(ontology);
	//
	// if (report.isInProfile()) {
	// System.out.println("The ontology is in LDL+ profile");
	// } else {
	// System.out.println("The ontology is not in LDL+ profile:");
	// Set<OWLProfileViolation> violations = report.getViolations();
	// System.out.println("The following " + violations.size()
	// + " axioms are violated");
	// for (OWLProfileViolation v : violations) {
	// System.out.println(v);
	// }
	// }
	// return ontology;
	// }
}
