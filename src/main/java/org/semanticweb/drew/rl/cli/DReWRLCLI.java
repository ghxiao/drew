package org.semanticweb.drew.rl.cli;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.semanticweb.drew.cli.CommandLine;
import org.semanticweb.drew.dlprogram.format.DLProgramStorer;
import org.semanticweb.drew.dlprogram.format.DLProgramStorerImpl;
import org.semanticweb.drew.dlprogram.format.RLProgramStorerImpl;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.DLProgramKB;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.el.reasoner.DReWELManager;
import org.semanticweb.drew.el.reasoner.NamingStrategy;
import org.semanticweb.drew.ldlp.profile.LDLPProfile;
import org.semanticweb.drew.ldlp.reasoner.LDLPOntologyCompiler;
import org.semanticweb.drew.ldlp.reasoner.LDLPQueryCompiler;
import org.semanticweb.drew.ldlpprogram.reasoner.LDLPProgramQueryResultDecompiler;
import org.semanticweb.drew.ldlpprogram.reasoner.RLProgramKBCompiler;
import org.semanticweb.drew.rl.sparql.SparqlCompiler;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

import com.google.common.io.CharStreams;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;

public class DReWRLCLI extends CommandLine {

	private String ontologyFile;
	private String sparqlFile;
	private String dlvPath;
	private String cqFile;
	private String dlpFile;
	private String filter;
	private String datalogFile;
	private int nModels = -1;
	private boolean rewriting_only = false;
	private String defaultFile;
	private String semantics = "asp";
	private String[] args;

	
	private long rewritingTime;
	private long dlvTotalTime;
	long dlvHandlerStartTime = 0;
	long dlvHandlerEndTime = 0;
	private boolean verbose;
	private int maxInt = -1;

	private DReWRLCLI(String[] args) {
		this.args = args;
	}

	public static void main(String... args) {
		new DReWRLCLI(args).go();
	}

	@Override
	public boolean parseArgs(String[] args) {
		int i = 0;
		while (i < args.length) {
			switch (args[i]) {
			case "-rl":
				i += 1; // fine.
				break;
			case "-el":
				throw new IllegalStateException("-el");
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
			case "-N":
				maxInt = Integer.parseInt(args[i + 1]);
				i += 2;
				break;
			case "-n":
				nModels = Integer.parseInt(args[i + 1]);
				i += 2;
				break;
			case "-verbose":
			case "-v":
				verbose = true;
				i += 1;
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
				System.err.println("Unknown option " + args[i]);
				System.err.println();
				return false;
			}
		}

		if (ontologyFile == null) {
			System.err.println("Please specify the ontology file");
			return false;
		}

		if (cqFile == null && sparqlFile == null && dlpFile == null
				&& defaultFile == null && !rewriting_only) {
			System.err
					.println("Please specify the cq file, or the sparql file, or dl program, or default rules file");
			return false;
		}

		if (dlvPath == null && !rewriting_only) {
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
		} else if (sparqlFile != null) {
			handleSparql(ontology, inputProgram);
		} else if (dlpFile != null) {
			handleDLProgram(ontology, inputProgram);
		} else { // ontology part only
			DReWELManager.getInstance().setNamingStrategy(
					NamingStrategy.IRIFragment);
			handleOntology(ontology);
			rewriting_only = true;
		}

		if (rewriting_only) {
			// do nothing
		} else {
			runDLV(inputProgram);
		}
	}

