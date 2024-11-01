/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportesToken;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author crisa
 */
public class SintacticoFrameError extends JFrame{
    
    public SintacticoFrameError(List<ReporteSintactico> erroresSintacticos) {
        setTitle("Reporte de Errores Sintácticos");
        String[] columnNames = {"Lexema", "Tipo Token", "Línea", "Columna", "Descripción"};
        Object[][] data = new Object[erroresSintacticos.size()][5];

        for (int i = 0; i < erroresSintacticos.size(); i++) {
            ReporteSintactico error = erroresSintacticos.get(i);
            data[i][0] = error.getLexema();
            data[i][1] = error.getTipoToken();
            data[i][2] = error.getLinea();
            data[i][3] = error.getColumna();
            data[i][4] = error.getDescripcion();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setSize(600, 300);
        setLocationRelativeTo(null);
    }
}
