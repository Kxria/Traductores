public class code {
    public static void main(String[] args) {
        int numero;
        double promedio;

        numero = 5;
        promedio = 0;

        for (int i = 1; i <= numero; i++) {
            promedio = promedio + i;
            System.out.println("Ciclo for " + i);
        }

        promedio = promedio / numero;
        System.out.println("Promedio calculado: " + promedio);

        if (promedio > 3) {
            System.out.println("El promedio es mayor que 3");
        } else {
            System.out.println("El promedio es menor o igual a 3");
        }

        System.out.println("Hola Chiggas!");
    }
}