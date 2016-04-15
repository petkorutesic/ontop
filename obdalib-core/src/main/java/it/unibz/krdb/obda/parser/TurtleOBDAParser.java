// $ANTLR 3.5.2 TurtleOBDA.g 2016-04-15 12:29:12

package it.unibz.krdb.obda.parser;

import it.unibz.krdb.obda.model.CQIE;
import it.unibz.krdb.obda.model.Constant;
import it.unibz.krdb.obda.model.Function;
import it.unibz.krdb.obda.model.Term;
import it.unibz.krdb.obda.model.OBDADataFactory;
import it.unibz.krdb.obda.model.DatatypeFactory;
import it.unibz.krdb.obda.model.OBDALibConstants;
import it.unibz.krdb.obda.model.Predicate;
import it.unibz.krdb.obda.model.URIConstant;
import it.unibz.krdb.obda.model.ValueConstant;
import it.unibz.krdb.obda.model.Variable;
import it.unibz.krdb.obda.model.Predicate.COL_TYPE;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;
import it.unibz.krdb.obda.model.impl.OBDAVocabulary;
import it.unibz.krdb.obda.utils.QueryUtils;
import it.unibz.krdb.obda.model.URITemplatePredicate;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class TurtleOBDAParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALPHA", "ALPHANUM", "AMPERSAND", 
		"APOSTROPHE", "ASTERISK", "AT", "BACKSLASH", "BASE", "BLANK", "BLANK_PREFIX", 
		"CARET", "CHAR", "COLON", "COMMA", "DECIMAL", "DECIMAL_NEGATIVE", "DECIMAL_POSITIVE", 
		"DIGIT", "DOLLAR", "DOUBLE", "DOUBLE_NEGATIVE", "DOUBLE_POSITIVE", "DOUBLE_SLASH", 
		"ECHAR", "EQUALS", "EXCLAMATION", "FALSE", "GREATER", "HASH", "ID", "ID_CORE", 
		"ID_START", "INTEGER", "INTEGER_NEGATIVE", "INTEGER_POSITIVE", "LCR_BRACKET", 
		"LESS", "LPAREN", "LSQ_BRACKET", "LTSIGN", "MINUS", "NAMESPACE", "NAME_CHAR", 
		"NAME_START_CHAR", "NCNAME", "NCNAME_EXT", "PERCENT", "PERIOD", "PLUS", 
		"PREFIX", "PREFIXED_NAME", "QUESTION", "QUOTE_DOUBLE", "QUOTE_SINGLE", 
		"RCR_BRACKET", "REFERENCE", "RPAREN", "RSQ_BRACKET", "RTSIGN", "SCHEMA", 
		"SEMI", "SLASH", "STRING_URI", "STRING_WITH_BRACKET", "STRING_WITH_CURLY_BRACKET", 
		"STRING_WITH_QUOTE", "STRING_WITH_QUOTE_DOUBLE", "TILDE", "TRUE", "UNDERSCORE", 
		"URI_PATH", "VARNAME", "WS", "'a'"
	};
	public static final int EOF=-1;
	public static final int T__77=77;
	public static final int ALPHA=4;
	public static final int ALPHANUM=5;
	public static final int AMPERSAND=6;
	public static final int APOSTROPHE=7;
	public static final int ASTERISK=8;
	public static final int AT=9;
	public static final int BACKSLASH=10;
	public static final int BASE=11;
	public static final int BLANK=12;
	public static final int BLANK_PREFIX=13;
	public static final int CARET=14;
	public static final int CHAR=15;
	public static final int COLON=16;
	public static final int COMMA=17;
	public static final int DECIMAL=18;
	public static final int DECIMAL_NEGATIVE=19;
	public static final int DECIMAL_POSITIVE=20;
	public static final int DIGIT=21;
	public static final int DOLLAR=22;
	public static final int DOUBLE=23;
	public static final int DOUBLE_NEGATIVE=24;
	public static final int DOUBLE_POSITIVE=25;
	public static final int DOUBLE_SLASH=26;
	public static final int ECHAR=27;
	public static final int EQUALS=28;
	public static final int EXCLAMATION=29;
	public static final int FALSE=30;
	public static final int GREATER=31;
	public static final int HASH=32;
	public static final int ID=33;
	public static final int ID_CORE=34;
	public static final int ID_START=35;
	public static final int INTEGER=36;
	public static final int INTEGER_NEGATIVE=37;
	public static final int INTEGER_POSITIVE=38;
	public static final int LCR_BRACKET=39;
	public static final int LESS=40;
	public static final int LPAREN=41;
	public static final int LSQ_BRACKET=42;
	public static final int LTSIGN=43;
	public static final int MINUS=44;
	public static final int NAMESPACE=45;
	public static final int NAME_CHAR=46;
	public static final int NAME_START_CHAR=47;
	public static final int NCNAME=48;
	public static final int NCNAME_EXT=49;
	public static final int PERCENT=50;
	public static final int PERIOD=51;
	public static final int PLUS=52;
	public static final int PREFIX=53;
	public static final int PREFIXED_NAME=54;
	public static final int QUESTION=55;
	public static final int QUOTE_DOUBLE=56;
	public static final int QUOTE_SINGLE=57;
	public static final int RCR_BRACKET=58;
	public static final int REFERENCE=59;
	public static final int RPAREN=60;
	public static final int RSQ_BRACKET=61;
	public static final int RTSIGN=62;
	public static final int SCHEMA=63;
	public static final int SEMI=64;
	public static final int SLASH=65;
	public static final int STRING_URI=66;
	public static final int STRING_WITH_BRACKET=67;
	public static final int STRING_WITH_CURLY_BRACKET=68;
	public static final int STRING_WITH_QUOTE=69;
	public static final int STRING_WITH_QUOTE_DOUBLE=70;
	public static final int TILDE=71;
	public static final int TRUE=72;
	public static final int UNDERSCORE=73;
	public static final int URI_PATH=74;
	public static final int VARNAME=75;
	public static final int WS=76;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public TurtleOBDAParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public TurtleOBDAParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return TurtleOBDAParser.tokenNames; }
	@Override public String getGrammarFileName() { return "TurtleOBDA.g"; }


	/** Map of directives */
	private HashMap<String, String> directives = new HashMap<String, String>();

	/** Additional atoms generated as the consequence of the complex nesting of bnodes*/
	private List<Function> additionalBNodeAtoms;

	/** All variables */
	private Set<Term> variableSet = new HashSet<Term>();

	/** A factory to construct the predicates and terms */
	private static final OBDADataFactory dfac = OBDADataFactoryImpl.getInstance();
	private static final DatatypeFactory dtfac = OBDADataFactoryImpl.getInstance().getDatatypeFactory();

	private String error = "";

	public String getError() {
	   return error;
	}

	protected void mismatch(IntStream input, int ttype, BitSet follow) throws RecognitionException {
	   throw new MismatchedTokenException(ttype, input);
	}

	public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow) throws RecognitionException {
	   throw e;
	}

	@Override
	public void recover(IntStream input, RecognitionException re) {
	   throw new RuntimeException(error);
	}

	@Override
	public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
	   String hdr = getErrorHeader(e);
	   String msg = getErrorMessage(e, tokenNames);
	   emitErrorMessage("Syntax error: " + msg + " Location: " + hdr);
	}

	@Override
	public void emitErrorMessage(String msg) {
	   error = msg;
	}
	    
	public Object recoverFromMismatchedTokenrecoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
	   throw new RecognitionException(input);
	}

	private String removeBrackets(String text) {
	   return text.substring(1, text.length()-1);
	}

		private Term construct(String text) {
		   Term toReturn = null;
		   final String PLACEHOLDER = "{}";
		   List<Term> terms = new LinkedList<Term>();
		   List<FormatString> tokens = parse(text);
		   int size = tokens.size();
		   if (size == 1) {
		      FormatString token = tokens.get(0);
		      if (token instanceof FixedString) {
		          ValueConstant uriTemplate = dfac.getConstantLiteral(token.toString()); // a single URI template
		          toReturn = dfac.getUriTemplate(uriTemplate);
		      }
		      else if (token instanceof ColumnString) {
		         ValueConstant uriTemplate = dfac.getConstantLiteral(PLACEHOLDER); // a single URI template
		         Variable column = dfac.getVariable(token.toString());
		         terms.add(0, uriTemplate);
		         terms.add(column);
		         toReturn = dfac.getUriTemplate(terms);
		      }
		   }
		   else {
		      StringBuilder sb = new StringBuilder();
		      for(FormatString token : tokens) {
		         if (token instanceof FixedString) { // if part of URI template
		            sb.append(token.toString());
		         }
		         else if (token instanceof ColumnString) {
		            sb.append(PLACEHOLDER);
		            Variable column = dfac.getVariable(token.toString());
		            terms.add(column);
		         }
		      }
		      ValueConstant uriTemplate = dfac.getConstantLiteral(sb.toString()); // complete URI template
		      terms.add(0, uriTemplate);
		      toReturn = dfac.getUriTemplate(terms);
		   }
		   return toReturn;
		}
		
	// Column placeholder pattern
	private static final String formatSpecifier = "\\{([^\\}]+)?\\}";
	private static Pattern chPattern = Pattern.compile(formatSpecifier);

	private List<FormatString> parse(String text) {
	   List<FormatString> toReturn = new ArrayList<FormatString>();
	   Matcher m = chPattern.matcher(text);
	   int i = 0;
	   while (i < text.length()) {
	      if (m.find(i)) {
	         if (m.start() != i) {
	            toReturn.add(new FixedString(text.substring(i, m.start())));
	         }
	         String value = m.group(1);
	         toReturn.add(new ColumnString(value));
	         i = m.end();
	      }
	      else {
	         toReturn.add(new FixedString(text.substring(i)));
	         break;
	      }
	   }
	   return toReturn;
	}

	private interface FormatString {
	   int index();
	   String toString();
	}

	private class FixedString implements FormatString {
	   private String s;
	   FixedString(String s) { this.s = s; }
	   @Override public int index() { return -1; }  // flag code for fixed string
	   @Override public String toString() { return s; }
	}

	private class ColumnString implements FormatString {
	   private String s;
	   ColumnString(String s) { this.s = s; }
	   @Override public int index() { return 0; }  // flag code for column string
	   @Override public String toString() { return s; }
	}

		//this function distinguishes curly bracket with 
		//back slash "\{" from curly bracket "{" 
		private int getIndexOfCurlyB(String str){
		   int i;
		   int j;
		   i = str.indexOf("{");
		   j = str.indexOf("\\{");
		      while((i-1 == j) &&(j != -1)){		
			i = str.indexOf("{",i+1);
			j = str.indexOf("\\{",j+1);		
		      }	
		  return i;
		}
		
		//in case of concat this function parses the literal 
		//and adds parsed constant literals and template literal to terms list
		private ArrayList<Term> addToTermsList(String str){
		   ArrayList<Term> terms = new ArrayList<Term>();
		   int i,j;
		   String st;
		   str = str.substring(1, str.length()-1);
		   while(str.contains("{")){
		      i = getIndexOfCurlyB(str);
		      if (i > 0){
		    	 st = str.substring(0,i);
		    	 st = st.replace("\\\\", "");
		         terms.add(dfac.getConstantLiteral(st));
		         str = str.substring(str.indexOf("{", i), str.length());
		      }else if (i == 0){
		         j = str.indexOf("}");
		         terms.add(dfac.getVariable(str.substring(1,j)));
		         str = str.substring(j+1,str.length());
		      } else {
		    	  break;
		      }
		   }
		   if(!str.equals("")){
		      str = str.replace("\\\\", "");
		      terms.add(dfac.getConstantLiteral(str));
		   }
		   return terms;
		}
		
		//this function returns nested concats 
		//in case of more than two terms need to be concatted
		private Term getNestedConcat(String str){
		   ArrayList<Term> terms = new ArrayList<Term>();
		   terms = addToTermsList(str);
		   if(terms.size() == 1){
		      Variable v = (Variable) terms.get(0);
	          variableSet.add(v);
	          return v;
		   }

		   Function f = dfac.getFunctionConcat(terms.get(0),terms.get(1));
	           for(int j=2;j<terms.size();j++){
	              f = dfac.getFunctionConcat(f,terms.get(j));
	           }

		   return f;
		}

	/**
	 * This methods construct an atom from a triple 
	 * 
	 * For the input (subject, pred, object), the result is 
	 * <ul>
	 *  <li> object(subject), if pred == rdf:type and subject is grounded ; </li>
	 *  <li> predicate(subject, object), if pred != rdf:type and predicate is grounded ; </li>
	 *  <li> triple(subject, pred, object), otherwise (it is a higher order atom). </li>
	 * </ul>
	 */
		private Function makeAtom(Term subject, Term pred, Term object) {
		     Function atom = null;

		        if (isRDFType(pred)) {
			             if (object instanceof  Function) {
			                  if(QueryUtils.isGrounded(object)) {
			                      ValueConstant c = ((ValueConstant) ((Function) object).getTerm(0));  // it has to be a URI constant
			                      Predicate predicate = dfac.getClassPredicate(c.getValue());
			                      atom = dfac.getFunction(predicate, subject);
			                  } else {
			                       atom = dfac.getTripleAtom(subject, pred, object);
			                  }
			             }
			             else if (object instanceof  Variable){
			                  Term uriOfPred = dfac.getUriTemplate(pred);
			                  Term uriOfObject = dfac.getUriTemplate(object);
			                  atom = dfac.getTripleAtom(subject, uriOfPred,  uriOfObject);
			              }
			             else {
			                  throw new IllegalArgumentException("parser cannot handle object " + object);
			              }
			        } else if( ! QueryUtils.isGrounded(pred) ){
			             atom = dfac.getTripleAtom(subject, pred,  object);
			        } else {
	                			             //Predicate predicate = dfac.getPredicate(pred.toString(), 2); // the data type cannot be determined here!
	                			             Predicate predicate;
	                			             if(pred instanceof Function) {
	                							 ValueConstant pr = (ValueConstant) ((Function) pred).getTerm(0);
	                							 if (object instanceof Variable) {
	                								 predicate = dfac.getPredicate(pr.getValue(), 2);
	                							 } else {
	                								 if (object instanceof Function) {
	                									 if (((Function) object).getFunctionSymbol() instanceof URITemplatePredicate) {

	                										 predicate = dfac.getObjectPropertyPredicate(pr.getValue());
	                									 } else {
	                										 predicate = dfac.getDataPropertyPredicate(pr.getValue());
	                									 }
	                								 }
	                									 else {
	                										 throw new IllegalArgumentException("parser cannot handle object " + object);
	                									 }
	                							 }
	                						 }else {
	                			                  throw new IllegalArgumentException("predicate should be a URI Function");
	                			             }
	                			             atom = dfac.getFunction(predicate, subject, object);
	                			       }
	                			       return atom;
		  }


	private static boolean isRDFType(Term pred) {
	//		if (pred instanceof Constant && ((Constant) pred).getValue().equals(OBDAVocabulary.RDF_TYPE)) {
	//			return true;
	//		}
			if (pred instanceof Function && ((Function) pred).getTerm(0) instanceof Constant ) {
				String c= ((Constant) ((Function) pred).getTerm(0)).getValue();
				return c.equals(OBDAVocabulary.RDF_TYPE);
			}	
			return false;
		}



	/**
	 * This method creates unique Bnode
	 *
	 */
	private Function createBNode() {
	    Function f;
	    List<Term> emptyTermList = new LinkedList<Term>();
	    f = dfac.getBNodeTemplate(emptyTermList);
	    return f;
	}




	// $ANTLR start "parse"
	// TurtleOBDA.g:399:1: parse returns [List<Function> value] : ( directiveStatement )* t1= triplesStatement (t2= triplesStatement )* EOF ;
	public final List<Function> parse() throws RecognitionException {
		List<Function> value = null;


		List<Function> t1 =null;
		List<Function> t2 =null;

		try {
			// TurtleOBDA.g:400:3: ( ( directiveStatement )* t1= triplesStatement (t2= triplesStatement )* EOF )
			// TurtleOBDA.g:400:5: ( directiveStatement )* t1= triplesStatement (t2= triplesStatement )* EOF
			{
			// TurtleOBDA.g:400:5: ( directiveStatement )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==AT) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// TurtleOBDA.g:400:5: directiveStatement
					{
					pushFollow(FOLLOW_directiveStatement_in_parse54);
					directiveStatement();
					state._fsp--;

					}
					break;

				default :
					break loop1;
				}
			}

			pushFollow(FOLLOW_triplesStatement_in_parse63);
			t1=triplesStatement();
			state._fsp--;


			      value =  t1;
			    
			// TurtleOBDA.g:404:7: (t2= triplesStatement )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= BLANK && LA2_0 <= BLANK_PREFIX)||LA2_0==LSQ_BRACKET||LA2_0==PREFIXED_NAME||(LA2_0 >= STRING_WITH_BRACKET && LA2_0 <= STRING_WITH_CURLY_BRACKET)) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// TurtleOBDA.g:404:8: t2= triplesStatement
					{
					pushFollow(FOLLOW_triplesStatement_in_parse76);
					t2=triplesStatement();
					state._fsp--;


					      List<Function> additionalTriples = t2;
					      if (additionalTriples != null) {
					        // If there are additional triple statements then just add to the existing body
					        List<Function> existingBody = value;
					        existingBody.addAll(additionalTriples);
					      }
					    
					}
					break;

				default :
					break loop2;
				}
			}

			match(input,EOF,FOLLOW_EOF_in_parse83); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "parse"



	// $ANTLR start "directiveStatement"
	// TurtleOBDA.g:414:1: directiveStatement : directive PERIOD ;
	public final void directiveStatement() throws RecognitionException {
		try {
			// TurtleOBDA.g:415:3: ( directive PERIOD )
			// TurtleOBDA.g:415:5: directive PERIOD
			{
			pushFollow(FOLLOW_directive_in_directiveStatement96);
			directive();
			state._fsp--;

			match(input,PERIOD,FOLLOW_PERIOD_in_directiveStatement98); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "directiveStatement"



	// $ANTLR start "triplesStatement"
	// TurtleOBDA.g:418:1: triplesStatement returns [List<Function> value] : triples ( WS )* PERIOD ;
	public final List<Function> triplesStatement() throws RecognitionException {
		List<Function> value = null;


		List<Function> triples1 =null;


		    additionalBNodeAtoms = new LinkedList<Function>();
		 
		try {
			// TurtleOBDA.g:429:3: ( triples ( WS )* PERIOD )
			// TurtleOBDA.g:429:5: triples ( WS )* PERIOD
			{
			pushFollow(FOLLOW_triples_in_triplesStatement128);
			triples1=triples();
			state._fsp--;

			// TurtleOBDA.g:429:13: ( WS )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==WS) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// TurtleOBDA.g:429:13: WS
					{
					match(input,WS,FOLLOW_WS_in_triplesStatement130); 
					}
					break;

				default :
					break loop3;
				}
			}

			match(input,PERIOD,FOLLOW_PERIOD_in_triplesStatement133); 
			 value = triples1; 
			}


			     if (additionalBNodeAtoms != null && !additionalBNodeAtoms.isEmpty() ) {
			         // If there are additional attoms which were made through nesting of bnodes
			         value.addAll(additionalBNodeAtoms);
			     }
			  
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "triplesStatement"



	// $ANTLR start "directive"
	// TurtleOBDA.g:432:1: directive : ( base | prefixID );
	public final void directive() throws RecognitionException {
		try {
			// TurtleOBDA.g:433:3: ( base | prefixID )
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==AT) ) {
				int LA4_1 = input.LA(2);
				if ( (LA4_1==BASE) ) {
					alt4=1;
				}
				else if ( (LA4_1==PREFIX) ) {
					alt4=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}

			switch (alt4) {
				case 1 :
					// TurtleOBDA.g:433:5: base
					{
					pushFollow(FOLLOW_base_in_directive148);
					base();
					state._fsp--;

					}
					break;
				case 2 :
					// TurtleOBDA.g:434:5: prefixID
					{
					pushFollow(FOLLOW_prefixID_in_directive154);
					prefixID();
					state._fsp--;

					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "directive"



	// $ANTLR start "base"
	// TurtleOBDA.g:437:1: base : AT BASE uriref ;
	public final void base() throws RecognitionException {
		try {
			// TurtleOBDA.g:438:3: ( AT BASE uriref )
			// TurtleOBDA.g:438:5: AT BASE uriref
			{
			match(input,AT,FOLLOW_AT_in_base167); 
			match(input,BASE,FOLLOW_BASE_in_base169); 
			pushFollow(FOLLOW_uriref_in_base171);
			uriref();
			state._fsp--;

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "base"



	// $ANTLR start "prefixID"
	// TurtleOBDA.g:441:1: prefixID : AT PREFIX ( namespace | defaultNamespace ) uriref ;
	public final void prefixID() throws RecognitionException {
		ParserRuleReturnScope namespace2 =null;
		ParserRuleReturnScope defaultNamespace3 =null;
		String uriref4 =null;


		  String prefix = "";

		try {
			// TurtleOBDA.g:445:3: ( AT PREFIX ( namespace | defaultNamespace ) uriref )
			// TurtleOBDA.g:445:5: AT PREFIX ( namespace | defaultNamespace ) uriref
			{
			match(input,AT,FOLLOW_AT_in_prefixID189); 
			match(input,PREFIX,FOLLOW_PREFIX_in_prefixID191); 
			// TurtleOBDA.g:445:15: ( namespace | defaultNamespace )
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==NAMESPACE) ) {
				alt5=1;
			}
			else if ( (LA5_0==COLON) ) {
				alt5=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}

			switch (alt5) {
				case 1 :
					// TurtleOBDA.g:445:16: namespace
					{
					pushFollow(FOLLOW_namespace_in_prefixID194);
					namespace2=namespace();
					state._fsp--;

					 prefix = (namespace2!=null?input.toString(namespace2.start,namespace2.stop):null); 
					}
					break;
				case 2 :
					// TurtleOBDA.g:445:58: defaultNamespace
					{
					pushFollow(FOLLOW_defaultNamespace_in_prefixID200);
					defaultNamespace3=defaultNamespace();
					state._fsp--;

					 prefix = (defaultNamespace3!=null?input.toString(defaultNamespace3.start,defaultNamespace3.stop):null); 
					}
					break;

			}

			pushFollow(FOLLOW_uriref_in_prefixID205);
			uriref4=uriref();
			state._fsp--;


			      String uriref = uriref4;
			      directives.put(prefix.substring(0, prefix.length()-1), uriref); // remove the end colon
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "prefixID"



	// $ANTLR start "triples"
	// TurtleOBDA.g:451:1: triples returns [List<Function> value] : ( subject predicateObjectList[$subject.value] | LSQ_BRACKET l= predicateObjectList[localSubject] RSQ_BRACKET );
	public final List<Function> triples() throws RecognitionException {
		List<Function> value = null;


		List<Function> l =null;
		Term subject5 =null;
		List<Function> predicateObjectList6 =null;

		try {
			// TurtleOBDA.g:452:3: ( subject predicateObjectList[$subject.value] | LSQ_BRACKET l= predicateObjectList[localSubject] RSQ_BRACKET )
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( ((LA6_0 >= BLANK && LA6_0 <= BLANK_PREFIX)||LA6_0==PREFIXED_NAME||(LA6_0 >= STRING_WITH_BRACKET && LA6_0 <= STRING_WITH_CURLY_BRACKET)) ) {
				alt6=1;
			}
			else if ( (LA6_0==LSQ_BRACKET) ) {
				alt6=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}

			switch (alt6) {
				case 1 :
					// TurtleOBDA.g:452:5: subject predicateObjectList[$subject.value]
					{
					pushFollow(FOLLOW_subject_in_triples224);
					subject5=subject();
					state._fsp--;

					pushFollow(FOLLOW_predicateObjectList_in_triples226);
					predicateObjectList6=predicateObjectList(subject5);
					state._fsp--;


					      value = predicateObjectList6;
					    
					}
					break;
				case 2 :
					// TurtleOBDA.g:456:5: LSQ_BRACKET l= predicateObjectList[localSubject] RSQ_BRACKET
					{
					match(input,LSQ_BRACKET,FOLLOW_LSQ_BRACKET_in_triples236); 

					        Term localSubject = createBNode();
					    
					pushFollow(FOLLOW_predicateObjectList_in_triples246);
					l=predicateObjectList(localSubject);
					state._fsp--;


					        value = l;
					    
					match(input,RSQ_BRACKET,FOLLOW_RSQ_BRACKET_in_triples254); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "triples"



	// $ANTLR start "predicateObjectList"
	// TurtleOBDA.g:465:1: predicateObjectList[Term subject] returns [List<Function> value] : v1= verb l1= objectList ( SEMI v2= verb l2= objectList )* ;
	public final List<Function> predicateObjectList(Term subject) throws RecognitionException {
		List<Function> value = null;


		Term v1 =null;
		List<Term> l1 =null;
		Term v2 =null;
		List<Term> l2 =null;


		   value = new LinkedList<Function>();

		try {
			// TurtleOBDA.g:469:3: (v1= verb l1= objectList ( SEMI v2= verb l2= objectList )* )
			// TurtleOBDA.g:469:5: v1= verb l1= objectList ( SEMI v2= verb l2= objectList )*
			{
			pushFollow(FOLLOW_verb_in_predicateObjectList279);
			v1=verb();
			state._fsp--;

			pushFollow(FOLLOW_objectList_in_predicateObjectList285);
			l1=objectList();
			state._fsp--;


			      for (Term object : l1) {
			        Function atom = makeAtom(subject, v1, object);
			        value.add(atom);
			      }
			    
			// TurtleOBDA.g:475:5: ( SEMI v2= verb l2= objectList )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==SEMI) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// TurtleOBDA.g:475:6: SEMI v2= verb l2= objectList
					{
					match(input,SEMI,FOLLOW_SEMI_in_predicateObjectList294); 
					pushFollow(FOLLOW_verb_in_predicateObjectList298);
					v2=verb();
					state._fsp--;

					pushFollow(FOLLOW_objectList_in_predicateObjectList302);
					l2=objectList();
					state._fsp--;


					      for (Term object : l2) {
					        Function atom = makeAtom(subject, v2, object);
					        value.add(atom);
					      }
					    
					}
					break;

				default :
					break loop7;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "predicateObjectList"



	// $ANTLR start "verb"
	// TurtleOBDA.g:484:1: verb returns [Term value] : ( predicate | 'a' );
	public final Term verb() throws RecognitionException {
		Term value = null;


		Term predicate7 =null;

		try {
			// TurtleOBDA.g:485:3: ( predicate | 'a' )
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==PREFIXED_NAME||LA8_0==STRING_WITH_BRACKET) ) {
				alt8=1;
			}
			else if ( (LA8_0==77) ) {
				alt8=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 8, 0, input);
				throw nvae;
			}

			switch (alt8) {
				case 1 :
					// TurtleOBDA.g:485:5: predicate
					{
					pushFollow(FOLLOW_predicate_in_verb326);
					predicate7=predicate();
					state._fsp--;

					 value = predicate7; 
					}
					break;
				case 2 :
					// TurtleOBDA.g:486:5: 'a'
					{
					match(input,77,FOLLOW_77_in_verb334); 

					  Term constant = dfac.getConstantLiteral(OBDAVocabulary.RDF_TYPE);
					  value = dfac.getUriTemplate(constant);
					  
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "verb"



	// $ANTLR start "objectList"
	// TurtleOBDA.g:492:1: objectList returns [List<Term> value] : o1= object ( COMMA o2= object )* ;
	public final List<Term> objectList() throws RecognitionException {
		List<Term> value = null;


		Term o1 =null;
		Term o2 =null;


		  value = new ArrayList<Term>();

		try {
			// TurtleOBDA.g:496:3: (o1= object ( COMMA o2= object )* )
			// TurtleOBDA.g:496:5: o1= object ( COMMA o2= object )*
			{
			pushFollow(FOLLOW_object_in_objectList360);
			o1=object();
			state._fsp--;

			 value.add(o1); 
			// TurtleOBDA.g:496:42: ( COMMA o2= object )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==COMMA) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// TurtleOBDA.g:496:43: COMMA o2= object
					{
					match(input,COMMA,FOLLOW_COMMA_in_objectList365); 
					pushFollow(FOLLOW_object_in_objectList369);
					o2=object();
					state._fsp--;

					 value.add(o2); 
					}
					break;

				default :
					break loop9;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "objectList"



	// $ANTLR start "subject"
	// TurtleOBDA.g:501:1: subject returns [Term value] : ( resource | variable | blank );
	public final Term subject() throws RecognitionException {
		Term value = null;


		Term resource8 =null;
		Variable variable9 =null;
		Term blank10 =null;

		try {
			// TurtleOBDA.g:502:3: ( resource | variable | blank )
			int alt10=3;
			switch ( input.LA(1) ) {
			case PREFIXED_NAME:
			case STRING_WITH_BRACKET:
				{
				alt10=1;
				}
				break;
			case STRING_WITH_CURLY_BRACKET:
				{
				alt10=2;
				}
				break;
			case BLANK:
			case BLANK_PREFIX:
				{
				alt10=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 10, 0, input);
				throw nvae;
			}
			switch (alt10) {
				case 1 :
					// TurtleOBDA.g:502:5: resource
					{
					pushFollow(FOLLOW_resource_in_subject392);
					resource8=resource();
					state._fsp--;

					 value = resource8; 
					}
					break;
				case 2 :
					// TurtleOBDA.g:503:5: variable
					{
					pushFollow(FOLLOW_variable_in_subject400);
					variable9=variable();
					state._fsp--;

					 value = variable9; 
					}
					break;
				case 3 :
					// TurtleOBDA.g:504:5: blank
					{
					pushFollow(FOLLOW_blank_in_subject408);
					blank10=blank();
					state._fsp--;

					 value = blank10;
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "subject"



	// $ANTLR start "predicate"
	// TurtleOBDA.g:508:1: predicate returns [Term value] : resource ;
	public final Term predicate() throws RecognitionException {
		Term value = null;


		Term resource11 =null;

		try {
			// TurtleOBDA.g:509:3: ( resource )
			// TurtleOBDA.g:509:5: resource
			{
			pushFollow(FOLLOW_resource_in_predicate428);
			resource11=resource();
			state._fsp--;


			  	value = resource11; 
			//      Term nl = resource11;
			//      if (nl instanceof URIConstant) {
			//        URIConstant c = (URIConstant) nl;
			//        value = c.getValue();
			//      } else {
			//        throw new RuntimeException("Unsupported predicate syntax: " + nl.toString());
			//      }
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "predicate"



	// $ANTLR start "object"
	// TurtleOBDA.g:521:1: object returns [Term value] : ( simpleObject | bnodeObject );
	public final Term object() throws RecognitionException {
		Term value = null;


		Term simpleObject12 =null;
		Term bnodeObject13 =null;

		try {
			// TurtleOBDA.g:522:3: ( simpleObject | bnodeObject )
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( ((LA11_0 >= BLANK && LA11_0 <= BLANK_PREFIX)||(LA11_0 >= DECIMAL && LA11_0 <= DECIMAL_POSITIVE)||(LA11_0 >= DOUBLE && LA11_0 <= DOUBLE_POSITIVE)||LA11_0==FALSE||(LA11_0 >= INTEGER && LA11_0 <= INTEGER_POSITIVE)||LA11_0==PREFIXED_NAME||(LA11_0 >= STRING_WITH_BRACKET && LA11_0 <= STRING_WITH_CURLY_BRACKET)||LA11_0==STRING_WITH_QUOTE_DOUBLE||LA11_0==TRUE) ) {
				alt11=1;
			}
			else if ( (LA11_0==LSQ_BRACKET) ) {
				alt11=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 11, 0, input);
				throw nvae;
			}

			switch (alt11) {
				case 1 :
					// TurtleOBDA.g:522:5: simpleObject
					{
					pushFollow(FOLLOW_simpleObject_in_object447);
					simpleObject12=simpleObject();
					state._fsp--;

					value = simpleObject12; 
					}
					break;
				case 2 :
					// TurtleOBDA.g:523:5: bnodeObject
					{
					pushFollow(FOLLOW_bnodeObject_in_object455);
					bnodeObject13=bnodeObject();
					state._fsp--;

					value = bnodeObject13; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "object"



	// $ANTLR start "simpleObject"
	// TurtleOBDA.g:526:1: simpleObject returns [Term value] : ( resource | literal | typedLiteral | variable | blank );
	public final Term simpleObject() throws RecognitionException {
		Term value = null;


		Term resource14 =null;
		Term literal15 =null;
		Function typedLiteral16 =null;
		Variable variable17 =null;
		Term blank18 =null;

		try {
			// TurtleOBDA.g:527:3: ( resource | literal | typedLiteral | variable | blank )
			int alt12=5;
			switch ( input.LA(1) ) {
			case PREFIXED_NAME:
			case STRING_WITH_BRACKET:
				{
				alt12=1;
				}
				break;
			case DECIMAL:
			case DECIMAL_NEGATIVE:
			case DECIMAL_POSITIVE:
			case DOUBLE:
			case DOUBLE_NEGATIVE:
			case DOUBLE_POSITIVE:
			case FALSE:
			case INTEGER:
			case INTEGER_NEGATIVE:
			case INTEGER_POSITIVE:
			case STRING_WITH_QUOTE_DOUBLE:
			case TRUE:
				{
				alt12=2;
				}
				break;
			case STRING_WITH_CURLY_BRACKET:
				{
				int LA12_3 = input.LA(2);
				if ( (LA12_3==AT||LA12_3==REFERENCE) ) {
					alt12=3;
				}
				else if ( (LA12_3==COMMA||LA12_3==PERIOD||LA12_3==RSQ_BRACKET||LA12_3==SEMI||LA12_3==WS) ) {
					alt12=4;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 12, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case BLANK:
			case BLANK_PREFIX:
				{
				alt12=5;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 12, 0, input);
				throw nvae;
			}
			switch (alt12) {
				case 1 :
					// TurtleOBDA.g:527:5: resource
					{
					pushFollow(FOLLOW_resource_in_simpleObject474);
					resource14=resource();
					state._fsp--;

					 value = resource14; 
					}
					break;
				case 2 :
					// TurtleOBDA.g:528:5: literal
					{
					pushFollow(FOLLOW_literal_in_simpleObject482);
					literal15=literal();
					state._fsp--;

					 value = literal15; 
					}
					break;
				case 3 :
					// TurtleOBDA.g:529:5: typedLiteral
					{
					pushFollow(FOLLOW_typedLiteral_in_simpleObject491);
					typedLiteral16=typedLiteral();
					state._fsp--;

					 value = typedLiteral16; 
					}
					break;
				case 4 :
					// TurtleOBDA.g:530:5: variable
					{
					pushFollow(FOLLOW_variable_in_simpleObject499);
					variable17=variable();
					state._fsp--;

					 value = variable17; 
					}
					break;
				case 5 :
					// TurtleOBDA.g:531:5: blank
					{
					pushFollow(FOLLOW_blank_in_simpleObject507);
					blank18=blank();
					state._fsp--;

					 value = blank18; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "simpleObject"



	// $ANTLR start "bnodeObject"
	// TurtleOBDA.g:534:1: bnodeObject returns [Term value] : LSQ_BRACKET l1= predicateObjectList[localSubject] RSQ_BRACKET ;
	public final Term bnodeObject() throws RecognitionException {
		Term value = null;


		List<Function> l1 =null;

		try {
			// TurtleOBDA.g:535:3: ( LSQ_BRACKET l1= predicateObjectList[localSubject] RSQ_BRACKET )
			// TurtleOBDA.g:535:5: LSQ_BRACKET l1= predicateObjectList[localSubject] RSQ_BRACKET
			{
			match(input,LSQ_BRACKET,FOLLOW_LSQ_BRACKET_in_bnodeObject526); 

			        Term localSubject = createBNode();
			        value = localSubject;
			  
			pushFollow(FOLLOW_predicateObjectList_in_bnodeObject536);
			l1=predicateObjectList(localSubject);
			state._fsp--;



			        /*
			            we create bnode for this object and add it as a subject for all triples which can be created from
			            objectList elements
			        */
			        additionalBNodeAtoms.addAll(l1);

			     
			match(input,RSQ_BRACKET,FOLLOW_RSQ_BRACKET_in_bnodeObject546); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "bnodeObject"



	// $ANTLR start "resource"
	// TurtleOBDA.g:551:1: resource returns [Term value] : ( uriref | qname );
	public final Term resource() throws RecognitionException {
		Term value = null;


		String uriref19 =null;
		String qname20 =null;

		try {
			// TurtleOBDA.g:552:4: ( uriref | qname )
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==STRING_WITH_BRACKET) ) {
				alt13=1;
			}
			else if ( (LA13_0==PREFIXED_NAME) ) {
				alt13=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}

			switch (alt13) {
				case 1 :
					// TurtleOBDA.g:552:6: uriref
					{
					pushFollow(FOLLOW_uriref_in_resource564);
					uriref19=uriref();
					state._fsp--;

					 value = construct(uriref19); 
					}
					break;
				case 2 :
					// TurtleOBDA.g:553:6: qname
					{
					pushFollow(FOLLOW_qname_in_resource573);
					qname20=qname();
					state._fsp--;

					 value = construct(qname20); 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "resource"



	// $ANTLR start "uriref"
	// TurtleOBDA.g:558:1: uriref returns [String value] : STRING_WITH_BRACKET ;
	public final String uriref() throws RecognitionException {
		String value = null;


		Token STRING_WITH_BRACKET21=null;

		try {
			// TurtleOBDA.g:559:3: ( STRING_WITH_BRACKET )
			// TurtleOBDA.g:559:5: STRING_WITH_BRACKET
			{
			STRING_WITH_BRACKET21=(Token)match(input,STRING_WITH_BRACKET,FOLLOW_STRING_WITH_BRACKET_in_uriref598); 
			 value = removeBrackets((STRING_WITH_BRACKET21!=null?STRING_WITH_BRACKET21.getText():null)); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "uriref"



	// $ANTLR start "qname"
	// TurtleOBDA.g:562:1: qname returns [String value] : PREFIXED_NAME ;
	public final String qname() throws RecognitionException {
		String value = null;


		Token PREFIXED_NAME22=null;

		try {
			// TurtleOBDA.g:563:3: ( PREFIXED_NAME )
			// TurtleOBDA.g:563:5: PREFIXED_NAME
			{
			PREFIXED_NAME22=(Token)match(input,PREFIXED_NAME,FOLLOW_PREFIXED_NAME_in_qname617); 

			      String[] tokens = (PREFIXED_NAME22!=null?PREFIXED_NAME22.getText():null).split(":", 2);
			      String uri = directives.get(tokens[0]);  // the first token is the prefix
			      value = uri + tokens[1];  // the second token is the local name
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "qname"



	// $ANTLR start "blank"
	// TurtleOBDA.g:570:1: blank returns [Term value] : ( nodeID | BLANK );
	public final Term blank() throws RecognitionException {
		Term value = null;


		try {
			// TurtleOBDA.g:571:3: ( nodeID | BLANK )
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==BLANK_PREFIX) ) {
				alt14=1;
			}
			else if ( (LA14_0==BLANK) ) {
				alt14=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 14, 0, input);
				throw nvae;
			}

			switch (alt14) {
				case 1 :
					// TurtleOBDA.g:571:5: nodeID
					{
					pushFollow(FOLLOW_nodeID_in_blank636);
					nodeID();
					state._fsp--;


					        value = createBNode();
					    
					}
					break;
				case 2 :
					// TurtleOBDA.g:574:5: BLANK
					{
					match(input,BLANK,FOLLOW_BLANK_in_blank644); 

					        value = createBNode();
					    
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "blank"



	// $ANTLR start "variable"
	// TurtleOBDA.g:579:1: variable returns [Variable value] : STRING_WITH_CURLY_BRACKET ;
	public final Variable variable() throws RecognitionException {
		Variable value = null;


		Token STRING_WITH_CURLY_BRACKET23=null;

		try {
			// TurtleOBDA.g:580:3: ( STRING_WITH_CURLY_BRACKET )
			// TurtleOBDA.g:580:5: STRING_WITH_CURLY_BRACKET
			{
			STRING_WITH_CURLY_BRACKET23=(Token)match(input,STRING_WITH_CURLY_BRACKET,FOLLOW_STRING_WITH_CURLY_BRACKET_in_variable663); 

			      value = dfac.getVariable(removeBrackets((STRING_WITH_CURLY_BRACKET23!=null?STRING_WITH_CURLY_BRACKET23.getText():null)));
			      variableSet.add(value);
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "variable"



	// $ANTLR start "function"
	// TurtleOBDA.g:586:1: function returns [Function value] : resource LPAREN terms RPAREN ;
	public final Function function() throws RecognitionException {
		Function value = null;


		Term resource24 =null;
		Vector<Term> terms25 =null;

		try {
			// TurtleOBDA.g:587:3: ( resource LPAREN terms RPAREN )
			// TurtleOBDA.g:587:5: resource LPAREN terms RPAREN
			{
			pushFollow(FOLLOW_resource_in_function684);
			resource24=resource();
			state._fsp--;

			match(input,LPAREN,FOLLOW_LPAREN_in_function686); 
			pushFollow(FOLLOW_terms_in_function688);
			terms25=terms();
			state._fsp--;

			match(input,RPAREN,FOLLOW_RPAREN_in_function690); 

			      String functionName = resource24.toString();
			      int arity = terms25.size();
			      Predicate functionSymbol = dfac.getPredicate(functionName, arity);
			      value = dfac.getFunction(functionSymbol, terms25);
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "function"



	// $ANTLR start "typedLiteral"
	// TurtleOBDA.g:595:1: typedLiteral returns [Function value] : ( variable AT language | variable REFERENCE resource );
	public final Function typedLiteral() throws RecognitionException {
		Function value = null;


		Variable variable26 =null;
		Term language27 =null;
		Variable variable28 =null;
		Term resource29 =null;

		try {
			// TurtleOBDA.g:596:3: ( variable AT language | variable REFERENCE resource )
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==STRING_WITH_CURLY_BRACKET) ) {
				int LA15_1 = input.LA(2);
				if ( (LA15_1==AT) ) {
					alt15=1;
				}
				else if ( (LA15_1==REFERENCE) ) {
					alt15=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 15, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}

			switch (alt15) {
				case 1 :
					// TurtleOBDA.g:596:5: variable AT language
					{
					pushFollow(FOLLOW_variable_in_typedLiteral709);
					variable26=variable();
					state._fsp--;

					match(input,AT,FOLLOW_AT_in_typedLiteral711); 
					pushFollow(FOLLOW_language_in_typedLiteral713);
					language27=language();
					state._fsp--;


					      Variable var = variable26;
					      Term lang = language27;   
					      value = dfac.getTypedTerm(var, lang);

					    
					}
					break;
				case 2 :
					// TurtleOBDA.g:602:5: variable REFERENCE resource
					{
					pushFollow(FOLLOW_variable_in_typedLiteral721);
					variable28=variable();
					state._fsp--;

					match(input,REFERENCE,FOLLOW_REFERENCE_in_typedLiteral723); 
					pushFollow(FOLLOW_resource_in_typedLiteral725);
					resource29=resource();
					state._fsp--;


					      Variable var = variable28;
					      //String functionName = resource29.toString();
					      // resource29 must be a URIConstant
					    String functionName = null;
					    if (resource29 instanceof Function){
					       functionName = ((ValueConstant) ((Function)resource29).getTerm(0)).getValue();
					    } else {
					        throw new IllegalArgumentException("resource29 should be an URI");
					    }
					    Predicate.COL_TYPE type = dtfac.getDatatype(functionName);
					    if (type == null)  
					 	  throw new RuntimeException("ERROR. A mapping involves an unsupported datatype. \nOffending datatype:" + functionName);
					    
					      value = dfac.getTypedTerm(var, type);

						
					     
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "typedLiteral"



	// $ANTLR start "language"
	// TurtleOBDA.g:622:1: language returns [Term value] : ( languageTag | variable );
	public final Term language() throws RecognitionException {
		Term value = null;


		ParserRuleReturnScope languageTag30 =null;
		Variable variable31 =null;

		try {
			// TurtleOBDA.g:623:3: ( languageTag | variable )
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==VARNAME) ) {
				alt16=1;
			}
			else if ( (LA16_0==STRING_WITH_CURLY_BRACKET) ) {
				alt16=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 16, 0, input);
				throw nvae;
			}

			switch (alt16) {
				case 1 :
					// TurtleOBDA.g:623:5: languageTag
					{
					pushFollow(FOLLOW_languageTag_in_language744);
					languageTag30=languageTag();
					state._fsp--;


					    	value = dfac.getConstantLiteral((languageTag30!=null?input.toString(languageTag30.start,languageTag30.stop):null).toLowerCase(), COL_TYPE.STRING);
					    
					}
					break;
				case 2 :
					// TurtleOBDA.g:626:5: variable
					{
					pushFollow(FOLLOW_variable_in_language752);
					variable31=variable();
					state._fsp--;


					    	value = variable31;
					    
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "language"



	// $ANTLR start "terms"
	// TurtleOBDA.g:631:1: terms returns [Vector<Term> value] : t1= term ( COMMA t2= term )* ;
	public final Vector<Term> terms() throws RecognitionException {
		Vector<Term> value = null;


		Term t1 =null;
		Term t2 =null;


		  value = new Vector<Term>();

		try {
			// TurtleOBDA.g:635:3: (t1= term ( COMMA t2= term )* )
			// TurtleOBDA.g:635:5: t1= term ( COMMA t2= term )*
			{
			pushFollow(FOLLOW_term_in_terms778);
			t1=term();
			state._fsp--;

			 value.add(t1); 
			// TurtleOBDA.g:635:40: ( COMMA t2= term )*
			loop17:
			while (true) {
				int alt17=2;
				int LA17_0 = input.LA(1);
				if ( (LA17_0==COMMA) ) {
					alt17=1;
				}

				switch (alt17) {
				case 1 :
					// TurtleOBDA.g:635:41: COMMA t2= term
					{
					match(input,COMMA,FOLLOW_COMMA_in_terms783); 
					pushFollow(FOLLOW_term_in_terms787);
					t2=term();
					state._fsp--;

					 value.add(t2); 
					}
					break;

				default :
					break loop17;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "terms"



	// $ANTLR start "term"
	// TurtleOBDA.g:638:1: term returns [Term value] : ( function | variable | literal );
	public final Term term() throws RecognitionException {
		Term value = null;


		Function function32 =null;
		Variable variable33 =null;
		Term literal34 =null;

		try {
			// TurtleOBDA.g:639:3: ( function | variable | literal )
			int alt18=3;
			switch ( input.LA(1) ) {
			case PREFIXED_NAME:
			case STRING_WITH_BRACKET:
				{
				alt18=1;
				}
				break;
			case STRING_WITH_CURLY_BRACKET:
				{
				alt18=2;
				}
				break;
			case DECIMAL:
			case DECIMAL_NEGATIVE:
			case DECIMAL_POSITIVE:
			case DOUBLE:
			case DOUBLE_NEGATIVE:
			case DOUBLE_POSITIVE:
			case FALSE:
			case INTEGER:
			case INTEGER_NEGATIVE:
			case INTEGER_POSITIVE:
			case STRING_WITH_QUOTE_DOUBLE:
			case TRUE:
				{
				alt18=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 18, 0, input);
				throw nvae;
			}
			switch (alt18) {
				case 1 :
					// TurtleOBDA.g:639:5: function
					{
					pushFollow(FOLLOW_function_in_term808);
					function32=function();
					state._fsp--;

					 value = function32; 
					}
					break;
				case 2 :
					// TurtleOBDA.g:640:5: variable
					{
					pushFollow(FOLLOW_variable_in_term816);
					variable33=variable();
					state._fsp--;

					 value = variable33; 
					}
					break;
				case 3 :
					// TurtleOBDA.g:641:5: literal
					{
					pushFollow(FOLLOW_literal_in_term824);
					literal34=literal();
					state._fsp--;

					 value = literal34; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "term"



	// $ANTLR start "literal"
	// TurtleOBDA.g:650:1: literal returns [Term value] : ( stringLiteral ( AT language )? | dataTypeString | numericLiteral | booleanLiteral );
	public final Term literal() throws RecognitionException {
		Term value = null;


		Term language35 =null;
		Term stringLiteral36 =null;
		Term dataTypeString37 =null;
		Term numericLiteral38 =null;
		Term booleanLiteral39 =null;

		try {
			// TurtleOBDA.g:651:3: ( stringLiteral ( AT language )? | dataTypeString | numericLiteral | booleanLiteral )
			int alt20=4;
			switch ( input.LA(1) ) {
			case STRING_WITH_QUOTE_DOUBLE:
				{
				int LA20_1 = input.LA(2);
				if ( (LA20_1==AT||LA20_1==COMMA||LA20_1==PERIOD||(LA20_1 >= RPAREN && LA20_1 <= RSQ_BRACKET)||LA20_1==SEMI||LA20_1==WS) ) {
					alt20=1;
				}
				else if ( (LA20_1==REFERENCE) ) {
					alt20=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 20, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case DECIMAL:
			case DECIMAL_NEGATIVE:
			case DECIMAL_POSITIVE:
			case DOUBLE:
			case DOUBLE_NEGATIVE:
			case DOUBLE_POSITIVE:
			case INTEGER:
			case INTEGER_NEGATIVE:
			case INTEGER_POSITIVE:
				{
				alt20=3;
				}
				break;
			case FALSE:
			case TRUE:
				{
				alt20=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 20, 0, input);
				throw nvae;
			}
			switch (alt20) {
				case 1 :
					// TurtleOBDA.g:651:5: stringLiteral ( AT language )?
					{
					pushFollow(FOLLOW_stringLiteral_in_literal844);
					stringLiteral36=stringLiteral();
					state._fsp--;

					// TurtleOBDA.g:651:19: ( AT language )?
					int alt19=2;
					int LA19_0 = input.LA(1);
					if ( (LA19_0==AT) ) {
						alt19=1;
					}
					switch (alt19) {
						case 1 :
							// TurtleOBDA.g:651:20: AT language
							{
							match(input,AT,FOLLOW_AT_in_literal847); 
							pushFollow(FOLLOW_language_in_literal849);
							language35=language();
							state._fsp--;

							}
							break;

					}


					       Term lang = language35;
					       Term literal = stringLiteral36;
					       if (literal instanceof Function){
					          Function f = (Function)stringLiteral36;
					          if (lang != null){
					             value = dfac.getTypedTerm(f,lang);
					          }else{
					             value = dfac.getTypedTerm(f, COL_TYPE.LITERAL);
					          }       
					       }else{

					       //if variable we cannot assign a datatype yet
					       if (literal instanceof Variable)
					       {
					            value = dfac.getTypedTerm(literal, COL_TYPE.LITERAL);
					       }
					       else{
					          ValueConstant constant = (ValueConstant)stringLiteral36;
					          if (lang != null) {
						     value = dfac.getTypedTerm(constant, lang);
					          } else {
					      	     value = dfac.getTypedTerm(constant, COL_TYPE.LITERAL);
					          }
					       }
					       }
					    
					}
					break;
				case 2 :
					// TurtleOBDA.g:678:5: dataTypeString
					{
					pushFollow(FOLLOW_dataTypeString_in_literal859);
					dataTypeString37=dataTypeString();
					state._fsp--;

					 value = dataTypeString37; 
					}
					break;
				case 3 :
					// TurtleOBDA.g:679:5: numericLiteral
					{
					pushFollow(FOLLOW_numericLiteral_in_literal867);
					numericLiteral38=numericLiteral();
					state._fsp--;

					 value = numericLiteral38; 
					}
					break;
				case 4 :
					// TurtleOBDA.g:680:5: booleanLiteral
					{
					pushFollow(FOLLOW_booleanLiteral_in_literal875);
					booleanLiteral39=booleanLiteral();
					state._fsp--;

					 value = booleanLiteral39; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "literal"



	// $ANTLR start "stringLiteral"
	// TurtleOBDA.g:683:1: stringLiteral returns [Term value] : STRING_WITH_QUOTE_DOUBLE ;
	public final Term stringLiteral() throws RecognitionException {
		Term value = null;


		Token STRING_WITH_QUOTE_DOUBLE40=null;

		try {
			// TurtleOBDA.g:684:3: ( STRING_WITH_QUOTE_DOUBLE )
			// TurtleOBDA.g:684:5: STRING_WITH_QUOTE_DOUBLE
			{
			STRING_WITH_QUOTE_DOUBLE40=(Token)match(input,STRING_WITH_QUOTE_DOUBLE,FOLLOW_STRING_WITH_QUOTE_DOUBLE_in_stringLiteral894); 

			      String str = (STRING_WITH_QUOTE_DOUBLE40!=null?STRING_WITH_QUOTE_DOUBLE40.getText():null);
			      if (str.contains("{")){
			      	value = getNestedConcat(str);
			      }else{
			      	value = dfac.getConstantLiteral(str.substring(1, str.length()-1), COL_TYPE.LITERAL); // without the double quotes
			      }
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "stringLiteral"



	// $ANTLR start "dataTypeString"
	// TurtleOBDA.g:694:1: dataTypeString returns [Term value] : stringLiteral REFERENCE resource ;
	public final Term dataTypeString() throws RecognitionException {
		Term value = null;


		Term stringLiteral41 =null;
		Term resource42 =null;

		try {
			// TurtleOBDA.g:695:3: ( stringLiteral REFERENCE resource )
			// TurtleOBDA.g:695:6: stringLiteral REFERENCE resource
			{
			pushFollow(FOLLOW_stringLiteral_in_dataTypeString914);
			stringLiteral41=stringLiteral();
			state._fsp--;

			match(input,REFERENCE,FOLLOW_REFERENCE_in_dataTypeString916); 
			pushFollow(FOLLOW_resource_in_dataTypeString918);
			resource42=resource();
			state._fsp--;


			      Term stringValue = stringLiteral41;

			          if (resource42 instanceof Function){
			          	    String functionName = ( (ValueConstant) ((Function)resource42).getTerm(0) ).getValue();

			                    Predicate.COL_TYPE type = dtfac.getDatatype(functionName);
			                    if (type == null) {
			                      throw new RuntimeException("Unsupported datatype: " + functionName);
			                    }
			                    value = dfac.getTypedTerm(stringValue, type);
			                    }
			           else {
			          value = dfac.getTypedTerm(stringValue, COL_TYPE.LITERAL);
			          }

			  
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "dataTypeString"



	// $ANTLR start "numericLiteral"
	// TurtleOBDA.g:713:1: numericLiteral returns [Term value] : ( numericUnsigned | numericPositive | numericNegative );
	public final Term numericLiteral() throws RecognitionException {
		Term value = null;


		Term numericUnsigned43 =null;
		Term numericPositive44 =null;
		Term numericNegative45 =null;

		try {
			// TurtleOBDA.g:714:3: ( numericUnsigned | numericPositive | numericNegative )
			int alt21=3;
			switch ( input.LA(1) ) {
			case DECIMAL:
			case DOUBLE:
			case INTEGER:
				{
				alt21=1;
				}
				break;
			case DECIMAL_POSITIVE:
			case DOUBLE_POSITIVE:
			case INTEGER_POSITIVE:
				{
				alt21=2;
				}
				break;
			case DECIMAL_NEGATIVE:
			case DOUBLE_NEGATIVE:
			case INTEGER_NEGATIVE:
				{
				alt21=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 21, 0, input);
				throw nvae;
			}
			switch (alt21) {
				case 1 :
					// TurtleOBDA.g:714:5: numericUnsigned
					{
					pushFollow(FOLLOW_numericUnsigned_in_numericLiteral934);
					numericUnsigned43=numericUnsigned();
					state._fsp--;

					 value = numericUnsigned43; 
					}
					break;
				case 2 :
					// TurtleOBDA.g:715:5: numericPositive
					{
					pushFollow(FOLLOW_numericPositive_in_numericLiteral942);
					numericPositive44=numericPositive();
					state._fsp--;

					 value = numericPositive44; 
					}
					break;
				case 3 :
					// TurtleOBDA.g:716:5: numericNegative
					{
					pushFollow(FOLLOW_numericNegative_in_numericLiteral950);
					numericNegative45=numericNegative();
					state._fsp--;

					 value = numericNegative45; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "numericLiteral"



	// $ANTLR start "nodeID"
	// TurtleOBDA.g:719:1: nodeID : BLANK_PREFIX name ;
	public final void nodeID() throws RecognitionException {
		try {
			// TurtleOBDA.g:720:3: ( BLANK_PREFIX name )
			// TurtleOBDA.g:720:5: BLANK_PREFIX name
			{
			match(input,BLANK_PREFIX,FOLLOW_BLANK_PREFIX_in_nodeID965); 
			pushFollow(FOLLOW_name_in_nodeID967);
			name();
			state._fsp--;

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "nodeID"



	// $ANTLR start "relativeURI"
	// TurtleOBDA.g:723:1: relativeURI : STRING_URI ;
	public final void relativeURI() throws RecognitionException {
		try {
			// TurtleOBDA.g:724:3: ( STRING_URI )
			// TurtleOBDA.g:724:5: STRING_URI
			{
			match(input,STRING_URI,FOLLOW_STRING_URI_in_relativeURI981); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "relativeURI"


	public static class namespace_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "namespace"
	// TurtleOBDA.g:727:1: namespace : NAMESPACE ;
	public final TurtleOBDAParser.namespace_return namespace() throws RecognitionException {
		TurtleOBDAParser.namespace_return retval = new TurtleOBDAParser.namespace_return();
		retval.start = input.LT(1);

		try {
			// TurtleOBDA.g:728:3: ( NAMESPACE )
			// TurtleOBDA.g:728:5: NAMESPACE
			{
			match(input,NAMESPACE,FOLLOW_NAMESPACE_in_namespace994); 
			}

			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "namespace"


	public static class defaultNamespace_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "defaultNamespace"
	// TurtleOBDA.g:731:1: defaultNamespace : COLON ;
	public final TurtleOBDAParser.defaultNamespace_return defaultNamespace() throws RecognitionException {
		TurtleOBDAParser.defaultNamespace_return retval = new TurtleOBDAParser.defaultNamespace_return();
		retval.start = input.LT(1);

		try {
			// TurtleOBDA.g:732:3: ( COLON )
			// TurtleOBDA.g:732:5: COLON
			{
			match(input,COLON,FOLLOW_COLON_in_defaultNamespace1009); 
			}

			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "defaultNamespace"



	// $ANTLR start "name"
	// TurtleOBDA.g:735:1: name : VARNAME ;
	public final void name() throws RecognitionException {
		try {
			// TurtleOBDA.g:736:3: ( VARNAME )
			// TurtleOBDA.g:736:5: VARNAME
			{
			match(input,VARNAME,FOLLOW_VARNAME_in_name1022); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "name"


	public static class languageTag_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "languageTag"
	// TurtleOBDA.g:739:1: languageTag : VARNAME ;
	public final TurtleOBDAParser.languageTag_return languageTag() throws RecognitionException {
		TurtleOBDAParser.languageTag_return retval = new TurtleOBDAParser.languageTag_return();
		retval.start = input.LT(1);

		try {
			// TurtleOBDA.g:740:3: ( VARNAME )
			// TurtleOBDA.g:740:5: VARNAME
			{
			match(input,VARNAME,FOLLOW_VARNAME_in_languageTag1035); 
			}

			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "languageTag"



	// $ANTLR start "booleanLiteral"
	// TurtleOBDA.g:743:1: booleanLiteral returns [Term value] : ( TRUE | FALSE );
	public final Term booleanLiteral() throws RecognitionException {
		Term value = null;


		Token TRUE46=null;
		Token FALSE47=null;

		try {
			// TurtleOBDA.g:744:3: ( TRUE | FALSE )
			int alt22=2;
			int LA22_0 = input.LA(1);
			if ( (LA22_0==TRUE) ) {
				alt22=1;
			}
			else if ( (LA22_0==FALSE) ) {
				alt22=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 22, 0, input);
				throw nvae;
			}

			switch (alt22) {
				case 1 :
					// TurtleOBDA.g:744:5: TRUE
					{
					TRUE46=(Token)match(input,TRUE,FOLLOW_TRUE_in_booleanLiteral1052); 

					  ValueConstant trueConstant = dfac.getConstantLiteral((TRUE46!=null?TRUE46.getText():null), COL_TYPE.LITERAL);
					  value = dfac.getTypedTerm(trueConstant, COL_TYPE.BOOLEAN); 
					}
					break;
				case 2 :
					// TurtleOBDA.g:747:5: FALSE
					{
					FALSE47=(Token)match(input,FALSE,FOLLOW_FALSE_in_booleanLiteral1061); 

					  ValueConstant falseConstant = dfac.getConstantLiteral((FALSE47!=null?FALSE47.getText():null), COL_TYPE.LITERAL);
					  value = dfac.getTypedTerm(falseConstant, COL_TYPE.BOOLEAN);
					  
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "booleanLiteral"



	// $ANTLR start "numericUnsigned"
	// TurtleOBDA.g:753:1: numericUnsigned returns [Term value] : ( INTEGER | DOUBLE | DECIMAL );
	public final Term numericUnsigned() throws RecognitionException {
		Term value = null;


		Token INTEGER48=null;
		Token DOUBLE49=null;
		Token DECIMAL50=null;

		try {
			// TurtleOBDA.g:754:3: ( INTEGER | DOUBLE | DECIMAL )
			int alt23=3;
			switch ( input.LA(1) ) {
			case INTEGER:
				{
				alt23=1;
				}
				break;
			case DOUBLE:
				{
				alt23=2;
				}
				break;
			case DECIMAL:
				{
				alt23=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 23, 0, input);
				throw nvae;
			}
			switch (alt23) {
				case 1 :
					// TurtleOBDA.g:754:5: INTEGER
					{
					INTEGER48=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_numericUnsigned1080); 

					  ValueConstant integerConstant = dfac.getConstantLiteral((INTEGER48!=null?INTEGER48.getText():null), COL_TYPE.LITERAL);
					  value = dfac.getTypedTerm(integerConstant, COL_TYPE.INTEGER);
					  
					}
					break;
				case 2 :
					// TurtleOBDA.g:758:5: DOUBLE
					{
					DOUBLE49=(Token)match(input,DOUBLE,FOLLOW_DOUBLE_in_numericUnsigned1088); 

					  ValueConstant doubleConstant = dfac.getConstantLiteral((DOUBLE49!=null?DOUBLE49.getText():null), COL_TYPE.LITERAL);
					  value = dfac.getTypedTerm(doubleConstant, COL_TYPE.DOUBLE);
					  
					}
					break;
				case 3 :
					// TurtleOBDA.g:762:5: DECIMAL
					{
					DECIMAL50=(Token)match(input,DECIMAL,FOLLOW_DECIMAL_in_numericUnsigned1097); 

					  ValueConstant decimalConstant = dfac.getConstantLiteral((DECIMAL50!=null?DECIMAL50.getText():null), COL_TYPE.LITERAL);
					  value = dfac.getTypedTerm(decimalConstant, COL_TYPE.DECIMAL);
					   
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "numericUnsigned"



	// $ANTLR start "numericPositive"
	// TurtleOBDA.g:768:1: numericPositive returns [Term value] : ( INTEGER_POSITIVE | DOUBLE_POSITIVE | DECIMAL_POSITIVE );
	public final Term numericPositive() throws RecognitionException {
		Term value = null;


		Token INTEGER_POSITIVE51=null;
		Token DOUBLE_POSITIVE52=null;
		Token DECIMAL_POSITIVE53=null;

		try {
			// TurtleOBDA.g:769:3: ( INTEGER_POSITIVE | DOUBLE_POSITIVE | DECIMAL_POSITIVE )
			int alt24=3;
			switch ( input.LA(1) ) {
			case INTEGER_POSITIVE:
				{
				alt24=1;
				}
				break;
			case DOUBLE_POSITIVE:
				{
				alt24=2;
				}
				break;
			case DECIMAL_POSITIVE:
				{
				alt24=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 24, 0, input);
				throw nvae;
			}
			switch (alt24) {
				case 1 :
					// TurtleOBDA.g:769:5: INTEGER_POSITIVE
					{
					INTEGER_POSITIVE51=(Token)match(input,INTEGER_POSITIVE,FOLLOW_INTEGER_POSITIVE_in_numericPositive1116); 

					   ValueConstant integerConstant = dfac.getConstantLiteral((INTEGER_POSITIVE51!=null?INTEGER_POSITIVE51.getText():null), COL_TYPE.LITERAL);
					   value = dfac.getTypedTerm(integerConstant, COL_TYPE.INTEGER);
					  
					}
					break;
				case 2 :
					// TurtleOBDA.g:773:5: DOUBLE_POSITIVE
					{
					DOUBLE_POSITIVE52=(Token)match(input,DOUBLE_POSITIVE,FOLLOW_DOUBLE_POSITIVE_in_numericPositive1124); 

					  ValueConstant doubleConstant = dfac.getConstantLiteral((DOUBLE_POSITIVE52!=null?DOUBLE_POSITIVE52.getText():null), COL_TYPE.LITERAL);
					  value = dfac.getTypedTerm(doubleConstant, COL_TYPE.DOUBLE);
					  
					}
					break;
				case 3 :
					// TurtleOBDA.g:777:5: DECIMAL_POSITIVE
					{
					DECIMAL_POSITIVE53=(Token)match(input,DECIMAL_POSITIVE,FOLLOW_DECIMAL_POSITIVE_in_numericPositive1133); 

					  ValueConstant decimalConstant = dfac.getConstantLiteral((DECIMAL_POSITIVE53!=null?DECIMAL_POSITIVE53.getText():null), COL_TYPE.LITERAL);
					  value = dfac.getTypedTerm(decimalConstant, COL_TYPE.DECIMAL);
					   
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "numericPositive"



	// $ANTLR start "numericNegative"
	// TurtleOBDA.g:783:1: numericNegative returns [Term value] : ( INTEGER_NEGATIVE | DOUBLE_NEGATIVE | DECIMAL_NEGATIVE );
	public final Term numericNegative() throws RecognitionException {
		Term value = null;


		Token INTEGER_NEGATIVE54=null;
		Token DOUBLE_NEGATIVE55=null;
		Token DECIMAL_NEGATIVE56=null;

		try {
			// TurtleOBDA.g:784:3: ( INTEGER_NEGATIVE | DOUBLE_NEGATIVE | DECIMAL_NEGATIVE )
			int alt25=3;
			switch ( input.LA(1) ) {
			case INTEGER_NEGATIVE:
				{
				alt25=1;
				}
				break;
			case DOUBLE_NEGATIVE:
				{
				alt25=2;
				}
				break;
			case DECIMAL_NEGATIVE:
				{
				alt25=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 25, 0, input);
				throw nvae;
			}
			switch (alt25) {
				case 1 :
					// TurtleOBDA.g:784:5: INTEGER_NEGATIVE
					{
					INTEGER_NEGATIVE54=(Token)match(input,INTEGER_NEGATIVE,FOLLOW_INTEGER_NEGATIVE_in_numericNegative1152); 

					  ValueConstant integerConstant = dfac.getConstantLiteral((INTEGER_NEGATIVE54!=null?INTEGER_NEGATIVE54.getText():null), COL_TYPE.LITERAL);
					  value = dfac.getTypedTerm(integerConstant, COL_TYPE.INTEGER);
					  
					}
					break;
				case 2 :
					// TurtleOBDA.g:788:5: DOUBLE_NEGATIVE
					{
					DOUBLE_NEGATIVE55=(Token)match(input,DOUBLE_NEGATIVE,FOLLOW_DOUBLE_NEGATIVE_in_numericNegative1160); 

					   ValueConstant doubleConstant = dfac.getConstantLiteral((DOUBLE_NEGATIVE55!=null?DOUBLE_NEGATIVE55.getText():null), COL_TYPE.LITERAL);
					   value = dfac.getTypedTerm(doubleConstant, COL_TYPE.DOUBLE);
					  
					}
					break;
				case 3 :
					// TurtleOBDA.g:792:5: DECIMAL_NEGATIVE
					{
					DECIMAL_NEGATIVE56=(Token)match(input,DECIMAL_NEGATIVE,FOLLOW_DECIMAL_NEGATIVE_in_numericNegative1169); 

					  ValueConstant decimalConstant = dfac.getConstantLiteral((DECIMAL_NEGATIVE56!=null?DECIMAL_NEGATIVE56.getText():null), COL_TYPE.LITERAL);
					  value = dfac.getTypedTerm(decimalConstant, COL_TYPE.DECIMAL);
					  
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "numericNegative"

	// Delegated rules



	public static final BitSet FOLLOW_directiveStatement_in_parse54 = new BitSet(new long[]{0x0040040000003200L,0x0000000000000018L});
	public static final BitSet FOLLOW_triplesStatement_in_parse63 = new BitSet(new long[]{0x0040040000003000L,0x0000000000000018L});
	public static final BitSet FOLLOW_triplesStatement_in_parse76 = new BitSet(new long[]{0x0040040000003000L,0x0000000000000018L});
	public static final BitSet FOLLOW_EOF_in_parse83 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_directive_in_directiveStatement96 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_PERIOD_in_directiveStatement98 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_triples_in_triplesStatement128 = new BitSet(new long[]{0x0008000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_WS_in_triplesStatement130 = new BitSet(new long[]{0x0008000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_PERIOD_in_triplesStatement133 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_base_in_directive148 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_prefixID_in_directive154 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AT_in_base167 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_BASE_in_base169 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_uriref_in_base171 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AT_in_prefixID189 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_PREFIX_in_prefixID191 = new BitSet(new long[]{0x0000200000010000L});
	public static final BitSet FOLLOW_namespace_in_prefixID194 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_defaultNamespace_in_prefixID200 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_uriref_in_prefixID205 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subject_in_triples224 = new BitSet(new long[]{0x0040000000000000L,0x0000000000002008L});
	public static final BitSet FOLLOW_predicateObjectList_in_triples226 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LSQ_BRACKET_in_triples236 = new BitSet(new long[]{0x0040000000000000L,0x0000000000002008L});
	public static final BitSet FOLLOW_predicateObjectList_in_triples246 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_RSQ_BRACKET_in_triples254 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_verb_in_predicateObjectList279 = new BitSet(new long[]{0x00400470439C3000L,0x0000000000000158L});
	public static final BitSet FOLLOW_objectList_in_predicateObjectList285 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000001L});
	public static final BitSet FOLLOW_SEMI_in_predicateObjectList294 = new BitSet(new long[]{0x0040000000000000L,0x0000000000002008L});
	public static final BitSet FOLLOW_verb_in_predicateObjectList298 = new BitSet(new long[]{0x00400470439C3000L,0x0000000000000158L});
	public static final BitSet FOLLOW_objectList_in_predicateObjectList302 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000001L});
	public static final BitSet FOLLOW_predicate_in_verb326 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_77_in_verb334 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_object_in_objectList360 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_objectList365 = new BitSet(new long[]{0x00400470439C3000L,0x0000000000000158L});
	public static final BitSet FOLLOW_object_in_objectList369 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_resource_in_subject392 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_variable_in_subject400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_blank_in_subject408 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_resource_in_predicate428 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simpleObject_in_object447 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_bnodeObject_in_object455 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_resource_in_simpleObject474 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_simpleObject482 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_typedLiteral_in_simpleObject491 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_variable_in_simpleObject499 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_blank_in_simpleObject507 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LSQ_BRACKET_in_bnodeObject526 = new BitSet(new long[]{0x0040000000000000L,0x0000000000002008L});
	public static final BitSet FOLLOW_predicateObjectList_in_bnodeObject536 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_RSQ_BRACKET_in_bnodeObject546 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_uriref_in_resource564 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_qname_in_resource573 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_WITH_BRACKET_in_uriref598 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PREFIXED_NAME_in_qname617 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nodeID_in_blank636 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_BLANK_in_blank644 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_WITH_CURLY_BRACKET_in_variable663 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_resource_in_function684 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_LPAREN_in_function686 = new BitSet(new long[]{0x00400070439C0000L,0x0000000000000158L});
	public static final BitSet FOLLOW_terms_in_function688 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_RPAREN_in_function690 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_variable_in_typedLiteral709 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_AT_in_typedLiteral711 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000810L});
	public static final BitSet FOLLOW_language_in_typedLiteral713 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_variable_in_typedLiteral721 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_REFERENCE_in_typedLiteral723 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_resource_in_typedLiteral725 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_languageTag_in_language744 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_variable_in_language752 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_term_in_terms778 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_terms783 = new BitSet(new long[]{0x00400070439C0000L,0x0000000000000158L});
	public static final BitSet FOLLOW_term_in_terms787 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_function_in_term808 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_variable_in_term816 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_term824 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stringLiteral_in_literal844 = new BitSet(new long[]{0x0000000000000202L});
	public static final BitSet FOLLOW_AT_in_literal847 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000810L});
	public static final BitSet FOLLOW_language_in_literal849 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dataTypeString_in_literal859 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numericLiteral_in_literal867 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_booleanLiteral_in_literal875 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_WITH_QUOTE_DOUBLE_in_stringLiteral894 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stringLiteral_in_dataTypeString914 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_REFERENCE_in_dataTypeString916 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_resource_in_dataTypeString918 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numericUnsigned_in_numericLiteral934 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numericPositive_in_numericLiteral942 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numericNegative_in_numericLiteral950 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_BLANK_PREFIX_in_nodeID965 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_name_in_nodeID967 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_URI_in_relativeURI981 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAMESPACE_in_namespace994 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COLON_in_defaultNamespace1009 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VARNAME_in_name1022 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VARNAME_in_languageTag1035 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TRUE_in_booleanLiteral1052 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FALSE_in_booleanLiteral1061 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INTEGER_in_numericUnsigned1080 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOUBLE_in_numericUnsigned1088 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DECIMAL_in_numericUnsigned1097 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INTEGER_POSITIVE_in_numericPositive1116 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOUBLE_POSITIVE_in_numericPositive1124 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DECIMAL_POSITIVE_in_numericPositive1133 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INTEGER_NEGATIVE_in_numericNegative1152 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOUBLE_NEGATIVE_in_numericNegative1160 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DECIMAL_NEGATIVE_in_numericNegative1169 = new BitSet(new long[]{0x0000000000000002L});
}
