package ReportesToken;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author crisa
 */
public class ReporteErroresFrame extends JFrame {
    
    private JTable tablaErrores;

    public ReporteErroresFrame(List<ReporteSintactico> erroresSintacticos) {
        setTitle("Reporte de Errores Sintácticos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Crear el modelo de la tabla con las columnas necesarias
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Token");
        model.addColumn("Línea");
        model.addColumn("Columna");
        model.addColumn("Descripción");

        // Llenar el modelo con los errores sintácticos
        for (ReporteSintactico error : erroresSintacticos) {
            model.addRow(new Object[]{
                error.getLexema(),
                error.getLinea(),
                error.getColumna(),
                error.getDescripcion()  // Usar la descripción del error
            });
        }

        // Crear la tabla con el modelo
        tablaErrores = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tablaErrores);

        // Añadir la tabla al panel de contenido
        add(scrollPane, BorderLayout.CENTER);
    }
}
