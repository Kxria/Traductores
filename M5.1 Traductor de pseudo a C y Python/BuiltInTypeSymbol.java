public class BuiltInTypeSymbol extends Symbol {
    public BuiltInTypeSymbol(String name) {
        super(name, null);
    }

    @Override
    public String toString() {
        return "<BuiltInType " + name + ">";
    }
}
