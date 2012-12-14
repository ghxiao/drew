package org.semanticweb.drew.default_logic.rewriter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.drew.default_logic.DefaultLogicKB;
import org.semanticweb.drew.default_logic.DefaultRule;
import org.semanticweb.drew.default_logic.OWLPredicate;
import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.Clause;
import org.semanticweb.drew.dlprogram.model.Constant;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.model.Literal;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;
import org.semanticweb.drew.dlprogram.model.ProgramStatement;
import org.semanticweb.drew.dlprogram.model.Term;
import org.semanticweb.drew.dlprogram.model.Variable;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;
import org.semanticweb.drew.el.reasoner.SROEL2DatalogRewriter;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLogicalEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

public class DefaultLogicKBRewriter {
	static final String POSTFIX_IN = "_in";
	static final String POSTFIX_OUT = "_out";

	private List<ProgramStatement> commonRules;

	NormalPredicate in = CacheManager.getInstance().getPredicate("in", 2);
	NormalPredicate out = CacheManager.getInstance().getPredicate("out", 2);
	private NormalPredicate im = CacheManager.getInstance().getPredicate("im", 2);
	private NormalPredicate dl = CacheManager.getInstance().getPredicate("dl", 3);
	private NormalPredicate isa = CacheManager.getInstance().getPredicate("isa", 2);
	private NormalPredicate dl_neg = CacheManager.getInstance().getPredicate("dl_neg",
			3);
	private NormalPredicate concl = CacheManager.getInstance().getPredicate("concl", 1);
	private NormalPredicate just = CacheManager.getInstance().getPredicate("just", 1);
	private NormalPredicate typing = CacheManager.getInstance().getPredicate("typing",
			1);

	private Variable X = CacheManager.getInstance().getVariable("X");
	Variable Y = CacheManager.getInstance().getVariable("Y");

	private Constant c_in = CacheManager.getInstance().getConstant("in");
	private Constant c_im = CacheManager.getInstance().getConstant("im");

	private ShortFormProvider sfp;

	// FIXME: in general, for a default ([a : b_1, ..., b_m] / [c] ),
	// every pre/just_i, concl can be a conjunction of literals. For simplicity,
	// we only consider the case that each conjunction only has one literal

	// im(X, c) :- dl(X, a, in), not dl_neg(X, b_1, in), ..., not dl_neg(X, b_m,
	// in).
    private boolean hasTyping = false;

	public DefaultLogicKBRewriter() {
		sfp = new SimpleShortFormProvider();
	}

	Constant toConstant(OWLPredicate p) {
		return CacheManager.getInstance().getConstant(
				unquote(sfp.getShortForm(p.getLogicalEntity())));
	}
	
	String unquote(String s){
		int len = s.length();
		if(s.charAt(0)=='<' && s.charAt(len-1)=='>'){
			return s.substring(1, len-1);
		} else {
			return s;
		}
	}

	/**
	 * rewrite a terminological default logic KB=<L,D> into a list of datalog
	 * rules
	 * 
	 * @param ontology
	 *            the ontology L
	 * @param dfs
	 *            the default rules D
	 * @return rewritten result in datalog rules
	 */
	public List<ProgramStatement> rewriteDefaultLogicKB(OWLOntology ontology,
			List<DefaultRule> dfs) {
		List<ProgramStatement> result = new ArrayList<>();

		SROEL2DatalogRewriter elRewriter = new SROEL2DatalogRewriter();

		DLProgram datalog_el = elRewriter.rewrite(ontology);
		List<Clause> datalog_dfs = rewrite(dfs);

		result.addAll(datalog_el.getStatements());
		result.addAll(datalog_dfs);
		if (hasTyping) {
			result.addAll(rulesFromFile("default.dl"));
		} else {
			result.addAll(rulesFromFile("default-no-typing.dl"));
		}
		result.addAll(rulesFromFile("el.dl"));
		result.addAll(rulesFromFile("el-i.dl"));
		result.addAll(rulesFromFile("el-i-n.dl"));
		return result;
	}

	public List<ProgramStatement> rewriteDefaultLogicKB(DefaultLogicKB kb) {
		return rewriteDefaultLogicKB(kb.getOntology(), kb.getDfRules());
	}

