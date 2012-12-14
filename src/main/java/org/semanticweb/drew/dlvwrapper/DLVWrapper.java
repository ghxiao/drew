package org.semanticweb.drew.dlvwrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.code.regex.NamedMatcher;
import com.google.code.regex.NamedPattern;

// using the offical DLVWrapper instead
@Deprecated
public class DLVWrapper {
	private String dlvPath;

	private String program;

	private static final Logger logger = LoggerFactory.getLogger(DLVWrapper.class);

	private Runtime runtime = Runtime.getRuntime();

	private String predicateRegex = "(?<predicate>[a-z][a-zA-Z\\d_]*)";

	private String termRegex = "(?<term>[a-z][a-zA-Z\\d_]*)";

	private String literalRegex = String.format("(?<literal>%s(\\((%s(,%s)*)?\\))?)",
			predicateRegex, termRegex, termRegex);

	private String literalListRegex = String.format("(?<literalList>%s(,\\s*%s)*)",
			literalRegex, literalRegex);

	private String lineRegex = String.format("(?<true>True:)?\\s*(\\{%s\\})\\s*",
			literalListRegex);

	private String trueLineRegex = String.format("True:\\s*(\\{%s\\})\\s*",
			literalListRegex);

	public DLVWrapper() {

	}

	/**
	 * @return the dlvPath
	 */
	public String getDlvPath() {
		return dlvPath;
	}

	/**
	 * @param dlvPath
	 *            the dlvPath to set
	 */
	public void setDlvPath(String dlvPath) {
		this.dlvPath = dlvPath;
	}

	/**
	 * @return the program
	 */
	public String getProgram() {
		return program;
	}

	/**
	 * @param program
	 *            the program to set
	 */
	public void setProgram(String program) {
		this.program = program;
	}

	public String getVersion() throws DLVInvocationException {
		String version = "The following warning(s) occurred:\n";
		try {
			Process dlv = this.runtime.exec(this.dlvPath);
			BufferedReader localBufferedReader = new BufferedReader(
					new InputStreamReader(dlv.getInputStream()));
			String str = localBufferedReader.readLine();
			if (str == null)
				throw new DLVInvocationException(
						"An error is occurred calling DLV.");
			version = str;
			dlv.waitFor();
		} catch (IOException | InterruptedException localIOException) {
			throw new DLVInvocationException(
					"An error is occurred calling DLV: "
							+ localIOException.getMessage());
		}
        return version;
	}

	public List<Literal> queryWFS(String queryStr)
			throws DLVInvocationException {
		return queryWFS(queryStr, "");
	}

	// Stable Model Semantics
	// Now only return the first model
	// TODO: return more models
	public List<Literal> querySM(String queryStr, String filter)
			throws DLVInvocationException {
		List<Literal> result = new ArrayList<>();

		// boolean result = false;
		try {
			String[] params = { dlvPath, "--", "-filter=" + filter };

			logger.debug("Run DLV with parameters:\n {} {} {}", new String[] {
					params[0], params[1], params[2] });
			Process dlv = this.runtime.exec(params);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					dlv.getInputStream()));

			PrintWriter writer = new PrintWriter(dlv.getOutputStream());
			writer.write(program);

			logger.debug("DLV Input:\n{}", program);

			writer.flush();
			writer.close();

			String line;

			while ((line = reader.readLine()) != null) {
				logger.debug("DLV Output: {}", line);

				DLProgramParser parser = new DLProgramParser(
						new StringReader(line));
				List<Literal> model = null;
				try {
					model = parser.getModel();
					return model;
				} catch (ParseException e) {
					// DO Nothing
					// Just Try Next
				}

				// NamedPattern linePattern = NamedPattern.compile(lineRegex);
				// NamedMatcher lineMatcher = linePattern.matcher(line);
				// if (lineMatcher.find()) {
				// String lits = lineMatcher.group("literalList");
				// System.out.println("literalList " + lits);
				// NamedPattern literalPattern = NamedPattern
				// .compile(literalRegex);
				// NamedMatcher literalMatcher = literalPattern.matcher(lits);
				// while (literalMatcher.find()) {
				// String lit = literalMatcher.group("literal");
				// // System.out.println("literal " + lit);
				// DLProgramParser parser = new DLProgramParser(
				// new StringReader(lit));
				// Literal literal = parser.literal();
				// result.add(literal);
				// // if (lit.equals(queryStr)) {
				// // logger.debug("Query \"{}\" found", queryStr);
				// // result = true;
				// // break;
				// // }
				// }
				// }
			}

			BufferedReader errorReader = new BufferedReader(
					new InputStreamReader(dlv.getErrorStream()));

			String message = "";

			while ((line = errorReader.readLine()) != null) {
				message += line + "\n";
			}

			if (message.length() > 0) {
				logger.error("DLV ERROR: {}", message);
				throw new DLVInvocationException(
						"An error is occurred calling DLV: " + message);
			}