	private void handleOntology(OWLOntology ontology) {
		LDLPOntologyCompiler rewriter = new LDLPOntologyCompiler();
		List<ProgramStatement> datalog = rewriter.compile(ontology);
		// DLProgramStorer storer = new DLProgramStorerImpl();
		DLProgramStorer storer = new RLProgramStorerImpl();
		// DatalogToStringHelper helper = new DatalogToStringHelper();

		datalogFile = ontologyFile + ".dlv";
		try (FileWriter writer = new FileWriter(datalogFile)) {
			storer.store(datalog, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handleSparql(OWLOntology ontology, DLVInputProgram inputProgram) {

		String queryText = "";

		int j = sparqlFile.lastIndexOf('/');
		String dlpTag = sparqlFile;
		if (j >= 0) {
			dlpTag = sparqlFile.substring(j + 1);
		}

		datalogFile = ontologyFile + "-" + dlpTag + "-rl.dlv";

		try (BufferedReader reader = new BufferedReader(new FileReader(
				sparqlFile)); //
				FileWriter w = new FileWriter(datalogFile) //
		) {
			queryText = CharStreams.toString(reader);
			Query query = QueryFactory.create(queryText, Syntax.syntaxARQ);
			SparqlCompiler sparqlCompiler = new SparqlCompiler();

			LDLPOntologyCompiler compiler = new LDLPOntologyCompiler();
			List<ProgramStatement> datalogClauses = compiler.compile(ontology);

			DLProgramStorer storer = new DLProgramStorerImpl();

			storer.store(datalogClauses, w);

			Clause drewQuery = sparqlCompiler.compileQuery(query);
			LDLPQueryCompiler queryCompiler = new LDLPQueryCompiler();
			Clause drewRLQuery = queryCompiler.compileQuery(drewQuery);
			storer.store(drewRLQuery, w);

			inputProgram.addFile(datalogFile);

			filter = "ans";

		} catch (IOException e) {
			e.printStackTrace();
		}
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
			parser.setOntology(ontology);
			elprogram = parser.program();
			kb.setProgram(elprogram);

			DLProgram datalog;

			long t0 = System.currentTimeMillis();

			RLProgramKBCompiler compiler = new RLProgramKBCompiler();
			datalog = compiler.rewrite(kb);

			int j = dlpFile.lastIndexOf('/');
			String dlpTag = dlpFile;
			if (j >= 0) {
				dlpTag = dlpFile.substring(j + 1);
			}

			datalogFile = ontologyFile + "-" + dlpTag + "-rl.dlv";

			double currentMemory = ((double) ((double) (Runtime.getRuntime()
					.totalMemory() / 1024) / 1024))
					- ((double) ((double) (Runtime.getRuntime().freeMemory() / 1024) / 1024));

			if (verbose) {
				System.err.println("#current memory = " + currentMemory + "M");
			}

			FileWriter w = new FileWriter(datalogFile);
			DLProgramStorer storer = new DLProgramStorerImpl();
			storer.store(datalog, w);
			w.close();

			long t1 = System.currentTimeMillis();

			rewritingTime = t1 - t0;
			if (verbose) {
				System.err.println("#rewrting time = " + rewritingTime + "ms");
			}

			inputProgram.addFile(datalogFile);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	void runDLV(DLVInputProgram inputProgram) {
		DLVInvocation invocation = DLVWrapper.getInstance().createInvocation(
				dlvPath);

		try {
			long t0 = System.currentTimeMillis();
			invocation.setInputProgram(inputProgram);

			if (nModels != -1){
				invocation.setNumberOfModels(nModels);
			}
			
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

			if (maxInt != -1) {
				invocation.setMaxint(maxInt);
			}

			if (semantics.equals("wf"))
				invocation.addOption("-wf");

			invocation.subscribe(new ModelHandler() {

				@Override
				public void handleResult(DLVInvocation paramDLVInvocation,
						ModelResult modelResult) {
					if (dlvHandlerStartTime == 0)
						dlvHandlerStartTime = System.currentTimeMillis();


					// System.out.println(nModels);
					System.out.print("{ ");
					Model model = (Model) modelResult;
					// ATTENTION !!! this is necessary and stupid, should we
					// report a bug to DLVWrapper?
					model.beforeFirst();
					while (model.hasMorePredicates()) {

						Predicate predicate = model.nextPredicate();
						while (predicate.hasMoreLiterals()) {

							Literal literal = predicate.nextLiteral();

							/**
							 * Instead of using a dedicated parser, we can
							 * actually access the literal directly by: <code>
							 	predicate.name();
							 	literal.attributes();
							 </code>
							 */

							DLProgramParser parser = new DLProgramParser(
									new StringReader(literal.toString()));

							LDLPProgramQueryResultDecompiler decompiler = new LDLPProgramQueryResultDecompiler();

							try {
								org.semanticweb.drew.dlprogram.model.Literal decompileLiteral = decompiler
										.decompileLiteral(parser.literal());
								System.out.print(decompileLiteral);
								System.out.print(" ");
							} catch (ParseException e) {
								e.printStackTrace();
							}

						}
					}

					System.out.println("}");
					System.out.println();

					dlvHandlerEndTime = System.currentTimeMillis();
				}
			});

			invocation.run();

			invocation.waitUntilExecutionFinishes();

			List<DLVError> dlvErrors = invocation.getErrors();
			if (dlvErrors.size() > 0)
				System.err.println(dlvErrors);

			long t1 = System.currentTimeMillis();

			dlvTotalTime = t1 - t0;

			long dlvHandlerTime = dlvHandlerEndTime - dlvHandlerStartTime;
			if (verbose) {
				System.err.println("#dlv running time = "
						+ (dlvTotalTime - dlvHandlerTime) + "ms");
				System.err.println("#postprocess time = " + dlvHandlerTime
						+ "ms");
			}

		} catch (DLVInvocationException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleCQ(OWLOntology ontology, DLVInputProgram inputProgram) {
		throw new UnsupportedOperationException(
				"not implemented yet! try using `drew -el`");
	}

	void printUsage() {

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
