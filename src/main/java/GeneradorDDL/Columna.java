/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GeneradorDDL;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author crisa
 */
public class Columna {
    
    private String nombre;
    private String tipoDato;
    private boolean esLlave; // Indica si es una llave primaria
    private boolean esUnica; // Indica si tiene restricción UNIQUE
    private boolean esNotNull; // Indica si tiene restricción NOT NULL
    private String foreignKeyColumn; // Columna de la llave foránea
    private String referencedTable; // Tabla referenciada
    private String referencedColumn; // Columna referenciada
    private List<String> restricciones; // Lista de restricciones adicionales

    // Constructor principal para definir nombre y tipo de dato
    public Columna(String nombre, String tipoDato) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.restricciones = new ArrayList<>();
    }

    // Métodos para agregar restricciones
    public void agregarRestriccion(String restriccion) {
        restricciones.add(restriccion);
        if (restriccion.equalsIgnoreCase("PRIMARY KEY")) {
            this.esLlave = true;
        } else if (restriccion.equalsIgnoreCase("UNIQUE")) {
            this.esUnica = true;
        } else if (restriccion.equalsIgnoreCase("NOT NULL")) {
            this.esNotNull = true;
        }
    }

    // Getters para restricciones y propiedades
    public String getNombre() {
        return nombre;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public boolean isLlave() {
        return esLlave;
    }

    public boolean isUnica() {
        return esUnica;
    }

    public boolean isNotNull() {
        return esNotNull;
    }

    public String getForeignKeyColumn() {
        return foreignKeyColumn;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public String getReferencedColumn() {
        return referencedColumn;
    }

    public List<String> getRestricciones() {
        return restricciones;
    }

    // Setters para propiedades adicionales
    public void setUnique(boolean esUnica) {
        this.esUnica = esUnica;
    }

    public void setNotNull(boolean esNotNull) {
        this.esNotNull = esNotNull;
    }

    public void setForeignKeyColumn(String foreignKeyColumn) {
        this.foreignKeyColumn = foreignKeyColumn;
    }

    public void setReferencedTable(String referencedTable) {
        this.referencedTable = referencedTable;
    }

    public void setReferencedColumn(String referencedColumn) {
        this.referencedColumn = referencedColumn;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public void setEsLlave(boolean esLlave) {
        this.esLlave = esLlave;
    }

    public void setEsUnica(boolean esUnica) {
        this.esUnica = esUnica;
    }

    public void setEsNotNull(boolean esNotNull) {
        this.esNotNull = esNotNull;
    }

    public void setRestricciones(List<String> restricciones) {
        this.restricciones = restricciones;
    }

    
    // Método para mostrar la información completa de la columna
    @Override
    public String toString() {
        return "Columna{"
                + "nombre='" + nombre + '\''
                + ", tipoDato='" + tipoDato + '\''
                + ", esLlave=" + esLlave
                + ", esUnica=" + esUnica
                + ", esNotNull=" + esNotNull
                + ", foreignKeyColumn='" + foreignKeyColumn + '\''
                + ", referencedTable='" + referencedTable + '\''
                + ", referencedColumn='" + referencedColumn + '\''
                + ", restricciones=" + restricciones
                + '}';
    }
    
}
