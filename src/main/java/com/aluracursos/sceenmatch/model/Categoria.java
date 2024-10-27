package com.aluracursos.sceenmatch.model;

public enum Categoria {
    ACCION("action", "acción"),
    ROMANCE("romance", "romance"),
    COMEDIA("comedy", "comedia"),
    DRAMA("drama", "drama"),
    CRIMEN("crime", "crimen"),
    ANIMACION("Animation", "animación");

    private String categoriaOmdb;
    private String categoriaEspanol;

    Categoria(String categoriaOmdb, String categoriaEspanol){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEspanol = categoriaEspanol;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

    public static Categoria fromEspanol(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEspanol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

}
