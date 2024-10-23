/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorSintactico;


import AnalizadorLexico.Token;
import AnalizadorLexico.TokenCreate;
import AnalizadorLexico.TokenDato;
import AnalizadorLexico.TokenEntero;
import AnalizadorLexico.TokenIdenti;
import AnalizadorLexico.TokenSigno;
import java.util.List;

/**
 *
 * @author crisa
 */
public class AnalizadorSintactico {
    
    private List<TokenCreate> tokenCreate;
    private List<TokenIdenti> tokenIdenti;
    private List<TokenSigno> tokenSigno;
    private List<TokenDato> tokenDato;
    private List<TokenEntero> tokenEntero;
    private List<Token> token;
    private int posiCreate = 0;
    private int posiIdenti = 0;
    private int posiSigno = 0;
    private int posiDato = 0;
    private int posiEntero = 0;
    private int postoken = 0;
    
    public AnalizadorSintactico(List<Token> token) {
        this.token = token;
    }

    private Token getToken() {
        if (postoken < token.size()) {
            return token.get(postoken);
        }
        return null;
    }
    private TokenCreate getTokenCreateActual() {
        if (posiCreate < tokenCreate.size()) {
            return tokenCreate.get(posiCreate);
        }
        return null; 
    }

    private TokenIdenti getTokenIdentiActual() {
        if (posiIdenti < tokenIdenti.size()) {
            return tokenIdenti.get(posiIdenti);
        }
        return null; 
    }

    private TokenSigno getTokenSignoActual() {
        if (posiSigno < tokenSigno.size()) {
            return tokenSigno.get(posiSigno);
        }
        return null; 
    }
    

    private TokenDato getTokenDatoActual() {
        if (posiDato < tokenDato.size()) {
            return tokenDato.get(posiDato);
        }
        return null; 
    }
    
    private TokenEntero getTokenEnteroActual() {
        if (posiEntero < tokenEntero.size()) {
            return tokenEntero.get(posiEntero);
        }
        return null;
    }

    private void avanzaTokenCreate() {
        posiCreate++;
    }

    private void avanzaTokenIdenti() {
        posiIdenti++;
    }

    private void avanzaTokenSigno() {
        posiSigno++;
    }

    private void avanzaTokenDato() {
        posiDato++;
    }
    
    private void avanzaTokenEntero() {
        posiEntero++;
    }
    
    private void tokenavanza() {
        postoken++;
    }

    public void analizarInstrucciones() {
        while (postoken < token.size()) {
            if (comprobaReservada("CREATE")) {
                if (comprobaReservada("DATABASE")) {
                    parseCreacionBase();
                } else if (comprobaReservada("TABLE")) {
                    parseCreacionTabla();
                } else {
                    System.out.println("Error: Se esperaba 'DATABASE' o 'TABLE' después de 'CREATE'.");
                }
            } else {
                tokenavanza(); 
            }
        }
    }

    private void parseCreacionBase() {
        if (comproIdentificador()) {
            if (comprobaSigno(";")) {
                System.out.println("Creación de base de datos válida.");
            } else {
                System.out.println("Error: Se esperaba cerrar con signo ';'.");
            }
        } else {
            System.out.println("Error: Se esperaba un nombre de base de datos.");
        }
    }

    private void parseCreacionTabla() {
        if (comproIdentificador()) {
            if (comprobaSigno("(")) {
                parseEstrucDecla();
                if (comprobaSigno(")")) {
                    if (comprobaSigno(";")) {
                        System.out.println("Creación de TABLA ESOSOSOSOSOS.");
                    }else{
                        System.out.println("falta punto coma");
                    }           
                }else{
                    System.out.println("falta cerra paren");
                }
            }else{
                System.out.println("Falta abrir");
            }
        }else{
            System.out.println("Falta nombre");
        }
    }

    private void parseEstrucDecla() {
         if (comproIdentificador()) { 
            parseDato();        
            parseRestricciones();
                     
        } else {
            System.out.println("Error: Falta identificador o está mal formado.");
        }
        parseEstruLlaves();  
    }




