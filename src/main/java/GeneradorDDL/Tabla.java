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
    private String nombre;
    private List<Columna> columnas; // Lista de columnas en la tabla

    // Constructor que recibe el nombre de la tabla
    public Tabla(String nombre) {
        this.nombre = nombre;
        this.columnas = new ArrayList<>();
    }

    // Método para agregar una columna a la tabla
    public void agregarColumna(Columna columna) {
        // Verificar si la columna ya existe en la tabla
        if (columnaExiste(columna.getNombre())) {
            System.out.println("Advertencia: La columna " + columna.getNombre() + " ya existe en la tabla " + nombre + ".");
        } else {
            columnas.add(columna);
        }
    }

    // Verifica si una columna con el mismo nombre ya existe en la tabla
    private boolean columnaExiste(String nombreColumna) {
        return columnas.stream().anyMatch(columna -> columna.getNombre().equals(nombreColumna));
    }

    // Obtiene el nombre de la tabla
    public String getNombre() {
        return nombre;
    }

    // Obtiene la lista de columnas de la tabla
    public List<Columna> getColumnas() {
        return columnas;
    }

    // Método para mostrar la información de la tabla y sus columnas (para depuración)
    @Override
    public String toString() {
        StringBuilder infoTabla = new StringBuilder("Tabla: " + nombre + "\n");
        for (Columna columna : columnas) {
            infoTabla.append("  - ").append(columna.toString()).append("\n");
        }
        return infoTabla.toString();
    }
}
