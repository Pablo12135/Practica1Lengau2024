package Clases_Utilizar;

public class AnalizadorLexico {

    private final String codigoFuente; // Almacena el código fuente que será analizado
    private final Token[] tokens; // Arreglo para almacenar los tokens identificados
    private int tokenCount; // Contador de tokens para manejar el índice del arreglo
    private int columnas; // Define la cantidad de columnas para el manejo de la cuadrícula
    private final Cuadricula cuadricula; // Referencia a la cuadrícula donde se pintarán los tokens   
    private final LienzoPanel lienzoPanel; // Referencia al panel de lienzo

    // Constructor de la clase que inicializa los atributos
    public AnalizadorLexico(String codigoFuente, Cuadricula cuadricula, LienzoPanel lienzoPanel) {
        this.codigoFuente = codigoFuente; // Asigna el código fuente que será analizado
        this.tokens = new Token[100]; // Inicializa el arreglo de tokens con un tamaño fijo de 100
        this.tokenCount = 0; // Inicializa el contador de tokens a 0
        this.cuadricula = cuadricula; // Asigna la referencia de la cuadrícula
        this.lienzoPanel = lienzoPanel; // Asigna la referencia del panel de lienzo
    }

    // Método principal para analizar el código fuente línea por línea y palabra por palabra
    public void analizar() {
        int secuenciaFila = 0; // Inicializa el contador de la secuencia de fila
        int secuenciaColumna = 0; // Inicializa el contador de la secuencia de columna

        StringBuilder lineaActual = new StringBuilder(); // Inicializa un StringBuilder para construir cada línea

        for (int i = 0; i < codigoFuente.length(); i++) { // Itera sobre cada carácter del código fuente
            char c = codigoFuente.charAt(i); // Obtiene el carácter actual

            // Detecta fin de línea
            if (c == '\n') {
                procesarLinea(lineaActual.toString(), i, secuenciaFila, secuenciaColumna); // Procesa la línea actual
                lineaActual.setLength(0); // Reinicia la construcción de la línea
            } else {
                lineaActual.append(c); // Añade el carácter actual a la construcción de la línea
            }
        }

        // Procesa la última línea si no termina en '\n'
        if (lineaActual.length() > 0) {
            procesarLinea(lineaActual.toString(), codigoFuente.length() - 1, secuenciaFila, secuenciaColumna);
        }
    }

    // Método para procesar una línea de código
    private void procesarLinea(String linea, int numeroLinea, int secuenciaFila, int secuenciaColumna) {
        int index = linea.indexOf("Square.Color("); // Busca el token especial "Square.Color("
        int lastIndex = 0; // Inicializa el índice de la última posición procesada

        while (index != -1) { // Mientras se encuentre el token especial
            if (index > lastIndex) { // Si hay contenido antes del token especial
                String beforeToken = linea.substring(lastIndex, index); // Extrae el contenido antes del token especial
                ProcesarLineaComun(beforeToken, numeroLinea); // Procesa el contenido común
            }

            int cierreParentesis = linea.indexOf(")", index); // Busca el cierre de paréntesis del token especial
            if (cierreParentesis != -1) { // Si encuentra el cierre de paréntesis
                String tokenEspecial = linea.substring(index, cierreParentesis + 1); // Extrae el token especial completo

                String[] detalles = ExtraerColorHex(tokenEspecial); // Extrae los detalles del token especial
                String colorHex = detalles[0]; // Extrae el color hexadecimal
                String fila = detalles[1]; // Extrae la fila
                String columna = detalles[2]; // Extrae la columna

                if (!fila.isEmpty() && !columna.isEmpty()) { // Si la fila y columna están especificadas
                    int filaInt = Integer.parseInt(fila.trim()); // Convierte la fila a entero
                    int columnaInt = Integer.parseInt(columna.trim()); // Convierte la columna a entero
                    agregarToken(new Token("Square_Color", tokenEspecial, numeroLinea + 1, index + 1, colorHex, filaInt, columnaInt)); // Agrega el token especial
                    cuadricula.cambiarColorCuadro(filaInt, columnaInt, colorHex); // Cambia el color del cuadro en la cuadrícula
                    lienzoPanel.repaint(); // Redibuja el panel de lienzo
                } else { // Si solo se especifica el color
                    agregarToken(new Token("Square_Color", tokenEspecial, numeroLinea + 1, index + 1, colorHex, secuenciaFila, secuenciaColumna)); // Agrega el token especial con la secuencia

                    secuenciaColumna++; // Incrementa la secuencia de columna
                    if (secuenciaColumna >= columnas) { // Si la columna excede el límite
                        secuenciaColumna = 0; // Reinicia la secuencia de columna
                        secuenciaFila++; // Incrementa la secuencia de fila
                    }
                }

                lastIndex = cierreParentesis + 1; // Actualiza el índice de la última posición procesada
            } else {
                break; // Si no se encuentra el cierre de paréntesis, se detiene el bucle
            }

            index = linea.indexOf("Square.Color(", lastIndex); // Busca la siguiente aparición del token especial
        }

        if (lastIndex < linea.length()) { // Si queda contenido después del último token especial
            String afterToken = linea.substring(lastIndex); // Extrae el contenido restante
            ProcesarLineaComun(afterToken, numeroLinea); // Procesa el contenido restante
        }
    }

