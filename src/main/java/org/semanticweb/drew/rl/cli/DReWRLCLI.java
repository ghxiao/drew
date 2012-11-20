package org.semanticweb.drew.rl.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.unical.mat.dlv.program.Literal;
import it.unical.mat.wrapper.DLVError;
import it.unical.mat.wrapper.DLVInputProgram;
import it.unical.mat.wrapper.DLVInputProgramImpl;
import it.unical.mat.wrapper.DLVInvocation;
import it.unical.mat.wrapper.DLVInvocationException;
import it.unical.mat.wrapper.DLVWrapper;
import it.unical.mat.wrapper.Model;
import it.unical.mat.wrapper.ModelHandler;
import it.unical.mat.wrapper.ModelResult;
import it.unical.mat.wrapper.Predicate;

import org.semanticweb.drew.cli.CommandLine;
import org.semanticweb.drew.dlprogram.format.DLProgramStorer;
import org.semanticweb.drew.dlprogram.format.DLProgramStorerImpl;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.model.DLProgramKBLoader;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.el.reasoner.DReWELManager;
import org.semanticweb.drew.el.reasoner.NamingStrategy;
import org.semanticweb.drew.ldlp.profile.LDLPProfile;
import org.semanticweb.drew.ldlpprogram.reasoner.RLProgramKBCompiler;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

public class DReWRLCLI extends CommandLine {

	protected String ontologyFile;
	protected String sparqlFile;
	protected String dlvPath;
	protected String cqFile;
	protected String dlpFile;
	protected String filter;
	protected String datalogFile;
	protected boolean rewriting_only = false;
	protected String defaultFile;
	protected String semantics = "asp";
	protected String[] args;

	public DReWRLCLI(String[] args) {
		this.args = args;
	}

	public static void main(String[] args) {
		new DReWRLCLI(args).go();
	}

	@Override
	public boolean parseArgs(String[] args) {
		int i = 0;
		while (i < args.length) {
			if (args[i].equals("-rl")) {
				// fine
			} else if (args[i].equals("-el")) {
				throw new IllegalStateException("-el");
			} else if (args[i].equals("-ontology")) {
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
			} else if (args[i].equals("-sparql")) {
				sparqlFile = args[i + 1];
				i += 2;
			} else if (args[i].equals("-default")) {
				defaultFile = args[i + 1];
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
			} else if (args[i].equals("--rewriting-only")) {
				rewriting_only = true;
				i += 1;
			} else if (args[i].equals("-wf")) {
				semantics = "wf";
				i += 1;
			} else if (args[i].equals("-asp")) {
				semantics = "asp";
				i += 1;
			} else {
				System.err.println("Unknow option " + args[i]);
				System.err.println();
				return false;
			}
		}

		if (ontologyFile == null) {
			System.err.println("Please specify the ontology file");
			return false;
		}

		if (cqFile == null && sparqlFile == null && dlpFile == null
				&& defaultFile == null) {
			System.err
					.println("Please specify the cq file, or the sparql file, or dl porgram, or default rules file");
			return false;
		}

		if (dlvPath == null) {
			System.err.println("Please specify the path of dlv reasoner");
			return false;
		}

		return true;
	}

	@Override
	public void go() {
		System.setProperty("entityExpansionLimit", "512000");

		if (!parseArgs(args)) {
			printUsage();
			System.exit(1);
		}

		DReWELManager.getInstance().setNamingStrategy(NamingStrategy.IRIFull);
		// DReWELManager.getInstance().setDatalogFormat(DatalogFormat.XSB);
		OWLProfile profile = new LDLPProfile();
		File file = new File(ontologyFile);
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ontology;
		try {
			ontology = man.loadOntologyFromOntologyDocument(file);
		} catch (OWLOntologyCreationException e) {
			throw new RuntimeException(e);
		}

		OWLProfileReport report = profile.checkOntology(ontology);
		if (!report.isInProfile()) {
			System.err.println(report);
		}
		DLVInputProgram inputProgram = new DLVInputProgramImpl();

		if (cqFile != null) {
			handleCQ(ontology, inputProgram);
		} else if (dlpFile != null) {
			handleDLProgram(ontology, inputProgram);
		} else if (defaultFile != null) {
			handleDefault(ontology, inputProgram);
		}
		
		runDLV(inputProgram);
	}

