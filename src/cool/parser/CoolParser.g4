parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

program
    : (coolClass SEMI)* | EOF							
    ; 
    
coolClass
	:	CLASS name=TYPE	(INHERITS parentType=TYPE)? LBRACK (features+=feature SEMI)* RBRACK
	;
feature
 	:	name=ID LPAREN (formals+=formal (COMMA formals+=formal)*)? RPAREN COLON type=TYPE
 			LBRACK body=expr RBRACK													# funcDef
 	|	def																			# varDef											
 	;
formal
	:	name = ID COLON type=TYPE											
	;
 		
csarg
	:	name=ID COLON type=TYPE CSARR init=expr						
	;
def	
	:	name = ID COLON type=TYPE (ASSIGN e=expr)?				
	;	
								
 expr
    :	e=expr (AT type=TYPE)? DOT id=ID 
    		LPAREN (args+=expr (COMMA args+=expr)*)? RPAREN					# dispatch
    |	name=ID LPAREN (args+=expr (COMMA args+=expr)*)? RPAREN				# call
    |   IF cond=expr THEN thenBranch=expr ELSE elseBranch=expr FI   		# if
    |	WHILE cond=expr LOOP body=expr POOL									# while
    |	LBRACK (body+=expr SEMI)+ RBRACK									# brackets
    |	LET defs+=def (COMMA defs+=def)* IN e=expr							# let
    |	CASE e=expr OF	(conds+=csarg SEMI)+ ESAC							# case
    |	NEW type=TYPE														# newType
    | 	ISVOID e=expr														# isVoid
    |	TILDE e=expr														# notA
    |   left=expr op=(MULT | DIV) right=expr                        		# multDiv
    |   left=expr op=(PLUS | MINUS) right=expr                      		# plusMinus
  	|   left=expr op=(LT | LTEQ | EQ) right=expr                			# relational
    |	NOT e=expr															# notL
    |   LPAREN e=expr RPAREN                                        		# paren
    |   ID                                                          		# id
    |   INT                                                         		# int
    |	STRING																# string
    |   TRUE                                                        		# true
    |   FALSE                                                       		# false
    | 	name=ID ASSIGN e=expr                                       		# assign
    ;
    
    
 