    // Método para procesar el contenido de la línea que no es el token especial
    private void ProcesarLineaComun(String linea, int numeroLinea) {
        // Reemplaza ciertos caracteres para separar símbolos de palabras
        linea = linea.replace("(", " ( ").replace(")", " ) ")
                .replace("{", " { ").replace("}", " } ")
                .replace("[", " [ ").replace("]", " ] ");

        String[] palabras = linea.split("\\s+"); // Divide la línea en palabras y símbolos

        for (int j = 0; j < palabras.length; j++) { // Itera sobre cada palabra
            String palabra = palabras[j];

            if (EsComentario(palabra)) { // Verifica si es un comentario
                agregarToken(new Token("Comentario", palabra, numeroLinea + 1, j + 1, "#B3B3B3")); // Agrega el token de comentario
                break; // Detiene el procesamiento de la línea si es un comentario
            }

            // Identifica el tipo de token y lo almacena en el arreglo
            if (EsPalabraReservada(palabra)) {
                agregarToken(new Token("Palabra_Reservada", palabra, numeroLinea + 1, j + 1, "#60A917"));
            } else if (EsBooleano(palabra)) { // Verifica primero si es un booleano
                agregarToken(new Token("Booleano", palabra, numeroLinea + 1, j + 1, "#FA6800"));
            } else if (EsLogico(palabra)) { // Verifica después si es un operador lógico
                agregarToken(new Token("Logico", palabra, numeroLinea + 1, j + 1, getLogico(palabra)));
            } else if (EsIdentificador(palabra)) { // Verifica identificadores después de booleanos y lógicos
                agregarToken(new Token("Identificador", palabra, numeroLinea + 1, j + 1, "#FFD300"));
            } else if (EsEntero(palabra)) {
                agregarToken(new Token("Entero", palabra, numeroLinea + 1, j + 1, "#1BA1E2"));
            } else if (EsDecimal(palabra)) {
                agregarToken(new Token("Decimal", palabra, numeroLinea + 1, j + 1, "#FFFF88"));
            } else if (EsCaracter(palabra)) {
                agregarToken(new Token("Caracter", palabra, numeroLinea + 1, j + 1, "#0050EF"));
            } else if (EsAritmeticos(palabra)) {
                agregarToken(new Token("Aritmetico", palabra, numeroLinea + 1, j + 1, getColorOperador(palabra)));
            } else if (EsRelacionoComparacion(palabra)) {
                agregarToken(new Token("Relacionales o Comparación", palabra, numeroLinea + 1, j + 1, getRelacionalesComparación(palabra)));
            } else if (EsAsignacionCompuesta(palabra)) {
                agregarToken(new Token("Asignacion Compuesta", palabra, numeroLinea + 1, j + 1, "#FFFFFF"));
            } else if (EsAsignacion(palabra)) {
                agregarToken(new Token("Asignacion", palabra, numeroLinea + 1, j + 1, "#41D9D4"));
            } else if (EsSignosySimbolos(palabra)) {
                agregarToken(new Token("Signos y Simbolos", palabra, numeroLinea + 1, j + 1, getSimbolo(palabra)));
            } else if (EsCadena(palabra)) {
                agregarToken(new Token("Cadena", palabra, numeroLinea + 1, j + 1, "#E51400"));
            }
        }
    }

