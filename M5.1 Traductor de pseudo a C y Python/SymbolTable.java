import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private GlobalScope global = new GlobalScope();
    private Map<String, ScopedSymbol> typeSymbols = new HashMap<>();
    private Map<String, BuiltInTypeSymbol> builtIns = new HashMap<>();

    public SymbolTable() {
        registerBuiltIn(new BuiltInTypeSymbol("int"));
        registerBuiltIn(new BuiltInTypeSymbol("float"));
        registerBuiltIn(new BuiltInTypeSymbol("void"));
    }

    public GlobalScope getGlobalScope() {
        return global;
    }

    public void registerBuiltIn(BuiltInTypeSymbol t) {
        builtIns.put(t.getName(), t);
        global.define(t);
    }

    public void defineStruct(StructSymbol s) {
        typeSymbols.put(s.getScopeName(), s);
        global.define(s);
    }

    public void defineClass(ClassSymbol c) {
        typeSymbols.put(c.getScopeName(), c);
        global.define(c);
    }

    public void defineMethod(MethodSymbol m) {
        global.define(m);
    }

    public void defineVariable(VariableSymbol v, BaseScope scope) {
        BaseScope target = (scope != null) ? scope : global;
        target.define(v);
    }

    public VariableSymbol resolveVariable(String name, BaseScope scope) {
        BaseScope target = (scope != null) ? scope : global;
        Symbol s = target.resolve(name);
        if (s instanceof VariableSymbol)
            return (VariableSymbol) s;

        return null;
    }

    public ScopedSymbol lookupType(String name) {
        return typeSymbols.get(name);
    }

    public BuiltInTypeSymbol lookupBuiltIn(String name) {
        return builtIns.get(name);
    }

    public Symbol resolveMemberAccess(String typeName, String memberName) {
        ScopedSymbol ts = lookupType(typeName);
        if (ts == null) {
            System.out.println("[resolveMemberAccess] tipo '" + typeName + "' no registrado.");
            return null;
        }
        if (ts.members.containsKey(memberName))
            return ts.members.get(memberName);

        System.out.println("[resolveMemberAccess] miembro '" + memberName + "' no encontrado en '" + typeName + "'");
        return null;
    }

    public void printGlobals() {
        System.out.println("globals: " + global.getScopeName() + ":" + global + "\n");
    }

    public void printScope(String title, BaseScope scope) {
        System.out.println(title + ": " + scope);
    }
}
