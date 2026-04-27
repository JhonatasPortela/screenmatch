package br.app.portela.screenmatch.model;

import java.text.Normalizer;

public enum Categoria {
    ACAO("Action", "Ação"),
    DRAMA("Drama", "Drama"),
    COMEDIA("Comedy", "Comédia"),
    CRIME("Crime", "Crime"),
    ROMANCE("Romance", "Romance"),
    TERROR("Horror", "Terror"),
    SUSPENSE("Thriller", "Suspense");

    private final String categoriaOmdb;
    private final String categoriaEmPortugues;


    Categoria(String categoriaOmdb, String categoriaEmPortugues) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEmPortugues = categoriaEmPortugues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria inválida: " + text);
    }

    public static Categoria fromPortugues(String text) {
        var normalizado = normalizar(text);
        for (Categoria categoria : Categoria.values()) {
            if (normalizar(categoria.categoriaEmPortugues).equalsIgnoreCase(normalizado)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria inválida: " + text);
    }

    private static String normalizar(String valor) {
        if (valor == null) return "";
        var trimmed = valor.trim();
        var semAcentos = Normalizer.normalize(trimmed, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcentos;
    }
}