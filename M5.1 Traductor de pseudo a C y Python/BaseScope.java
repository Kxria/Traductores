import java.util.HashMap;
import java.util.Map;

public class BaseScope implements Scope {
    protected String name;
    protected BaseScope enclosing;
    protected Map<String, Symbol> symbols = new HashMap<>();

    public BaseScope(String name, BaseScope enclosing) {
        this.name = name;
        this.enclosing = enclosing;
    }

    @Override
    public void define(Symbol sym) {
        symbols.put(sym.getName(), sym);
    }

    @Override
    public Symbol resolve(String name) {
        if (symbols.containsKey(name))
            return symbols.get(name);
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
        return name;
    }

    @Override
    public String toString() {
        return "<BaseScope " + name + " symbols=" + symbols.keySet() + ">";
    }
}
