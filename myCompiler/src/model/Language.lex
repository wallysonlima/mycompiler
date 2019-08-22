
%%

%{

private void imprimir(String descricao, String lexema) {
    System.out.println(lexema + " - " + descricao);
}

%}

%class Analyze
%type void


BRANCO = [\n| |\t|\r]
ID = [_|a-z|A-Z][a-z|A-Z|0-9|_]*
OP_SOMA = "+"
OP_SUB = "-"
OP_DIV = "/"
OP_MULT = "*"
OP_MENOR = "<"
OP_IGUAL = "="
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

INTEIRO = 0|[1-9][0-9]*
REAL = [0-9][0-9]*","[0-9]+

%%

"if"                         { imprimir("Palavra reservada if", yytext()); }
"then"                       { imprimir("Palavra reservada then", yytext()); }
{BRANCO}                     { imprimir("Espaço em branco", yytext()); }
{ID}                         { imprimir("Identificador", yytext()); }
{SOMA}                         { imprimir("Operador de soma", yytext()); }
{INTEIRO}                     { imprimir("Número Inteiro", yytext()); }

. { throw new RuntimeException("Caractere inválido " + yytext()); }