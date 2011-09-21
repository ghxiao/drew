/* Generated By:JavaCC: Do not edit this line. DLProgramParser.java */
package org.semanticweb.drew.dlprogram.parser;

import org.semanticweb.drew.dlprogram.model.*;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.semanticweb.owlapi.model.OWLLogicalEntity;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class DLProgramParser implements DLProgramParserConstants {
 // final static Logger logger = LoggerFactory.getLogger(DLProgramParser.class);
  Map < String, String > namespaces = new HashMap < String, String > ();

  public void process() throws ParseException
  {}

  public void adjustDLInputOperationArity(DLProgram program)
  {
    for (DLInputSignature signature : program.getDLInputSignatures())
    {
      for (DLInputOperation op : signature.getOperations())
      {
        NormalPredicate inputPredicate = op.getInputPredicate();
        String name = inputPredicate.getName();
        int arity = CacheManager.getInstance().getArity(name);
        inputPredicate.setArity(arity);
      }
    }
  }

  final public String dlPredicate() throws ParseException {
  String name;
    if (jj_2_1(2147483647)) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VARIABLE:
        jj_consume_token(VARIABLE);
        break;
      case DLPREDICATE:
        jj_consume_token(DLPREDICATE);
        break;
      case IDENTIFIER:
        jj_consume_token(IDENTIFIER);
        break;
      default:
        jj_la1[0] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
        String key = token.image;
        String namespace = namespaces.get(key);
      jj_consume_token(COLON);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VARIABLE:
        jj_consume_token(VARIABLE);
        break;
      case DLPREDICATE:
        jj_consume_token(DLPREDICATE);
        break;
      case IDENTIFIER:
        jj_consume_token(IDENTIFIER);
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
        name = namespace + token.image;
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
      case VARIABLE:
      case DLPREDICATE:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case VARIABLE:
          jj_consume_token(VARIABLE);
          break;
        case DLPREDICATE:
          jj_consume_token(DLPREDICATE);
          break;
        case IDENTIFIER:
          jj_consume_token(IDENTIFIER);
          break;
        default:
          jj_la1[2] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      name = token.image;
        break;
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    {if (true) return name;}
    throw new Error("Missing return statement in function");
  }

  final public DLInputOperation dlInputOperation() throws ParseException {
  DLInputOperation op = new DLInputOperation();
  String name;
    name = dlPredicate();
      //logger.debug("dlPredicat: " + token.image);
      //FIXME: role is possible here
      OWLLogicalEntity dlPredicate = OWLManager.getOWLDataFactory().getOWLClass(IRI.create(name));
      op.setDLPredicate(dlPredicate);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case UPLUS:
      jj_consume_token(UPLUS);
        op.setType(DLInputOperator.U_PLUS);
      break;
    case UMINUS:
      jj_consume_token(UMINUS);
        op.setType(DLInputOperator.U_MINUS);
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
      name = dlPredicate();
      //The arity will be set later
      int arity = - 1;
      NormalPredicate predicate = CacheManager.getInstance().getPredicate(name, arity);
      op.setInputPredicate(predicate);
    {if (true) return op;}
    throw new Error("Missing return statement in function");
  }

  final public DLInputSignature dlInputSignature() throws ParseException {
  DLInputSignature signature = new DLInputSignature();
  DLInputOperation op;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
    case VARIABLE:
    case DLPREDICATE:
      op = dlInputOperation();
      signature.getOperations().add(op);
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case CONJUNCTION:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_1;
        }
        jj_consume_token(CONJUNCTION);
        op = dlInputOperation();
        signature.getOperations().add(op);
      }
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
    {if (true) return signature;}
    throw new Error("Missing return statement in function");
  }

  final public DLAtomPredicate dlAtomPredicate() throws ParseException {
  DLAtomPredicate predicate = new DLAtomPredicate();
  DLInputSignature signature;
  String name;
  OWLLogicalEntity query;
    jj_consume_token(DL_ATOM);
    jj_consume_token(LEFT_SQUARE_BRACKET);
    signature = dlInputSignature();
      predicate.setInputSignature(signature);
    jj_consume_token(31);
    name = dlPredicate();
      //FIXME: can be property
      query = OWLManager.getOWLDataFactory().getOWLClass(IRI.create(name));
      predicate.setQuery(query);
    jj_consume_token(RIGHT_SQUARE_BRACKET);
    {if (true) return predicate;}
    throw new Error("Missing return statement in function");
  }

  final public Constant constant() throws ParseException {
  String name;
  int type;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER:
      jj_consume_token(INTEGER);
      name = token.image;
      type = Types.INTEGER;
      {if (true) return CacheManager.getInstance().getConstant(name, type);}
      break;
    case STRING:
      jj_consume_token(STRING);
      name = token.image.substring(1, token.image.length() - 1);
      type = Types.VARCHAR;
      {if (true) return CacheManager.getInstance().getConstant(name, type);}
      break;
    case IDENTIFIER:
      jj_consume_token(IDENTIFIER);
      name = token.image;
      type = Types.VARCHAR;
      {if (true) return CacheManager.getInstance().getConstant(name, type);}
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }

    throw new Error("Missing return statement in function");
  }

  final public Term variable() throws ParseException {
    jj_consume_token(VARIABLE);
    {if (true) return CacheManager.getInstance().getVariable(token.image);}
    throw new Error("Missing return statement in function");
  }

  final public Functor functor() throws ParseException {
  Functor functor = new Functor();
    jj_consume_token(IDENTIFIER);
    functor.setName(token.image);
    {if (true) return functor;}
    throw new Error("Missing return statement in function");
  }

  final public Function function() throws ParseException {
  Function function = new Function();
  Functor functor;
  Term term;
    functor = functor();
    function.setFunctor(functor);
    jj_consume_token(LEFTBRACKET);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
    case INTEGER:
    case STRING:
    case VARIABLE:
    case LEFTBRACKET:
      term = term();
      function.getTerms().add(term);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case CONJUNCTION:
          ;
          break;
        default:
          jj_la1[8] = jj_gen;
          break label_2;
        }
        jj_consume_token(CONJUNCTION);
        term = term();
        function.getTerms().add(term);
      }
      break;
    default:
      jj_la1[9] = jj_gen;
      ;
    }
    jj_consume_token(RIGHTBRACKET);
    {if (true) return function;}
    throw new Error("Missing return statement in function");
  }

  final public Term unary() throws ParseException {
  Term term;
    if (jj_2_2(2147483647)) {
      term = function();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
      case INTEGER:
      case STRING:
        term = constant();
        break;
      case VARIABLE:
        term = variable();
        break;
      case LEFTBRACKET:
        jj_consume_token(LEFTBRACKET);
        term = additive();
        jj_consume_token(RIGHTBRACKET);
        break;
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    {if (true) return term;}
    throw new Error("Missing return statement in function");
  }

  final public Term multiplicative() throws ParseException {
  Term left, right;
  Token token;
    left = unary();

    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TIMES:
      case DIVIDE:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TIMES:
        token = jj_consume_token(TIMES);
        break;
      case DIVIDE:
        token = jj_consume_token(DIVIDE);
        break;
      default:
        jj_la1[12] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      right = unary();
      Function function = new Function();
      Functor functor = new Functor();
      functor.setName(token.image);
      function.setFunctor(functor);
      function.getTerms().add(left);
      function.getTerms().add(right);
      left = function;
    }
    {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final public Term additive() throws ParseException {
  Term left, right;
  Token token;
    left = multiplicative();

    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        jj_la1[13] = jj_gen;
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        token = jj_consume_token(PLUS);
        break;
      case MINUS:
        token = jj_consume_token(MINUS);
        break;
      default:
        jj_la1[14] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      right = multiplicative();
      Function function = new Function();
      Functor functor = new Functor();
      functor.setName(token.image);
      function.setFunctor(functor);
      function.getTerms().add(left);
      function.getTerms().add(right);
      left = function;
    }
    {if (true) return left;}
    throw new Error("Missing return statement in function");
  }

  final public Term term() throws ParseException {
  Term term;
    term = additive();
    {if (true) return term;}
    throw new Error("Missing return statement in function");
  }

//Predicate predicate() :
//{
//	Predicate predicate = new Predicate();
//}
//{
//	<IDENTIFIER>
//	{
//		predicate.setName(token.image);
//		return predicate;
//	}
//}
  final public Literal literal() throws ParseException {
  Literal literal = new Literal();
  Predicate predicate = null;
  String name = null;
  Term term;
  int arity = 0;
  boolean isDLAtomPredicate = false;
  boolean neg = false;
    if (jj_2_3(2147483647)) {
      term = term();
    literal.getTerms().add(term);
      jj_consume_token(COMPARISON);
    predicate = CacheManager.getInstance().getPredicate(token.image, 2);
    literal.setPredicate(predicate);
      term = term();
    literal.getTerms().add(term);
    {if (true) return literal;}
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DL_ATOM:
      case IDENTIFIER:
      case MINUS:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DL_ATOM:
          predicate = dlAtomPredicate();
      isDLAtomPredicate = true;
          break;
        case IDENTIFIER:
        case MINUS:
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case MINUS:
            jj_consume_token(MINUS);
          neg = true;
            break;
          default:
            jj_la1[15] = jj_gen;
            ;
          }
          jj_consume_token(IDENTIFIER);
//      	if(!neg)
//      	{
                name = token.image;
//    	}else
//    	{
//			name = "-" + token.image;
//    	}
                if(neg)
                {
                        literal.setNegative(true);
                }
          break;
        default:
          jj_la1[16] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LEFTBRACKET:
          jj_consume_token(LEFTBRACKET);
          term = term();
      literal.getTerms().add(term);
      arity++;
          label_5:
          while (true) {
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case CONJUNCTION:
              ;
              break;
            default:
              jj_la1[17] = jj_gen;
              break label_5;
            }
            jj_consume_token(CONJUNCTION);
            term = term();
        literal.getTerms().add(term);
        arity++;
          }
          jj_consume_token(RIGHTBRACKET);
          break;
        default:
          jj_la1[18] = jj_gen;
          ;
        }
    if (!isDLAtomPredicate)
    {
      predicate = CacheManager.getInstance().getPredicate(name, arity);
    }
    else
    {
      predicate.setArity(arity);
    }
    literal.setPredicate(predicate);
    {if (true) return literal;}
        break;
      default:
        jj_la1[19] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

  final public Clause clause() throws ParseException {
  Clause clause = new Clause();
  Literal literal;
  boolean not = false;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case DL_ATOM:
    case IDENTIFIER:
    case INTEGER:
    case STRING:
    case VARIABLE:
    case MINUS:
    case LEFTBRACKET:
      literal = literal();
      clause.setHead(literal);
      break;
    default:
      jj_la1[20] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IMPLY:
      jj_consume_token(IMPLY);
      // initialize the not state
      not = false;
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NAF:
        jj_consume_token(NAF);
        not = true;
        break;
      default:
        jj_la1[21] = jj_gen;
        ;
      }
      literal = literal();
      if (not)
      {
        clause.getNegativeBody().add(literal);
      }
      else
      {
        clause.getPositiveBody().add(literal);
      }
      label_6:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case CONJUNCTION:
          ;
          break;
        default:
          jj_la1[22] = jj_gen;
          break label_6;
        }
        jj_consume_token(CONJUNCTION);
        // initialize the not state
        not = false;
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NAF:
          jj_consume_token(NAF);
          not = true;
          break;
        default:
          jj_la1[23] = jj_gen;
          ;
        }
        literal = literal();
        if (not)
        {
          clause.getNegativeBody().add(literal);
        }
        else
        {
          clause.getPositiveBody().add(literal);
        }
      }
      break;
    default:
      jj_la1[24] = jj_gen;
      ;
    }
    jj_consume_token(ENDOFSTATEMENT);
    if (clause.getBody().size() == 0)
    {
      //Why we need this?
      //clause.getPositiveBody().add(Literal.TRUE);
    }
    {if (true) return clause;}
    throw new Error("Missing return statement in function");
  }

  final public void namespace() throws ParseException {
  String key, value;
    jj_consume_token(NAMESPACE);
    jj_consume_token(LEFTBRACKET);
    jj_consume_token(STRING);
    key = token.image.substring(1, token.image.length() - 1);
    jj_consume_token(CONJUNCTION);
    jj_consume_token(STRING);
    value = token.image.substring(1, token.image.length() - 1);
    jj_consume_token(RIGHTBRACKET);
    jj_consume_token(ENDOFSTATEMENT);
    namespaces.put(key, value);
  }

  final public DLProgram program() throws ParseException {
  DLProgram program = new DLProgram();
  Clause clause;
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NAMESPACE:
        ;
        break;
      default:
        jj_la1[25] = jj_gen;
        break label_7;
      }
      namespace();
    }
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IMPLY:
      case ENDOFSTATEMENT:
      case DL_ATOM:
      case IDENTIFIER:
      case INTEGER:
      case STRING:
      case VARIABLE:
      case MINUS:
      case LEFTBRACKET:
        ;
        break;
      default:
        jj_la1[26] = jj_gen;
        break label_8;
      }
      clause = clause();
      if (clause.getHead().equals(Literal.FALSE) && (clause.getPositiveBody().contains(Literal.TRUE)))
      { // skip empty clause
      }
      else
      {
        program.getClauses().add(clause);
      }
    }
    jj_consume_token(0);
    adjustDLInputOperationArity(program);
    {if (true) return program;}
    throw new Error("Missing return statement in function");
  }

  final public List < Literal > getModel() throws ParseException {
  List < Literal > literals = new ArrayList < Literal > ();
  Literal literal;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
      jj_consume_token(TRUE);
      jj_consume_token(COLON);
      break;
    default:
      jj_la1[27] = jj_gen;
      ;
    }
    jj_consume_token(32);
    literal = literal();
    literals.add(literal);
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CONJUNCTION:
        ;
        break;
      default:
        jj_la1[28] = jj_gen;
        break label_9;
      }
      jj_consume_token(CONJUNCTION);
      literal = literal();
      literals.add(literal);
    }
    jj_consume_token(33);
    {if (true) return literals;}
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_3R_15() {
    if (jj_scan_token(CONJUNCTION)) return true;
    if (jj_3R_11()) return true;
    return false;
  }

  private boolean jj_3_1() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(21)) {
    jj_scanpos = xsp;
    if (jj_scan_token(30)) {
    jj_scanpos = xsp;
    if (jj_scan_token(17)) return true;
    }
    }
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  private boolean jj_3R_28() {
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  private boolean jj_3R_11() {
    if (jj_3R_14()) return true;
    return false;
  }

  private boolean jj_3R_13() {
    if (jj_3R_11()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_15()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_27() {
    if (jj_scan_token(STRING)) return true;
    return false;
  }

  private boolean jj_3R_19() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(24)) {
    jj_scanpos = xsp;
    if (jj_scan_token(25)) return true;
    }
    if (jj_3R_18()) return true;
    return false;
  }

  private boolean jj_3R_10() {
    if (jj_3R_12()) return true;
    if (jj_scan_token(LEFTBRACKET)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_13()) jj_scanpos = xsp;
    if (jj_scan_token(RIGHTBRACKET)) return true;
    return false;
  }

  private boolean jj_3_3() {
    if (jj_3R_11()) return true;
    if (jj_scan_token(COMPARISON)) return true;
    return false;
  }

  private boolean jj_3R_26() {
    if (jj_scan_token(INTEGER)) return true;
    return false;
  }

  private boolean jj_3R_16() {
    if (jj_3R_18()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_19()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_24() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_26()) {
    jj_scanpos = xsp;
    if (jj_3R_27()) {
    jj_scanpos = xsp;
    if (jj_3R_28()) return true;
    }
    }
    return false;
  }

  private boolean jj_3_2() {
    if (jj_3R_10()) return true;
    return false;
  }

  private boolean jj_3R_23() {
    if (jj_scan_token(LEFTBRACKET)) return true;
    if (jj_3R_14()) return true;
    if (jj_scan_token(RIGHTBRACKET)) return true;
    return false;
  }

  private boolean jj_3R_22() {
    if (jj_3R_25()) return true;
    return false;
  }

  private boolean jj_3R_12() {
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  private boolean jj_3R_21() {
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3R_17() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(22)) {
    jj_scanpos = xsp;
    if (jj_scan_token(23)) return true;
    }
    if (jj_3R_16()) return true;
    return false;
  }

  private boolean jj_3R_20() {
    if (jj_3R_10()) return true;
    return false;
  }

  private boolean jj_3R_18() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_20()) {
    jj_scanpos = xsp;
    if (jj_3R_21()) {
    jj_scanpos = xsp;
    if (jj_3R_22()) {
    jj_scanpos = xsp;
    if (jj_3R_23()) return true;
    }
    }
    }
    return false;
  }

  private boolean jj_3R_14() {
    if (jj_3R_16()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_17()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_25() {
    if (jj_scan_token(VARIABLE)) return true;
    return false;
  }

  /** Generated Token Manager. */
  public DLProgramParserTokenManager token_source;
  JavaCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[29];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x40220000,0x40220000,0x40220000,0x40220000,0x30000000,0x200,0x40220000,0x160000,0x200,0x4360000,0x4360000,0x3000000,0x3000000,0xc00000,0xc00000,0x800000,0x824000,0x200,0x4000000,0x824000,0x4b64000,0x40,0x200,0x40,0x400,0x100,0x4b65400,0x80,0x200,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[3];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public DLProgramParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public DLProgramParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new JavaCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new DLProgramParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 29; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 29; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public DLProgramParser(java.io.Reader stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new DLProgramParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 29; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 29; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public DLProgramParser(DLProgramParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 29; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(DLProgramParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 29; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[34];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 29; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 34; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 3; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
