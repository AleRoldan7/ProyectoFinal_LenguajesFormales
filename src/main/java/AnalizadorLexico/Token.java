package AnalizadorLexico;

/**
 * Clase que representa un token en el análisis léxico.
 */
public class Token {

    private String lexema;
    private String tipoToken;
    private int fila;
    private int columna;
    private int posicionInicial;
    private int tamaLexema;

    
    public static final String CREATE = "CREATE";
    public static final String DATABASE = "DATABASE";
    public static final String TABLE = "TABLE";
    public static final String IF = "IF";
    public static final String EXISTS = "EXISTS";
    public static final String CASCADE = "CASCADE";
    public static final String FOREIGN = "FOREIGN";
    public static final String KEY = "KEY";
    public static final String REFERENCES = "REFERENCES";
    public static final String ALTER = "ALTER";
    public static final String ADD = "ADD";
    public static final String COLUMN = "COLUMN";
    public static final String DROP = "DROP";
    public static final String CONSTRAINT = "CONSTRAINT";
    public static final String PRIMARY = "PRIMARY";
    public static final String NOT = "NOT";
    public static final String NULL = "NULL";
    public static final String UNIQUE = "UNIQUE";
    public static final String TYPE = "TYPE";
    public static final String ON = "ON";                 
    public static final String DELETE = "DELETE";             
    public static final String SET = "SET";               
    public static final String UPDATE = "UPDATE";      
    public static final String INSERT = "INSERT";            
    public static final String INTO = "INTO";               
    public static final String VALUES = "VALUES";             
    public static final String SELECT = "SELECT";             
    public static final String FROM = "FROM";              
    public static final String WHERE = "WHERE";              
    public static final String AS = "AS";                
    public static final String GROUP = "GROUP";              
    public static final String ORDER = "ORDER";             
    public static final String BY = "BY";                
    public static final String ASC = "ASC";               
    public static final String DESC = "DESC";               
    public static final String LIMIT = "LIMIT";              
    public static final String JOIN = "JOIN";               
    
    
    public static final String SERIAL = "SERIAL";
    public static final String INTEGER = "INTEGER";
    public static final String BIGINT = "BIGINT";
    public static final String VARCHAR = "VARCHAR";
    public static final String DECIMAL = "DECIMAL";
    public static final String DATE = "DATE";
    public static final String TEXT = "TEXT";
    public static final String BOOLEAN = "BOOLEAN";

 
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";

  
    public static final String SUM = "SUM";
    public static final String AVG = "AVG";
    public static final String COUNT = "COUNT";
    public static final String MAX = "MAX";
    public static final String MIN = "MIN";

   
    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final String NOT_OPERATOR = "NOT_OPERATOR"; 


    public static final String IDENTIFICADOR = "IDENTIFICADOR";
    public static final String PUNTO_COMA = "PUNTO_COMA";
    public static final String COMA = "COMA";
    public static final String PARENTESIS_APERTURA = "PARENTESIS_APERTURA";
    public static final String PARENTESIS_CIERRE = "PARENTESIS_CIERRE";
    public static final String PUNTO = "PUNTO";
    public static final String IGUAL = "IGUAL";
   
    public static final String STRING_LITERAL = "STRING_LITERAL";
    public static final String DATE_FORMAT = "DATE_FORMAT";
    public static final String COMMENT_LINE = "COMMENT_LINE";
    public static final String ENTERO = "ENTERO";
    public static final String DECIMAL_LITERAL = "DECIMAL_LITERAL";

    public static final String SUMA = "+";
    public static final String RESTA = "-";
    public static final String MULTI = "*";
    public static final String DIVISION = "/";

    public static final String MENOR = "<";
    public static final String MAYOR = ">";
    public static final String IGUALMAYOR = ">=";
    public static final String IGUALMENOR = "<=";
    
    
    
    public Token(String lexema, String tipoToken, int fila, int columna, int posicionInicial, int tamaLexema) {
        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.fila = fila;
        this.columna = columna;
        this.posicionInicial = posicionInicial;
        this.tamaLexema = tamaLexema;
    }

   
    public String getLexema() { return lexema; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public int getPosicionInicial() { return posicionInicial; }
    public int getTamaLexema() { return tamaLexema; }
    public String getTipoToken() { return tipoToken; }
    public void setTipoToken(String tipoToken) { this.tipoToken = tipoToken; }

    @Override
    public String toString() {
        return "Token [ lexema = " + lexema + ", tipo = " + tipoToken + ", fila = " + fila + ", columna = " + columna + 
               ", tamaño = " + tamaLexema + "]";
    }
}
