package br.app.portela.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer temporadas,
                         @JsonAlias("imdbRating") String avaliacao) {
}
