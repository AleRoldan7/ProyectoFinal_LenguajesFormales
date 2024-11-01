/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GeneradorDDL;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author crisa
 */
public class Grafica {
    
    private List<Tabla> tablas;
   

    public Grafica(List<Tabla> tablas) {
        this.tablas = tablas;
     
    }

    public void graficarTodo() {
        try {
            FileWriter fileWriter = new FileWriter("todo.dot");
            fileWriter.write("digraph G {\n");
            fileWriter.write("rankdir=TB;\n");
            fileWriter.write("node [shape=record];\n");

            // Graficar cada tabla
            for (Tabla tabla : tablas) {
                String nombreTabla = tabla.getNombre();
                fileWriter.write(nombreTabla + " [label=\"{" + nombreTabla + "|");

                // Añadir las columnas al nodo de la tabla
                for (Columna columna : tabla.getColumnas()) {
                    String llaves = columna.isLlave() ? " (Llave)" : "";
                    // Agregar el tipo de dato directamente
                    fileWriter.write(columna.getNombre() + " : " + columna.getTipoDato() + llaves + "\\l");
                }

                fileWriter.write("}\"];\n");
            }


            fileWriter.write("}\n");
            fileWriter.close();

            System.out.println("Archivo .dot de tablas y modificadores creado correctamente.");
            generarImagen("todo.dot", "todo.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para generar imagen a partir del archivo .dot
    private void generarImagen(String dotFile, String pngFile) throws IOException {
        Process proceso = Runtime.getRuntime().exec("dot -Tpng " + dotFile + " -o " + pngFile);
        try {
            int exitCode = proceso.waitFor();
            if (exitCode == 0) {
                System.out.println("Imagen del diagrama generada como " + pngFile);
            } else {
                System.out.println("Error al generar la imagen del diagrama. Código de salida: " + exitCode);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Mostrar la información de las tablas y modificadores en una ventana
    public void mostrarInformacion() {
        graficarTodo();
        mostrarImagen();
    }

    // Método para mostrar el diagrama generado
    private void mostrarImagen() {
        File archivoImagen = new File("todo.png");

        if (archivoImagen.exists()) {
            try {
                BufferedImage imagen = ImageIO.read(archivoImagen);
                ImageIcon iconoImagen = new ImageIcon(imagen);

                JFrame ventana = new JFrame("Diagrama de Tablas y Modificadores");
                ventana.setSize(800, 600);
                ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JLabel etiquetaImagen = new JLabel(iconoImagen);
                ventana.add(etiquetaImagen);
                ventana.setVisible(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar la imagen generada.");
        }
    }
}
