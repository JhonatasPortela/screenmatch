package br.app.portela.screenmatch.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.app.portela.screenmatch.dto.EpisodioDto;
import br.app.portela.screenmatch.model.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.app.portela.screenmatch.dto.SerieDto;
import br.app.portela.screenmatch.repository.SerieRepository;
import br.app.portela.screenmatch.model.Serie;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repositorio;

    public List<SerieDto> obterSeries() {
        return converterParaDto(repositorio.findAll());
    }

    public List<SerieDto> obterTop5SeriesMaisAvaliadas() {
        return converterParaDto(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    private List<SerieDto> converterParaDto(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDto(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(),
                        s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDto> obterTop5Lancamentos() {
        return converterParaDto(repositorio.encontrarEpisodiosMaisRecentes());
    }

    public SerieDto obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDto(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(),
                    s.getAtores(), s.getPoster(), s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDto> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDto(e.getNumeroTemporada(), e.getNomeEpisodio(), e.getNumeroEpisodio()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDto> obterEpisodiosPorTemporada(Long id, Long numero) {
        return repositorio.obterEpisodiosPorTemporada(id, numero).stream()
                .map(e -> new EpisodioDto(e.getNumeroTemporada(), e.getNomeEpisodio(), e.getNumeroEpisodio()))
                .collect(Collectors.toList());
    }

    public List<SerieDto> obterSeriesPorGenero(String genero) {
        Categoria categoria = Categoria.fromPortugues(genero);
        return converterParaDto(repositorio.findByGenero(categoria));
    }

    public List<EpisodioDto> obterTop5EpisodiosDaSerie(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return repositorio.filtrarTop5EpisodiosPorSerie(s).stream()
                    .map(e -> new EpisodioDto(e.getNumeroTemporada(), e.getNomeEpisodio(), e.getNumeroEpisodio()))
                    .collect(Collectors.toList());
        }
        return null;
    }

}
