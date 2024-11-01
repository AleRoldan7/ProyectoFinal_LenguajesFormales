package GeneradorDDL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GraficaTabla {
    

    private final String outputPath = "C:\\Users\\crisa\\Documents\\NetBeansProjects\\ProyectoFinalLenguajesFormales\\";

    public void generarDOT(String nombreTabla, List<Columna> columnas) {
        System.out.println("Generando archivo DOT para la tabla: " + nombreTabla);

        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("    node [shape=record];\n");
        dot.append("    ").append(nombreTabla).append(" [label=\"{").append(nombreTabla).append("|");

        // Recorrer todas las columnas de la tabla
        for (Columna columna : columnas) {
            System.out.println("COLUMNAS"+columna);
            dot.append(columna.getNombre()).append(": ").append(columna.getTipoDato());

            // Agregar restricciones de cada columna si existen
            if (!columna.getRestricciones().isEmpty()) {
                dot.append(" (");
                for (String restriccion : columna.getRestricciones()) {
                    dot.append(restriccion).append(", ");
                }
                dot.setLength(dot.length() - 2); // Eliminar la última coma y espacio
                dot.append(")");
            }
            dot.append("|");  // Separador de columnas
        }

        // Eliminar el último separador "|"
        dot.setLength(dot.length() - 1);
        dot.append("}\"];\n");
        dot.append("}\n");

        // Guardar el archivo .dot en la ruta deseada
        String outputPath = "C:/Users/crisa/Documents/NetBeansProjects/ProyectoFinalLenguajesFormales/";
        try {
            Files.write(Paths.get(outputPath + "tabla_" + nombreTabla + ".dot"), dot.toString().getBytes());
            System.out.println("Archivo DOT generado en: " + outputPath + "tabla_" + nombreTabla + ".dot");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void generarJPG(String nombreTabla) {
        try {
            String dotPath = "C:\\Program Files\\Graphviz\\bin\\dot.exe"; // Ruta de Graphviz
            Process process = Runtime.getRuntime().exec(dotPath + " -Tjpg " + outputPath + "tabla_" + nombreTabla + ".dot -o " + outputPath + "tabla_" + nombreTabla + ".jpg");
            process.waitFor();  
            System.out.println("Archivo JPG generado: " + outputPath + "tabla_" + nombreTabla + ".jpg");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
