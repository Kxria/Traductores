import java.io.*;
import java.nio.file.*;
import java.util.List;

/*
 * Para lenguaje C --> javac src/*.java && java -cp src Main test.txt c
 * Para python ------> javac src/*.java && java -cp src Main test.txt python
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String source;
        String targetLang;

        if (args.length >= 2) {
            source = new String(Files.readAllBytes(Paths.get(args[0])));
            targetLang = args[1].toLowerCase();
        } else {
            source = "inicio-programa\n"
                    + "  leer \"Cuantas calificaciones\", n\n"
                    + "  prom = 0\n"
                    + "  i = 0\n"
                    + "  mientras (i < n)\n"
                    + "    leer \"Da una calificacion\", cal\n"
                    + "    prom = prom + cal\n"
                    + "    i = i + 1\n"
                    + "  fin-mientras\n"
                    + "  prom = prom / n\n"
                    + "  si (prom > 5) entonces\n"
                    + "    escribir \"Aprobado con: \", prom\n"
                    + "  fin-si\n"
                    + "  si (prom == 10) entonces\n"
                    + "    escribir \"Excelente\"\n"
                    + "  fin-si\n"
                    + "fin-programa\n";
            targetLang = (args.length == 1) ? args[0].toLowerCase() : "ambos";
        }

        if (targetLang.equals("ambos") || targetLang.equals("all")) {
            translateAndPrint(source, "c");
            System.out.println("\n" + "=".repeat(60) + "\n");
            translateAndPrint(source, "python");
        } else {
            translateAndPrint(source, targetLang);
        }
    }

    private static void translateAndPrint(String source, String lang) {
        System.out.println("+════════════════════════════════════════════════+");
        System.out.print("| Traduccion a: " + lang.toUpperCase());
        System.out.println("|\n+════════════════════════════════════════════════+");

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        Translator translator;
        String extension;
        if (lang.equals("python") || lang.equals("py")) {
            translator = new PythonTranslator();
            extension = ".py";
        } else {
            translator = new CTranslator();
            extension = ".c";
        }

        Parser parser = new Parser(tokens, translator);
        String result = parser.parse();

        System.out.println(result);

        try {
            String outFile = "output" + extension;
            Files.write(Paths.get(outFile), result.getBytes());
            System.out.println("[ Archivo generado: " + outFile + " ]");
        } catch (IOException e) {
            System.err.println("No se pudo guardar: " + e.getMessage());
        }
    }
}
