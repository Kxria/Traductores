import java.nio.file.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String entrada = Files.readString(Paths.get("test.txt"));

        Lexer lexer = new Lexer(entrada);
        List<Token> tokens = lexer.tokenize();

        TablaSimbolos ts = new TablaSimbolos();
        Parser parser = new Parser(tokens, ts);
        parser.analizar();

        System.out.println("***Tabla de simbolos***");
        
        for (Variable s : ts.getSimbolos()) 
            System.out.println(s);

        System.out.println("\n***Tuplas generadas***");
        
        for (Tupla t : parser.getTuplas()) 
            System.out.println(t);
        
        System.out.println("\n***Ejecucion del programa***");

        PseudoInterprete interprete = new PseudoInterprete(parser.getTuplas(), ts);
        interprete.ejecutar();
    }
}