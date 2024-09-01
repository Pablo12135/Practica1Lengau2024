package Clases_Utilizar;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EditorTexto extends JTextArea {

    private JLabel statusLabel; // Etiqueta para mostrar el estado (línea y columna)

    // Constructor de EditorTexto
    public EditorTexto(JLabel statusLabel) {
        super(); // Llama al constructor de JTextArea
        this.statusLabel = statusLabel; // Inicializa la etiqueta de estado

        // Agrega un CaretListener para actualizar la posición del cursor
        this.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                int pos = getCaretPosition(); // Obtiene la posición actual del cursor
                int line = 0;
                int column = 0;

                try {
                    // Calcula la línea y columna actuales
                    line = getLineOfOffset(pos);
                    column = pos - getLineStartOffset(line);
                } catch (Exception ex) {
                    ex.printStackTrace(); // Maneja posibles excepciones
                }
                // Actualiza la etiqueta de estado con la línea y columna
                statusLabel.setText("Línea: " + (line + 1) + ", Columna: " + (column + 1));
            }
        });
    }

    // Método para cargar el contenido de un archivo en el editor de texto
    public void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser(); // Crea un selector de archivos
        int result = fileChooser.showOpenDialog(null); // Muestra el diálogo de selección de archivos

        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile(); // Obtiene el archivo seleccionado
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                this.setText(""); // Limpia el área de texto antes de cargar el nuevo contenido
                String linea;
                // Lee el archivo línea por línea y agrega el contenido al editor
                while ((linea = reader.readLine()) != null) {
                    this.append(linea + "\n");
                }
            } catch (IOException e) {
                // Muestra un mensaje de error si ocurre una excepción
                JOptionPane.showMessageDialog(null, "Error al cargar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método estático para crear un menú con la opción de cargar archivo
    public static JMenuBar crearMenu(EditorTexto editor) {
        JMenuBar menuBar = new JMenuBar(); // Crea la barra de menús
        JMenu archivoMenu = new JMenu("Archivo"); // Crea el menú "Archivo"

        JMenuItem cargarArchivo = new JMenuItem("Cargar archivo"); // Crea un ítem de menú para cargar archivo
        // Agrega un ActionListener para manejar el clic en el ítem de menú
        cargarArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.cargarArchivo(); // Llama al método para cargar el archivo
            }
        });

        archivoMenu.add(cargarArchivo); // Agrega el ítem de menú al menú "Archivo"
        menuBar.add(archivoMenu); // Agrega el menú "Archivo" a la barra de menús
        return menuBar; // Devuelve la barra de menús
    }
}
