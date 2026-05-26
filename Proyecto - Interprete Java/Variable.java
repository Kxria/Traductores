// Proyecto Final - Interprete de lenguaje de programacion Java
// Traductores - 361
// 2200357 - Chaparro Herrera Hugo Giovanni
// 2200073 - Rivera Vazquez Hugo Alexis

public class Variable {
    private final String nombre;
    private final String tipo;
    private Object valor;

    public Variable(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;

        if (tipo.equals("String")) {
            this.valor = "";
        } else if (tipo.equals("double")) {
            this.valor = 0.0;
        } else { // int
            this.valor = 0;
        }
    }

    public String getNombre() {
        return nombre;
    }
    public String getTipo() {
        return tipo;
    }
    public Object getValor() {
        return valor;
    }
    
    public void setValor(Object valor) { 
        this.valor = valor; 
    }

    @Override
    public String toString() {
        return "Variable(nombre='" + nombre + "', tipo='" + tipo + "', valor=" + valor + ")";
    }
}