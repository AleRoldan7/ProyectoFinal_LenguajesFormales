/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

/**
 *
 * @author crisa
 */
public class TokenError extends Token {
    
    private String descripcion;

    public TokenError(String Lexema, String tipoToken, int fila, int columna, int posicionInicial, int tamaLexema) {
        super(Lexema, tipoToken, fila, columna, posicionInicial, tamaLexema);
        this.descripcion = "Error Encontrado";
    }
    
    
    public String getDescripcion() {
        return descripcion;
    }
    
    
    
    
}