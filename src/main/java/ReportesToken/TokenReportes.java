package ReportesToken;

import AnalizadorLexico.TokenError;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TokenReportes extends JFrame {

    private JTable table;

    public TokenReportes(List<TokenError> errores) {
        String[] columnNames = {"Token", "Línea", "Columna", "Descripción"};
        Object[][] data = new Object[errores.size()][4];

        for (int i = 0; i < errores.size(); i++) {
            TokenError error = errores.get(i);
            data[i][0] = error.getLexema();            
            data[i][1] = error.getLinea();            
            data[i][2] = error.getColumna();           
            data[i][3] = "Token no reconocido";        
        }

        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setTitle("Reporte de Errores Léxicos");
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
}
