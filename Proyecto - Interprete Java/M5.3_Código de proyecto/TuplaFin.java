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