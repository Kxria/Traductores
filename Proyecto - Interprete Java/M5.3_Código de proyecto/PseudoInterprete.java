// Proyecto Final - Interprete de lenguaje de programacion Java
// Traductores - 361
// 2200357 - Chaparro Herrera Hugo Giovanni
// 2200073 - Rivera Vazquez Hugo Alexis

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
            int siguientePC = t.ejecutar(tablaSimbolos);

            if (siguientePC == -1) {
                break;
            }

            pc = siguientePC;
        }
    }
}