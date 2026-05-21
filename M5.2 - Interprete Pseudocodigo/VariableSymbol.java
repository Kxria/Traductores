public class VariableSymbol extends Symbol {
    public VariableSymbol(String name, String type) {
        super(name, type);
    }

    public VariableSymbol(String name) {
        super(name, null);
    }

    @Override
    public String toString() {
        return "VariableSymbol(name='" + name + "', type='" + type + "')";
    }

    private float valor = 0;
    public void setValor(float v) { this.valor = v; }
    public float getValor() { return valor; }
}
