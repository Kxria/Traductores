import java.util.*;

public class Parser {
    private final List<Token> tokens;
    private int index = 0;
    private final Translator translator;
    private final StringBuilder output = new StringBuilder();
    private final SymbolTable symbolTable = new SymbolTable();
    private BaseScope currentScope;

    private final Map<String, String> declaredVars = new HashMap<>();

    public Parser(List<Token> tokens, Translator translator) {
        this.tokens = tokens;
        this.translator = translator;
        this.currentScope = symbolTable.getGlobalScope();
    }

    private Token peek() {
        return tokens.get(index);
    }

    private Token consume() {
        return tokens.get(index++);
    }

    private boolean check(Token.Type type) {
        return peek().type == type;
    }

    private boolean match(Token.Type type) {
        if (check(type)) {
            consume();
            return true;
        }
        return false;
    }

    private Token expect(Token.Type type) {
        if (!check(type)) {
            throw new RuntimeException("Error sintactico en linea " + peek().line
                    + ": se esperaba " + type + " pero se encontro " + peek().type
                    + " (\"" + peek().data + "\")");
        }
        return consume();
    }

    public String parse() {
        parsePrograma();
        return output.toString();
    }

    private void parsePrograma() {
        expect(Token.Type.INICIO_PROGRAMA);
        output.append(translator.initProgram());
        translator.increaseIndent();

        parseEnunciados();

        expect(Token.Type.FIN_PROGRAMA);
        translator.decreaseIndent();
        output.append(translator.endProgram());
    }

    private void parseEnunciados() {
        while (!check(Token.Type.FIN_PROGRAMA)
                && !check(Token.Type.FIN_SI)
                && !check(Token.Type.SINO)
                && !check(Token.Type.FIN_MIENTRAS)
                && !check(Token.Type.FIN_FUNCION)
                && !check(Token.Type.EOF)) {
            parseEnunciado();
        }
    }

    private void parseEnunciado() {
        Token t = peek();
        switch (t.type) {
            case ENTERO:
            case REAL:
            case CADENA:
            case BOOLEANO:
                parseDeclaracion();
                break;

            case IDENTIFICADOR:
                if (index + 1 < tokens.size() && tokens.get(index + 1).type == Token.Type.IGUAL) {
                    parseAsignacion();
                } else {
                    parseLlamadaFuncion();
                }
                break;

            case LEER:
                parseLeer();
                break;

            case ESCRIBIR:
                parseEscribir();
                break;

            case SI:
                parseSi();
                break;

            case MIENTRAS:
                parseMientras();
                break;

            case FUNCION:
                parseFuncion();
                break;

            case RETORNAR:
                parseRetornar();
                break;

            default:
                consume();
                break;
        }
    }

    private void parseDeclaracion() {
        Token typeToken = consume();
        String type = typeToken.data;
        Token nameToken = expect(Token.Type.IDENTIFICADOR);
        String name = nameToken.data;

        VariableSymbol vs = new VariableSymbol(name, type);
        symbolTable.defineVariable(vs, (BaseScope) currentScope);
        declaredVars.put(name, type);

        output.append(translator.declareVar(type, name));
    }

    private void parseAsignacion() {
        Token nameToken = expect(Token.Type.IDENTIFICADOR);
        expect(Token.Type.IGUAL);
        String expr = parseExpresion();

        output.append(translator.assign(nameToken.data, expr));
    }

    private void parseLeer() {
        expect(Token.Type.LEER);
        String prompt = "";
        if (check(Token.Type.LITERAL_CADENA)) {
            prompt = consume().data;
            match(Token.Type.COMA);
        }
        Token varToken = expect(Token.Type.IDENTIFICADOR);

        output.append(translator.readVar(prompt, varToken.data));
    }

    private void parseEscribir() {
        expect(Token.Type.ESCRIBIR);
        List<String> parts = new ArrayList<>();
        parts.add(parseExpresionEscribir());

        while (match(Token.Type.COMA)) {
            parts.add(parseExpresionEscribir());
        }
    
        String expr = String.join(" + \" \" + ", parts);
        output.append(translator.writeExpr(expr));
    }

    private String parseExpresionEscribir() {
        if (check(Token.Type.LITERAL_CADENA)) {
            return "\"" + consume().data + "\"";
        }

        return parseExpresion();
    }

