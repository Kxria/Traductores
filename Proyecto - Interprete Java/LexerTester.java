public class LexerTester {
    public static void main(String[] args) {
        // Code test
        String test = 
            "// Coment\n" +
            "int x = 10;\n" +
            "double pi = 3.1416;\n" +
            "String mensaje = \"Hello Word!\";\n" +
            "\n" +
            "while (x > 0) {\n" +
            "    if (x == 5) {\n" +
            "        System.out.println(mensaje);\n" +
            "    } else {\n" +
            "        x = x - 1;\n" +
            "    }\n" +
            "}\n";

        System.out.println("--- Analisis Lexico ---");
        System.out.println(test);
        System.out.println("---------------------------------");

        // init lexer
        Lexer lexer = new Lexer(test);
        
        try {
            // tokenize code
            java.util.List<Token> listaTokens = lexer.tokenize();

            // print generated tokens
            System.out.println("\n--- Tokens ---");
            for (Token t : listaTokens) {
                System.out.println(t);
            }
            System.out.println("------------------------");
            
        } catch (Exception e) {
            System.err.println("Error al tokenizar." + e.getMessage());
            e.printStackTrace();
        }
    }
}