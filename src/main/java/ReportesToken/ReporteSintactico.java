/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportesToken;

/**
 *
 * @author crisa
 */
public class ReporteSintactico {
    private String lexema;
    private String tipoToken;
    private int linea;
    private int columna;
    private String descripcion;

    public ReporteSintactico(String lexema, String tipoToken, int linea, int columna, String descripcion) {
        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.linea = linea;
        this.columna = columna;
        this.descripcion = descripcion;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return "Error: " + descripcion + " en '" + lexema + "' (Línea: " + linea + ", Columna: " + columna + ")";
    }
}
