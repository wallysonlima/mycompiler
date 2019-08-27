import java_cup.runtime.*;
import model.Analyse;

%%

%{

private Analyse createAnalyse(String lexeme, String token, int line, int iniCol, int endCol) {
    return (new Analyse(lexeme, token, String.valueOf(line), String.valueOf(iniCol), String.valueOf(yylength() + endCol)));
}

%}

%public
%class LexicalAnalyzer
%type Analyse
%line
%column

BRANCO = [\n| |\t|\r]
IDENTIFICADOR = [_|a-z|A-Z][a-z|A-Z|0-9|_]*
INTEIRO = 0|[1-9][0-9]*
REAL = [0-9][0-9]*","[0-9]+
INVALID_CHARACTERE = [ ! | @ | # | $ | % | & ]*

LineTerminator = \r|\n|\r\n
AC = \{
FC = \}
AbreChaveNegado = [^}]
ComentarioCorpo = {AbreChaveNegado}*
Comment = {AC}{ComentarioCorpo}{FC}
CommentLine = {OP_DIV}{OP_DIV}{ComentarioCorpo}

AP = "("
FP = ")"
ACO = "["
FCO = "]"
PONTO = "."
VIRGULA = ","
PONTO_VIRGULA = ";"

OP_SOMA = "+"
OP_SUB = "-"
OP_DIV = "/"
OP_MULT = "*"

OP_MENOR = "<"
OP_IGUAL = ":="
OP_MAIOR = ">"
OP_MENORIGUAL = "<="
OP_MAIORIGUAL = ">="
OP_DIFERENTE = "<>"

PROGRAM = "program"
PROCEDURE = "procedure"
VAR = "var"
INT = "int"
BOOLEAN = "boolean"
READ = "read"
WRITE = "write"
TRUE = "true"
FALSE = "false"
BEGIN = "begin"
END = "end"
IF = "if"
WHILE = "while"
DO = "do"
ELSE = "else"
THEN = "then"
DIV = "div"
AND = "and"
OR = "or"
NOT = "not"

%%

{BRANCO} { /* */ }

{INTEIRO} { return createAnalyse(yytext(), "Inteiro", yyline, yycolumn, yycolumn); }
{REAL} { return createAnalyse(yytext(), "Real", yyline, yycolumn, yycolumn); }
{INVALID_CHARACTERE} { return createAnalyse(yytext(), "Caractere_Inválido", yyline, yycolumn, yycolumn); }


{AP} { return createAnalyse(yytext(), "Abre_Parenteses", yyline, yycolumn, yycolumn); }
{FP} { return createAnalyse(yytext(), "Fecha_Parenteses", yyline, yycolumn, yycolumn); }
{ACO} { return createAnalyse(yytext(), "Abre_Colchetes", yyline, yycolumn, yycolumn); }
{FCO} { return createAnalyse(yytext(), "Fecha_Colchetes", yyline, yycolumn, yycolumn); }
{PONTO} { return createAnalyse(yytext(), "Ponto", yyline, yycolumn, yycolumn); }
{VIRGULA} { return createAnalyse(yytext(), "Virgula", yyline, yycolumn, yycolumn); }
{PONTO_VIRGULA} { return createAnalyse(yytext(), "Ponto_Virgula", yyline, yycolumn, yycolumn); }

{OP_SOMA} { return createAnalyse(yytext(), "Operador_Soma", yyline, yycolumn, yycolumn); }
{OP_SUB} { return createAnalyse(yytext(), "Operador_Subtração", yyline, yycolumn, yycolumn); }
{OP_MULT} { return createAnalyse(yytext(), "Operador_Multiplicação", yyline, yycolumn, yycolumn); }