    private void parseSi() {
        expect(Token.Type.SI);
        expect(Token.Type.LPAREN);
        String cond = parseCondicion();
        expect(Token.Type.RPAREN);
        expect(Token.Type.ENTONCES);

        output.append(translator.ifStart(cond));

        parseEnunciados();

        if (match(Token.Type.SINO)) {
            output.append(translator.elseClause());
            parseEnunciados();
        }

        expect(Token.Type.FIN_SI);
        output.append(translator.ifEnd());
    }

    private void parseMientras() {
        expect(Token.Type.MIENTRAS);
        expect(Token.Type.LPAREN);
        String cond = parseCondicion();
        expect(Token.Type.RPAREN);

        output.append(translator.whileStart(cond));

        parseEnunciados();

        expect(Token.Type.FIN_MIENTRAS);

        output.append(translator.whileEnd());
    }

    private void parseFuncion() {
        expect(Token.Type.FUNCION);
        String returnType = "void";

        if (check(Token.Type.ENTERO) || check(Token.Type.REAL)
                || check(Token.Type.CADENA) || check(Token.Type.BOOLEANO)) {
            returnType = consume().data;
        }

        Token nameToken = expect(Token.Type.IDENTIFICADOR);
        expect(Token.Type.LPAREN);

        StringBuilder params = new StringBuilder();
        while (!check(Token.Type.RPAREN) && !check(Token.Type.EOF)) {
            String pType = "";

            if (check(Token.Type.ENTERO) || check(Token.Type.REAL)
                    || check(Token.Type.CADENA) || check(Token.Type.BOOLEANO)) {
                pType = consume().data + " ";
            }

            Token pName = expect(Token.Type.IDENTIFICADOR);
            params.append(pType).append(pName.data);
            if (!match(Token.Type.COMA))
                break;

            params.append(", ");
        }

        expect(Token.Type.RPAREN);

        output.append(translator.funcStart(nameToken.data, returnType, params.toString()));

        parseEnunciados();

        expect(Token.Type.FIN_FUNCION);
        output.append(translator.funcEnd());
    }

    private void parseRetornar() {
        expect(Token.Type.RETORNAR);
        String expr = parseExpresion();

        output.append(translator.returnStmt(expr));
    }

    private void parseLlamadaFuncion() {
        Token nameToken = expect(Token.Type.IDENTIFICADOR);
        expect(Token.Type.LPAREN);
        StringBuilder args = new StringBuilder();
        if (!check(Token.Type.RPAREN)) {
            args.append(parseExpresion());
            while (match(Token.Type.COMA)) {
                args.append(", ").append(parseExpresion());
            }
        }
        expect(Token.Type.RPAREN);

        output.append(translator.funcCall(nameToken.data, args.toString()));
    }

    private String parseCondicion() {
        String left = parseExpresion();
        Token op = peek();
        switch (op.type) {
            case IGUAL_IGUAL:
            case DIFERENTE:
            case MENOR:
            case MAYOR:
            case MENOR_IGUAL:
            case MAYOR_IGUAL:
                consume();
                String right = parseExpresion();
                return left + " " + op.data + " " + right;
            default:
                return left;
        }
    }

    private String parseExpresion() {
        String result = parseTermino();
        while (check(Token.Type.MAS) || check(Token.Type.MENOS)) {
            String op = consume().data;
            result = result + " " + op + " " + parseTermino();
        }
        return result;
    }

    private String parseTermino() {
        String result = parseFactor();
        while (check(Token.Type.MULT) || check(Token.Type.DIV) || check(Token.Type.MOD)) {
            String op = consume().data;
            result = result + " " + op + " " + parseFactor();
        }
        return result;
    }

    private String parseFactor() {
        Token t = peek();
        switch (t.type) {
            case NUMERO_ENTERO:
            case NUMERO_REAL:
                consume();
                return t.data;

            case LITERAL_CADENA:
                consume();
                return "\"" + t.data + "\"";

            case VERDADERO:
                consume();
                return "true";

            case FALSO:
                consume();
                return "false";

            case IDENTIFICADOR:
                consume();
                if (check(Token.Type.LPAREN)) {
                    consume();
                    StringBuilder args = new StringBuilder();

                    if (!check(Token.Type.RPAREN)) {
                        args.append(parseExpresion());
                        while (match(Token.Type.COMA))
                            args.append(", ").append(parseExpresion());
                    }
                    expect(Token.Type.RPAREN);
                    return t.data + "(" + args + ")";
                }
                return t.data;

            case LPAREN:
                consume();
                String expr = parseExpresion();
                expect(Token.Type.RPAREN);
                return "(" + expr + ")";

            case MENOS:
                consume();
                return "-" + parseFactor();
                
            default:
                return "";
        }
    }
}
