import java.util.List;

public class Main {
    public static void main(String[] args) {
        String code = 
            "int contador;\n" +
            "double pi;\n" +
            "String mensaje;\n" +
            "contador = 1;\n" +
            "pi = 3.1416;\n" +
            "mensaje = \"La cantidad de ciclos son: \";\n" +
            "while (contador <= 3) {\n" +
            "    System.out.println(mensaje + contador);\n" +
            "    contador = contador + 1;\n" +
            "}\n" +
            "System.out.println(\"Valor final de pi: \" + pi);\n";

        String code2 = 
            "int numero;\n" +
            "String ciclo;\n" +
            "double promedio;\n" +
            "numero = 5;\n" +
            "ciclo = \"Ciclo \";\n" +                                                                                                                                                                                   
            "promedio = 0;\n" +
            "for (int i = 1; i <= numero; i++) {\n" +
            "    promedio = promedio + i;\n" +
            "    System.out.println(ciclo + i);\n" +
            "}\n" +
            "promedio = promedio / numero;\n" +
            "System.out.println(\"Promedio calculado: \" + promedio);\n" +
            "if (promedio > 3) {\n" +
            "    System.out.println(\"El promedio es mayor que 3\");\n" +
            "} else {\n" +
            "    System.out.println(\"El promedio es menor o igual a 3\");\n" +
            "}\n";

        try {
            System.out.println("============ Analisis lexico ============");
            Lexer lexer = new Lexer(code2);
            List<Token> tokens = lexer.tokenize();
            
            System.out.println("============ Parser ============");
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

            System.out.println("\n============ Tabla de símbolos ============");
            for (Variable v : ts.getSimbolos()) {
                System.out.println("  " + v);
            }

            System.out.println("\n============ Ejecucion del programa ============");
            PseudoInterprete interprete = new PseudoInterprete(parser.getTuplas(), ts);
            interprete.ejecutar();
            
        } catch (Exception e) {
            System.err.println("\nError durante el proceso: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
