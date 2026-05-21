public class LocalScope extends BaseScope {
    public LocalScope(String name, BaseScope enclosing) {
        super(name, enclosing);
    }

    public LocalScope(BaseScope enclosing) {
        super("local", enclosing);
    }

    @Override
    public String toString() {
        return "<LocalScope " + name + " symbols=" + symbols.keySet() + ">";
    }
}