			dlv.waitFor();
		} catch (IOException | InterruptedException ex) {
			throw new DLVInvocationException(
					"An error is occurred calling DLV: " + ex.getMessage());
		}

        return result;

	}

	// Well founded Semantics
	public List<Literal> queryWFS(String queryStr, String filter)
			throws DLVInvocationException {

		List<Literal> result = new ArrayList<>();

		// boolean result = false;
		try {
			String[] params = { dlvPath, "-wf", "--", "-filter=" + filter };

			logger
					.debug("Run DLV with parameters:\n {} {} {} {}",
							new String[] { params[0], params[1], params[2],
									params[3] });
			Process dlv = this.runtime.exec(params);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					dlv.getInputStream()));

			PrintWriter writer = new PrintWriter(dlv.getOutputStream());
			writer.write(program);

			logger.debug("DLV Input:\n{}", program);

			writer.flush();
			writer.close();

			String line;

			while ((line = reader.readLine()) != null) {
				logger.info("DLV Output: {}", line);

				NamedPattern linePattern = NamedPattern.compile(lineRegex);
				NamedMatcher lineMatcher = linePattern.matcher(line);
				if (lineMatcher.find()) {
					String lits = lineMatcher.group("literalList");
					// System.out.println("literalList " + lits);
					NamedPattern literalPattern = NamedPattern
							.compile(literalRegex);
					NamedMatcher literalMatcher = literalPattern.matcher(lits);
					while (literalMatcher.find()) {
						String lit = literalMatcher.group("literal");
						// System.out.println("literal " + lit);
						DLProgramParser parser = new DLProgramParser(
								new StringReader(lit));
						Literal literal = parser.literal();
						result.add(literal);
						// if (lit.equals(queryStr)) {
						// logger.debug("Query \"{}\" found", queryStr);
						// result = true;
						// break;
						// }
					}
				}
			}

			BufferedReader errorReader = new BufferedReader(
					new InputStreamReader(dlv.getErrorStream()));

			String message = "";

			while ((line = errorReader.readLine()) != null) {
				message += line + "\n";
			}

			if (message.length() > 0) {
				logger.error("DLV ERROR: {}", message);
				throw new DLVInvocationException(
						"An error is occurred calling DLV: " + message);
			}

			dlv.waitFor();
		} catch (IOException | ParseException | InterruptedException ex) {
			throw new DLVInvocationException(
					"An error is occurred calling DLV: " + ex.getMessage());
		}

        return result;

	}

	public boolean isEntailed(String queryStr) throws DLVInvocationException {
		return isEntailed(queryStr, "");
	}

	// Well founded Semantics
	public boolean isEntailed(String queryStr, String filter)
			throws DLVInvocationException {

		boolean result = false;

		try {
			String[] params = { dlvPath, "-wf", "--", "-filter=" + filter };

			logger
					.debug("Run DLV with parameters:\n {} {} {} {}",
							new String[] { params[0], params[1], params[2],
									params[3] });
			Process dlv = this.runtime.exec(params);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					dlv.getInputStream()));

			PrintWriter writer = new PrintWriter(dlv.getOutputStream());
			writer.write(program);

			logger.debug("DLV Input:\n{}", program);

			writer.flush();
			writer.close();

			String line;

			NamedPattern linePattern = NamedPattern.compile(trueLineRegex);

			NamedPattern literalPattern = NamedPattern.compile(literalRegex);

			while ((line = reader.readLine()) != null) {
				logger.info("DLV Output: {}", line);

				NamedMatcher lineMatcher = linePattern.matcher(line);
				if (lineMatcher.find()) {
					String lits = lineMatcher.group("literalList");
					// System.out.println("literalList " + lits);

					NamedMatcher literalMatcher = literalPattern.matcher(lits);
					while (literalMatcher.find()) {
						String lit = literalMatcher.group("literal");
						// System.out.println("literal " + lit);

						if (lit.equals(queryStr)) {
							logger.debug("Query \"{}\" found", queryStr);
							result = true;
							break;
						}
					}
				}
			}

			BufferedReader errorReader = new BufferedReader(
					new InputStreamReader(dlv.getErrorStream()));

			String message = "";

			while ((line = errorReader.readLine()) != null) {
				message += line + "\n";
			}

			if (message.length() > 0) {
				logger.error("DLV ERROR: {}", message);
				throw new DLVInvocationException(
						"An error is occurred calling DLV: " + message);
			}

			dlv.waitFor();
		} catch (IOException | InterruptedException ex) {
			throw new DLVInvocationException(
					"An error is occurred calling DLV: " + ex.getMessage());
		}

        return result;

	}

	public void run() throws DLVInvocationException {
		String[] params = { dlvPath, "--" };
		run(params);
	}

	public void runWFS() throws DLVInvocationException {
		String[] params = { dlvPath, "-wf", "--" };
		run(params);
	}

	private void run(String[] params) throws DLVInvocationException {
		try {
			Process dlv = this.runtime.exec(params);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					dlv.getInputStream()));

			PrintWriter writer = new PrintWriter(dlv.getOutputStream());
			writer.write(program);
			writer.flush();
			writer.close();

			String str;

			while ((str = reader.readLine()) != null) {
				System.out.println(str);
			}

			dlv.waitFor();
		} catch (IOException | InterruptedException ex) {
			throw new DLVInvocationException(
					"An error is occurred calling DLV: " + ex.getMessage());
		}
    }

}
