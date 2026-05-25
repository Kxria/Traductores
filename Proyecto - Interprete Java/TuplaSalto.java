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