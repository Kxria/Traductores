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
}
