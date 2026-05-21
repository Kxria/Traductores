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
        float operando1 = resolverOperando(valor1, ts);
        float operando2 = resolverOperando(valor2, ts);

        boolean resultado;

        switch (operador) {
            case "<":
                resultado = operando1 < operando2;
                break;
            case "<=":
                resultado = operando1 <= operando2;
                break;
            case ">":
                resultado = operando1 > operando2;
                break;
            case ">=":
                resultado = operando1 >= operando2;
                break;
            case "==":
                resultado = operando1 == operando2;
                break;
            case "!=":
                resultado = operando1 != operando2;
                break;
            default:
                System.out.println("Error: Operador de comparacion desconocido '" + operador + "'");
                resultado = false;
        }

        return resultado ? saltoVerdadero : saltoFalso;
    }

    private float resolverOperando(String operando, TablaSimbolos ts) {
        try {
            return Float.parseFloat(operando);
        } catch (NumberFormatException e) {
            return ts.resolver(operando).getValor();
        }
    }

    @Override
    public String toString() {
        return "( TuplaComparacion, " + super.toString() + ", [ " + valor1 + ", " + operador + ", " + valor2 + " ] )";
    }
}