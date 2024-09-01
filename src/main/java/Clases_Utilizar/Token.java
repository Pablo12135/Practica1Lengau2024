package Clases_Utilizar;

import java.awt.Color;

public class Token {

    private final String tipo;          // Tipo de token (por ejemplo, identificador, palabra reservada)
    private final String lexema;        // Texto del token
    private final int linea;            // Línea en la que se encuentra el token
    private final int columna;          // Columna en la que se encuentra el token
    private Color color;                // Color asociado al token (ahora no es final para permitir cambios)
    private final int filaCuadricula;   // Fila en la cuadrícula (opcional)
    private final int columnaCuadricula;// Columna en la cuadrícula (opcional)

    // Constructor para token con coordenadas especificadas
    public Token(String tipo, String lexema, int linea, int columna, String colorHex, int filaCuadricula, int columnaCuadricula) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
        this.color = Color.decode(asegurarFormatoHexadecimal(colorHex));
        this.filaCuadricula = filaCuadricula;
        this.columnaCuadricula = columnaCuadricula;
    }

    // Constructor para token sin coordenadas especificadas (se usará la posición en la secuencia)
    public Token(String tipo, String lexema, int linea, int columna, String colorHex) {
        this(tipo, lexema, linea, columna, colorHex, -1, -1); // -1 indica que no se especificaron coordenadas
    }

    // Método para asegurar que el formato del color hexadecimal es válido
    private String asegurarFormatoHexadecimal(String colorHex) {
        if (!colorHex.startsWith("#")) {
            colorHex = "#" + colorHex;
        }
        if (colorHex.length() != 7) {
            throw new IllegalArgumentException("Formato de color hexadecimal inválido: " + colorHex);
        }
        return colorHex;
    }

    // Métodos getter
    public String getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public Color getColor() {
        return color;
    }

    // Método setter para permitir la modificación del color
    public void setColor(String colorHex) {
        this.color = Color.decode(asegurarFormatoHexadecimal(colorHex));
    }

    // Métodos para obtener las coordenadas de la cuadrícula, devolviendo -1 si no están especificadas
    public int getFilaCuadricula() {
        return filaCuadricula;
    }

    public int getColumnaCuadricula() {
        return columnaCuadricula;
    }

    // Método toString para mostrar la información del token de forma legible
    @Override
    public String toString() {
        return String.format("Tipo de Token: %s, Lexema: %s, Línea: %d, Columna: %d, Color: %s, Fila Cuadrícula: %d, Columna Cuadrícula: %d",
                tipo, lexema, linea, columna, color.toString(), filaCuadricula, columnaCuadricula);
    }
}
