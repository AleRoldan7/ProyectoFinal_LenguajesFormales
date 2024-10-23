package AnalizadorLexico;

//Seccion de imports
import java.util.ArrayList;
import java.util.List;

%%
%{

// Codigo Java

    private List<String> lista = new ArrayList<>();
    private List<String> listaErrores = new ArrayList<>();
    private List<TokenCreate> listCreate = new ArrayList<>();
    private List<TokenDato> listTipoDato = new ArrayList<>();
    private List<Token> tokens = new ArrayList<>();
    private List<TokenEntero> listEntero = new ArrayList<>();
    private List<TokenIdenti> listIden = new ArrayList<>();
    private List<TokenError> tokenError = new ArrayList<>();
    private List<TokenSigno> listSigno = new ArrayList<>();
    public void addList(String token) {
        lista.add(token);
    }

    public void addListaErrores(String token) {
        listaErrores.add(token);
    }

    public List<String> getLista(){
        return lista;
    }
    
    public List<String> getListaErrores(){
        return listaErrores;
    }

    private Token token(String lexema, String tipo, int line, int column, int posicion, int tama) {
        return new Token(lexema,  tipo, line+1, column+1, posicion, tama);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    private TokenCreate tokencreate(String lexema, String tipo, int line, int column, int posicion, int tama) {
        return new TokenCreate(lexema, tipo, line+1, column+1,  posicion, tama);
    }

    public List<TokenCreate> getTokensCreate() {
        return listCreate;
    }
    
    public TokenDato tokendato(String lexema, String tipo, int line, int column, int posicion, int tama){
        return new TokenDato(lexema, tipo, line+1, column+1, posicion, tama);
    }

    public List<TokenDato> getTokensDato() {
        return listTipoDato;
    }

    public TokenIdenti tokenidenti(String lexema, String tipo, int line, int column, int posicion, int tama){
        return new TokenIdenti(lexema, tipo, line+1, column+1, posicion, tama);
    }

    public List<TokenIdenti> getTokensIdenti() {
        return listIden;
    }

    public TokenEntero tokenentero(String lexema, String tipo, int line, int column, int posicion, int tama){
        return new TokenEntero(lexema, tipo, line+1, column+1, posicion, tama);
    }

    public List<TokenEntero> getTokensEntero() {
        return listEntero;
    }

    public TokenSigno tokensigno(String lexema, String tipo, int line, int column, int posicion, int tama){
        return new TokenSigno(lexema, tipo, line+1, column+1, posicion, tama);
    }

    public List<TokenSigno> getTokensSigno() {
        return listSigno;
    }

    
    public TokenError tokenerror(String lexema, String tipo, int line, int column, int posicion, int tama){
        return new TokenError(lexema, tipo, line+1, column+1, posicion, tama);
    }

    public List<TokenError> getTokenError() {
        return tokenError;
    }
    


%}

// Configuracion
%public
%class AnalizadorLexico
%unicode
%line
%column
%standalone

// Expresiones Regulares
ENTERO = [0-9]+
IDENTIFICADOR = [a-z0-9_]+
ESPACIOS = [" "\r\t\b\n]


%%
//PALABRAS RESERVADAS CREATE
"CREATE"                                    { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"DATABASE"                                  { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"TABLE"                                     { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length()));}
"KEY"                                       { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"NULL"                                      { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"PRIMARY"                                   { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"UNIQUE"                                    { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"FOREIGN"                                   { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"REFERENCES"                                { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"ALTER"                                     { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"ADD"                                       { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"COLUMN"                                    { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"TYPE"                                      { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"DROP"                                      { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"CONSTRAINT"                                { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"IF"                                        { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"EXIST"                                     { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"CASCADE"                                   { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"ON"                                        { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"DELETE"                                    { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"SET"                                       { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"UPDATE"                                    { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"INSERT"                                    { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"INTO"                                      { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"VALUES"                                    { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"SELECT"                                    { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"FROM"                                      { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"WHERE"                                     { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"AS"                                        { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"GROUP"                                     { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"ORDER"                                     { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"BY"                                        { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"ASC"                                       { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"DESC"                                      { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"LIMIT"                                     { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"JOIN"                                      { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }

//TIPO DATO
"INTEGER"                                   { listTipoDato.add(tokendato(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"BIGINT"                                    { listTipoDato.add(tokendato(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"VARCHAR"                                   { listTipoDato.add(tokendato(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"DECIMAL"                                   { listTipoDato.add(tokendato(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"DATE"                                      { listTipoDato.add(tokendato(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"TEXT"                                      { listTipoDato.add(tokendato(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"BOOLEAN"                                   { listTipoDato.add(tokendato(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }
"SERIAL"                                    { listTipoDato.add(tokendato(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }



// DATO ENTERO
{ENTERO}                                    { listEntero.add(tokenentero(yytext(), "Entero", yyline, yycolumn, (int) yychar, yytext().length())); }

// DATO DECIMAL
{ENTERO}"."{ENTERO}                         { listEntero.add(tokenentero(yytext(), "Entero", yyline, yycolumn, (int) yychar, yytext().length())); }

//IDENTIFICADOR
{IDENTIFICADOR}                             { listIden.add(tokenidenti(yytext(), "IDENTIFICADOR", yyline, yycolumn, (int) yychar, yytext().length())); }

// CADENA
\'[^']*\'                                   { System.out.println("CADENA: "+yytext()); addList(yytext()); }



// BOOLEANO
"TRUE"                                      { System.out.println("Booleano: "+yytext()); addList(yytext());}
"FALSE"                                     { System.out.println("Booleano: "+yytext()); addList(yytext());}

//FUNCIONES AGREGACION
"SUM"                                       { System.out.println("Funciones de Agregación: "+yytext()); addList(yytext());}
"AVG"                                       { System.out.println("Funciones de Agregación: "+yytext()); addList(yytext());}
"COUNT"                                     { System.out.println("Funciones de Agregación: "+yytext()); addList(yytext());}
"MAX"                                       { System.out.println("Funciones de Agregación: "+yytext()); addList(yytext());}
"MIN"                                       { System.out.println("Funciones de Agregación: "+yytext()); addList(yytext());}

//SIGNOS
"("                                         { listSigno.add(tokensigno(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length()));}
")"                                         { listSigno.add(tokensigno(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length()));}
","                                         { listSigno.add(tokensigno(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length()));}
";"                                         { listSigno.add(tokensigno(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length()));}
"."                                         { listSigno.add(tokensigno(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length()));}
"="                                         { listSigno.add(tokensigno(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length()));}


//ARITMETICOS
"+"                                         { System.out.println("ARITMETICOS: "+yytext()); addList(yytext()); }
"-"                                         { System.out.println("ARITMETICOS: "+yytext()); addList(yytext()); }
"*"                                         { System.out.println("ARITMETICOS: "+yytext()); addList(yytext()); }
"/"                                         { System.out.println("ARITMETICOS: "+yytext()); addList(yytext()); }

//RELACIONALES
"<"                                         { System.out.println("RELACIONALES: "+yytext()); addList(yytext()); }
">"                                         { System.out.println("RELACIONALES: "+yytext()); addList(yytext()); }
"<="                                        { System.out.println("RELACIONALES: "+yytext()); addList(yytext()); }
">="                                        { System.out.println("RELACIONALES: "+yytext()); addList(yytext()); }

//LOGICOS
"AND"                                       { System.out.println("LOGICOS: "+yytext()); addList(yytext()); }
"OR"                                        { System.out.println("LOGICOS: "+yytext()); addList(yytext()); }
"NOT"                                       { listCreate.add(tokencreate(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length()));}

{ESPACIOS}                                  { /*Ignore*/ }

. | "<>"                                    { tokenError.add(tokenerror(yytext(), yytext(), yyline, yycolumn, (int) yychar, yytext().length())); }




