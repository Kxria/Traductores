// Proyecto Final - Interprete de lenguaje de programacion Java
// Traductores - 361
// 2200357 - Chaparro Herrera Hugo Giovanni
// 2200073 - Rivera Vazquez Hugo Alexis

public class TuplaSalto extends Tupla {
    public TuplaSalto(int destino) {
        super(destino, destino);
    }

    @Override
    public int ejecutar(TablaSimbolos ts) {
        return saltoVerdadero;
    }

    @Override
    public String toString() {
        return "( TuplaSalto, destino=" + saltoVerdadero + " )";
    }
}