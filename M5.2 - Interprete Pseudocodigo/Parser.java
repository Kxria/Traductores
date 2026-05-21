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

    public ArrayList<Tupla> getTuplas() {
        return tuplas;
    }

    private Token peek() {
        return tokens.get(index);
    }

    private Token consume() {
        return tokens.get(index++);
    }

    private boolean check(Token.Type t) {
        return peek().type == t;
    }

    private boolean match(Token.Type t) {
        if (check(t)) {
            consume();
            return true;
        }
        return false;
    }

    private Token expect(Token.Type t) {
        if (!check(t))
            throw new RuntimeException(
                    "Error sintactico en linea " + peek().line
                            + ": se esperaba " + t + " pero se encontro '" + peek().data + "'");
        return consume();
    }

    private int siguiente() {
        return tuplas.size();
    }

    public void analizar() {
        expect(Token.Type.INICIO_PROGRAMA);
        parseEnunciados();
        expect(Token.Type.FIN_PROGRAMA);
        tuplas.add(new TuplaFin());
    }

    private void parseEnunciados() {
        while (!check(Token.Type.FIN_PROGRAMA)
                && !check(Token.Type.FIN_SI)
                && !check(Token.Type.SINO)
                && !check(Token.Type.FIN_MIENTRAS)
                && !check(Token.Type.EOF)) {
            parseEnunciado();
        }
    }

    private void parseEnunciado() {
        switch (peek().type) {
            case LEER:
                parseLeer();
                break;

            case ESCRIBIR:
                parseEscribir();
                break;

            case MIENTRAS:
                parseMientras();
                break;

            case SI:
                parseSi();
                break;

            case IDENTIFICADOR:
                parseAsignacion();
                break;

            case ENTERO:
            case REAL:
            case CADENA:
            
            case BOOLEANO:
                parseDeclaracion();
                break;

            default:
                consume();
                break;
        }
    }

    private void parseDeclaracion() {
        String tipo = consume().data;
        String nombre = expect(Token.Type.IDENTIFICADOR).data;
        Variable v = new Variable(nombre, tipo);
        tablaSimbolos.definir(v);
        int idx = siguiente();
        tuplas.add(new TuplaAsignacion(nombre, "0", idx + 1, idx + 1));
    }

    private void parseLeer() {
        expect(Token.Type.LEER);
        String prompt = "";
        if (check(Token.Type.LITERAL_CADENA)) {
            prompt = consume().data;
            match(Token.Type.COMA);
        }
        String nombreVar = expect(Token.Type.IDENTIFICADOR).data;
        tablaSimbolos.resolver(nombreVar);
        int idx = siguiente();
        tuplas.add(new TuplaLeer(nombreVar, prompt, idx + 1, idx + 1));
    }

    private void parseEscribir() {
        expect(Token.Type.ESCRIBIR);
        int idx = siguiente();

        if (check(Token.Type.LITERAL_CADENA)) {
            String cadena = consume().data;
            if (match(Token.Type.COMA)) {
                String nombreVar = expect(Token.Type.IDENTIFICADOR).data;
                tablaSimbolos.resolver(nombreVar);
                tuplas.add(new TuplaEscribir(cadena, nombreVar, idx + 1, idx + 1));
            } else {
                tuplas.add(new TuplaEscribir(cadena, idx + 1, idx + 1));
            }
        } else {
            String nombreVar = expect(Token.Type.IDENTIFICADOR).data;
            tuplas.add(new TuplaEscribir(idx + 1, idx + 1, nombreVar));
        }
    }

    private void parseAsignacion() {
        String varDestino = expect(Token.Type.IDENTIFICADOR).data;
        expect(Token.Type.IGUAL);
        tablaSimbolos.resolver(varDestino);

        String op1 = parseOperando();

        if (check(Token.Type.MAS) || check(Token.Type.MENOS)
                || check(Token.Type.MULT) || check(Token.Type.DIV)
                || check(Token.Type.MOD)) {
            String operador = consume().data;
            String op2 = parseOperando();
            int idx = siguiente();
            tuplas.add(new TuplaAsignacion(varDestino, op1, operador, op2, idx + 1, idx + 1));
        } else {
            int idx = siguiente();
            tuplas.add(new TuplaAsignacion(varDestino, op1, idx + 1, idx + 1));
        }
    }

    private String parseOperando() {
        if (check(Token.Type.NUMERO_ENTERO) || check(Token.Type.NUMERO_REAL)) {
            return consume().data;
        } else if (check(Token.Type.IDENTIFICADOR)) {
            return consume().data;
        } else if (check(Token.Type.MENOS)) {
            consume();
            return "-" + consume().data;
        }
        return "0";
    }

    private void parseMientras() {
        expect(Token.Type.MIENTRAS);
        expect(Token.Type.LPAREN);

        String val1 = parseOperando();
        String op = parseOperadorComparacion();
        String val2 = parseOperando();

        expect(Token.Type.RPAREN);

        int idxComparacion = siguiente();
        TuplaComparacion comp = new TuplaComparacion(val1, op, val2, idxComparacion + 1,0);
        tuplas.add(comp);

        parseEnunciados();
        expect(Token.Type.FIN_MIENTRAS);

        int idxSaltoRegreso = siguiente();
        tuplas.add(new TuplaSalto(idxComparacion));
        comp.setSaltoFalso(idxSaltoRegreso + 1);
    }

    private void parseSi() {
        expect(Token.Type.SI);
        expect(Token.Type.LPAREN);

        String val1 = parseOperando();
        String op = parseOperadorComparacion();
        String val2 = parseOperando();

        expect(Token.Type.RPAREN);
        expect(Token.Type.ENTONCES);

        int idxComparacion = siguiente();
        TuplaComparacion comp = new TuplaComparacion(val1, op, val2,idxComparacion + 1,0);

        parseEnunciados();
        if (match(Token.Type.SINO)) {
            TuplaSalto saltoSobre = new TuplaSalto(0);
            tuplas.add(saltoSobre);

            comp.setSaltoFalso(siguiente());

            parseEnunciados();
            expect(Token.Type.FIN_SI);

            int despuesSino = siguiente();
            saltoSobre.setSaltoVerdadero(despuesSino);
            saltoSobre.setSaltoFalso(despuesSino);

        } else {
            expect(Token.Type.FIN_SI);
            comp.setSaltoFalso(siguiente());
        }
    }

    private String parseOperadorComparacion() {
        Token t = peek();
        switch (t.type) {
            case MENOR:
            case MAYOR:
            case MENOR_IGUAL:
            case MAYOR_IGUAL:
            case IGUAL_IGUAL:
            case DIFERENTE:
                consume();
                return t.data;
            default:
                throw new RuntimeException("Se esperaba operador de comparacion, se encontro: " + t.data);
        }
    }
}