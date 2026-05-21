public class TuplaEscribir extends Tupla {
    private final String cadena;
    private final String nombreVar;

    public TuplaEscribir(String cadena, int sv, int sf) {
        super(sv, sf);
        this.cadena = cadena;
        this.nombreVar = null;
    }

    public TuplaEscribir(String cadena, String nombreVar, int sv, int sf) {
        super(sv, sf);
        this.cadena = cadena;
        this.nombreVar = nombreVar;
    }

    public TuplaEscribir(int sv, int sf, String nombreVar) {
        super(sv, sf);
        this.cadena = null;
        this.nombreVar = nombreVar;
    }

    @Override
    public int ejecutar(TablaSimbolos ts) {
        if (cadena == null) {
            Variable v = ts.resolver(nombreVar);
            System.out.println(formatearValor(v.getValor()));
        } else if (nombreVar == null) {
            System.out.println(cadena);
        } else {
            Variable v = ts.resolver(nombreVar);
            System.out.println(cadena + formatearValor(v.getValor()));
        }

        return saltoVerdadero;
    }

    private String formatearValor(float val) {
        if (val == (int) val) {
            return String.valueOf((int) val);
        }
        return String.valueOf(val);
    }

    @Override
    public String toString() {
        String c = cadena   != null ? "\"" + cadena + "\"" : "null";
        String v = nombreVar != null ? nombreVar : "null";
        return "( TuplaEscribir, " + super.toString() + ", [ " + c + ", " + v + " ] )";
    }
}