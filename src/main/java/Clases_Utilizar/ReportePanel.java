package Clases_Utilizar;

import javax.swing.*;
import java.awt.*;

public class ReportePanel extends JPanel {

    private final Token[] tokens;
    private final int cantidadTokens;

    public ReportePanel(Token[] tokens, int cantidadTokens) {
        this.tokens = tokens;
        this.cantidadTokens = cantidadTokens;
        setLayout(new GridLayout(cantidadTokens + 1, 1));

        // Add header row
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(1, 6)); // 6 columns
        headerPanel.add(new JLabel("Token"));
        headerPanel.add(new JLabel("Lexema"));
        headerPanel.add(new JLabel("Línea"));
        headerPanel.add(new JLabel("Columna"));
        headerPanel.add(new JLabel("Cuadro"));
        headerPanel.add(new JLabel("Color"));
        add(headerPanel);

        // Add rows for each token
        for (Token token : tokens) {
            if (token != null) {
                JPanel tokenPanel = new JPanel();
                tokenPanel.setLayout(new GridLayout(1, 6)); // 6 columns

                // Token type
                tokenPanel.add(new JLabel(token.getTipo()));

                // Lexema
                tokenPanel.add(new JLabel(token.getLexema()));

                // Line number
                tokenPanel.add(new JLabel(String.valueOf(token.getLinea())));

                // Column number
                tokenPanel.add(new JLabel(String.valueOf(token.getColumna())));

                // Cuadro
                String cuadro = "Fila: " + (token.getLinea() + 1) + " Col: " + (token.getColumna() + 1);
                tokenPanel.add(new JLabel(cuadro));

                // Color
                JPanel colorPanel = new JPanel();
                colorPanel.setBackground(token.getColor());
                tokenPanel.add(colorPanel);

                add(tokenPanel);
            }
        }
    }
}
