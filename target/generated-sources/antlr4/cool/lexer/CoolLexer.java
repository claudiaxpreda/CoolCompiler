// Generated from CoolLexer.g4 by ANTLR 4.4

    package cool.lexer;	

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CoolLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ERROR=1, BLOCK_COMMENT=2, LINE_COMMENT=3, CLASS=4, ELSE=5, FALSE=6, FI=7, 
		IF=8, IN=9, INHERITS=10, ISVOID=11, LET=12, LOOP=13, POOL=14, THEN=15, 
		WHILE=16, CASE=17, ESAC=18, NEW=19, OF=20, NOT=21, TRUE=22, TILDE=23, 
		MULT=24, DIV=25, PLUS=26, MINUS=27, ASSIGN=28, LT=29, LTEQ=30, EQ=31, 
		CSARR=32, AT=33, DOT=34, SEMI=35, COMMA=36, COLON=37, LPAREN=38, RPAREN=39, 
		LBRACK=40, RBRACK=41, STRING=42, TYPE=43, ID=44, INT=45, WS=46, UNMATCHED_COMMENT=47, 
		OTHERS=48;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'", "'\\u0018'", 
		"'\\u0019'", "'\\u001A'", "'\\u001B'", "'\\u001C'", "'\\u001D'", "'\\u001E'", 
		"'\\u001F'", "' '", "'!'", "'\"'", "'#'", "'$'", "'%'", "'&'", "'''", 
		"'('", "')'", "'*'", "'+'", "','", "'-'", "'.'", "'/'", "'0'"
	};
	public static final String[] ruleNames = {
		"A", "B", "C", "D", "E", "F", "H", "G", "I", "K", "L", "M", "N", "O", 
		"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "LETTER", "SLETTER", 
		"CLETTER", "DIGIT", "NEW_LINE", "BLOCK_COMMENT", "LINE_COMMENT", "CLASS", 
		"ELSE", "FALSE", "FI", "IF", "IN", "INHERITS", "ISVOID", "LET", "LOOP", 
		"POOL", "THEN", "WHILE", "CASE", "ESAC", "NEW", "OF", "NOT", "TRUE", "TILDE", 
		"MULT", "DIV", "PLUS", "MINUS", "ASSIGN", "LT", "LTEQ", "EQ", "CSARR", 
		"AT", "DOT", "SEMI", "COMMA", "COLON", "LPAREN", "RPAREN", "LBRACK", "RBRACK", 
		"ESC", "STRING", "TYPE", "ID", "INT", "WS", "UNMATCHED_COMMENT", "OTHERS"
	};

	    
	    private void raiseError(String msg) {
	        setText(msg);
	        setType(ERROR);
	    }


	public CoolLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CoolLexer.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 30: BLOCK_COMMENT_action((RuleContext)_localctx, actionIndex); break;
		case 71: STRING_action((RuleContext)_localctx, actionIndex); break;
		case 76: UNMATCHED_COMMENT_action((RuleContext)_localctx, actionIndex); break;
		case 77: OTHERS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void BLOCK_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: raiseError("EOF in comment"); break;
		}
	}
	private void OTHERS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3: String s = getText();  raiseError("Invalid character: ".concat(s)); break;
		}
	}
	private void STRING_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1: 
				String s = getText();	
					
				if(s.contains("\u0000")) {
					raiseError("String contains null character");
					return;
				}
				
				if(s.charAt(s.length()-1) == '\r') {
					raiseError("Unterminated string constant");
					return;
				}
				
				if(s.charAt(s.length()-1) == '\n') {
					raiseError("Unterminated string constant");
					return;
				}
				
				if(s.charAt(s.length()-1) != '"') {
					raiseError("EOF in string constant");
					return;
				}
				
				//Replace escaped characters;
				String[] replaced = {"\\n","\\\r\n", "\\t", "\\b", "\\f"};
				String[] replacing = {"\n","\r\n" , "\t", "\b", "\f"};
				
				s = s.substring(1, s.length() -1);
				for (var i = 0; i < replaced.length; i++) {
					s = s.replace(replaced[i], replacing[i]);
				}
				
				for (int i = 32; i < 128; i++){
					String escp = "bfnrt\\";
					char c = (char)i;
					
					if (! escp.contains(c + "")){
						s = s.replace("\\" + c, "" + c);
					}
				}
				if(s.length() > 1024) {
					raiseError("String constant too long");
					return;
				}
				setText(s);
			 break;
		}
	}
	private void UNMATCHED_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2: raiseError("Unmatched *)"); break;
		}
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\62\u01ca\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4"+
		"I\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3"+
		"\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3"+
		"\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3"+
		"\34\3\34\3\35\3\35\3\36\3\36\3\37\5\37\u00db\n\37\3\37\3\37\3 \3 \3 \3"+
		" \3 \7 \u00e4\n \f \16 \u00e7\13 \3 \3 \3 \3 \5 \u00ed\n \3 \3 \3!\3!"+
		"\3!\3!\7!\u00f5\n!\f!\16!\u00f8\13!\3!\3!\5!\u00fc\n!\3!\3!\3\"\3\"\3"+
		"\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3%\3%\3%\3&\3&\3&\3\'"+
		"\3\'\3\'\3(\3(\3(\3(\3(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3"+
		"+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3/\3/\3"+
		"/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3"+
		"\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3"+
		"\67\38\38\39\39\3:\3:\3:\3;\3;\3<\3<\3<\3=\3=\3>\3>\3>\3?\3?\3@\3@\3A"+
		"\3A\3B\3B\3C\3C\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H\3H\3H\3H\5H\u018b\nH\3I"+
		"\3I\3I\7I\u0190\nI\fI\16I\u0193\13I\3I\5I\u0196\nI\3I\3I\3J\3J\3J\3J\7"+
		"J\u019e\nJ\fJ\16J\u01a1\13J\3K\3K\3K\3K\7K\u01a7\nK\fK\16K\u01aa\13K\3"+
		"L\6L\u01ad\nL\rL\16L\u01ae\3M\6M\u01b2\nM\rM\16M\u01b3\3M\3M\3N\6N\u01b9"+
		"\nN\rN\16N\u01ba\3N\3N\3N\5N\u01c0\nN\3N\3N\3O\6O\u01c5\nO\rO\16O\u01c6"+
		"\3O\3O\6\u00e5\u00f6\u0191\u01c6\2P\3\2\5\2\7\2\t\2\13\2\r\2\17\2\21\2"+
		"\23\2\25\2\27\2\31\2\33\2\35\2\37\2!\2#\2%\2\'\2)\2+\2-\2/\2\61\2\63\2"+
		"\65\2\67\29\2;\2=\2?\4A\5C\6E\7G\bI\tK\nM\13O\fQ\rS\16U\17W\20Y\21[\22"+
		"]\23_\24a\25c\26e\27g\30i\31k\32m\33o\34q\35s\36u\37w y!{\"}#\177$\u0081"+
		"%\u0083&\u0085\'\u0087(\u0089)\u008b*\u008d+\u008f\2\u0091,\u0093-\u0095"+
		".\u0097/\u0099\60\u009b\61\u009d\62\3\2\"\4\2CCcc\4\2DDdd\4\2EEee\4\2"+
		"FFff\4\2GGgg\4\2HHhh\4\2JJjj\4\2IIii\4\2KKkk\4\2MMmm\4\2NNnn\4\2OOoo\4"+
		"\2PPpp\4\2QQqq\4\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4\2VVvv\4\2WWww\4\2XXx"+
		"x\4\2YYyy\4\2ZZzz\4\2[[{{\4\2\\\\||\4\2C\\c|\3\2c|\3\2C\\\3\2\62;\3\2"+
		"\17\17\5\3\f\f\17\17$$\5\2\13\f\16\17\"\"\u01bf\2?\3\2\2\2\2A\3\2\2\2"+
		"\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O"+
		"\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2"+
		"\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2"+
		"\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u"+
		"\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081"+
		"\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2"+
		"\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095"+
		"\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2"+
		"\2\3\u009f\3\2\2\2\5\u00a1\3\2\2\2\7\u00a3\3\2\2\2\t\u00a5\3\2\2\2\13"+
		"\u00a7\3\2\2\2\r\u00a9\3\2\2\2\17\u00ab\3\2\2\2\21\u00ad\3\2\2\2\23\u00af"+
		"\3\2\2\2\25\u00b1\3\2\2\2\27\u00b3\3\2\2\2\31\u00b5\3\2\2\2\33\u00b7\3"+
		"\2\2\2\35\u00b9\3\2\2\2\37\u00bb\3\2\2\2!\u00bd\3\2\2\2#\u00bf\3\2\2\2"+
		"%\u00c1\3\2\2\2\'\u00c3\3\2\2\2)\u00c5\3\2\2\2+\u00c7\3\2\2\2-\u00c9\3"+
		"\2\2\2/\u00cb\3\2\2\2\61\u00cd\3\2\2\2\63\u00cf\3\2\2\2\65\u00d1\3\2\2"+
		"\2\67\u00d3\3\2\2\29\u00d5\3\2\2\2;\u00d7\3\2\2\2=\u00da\3\2\2\2?\u00de"+
		"\3\2\2\2A\u00f0\3\2\2\2C\u00ff\3\2\2\2E\u0105\3\2\2\2G\u010a\3\2\2\2I"+
		"\u0110\3\2\2\2K\u0113\3\2\2\2M\u0116\3\2\2\2O\u0119\3\2\2\2Q\u0122\3\2"+
		"\2\2S\u0129\3\2\2\2U\u012d\3\2\2\2W\u0132\3\2\2\2Y\u0137\3\2\2\2[\u013c"+
		"\3\2\2\2]\u0142\3\2\2\2_\u0147\3\2\2\2a\u014c\3\2\2\2c\u0150\3\2\2\2e"+
		"\u0153\3\2\2\2g\u0157\3\2\2\2i\u015c\3\2\2\2k\u015e\3\2\2\2m\u0160\3\2"+
		"\2\2o\u0162\3\2\2\2q\u0164\3\2\2\2s\u0166\3\2\2\2u\u0169\3\2\2\2w\u016b"+
		"\3\2\2\2y\u016e\3\2\2\2{\u0170\3\2\2\2}\u0173\3\2\2\2\177\u0175\3\2\2"+
		"\2\u0081\u0177\3\2\2\2\u0083\u0179\3\2\2\2\u0085\u017b\3\2\2\2\u0087\u017d"+
		"\3\2\2\2\u0089\u017f\3\2\2\2\u008b\u0181\3\2\2\2\u008d\u0183\3\2\2\2\u008f"+
		"\u0185\3\2\2\2\u0091\u018c\3\2\2\2\u0093\u0199\3\2\2\2\u0095\u01a2\3\2"+
		"\2\2\u0097\u01ac\3\2\2\2\u0099\u01b1\3\2\2\2\u009b\u01b8\3\2\2\2\u009d"+
		"\u01c4\3\2\2\2\u009f\u00a0\t\2\2\2\u00a0\4\3\2\2\2\u00a1\u00a2\t\3\2\2"+
		"\u00a2\6\3\2\2\2\u00a3\u00a4\t\4\2\2\u00a4\b\3\2\2\2\u00a5\u00a6\t\5\2"+
		"\2\u00a6\n\3\2\2\2\u00a7\u00a8\t\6\2\2\u00a8\f\3\2\2\2\u00a9\u00aa\t\7"+
		"\2\2\u00aa\16\3\2\2\2\u00ab\u00ac\t\b\2\2\u00ac\20\3\2\2\2\u00ad\u00ae"+
		"\t\t\2\2\u00ae\22\3\2\2\2\u00af\u00b0\t\n\2\2\u00b0\24\3\2\2\2\u00b1\u00b2"+
		"\t\13\2\2\u00b2\26\3\2\2\2\u00b3\u00b4\t\f\2\2\u00b4\30\3\2\2\2\u00b5"+
		"\u00b6\t\r\2\2\u00b6\32\3\2\2\2\u00b7\u00b8\t\16\2\2\u00b8\34\3\2\2\2"+
		"\u00b9\u00ba\t\17\2\2\u00ba\36\3\2\2\2\u00bb\u00bc\t\20\2\2\u00bc \3\2"+
		"\2\2\u00bd\u00be\t\21\2\2\u00be\"\3\2\2\2\u00bf\u00c0\t\22\2\2\u00c0$"+
		"\3\2\2\2\u00c1\u00c2\t\23\2\2\u00c2&\3\2\2\2\u00c3\u00c4\t\24\2\2\u00c4"+
		"(\3\2\2\2\u00c5\u00c6\t\25\2\2\u00c6*\3\2\2\2\u00c7\u00c8\t\26\2\2\u00c8"+
		",\3\2\2\2\u00c9\u00ca\t\27\2\2\u00ca.\3\2\2\2\u00cb\u00cc\t\30\2\2\u00cc"+
		"\60\3\2\2\2\u00cd\u00ce\t\31\2\2\u00ce\62\3\2\2\2\u00cf\u00d0\t\32\2\2"+
		"\u00d0\64\3\2\2\2\u00d1\u00d2\t\33\2\2\u00d2\66\3\2\2\2\u00d3\u00d4\t"+
		"\34\2\2\u00d48\3\2\2\2\u00d5\u00d6\t\35\2\2\u00d6:\3\2\2\2\u00d7\u00d8"+
		"\t\36\2\2\u00d8<\3\2\2\2\u00d9\u00db\7\17\2\2\u00da\u00d9\3\2\2\2\u00da"+
		"\u00db\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00dd\7\f\2\2\u00dd>\3\2\2\2"+
		"\u00de\u00df\7*\2\2\u00df\u00e0\7,\2\2\u00e0\u00e5\3\2\2\2\u00e1\u00e4"+
		"\5? \2\u00e2\u00e4\13\2\2\2\u00e3\u00e1\3\2\2\2\u00e3\u00e2\3\2\2\2\u00e4"+
		"\u00e7\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e6\u00ec\3\2"+
		"\2\2\u00e7\u00e5\3\2\2\2\u00e8\u00e9\7,\2\2\u00e9\u00ed\7+\2\2\u00ea\u00eb"+
		"\7\2\2\3\u00eb\u00ed\b \2\2\u00ec\u00e8\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ed"+
		"\u00ee\3\2\2\2\u00ee\u00ef\b \3\2\u00ef@\3\2\2\2\u00f0\u00f1\7/\2\2\u00f1"+
		"\u00f2\7/\2\2\u00f2\u00f6\3\2\2\2\u00f3\u00f5\13\2\2\2\u00f4\u00f3\3\2"+
		"\2\2\u00f5\u00f8\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f6\u00f4\3\2\2\2\u00f7"+
		"\u00fb\3\2\2\2\u00f8\u00f6\3\2\2\2\u00f9\u00fc\5=\37\2\u00fa\u00fc\7\2"+
		"\2\3\u00fb\u00f9\3\2\2\2\u00fb\u00fa\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd"+
		"\u00fe\b!\3\2\u00feB\3\2\2\2\u00ff\u0100\5\7\4\2\u0100\u0101\5\27\f\2"+
		"\u0101\u0102\5\3\2\2\u0102\u0103\5%\23\2\u0103\u0104\5%\23\2\u0104D\3"+
		"\2\2\2\u0105\u0106\5\13\6\2\u0106\u0107\5\27\f\2\u0107\u0108\5%\23\2\u0108"+
		"\u0109\5\13\6\2\u0109F\3\2\2\2\u010a\u010b\7h\2\2\u010b\u010c\5\3\2\2"+
		"\u010c\u010d\5\27\f\2\u010d\u010e\5%\23\2\u010e\u010f\5\13\6\2\u010fH"+
		"\3\2\2\2\u0110\u0111\5\r\7\2\u0111\u0112\5\23\n\2\u0112J\3\2\2\2\u0113"+
		"\u0114\5\23\n\2\u0114\u0115\5\r\7\2\u0115L\3\2\2\2\u0116\u0117\5\23\n"+
		"\2\u0117\u0118\5\33\16\2\u0118N\3\2\2\2\u0119\u011a\5\23\n\2\u011a\u011b"+
		"\5\33\16\2\u011b\u011c\5\17\b\2\u011c\u011d\5\13\6\2\u011d\u011e\5#\22"+
		"\2\u011e\u011f\5\23\n\2\u011f\u0120\5\'\24\2\u0120\u0121\5%\23\2\u0121"+
		"P\3\2\2\2\u0122\u0123\5\23\n\2\u0123\u0124\5%\23\2\u0124\u0125\5+\26\2"+
		"\u0125\u0126\5\35\17\2\u0126\u0127\5\23\n\2\u0127\u0128\5\t\5\2\u0128"+
		"R\3\2\2\2\u0129\u012a\5\27\f\2\u012a\u012b\5\13\6\2\u012b\u012c\5\'\24"+
		"\2\u012cT\3\2\2\2\u012d\u012e\5\27\f\2\u012e\u012f\5\35\17\2\u012f\u0130"+
		"\5\35\17\2\u0130\u0131\5\37\20\2\u0131V\3\2\2\2\u0132\u0133\5\37\20\2"+
		"\u0133\u0134\5\35\17\2\u0134\u0135\5\35\17\2\u0135\u0136\5\27\f\2\u0136"+
		"X\3\2\2\2\u0137\u0138\5\'\24\2\u0138\u0139\5\17\b\2\u0139\u013a\5\13\6"+
		"\2\u013a\u013b\5\33\16\2\u013bZ\3\2\2\2\u013c\u013d\5-\27\2\u013d\u013e"+
		"\5\17\b\2\u013e\u013f\5\23\n\2\u013f\u0140\5\27\f\2\u0140\u0141\5\13\6"+
		"\2\u0141\\\3\2\2\2\u0142\u0143\5\7\4\2\u0143\u0144\5\3\2\2\u0144\u0145"+
		"\5%\23\2\u0145\u0146\5\13\6\2\u0146^\3\2\2\2\u0147\u0148\5\13\6\2\u0148"+
		"\u0149\5%\23\2\u0149\u014a\5\3\2\2\u014a\u014b\5\7\4\2\u014b`\3\2\2\2"+
		"\u014c\u014d\5\33\16\2\u014d\u014e\5\13\6\2\u014e\u014f\5-\27\2\u014f"+
		"b\3\2\2\2\u0150\u0151\5\35\17\2\u0151\u0152\5\r\7\2\u0152d\3\2\2\2\u0153"+
		"\u0154\5\33\16\2\u0154\u0155\5\35\17\2\u0155\u0156\5\'\24\2\u0156f\3\2"+
		"\2\2\u0157\u0158\7v\2\2\u0158\u0159\5#\22\2\u0159\u015a\5)\25\2\u015a"+
		"\u015b\5\13\6\2\u015bh\3\2\2\2\u015c\u015d\7\u0080\2\2\u015dj\3\2\2\2"+
		"\u015e\u015f\7,\2\2\u015fl\3\2\2\2\u0160\u0161\7\61\2\2\u0161n\3\2\2\2"+
		"\u0162\u0163\7-\2\2\u0163p\3\2\2\2\u0164\u0165\7/\2\2\u0165r\3\2\2\2\u0166"+
		"\u0167\7>\2\2\u0167\u0168\7/\2\2\u0168t\3\2\2\2\u0169\u016a\7>\2\2\u016a"+
		"v\3\2\2\2\u016b\u016c\7>\2\2\u016c\u016d\7?\2\2\u016dx\3\2\2\2\u016e\u016f"+
		"\7?\2\2\u016fz\3\2\2\2\u0170\u0171\7?\2\2\u0171\u0172\7@\2\2\u0172|\3"+
		"\2\2\2\u0173\u0174\7B\2\2\u0174~\3\2\2\2\u0175\u0176\7\60\2\2\u0176\u0080"+
		"\3\2\2\2\u0177\u0178\7=\2\2\u0178\u0082\3\2\2\2\u0179\u017a\7.\2\2\u017a"+
		"\u0084\3\2\2\2\u017b\u017c\7<\2\2\u017c\u0086\3\2\2\2\u017d\u017e\7*\2"+
		"\2\u017e\u0088\3\2\2\2\u017f\u0180\7+\2\2\u0180\u008a\3\2\2\2\u0181\u0182"+
		"\7}\2\2\u0182\u008c\3\2\2\2\u0183\u0184\7\177\2\2\u0184\u008e\3\2\2\2"+
		"\u0185\u018a\7^\2\2\u0186\u018b\n\37\2\2\u0187\u0188\7\17\2\2\u0188\u018b"+
		"\7\f\2\2\u0189\u018b\7\f\2\2\u018a\u0186\3\2\2\2\u018a\u0187\3\2\2\2\u018a"+
		"\u0189\3\2\2\2\u018b\u0090\3\2\2\2\u018c\u0191\7$\2\2\u018d\u0190\5\u008f"+
		"H\2\u018e\u0190\13\2\2\2\u018f\u018d\3\2\2\2\u018f\u018e\3\2\2\2\u0190"+
		"\u0193\3\2\2\2\u0191\u0192\3\2\2\2\u0191\u018f\3\2\2\2\u0192\u0195\3\2"+
		"\2\2\u0193\u0191\3\2\2\2\u0194\u0196\t \2\2\u0195\u0194\3\2\2\2\u0196"+
		"\u0197\3\2\2\2\u0197\u0198\bI\4\2\u0198\u0092\3\2\2\2\u0199\u019f\59\35"+
		"\2\u019a\u019e\5\65\33\2\u019b\u019e\5;\36\2\u019c\u019e\7a\2\2\u019d"+
		"\u019a\3\2\2\2\u019d\u019b\3\2\2\2\u019d\u019c\3\2\2\2\u019e\u01a1\3\2"+
		"\2\2\u019f\u019d\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0\u0094\3\2\2\2\u01a1"+
		"\u019f\3\2\2\2\u01a2\u01a8\5\67\34\2\u01a3\u01a7\5\65\33\2\u01a4\u01a7"+
		"\5;\36\2\u01a5\u01a7\7a\2\2\u01a6\u01a3\3\2\2\2\u01a6\u01a4\3\2\2\2\u01a6"+
		"\u01a5\3\2\2\2\u01a7\u01aa\3\2\2\2\u01a8\u01a6\3\2\2\2\u01a8\u01a9\3\2"+
		"\2\2\u01a9\u0096\3\2\2\2\u01aa\u01a8\3\2\2\2\u01ab\u01ad\5;\36\2\u01ac"+
		"\u01ab\3\2\2\2\u01ad\u01ae\3\2\2\2\u01ae\u01ac\3\2\2\2\u01ae\u01af\3\2"+
		"\2\2\u01af\u0098\3\2\2\2\u01b0\u01b2\t!\2\2\u01b1\u01b0\3\2\2\2\u01b2"+
		"\u01b3\3\2\2\2\u01b3\u01b1\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4\u01b5\3\2"+
		"\2\2\u01b5\u01b6\bM\3\2\u01b6\u009a\3\2\2\2\u01b7\u01b9\7,\2\2\u01b8\u01b7"+
		"\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01b8\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb"+
		"\u01bc\3\2\2\2\u01bc\u01bf\7+\2\2\u01bd\u01c0\5=\37\2\u01be\u01c0\7\2"+
		"\2\3\u01bf\u01bd\3\2\2\2\u01bf\u01be\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1"+
		"\u01c2\bN\5\2\u01c2\u009c\3\2\2\2\u01c3\u01c5\13\2\2\2\u01c4\u01c3\3\2"+
		"\2\2\u01c5\u01c6\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c6\u01c4\3\2\2\2\u01c7"+
		"\u01c8\3\2\2\2\u01c8\u01c9\bO\6\2\u01c9\u009e\3\2\2\2\26\2\u00da\u00e3"+
		"\u00e5\u00ec\u00f6\u00fb\u018a\u018f\u0191\u0195\u019d\u019f\u01a6\u01a8"+
		"\u01ae\u01b3\u01ba\u01bf\u01c6\7\3 \2\b\2\2\3I\3\3N\4\3O\5";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}