	List<Clause> rewrite(List<DefaultRule> dfs) {
		List<Clause> result = new ArrayList<>();
		for (DefaultRule df : dfs) {
			result.addAll(rewrite(df));
		}
		return result;
	}

	List<Clause> rewrite(DefaultRule df) {
		List<Clause> result = new ArrayList<>();
		// Literal pre = df.getPrerequisite().get(0);
		Literal conc = df.getConclusion().get(0);

		NormalPredicate p;

		final OWLPredicate concPredicate = conc.getPredicate().asOWLPredicate();

		// result.add(new Clause(
		// new Literal(concl, X), //
		// new Literal[] { new Literal(isa, X, toConstant(concPredicate)) }));

		result.add(new Clause(new Literal(concl, toConstant(concPredicate))));

		for (List<Literal> j : df.getJustifications()) {
			result.add(new Clause(//
					new Literal(just, toConstant(j.get(0).getPredicate()
							.asOWLPredicate()))));

		}

		if (df.getTyping().size() > 0) {
			hasTyping = true;
			result.add(new Clause(new Literal(typing, X), //
					new Literal[] { new Literal(isa, X,
							toConstant(df.getTyping().get(0).getPredicate()
									.asOWLPredicate())) }));
		}

		// "concl(X) :- isa(X, "conc")"
		// Literal h =

		// in(X, Y) :- not out(X, Y), nom(X), concl(Y), q(X)

		//
		// im(X, "Deny") :- dl(X, "UserRequest", im), not dl_neg(X, "Deny", in),
		// q(X).

		Literal head = new Literal(im, X, toConstant(df.getConclusion().get(0)
				.getPredicate().asOWLPredicate()));

		List<Literal> positiveBody = new ArrayList<>();

		for (Literal pre : df.getPrerequisite()) {
			p = pre.isNegative() ? dl_neg : dl;
			positiveBody.add(new Literal(p, X, //
					toConstant(pre.getPredicate().asOWLPredicate()), c_im));
		}

		for (Literal type : df.getTyping()) {
			positiveBody.add(new Literal(isa, X, //
					toConstant(type.getPredicate().asOWLPredicate())));
		}

		List<Literal> negativeBody = new ArrayList<>();

		for (List<Literal> justs : df.getJustifications()) {
			Literal just0 = justs.get(0);
			p = just0.isNegative() ? dl : dl_neg;
			negativeBody.add(new Literal(p, //
					X, //
					toConstant(just0.getPredicate().asOWLPredicate()), c_in));
		}

		Clause clause = new Clause(head, positiveBody, negativeBody);

		result.add(clause);
		return result;
	}

	protected OWLPredicate addPostfix(OWLPredicate p, String postfix) {
		IRI iri = p.getLogicalEntity().getIRI();
		IRI new_iri = IRI.create(iri.toString() + postfix);
		OWLLogicalEntity e = null;
		if (p.getArity() == 1) {
			e = OWLManager.getOWLDataFactory().getOWLClass(new_iri);
		} else if (p.getArity() == 2) {
			e = OWLManager.getOWLDataFactory().getOWLObjectProperty(new_iri);
		} else {
			throw new IllegalStateException();
		}
		return new OWLPredicate(e);

	}

	List<ProgramStatement> rulesFromFile(String fileName) {
		InputStream stream = DefaultLogicKBRewriter.class
				.getResourceAsStream(fileName);
		DLProgramParser parser = new DLProgramParser(stream);
		DLProgram program = null;
		try {
			program = parser.program();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return program.getStatements();

	}

	List<ProgramStatement> commonRules() {
		if (commonRules == null) {

			InputStream stream = DefaultLogicKBRewriter.class
					.getResourceAsStream("default.dl");
			DLProgramParser parser = new DLProgramParser(stream);
			DLProgram program = null;
			try {
				program = parser.program();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			commonRules = program.getStatements();
		}
		return commonRules;
	}

	String behalfPredicateName(List<Term> terms) {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Term t : terms) {
			if (!first)
				result.append("_");
			result.append(t.getName());
			first = false;
		}

		return result.toString();

	}

}
