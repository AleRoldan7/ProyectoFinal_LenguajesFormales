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
import ReportesToken.TokenReporte;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
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

/**
 *
 * @author crisa
 */
public class AnalizarFrame extends javax.swing.JFrame {
    
    private AnalizadorLexico analizadorLexico;
    private JTextArea lineNumberArea; 
    /**
     * Creates new form AnalizarFrame
     */
    public AnalizarFrame() {
        initComponents();
        initLineNumbers();
       
        jTextPane1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                   
                    updateLineNumbers();
                    //pintarAnalisis();
                });
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    updateLineNumbers();
                    //pintarAnalisis();
                
                });
                
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    updateLineNumbers();
                    //pintarAnalisis();
                });
                    

            }
        });
        
      
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
        String content = jTextPane1.getText(); // Obtenemos el contenido del JTextPane
        int totalLines = content.split("\n").length; // Contamos las líneas

        StringBuilder lineNumbers = new StringBuilder();
        for (int i = 1; i <= totalLines; i++) {
            lineNumbers.append(i).append("\n"); // Generamos los números de línea
        }

        lineNumberArea.setText(lineNumbers.toString());
    }
    

    
    
    public void analizar(){
        String entrada = jTextPane1.getText();
        analizadorLexico = new AnalizadorLexico(new StringReader(entrada));
        
        try {
            while (analizadorLexico.yylex() != AnalizadorLexico.YYEOF) {}
            List<TokenCreate> tokenCreates = analizadorLexico.getTokensCreate();
            List<TokenIdenti> tokenIdenti = analizadorLexico.getTokensIdenti();
            List<TokenSigno> tokenSigno = analizadorLexico.getTokensSigno();
            List<TokenDato> tokenDato = analizadorLexico.getTokensDato();
            List<TokenEntero> tokenEntero = analizadorLexico.getTokensEntero();
            List<Token> token = analizadorLexico.getTokens();
            AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico(token);
            analizadorSintactico.analizarInstrucciones();
            for (Token tokenEntero1 : token) {
                System.out.println(tokenEntero1.toString());
            }
            
            
            /*
            
            for (TokenCreate token : tokenCreates) {
                System.out.println("Esta en create");
                System.out.println(tokenCreates.toString());
               // colorToken(token.getPosicionInicial(), token.getTamaLexema(), Color.ORANGE);
            }
            
            
           
            
            List<TokenDato> tokenDato = analizadorLexico.getTokensDato();
            for (TokenDato token : tokenDato) {
                Color morado = new Color(143, 0, 255);
                System.out.println("Esta en dato");
                System.out.println(tokenDato.toString());
                //colorToken(token.getPosicionInicial(), token.getTamaLexema(), morado);
            }
            
            List<TokenEntero> tokenEntero = analizadorLexico.getTokensEntero();
            for (TokenEntero token : tokenEntero) {
                System.out.println("Esta en entero");
                System.out.println(tokenEntero.toString());
               // colorToken(token.getPosicionInicial(), token.getTamaLexema(), Color.BLUE);
            }
            
            List<TokenIdenti> tokenIdenti = analizadorLexico.getTokensIdenti();
            for (TokenIdenti token : tokenIdenti) {
                System.out.println("Esta identificador");
                System.out.println(tokenIdenti.toString());
                //colorToken(token.getPosicionInicial(), token.getTamaLexema(), Color.MAGENTA);
            }
            
            List<TokenError> error = analizadorLexico.getTokenError();
            for (TokenError tokenError : error) {
                System.out.println("Error");
                System.out.println(error.toString());
                //colorToken(tokenError.getPosicionInicial(), tokenError.getTamaLexema(), Color.BLACK);
            }
            */
           
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public String getTextoPane(){
        return jTextPane1.getText();
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

    


    private void pintarAnalisis() {
        String entrada = jTextPane1.getText();
        colorLexico color = new colorLexico(new StringReader(entrada));
        
        try {
            
            while (color.yylex() != color.YYEOF) {}
            
            List<ColorTexto> colorTextos = color.getListaTokensColoreados();
            for (ColorTexto colorTexto : colorTextos) {
                System.out.println("Esta pintado");
                colorToken(colorTexto.getInicio(), colorTexto.getTama(), colorTexto.getColor());
            }
            
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }

    private void sintactico(){
       
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane2.setName(" 1"); // NOI18N

        jScrollPane1.setViewportView(jTextPane1);

        buttonArchivo.setText("ARCHIVO");

        buttonGrafico.setText("GENERAR GRAFICO");

        buttonReportes.setText("REPORTES");
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
                            .addComponent(buttonReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                            .addComponent(buttonReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGap(0, 905, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 662, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
        analizar();
        //sintactico();
        List<String> listaErrores = new ArrayList<>();
        System.out.println(listaErrores);
    }//GEN-LAST:event_buttonAnalizarActionPerformed

    private void buttonReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReportesActionPerformed
        
        List<TokenError> error = analizadorLexico.getTokenError();
        System.out.println(error);
        TokenReporte tokenReporte = new TokenReporte(error);
        tokenReporte.agregarDatos();
        tokenReporte.setLocationRelativeTo(null);
        tokenReporte.setVisible(true);
    }//GEN-LAST:event_buttonReportesActionPerformed

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
    private javax.swing.JLabel jLabelPosicion;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