    // Método para agregar un token al arreglo
    private void agregarToken(Token token) {
        if (tokenCount < tokens.length) {
            tokens[tokenCount++] = token; // Almacena el token y aumenta el contador
        } else {
            System.out.println("Arreglo de tokens lleno. Considera aumentar el tamaño del arreglo.");
        }
    }

    // Métodos de validación para identificar diferentes tipos de tokens
    private boolean EsEntero(String palabra) {
        try {
            // Intenta convertir la palabra a un entero usando Integer.parseInt
            Integer.parseInt(palabra);
            // Si no se lanza excepción, la conversión fue exitosa, por lo que la palabra es un entero
            return true;
        } catch (NumberFormatException e) {
            // Si ocurre una excepción, la palabra no es un entero
            return false;
        }
    }

    private boolean EsDecimal(String palabra) {
        try {
            // Intenta convertir la palabra a un número decimal usando Double.parseDouble
            Double.parseDouble(palabra);
            // Verifica que la palabra contenga un punto, asegurando que sea un decimal, no un entero
            return palabra.contains(".");
        } catch (NumberFormatException e) {
            // Si ocurre una excepción, la palabra no es un decimal
            return false;
        }
    }

    private boolean EsBooleano(String palabra) {
        // Lista de valores booleanos válidos
        String[] Boolenas = {"True", "False"};
        // Itera sobre los valores válidos
        for (String Palabrabol : Boolenas) {
            // Si la palabra coincide con un valor válido, es un booleano
            if (palabra.equals(Palabrabol)) {
                return true;
            }
        }
        // Si no coincide con ningún valor válido, no es un booleano
        return false;
    }

    private boolean EsCaracter(String palabra) {
        // Verifica que la palabra tenga exactamente 3 caracteres y que comience y termine con acento grave (`´`)
        return palabra.length() == 3 && palabra.startsWith("´") && palabra.endsWith("´");
    }

    private boolean EsCadena(String palabra) {
        // Verifica que la palabra comience y termine con comillas dobles (“¨”)
        return palabra.startsWith("¨") && palabra.endsWith("¨");
    }

    private boolean EsPalabraReservada(String palabra) {
        String[] palabrasReservadas = {"Module", "End", "Sub", "Main", "Dim", "As", "Integer", "String", "Boolean", "Double", "Char",
            "Console.WriteLine", "Console.ReadLine", "If", "ElseIf", "Else", "Then", "While", "Do", "Loop", "For", "To", "Next", "Function",
            "Return", "Const"};
        // Itera sobre la lista de palabras reservadas
        for (String reservada : palabrasReservadas) {
            // Si la palabra coincide con una palabra reservada, es una palabra reservada
            if (palabra.equals(reservada)) {
                return true;
            }
        }
        // Si no coincide con ninguna palabra reservada, no es una palabra reservada
        return false;
    }

    private boolean EsIdentificador(String palabra) {
        // Verifica si la palabra sigue el formato de identificador (comienza con una letra y puede contener letras, números y guiones bajos)
        return palabra.matches("[a-zA-Z][a-zA-Z0-9_]*");
    }

    private boolean EsAritmeticos(String palabra) {
        String[] operadores = {"+", "-", "^", "/", "Mod", "*"};
        // Itera sobre la lista de operadores
        for (String operador : operadores) {
            // Si la palabra coincide con un operador aritmético, es un operador aritmético
            if (palabra.equals(operador)) {
                return true;
            }
        }
        // Si no coincide con ningún operador aritmético, no es un operador aritmético
        return false;
    }

    private boolean EsRelacionoComparacion(String palabra) {
        String[] operadores = {"==", "<>", ">", "<", ">=", "<="};
        for (String operador : operadores) {
            if (palabra.equals(operador)) {
                return true;
            }
        }
        return false;
    }

    private boolean EsAsignacionCompuesta(String palabra) {
        String[] operadores = {"+=", "-=", "*=", "/="};
        for (String operador : operadores) {
            if (palabra.equals(operador)) {
                return true;
            }
        }
        return false;
    }

    private boolean EsAsignacion(String palabra) {
        return palabra.equals("=");
    }

