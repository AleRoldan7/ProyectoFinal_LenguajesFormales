package AnalizadorLexico;

// Sección de imports
import java.util.ArrayList;
import java.util.List;

%%
// Código Java
%{
    private List<Token> tokens = new ArrayList<>();  // Lista general de tokens
    private List<TokenError> tokenError = new ArrayList<>();  // Lista para errores

    public List<Token> getTokens() {
        return tokens;
    }

    public List<TokenError> getTokenError() {
        return tokenError;
    }

    private Token createToken(String lexema, String tipo, int line, int column, int posicion, int tama) {
        return new Token(lexema, tipo, line+1, column+1, posicion, tama);
    }

    private TokenError createErrorToken(String lexema, int line, int column, int posicion, int tama) {
        return new TokenError(lexema, "ERROR", line+1, column+1, posicion, tama);
    }
%}

// Configuración
%public
%class AnalizadorLexico
%unicode
%line
%column
%standalone

// Expresiones regulares
FECHA_VALIDA = ([1-2][0-9]{3})"-"([0-1][0-9])"-"([0-3][0-9])
FECHA = "'" {FECHA_VALIDA} "'"
CADENA = "'".*"'"
// Comentarios de una línea que comienzan con dos guiones medio seguidos de un espacio
COMENTARIO_LINEA = "--"[^(\r|\n)]*

// Expresiones Regulares
ENTERO = [0-9]+
// IDENTIFICADOR en formato snake_case: letras minúsculas, guion bajo y números
IDENTIFICADOR = [a-z]+(_[a-z0-9]+)*
// Espacios
ESPACIOS = [" "\r\t\b\n]



%%
// PALABRAS RESERVADAS SQL
"CREATE"             { tokens.add(createToken(yytext(), Token.CREATE, yyline, yycolumn, (int) yychar, yytext().length())); }
"DATABASE"           { tokens.add(createToken(yytext(), Token.DATABASE, yyline, yycolumn, (int) yychar, yytext().length())); }
"TABLE"              { tokens.add(createToken(yytext(), Token.TABLE, yyline, yycolumn, (int) yychar, yytext().length())); }
"PRIMARY"            { tokens.add(createToken(yytext(), Token.PRIMARY, yyline, yycolumn, (int) yychar, yytext().length())); }
"KEY"                { tokens.add(createToken(yytext(), Token.KEY, yyline, yycolumn, (int) yychar, yytext().length())); }
"NOT"                { tokens.add(createToken(yytext(), Token.NOT, yyline, yycolumn, (int) yychar, yytext().length())); }
"NULL"               { tokens.add(createToken(yytext(), Token.NULL, yyline, yycolumn, (int) yychar, yytext().length())); }
"UNIQUE"             { tokens.add(createToken(yytext(), Token.UNIQUE, yyline, yycolumn, (int) yychar, yytext().length())); }
"FOREIGN"            { tokens.add(createToken(yytext(), Token.FOREIGN, yyline, yycolumn, (int) yychar, yytext().length())); }
"REFERENCES"         { tokens.add(createToken(yytext(), Token.REFERENCES, yyline, yycolumn, (int) yychar, yytext().length())); }
"ALTER"              { tokens.add(createToken(yytext(), Token.ALTER, yyline, yycolumn, (int) yychar, yytext().length())); }
"ADD"                { tokens.add(createToken(yytext(), Token.ADD, yyline, yycolumn, (int) yychar, yytext().length())); }
"COLUMN"             { tokens.add(createToken(yytext(), Token.COLUMN, yyline, yycolumn, (int) yychar, yytext().length())); }
"TYPE"               { tokens.add(createToken(yytext(), Token.TYPE, yyline, yycolumn, (int) yychar, yytext().length())); }
"DROP"               { tokens.add(createToken(yytext(), Token.DROP, yyline, yycolumn, (int) yychar, yytext().length())); }
"CONSTRAINT"         { tokens.add(createToken(yytext(), Token.CONSTRAINT, yyline, yycolumn, (int) yychar, yytext().length())); }
"IF"                 { tokens.add(createToken(yytext(), Token.IF, yyline, yycolumn, (int) yychar, yytext().length())); }
"EXISTS"             { tokens.add(createToken(yytext(), Token.EXISTS, yyline, yycolumn, (int) yychar, yytext().length())); }
"CASCADE"            { tokens.add(createToken(yytext(), Token.CASCADE, yyline, yycolumn, (int) yychar, yytext().length())); }
"ON"                 { tokens.add(createToken(yytext(), Token.ON, yyline, yycolumn, (int) yychar, yytext().length())); }
"DELETE"             { tokens.add(createToken(yytext(), Token.DELETE, yyline, yycolumn, (int) yychar, yytext().length())); }
"SET"                { tokens.add(createToken(yytext(), Token.SET, yyline, yycolumn, (int) yychar, yytext().length())); }
"UPDATE"             { tokens.add(createToken(yytext(), Token.UPDATE, yyline, yycolumn, (int) yychar, yytext().length())); }
"INSERT"             { tokens.add(createToken(yytext(), Token.INSERT, yyline, yycolumn, (int) yychar, yytext().length())); }
"INTO"               { tokens.add(createToken(yytext(), Token.INTO, yyline, yycolumn, (int) yychar, yytext().length())); }
"VALUES"             { tokens.add(createToken(yytext(), Token.VALUES, yyline, yycolumn, (int) yychar, yytext().length())); }
"SELECT"             { tokens.add(createToken(yytext(), Token.SELECT, yyline, yycolumn, (int) yychar, yytext().length())); }
"FROM"               { tokens.add(createToken(yytext(), Token.FROM, yyline, yycolumn, (int) yychar, yytext().length())); }
"WHERE"              { tokens.add(createToken(yytext(), Token.WHERE, yyline, yycolumn, (int) yychar, yytext().length())); }
"AS"                 { tokens.add(createToken(yytext(), Token.AS, yyline, yycolumn, (int) yychar, yytext().length())); }
"GROUP"              { tokens.add(createToken(yytext(), Token.GROUP, yyline, yycolumn, (int) yychar, yytext().length())); }
"ORDER"              { tokens.add(createToken(yytext(), Token.ORDER, yyline, yycolumn, (int) yychar, yytext().length())); }
"BY"                 { tokens.add(createToken(yytext(), Token.BY, yyline, yycolumn, (int) yychar, yytext().length())); }
"ASC"                { tokens.add(createToken(yytext(), Token.ASC, yyline, yycolumn, (int) yychar, yytext().length())); }
"DESC"               { tokens.add(createToken(yytext(), Token.DESC, yyline, yycolumn, (int) yychar, yytext().length())); }
"LIMIT"              { tokens.add(createToken(yytext(), Token.LIMIT, yyline, yycolumn, (int) yychar, yytext().length())); }
"JOIN"               { tokens.add(createToken(yytext(), Token.JOIN, yyline, yycolumn, (int) yychar, yytext().length())); }

