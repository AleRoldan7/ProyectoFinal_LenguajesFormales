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
    
    String nombre;
    String tipo;
    List<String> restricciones;

    public Columna(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.restricciones = new ArrayList<>();
    }

    public void agregarRestriccion(String restriccion) {
        this.restricciones.add(restriccion);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<String> getRestricciones() {
        return restricciones;
    }

    public void setRestricciones(List<String> restricciones) {
        this.restricciones = restricciones;
    }
    
    
}
