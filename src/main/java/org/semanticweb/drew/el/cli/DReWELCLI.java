package org.semanticweb.drew.el.cli;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.semanticweb.drew.cli.CommandLine;
import org.semanticweb.drew.default_logic.DefaultRule;
import org.semanticweb.drew.default_logic.rewriter.DefaultLogicKBRewriter;
import org.semanticweb.drew.dlprogram.DLProgramKB2DatalogRewriter;
import org.semanticweb.drew.dlprogram.format.DLProgramStorer;
import org.semanticweb.drew.dlprogram.format.DLProgramStorerImpl;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.el.profile.SROELProfile;
import org.semanticweb.drew.el.reasoner.DReWELManager;
import org.semanticweb.drew.el.reasoner.NamingStrategy;
import org.semanticweb.drew.el.reasoner.SROEL2DatalogRewriter;
import org.semanticweb.drew.elprogram.ELProgramKBRewriter;
import org.semanticweb.drew.elprogram.incremental.IncrementalELProgramRewriter;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

public class DReWELCLI extends CommandLine {

	protected String ontologyFile;
	protected String sparqlFile;
	protected String dlvPath;
	protected String cqFile;
	protected String dlpFile;
	protected String filter;
	protected String datalogFile;
	protected String rewriting = "default";
	protected boolean rewriting_only = false;
	protected String defaultFile;
	protected String semantics = "asp";
	protected String[] args;

	public DReWELCLI(String[] args) {
		this.args = args;
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
		SROELProfile profile = new SROELProfile();
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
		} else if (sparqlFile != null) {
			handleSparql(ontology, inputProgram);
		}

		if (rewriting_only) {
			// do nothing
		} else {
			runDLV(inputProgram);
		}

	}

	private void handleSparql(OWLOntology ontology, DLVInputProgram inputProgram) {
		throw new UnsupportedOperationException("not implemented yet! try using `drew -rl`");
	}

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
		new DReWELCLI(args).go();
	}

	@Override
	public void handleDefault(OWLOntology ontology, DLVInputProgram inputProgram) {
		try {
			DLProgramParser parser;
			parser = new DLProgramParser(new FileReader(defaultFile));

			List<DefaultRule> dfRules = parser.defaultRules();
			DefaultLogicKBRewriter rewriter = new DefaultLogicKBRewriter();
			List<ProgramStatement> result = rewriter.rewriteDefaultLogicKB(
					ontology, dfRules);
			StringBuilder target = new StringBuilder();

			DLProgramStorer storer = new DLProgramStorerImpl();
			DReWELManager.getInstance().setNamingStrategy(
					NamingStrategy.IRIFragment);
			storer.store(result, target);
			FileWriter writer = null;
			try {
				datalogFile = "tmp.dlv";
				writer = new FileWriter(datalogFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			filter = "in,out";

			storer.store(result, writer);
			writer.close();
			inputProgram.addFile(datalogFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleDLProgram(OWLOntology ontology,
			DLVInputProgram inputProgram) {
		try {
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

			DLProgramStorer storer = new DLProgramStorerImpl();
			StringBuilder target = new StringBuilder();
			storer.store(datalog, target);

			String strDatalog = target.toString();
			int j = dlpFile.lastIndexOf('/');
			String dlpTag = dlpFile;
			if (j >= 0) {
				dlpTag = dlpFile.substring(j + 1);
			}

			datalogFile = ontologyFile + "-" + dlpTag + "-" + rewriting + ".dl";
			// inputProgram.addFile(datalogFile);

			FileWriter w = new FileWriter(datalogFile);
			w.write(strDatalog);
			w.close();
			inputProgram.addFile(datalogFile);
			// inputProgram.addText(strDatalog);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleCQ(OWLOntology ontology, DLVInputProgram inputProgram) {
		try {
			SROEL2DatalogRewriter rewriter = new SROEL2DatalogRewriter();
			DLProgram datalog = rewriter.rewrite(ontology);
			DLProgramStorer storer = new DLProgramStorerImpl();
			// DatalogToStringHelper helper = new DatalogToStringHelper();
			FileWriter writer = null;

			datalogFile = "tmp.dlv";
			writer = new FileWriter(datalogFile);

			StringBuilder target = new StringBuilder();
			storer.store(datalog, target);
			String strDatalog = target.toString();

			String cq = parseCQ();

			/* I can add some file to the DLVInputProgram */
			writer.append(cq);
			writer.append(strDatalog);
			writer.close();
			inputProgram.addFile(datalogFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String parseCQ() {
		try {
			FileReader reader = new FileReader(cqFile);
			DLProgramParser dlProgramParser = new DLProgramParser(reader);
			DLProgram cqProgram;

			cqProgram = dlProgramParser.program();

			DLProgramStorer storer = new DLProgramStorerImpl();
			StringBuilder target = new StringBuilder();
			storer.store(cqProgram, target);

			String cq = target.toString();
			return cq;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public boolean parseArgs(String[] args) {
		int i = 0;
		while (i < args.length) {
            switch (args[i]) {
                case "-el":
                    i += 1;
                    // fine
                    break;
                case "-rl":
                    throw new IllegalStateException("-rl");
                case "-ontology":
                    ontologyFile = args[i + 1];
                    i += 2;
                    break;
                case "-sparql":
                    sparqlFile = args[i + 1];
                    i += 2;
                    break;
                case "-cq":
                    cqFile = args[i + 1];
                    i += 2;
                    break;
                case "-dlp":
                    dlpFile = args[i + 1];
                    i += 2;
                    break;
                case "-sparql":
                    sparqlFile = args[i + 1];
                    i += 2;
                    break;
                case "-default":
                    defaultFile = args[i + 1];
                    i += 2;
                    break;
                case "-filter":
                    filter = args[i + 1];
                    i += 2;
                    break;
                case "-dlv":
                    dlvPath = args[i + 1];
                    i += 2;
                    break;
                case "-verbose":
                    DReWELManager.getInstance().setVerboseLevel(
                            Integer.parseInt(args[i + 1]));
                    i += 2;
                    break;
                case "-rewriting":
                    rewriting = args[i + 1];
                    i += 2;
                    break;
                case "--rewriting-only":
                    rewriting_only = true;
                    i += 1;
                    break;
                case "-wf":
                    semantics = "wf";
                    i += 1;
                    break;
                case "-asp":
                    semantics = "asp";
                    i += 1;
                    break;
                default:
                    System.err.println("Unknow option " + args[i]);
                    System.err.println();
                    return false;
            }
		}

		if (ontologyFile == null) {
			System.err.println("Please specify the ontology file");
			return false;
		}

		if (rewriting_only && cqFile == null && sparqlFile == null && dlpFile == null
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

	public void runDLV(DLVInputProgram inputProgram) {
		DLVInvocation invocation = DLVWrapper.getInstance().createInvocation(
				dlvPath);

		try {
			invocation.setInputProgram(inputProgram);

			// invocation.setNumberOfModels(1);
			List<String> filters = new ArrayList<>();

			if (cqFile != null) {
				filters.add("ans");
			}
			if (filter != null) {
				String[] ss = filter.split(",");

                Collections.addAll(filters, ss);
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

	public void printUsage() {

		String usage = //
		"Usage: drew.el.sh -ontology <ontology_file> { -sparql <sparql_file> | -cq <cq_file> | -dlp <dlp_file> | -default <df_file> } "
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
				"Example: java -jar drew.el.jar -ontology university.owl -dlp rule.dlp -dlv /usr/bin/dlv " //
		;

		System.out.println(usage);

	}

}
