package org.semanticweb.drew.el.cli;

import it.unical.mat.wrapper.DLVError;
import it.unical.mat.wrapper.DLVInputProgram;
import it.unical.mat.wrapper.DLVInputProgramImpl;
import it.unical.mat.wrapper.DLVInvocation;
import it.unical.mat.wrapper.DLVInvocationException;
import it.unical.mat.wrapper.DLVWrapper;
import it.unical.mat.wrapper.FactHandler;
import it.unical.mat.wrapper.FactResult;
import it.unical.mat.wrapper.ModelBufferedHandler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.drew.dlprogram.DLProgramKB2DatalogRewriter;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.el.profile.SROELProfile;
import org.semanticweb.drew.el.reasoner.DReWELManager;
import org.semanticweb.drew.el.reasoner.DatalogToStringHelper;
import org.semanticweb.drew.el.reasoner.NamingStrategy;
import org.semanticweb.drew.el.reasoner.SROEL2DatalogRewriter;
import org.semanticweb.drew.elprogram.ELProgramKBRewriter;
import org.semanticweb.drew.elprogram.incremental.IncrementalELProgramRewriter;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

public class DReWELCLI {

	/**
	 * @param args
	 */
	private static String ontologyFile;
	private static String sparqlFile;
	private static String dlvPath;
	private static String cqFile;
	private static String dlpFile;
	private static String filter;
	private static String datalogFile;
	private static String rewriting = "default";
	private static boolean rewriting_only = false;

	/**
	 * @param args
	 * @throws RecognitionException
	 * @throws OWLOntologyCreationException
	 * @throws IOException
	 * @throws ParseException
	 * @throws DLVInvocationException
	 */
	public static void main(String[] args) throws OWLOntologyCreationException,
			IOException, ParseException, DLVInvocationException {
		System.setProperty("entityExpansionLimit", "512000");

		if (!parseArgs(args)) {
			printUsage();
			System.exit(1);
		}

		DReWELManager.getInstance().setNamingStrategy(NamingStrategy.IRIFull);
		// DReWELManager.getInstance().setDatalogFormat(DatalogFormat.XSB);
		SROELProfile profile = new SROELProfile();
		File file = new File(ontologyFile);
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = man.loadOntologyFromOntologyDocument(file);

		OWLProfileReport report = profile.checkOntology(ontology);
		System.out.println(report);
		DLVInputProgram inputProgram = new DLVInputProgramImpl();
		if (cqFile != null) {
			SROEL2DatalogRewriter rewriter = new SROEL2DatalogRewriter();
			DLProgram datalog = rewriter.rewrite(ontology);

			DatalogToStringHelper helper = new DatalogToStringHelper();
			String strDatalog = helper.toString(datalog);

			String cq = parseCQ();

			/* I can add some file to the DLVInputProgram */

			inputProgram.addText(strDatalog);
			inputProgram.addText(cq);
		} else if (dlpFile != null) {
			DLProgramKB kb = new DLProgramKB();
			kb.setOntology(ontology);

			DLProgramParser parser = new DLProgramParser(
					new FileReader(dlpFile));
			DLProgram elprogram = parser.program();
			kb.setProgram(elprogram);

			DLProgram datalog;

			DLProgramKB2DatalogRewriter compiler;

			if (rewriting.equals("inc")) {
				compiler = new IncrementalELProgramRewriter();
			} else {
				compiler = new ELProgramKBRewriter();
			}

			datalog = compiler.rewrite(kb);

			DatalogToStringHelper helper = new DatalogToStringHelper();
			String strDatalog = helper.toString(datalog);
			int j = dlpFile.indexOf('/');
			String dlpTag = dlpFile;
			if (j >= 0) {
				dlpTag = dlpFile.substring(j + 1);
			}

			datalogFile = ontologyFile + "-" + dlpTag + "-" + rewriting + ".dl";
			//inputProgram.addFile(datalogFile);

			 FileWriter w = new FileWriter(datalogFile);
			 w.write(strDatalog);
			 w.close();
			 inputProgram.addText(strDatalog);
		}

		if (rewriting_only) {

		} else {
			DLVInvocation invocation = DLVWrapper.getInstance()
					.createInvocation(dlvPath);
			/* I can specify a part of DLV program using simple strings */

			// Creates an instance of DLVInvocation

			invocation.setInputProgram(inputProgram);

			invocation.setNumberOfModels(1);
			List<String> filters = new ArrayList<String>();

			if (cqFile != null) {
				filters.add("ans");
			}
			if (filter != null) {
				filters.add(filter);
			}
			invocation.setFilter(filters, true);
			ModelBufferedHandler modelBufferedHandler = new ModelBufferedHandler(
					invocation);

			/* In this moment I can start the DLV execution */
			FactHandler factHandler = new FactHandler() {
				@Override
				public void handleResult(DLVInvocation obsd, FactResult res) {
					String answerString = res.toString();
					System.out.println(answerString);
					// answers.add(answerString);
				}
			};

			invocation.subscribe(factHandler);
			invocation.run();
			if (!modelBufferedHandler.hasMoreModels())
				System.out.println("No model");
			invocation.waitUntilExecutionFinishes();
			List<DLVError> k = invocation.getErrors();
			if (k.size() > 0)
				System.out.println(k);
		}
		// invocation.s
	}

