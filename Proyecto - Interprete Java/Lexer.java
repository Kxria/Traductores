import java.util.*;

public class Lexer {
    private final String input;
    private int pos = 0;
    private int line = 1;

    private static final Map<String, Token.Type> KEYWORDS = new LinkedHashMap<>();
    static {
        // Keyword assign
        KEYWORDS.put("int", Token.Type.INT);
        KEYWORDS.put("double", Token.Type.DOUBLE);
        KEYWORDS.put("String", Token.Type.STRING);
        KEYWORDS.put("if", Token.Type.IF);
        KEYWORDS.put("else", Token.Type.ELSE);
        KEYWORDS.put("while", Token.Type.WHILE);
        KEYWORDS.put("for", Token.Type.FOR);
    }

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            skipWhitespaceAndComments();
            
            if (pos >= input.length()) {
                break;
            }

            char c = input.charAt(pos);
            
            // NL / ret
            if (c == '\n') {
                line++;
                pos++;
                continue;
            }
            if (c == '\r') {
                pos++;
                continue;
            }

            // System.out.println
            if (input.startsWith("System.out.println", pos)) {
                tokens.add(new Token(Token.Type.SYSTEM_OUT_PRINTLN, "System.out.println", line));
                pos += "System.out.println".length();
                continue;
            }

            // int / double
            if (Character.isDigit(c)) {
                tokens.add(readNumber());
                continue;
            }

            // String
            if (c == '"') {
                tokens.add(readString());
                continue;
            }

            // Ids / Keywords
            if (Character.isLetter(c) || c == '_') {
                tokens.add(readIdentifierOrKeyword());
                continue;
            }

            // Delimitadores
            Token op = readOperator();
            if (op != null) {
                tokens.add(op);
                continue;
            }

            System.err.println("Error en linea: " + line + ": caracter invalido '" + c + "'");
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
            } // Comentarios
            else if (c == '/' && pos + 1 < input.length() && input.charAt(pos + 1) == '/') {
                while (pos < input.length() && input.charAt(pos) != '\n') {
                    pos++;
                }
            } else {
                break;
            }
        }
    }

    private Token readNumber() {
        int start = pos;
        boolean isReal = false;

        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            pos++;
        }

        // Real == double
        if (pos < input.length() && input.charAt(pos) == '.') {
            isReal = true;
            pos++;

            while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                pos++;
            }
        }

        String val = input.substring(start, pos);
        return new Token(isReal ? Token.Type.NUMERO_REAL : Token.Type.NUMERO_ENTERO, val, line);
    }

    private Token readString() {
        pos++;
        int start = pos;

        while (pos < input.length() && input.charAt(pos) != '"') {
            if (input.charAt(pos) == '\n') {
                line++;
            }
            pos++;
        }

        String val = input.substring(start, pos);
        if (pos < input.length()) {
            pos++;
        }

        return new Token(Token.Type.LITERAL_CADENA, val, line);
    }

    private Token readIdentifierOrKeyword() {
        int start = pos;

        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (Character.isLetterOrDigit(c) || c == '_') {
                pos++;
            } else {
                break;
            }
        }

        String raw = input.substring(start, pos);
        
        // case sensitive
        Token.Type kwType = KEYWORDS.get(raw);

        if (kwType != null) {
            return new Token(kwType, raw, line);
        }
        return new Token(Token.Type.IDENTIFICADOR, raw, line);
    }

    private Token readOperator() {
        char c = input.charAt(pos);
        char next = (pos + 1 < input.length()) ? input.charAt(pos + 1) : '\0';

        switch (c) {
            case '=':
                if (next == '=') { pos += 2; return new Token(Token.Type.IGUAL_IGUAL, "==", line); }
                pos++; return new Token(Token.Type.IGUAL, "=", line);

            case '!':
                if (next == '=') { pos += 2; return new Token(Token.Type.DIFERENTE, "!=", line); }
                break;

            case '<':
                if (next == '=') { pos += 2; return new Token(Token.Type.MENOR_IGUAL, "<=", line); }
                pos++; return new Token(Token.Type.MENOR, "<", line);

            case '>':
                if (next == '=') { pos += 2; return new Token(Token.Type.MAYOR_IGUAL, ">=", line); }
                pos++; return new Token(Token.Type.MAYOR, ">", line);

            case '+':
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '+') {
                    pos += 2;
                    return new Token(Token.Type.INCREMENTO, "++", line);
                }
                pos++;
                return new Token(Token.Type.MAS, "+", line);

            case '-': pos++; return new Token(Token.Type.MENOS, "-", line);
            case '*': pos++; return new Token(Token.Type.MULT, "*", line);
            case '/': pos++; return new Token(Token.Type.DIV, "/", line);
            case '%': pos++; return new Token(Token.Type.MOD, "%", line);
            case '(': pos++; return new Token(Token.Type.LPAREN, "(", line);
            case ')': pos++; return new Token(Token.Type.RPAREN, ")", line);
            case '{': pos++; return new Token(Token.Type.LBRACE, "{", line);
            case '}': pos++; return new Token(Token.Type.RBRACE, "}", line);
            case ',': pos++; return new Token(Token.Type.COMA, ",", line);
            case ';': pos++; return new Token(Token.Type.PUNTO_COMA, ";", line);
        }
        return null;
    }
}