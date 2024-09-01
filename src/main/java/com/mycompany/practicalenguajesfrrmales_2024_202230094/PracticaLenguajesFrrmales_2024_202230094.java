package com.mycompany.practicalenguajesfrrmales_2024_202230094;

import Clases_Utilizar.Cuadricula;
import Clases_Utilizar.EditorTexto;
import Clases_Utilizar.AnalizadorLexico;
import Clases_Utilizar.LienzoPanel;
import Clases_Utilizar.Reporte;
import Clases_Utilizar.Token;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PracticaLenguajesFrrmales_2024_202230094 extends JFrame {

    private EditorTexto editorTexto;
    private LienzoPanel lienzoPanel;
    private Cuadricula cuadricula;
    private Token[] tokens;
    private JLabel statusLabel;

    public PracticaLenguajesFrrmales_2024_202230094() {
        setTitle("Analizador Léxico Visual Basic");
        setSize(1000, 600); // Tamaño inicial de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Línea: 1, Columna: 1");

        editorTexto = new EditorTexto(statusLabel);
        JScrollPane scrollPane = new JScrollPane(editorTexto);

        // Inicializa la cuadrícula con dimensiones mínimas
        cuadricula = new Cuadricula(1, 1); // Inicialmente con cuadrícula mínima
        lienzoPanel = new LienzoPanel(cuadricula);
        lienzoPanel.setPreferredSize(new Dimension(100, 100)); // Tamaño mínimo para iniciar

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, lienzoPanel);
        splitPane.setResizeWeight(0.5); // Divide el espacio en partes iguales
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.CENTER);

        JButton analyzeButton = new JButton("Analizar Código");
        analyzeButton.addActionListener(e -> analizarCodigo());

        JButton BotonReporte = new JButton("Generar Reporte");
        BotonReporte.addActionListener(e -> {
            if (tokens != null) {
                Reporte reporte = new Reporte(tokens, tokens.length);
                reporte.generarReporte();
            } else {
                JOptionPane.showMessageDialog(this, "Primero analice el código para generar un reporte.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton saveImageButton = new JButton("Guardar Imagen");
        saveImageButton.addActionListener(e -> GuardarImagen());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(analyzeButton);
        buttonPanel.add(BotonReporte);
        buttonPanel.add(saveImageButton);
        add(buttonPanel, BorderLayout.NORTH);

        setJMenuBar(EditorTexto.crearMenu(editorTexto));

        add(statusLabel, BorderLayout.SOUTH);

        // Inicializa la cuadrícula
        inicializarCuadricula();
    }

    // Método para inicializar la cuadrícula
    private void inicializarCuadricula() {
        // Solicita el tamaño de la cuadrícula al usuario
        String filasStr = JOptionPane.showInputDialog(this, "Ingrese el número de filas para la cuadrícula:", "Tamaño de la cuadrícula", JOptionPane.QUESTION_MESSAGE);
        String columnasStr = JOptionPane.showInputDialog(this, "Ingrese el número de columnas para la cuadrícula:", "Tamaño de la cuadrícula", JOptionPane.QUESTION_MESSAGE);

        // Valida y convierte la entrada a enteros
        int filas, columnas;
        try {
            filas = Integer.parseInt(filasStr);
            columnas = Integer.parseInt(columnasStr);
        } catch (NumberFormatException e) {
            // Muestra un mensaje de error si la entrada no es válida
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crea una nueva cuadrícula con el tamaño especificado
        cuadricula = new Cuadricula(filas, columnas);

        // Redimensiona el LienzoPanel para que coincida con el tamaño de la cuadrícula
        lienzoPanel.setCuadricula(cuadricula);
        lienzoPanel.setPreferredSize(new Dimension(columnas * 10, filas * 10)); // Ajusta el tamaño basado en el tamaño de cuadro
        lienzoPanel.revalidate();
        lienzoPanel.repaint();
    }

    // Método para analizar el código
    private void analizarCodigo() {
        if (cuadricula == null) {
            JOptionPane.showMessageDialog(this, "Primero defina el tamaño de la cuadrícula.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Analiza el código fuente del EditorTexto
        String codigo = editorTexto.getText();
        AnalizadorLexico analizador = new AnalizadorLexico(codigo, cuadricula, lienzoPanel); // Añade el parámetro lienzoPanel
        analizador.analizar();

        // Obtiene los tokens analizados
        tokens = analizador.getTokens();

        // Calcula el número total de cuadros en la cuadrícula
        int totalCuadros = cuadricula.getFilas() * cuadricula.getColumnas();
        int tokenCount = 0;

        // Cuenta el número de tokens válidos
        for (Token token : tokens) {
            if (token != null) {
                tokenCount++;
            }
        }

        // Valida si hay suficiente espacio en la cuadrícula para todos los tokens
        if (tokenCount > totalCuadros) {
            // Muestra un mensaje de error si no hay suficiente espacio
            JOptionPane.showMessageDialog(this, "Nuevos tokens identificados.", "", JOptionPane.INFORMATION_MESSAGE);
            return;  // Detiene la ejecución si no hay suficiente espacio
        } else if (tokenCount <= totalCuadros) {
            // Muestra un mensaje de confirmación si hay suficiente espacio
            JOptionPane.showMessageDialog(this, "Hay suficiente espacio para " + tokenCount + " tokens. La cuadrícula tiene " + totalCuadros + " cuadros disponibles.", "Espacio Suficiente", JOptionPane.INFORMATION_MESSAGE);
        }

        // Asigna los tokens a la cuadrícula
        cuadricula.asignarTokens(tokens, tokenCount);
        // Redibuja la cuadrícula en el lienzo con los tokens actualizados
        lienzoPanel.repaint();
    }

    // Método para guardar la imagen de la cuadrícula
    private void GuardarImagen() {
        // Solicita la ruta de archivo al usuario
        JFileChooser fileChooser = new JFileChooser();
        int opcion = fileChooser.showSaveDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String rutaArchivo = archivo.getAbsolutePath();

            // Añade extensión si no está presente
            if (!rutaArchivo.endsWith(".png") && !rutaArchivo.endsWith(".jpg")) {
                rutaArchivo += ".png";
            }

            // Guarda la imagen en el formato especificado
            if (rutaArchivo.endsWith(".png")) {
                lienzoPanel.guardarImagenPNG(rutaArchivo);
            } else if (rutaArchivo.endsWith(".jpg")) {
                lienzoPanel.guardarImagenJPG(rutaArchivo);
            }
        }
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PracticaLenguajesFrrmales_2024_202230094 frame = new PracticaLenguajesFrrmales_2024_202230094();
            frame.setVisible(true);
        });
    }
}
