import java.util.Objects;

public class Symbol {
    protected String name;
    protected String type;

    public Symbol(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Symbol(String name) {
        this(name, null);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String t) {
        this.type = t;
    }

    @Override
    public String toString() {
        return "Symbol(name='" + name + "', type='" + type + "')";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Symbol))
            return false;
        Symbol s = (Symbol) o;
        return Objects.equals(name, s.name) && Objects.equals(type, s.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