// TIPOS DE DATOS
"INTEGER"            { tokens.add(createToken(yytext(), Token.INTEGER, yyline, yycolumn, (int) yychar, yytext().length())); }
"BIGINT"             { tokens.add(createToken(yytext(), Token.BIGINT, yyline, yycolumn, (int) yychar, yytext().length())); }
"VARCHAR"            { tokens.add(createToken(yytext(), Token.VARCHAR, yyline, yycolumn, (int) yychar, yytext().length())); }
"DECIMAL"            { tokens.add(createToken(yytext(), Token.DECIMAL, yyline, yycolumn, (int) yychar, yytext().length())); }
"DATE"               { tokens.add(createToken(yytext(), Token.DATE, yyline, yycolumn, (int) yychar, yytext().length())); }
"TEXT"               { tokens.add(createToken(yytext(), Token.TEXT, yyline, yycolumn, (int) yychar, yytext().length())); }
"BOOLEAN"            { tokens.add(createToken(yytext(), Token.BOOLEAN, yyline, yycolumn, (int) yychar, yytext().length())); }
"SERIAL"             { tokens.add(createToken(yytext(), Token.SERIAL, yyline, yycolumn, (int) yychar, yytext().length())); }

// TIPOS AGREGACION
"SUM"               { tokens.add(createToken(yytext(), Token.SUM, yyline, yycolumn, (int) yychar, yytext().length())); }
"AVG"               { tokens.add(createToken(yytext(), Token.AVG, yyline, yycolumn, (int) yychar, yytext().length())); }
"COUNT"             { tokens.add(createToken(yytext(), Token.COUNT, yyline, yycolumn, (int) yychar, yytext().length())); }
"MAX"               { tokens.add(createToken(yytext(), Token.MAX, yyline, yycolumn, (int) yychar, yytext().length())); }
"MIN"               { tokens.add(createToken(yytext(), Token.MIN, yyline, yycolumn, (int) yychar, yytext().length())); }


