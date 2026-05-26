public class TuplaComparacion extends Tupla {
    private final String valor1;
    private final String operador;
    private final String valor2;

    public TuplaComparacion(String valor1, String operador, String valor2, int sv, int sf) {
        super(sv, sf);
        this.valor1 = valor1;
        this.operador = operador;
        this.valor2 = valor2;
    }

    @Override
    public int ejecutar(TablaSimbolos ts) {
        Object op1 = resolverOperando(valor1, ts);
        Object op2 = resolverOperando(valor2, ts);
        boolean resultado = false;

        if (op1 instanceof Number && op2 instanceof Number) {
            double n1 = ((Number) op1).doubleValue();
            double n2 = ((Number) op2).doubleValue();
            switch (operador) {
                case "<":  resultado = n1 < n2; break;
                case "<=": resultado = n1 <= n2; break;
                case ">":  resultado = n1 > n2; break;
                case ">=": resultado = n1 >= n2; break;
                case "==": resultado = n1 == n2; break;
                case "!=": resultado = n1 != n2; break;
            }
        } else {
            // String compare
            String s1 = String.valueOf(op1);
            String s2 = String.valueOf(op2);
            switch (operador) {
                case "==": resultado = s1.equals(s2); break;
                case "!=": resultado = !s1.equals(s2); break;
                default: throw new RuntimeException("Error Semántico: Operador '" + operador + "' inválido para tipos String.");
            }
        }

        return resultado ? saltoVerdadero : saltoFalso;
    }

    @Override
    public String toString() {
        return "( TuplaComparacion, " + super.toString() + ", [ " + valor1 + " " + operador + " " + valor2 + " ] )";
    }
}