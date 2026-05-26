// Proyecto Final - Interprete de lenguaje de programacion Java
// Traductores - 361
// 2200357 - Chaparro Herrera Hugo Giovanni
// 2200073 - Rivera Vazquez Hugo Alexis

public class TuplaFin extends Tupla {
    public TuplaFin() {
        super(-1, -1);
    }

    @Override
    public int ejecutar(TablaSimbolos ts) {
        return -1;
    }

    @Override
    public String toString() {
        return "( TuplaFin, [ Fin del Programa ] )";
    }
}