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
public class TablaCreada {
    
    private String nombre;
    private List<String> columnas;

    public TablaCreada(String nombre) {
        this.nombre = nombre;
        this.columnas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getColumnas() {
        return columnas;
    }

    public void agregarColumna(String columna) {
        columnas.add(columna);
    }
}
