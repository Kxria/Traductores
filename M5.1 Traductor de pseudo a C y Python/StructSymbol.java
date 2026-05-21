public class StructSymbol extends ScopedSymbol {
    public StructSymbol(String name, Scope enclosing) {
        super(name, enclosing);
    }

    public StructSymbol(String name) {
        super(name, null);
    }

    @Override
    public String toString() {
        return "<Struct " + name + " members=" + members.keySet() + ">";
    }
}
