package br.app.portela.screenmatch.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.portela.screenmatch.dto.SerieDto;
import br.app.portela.screenmatch.model.Serie;
import br.app.portela.screenmatch.repository.SerieRepository;
import java.util.stream.Collectors;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository repositorio;

    @GetMapping("/series")
    public List<SerieDto> obterSeries() {
        return repositorio.findAll()
        .stream()
        .map(s -> new SerieDto(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()))
        .collect(Collectors.toList());
    }
}
