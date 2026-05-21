public class Variable {
    private final String nombre;
    private String tipo;
    private float valor = 0;

    public Variable(String nombre) {
        this.nombre = nombre;
        this.tipo = "real";
    }

    public Variable(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Variable(nombre='" + nombre + "', tipo='" + tipo + "', valor=" + valor + ")";
    }
}