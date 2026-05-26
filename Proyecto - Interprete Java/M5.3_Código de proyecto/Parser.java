import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int index = 0;
    private final ArrayList<Tupla> tuplas = new ArrayList<>();
    private final TablaSimbolos tablaSimbolos;

    public Parser(List<Token> tokens, TablaSimbolos ts) {
        this.tokens = tokens;
        this.tablaSimbolos = ts;
    }

    public ArrayList<Tupla> getTuplas() { return tuplas; }
    private Token peek() { return tokens.get(index); }
    private Token consume() { return tokens.get(index++); }
    private boolean check(Token.Type t) { return peek().type == t; }
    
    private boolean match(Token.Type t) {
        if (check(t)) { consume(); return true; }
        return false;
    }

    private Token expect(Token.Type t) {
        if (!check(t)) {
            throw new RuntimeException("Error sintactico en línea " + peek().line 
                + ": se esperaba " + t + " pero se encontro '" + peek().data + "'");
        }
        return consume();
    }

    private int siguiente() { return tuplas.size(); }

    public void analizar() {
        if (check(Token.Type.PUBLIC) || check(Token.Type.CLASS)) {
            
            if (match(Token.Type.PUBLIC)) {
                expect(Token.Type.CLASS);
            } else {
                expect(Token.Type.CLASS);
            }
            expect(Token.Type.IDENTIFICADOR);
            expect(Token.Type.LBRACE);

            expect(Token.Type.PUBLIC);
            expect(Token.Type.STATIC);
            expect(Token.Type.VOID);
            expect(Token.Type.MAIN);
            expect(Token.Type.LPAREN);
            expect(Token.Type.STRING);
            expect(Token.Type.LBRACKET);
            expect(Token.Type.RBRACKET);
            expect(Token.Type.IDENTIFICADOR);
            expect(Token.Type.RPAREN);
            expect(Token.Type.LBRACE);

            parseEnunciados();
            expect(Token.Type.RBRACE);
            expect(Token.Type.RBRACE);
        } else {
            parseEnunciados();
        }

        expect(Token.Type.EOF);
        tuplas.add(new TuplaFin());
    }

    private void parseEnunciados() {
        while (!check(Token.Type.RBRACE) && !check(Token.Type.EOF)) {
            parseEnunciado();
        }
    }

    private void parseEnunciado() {
        switch (peek().type) {
            case INT:
            case DOUBLE:
            case STRING:
                parseDeclaracion();
                break;
            case IDENTIFICADOR:
                parseAsignacion();
                break;
            case WHILE:
                parseMientras();
                break;
            case FOR:
            parseFor();
            break;
            case IF:
                parseSi();
                break;
            case SYSTEM_OUT_PRINTLN:
                parseEscribir();
                break;
            default:
                throw new RuntimeException("Error sintáctico en línea " + peek().line 
                    + ": instrucción no reconocida '" + peek().data + "'");
        }
    }

    private void parseDeclaracion() {
        String tipo = consume().data; 
        String nombre = expect(Token.Type.IDENTIFICADOR).data;
        expect(Token.Type.PUNTO_COMA); 

        Variable v = new Variable(nombre, tipo);
        tablaSimbolos.definir(v);
        
        int idx = siguiente();
        // Asigna un valor inicial por defecto protegido para Strings
        String valorDefecto = tipo.equals("String") ? "\"\"" : "0"; 
        tuplas.add(new TuplaAsignacion(nombre, valorDefecto, idx + 1, idx + 1));
    }

    private void parseAsignacion() {
        String varDestino = expect(Token.Type.IDENTIFICADOR).data;
        expect(Token.Type.IGUAL);
        tablaSimbolos.resolver(varDestino);

        String op1 = parseOperando();

        if (check(Token.Type.MAS) || check(Token.Type.MENOS) ||
            check(Token.Type.MULT) || check(Token.Type.DIV) || check(Token.Type.MOD)) {
            String operador = consume().data;
            String op2 = parseOperando();
            expect(Token.Type.PUNTO_COMA);
            
            int idx = siguiente();
            tuplas.add(new TuplaAsignacion(varDestino, op1, operador, op2, idx + 1, idx + 1));
        } else {
            expect(Token.Type.PUNTO_COMA);
            int idx = siguiente();
            tuplas.add(new TuplaAsignacion(varDestino, op1, idx + 1, idx + 1));
        }
    }

    private String parseOperando() {
        if (check(Token.Type.LITERAL_CADENA)) {
            return "\"" + consume().data + "\"";
        }
        if (check(Token.Type.NUMERO_ENTERO) || check(Token.Type.NUMERO_REAL)) {
            return consume().data;
        } else if (check(Token.Type.IDENTIFICADOR)) {
            String nombreVar = consume().data;
            tablaSimbolos.resolver(nombreVar);
            return nombreVar;
        } else if (match(Token.Type.MENOS)) {
            return "-" + expect(Token.Type.NUMERO_ENTERO).data;
        }
        throw new RuntimeException("Error sintactico en linea " + peek().line + ": Operando invalido '" + peek().data + "'");
    }

    private void parseMientras() {
        expect(Token.Type.WHILE);
        expect(Token.Type.LPAREN);
        String val1 = parseOperando();
        String op = parseOperadorComparacion();
        String val2 = parseOperando();
        expect(Token.Type.RPAREN);

        int idxComparacion = siguiente();
        TuplaComparacion comp = new TuplaComparacion(val1, op, val2, idxComparacion + 1, 0);
        tuplas.add(comp);

        expect(Token.Type.LBRACE);
        parseEnunciados();
        expect(Token.Type.RBRACE);

        int idxSaltoRegreso = siguiente();
        tuplas.add(new TuplaSalto(idxComparacion));
        comp.setSaltoFalso(idxSaltoRegreso + 1);
    }

    private void parseFor() {
        expect(Token.Type.FOR);
        expect(Token.Type.LPAREN);

        if (check(Token.Type.INT) || check(Token.Type.DOUBLE)) {
            String tipo = consume().data;
            String nombre = expect(Token.Type.IDENTIFICADOR).data;
            expect(Token.Type.IGUAL);
            String valorInicial = parseOperando();
            expect(Token.Type.PUNTO_COMA);
            
            Variable v = new Variable(nombre, tipo);
            tablaSimbolos.definir(v);
            int idx = siguiente();
            tuplas.add(new TuplaAsignacion(nombre, valorInicial, idx + 1, idx + 1));
        } else {
            String nombre = expect(Token.Type.IDENTIFICADOR).data;
            tablaSimbolos.resolver(nombre);
            expect(Token.Type.IGUAL);
            String valorInicial = parseOperando();
            expect(Token.Type.PUNTO_COMA);
            
            int idx = siguiente();
            tuplas.add(new TuplaAsignacion(nombre, valorInicial, idx + 1, idx + 1));
        }

        int idxCondicion = siguiente();
        String val1 = parseOperando();
        String op = parseOperadorComparacion();
        String val2 = parseOperando();
        expect(Token.Type.PUNTO_COMA);

        // Creamos la tupla de comparacion
        TuplaComparacion comp = new TuplaComparacion(val1, op, val2, idxCondicion + 1, 0);
        tuplas.add(comp);

        String varIncremento = expect(Token.Type.IDENTIFICADOR).data;
        tablaSimbolos.resolver(varIncremento); // Validar existencia
        
        Tupla tuplaIncremento;
        if (match(Token.Type.INCREMENTO)) { // Si es i++
            tuplaIncremento = new TuplaAsignacion(varIncremento, varIncremento, "+", "1", idxCondicion, idxCondicion);
        } else { // Si es i = i + 1
            expect(Token.Type.IGUAL);
            String op1 = parseOperando();
            String operador = consume().data;
            String op2 = parseOperando();
            tuplaIncremento = new TuplaAsignacion(varIncremento, op1, operador, op2, idxCondicion, idxCondicion);
        }
        
        expect(Token.Type.RPAREN);
        expect(Token.Type.LBRACE);

        parseEnunciados();
        expect(Token.Type.RBRACE);

        // Anadimos la tupla del incremento al final de todas las instrucciones del cuerpo
        tuplas.add(tuplaIncremento); 
        
        // El índice de salida del ciclo es la posición inmediatamente posterior al incremento
        int idxSalir = siguiente();
        comp.setSaltoFalso(idxSalir);
    }

    private void parseSi() {
        expect(Token.Type.IF);
        expect(Token.Type.LPAREN);
        String val1 = parseOperando();
        String op = parseOperadorComparacion();
        String val2 = parseOperando();
        expect(Token.Type.RPAREN);

        int idxComparacion = siguiente();
        TuplaComparacion comp = new TuplaComparacion(val1, op, val2, idxComparacion + 1, 0);
        tuplas.add(comp);

        expect(Token.Type.LBRACE);
        parseEnunciados();
        expect(Token.Type.RBRACE);

        if (match(Token.Type.ELSE)) {
            TuplaSalto saltoSobre = new TuplaSalto(0);
            tuplas.add(saltoSobre);
            comp.setSaltoFalso(siguiente());

            expect(Token.Type.LBRACE);
            parseEnunciados();
            expect(Token.Type.RBRACE);

            int despuesSino = siguiente();
            saltoSobre.setSaltoVerdadero(despuesSino);
            saltoSobre.setSaltoFalso(despuesSino);
        } else {
            comp.setSaltoFalso(siguiente());
        }
    }

    private void parseEscribir() {
        expect(Token.Type.SYSTEM_OUT_PRINTLN);
        expect(Token.Type.LPAREN);
        int idx = siguiente();

        // Leemos el primer operando
        String op1 = parseOperando();
        
        // Si el siguiente token es un '+', hay una concatenacion
        if (match(Token.Type.MAS)) { 
            String op2 = parseOperando(); // Leemos el segundo operando
            tuplas.add(new TuplaEscribir(op1, op2, idx + 1, idx + 1));
        } else {
            // Impresión simple de un solo elemento
            tuplas.add(new TuplaEscribir(op1, idx + 1, idx + 1));
        }

        expect(Token.Type.RPAREN);
        expect(Token.Type.PUNTO_COMA);
    }

    private String parseOperadorComparacion() {
        Token t = peek();
        switch (t.type) {
            case MENOR: case MAYOR: case MENOR_IGUAL: case MAYOR_IGUAL: case IGUAL_IGUAL: case DIFERENTE:
                consume();
                return t.data;
            default:
                throw new RuntimeException("Se esperaba un operador de comparacion en linea " + t.line + ", se encontro: " + t.data);
        }
    }
}