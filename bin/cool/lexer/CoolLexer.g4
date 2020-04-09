lexer grammar CoolLexer;

tokens { ERROR } 

@header{
    package cool.lexer;	
}

@members{    
    private void raiseError(String msg) {
        setText(msg);
        setType(ERROR);
    }
}

//Letters for case insensitive keyword in COOL;
fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment H : [hH];
fragment G : [gG];
fragment I : [iI];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];

fragment LETTER : [a-zA-Z];
fragment SLETTER : [a-z];
fragment CLETTER : [A-Z];
fragment DIGIT : [0-9];

fragment NEW_LINE : '\r'? '\n';

BLOCK_COMMENT : '(*' (BLOCK_COMMENT | .)*? 
	('*)' | EOF {raiseError("EOF in comment");} )  -> skip;
	
LINE_COMMENT : '--' .*? (NEW_LINE | EOF) -> skip;


//Keywords
CLASS : C L A S S;
ELSE : E L S E;
FALSE : 'f' A L S E;
FI	: F I;
IF : I F;
IN : I N;
INHERITS : I N H E R I T S;
ISVOID: I S V O I D;
LET : L E T;
LOOP : L O O P;
POOL : P O O L;
THEN : T H E N;
WHILE : W H I L E;
CASE : C A S E;
ESAC : E S A C;
NEW : N E W;
OF : O F;
NOT : N O T;
TRUE : 't' R U E ;

//Operators:
TILDE : '~';
MULT  : '*';
DIV  : '/';
PLUS : '+';
MINUS : '-';
ASSIGN : '<-';
LT : '<';
LTEQ : '<=';
EQ : '=';
CSARR : '=>';

//Symbols:
AT : '@';
DOT : '.';
SEMI : ';';
COMMA : ',';
COLON : ':';
LPAREN : '(';
RPAREN : ')';
LBRACK : '{';
RBRACK : '}'; 


//Defining string and treat possible errors
fragment ESC: '\\' (~'\r' | '\r\n' | '\n') ; 
STRING: '"' (ESC | . )*? ('"' | '\n' | '\r' | EOF)
	{
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
	};

//Type ID:
TYPE: CLETTER (LETTER | DIGIT | '_')*;
//Object ID:
ID  : SLETTER (LETTER | DIGIT | '_')*;

INT : DIGIT+;

WS :   [ \n\f\r\t]+ -> skip;
UNMATCHED_COMMENT : '*'+')'  (NEW_LINE | EOF) {raiseError("Unmatched *)");};

//Error for unknown characters
OTHERS: .+? {String s = getText();  raiseError("Invalid character: ".concat(s));};
