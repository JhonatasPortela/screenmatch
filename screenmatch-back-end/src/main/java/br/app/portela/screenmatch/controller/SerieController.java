package br.app.portela.screenmatch.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.portela.screenmatch.dto.SerieDto;
import br.app.portela.screenmatch.repository.SerieRepository;

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

    @GetMapping("/inicio")
    public String paginaInicial() {
        return "TESTE DE PÁGINA INICIAL";
    }
}
