import java.util.*;

public class Lexer {
    private final String input;
    private int pos = 0;
    private int line = 1;

    private static final Map<String, Token.Type> KEYWORDS = new LinkedHashMap<>();
    static {
        KEYWORDS.put("inicio-programa", Token.Type.INICIO_PROGRAMA);
        KEYWORDS.put("fin-programa", Token.Type.FIN_PROGRAMA);
        KEYWORDS.put("fin-mientras", Token.Type.FIN_MIENTRAS);
        KEYWORDS.put("fin-funcion", Token.Type.FIN_FUNCION);
        KEYWORDS.put("fin-si", Token.Type.FIN_SI);
        KEYWORDS.put("leer", Token.Type.LEER);
        KEYWORDS.put("escribir", Token.Type.ESCRIBIR);
        KEYWORDS.put("si", Token.Type.SI);
        KEYWORDS.put("entonces", Token.Type.ENTONCES);
        KEYWORDS.put("sino", Token.Type.SINO);
        KEYWORDS.put("mientras", Token.Type.MIENTRAS);
        KEYWORDS.put("funcion", Token.Type.FUNCION);
        KEYWORDS.put("retornar", Token.Type.RETORNAR);
        KEYWORDS.put("entero", Token.Type.ENTERO);
        KEYWORDS.put("real", Token.Type.REAL);
        KEYWORDS.put("cadena", Token.Type.CADENA);
        KEYWORDS.put("booleano", Token.Type.BOOLEANO);
        KEYWORDS.put("verdadero", Token.Type.VERDADERO);
        KEYWORDS.put("falso", Token.Type.FALSO);
        KEYWORDS.put("y", Token.Type.AND);
        KEYWORDS.put("o", Token.Type.OR);
        KEYWORDS.put("no", Token.Type.NOT);
        KEYWORDS.put("mod", Token.Type.MOD);
    }

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            skipWhitespaceAndComments();
            if (pos >= input.length())
                break;

            char c = input.charAt(pos);

            if (c == '\n') {
                line++;
                pos++;
                continue;
            }
            if (c == '\r') {
                pos++;
                continue;
            }

            if (Character.isDigit(c)) {
                tokens.add(readNumber());
                continue;
            }

            if (c == '"') {
                tokens.add(readString());
                continue;
            }

            if (Character.isLetter(c) || c == '_') {
                tokens.add(readIdentifierOrKeyword());
                continue;
            }

            Token op = readOperator();
            if (op != null) {
                tokens.add(op);
                continue;
            }

            pos++;
        }

        tokens.add(new Token(Token.Type.EOF, "", line));
        return tokens;
    }

    private void skipWhitespaceAndComments() {
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (c == ' ' || c == '\t') {
                pos++;
            } else if (c == '/' && pos + 1 < input.length() && input.charAt(pos + 1) == '/') {
                while (pos < input.length() && input.charAt(pos) != '\n')
                    pos++;
            } else {
                break;
            }
        }
    }

    private Token readNumber() {
        int start = pos;
        boolean isReal = false;
        while (pos < input.length() && Character.isDigit(input.charAt(pos)))
            pos++;
        if (pos < input.length() && input.charAt(pos) == '.') {
            isReal = true;
            pos++;
            while (pos < input.length() && Character.isDigit(input.charAt(pos)))
                pos++;
        }
        String val = input.substring(start, pos);
        return new Token(isReal ? Token.Type.NUMERO_REAL : Token.Type.NUMERO_ENTERO, val, line);
    }

    private Token readString() {
        pos++;
        int start = pos;

        while (pos < input.length() && input.charAt(pos) != '"') {
            if (input.charAt(pos) == '\n')
                line++;
            pos++;
        }

        String val = input.substring(start, pos);

        if (pos < input.length())
            pos++;

        return new Token(Token.Type.LITERAL_CADENA, val, line);
    }

    private Token readIdentifierOrKeyword() {
        int start = pos;

        while (pos < input.length()) {
            char c = input.charAt(pos);

            if (Character.isLetterOrDigit(c) || c == '_') {
                pos++;
            } else if (c == '-' && pos + 1 < input.length() && Character.isLetter(input.charAt(pos + 1))) {
                int savedPos = pos;
                pos++;
                int wordStart = pos;

                while (pos < input.length() && Character.isLetter(input.charAt(pos)))
                    pos++;

                String candidate = input.substring(start, pos).toLowerCase();

                if (!KEYWORDS.containsKey(candidate)) {
                    pos = savedPos;
                    break;
                }
            } else {
                break;
            }
        }
        String raw = input.substring(start, pos);
        String lower = raw.toLowerCase();
        Token.Type kwType = KEYWORDS.get(lower);

        if (kwType != null)
            return new Token(kwType, lower, line);

        return new Token(Token.Type.IDENTIFICADOR, raw, line);
    }

    private Token readOperator() {
        char c = input.charAt(pos);
        char next = (pos + 1 < input.length()) ? input.charAt(pos + 1) : '\0';

        switch (c) {
            case '=':
                if (next == '=') {
                    pos += 2;
                    return new Token(Token.Type.IGUAL_IGUAL, "==", line);
                }
                pos++;
                return new Token(Token.Type.IGUAL, "=", line);

            case '!':
                if (next == '=') {
                    pos += 2;
                    return new Token(Token.Type.DIFERENTE, "!=", line);
                }
                break;

            case '<':
                if (next == '=') {
                    pos += 2;
                    return new Token(Token.Type.MENOR_IGUAL, "<=", line);
                }
                pos++;
                return new Token(Token.Type.MENOR, "<", line);

            case '>':
                if (next == '=') {
                    pos += 2;
                    return new Token(Token.Type.MAYOR_IGUAL, ">=", line);
                }
                pos++;
                return new Token(Token.Type.MAYOR, ">", line);

            case '+':
                pos++;
                return new Token(Token.Type.MAS, "+", line);

            case '-':
                pos++;
                return new Token(Token.Type.MENOS, "-", line);

            case '*':
                pos++;
                return new Token(Token.Type.MULT, "*", line);

            case '/':
                pos++;
                return new Token(Token.Type.DIV, "/", line);

            case '%':
                pos++;
                return new Token(Token.Type.MOD, "%", line);

            case '(':
                pos++;
                return new Token(Token.Type.LPAREN, "(", line);

            case ')':
                pos++;
                return new Token(Token.Type.RPAREN, ")", line);

            case '[':
                pos++;
                return new Token(Token.Type.LBRACKET, "[", line);

            case ']':
                pos++;
                return new Token(Token.Type.RBRACKET, "]", line);

            case ',':
                pos++;
                return new Token(Token.Type.COMA, ",", line);

            case ';':
                pos++;
                return new Token(Token.Type.PUNTO_COMA, ";", line);

            case '.':
                pos++;

                return new Token(Token.Type.PUNTO, ".", line);
        }

        return null;
    }
}
