package AnalizadorSintactico;

import AnalizadorLexico.Token;
import GeneradorDDL.Columna;
import ReportesToken.ReporteSintactico;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AnalizadorSintactico {

    private List<Token> tokens;
    private int pos = 0;
    private List<ReporteSintactico> erroresSintacticos = new ArrayList<>();
    private int contadorTablasEncontradas = 0;
    private int contadorTablasModificadas = 0;
    private List<String> reportesTablasEncontradas = new ArrayList<>();
    private List<String> reportesTablasModificadas = new ArrayList<>();
    private Map<String, Integer> contadorOperaciones = new HashMap<>();
    
    public AnalizadorSintactico(List<Token> tokens) {
        this.tokens = tokens;
        contadorOperaciones.put("create", 0);
        contadorOperaciones.put("delete", 0);
        contadorOperaciones.put("update", 0);
        contadorOperaciones.put("select", 0);
        contadorOperaciones.put("alter", 0);
    }

    public void analizarInstrucciones() {
        while (pos < tokens.size()) {
            Token token = getTokenActual();
            if (token == null) break;

            switch (token.getTipoToken()) {
                case Token.CREATE:
                    parseCreate();
                    break;
                case Token.ALTER:
                    parseAlterTable();
                    break;
                case Token.DROP:
                    parseDropTable();
                    break;
                case Token.INSERT:
                    parseInsert();
                    break;
                case Token.SELECT:
                    parseSelect();
                    break;
                case Token.UPDATE:
                    parseUpdate();
                    break;
                case Token.DELETE:
                    parseDelete();
                    break;
                default:
                    agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "Instrucción no reconocida.");
                    avanzarToken();
            }

            if (getTokenActual() != null && getTokenActual().getTipoToken().equals(Token.PUNTO_COMA)) {
                avanzarToken(); // Avanza después del ;
            }
        }
        mostrarErrores();
        mostrarContadores();
    }

    public List<ReporteSintactico> getErroresSintacticos() {
        return erroresSintacticos;
    }

    private void agregarErrorSintactico(String lexema, String tipoToken, int linea, int columna, String descripcion) {
        System.out.println("Agregando error: " + descripcion + " en " + lexema + " (Línea: " + linea + ", Columna: " + columna + ")");
        erroresSintacticos.add(new ReporteSintactico(lexema, tipoToken, linea, columna, descripcion));
    }

    private void parseCreate() {
        avanzarToken();
        if (comprobaReservada(Token.DATABASE)) {
            parseCreacionBase();
        } else if (comprobaReservada(Token.TABLE)) {
            parseCreacionTabla();
        } else {
            Token token = getTokenActual();
            agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "Se esperaba 'DATABASE' o 'TABLE' después de 'CREATE'");
        }
    }

    private void parseCreacionBase() {
        Token token = getTokenActual();
        
        if (token == null) {
            System.out.println("Falta nombre de la base");
            return;
        }
        
        if (comproIdentificador()) {
            if (comprobaSigno(Token.PUNTO_COMA)) {
                System.out.println("Base de datos creada");
            } else {
                agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "No tiene punto y coma");
                System.out.println("No tiene punto y coma");
            }
        } else {
            agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "No tiene nombre la base de datos");
            System.out.println("No tiene nombre");
        }
    }

    private void parseCreacionTabla() {
        Token token = getTokenActual();  // Obtener el token actual
        if (token == null) {
            agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "No tiene nombre la tabla");
            System.out.println("Error: Falta el nombre de la tabla.");
            return;
        }

        String nombreTabla = token.getLexema();  
        avanzarToken();

        List<Columna> columnas = new ArrayList<>();

        if (comprobaSigno(Token.PARENTESIS_APERTURA)) {
            boolean errorEnColumna = false;
            do {
                if (!parseColumna()) {
                    agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "Error en la columna");
                    System.out.println("Error en la declaración de una columna.");
                    errorEnColumna = true;
                    break;
                }
            } while (comprobaSigno(Token.COMA));

            if (!errorEnColumna && comprobaSigno(Token.PARENTESIS_CIERRE) && comprobaSigno(Token.PUNTO_COMA)) {
                contadorTablasEncontradas++;
                reportesTablasEncontradas.add("Tabla: " + nombreTabla + " (Línea: " + token.getFila() + ", Columna: " + token.getColumna() + ")");
                System.out.println("Tabla creada correctamente.");
                
            } else {
                agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "Falta ')' o ';' en la declaración de la tabla.");
                System.out.println("Error: Falta ')' o ';' en la declaración de la tabla.");
            }
        } else {
            agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "Falta abrir paréntesis '(' en la declaración de la tabla");
            System.out.println("Error: Falta abrir paréntesis '(' en la declaración de la tabla.");
        }
    }

    private String parseTipoDeDato() {
        if (comprobaReservada(Token.SERIAL)) return "SERIAL";
        if (comprobaReservada(Token.INTEGER)) return "INTEGER";
        if (comprobaReservada(Token.VARCHAR)) {
            if (comprobaSigno(Token.PARENTESIS_APERTURA) && comprobaEntero() && comprobaSigno(Token.PARENTESIS_CIERRE)) {
                return "VARCHAR";
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Formato incorrecto para VARCHAR.");
                return null;
            }
        }
        if (comprobaReservada(Token.DECIMAL)) {
            if (comprobaSigno(Token.PARENTESIS_APERTURA) && comprobaEntero() && comprobaSigno(Token.COMA) && comprobaEntero() && comprobaSigno(Token.PARENTESIS_CIERRE)) {
                return "DECIMAL";
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Formato incorrecto para DECIMAL.");
                return null;
            }
        }
        if (comprobaReservada(Token.DATE)) return "DATE";
        if (comprobaReservada(Token.BOOLEAN)) return "BOOLEAN";
        return null;
    }

    private boolean parseColumna() {
        if (comproIdentificador()) {
            String tipo = parseTipoDeDato();
            if (tipo != null) {
                parseRestricciones();  
                return true;
            } else {
                Token token = getTokenActual();
                agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "Tipo de dato no válido.");
                return false;
            }
        } else if (comprobaReservada(Token.CONSTRAINT)) {
            parseConstraint();
            return true;
        } else {
            Token token = getTokenActual();
            agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "Falta identificador de columna o constraint.");
            return false;
        }
    }

    private List<String> parseRestricciones() {
        List<String> restricciones = new ArrayList<>();
        while (true) {
            if (comprobaReservada(Token.PRIMARY) && comprobaReservada(Token.KEY)) {
                restricciones.add("PRIMARY KEY");
                System.out.println("Llave primaria encontrada.");
            } else if (comprobaReservada(Token.NOT) && comprobaReservada(Token.NULL)) {
                restricciones.add("NOT NULL");
                System.out.println("Restricción NOT NULL encontrada.");
            } else if (comprobaReservada(Token.UNIQUE)) {
                restricciones.add("UNIQUE");
                System.out.println("Restricción UNIQUE encontrada.");
            } else {
                break;
            }
        }
        return restricciones;
    }

    private void parseConstraint() {
        if (comproIdentificador()) {  // Nombre de la constraint
            if (comprobaReservada(Token.FOREIGN) && comprobaReservada(Token.KEY)) {
                if (comprobaSigno(Token.PARENTESIS_APERTURA) && comproIdentificador() && comprobaSigno(Token.PARENTESIS_CIERRE)) {
                    if (comprobaReservada(Token.REFERENCES) && comproIdentificador()) {
                        if (comprobaSigno(Token.PARENTESIS_APERTURA) && comproIdentificador() && comprobaSigno(Token.PARENTESIS_CIERRE)) {
                            System.out.println("Llave foránea añadida correctamente.");
                        } else {
                            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Llave foránea mal definida en REFERENCES.");
                        }
                    } else {
                        agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta REFERENCES o identificador de tabla referenciada.");
                    }
                } else {
                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Llave foránea mal definida.");
                }
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba FOREIGN KEY después de CONSTRAINT.");
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta identificador después de CONSTRAINT.");
        }
    }

    private void parseAlterTable() {
        avanzarToken();
        if (comprobaReservada(Token.TABLE)) {
            if (comproIdentificador()) {
                Token tokenTabla = getTokenActual(); // Guardar el token de la tabla
                contadorOperaciones.put("alter", contadorOperaciones.get("alter") + 1); // Incrementa el contador de operaciones ALTER
                
                if (comprobaReservada(Token.ADD)) {
                    parseAlterAdd();
                } else if (comprobaReservada(Token.DROP)) {
                    parseAlterDrop();
                } else if (comprobaReservada(Token.ALTER)) {
                    parseAlterColumnType();
                } else {
                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Instrucción ALTER TABLE no válida.");
                }
                
                // Registro exitoso del ALTER TABLE
                System.out.println("Instrucción ALTER TABLE procesada correctamente en " + 
                    tokenTabla.getLexema() + " (Línea: " + tokenTabla.getFila() + 
                    ", Columna: " + tokenTabla.getColumna() + ")");
                contadorTablasModificadas++;
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta identificador de tabla en ALTER TABLE.");
            }
        }
    }


    private void parseAlterAdd() {
        if (comprobaReservada(Token.COLUMN)) { 
            if (comproIdentificador()) { 
                String tipoDato = parseTipoDeDato(); 
                if (tipoDato != null) {
                    if (comprobaSigno(Token.PUNTO_COMA)) {
                        System.out.println("Columna añadida correctamente.");
                    } else {
                        agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta ';' al final de la instrucción.");
                    }
                } else {
                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Tipo de dato no válido.");
                }
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta el nombre de la columna en ADD COLUMN.");
            }
        } else if (comprobaReservada(Token.CONSTRAINT)) {
            if (comproIdentificador()) { 
                parseConstraintType();
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta el nombre de la constraint en ADD CONSTRAINT.");
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba 'COLUMN' o 'CONSTRAINT' después de 'ADD'.");
        }
    }

    private void parseAlterColumnType() {
        if (comprobaReservada(Token.COLUMN) && comproIdentificador()) {
            if (comprobaReservada(Token.TYPE)) {
                String tipoDato = parseTipoDeDato();
                if (tipoDato != null && comprobaSigno(Token.PUNTO_COMA)) {
                    System.out.println("Tipo de columna alterado correctamente.");
                } else {
                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta ';' al final de la instrucción.");
                }
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba 'TYPE' en ALTER COLUMN.");
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error en ALTER COLUMN: falta identificador de columna.");
        }
    }

    private void parseAlterDrop() {
        if (comprobaReservada(Token.COLUMN) && comproIdentificador()) {
            if (comprobaSigno(Token.PUNTO_COMA)) {
                System.out.println("Columna eliminada correctamente.");
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta ';' al final de la instrucción.");
            }
        } else if (comprobaReservada(Token.CONSTRAINT) && comproIdentificador()) {
            if (comprobaSigno(Token.PUNTO_COMA)) {
                System.out.println("Restricción eliminada correctamente.");
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta ';' al final de la instrucción.");
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error en DROP COLUMN o CONSTRAINT.");
        }
    }

    private void parseConstraintType() {
        if (comprobaReservada(Token.UNIQUE) && comprobaSigno(Token.PARENTESIS_APERTURA) && comproIdentificador() && comprobaSigno(Token.PARENTESIS_CIERRE)) {
            System.out.println("Restricción UNIQUE añadida correctamente.");
        } else if (comprobaReservada(Token.FOREIGN) && comprobaReservada(Token.KEY) && comprobaSigno(Token.PARENTESIS_APERTURA) && comproIdentificador() && comprobaSigno(Token.PARENTESIS_CIERRE) && comprobaReservada(Token.REFERENCES) && comproIdentificador() && comprobaSigno(Token.PARENTESIS_APERTURA) && comproIdentificador() && comprobaSigno(Token.PARENTESIS_CIERRE)) {
            System.out.println("Llave foránea añadida correctamente.");
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error en la definición de constraint.");
        }
    }

    private void parseDropTable() {
        avanzarToken();
        if (comprobaReservada(Token.TABLE)) {
            if (comprobaReservada(Token.IF) && comprobaReservada(Token.EXISTS)) {
                if (comproIdentificador()) {
                    if (comprobaReservada(Token.CASCADE) && comprobaSigno(Token.PUNTO_COMA)) {
                        System.out.println("Tabla eliminada correctamente con CASCADE.");
                    } else if (comprobaSigno(Token.PUNTO_COMA)) {
                        System.out.println("Tabla eliminada correctamente.");
                    } else {
                        agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta ';' al final de la instrucción.");
                    }
                } else {
                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta el nombre de la tabla en DROP TABLE.");
                }
            } else if (comproIdentificador() && comprobaSigno(Token.PUNTO_COMA)) {
                System.out.println("Tabla eliminada correctamente.");
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Instrucción DROP TABLE incompleta.");
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba 'TABLE' después de 'DROP'.");
        }
    }

    private void agregarErrorPuntoComa() {
        Token token = getTokenActual();
        agregarErrorSintactico(token != null ? token.getLexema() : "null", token != null ? token.getTipoToken() : "null", token != null ? token.getFila() : -1, token != null ? token.getColumna() : -1, "Se esperaba ';' al final de la declaración.");
    }

    private void agregarErrorTipoDato() {
        Token token = getTokenActual();
        agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "Tipo de dato no válido.");
    }

    private void agregarErrorNombreColumna() {
        Token token = getTokenActual();
        agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "Falta el nombre de la columna en ADD COLUMN.");
    }

    private void agregarErrorNombreConstraint() {
        Token token = getTokenActual();
        agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "Falta el nombre de la constraint en ADD CONSTRAINT.");
    }

    private void agregarErrorColumnOrConstraint() {
        Token token = getTokenActual();
        agregarErrorSintactico(token.getLexema(), token.getTipoToken(), token.getFila(), token.getColumna(), "Se esperaba 'COLUMN' o 'CONSTRAINT' después de 'ADD'.");
    }

    private void parseInsert() {
        avanzarToken(); 
        if (comprobaReservada(Token.INTO)) {
            if (comproIdentificador()) {
                if (comprobaSigno(Token.PARENTESIS_APERTURA)) {
                    parseIdentifiers(); 
                    if (comprobaSigno(Token.PARENTESIS_CIERRE) && comprobaReservada(Token.VALUES)) {
                        if (comprobaSigno(Token.PARENTESIS_APERTURA)) {
                            parseDataValues(); 
                            System.out.println("SI ES VALIDO INSERT");
                            if (!comprobaSigno(Token.PARENTESIS_CIERRE)) {
                                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Falta ')' en la declaración de INSERT.");
                                System.out.println("Error: Falta ')' en la declaración de INSERT.");
                            }
                            if (!comprobaSigno(Token.PUNTO_COMA)) {
                                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Falta ';' en la declaración de INSERT.");
                                System.out.println("Error: Falta ';' en la declaración de INSERT.");
                            }
                        } else {
                            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Falta '(' después de VALUES.");
                            System.out.println("Error: Falta '(' después de VALUES.");
                        }
                    } else {
                        agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Falta ')' o no se ha especificado VALUES en la declaración de INSERT.");
                        System.out.println("Error: Falta ')' o no se ha especificado VALUES.");
                    }
                } else {
                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Falta '(' después del nombre de la tabla.");
                    System.out.println("Error: Falta '(' después del nombre de la tabla.");
                }
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Falta el nombre de la tabla en INSERT.");
                System.out.println("Error: Falta el nombre de la tabla en INSERT.");
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Instrucción INSERT mal formada.");
            System.out.println("Error: Instrucción INSERT mal formada.");
        }
    }

    private void parseSelect() {
        avanzarToken(); // Avanza después de SELECT
        System.out.println("Procesando instrucción SELECT");

        parseSelectColumns(); // Parsear las columnas seleccionadas

        if (comprobaReservada(Token.FROM)) {
            System.out.println("Token FROM encontrado");
            parseFromClause(); // Llama a la función para manejar la cláusula FROM

            // Aquí llamamos a las cláusulas opcionales
            parseOptionalClauses();

            if (!comprobaSigno(Token.PUNTO_COMA)) {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                    getTokenActual().getFila(), getTokenActual().getColumna(),
                    "Error: Falta ';' al final de SELECT.");
            } else {
                System.out.println("Instrucción SELECT procesada correctamente");
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                getTokenActual().getFila(), getTokenActual().getColumna(),
                "Error: Falta el token FROM en la instrucción SELECT.");
        }
    }

    private void parseFromClause() {
        if (comproIdentificador()) {
            // Aquí procesas el identificador de la tabla
            while (comprobaReservada(Token.JOIN)) {
                parseJoin(); // Maneja el JOIN y sus condiciones
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                getTokenActual().getFila(), getTokenActual().getColumna(),
                "Error: Se esperaba un identificador de tabla en la cláusula FROM.");
        }
    }

    private void parseSelectColumns() {
        do {
            if (comprobaReservada(Token.MULTI)) {
                // Para el caso SELECT *
                return; // Aceptar y salir
            } else if (comprobaReservada(Token.AVG) || comprobaReservada(Token.SUM) || 
                       comprobaReservada(Token.MAX) || comprobaReservada(Token.MIN)) {
                // Reconocimiento de funciones agregadas
                Token funcionAgregada = getTokenActual();
                if (comprobaSigno(Token.PARENTESIS_APERTURA)) {
                    if (comproIdentificador()) { // Debe haber un identificador dentro del paréntesis
                        if (comprobaSigno(Token.PARENTESIS_CIERRE)) {
                            // Se permite alias para la función
                            if (comprobaSigno(Token.AS)) {
                                if (!comproIdentificador()) {
                                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                                        getTokenActual().getFila(), getTokenActual().getColumna(),
                                        "Error: Se esperaba un alias después de AS.");
                                }
                            }
                        } else {
                            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                                getTokenActual().getFila(), getTokenActual().getColumna(),
                                "Error: Falta ')' en la función agregada.");
                        }
                    } else {
                        agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                            getTokenActual().getFila(), getTokenActual().getColumna(),
                            "Error: Se esperaba un identificador dentro de la función agregada.");
                    }
                } else {
                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                        getTokenActual().getFila(), getTokenActual().getColumna(),
                        "Error: Falta '(' después de la función agregada.");
                }
            } else if (comproIdentificador()) {
                // Se permite alias para el identificador
                if (comprobaSigno(Token.AS)) {
                    comproIdentificador(); // Alias de columna
                }
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                    getTokenActual().getFila(), getTokenActual().getColumna(),
                    "Error: Se esperaba un identificador o '*' en la selección.");
            }
        } while (comprobaSigno(Token.COMA)); // Para múltiples columnas
    }

    private void parseOptionalClauses() {
        if (comprobaReservada(Token.WHERE)) parseConditions();
        if (comprobaReservada(Token.GROUP) && comprobaReservada(Token.BY)) parseIdentifiers();
        if (comprobaReservada(Token.ORDER) && comprobaReservada(Token.BY)) parseOrderBy();
        if (comprobaReservada(Token.LIMIT)) {
            if (!comprobaEntero()) {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                    getTokenActual().getFila(), getTokenActual().getColumna(),
                    "Error: Se esperaba un número después de LIMIT.");
            }
        }
    }

    private void parseOrderBy() {
        if (comproIdentificador()) {
            // Verifica si hay más columnas para ordenar
            while (comprobaSigno(Token.COMA)) {
                if (!comproIdentificador()) {
                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                        getTokenActual().getFila(), getTokenActual().getColumna(),
                        "Error: Se esperaba un identificador después de la coma.");
                }
            }
            // Opcional: manejar ASC/DESC
            if (comprobaReservada(Token.ASC) || comprobaReservada(Token.DESC)) {
                // Se permite especificar ASC o DESC
                return; // Salir después de procesar correctamente
            } else {
                // Si no hay especificación de dirección, considera por defecto ASC
                System.out.println("Ordenamiento ascendente por defecto.");
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                getTokenActual().getFila(), getTokenActual().getColumna(),
                "Error: Se esperaba un identificador después de ORDER BY.");
        }
    }

    private boolean parseAggregateFunction() {
        Token tokenActual = getTokenActual();

        // Verifica si es una función agregada reconocida
        if (tokenActual != null && 
            (tokenActual.getTipoToken().equals(Token.COUNT) ||
             tokenActual.getTipoToken().equals(Token.SUM) ||
             tokenActual.getTipoToken().equals(Token.AVG) ||
             tokenActual.getTipoToken().equals(Token.MAX) ||
             tokenActual.getTipoToken().equals(Token.MIN))) {

            avanzarToken(); // Avanza al siguiente token

            // Verifica si hay un paréntesis de apertura
            if (comprobaSigno(Token.PARENTESIS_APERTURA)) {
                // Para COUNT(*)
                if (tokenActual.getTipoToken().equals(Token.COUNT) && comprobaSigno(Token.MULTI)) {
                    if (comprobaSigno(Token.PARENTESIS_CIERRE)) {
                        System.out.println("Función agregada COUNT(*) encontrada.");
                        return true; // COUNT(*) está completo
                    } else {
                        agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                            getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba ')' después de COUNT(*).");
                    }
                } else {
                    // Verifica que haya un identificador dentro del paréntesis
                    if (comproIdentificador() && comprobaSigno(Token.PARENTESIS_CIERRE)) {
                        System.out.println("Función agregada " + tokenActual.getLexema() + " encontrada.");
                        return true; // Función agregada está completa
                    } else {
                        agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                            getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba un identificador o ')' después de la función agregada.");
                    }
                }
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                    getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba '(' después de la función agregada.");
            }
        }

        return false; // No se detectó una función agregada válida
    }

    private void parseUpdate() {
        avanzarToken(); // Avanza después de UPDATE
        if (comproIdentificador()) { // Verifica si el nombre de la tabla es válido
            Token tokenTabla = getTokenActual(); // Guardar el token de la tabla
            contadorOperaciones.put("update", contadorOperaciones.get("update") + 1); // Incrementa el contador de operaciones UPDATE

            if (comprobaReservada(Token.SET)) {
                // Analiza las asignaciones
                parseUpdateAssignments();

                // Opcional: manejar la cláusula WHERE
                if (comprobaReservada(Token.WHERE)) {
                    parseConditions();
                }

                // Verifica si falta el punto y coma al final
                if (!comprobaSigno(Token.PUNTO_COMA)) {
                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                        getTokenActual().getFila(), getTokenActual().getColumna(),
                        "Error: Se esperaba ';' al final de la instrucción UPDATE.");
                } else {
                    // Registro exitoso del UPDATE
                    System.out.println("Instrucción UPDATE procesada correctamente en " + 
                        tokenTabla.getLexema() + " (Línea: " + tokenTabla.getFila() + 
                        ", Columna: " + tokenTabla.getColumna() + ")");
                }
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                    getTokenActual().getFila(), getTokenActual().getColumna(),
                    "Error: Se esperaba 'SET' después de 'UPDATE'.");
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                getTokenActual().getFila(), getTokenActual().getColumna(),
                "Error: Se esperaba un identificador para el nombre de la tabla.");
        }
    }


    private void parseUpdateAssignments() {
        do {
            if (comproIdentificador()) { // Identificador de la columna
                if (comprobaSigno(Token.IGUAL)) { // Verifica el signo igual
                    if (comprobaEntero() || parseString() || parseDate() || comprobaReservada(Token.DECIMAL_LITERAL)) {
                        // Si hay operaciones aritméticas
                        while (comprobaSigno(Token.SUMA) || comprobaSigno(Token.RESTA) || 
                               comprobaSigno(Token.MULTI) || comprobaSigno(Token.DIVISION)) {
                            if (!comprobaEntero() && !comprobaReservada(Token.DECIMAL_LITERAL) && !parseString()) {
                                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                                    getTokenActual().getFila(), getTokenActual().getColumna(),
                                    "Error: Se esperaba un valor numérico o cadena después de una operación.");
                            }
                        }
                    } else {
                        agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                            getTokenActual().getFila(), getTokenActual().getColumna(),
                            "Error en la asignación de valores en UPDATE.");
                    }
                } else {
                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                        getTokenActual().getFila(), getTokenActual().getColumna(),
                        "Error: Se esperaba '=' después del identificador.");
                }
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                    getTokenActual().getFila(), getTokenActual().getColumna(),
                    "Error: Se esperaba un identificador para la asignación.");
            }
        } while (comprobaSigno(Token.COMA)); // Permite múltiples asignaciones
    }


    private void parseDelete() {
        avanzarToken(); // Avanza después de DELETE

        // Verifica si se encuentra la palabra reservada FROM
        if (comprobaReservada(Token.FROM)) {
            if (comproIdentificador()) {
                // Comprueba si hay una cláusula WHERE
                if (comprobaReservada(Token.WHERE)) {
                    parseConditions(); // Llama a la función para analizar las condiciones
                    System.out.println("WHERE JALA BIEN ");
                }

                // Verifica el punto y coma al final de la instrucción
                if (!comprobaSigno(Token.PUNTO_COMA)) {
                    agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta ';' al final de DELETE.");
                    System.out.println("Error: Falta ';' al final de DELETE.");
                } else {
                    System.out.println("ES VALIDO DELETE");
                }
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Falta el nombre de la tabla en DELETE.");
                System.out.println("Error: Falta el nombre de la tabla en DELETE.");
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba 'FROM' después de 'DELETE'.");
            System.out.println("Error: Se esperaba 'FROM' después de 'DELETE'.");
        }
    }

    private void parseIdentifiers() {
        do {
            if (comproIdentificador()) {
                System.out.println("Se es identificador");
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: No jala en parseIdentifiers");
            }
        } while (comprobaSigno(Token.COMA)); // Parsear múltiples identificadores
    }

    private void parseDataValues() {
        do {
            if (comprobaEntero() || parseString() || comprobaReservada(Token.DECIMAL_LITERAL) || parseDate()) {
                System.out.println("Valor reconocido en INSERT.");
            } else if (comprobaReservada(Token.IDENTIFICADOR)) {
                // Permitir identificadores en lugar de solo valores
                System.out.println("Valor reconocido en INSERT: " + getTokenActual().getLexema());
            } else {
                // Se ha encontrado un valor no reconocido
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Valor no reconocido en INSERT.");
                System.out.println("Valor no reconocido en INSERT: " + getTokenActual().getLexema());
            }
        } while (comprobaSigno(Token.COMA)); // Parsear múltiples valores
    }

    private boolean parseValue() {
        return comprobaEntero() || comprobaReservada(Token.STRING_LITERAL) || comprobaReservada(Token.DECIMAL_LITERAL);
    }

    private void parseConditions() {
        // Verificar si hay un paréntesis de apertura
        if (comprobaSigno(Token.PARENTESIS_APERTURA)) {
            // Procesar condiciones dentro de paréntesis
            parseConditions(); // Llamar recursivamente para manejar condiciones complejas

            // Verificar que hay un paréntesis de cierre
            if (!comprobaSigno(Token.PARENTESIS_CIERRE)) {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                    getTokenActual().getFila(), getTokenActual().getColumna(),
                    "Error: Falta ')' en la condición.");
            }
        } else {
            // Si no hay paréntesis, procesar una condición normal
            if (comproIdentificador()) {
                Token operador = getTokenActual(); // Captura el operador para mostrar errores si es necesario

                // Verificar que hay un operador válido
                if (comprobaSigno(Token.IGUAL) || comprobaSigno(Token.MAYOR) || 
                    comprobaSigno(Token.MENOR) || comprobaSigno(Token.IGUALMAYOR) || 
                    comprobaSigno(Token.IGUALMENOR)) {

                    // Después de un operador, se espera un valor
                    if (comprobaEntero() || parseString()) {
                        System.out.println("Condición válida.");
                    } else {
                        agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                            getTokenActual().getFila(), getTokenActual().getColumna(),
                            "Error: Se esperaba un valor después del operador.");
                    }
                } else {
                    agregarErrorSintactico(operador.getLexema(), operador.getTipoToken(),
                        operador.getFila(), operador.getColumna(), "Error: Operador no reconocido en la condición.");
                }
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                    getTokenActual().getFila(), getTokenActual().getColumna(),
                    "Error: Se esperaba un identificador en la condición.");
            }
        }

        // Manejo de operadores lógicos como AND/OR
        while (comprobaReservada(Token.AND) || comprobaReservada(Token.OR)) {
            // Verificar la siguiente condición
            parseConditions();
        }
    }

    private void parseJoin() {
        if (comprobaReservada(Token.JOIN) && comproIdentificador()) {
            if (comprobaReservada(Token.ON)) {
                parseConditions(); // Manejar condiciones para el JOIN
            } else {
                agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba ON después de JOIN.");
            }
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(), getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba un JOIN seguido de un identificador de tabla.");
        }
    }

    private boolean comprobaReservada(String reservada) {
        Token token = getTokenActual();
        if (token != null && token.getTipoToken().equals(reservada)) {
            avanzarToken();
            return true;
        }
        return false;
    }

    private boolean comproIdentificador() {
        Token token = getTokenActual();
        if (token != null && token.getTipoToken().equals(Token.IDENTIFICADOR)) {
            System.out.println("Identificador encontrado: " + token.getLexema());
            avanzarToken();
            return true;
        } else if (token != null) {
            System.out.println("Token inesperado en lugar de IDENTIFICADOR: " + token.getTipoToken() + " - " + token.getLexema());
        }
        return false;
    }

    private boolean parseString() {
        if (comprobaReservada(Token.STRING_LITERAL)) {
            return true;
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba una cadena.");
            return false;
        }
    }

    private boolean parseDate() {
        if (comprobaReservada(Token.DATE_FORMAT)) {
            return true;
        } else {
            agregarErrorSintactico(getTokenActual().getLexema(), getTokenActual().getTipoToken(),
                getTokenActual().getFila(), getTokenActual().getColumna(), "Error: Se esperaba una fecha.");
            return false;
        }
    }

    private boolean comprobaSigno(String signo) {
        Token token = getTokenActual();
        if (token != null && token.getTipoToken().equals(signo)) {
            avanzarToken();
            return true;
        }
        return false;
    }

    private boolean comprobaEntero() {
        Token token = getTokenActual();
        if (token != null && (token.getTipoToken().equals(Token.ENTERO) || token.getTipoToken().equals(Token.DECIMAL_LITERAL))) {
            avanzarToken();
            return true;
        }
        return false;
    }

    private Token getTokenActual() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    private void avanzarToken() {
        if (pos < tokens.size()) {
            System.out.println("Avanzando al siguiente token: " + tokens.get(pos).getLexema());
        }
        pos++;
    }

    public void mostrarErrores() {
        for (ReporteSintactico error : erroresSintacticos) {
            System.out.println("Error: " + error.getDescripcion() + " en " + error.getLexema() + " (Fila: " + error.getLinea() + ", Columna: " + error.getColumna() + ")");
        }
    }
    
    

    public DefaultTableModel crearModeloReportes() {
        // Crear columnas para el modelo de la tabla
        String[] columnas = {"Tipo de Reporte", "Detalles", "Línea", "Columna"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // Agregar tablas encontradas al modelo
        for (String reporte : reportesTablasEncontradas) {
            String[] partes = reporte.split("\\s+"); // Usar expresión regular para dividir por espacios

            // Verificar si tenemos suficientes partes y el formato esperado
            if (partes.length >= 5 && partes[0].equals("Tabla:")) {
                String nombre = partes[1]; // nombre
                String linea = partes[3].substring(6); // obtener línea (debería empezar con "Línea: ")
                String columna = partes[4].substring(8); // obtener columna (debería empezar con "Columna: ")

                // Verificar que las subcadenas no sean vacías o fuera de rango
                if (!linea.isEmpty() && !columna.isEmpty()) {
                    modelo.addRow(new Object[]{
                        "Tabla Encontrada",
                        nombre,
                        linea,
                        columna
                    });
                } else {
                    System.out.println("Formato inesperado en reporte de tabla encontrada: " + reporte);
                }
            } else {
                System.out.println("Formato inesperado en reporte de tabla encontrada: " + reporte);
            }
        }

        // Agregar tablas modificadas al modelo
        for (String reporte : reportesTablasModificadas) {
            String[] partes = reporte.split("\\s+"); // Usar expresión regular para dividir por espacios

            // Verificar si tenemos suficientes partes y el formato esperado
            if (partes.length >= 5 && partes[0].equals("Tabla:")) {
                String nombre = partes[1]; // nombre
                String linea = partes[3].substring(6); // obtener línea
                String columna = partes[4].substring(8); // obtener columna

                // Verificar que las subcadenas no sean vacías o fuera de rango
                if (!linea.isEmpty() && !columna.isEmpty()) {
                    modelo.addRow(new Object[]{
                        "Tabla Modificada",
                        nombre,
                        linea,
                        columna
                    });
                } else {
                    System.out.println("Formato inesperado en reporte de tabla modificada: " + reporte);
                }
            } else {
                System.out.println("Formato inesperado en reporte de tabla modificada: " + reporte);
            }
        }

        return modelo;
    }




    public void mostrarReportes() {
        // Crear el JTable con el modelo generado
        DefaultTableModel modelo = crearModeloReportes();
        JTable tablaReportes = new JTable(modelo);

        // Crear un JScrollPane para hacer scroll en la tabla
        JScrollPane scrollPane = new JScrollPane(tablaReportes);
        
        // Crear un JFrame para mostrar la tabla
        JFrame frame = new JFrame("Reportes de Tablas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(scrollPane);
        frame.setSize(600, 400); // Ajusta el tamaño según sea necesario
        frame.setVisible(true);
    }
    
    public void mostrarContadores() {
        System.out.println("Contador de Tablas Encontradas: " + contadorTablasEncontradas);
        System.out.println("Contador de Tablas Modificadas: " + contadorTablasModificadas);
    }
    
    public void tablasEncontradas(){
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Contador de Tablas Encontradas: ").append(contadorTablasEncontradas).append("\n");
        mensaje.append("Contador de Tablas Modificadas: ").append(contadorTablasModificadas).append("\n");
        mensaje.append("Tablas Encontradas:\n");

        // Agregar las tablas encontradas al mensaje
        for (String reporte : reportesTablasEncontradas) {
            mensaje.append(reporte).append("\n");
        }

        // Agregar las tablas modificadas al mensaje
        mensaje.append("Tablas Modificadas:\n");
        for (String reporte : reportesTablasModificadas) {
            mensaje.append(reporte).append("\n");
        }

        // Mostrar el mensaje en un cuadro de diálogo
        JOptionPane.showMessageDialog(null, mensaje.toString(), "Reportes de Tablas", JOptionPane.INFORMATION_MESSAGE);
    }

}
