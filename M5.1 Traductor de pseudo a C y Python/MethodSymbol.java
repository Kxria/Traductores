public class MethodSymbol extends Symbol {
    public MethodSymbol(String name, String returnType) {
        super(name, returnType);
    }

    @Override
    public String toString() {
        return "MethodSymbol(name='" + name + "', returnType='" + type + "')";
    }
}
