package br.app.portela.screenmatch.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.portela.screenmatch.dto.SerieDto;
import br.app.portela.screenmatch.service.SerieService;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDto> obterSeries() {
        return service.obterSeries();
    }

    @GetMapping("/top5")
    public List<SerieDto> obterTop5SeriesMaisAvaliadas() {
        return service.obterTop5SeriesMaisAvaliadas();
    }

    @GetMapping("/lancamentos")
    public List<SerieDto> obterTop5Lancamentos(){
        return service.obterTop5Lancamentos();
    }

    @GetMapping("/{id}")
    public SerieDto obterPorId(@PathVariable Long id){
        return service.obterPorId(id);
    }
}
