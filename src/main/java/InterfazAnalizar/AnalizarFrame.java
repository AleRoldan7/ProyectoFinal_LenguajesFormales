/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package InterfazAnalizar;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.ColorTexto;
import AnalizadorLexico.Token;
import AnalizadorLexico.TokenCreate;
import AnalizadorLexico.TokenDato;
import AnalizadorLexico.TokenEntero;
import AnalizadorLexico.TokenError;
import AnalizadorLexico.TokenIdenti;
import AnalizadorLexico.TokenSigno;
import AnalizadorLexico.colorLexico;
import AnalizadorSintactico.AnalizadorSintactico;
import GeneradorDDL.GraficaTabla;
import GeneradorDDL.Tabla;
import GeneradorDDL.TablaCreada;
import ReportesToken.ReporteErroresFrame;
import ReportesToken.ReporteSintactico;
import ReportesToken.SintacticoFrameError;
import ReportesToken.TokenReportes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.text.Utilities;

/**
 *
 * @author crisa
 */
public class AnalizarFrame extends javax.swing.JFrame {
    
    private AnalizadorLexico analizadorLexico;
    private AnalizadorSintactico analizadorSintactico;
    private JTextArea lineNumberArea; 
    private Timer colorTimer;
    private static final int COLOR_DELAY = 300;
  
