// Proyecto Final - Interprete de lenguaje de programacion Java
// Traductores - 361
// 2200357 - Chaparro Herrera Hugo Giovanni
// 2200073 - Rivera Vazquez Hugo Alexis

import java.util.List;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        try {
            String code = Files.readString(Paths.get("code.java"));

            System.out.println("============ Analisis lexico ============");
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.tokenize();
            for (Token token : tokens) {
                System.out.println(token);
            }
            System.out.println("Tokens detectados: " + tokens.size() + "\n");

            TablaSimbolos ts = new TablaSimbolos();
            Parser parser = new Parser(tokens, ts);
            parser.analizar();

            System.out.println("\n============ Tabla de Símbolos ============");
            for (Variable v : ts.getSimbolos()) {
                System.out.println("  " + v);
            }

            System.out.println("\n============ Codigo - Tuplas ============");
            int i = 0;
            for (Tupla t : parser.getTuplas()) {
                System.out.println("  [" + i + "] " + t);
                i++;
            }

            System.out.println("\n============ Ejecucion del programa ============");
            PseudoInterprete interprete = new PseudoInterprete(parser.getTuplas(), ts);
            interprete.ejecutar();
        } catch(Exception e) {
            System.err.println("\nError durante el proceso: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
