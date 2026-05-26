// Proyecto Final - Interprete de lenguaje de programacion Java
// Traductores - 361
// 2200357 - Chaparro Herrera Hugo Giovanni
// 2200073 - Rivera Vazquez Hugo Alexis

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    private final Map<String, Variable> tabla = new HashMap<>();

    public void definir(Variable v) {
        if (tabla.containsKey(v.getNombre())) {
            throw new RuntimeException("Error Semantico: La variable '" + v.getNombre() + "' ya ha sido definida.");
        }
        tabla.put(v.getNombre(), v);
    }

    public Variable resolver(String nombre) {
        if (!tabla.containsKey(nombre)) {
            throw new RuntimeException("Error Semantico: La variable '" + nombre + "' no ha sido declarada.");
        }
        return tabla.get(nombre);
    }

    public ArrayList<Variable> getSimbolos() {
        return new ArrayList<>(tabla.values());
    }
}