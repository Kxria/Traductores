public class GlobalScope extends BaseScope {
    public GlobalScope() {
        super("global", null);
    }

    @Override
    public String toString() {
        return "<GlobalScope symbols=" + symbols.keySet() + ">";
    }
}
