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
public class Tabla {
    
    String nombre;        
    List<Columna> columnas;  


    public Tabla(String nombre) {
        this.nombre = nombre;
        this.columnas = new ArrayList<>();
    }

 
    public void agregarColumna(Columna columna) {
        columnas.add(columna);
    }

  
    public String getNombre() {
        return nombre;
    }

   
    public List<Columna> getColumnas() {
        return columnas;
    }
}
