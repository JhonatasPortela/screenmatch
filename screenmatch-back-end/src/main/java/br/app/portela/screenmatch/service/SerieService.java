package br.app.portela.screenmatch.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        .map(s -> new SerieDto(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()) )
        .collect(Collectors.toList());
    }

    public List<SerieDto> obterTop5Lancamentos(){
        return  converterParaDto(repositorio.encontrarEpisodiosMaisRecentes());
    }


    public SerieDto obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDto(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse());
        }
        return null;
    }
}
