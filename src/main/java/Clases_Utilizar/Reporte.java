package Clases_Utilizar;

import javax.swing.*;

public class Reporte {

    private final Token[] tokens; // Arreglo de tokens para almacenar los tokens a reportar
    private final int cantidadTokens; // Cantidad de tokens en el arreglo

    // Constructor que inicializa el reporte con un arreglo de tokens y su cantidad
    public Reporte(Token[] tokens, int cantidadTokens) {
        this.tokens = tokens;
        this.cantidadTokens = cantidadTokens;
    }

    // Método para generar el reporte y mostrarlo en una ventana
    public void generarReporte() {
        // Crear el panel del reporte
        ReportePanel reportePanel = new ReportePanel(tokens, cantidadTokens);

        // Mostrar el panel en una ventana
        JOptionPane.showMessageDialog(null, reportePanel, "Reporte de Tokens", JOptionPane.PLAIN_MESSAGE);
    }
}
