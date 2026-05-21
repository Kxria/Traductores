public class Token {
    public enum Type {
        INICIO_PROGRAMA, 
        FIN_PROGRAMA,
        
        LEER, 
        ESCRIBIR,
        
        SI, 
        ENTONCES, 
        SINO, 
        FIN_SI,

        MIENTRAS, 
        FIN_MIENTRAS,

        FUNCION,
        FIN_FUNCION, 
        RETORNAR,

        ENTERO, 
        REAL, 
        CADENA, 
        BOOLEANO,

        NUMERO_ENTERO, 
        NUMERO_REAL, 
        LITERAL_CADENA,
        
        VERDADERO, 
        FALSO,

        IDENTIFICADOR,
        IGUAL,
        IGUAL_IGUAL,
        DIFERENTE,
        MENOR,
        MAYOR,
        MENOR_IGUAL,
        MAYOR_IGUAL,
        MAS, 
        MENOS, 
        MULT, 
        DIV, 
        MOD,
        AND, 
        OR, 
        NOT,
        LPAREN, 
        RPAREN,
        LBRACKET, 
        RBRACKET,
        COMA, 
        PUNTO_COMA, 
        PUNTO,

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
        return "Token(" + type + ", \"" + data + "\", línea=" + line + ")";
    }
}
