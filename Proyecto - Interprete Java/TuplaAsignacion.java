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
        Object op1 = resolverOperando(valor1, ts);

        if (operador == null) {
            v.setValor(op1);
        } else {
            Object op2 = resolverOperando(valor2, ts);

            // Gestor de operadores
            if (operador.equals("+") && (op1 instanceof String || op2 instanceof String)) {
                v.setValor(String.valueOf(op1) + String.valueOf(op2));
            } else if (op1 instanceof Double || op2 instanceof Double) {
                // Aritmetica de double
                double n1 = ((Number) op1).doubleValue();
                double n2 = ((Number) op2).doubleValue();
                switch (operador) {
                    case "+": v.setValor(n1 + n2); break;
                    case "-": v.setValor(n1 - n2); break;
                    case "*": v.setValor(n1 * n2); break;
                    case "/": 
                        if (n2 == 0) throw new RuntimeException("Error en ejecucion: Division entre cero.");
                        v.setValor(n1 / n2); 
                        break;
                    case "%": v.setValor(n1 % n2); break;
                }
            } else {
                // Aritmetica de int
                int n1 = ((Number) op1).intValue();
                int n2 = ((Number) op2).intValue();
                switch (operador) {
                    case "+": v.setValor(n1 + n2); break;
                    case "-": v.setValor(n1 - n2); break;
                    case "*": v.setValor(n1 * n2); break;
                    case "/": 
                        if (n2 == 0) throw new RuntimeException("Error en ejecucion: Division entera entre cero.");
                        v.setValor(n1 / n2); 
                        break;
                    case "%": v.setValor(n1 % n2); break;
                }
            }
        }
        return saltoVerdadero;
    }

    @Override
    public String toString() {
        if (operador == null) {
            return "( TuplaAsignacion, " + super.toString() + ", [ " + variable + " = " + valor1 + " ] )";
        }
        return "( TuplaAsignacion, " + super.toString() + ", [ " + variable + " = " + valor1 + " " + operador + " " + valor2 + " ] )";
    }
}