package Clases_Utilizar;

import java.awt.Color;
import java.awt.Graphics;

public class Cuadricula {
    // Matriz bidimensional que almacena los tokens en la cuadrícula

    private final Token[][] tokens;
    // Número de filas en la cuadrícula
    private final int filas;
    // Número de columnas en la cuadrícula
    private final int columnas;

    private final boolean[][] areasModificadas;

    // Constructor que inicializa la cuadrícula con el número de filas y columnas
    public Cuadricula(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        // Inicializa la matriz de tokens con las filas y columnas especificadas
        this.tokens = new Token[filas][columnas];

        this.areasModificadas = new boolean[filas][columnas];
    }

    // Devuelve el número de filas en la cuadrícula
    public int getFilas() {
        return filas;
    }

    // Devuelve el número de columnas en la cuadrícula
    public int getColumnas() {
        return columnas;
    }

    // Asigna un token a una posición específica en la cuadrícula
    public void setToken(int fila, int columna, Token token) {
        int filaIndex = fila - 1;
        int columnaIndex = columna - 1;

        // Verifica si la fila y la columna están dentro de los límites de la cuadrícula
        if (filaIndex >= 0 && filaIndex < filas && columnaIndex >= 0 && columnaIndex < columnas) {
            // Asigna el token a la posición especificada en la cuadrícula
            tokens[filaIndex][columnaIndex] = token;
        }
    }

    // Devuelve el token en una posición específica de la cuadrícula
    public Token getToken(int fila, int columna) {
        // Ajusta el índice para que la base sea 0
        int filaIndex = fila - 1;
        int columnaIndex = columna - 1;

        // Verifica si la fila y la columna están dentro de los límites de la cuadrícula
        if (filaIndex >= 0 && filaIndex < filas && columnaIndex >= 0 && columnaIndex < columnas) {
            // Devuelve el token en la posición especificada
            return tokens[filaIndex][columnaIndex];
        }
        // Devuelve null si la posición está fuera de los límites
        return null;
    }

    // Dibuja la cuadrícula y los tokens en el componente gráfico
    public void dibujarCuadricula(Graphics g, int anchoCuadro, int altoCuadro) {
        // Itera sobre cada fila de la cuadrícula
        for (int i = 1; i <= filas; i++) {
            // Itera sobre cada columna de la cuadrícula
            for (int j = 1; j <= columnas; j++) {
                // Obtiene el token en la posición (i, j)
                Token token = tokens[i - 1][j - 1];
                if (token != null) {
                    // Establece el color del token
                    Color color = token.getColor();
                    g.setColor(color);
                } else {
                    // Si no hay token, dibuja el cuadro vacío
                    g.setColor(Color.WHITE);
                }
                // Dibuja el cuadro en la posición (i, j)
                g.fillRect((j - 1) * anchoCuadro, (i - 1) * altoCuadro, anchoCuadro, altoCuadro);
                // Dibuja el borde del cuadro
                g.setColor(Color.BLACK);
                g.drawRect((j - 1) * anchoCuadro, (i - 1) * altoCuadro, anchoCuadro, altoCuadro);
            }
        }
    }

    // Asigna tokens a los cuadros disponibles de manera secuencial
    public void asignarTokens(Token[] listaTokens, int tokenCount) {
        int index = 0; // Índice para recorrer la lista de tokens
        int totalCuadros = filas * columnas; // Número total de cuadros en la cuadrícula
        int maxTokens = Math.min(tokenCount, totalCuadros); // Número máximo de tokens que se pueden asignar

        // Recorre cada fila de la cuadrícula
        for (int i = 0; i < filas; i++) {
            // Recorre cada columna de la cuadrícula
            for (int j = 0; j < columnas; j++) {
                // Si aún hay tokens para asignar, los coloca en la cuadrícula
                if (index < maxTokens) {
                    setToken(i + 1, j + 1, listaTokens[index]);
                    System.out.printf("Asignando Token %s a posición: (%d, %d)%n",
                            listaTokens[index].getLexema(), i, j);
                    index++; // Incrementa el índice para el siguiente token
                } else {
                    return; // Termina el método si ya no hay más tokens para asignar
                }
            }
        }
    }

    // Método para actualizar el color de un cuadro en una posición específica
    public void cambiarColorCuadro(int fila, int columna, String colorHex) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            // Ajusta el índice para que la base sea 0
            int filaIndex = fila;
            int columnaIndex = columna;

            Token token = getToken(filaIndex + 1, columnaIndex + 1); // Ajusta para obtener el token en base 1

            if (token != null) {
                token.setColor(colorHex);
            } else {
                // Crea un nuevo token con color, fila y columna especificados en base 1
                tokens[filaIndex][columnaIndex] = new Token("Palabra_Reservada", "", fila + 1, columna + 1, colorHex);
            }

            // Marca la área como modificada
            areasModificadas[filaIndex][columnaIndex] = true;
        }
    }
    
    
    // Método para obtener las áreas modificadas
    public boolean[][] getAreasModificadas() {
        return areasModificadas;
    }
}
