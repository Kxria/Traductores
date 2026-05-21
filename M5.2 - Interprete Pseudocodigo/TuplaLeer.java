import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TuplaLeer extends Tupla {
    private final String nombreVariable;
    private final String prompt;

    public TuplaLeer(String nombreVariable, String prompt, int sv, int sf) {
        super(sv, sf);
        this.nombreVariable = nombreVariable;
        this.prompt = prompt;
    }

    public TuplaLeer(String nombreVariable, int sv, int sf) {
        this(nombreVariable, "", sv, sf);
    }

    @Override
    public int ejecutar(TablaSimbolos ts) {
        if (!prompt.isEmpty()) {
            System.out.print("Da un valor para " + prompt + " (" + nombreVariable + "): ");
        } else {
            System.out.print("Da un valor para " + nombreVariable + ": ");
        }

        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        String valor = "0";
        try {
            valor = entrada.readLine();
        } catch (IOException ex) {
            System.out.println("Error de lectura.");
        }

        Variable v = ts.resolver(nombreVariable);
        try {
            v.setValor(Float.parseFloat(valor.trim()));
        } catch (NumberFormatException e) {
            System.out.println("Error: Numero invalido, se asigna 0.");
            v.setValor(0);
        }

        return saltoVerdadero;
    }

    @Override
    public String toString() {
        return "( TuplaLeer, " + super.toString() + ", [ " + nombreVariable + " ] )";
    }
}