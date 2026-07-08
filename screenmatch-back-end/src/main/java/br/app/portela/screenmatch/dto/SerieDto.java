package br.app.portela.screenmatch.dto;

import br.app.portela.screenmatch.model.Categoria;
public record SerieDto(Long id, String titulo, Integer totalTemporadas, Double avaliacao, Categoria genero, String atores, String poster, String sinopse) {
    
}
