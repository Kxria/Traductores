import java.util.ArrayList;

public class PseudoInterprete {
    private final ArrayList<Tupla> tuplas;
    private final TablaSimbolos tablaSimbolos;

    public PseudoInterprete(ArrayList<Tupla> tuplas, TablaSimbolos ts) {
        this.tuplas = tuplas;
        this.tablaSimbolos = ts;
    }

    public void ejecutar() {
        int pc = 0;

        while (pc >= 0 && pc < tuplas.size()) {
            Tupla t = tuplas.get(pc);
            int siguiente = t.ejecutar(tablaSimbolos);

            if (siguiente == -1) {
                break;
            }

            pc = siguiente;
        }
    }
}