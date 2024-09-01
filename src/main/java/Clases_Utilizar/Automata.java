package Clases_Utilizar;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Automata {

    private Token token;

    public Automata(Token token) {
        this.token = token;
    }

    // Método para graficar el autómata usando Graphviz
    public void graficarAutomata() {
        try {
            System.out.println("Iniciando la generación del autómata para el token: " + token.getLexema());

            // Crear el archivo .dot que se usará para generar el gráfico con Graphviz
            FileWriter fileWriter = new FileWriter("automata.dot");

            // Generar el código .dot para representar el autómata del token
            fileWriter.write("digraph G {\n");
            fileWriter.write("rankdir=LR;\n");
            fileWriter.write("node [shape=circle];\n");

            // Crear un nodo por cada carácter del lexema
            String lexema = token.getLexema(); // Obtiene el lexema del token
            for (int i = 0; i < lexema.length(); i++) { // Itera sobre cada carácter del lexema
                String nodoActual = "q" + i; // Define el nombre del nodo actual (q0, q1, q2, etc.)
                String nodoSiguiente = "q" + (i + 1); // Define el nombre del siguiente nodo (q1, q2, q3, etc.)

                // Último nodo se convertirá en un nodo final
                fileWriter.write(nodoActual + " -> " + nodoSiguiente + " [label=\"" + lexema.charAt(i) + "\"];\n");
                // Escribe en el archivo la transición desde el nodo actual al siguiente nodo
                // Etiqueta la transición con el carácter del lexema correspondiente

                if (i == lexema.length() - 1) { // Verifica si es el último carácter del lexema
                    fileWriter.write(nodoSiguiente + " [shape=doublecircle];\n");
                    // Si es el último carácter, convierte el nodo siguiente en un nodo final (doble círculo)
                }
            }

            fileWriter.write("}\n");
            fileWriter.close();
            System.out.println("Archivo .dot creado correctamente.");

            // Ejecutar el comando de Graphviz para generar el gráfico en formato PNG
            Process proceso = Runtime.getRuntime().exec("dot -Tpng automata.dot -o automata.png");

            // Esperar a que el proceso termine para asegurar que el archivo esté listo
            int exitCode = proceso.waitFor();
            if (exitCode == 0) {
                System.out.println("Imagen del autómata generada como automata.png");
            } else {
                System.out.println("Error al generar la imagen del autómata. Código de salida: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Mostrar la información del token en una ventana
    public void mostrarInformacion() {
        // Graficar y mostrar el autómata automáticamente
        graficarAutomata();
        mostrarImagenAutomata();
    }

    // Método para mostrar el autómata generado
    private void mostrarImagenAutomata() {
        File archivoImagen = new File("automata.png");

        if (archivoImagen.exists()) {
            try {
                // Cargar la imagen generada por Graphviz
                BufferedImage imagen = ImageIO.read(archivoImagen);
                ImageIcon iconoImagen = new ImageIcon(imagen);

                JFrame ventanaAutomata = new JFrame("Autómata");
                ventanaAutomata.setSize(500, 500);
                ventanaAutomata.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JLabel etiquetaImagen = new JLabel(iconoImagen);
                ventanaAutomata.add(etiquetaImagen);
                ventanaAutomata.setVisible(true);

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar la imagen del autómata.");
            }
        } else {
            // Mostrar un mensaje de error si la imagen no fue generada
            JOptionPane.showMessageDialog(null, "Error: no se pudo generar el autómata.");
        }
    }
}
