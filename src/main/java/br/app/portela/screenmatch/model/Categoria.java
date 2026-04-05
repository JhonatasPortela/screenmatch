package br.app.portela.screenmatch.model;

public enum Categoria {
    ACAO("Action"),
    DRAMA("Drama"),
    COMEDIA("Comedy"),
    CRIME("Crime"),
    ROMANCE("Romance"),
    TERROR("Horror"),
    SUSPENSE("Thriller");

    private final String categoriaOmdb;

    Categoria(String categoriaOmdb) {
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria inválida: " + text);
    }
}