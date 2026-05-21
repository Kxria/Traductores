public interface Scope {
    void define(Symbol sym);

    Symbol resolve(String name);

    Scope getEnclosingScope();

    default Scope getParentScope() {
        return getEnclosingScope();
    }

    String getScopeName();
}