	protected static String parseCQ() throws IOException, ParseException {
		FileReader reader = new FileReader(cqFile);
		DLProgramParser dlProgramParser = new DLProgramParser(reader);
		DLProgram cqProgram = dlProgramParser.program();
		String cq = new DatalogToStringHelper().toString(cqProgram);

		return cq;
	}

	public static boolean parseArgs(String[] args) {
		int i = 0;
		while (i < args.length) {
			if (args[i].equals("-ontology")) {
				ontologyFile = args[i + 1];
				i += 2;
			} else if (args[i].equals("-sparql")) {
				sparqlFile = args[i + 1];
				i += 2;
			} else if (args[i].equals("-cq")) {
				cqFile = args[i + 1];
				i += 2;
			} else if (args[i].equals("-dlp")) {
				dlpFile = args[i + 1];
				i += 2;
			} else if (args[i].equals("-filter")) {
				filter = args[i + 1];
				i += 2;
			} else if (args[i].equals("-dlv")) {
				dlvPath = args[i + 1];
				i += 2;
			} else if (args[i].equals("-verbose")) {
				DReWELManager.getInstance().setVerboseLevel(
						Integer.parseInt(args[i + 1]));
				i += 2;
			} else if (args[i].equals("-rewriting")) {
				rewriting = args[i + 1];
				i += 2;
			} else if (args[i].equals("--rewriting-only")) {
				rewriting_only = true;
				i += 1;
			} else {
				System.err.println("Unknow option " + args[i]);
				return false;
			}
		}

		if (ontologyFile == null) {
			System.err.println("Please specify the ontology file");
			return false;
		}

		if (cqFile == null && sparqlFile == null && dlpFile == null) {
			System.err.println("Please specify the cq file or the sparql file");
			return false;
		}

		if (dlvPath == null) {
			System.err.println("Please specify the path of dlv reasoner");
			return false;
		}
		// if (ontologyFile != null && sparqlFile != null && dlvPath != null) {
		// return true;
		// }
		return true;

	}

	private static void printUsage() {

		String usage = //
		"Usage: drew.el.sh -ontology <ontology_file> {-sparql <sparql_file> | -cq <cq_file> | -dlp <dlp_file>} "
				+ "[-filter <filter>]"
				+ "-dlv <dlv_path> [-verbose <verbose_level>]\n"
				+ //
				"  <ontology_file>\n"
				+ //
				"    the ontology file to be read, which has to be in Horn-SHIQ fragment \n"
				+ //
				"  <sparql_file>\n"
				+ //
				"    the sparql file to be query, which has to be a Conjunctive Query. \n"
				+ //
				"  <cq_file>\n"
				+ //
				"    the cq file to be query, which has to be a Conjunctive Query. \n"
				+ //
				"  <dlp_file>\n"
				+ //
				"    the dl-program file. \n"
				+ //
				"  <dlv_path>\n"
				+ //
				"    the path of dlv \n"
				+ //
				"  <verbose_level>\n"
				+ //
				"    Specify verbose category (default: 0)\n"
				+ "\n"
				+ //
				"Example: java -jar kaos.jar -ontology university.owl -sparql q1.sparql -dlv /usr/bin/dlv " //
		;

		System.out.println(usage);

	}

}
