#!/usr/bin/env python3
# -*- coding: utf-8 -*-

def main():
    n = input("Cuantas calificaciones: ")
    prom = 0
    i = 0
    while i < n:
        cal = input("Da una calificacion: ")
        prom = prom + cal
        i = i + 1
    prom = prom / n
    if prom > 5:
        print("Aprobado con: " + " " + prom)
    if prom == 10:
        print("Excelente")

if __name__ == "__main__":
    main()