    private boolean EsSignosySimbolos(String palabra) {
        String[] operadores = {"(", ")", "[", "]", "{", "}", ",", "."};
        for (String operador : operadores) {
            if (palabra.equals(operador)) {
                return true;
            }
        }
        return false;
    }

    private boolean EsLogico(String palabra) {
        String[] operadores = {"And", "Or", "Not"};
        for (String operador : operadores) {
            if (palabra.equals(operador)) {
                return true;
            }
        }
        return false;
    }

    //extraer el color hexadecimal 
    private String[] ExtraerColorHex(String tokenEspecial) {
        // Se definen variables para almacenar el color, fila y columna extraídos
        String color = "";
        String fila = "";
        String columna = "";

        // Verifica que el formato del token sea "Square.Color(...)"
        if (tokenEspecial.startsWith("Square.Color(") && tokenEspecial.endsWith(")")) {
            // Extrae el contenido entre "Square.Color(" y ")"
            String contenido = tokenEspecial.substring("Square.Color(".length(), tokenEspecial.length() - 1);

            // Divide el contenido en partes usando la coma como delimitador
            String[] partes = contenido.split(",");

            if (partes.length == 1) {
                // Caso en el que solo se especifica el color
                if (partes[0].startsWith("#") && partes[0].length() == 7) {
                    // Verifica que el color esté en formato hexadecimal (#RRGGBB)
                    color = partes[0];
                } else {
                    // Si el color no está en formato hexadecimal, lanza una excepción
                    throw new IllegalArgumentException("Formato de color hexadecimal inválido: " + partes[0]);
                }
            } else if (partes.length == 3) {
                // Caso en el que se especifican color, fila y columna
                if (partes[0].startsWith("#") && partes[0].length() == 7) {
                    // Verifica que el color esté en formato hexadecimal
                    color = partes[0];
                    fila = partes[1].trim(); // Extrae y limpia la fila
                    columna = partes[2].trim(); // Extrae y limpia la columna
                } else {
                    // Si el color no está en formato hexadecimal, lanza una excepción
                    throw new IllegalArgumentException("Formato de color hexadecimal inválido: " + partes[0]);
                }
            } else {
                // Si el formato del token no es el esperado, lanza una excepción
                throw new IllegalArgumentException("Formato de token especial inválido: " + tokenEspecial);
            }
        } else {
            // Si el token no comienza con "Square.Color(" o no termina con ")", lanza una excepción
            throw new IllegalArgumentException("Formato de token especial inválido: " + tokenEspecial);
        }

        // Retorna un arreglo con el color, fila y columna extraídos
        return new String[]{color, fila, columna};
    }

    // Métodos para obtener los colores de los operadores
    private String getColorOperador(String operador) {
        switch (operador) {
            case "+":
                return "#FF33FF";
            case "-":
                return "#C19A6B";
            case "^":
                return "#FCD0B4";
            case "/":
                return "#B4D941";
            case "Mod":
                return "#D9AB41";
            case "*":
                return "#D80073";
            default:
                return "#000000";
        }
    }

    private String getRelacionalesComparación(String operador) {
        switch (operador) {
            case "==":
                return "#6A00FF";
            case "<>":
                return "#3F2212";
            case ">":
                return "#D9D441";
            case "<":
                return "#D94A41";
            case ">=":
                return "#E3C800";
            case "<=":
                return "#F0A30A";
            default:
                return "#000000";
        }
    }

    private String getSimbolo(String simbolo) {
        return switch (simbolo) {
            case "(" ->
                "#9AD8DB";
            case ")" ->
                "#9AD8DB";
            case "{" ->
                "#DBD29A";
            case "}" ->
                "#DBD29A";
            case "[" ->
                "#DBA49A";
            case "]" ->
                "#DBA49A";
            case "," ->
                "#B79ADB";
            case "." ->
                "#9ADBA6";
            default ->
                "#000000";
        };
    }

    private String getLogico(String operador) {
        switch (operador) {
            case "And":
                return "#414ED9";
            case "Or":
                return "#41D95D";
            case "Not":
                return "#A741D9";
            default:
                return "#000000";
        }
    }

    private boolean EsComentario(String palabra) {
        return palabra.startsWith("'");
    }

    // Método para obtener los tokens procesados
    public Token[] getTokens() {
        return tokens;
    }
}
