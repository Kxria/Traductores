public class TuplaEscribir extends Tupla {
    private final String operando1;
    private final String operando2;

    // Constructor para un solo elemento (System.out.println(contador))
    public TuplaEscribir(String operando1, int sv, int sf) {
        super(sv, sf);
        this.operando1 = operando1;
        this.operando2 = null;
    }

    // Constructor para concatenación (System.out.println(mensaje + contador)
    public TuplaEscribir(String operando1, String operando2, int sv, int sf) {
        super(sv, sf);
        this.operando1 = operando1;
        this.operando2 = operando2;
    }

    @Override
    public int ejecutar(TablaSimbolos ts) {
        Object op1 = resolverOperando(operando1, ts);
        
        if (operando2 == null) {
            System.out.println(op1);
        } else {
            Object op2 = resolverOperando(operando2, ts);
            System.out.println(String.valueOf(op1) + String.valueOf(op2));
        }
        return saltoVerdadero;
    }

    @Override
    public String toString() {
        String expresion = operando1 + (operando2 != null ? " + " + operando2 : "");
        return "( TuplaEscribir, " + super.toString() + ", [ " + expresion + " ] )";
    }
}