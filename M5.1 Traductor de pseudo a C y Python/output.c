#include <stdio.h>

int main(void) {
    printf("Cuantas calificaciones: ");
    scanf("%s", &n);
    prom = 0;
    i = 0;
    while (i < n) {
        printf("Da una calificacion: ");
        scanf("%s", &cal);
        prom = prom + cal;
        i = i + 1;
    }
    prom = prom / n;
    if (prom > 5) {
        printf("%s\n", "Aprobado con: " + " " + prom);
    }
    if (prom == 10) {
        printf("Excelente");
        printf("\n");
    }
return 0;
}