	@Override
	public void handleDefault(OWLOntology ontology, DLVInputProgram inputProgram) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleDLProgram(OWLOntology ontology,
			DLVInputProgram inputProgram) {
		try {
			DLProgramKB kb = new DLProgramKB();
			kb.setOntology(ontology);
			DLProgram elprogram = null;

			DLProgramParser parser;
			parser = new DLProgramParser(new FileReader(dlpFile));
			elprogram = parser.program();
			kb.setProgram(elprogram);

			DLProgram datalog;

			RLProgramKBCompiler compiler = new RLProgramKBCompiler();
			datalog = compiler.rewrite(kb);

			DLProgramStorer storer = new DLProgramStorerImpl();
			StringBuilder target = new StringBuilder();
			storer.storeDLProgram(datalog, target);

			String strDatalog = target.toString();
			int j = dlpFile.lastIndexOf('/');
			String dlpTag = dlpFile;
			if (j >= 0) {
				dlpTag = dlpFile.substring(j + 1);
			}

			datalogFile = ontologyFile + "-" + dlpTag + ".dl";
			// inputProgram.addFile(datalogFile);

			FileWriter w = new FileWriter(datalogFile);
			w.write(strDatalog);
			w.close();
			inputProgram.addFile(datalogFile);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (rewriting_only) {
			// do nothing
		} else {
			runDLV(inputProgram);
			// TODO decode 
		}
	}

	public void runDLV(DLVInputProgram inputProgram) {
		DLVInvocation invocation = DLVWrapper.getInstance().createInvocation(
				dlvPath);

		try {
			invocation.setInputProgram(inputProgram);

			// invocation.setNumberOfModels(1);
			List<String> filters = new ArrayList<String>();

			if (cqFile != null) {
				filters.add("ans");
			}
			if (filter != null) {
				String[] ss = filter.split(",");

				for (String s : ss)
					filters.add(s);
			}

			if (filters != null && filters.size() > 0)
				invocation.setFilter(filters, true);

			if (semantics.equals("wf"))
				invocation.addOption("-wf");

			invocation.subscribe(new ModelHandler() {

				@Override
				public void handleResult(DLVInvocation paramDLVInvocation,
						ModelResult modelResult) {
					System.out.print("{ ");
					Model model = (Model) modelResult;
					// ATTENTION !!! this is necessary and stupid, should we
					// report
					// a bug to DLVWrapper?
					model.beforeFirst();
					while (model.hasMorePredicates()) {

						Predicate predicate = model.nextPredicate();
						while (predicate.hasMoreLiterals()) {

							Literal literal = predicate.nextLiteral();
							System.out.print(literal);
							System.out.print(" ");
						}
					}

					System.out.println("}");
					System.out.println();
				}
			});

			invocation.run();

			invocation.waitUntilExecutionFinishes();
			List<DLVError> k = invocation.getErrors();
			if (k.size() > 0)
				System.err.println(k);

		} catch (DLVInvocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleCQ(OWLOntology ontology, DLVInputProgram inputProgram) {
		// TODO Auto-generated method stub

	}

	public void printUsage() {

		String usage = //
		"Usage: drew -rl -ontology <ontology_file> { -sparql <sparql_file> | -cq <cq_file> | -dlp <dlp_file> | -default <df_file> } "
				+ "[-filter <filter>] "
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
				"  <df_file>\n"
				+ //
				"    the default rules file. \n"
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
				"Example: java -jar drew.rl.jar -ontology university.owl -dlp rule.dlp -dlv /usr/bin/dlv " //
		;

		System.out.println(usage);

	}

}
