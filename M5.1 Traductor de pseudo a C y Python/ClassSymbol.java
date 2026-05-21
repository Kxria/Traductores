public class ClassSymbol extends ScopedSymbol {
    private ClassSymbol superClass;

    public ClassSymbol(String name, Scope enclosing, ClassSymbol superClass) {
        super(name, enclosing);
        this.superClass = superClass;
    }

    public ClassSymbol(String name, ClassSymbol superClass) {
        super(name, null);
        this.superClass = superClass;
    }

    public ClassSymbol(String name) {
        super(name, null);
    }

    public void setSuperClass(ClassSymbol sup) {
        this.superClass = sup;
    }

    public ClassSymbol getSuperClass() {
        return superClass;
    }

    @Override
    public Scope getParentScope() {
        return (superClass != null) ? superClass : super.getEnclosingScope();
    }

    @Override
    public String toString() {
        String sup = (superClass != null) ? superClass.getScopeName() : "null";
        return "<Class " + name + " extends=" + sup + " members=" + members.keySet() + ">";
    }
}
