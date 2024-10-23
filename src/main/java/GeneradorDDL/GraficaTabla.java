/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GeneradorDDL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author crisa
 */
public class GraficaTabla {
    
     public void generarDOT(String nombreTabla, List<Columna> columnas) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("    node [shape=record];\n");
        dot.append("    ").append(nombreTabla).append(" [label=\"{").append(nombreTabla).append("|");

        
        for (Columna columna : columnas) {
            dot.append(columna.nombre).append(": ").append(columna.tipo);
            for (String restriccion : columna.restricciones) {
                dot.append(" [").append(restriccion).append("]");
            }
            dot.append("|");
        }

        dot.setLength(dot.length() - 1); // Eliminar la última barra vertical
        dot.append("}\"];\n");
        dot.append("}\n");

        
        try {
            Files.write(Paths.get("tabla_" + nombreTabla + ".dot"), dot.toString().getBytes());
            System.out.println("Archivo DOT generado: tabla_" + nombreTabla + ".dot");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 
    public void generarJPG(String nombreTabla) {
        try {
           
            String dotPath = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
            Process process = Runtime.getRuntime().exec(dotPath + " -Tjpg tabla_" + nombreTabla + ".dot -o tabla_" + nombreTabla + ".jpg");
            process.waitFor();  
            System.out.println("Archivo JPG generado: tabla_" + nombreTabla + ".jpg");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
