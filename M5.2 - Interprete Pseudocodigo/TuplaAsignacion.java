public class TuplaAsignacion extends Tupla {
    private final String variable;
    private final String valor1;
    private final String operador;
    private final String valor2;

    public TuplaAsignacion(String variable, String valor1, int sv, int sf) {
        super(sv, sf);
        this.variable = variable;
        this.valor1 = valor1;
        this.operador = null;
        this.valor2 = null;
    }

    public TuplaAsignacion(String variable, String valor1, String operador, String valor2, int sv, int sf) {
        super(sv, sf);
        this.variable = variable;
        this.valor1 = valor1;
        this.operador = operador;
        this.valor2 = valor2;
    }

    @Override
    public int ejecutar(TablaSimbolos ts) {
        Variable v = ts.resolver(variable);
        float operando1 = resolverOperando(valor1, ts);

        if (operador == null) {
            v.setValor(operando1);
        } else {
            float operando2 = resolverOperando(valor2, ts);

            switch (operador) {
                case "+":
                    v.setValor(operando1 + operando2);
                    break;

                case "-":
                    v.setValor(operando1 - operando2);
                    break;

                case "*":
                    v.setValor(operando1 * operando2);
                    break;

                case "/":
                    if (operando2 != 0) {
                        v.setValor(operando1 / operando2);
                    } else {
                        System.out.println("Error: Division entre cero.");
                        System.exit(1);
                    }
                    break;

                case "%":
                    v.setValor(operando1 % operando2);
                    break;

                default:
                    System.out.println("Error: Operador desconocido '" + operador + "'");
            }
        }

        return saltoVerdadero;
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
        if (operador == null) {
            return "( TuplaAsignacion, " + super.toString() + ", [ " + variable + ", " + valor1 + " ] )";
        }
        return "( TuplaAsignacion, " + super.toString() + ", [ " + variable + ", " + valor1 + ", " + operador + ", " + valor2 + " ] )";
    }
}