import java.util.HashMap;
import java.util.Map;

public class ScopedSymbol extends Symbol implements Scope {
    protected Map<String, Symbol> members = new HashMap<>();
    protected Scope enclosing;

    public ScopedSymbol(String name, Scope enclosing) {
        super(name);
        this.enclosing = enclosing;
    }

    public ScopedSymbol(String name) {
        this(name, null);
    }

    @Override
    public void define(Symbol sym) {
        members.put(sym.getName(), sym);
    }

    @Override
    public Symbol resolve(String name) {
        if (members.containsKey(name))
            return members.get(name);
        if (enclosing != null)
            return enclosing.resolve(name);
        
        return null;
    }

    @Override
    public Scope getEnclosingScope() {
        return enclosing;
    }

    @Override
    public String getScopeName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "<ScopedSymbol " + name + " members=" + members.keySet() + ">";
    }
}
