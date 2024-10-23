/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

/**
 *
 * @author crisa
 */
public class Token {
    
    private String Lexema;
    private String tipoToken;
    private int fila;
    private int columna;
    private int posicionInicial;
    private int tamaLexema;

    public Token(String Lexema, String tipoToken, int fila, int columna, int posicionInicial, int tamaLexema) {
        this.Lexema = Lexema;
        this.tipoToken = tipoToken;
        this.fila = fila;
        this.columna = columna;
        this.posicionInicial = posicionInicial;
        this.tamaLexema = tamaLexema;
    }

   

    public String getLexema() {
        return Lexema;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getPosicionInicial() {
        return posicionInicial;
    }

    public int getTamaLexema() {
        return tamaLexema;
    }

    public void setLexema(String Lexema) {
        this.Lexema = Lexema;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public void setPosicionInicial(int posicionInicial) {
        this.posicionInicial = posicionInicial;
    }

    public void setTamaLexema(int tamaLexema) {
        this.tamaLexema = tamaLexema;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }
    
    
    
    
    @Override
    public String toString() {
        return " Token [ lexema = " + Lexema + ", tipo " + tipoToken + " , fila = " + fila + " , columna = " + columna + 
                " tamaño = " + tamaLexema + "]";
        
    }

}