    private void parseRestricciones() {
     
        do {            
            if (comprobaReservada("PRIMARY") && comprobaReservada("KEY")) {
        
                System.out.println("Llave primaria encontrada.");
            }


            if (comprobaReservada("NOT") && comprobaReservada("NULL")) {

                System.out.println("Restricción NOT NULL encontrada.");
            }


            if (comprobaReservada("UNIQUE")) {

                System.out.println("Restricción UNIQUE encontrada.");
            }
        } while (comprobaSigno(","));
        

        
        
    }


    
    
    private void parseDato() {
        if (comprobaDato("SERIAL")) {
            System.out.println("HAGARRA SERIAL");
        } else if (comprobaDato("INTEGER")) {
            System.out.println("INTEGER SSSSS");
        } else if (comprobaDato("BIGINT")) {
            System.out.println("BIGINT DANDO");
        } else if (comprobaDato("VARCHAR")) {
            if (comprobaSigno("(")) {
                if (comprobaEntero()) {
                    if (comprobaSigno(")")) {
                        System.out.println("VARCHAR CORRECTO");
                    }else{
                        System.out.println("no jala pantensisi )");
                    }
                }
               
            } else {
                System.out.println("VARCHAR SIN NUMERO");
            }
        } else if (comprobaDato("DECIMAL")) {
            if (comprobaSigno("(") && comprobaEntero() && comprobaSigno(",") && comprobaEntero() && comprobaSigno(")")) {
                System.out.println("DECIMAL DANDO.");
            } else {
                System.out.println("ERROR DECIMAL");
            }
        } else if (comprobaDato("DATE")) {
            System.out.println("DATE JAANDO");
        } else if (comprobaDato("TEXT")) {
            System.out.println("TEXT JALA");
        } else if (comprobaDato("BOOLEAN")) {
            System.out.println("BOOLENA ESOO");
        } else {
            System.out.println("ERROR NO JALA LOS TOKENS DATO");
        }
    }


    
    /*
    private void parseDato() {
        if (comprobaDato("SERIAL")) {
            System.out.println("Agarro serial");
        }else{
            System.out.println("no tiene datos");
        }
      
    }
    */
    private void parseEstruLlaves() {
        if (comprobaReservada("CONSTRAINT") && comproIdentificador()) {
            if (comprobaReservada("FOREIGN") && comprobaReservada("KEY") && comprobaSigno("(")
                && comproIdentificador() && comprobaSigno(")")) {
                if (comprobaReservada("REFERENCES") && comproIdentificador() && comprobaSigno("(") 
                    && comproIdentificador() && comprobaSigno(")")) {
                    System.out.println("Llave foránea válida.");
                } else {
                    System.out.println("Error en la estructura de REFERENCES.");
                }
            } else {
                System.out.println("Error en la estructura de FOREIGN KEY.");
            }
        } else {
            System.out.println("No se encontró una llave foránea.");
        }
    }

    
   
    private boolean comproIdentificador() {
        Token tokenActual = getToken();
        if (tokenActual != null && tokenActual.getTipoToken().equals("IDENTIFICADOR")) {
            System.out.println("Token actual (identificador): " + tokenActual.getLexema());
            tokenavanza();
            return true;
        }
        return false;
    }

    private boolean comprobaSigno(String signoEsperado) {
         Token tokenActual = getToken();
        if (tokenActual != null && tokenActual.getTipoToken().equals(signoEsperado)) {
            System.out.println("Token actual (signo): " + tokenActual.getLexema());
            tokenavanza();
            return true;
        }
        return false;
    }
    

    private boolean comprobaDato(String datoEsperado) {
         Token tokenActual = getToken();
        if (tokenActual != null && tokenActual.getTipoToken().equals(datoEsperado)) {
            System.out.println("Token actual (dato): " + tokenActual.getLexema());
           tokenavanza();
            return true;
        }
        return false;
    }

    private boolean comprobaReservada(String reservada) {
         Token tokenActual = getToken();
        if (tokenActual != null && tokenActual.getTipoToken().equals(reservada)) {
            System.out.println("Token actual (reservada): " + tokenActual.getLexema());
            tokenavanza();
            return true;
        }
        return false;
    }

    private boolean comprobaEntero() {
         Token tokenActual = getToken();
        if (tokenActual != null && tokenActual.getTipoToken().equals("Entero")) {
            System.out.println("Token actual (entero): " + tokenActual.getLexema());
            avanzaTokenEntero();
            return true;
        }
        return false;
    }
    
    

}
