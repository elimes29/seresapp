package com.aluracursos.sceenmatch.principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EjemploStreams {

    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Brenda","Luis", "María Fernanda", "Erick", "Genesis");
        nombres.stream()
                .sorted()
                .limit(4)
                .filter(n -> n.startsWith("E"))
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);

    }
}
