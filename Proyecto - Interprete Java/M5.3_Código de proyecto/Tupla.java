public abstract class Tupla {
    protected int saltoVerdadero;
    protected int saltoFalso;

    public Tupla(int sv, int sf) {
        this.saltoVerdadero = sv;
        this.saltoFalso = sf;
    }

    public void setSaltoVerdadero(int sv) { this.saltoVerdadero = sv; }
    public int getSaltoVerdadero() { return saltoVerdadero; }
    public void setSaltoFalso(int sf) { this.saltoFalso = sf; }
    public int getSaltoFalso() { return saltoFalso; }

    protected Object resolverOperando(String operando, TablaSimbolos ts) {
        if (operando == null) return null;
        
        // Si el operando viene protegido con comillas, es un String
        if (operando.startsWith("\"") && operando.endsWith("\"")) {
            return operando.substring(1, operando.length() - 1);
        }
        
        try {
            Variable v = ts.resolver(operando);
            return v.getValor();
        } catch (RuntimeException e) {
            // Si no es variable, es un int o double
            if (operando.contains(".")) {
                return Double.parseDouble(operando);
            }
            try {
                return Integer.parseInt(operando);
            } catch (NumberFormatException nfe) {
                return operando; 
            }
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ", sv=" + saltoVerdadero + ", sf=" + saltoFalso;
    }

    public abstract int ejecutar(TablaSimbolos ts);
}