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

// Expresiones Regulares
//ENTERO = [0-9]+
//IDENTIFICADOR = [a-z0-9_]+
//ESPACIOS = [" "\r\t\b\n]

%%

// PALABRAS RESERVADAS CREATE

"CREATE"                                    { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0));  }
"DATABASE"                                  { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0));  }
"TABLE"                                     { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0));  }
"KEY"                                       { agregarColor(yytext(), (int) yychar, yytext().length(),  new Color(255, 140, 0));  }


