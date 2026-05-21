public abstract class Tupla {
    protected int saltoVerdadero;
    protected int saltoFalso;

    public Tupla(int sv, int sf) {
        this.saltoVerdadero = sv;
        this.saltoFalso = sf;
    }

    public void setSaltoVerdadero(int sv) {
        this.saltoVerdadero = sv;
    }

    public int getSaltoVerdadero() {
        return saltoVerdadero;
    }

    public void setSaltoFalso(int sf) {
        this.saltoFalso = sf;
    }

    public int getSaltoFalso() {
        return saltoFalso;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ", sv=" + saltoVerdadero + ", sf=" + saltoFalso;
    }

    public abstract int ejecutar(TablaSimbolos ts);
}