// Cadenas
{CADENA} { 
    tokens.add(createToken(yytext(), Token.STRING_LITERAL, yyline, yycolumn, (int) yychar, yytext().length())); 
    System.out.println("Cadena válida: " + yytext());
}

// Fechas
{FECHA} { 
    tokens.add(createToken(yytext(), Token.DATE_FORMAT, yyline, yycolumn, (int) yychar, yytext().length())); 
    System.out.println("Fecha válida: " + yytext());
}

// Comentarios de línea
{COMENTARIO_LINEA} { 
    tokens.add(createToken(yytext(), Token.COMMENT_LINE, yyline, yycolumn, (int) yychar, yytext().length())); 
    System.out.println("Comentario: " + yytext());
}


// DATO ENTERO Y DECIMAL
{ENTERO}             { tokens.add(createToken(yytext(), Token.ENTERO, yyline, yycolumn, (int) yychar, yytext().length())); }
{ENTERO}"."{ENTERO}  { tokens.add(createToken(yytext(), Token.DECIMAL_LITERAL, yyline, yycolumn, (int) yychar, yytext().length())); }

// IDENTIFICADOR en formato snake_case
{IDENTIFICADOR}      { tokens.add(createToken(yytext(), Token.IDENTIFICADOR, yyline, yycolumn, (int) yychar, yytext().length())); }



// BOOLEANOS
"TRUE"               { tokens.add(createToken(yytext(), Token.TRUE, yyline, yycolumn, (int) yychar, yytext().length())); }
"FALSE"              { tokens.add(createToken(yytext(), Token.FALSE, yyline, yycolumn, (int) yychar, yytext().length())); }

// SIGNOS
"("                  { tokens.add(createToken(yytext(), Token.PARENTESIS_APERTURA, yyline, yycolumn, (int) yychar, yytext().length())); }
")"                  { tokens.add(createToken(yytext(), Token.PARENTESIS_CIERRE, yyline, yycolumn, (int) yychar, yytext().length())); }
","                  { tokens.add(createToken(yytext(), Token.COMA, yyline, yycolumn, (int) yychar, yytext().length())); }
";"                  { tokens.add(createToken(yytext(), Token.PUNTO_COMA, yyline, yycolumn, (int) yychar, yytext().length())); }
"."                  { tokens.add(createToken(yytext(), Token.PUNTO, yyline, yycolumn, (int) yychar, yytext().length())); }
"="                  { tokens.add(createToken(yytext(), Token.IGUAL, yyline, yycolumn, (int) yychar, yytext().length())); }

// ARITMETICOS
"+"                  { tokens.add(createToken(yytext(), Token.SUMA, yyline, yycolumn, (int) yychar, yytext().length())); }
"-"                  { tokens.add(createToken(yytext(), Token.RESTA, yyline, yycolumn, (int) yychar, yytext().length())); }
"*"                  { tokens.add(createToken(yytext(), Token.MULTI, yyline, yycolumn, (int) yychar, yytext().length())); }
"/"                  { tokens.add(createToken(yytext(), Token.DIVISION, yyline, yycolumn, (int) yychar, yytext().length())); }

// RELACIONALES
"<"                  { tokens.add(createToken(yytext(), Token.MENOR, yyline, yycolumn, (int) yychar, yytext().length())); }
">"                  { tokens.add(createToken(yytext(), Token.MAYOR, yyline, yycolumn, (int) yychar, yytext().length())); }
"<="                 { tokens.add(createToken(yytext(), Token.IGUALMENOR, yyline, yycolumn, (int) yychar, yytext().length())); }
">="                 { tokens.add(createToken(yytext(), Token.IGUALMAYOR, yyline, yycolumn, (int) yychar, yytext().length())); }

// LOGICOS
"AND"                { tokens.add(createToken(yytext(), Token.AND, yyline, yycolumn, (int) yychar, yytext().length())); }
"OR"                 { tokens.add(createToken(yytext(), Token.OR, yyline, yycolumn, (int) yychar, yytext().length())); }

{ESPACIOS}           { /*Ignore*/ }

[a-zA-Z_][a-zA-Z0-9_]* {
    tokenError.add(createErrorToken(yytext(), yyline, yycolumn, (int) yychar, yytext().length()));
}

/*
. {
    tokenError.add(createErrorToken(yytext(), yyline, yycolumn, (int) yychar, yytext().length()));
}
*/

