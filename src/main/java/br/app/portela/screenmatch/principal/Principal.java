package br.app.portela.screenmatch.principal;

import br.app.portela.screenmatch.model.DadosEpisodio;
import br.app.portela.screenmatch.model.DadosSerie;
import br.app.portela.screenmatch.model.DadosTemporada;
import br.app.portela.screenmatch.service.ConsumoApi;
import br.app.portela.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    Scanner scanner = new Scanner(System.in);
    private final String ENDERECO_COM_API = "https://www.omdbapi.com/?apikey=92a14529&t=";
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {

        System.out.println("Digite o nome da série:");
        String nomeSerie = scanner.nextLine();
        String json = consumoApi.obterDados(ENDERECO_COM_API + nomeSerie.replace(" ", "+"));
        DadosSerie serie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(serie);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= serie.totalTemporadas(); i++) {
            json = consumoApi.obterDados(ENDERECO_COM_API + nomeSerie.replace(" ", "+") + "&season=" + i);
            DadosTemporada temporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(temporada);
        }

        temporadas.forEach(System.out::println);

       // temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\n top 5");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);
    }
}