{OP_MENOR} { return createAnalyse(yytext(), "Operador_Menor", yyline, yycolumn, yycolumn); }
{OP_IGUAL} { return createAnalyse(yytext(), "Operador_Igual", yyline, yycolumn, yycolumn); }
{OP_MAIOR} { return createAnalyse(yytext(), "Operador_Maior", yyline, yycolumn, yycolumn); }
{OP_MENORIGUAL} { return createAnalyse(yytext(), "Operador_Menor_Igual", yyline, yycolumn, yycolumn); }
{OP_MAIORIGUAL} { return createAnalyse(yytext(), "Operador_Maior_Igual", yyline, yycolumn, yycolumn); }
{OP_DIFERENTE} { return createAnalyse(yytext(), "Operador_Diferente", yyline, yycolumn, yycolumn); }

{PROGRAM} { return createAnalyse(yytext(), "Palavra_Reservada_Program", yyline, yycolumn, yycolumn); }
{PROCEDURE} { return createAnalyse(yytext(), "Palavra_Reservada_Procedure", yyline, yycolumn, yycolumn); }
{VAR} { return createAnalyse(yytext(), "Palavra_Reservada_Var", yyline, yycolumn, yycolumn); }
{INT} { return createAnalyse(yytext(), "Palavra_Reservada_Int", yyline, yycolumn, yycolumn); }
{BOOLEAN} { return createAnalyse(yytext(), "Palavra_Reservada_Boolean", yyline, yycolumn, yycolumn); }
{READ} { return createAnalyse(yytext(), "Palavra_Reservada_Read", yyline, yycolumn, yycolumn); }
{WRITE} { return createAnalyse(yytext(), "Palavra_Reservada_Write", yyline, yycolumn, yycolumn); }
{TRUE} { return createAnalyse(yytext(), "Palavra_Reservada_True", yyline, yycolumn, yycolumn); }
{FALSE} { return createAnalyse(yytext(), "Palavra_Reservada_False", yyline, yycolumn, yycolumn); }
{BEGIN} { return createAnalyse(yytext(), "Palavra_Reservada_Begin", yyline, yycolumn, yycolumn); }
{END} { return createAnalyse(yytext(), "Palavra_Reservada_End", yyline, yycolumn, yycolumn); }
{IF} { return createAnalyse(yytext(), "Palavra_Reservada_If", yyline, yycolumn, yycolumn); }
{WHILE} { return createAnalyse(yytext(), "Palavra_Reservada_While", yyline, yycolumn, yycolumn); }
{DO} { return createAnalyse(yytext(), "Palavra_Reservada_Do", yyline, yycolumn, yycolumn); }
{ELSE} { return createAnalyse(yytext(), "Palavra_Reservada_Else", yyline, yycolumn, yycolumn); }
{THEN} { return createAnalyse(yytext(), "Palavra_Reservada_Then", yyline, yycolumn, yycolumn); }
{DIV} { return createAnalyse(yytext(), "Palavra_Reservada_Div", yyline, yycolumn, yycolumn); }
{AND} { return createAnalyse(yytext(), "Palavra_Reservada_And", yyline, yycolumn, yycolumn); }
{OR} { return createAnalyse(yytext(), "Palavra_Reservada_Or", yyline, yycolumn, yycolumn); }
{NOT} { return createAnalyse(yytext(), "Palavra_Reservada_Not", yyline, yycolumn, yycolumn); }

{IDENTIFICADOR} { return createAnalyse(yytext(), "Identificador", yyline, yycolumn, yycolumn); }

{Comment} { /* Ignore Comments */ }
{CommentLine} { /* Ignore Comments */ }

{AC} { return createAnalyse(yytext(), "Abre_Chaves", yyline, yycolumn, yycolumn); }
{FC} { return createAnalyse(yytext(), "Fecha_Chaves", yyline, yycolumn, yycolumn); }
{OP_DIV} { return createAnalyse(yytext(), "Operador_Divisão", yyline, yycolumn, yycolumn); }

. { return createAnalyse(yytext(), "Error ! Invalid Charactere !", yyline, yycolumn, yycolumn); }

