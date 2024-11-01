package AnalizadorLexico;

// Sección de imports
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
%%

%{
// Código Java
    private List<ColorTexto> listaColores = new ArrayList<>();

    private void agregarColor(String nombre, int inicio, int tamanio, Color color) {
        // Evitar agregar múltiples entradas para el mismo token en la misma posición
        if (!listaColores.isEmpty()) {
            ColorTexto ultimo = listaColores.get(listaColores.size() - 1);
            if (ultimo.getInicio() == inicio && ultimo.getnToken().equals(nombre)) {
                return; // Evita agregar duplicados
            }
        }
        listaColores.add(new ColorTexto(nombre, inicio, tamanio, color));
    }


    
    public List<ColorTexto> getListaTokensColoreados() {
        return listaColores;
    }
    

%}

// Configuración de JFlex
%public
%class colorLexico
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
IDENTIFICADOR = [a-z0-9_]+
ESPACIOS = [" "\r\t\b\n]

%%

// PALABRAS RESERVADAS CREATE

"CREATE"                                    { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0));  }
"DATABASE"                                  { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0));  }
"TABLE"                                     { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0));  }
"KEY"                                       { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0));  }
"PRIMARY"            { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"NOT"                { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0));}
"NULL"               { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"UNIQUE"             { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"FOREIGN"            { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"REFERENCES"         { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"ALTER"              { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"ADD"                { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0));}
"COLUMN"             { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"TYPE"               { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"DROP"               { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"CONSTRAINT"         { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"IF"                 { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"EXISTS"             { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"CASCADE"            { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"ON"                 { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"DELETE"             { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"SET"                { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"UPDATE"             { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"INSERT"             { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"INTO"               { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"VALUES"             { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"SELECT"             { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"FROM"               { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"WHERE"              { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"AS"                 { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"GROUP"              { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"ORDER"              { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"BY"                 { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"ASC"                { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"DESC"               { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"LIMIT"              { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"JOIN"               { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }

// TIPOS DE DATOS
"INTEGER"            { agregarColor(yytext(), (int) yychar, yytext().length(), new Color(143, 0, 255)); }
"BIGINT"             { agregarColor(yytext(), (int) yychar, yytext().length(), new Color(143, 0, 255)); }
"VARCHAR"            { agregarColor(yytext(), (int) yychar, yytext().length(), new Color(143, 0, 255)); }
"DECIMAL"            { agregarColor(yytext(), (int) yychar, yytext().length(), new Color(143, 0, 255)); }
"DATE"               { agregarColor(yytext(), (int) yychar, yytext().length(), new Color(143, 0, 255)); }
"TEXT"               { agregarColor(yytext(), (int) yychar, yytext().length(), new Color(143, 0, 255)); }
"BOOLEAN"            { agregarColor(yytext(), (int) yychar, yytext().length(), new Color(143, 0, 255)); }
"SERIAL"             { agregarColor(yytext(), (int) yychar, yytext().length(), new Color(143, 0, 255)); }

// TIPOS AGREGACION
"SUM"               { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLUE); }
"AVG"               { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLUE); }
"COUNT"             { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLUE);}
"MAX"               { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLUE); }
"MIN"               { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLUE); }




// FECHAS
{FECHA}                                      { agregarColor(yytext(), (int) yychar, yytext().length(), Color.YELLOW); }

{ENTERO}            { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLUE); }
{ENTERO}"."{ENTERO} { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLUE); }


{IDENTIFICADOR} { agregarColor(yytext(), (int) yychar, yytext().length(), Color.MAGENTA); }

// CADENAS
{CADENA}                                     { agregarColor(yytext(), (int) yychar, yytext().length(), Color.GREEN); }





"TRUE"            { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLUE); }
"FALSE"           { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLUE); }

// SIGNOS
"("            { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }
")"            { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }
","            { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }
";"            { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }
"="            { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }
"."            { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }


// ARITMETICOS
"+"                 { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }
"-"                 { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }
"*"                 { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }
"/"                 { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }

// RELACIONALES
"<"                  { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }
">"                  { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }
"<="                 { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }
">="                 { agregarColor(yytext(), (int) yychar, yytext().length(), Color.BLACK); }

// LOGICOS
"AND"                { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }
"OR"                 { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0)); }

{ESPACIOS}           { /*Ignore*/ }