    /**
     * Creates new form AnalizarFrame
     */
    public AnalizarFrame() {
        initComponents();
        initLineNumbers();
        
       
      
        
        jTextPane1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                initLineNumbers();
                colorTimer.restart();  
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                initLineNumbers();
                colorTimer.restart();  
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                initLineNumbers();
                colorTimer.restart();  
            }
        });

        colorTimer = new Timer(COLOR_DELAY, e -> {
            pintarUltimaPalabra();
            colorTimer.stop(); 
        });
        colorTimer.setRepeats(false); 



       
        jTextPane1.addCaretListener(e -> {
            int pos = jTextPane1.getCaretPosition();
            try {
                int line = jTextPane1.getDocument().getDefaultRootElement().getElementIndex(pos) + 1;
                int column = pos - jTextPane1.getDocument().getDefaultRootElement().getElement(line - 1).getStartOffset() + 1;
                jLabelPosicion.setText("Fila: " + line + ", Columna: " + column);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private String ultimaPalabraProcesada = "";

    private void pintarUltimaPalabra() {
        try {
            int caretPos = jTextPane1.getCaretPosition();
            Document doc = jTextPane1.getDocument();

            int start = Utilities.getWordStart(jTextPane1, caretPos);
            int end = Utilities.getWordEnd(jTextPane1, caretPos);
            String palabra = doc.getText(start, end - start);

         
            if (palabra.equals(ultimaPalabraProcesada)) {
                return;
            }

            ultimaPalabraProcesada = palabra; 

           
            colorLexico color = new colorLexico(new StringReader(palabra));
            color.yylex();

            List<ColorTexto> colorTextos = color.getListaTokensColoreados();
            for (ColorTexto colorTexto : colorTextos) {
                colorToken(start, colorTexto.getTama(), colorTexto.getColor());
                System.out.println("Aplicando color: " + colorTexto.getColor() + " a token: " + colorTexto.getnToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    
    private void colorToken(int inicio, int tamano, Color color) {
        StyledDocument doc = jTextPane1.getStyledDocument();
        Style estilo = jTextPane1.addStyle("miEstilo", null);
        StyleConstants.setForeground(estilo, color);

        try {
            doc.setCharacterAttributes(inicio, tamano, estilo, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    


    private void initLineNumbers() {
        lineNumberArea = new JTextArea("1");
        lineNumberArea.setEditable(false);
        lineNumberArea.setBackground(jTextPane1.getBackground());
        lineNumberArea.setForeground(java.awt.Color.BLACK);
        lineNumberArea.setFont(jTextPane1.getFont());
        jScrollPane2.setViewportView(lineNumberArea);
        updateLineNumbers();
    }

    private void updateLineNumbers() {
        String content = jTextPane1.getText();
        int totalLines = content.split("\n").length;
        StringBuilder lineNumbers = new StringBuilder();
        for (int i = 1; i <= totalLines; i++) {
            lineNumbers.append(i).append("\n");
        }
        lineNumberArea.setText(lineNumbers.toString());
    }

    
    
    public void analizar() {
        String entrada = jTextPane1.getText();
        analizadorLexico = new AnalizadorLexico(new StringReader(entrada));

        try {
            while (analizadorLexico.yylex() != AnalizadorLexico.YYEOF) {}
            List<Token> tokens = analizadorLexico.getTokens();
            analizadorSintactico = new AnalizadorSintactico(tokens);
            analizadorSintactico.analizarInstrucciones();

            // Obtener la lista de errores léxicos
            List<TokenError> errores = analizadorLexico.getTokenError(); 
            // Imprimir tokens obtenidos
            for (Token token : tokens) {
                System.out.println(token.toString());
            }
            List<ReporteSintactico> erroresSintacticos = analizadorSintactico.getErroresSintacticos();
            if (erroresSintacticos.isEmpty()) {
                System.out.println("No se encontraron errores sintácticos.");
            } else {
                System.out.println("Errores sintácticos encontrados:");
                for (ReporteSintactico error : erroresSintacticos) {
                    System.out.println(error.toString());
                }
            }


           
            if (!errores.isEmpty()) {
                TokenReportes tokenReporte = new TokenReportes(errores);
                tokenReporte.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public String getTextoPane(){
        return jTextPane1.getText();
    }
    
    
    /*
    public void mostrarReporteTablasCreadas() {
        List<TablaCreada> tablas = analizadorSintactico.getTablasCreadas();
        String[] columnNames = {"Tabla", "Columnas"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (TablaCreada tabla : tablas) {
            String columnas = String.join(", ", tabla.getColumnas());
            model.addRow(new Object[]{tabla.getNombre(), columnas});
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("Reporte de Tablas Creadas");
        frame.add(scrollPane);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }
    */


    private void mostrarGrafico(String filePath) {
        // Crear un JFrame para mostrar la imagen
        JFrame frame = new JFrame("Gráfico de Tabla");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Cargar la imagen y agregarla a un JLabel
        ImageIcon icon = new ImageIcon(filePath);
        JLabel label = new JLabel(icon);

        // Agregar el JLabel al JFrame
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        buttonArchivo = new javax.swing.JButton();
        buttonGrafico = new javax.swing.JButton();
        buttonReportes = new javax.swing.JButton();
        buttonAnalizar = new javax.swing.JButton();
        jLabelPosicion = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane2.setName(" 1"); // NOI18N

        jScrollPane1.setViewportView(jTextPane1);

        buttonArchivo.setText("ARCHIVO");
        buttonArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonArchivoActionPerformed(evt);
            }
        });

        buttonGrafico.setText("GENERAR GRAFICO");
        buttonGrafico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGraficoActionPerformed(evt);
            }
        });

        buttonReportes.setText("REPORTES LEXICOS");
        buttonReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReportesActionPerformed(evt);
            }
        });

        buttonAnalizar.setText("Analizar");
        buttonAnalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAnalizarActionPerformed(evt);
            }
        });

        jButton2.setText("REPORTES SINTACTICO");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("REPORTE DE TABLAS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(buttonAnalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(595, 595, 595)
                        .addComponent(jLabelPosicion, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 838, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(buttonArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(buttonGrafico)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(buttonReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(576, Short.MAX_VALUE)
                        .addComponent(jLabelPosicion, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonGrafico, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                            .addComponent(jScrollPane2))
                        .addGap(18, 18, 18)
                        .addComponent(buttonAnalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(50, 50, 50))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAnalizarActionPerformed
        analizar(); // Llama a tu método de análisis
        List<ReporteSintactico> erroresSintacticos = analizadorSintactico.getErroresSintacticos(); // Obtén la lista de errores

        if (erroresSintacticos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El análisis fue satisfactorio.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder mensajeErrores = new StringBuilder("Se encontraron errores sintácticos:\n");
            for (ReporteSintactico error : erroresSintacticos) {
                mensajeErrores.append(error.toString()).append("\n"); // Ajusta la forma en que deseas mostrar los errores
            }
            JOptionPane.showMessageDialog(this, mensajeErrores.toString(), "Errores Sintácticos", JOptionPane.ERROR_MESSAGE);

           
        }
    }//GEN-LAST:event_buttonAnalizarActionPerformed

    private void buttonReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReportesActionPerformed
      List<TokenError> errores = analizadorLexico.getTokenError();
    
        if (errores.isEmpty()) {
            System.out.println("No se encontraron errores léxicos.");
        } else {
            TokenReportes tokenReporte = new TokenReportes(errores);
            tokenReporte.setVisible(true);
        }
    }//GEN-LAST:event_buttonReportesActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
            List<ReporteSintactico> erroresSintacticos = analizadorSintactico.getErroresSintacticos(); // Obtén la lista de errores
            ReporteErroresFrame reporteFrame = new ReporteErroresFrame(erroresSintacticos);
            reporteFrame.setLocationRelativeTo(null);
            reporteFrame.setVisible(true); // Muestra la ventana
    }//GEN-LAST:event_jButton2ActionPerformed

    private void buttonGraficoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGraficoActionPerformed
        if (analizadorSintactico == null) {
            System.out.println("Error: el análisis sintáctico no se ha realizado.");
            return;
        }
        /*
        List<Tabla> tablas = analizadorSintactico.getTablasCreadas(); // Obtén las tablas generadas del análisis
        if (!tablas.isEmpty()) {
            GraficaTabla generadorGrafico = new GraficaTabla();

            // Generar gráficos para cada tabla
            for (Tabla tabla : tablas) {
                generadorGrafico.generarDOT(tabla.getNombre(), tabla.getColumnas());
                generadorGrafico.generarJPG(tabla.getNombre());

                // Mostrar el gráfico generado
                mostrarGrafico("C:\\Users\\crisa\\Documents\\NetBeansProjects\\ProyectoFinalLenguajesFormales\\tabla_" + tabla.getNombre() + ".jpg");
            }
        } else {
            System.out.println("No hay tablas para graficar.");
        }
        */
    }//GEN-LAST:event_buttonGraficoActionPerformed

    private void buttonArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonArchivoActionPerformed
       
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Archivo"); // Título del diálogo

        
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile(); 
            System.out.println("Archivo seleccionado: " + fileToOpen.getAbsolutePath());

            
            try {
                
                
                BufferedReader reader = new BufferedReader(new FileReader(fileToOpen));
                String line;
                StringBuilder fileContent = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append("\n"); 
                }

                reader.close(); 

                
                jTextPane1.setText(fileContent.toString());

            } catch (IOException e) {
                e.printStackTrace(); 
            }
        } else {
            System.out.println("Operación de selección de archivo cancelada.");
        }
    }//GEN-LAST:event_buttonArchivoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AnalizarFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AnalizarFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AnalizarFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AnalizarFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AnalizarFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAnalizar;
    private javax.swing.JButton buttonArchivo;
    private javax.swing.JButton buttonGrafico;
    private javax.swing.JButton buttonReportes;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabelPosicion;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
