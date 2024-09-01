package Clases_Utilizar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LienzoPanel extends JPanel {

    private Cuadricula cuadricula;
    private int tamanoCuadro; // Tamaño del cuadro

    public LienzoPanel(Cuadricula cuadricula) {
        this.cuadricula = cuadricula;
        setPreferredSize(new Dimension(400, 400)); // Tamaño inicial
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = e.getY() / tamanoCuadro;
                int columna = e.getX() / tamanoCuadro;

                Token token = LienzoPanel.this.cuadricula.getToken(fila + 1, columna + 1);
                System.out.println("Token obtenido: " + token);

                if (token != null) {
                    String mensaje = String.format("""
                            Token: %s
                            Lexema: %s
                            Línea: %d
                            Columna: %d
                            Fila en cuadrícula: %d
                            Columna en cuadrícula: %d""",
                            token.getTipo(), token.getLexema(), token.getLinea(), token.getColumna(),
                            fila, columna);

                    JOptionPane.showMessageDialog(null, mensaje);

                    // Crear una instancia de Automata para el token seleccionado
                    Automata automata = new Automata(token);
                    automata.mostrarInformacion();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cuadricula != null) {
            int width = getWidth();
            int height = getHeight();
            int filas = cuadricula.getFilas();
            int columnas = cuadricula.getColumnas();

            tamanoCuadro = Math.min(width / columnas, height / filas);

            // Dibuja la cuadrícula y los tokens
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    Token token = cuadricula.getToken(i + 1, j + 1);
                    if (token != null) {
                        g.setColor(token.getColor());
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(j * tamanoCuadro, i * tamanoCuadro, tamanoCuadro, tamanoCuadro);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * tamanoCuadro, i * tamanoCuadro, tamanoCuadro, tamanoCuadro);
                }
            }
        }
    }

    // Método para guardar la cuadrícula como imagen PNG
    public void guardarImagenPNG(String rutaArchivo) {
        GuardarImagen(rutaArchivo, "png");
    }

    // Método para guardar la cuadrícula como imagen JPG
    public void guardarImagenJPG(String rutaArchivo) {
        GuardarImagen(rutaArchivo, "jpg");
    }

    private void GuardarImagen(String rutaArchivo, String formato) {
        try {
            // Crear una imagen en memoria
            BufferedImage imagen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = imagen.getGraphics();
            paint(g); // Dibuja el contenido del panel en la imagen
            g.dispose();

            // Guardar la imagen en el archivo especificado
            File archivo = new File(rutaArchivo);
            ImageIO.write(imagen, formato, archivo);
            JOptionPane.showMessageDialog(this, "Imagen guardada exitosamente como " + formato.toUpperCase() + " en: " + rutaArchivo);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar la imagen: " + e.getMessage());
        }
    }

    // Método para actualizar la cuadrícula y ajustar el tamaño del panel
    public void setCuadricula(Cuadricula cuadricula) {
        this.cuadricula = cuadricula;
        if (cuadricula != null) {
            // Inicializa el tamaño del panel basado en la cuadrícula
            int width = cuadricula.getColumnas() * tamanoCuadro;
            int height = cuadricula.getFilas() * tamanoCuadro;
            setPreferredSize(new Dimension(width, height));
            revalidate();
            repaint(); // Se llama a repaint para actualizar el panel inicialmente
        }
    }
}
