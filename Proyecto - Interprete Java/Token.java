public class Token {
    public enum Type {
        // Keywords existentes
        INT, DOUBLE, STRING,
        IF, ELSE, WHILE,
        SYSTEM_OUT_PRINTLN,
        FOR, 
        INCREMENTO,

        PUBLIC, CLASS, STATIC, VOID, MAIN,

        // Literales / Ids
        IDENTIFICADOR,
        NUMERO_ENTERO, 
        NUMERO_REAL, 
        LITERAL_CADENA,

        // Operadores
        IGUAL,          // =
        MAS,            // +
        MENOS,          // -
        MULT,           // *
        DIV,            // /
        MOD,            // %

        // Condicionales
        IGUAL_IGUAL,    // ==
        DIFERENTE,      // !=
        MENOR,          // <
        MAYOR,          // >
        MENOR_IGUAL,    // <=
        MAYOR_IGUAL,    // >=

        // Delimitadores
        LPAREN, RPAREN, // ( )
        LBRACE, RBRACE, // { }
        PUNTO_COMA,     // ;
        COMA,           // ,

        LBRACKET, RBRACKET, // [ ]

        // EOF
        EOF
    }

    public final Type type;
    public final String data;
    public final int line;

    public Token(Type type, String data, int line) {
        this.type = type;
        this.data = data;
        this.line = line;
    }

    @Override
    public String toString() {
        return "Token(" + type + ", \"" + data + "\", línea = " + line + ")";
    }
}