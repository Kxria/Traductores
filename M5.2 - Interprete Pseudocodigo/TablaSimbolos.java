import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    private final Map<String, Variable> tabla = new HashMap<>();

    public void definir(Variable v) {
        tabla.put(v.getNombre(), v);
    }

    public Variable resolver(String nombre) {
        if (!tabla.containsKey(nombre)) {
            Variable nueva = new Variable(nombre);
            tabla.put(nombre, nueva);
        }

        return tabla.get(nombre);
    }

    public ArrayList<Variable> getSimbolos() {
        return new ArrayList<>(tabla.values());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Variable v : tabla.values()) {
            sb.append("  ").append(v).append("\n");
        }

        return sb.toString();
    }
}