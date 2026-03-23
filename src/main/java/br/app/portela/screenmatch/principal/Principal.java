package br.app.portela.screenmatch.principal;

import br.app.portela.screenmatch.model.DadosEpisodio;
import br.app.portela.screenmatch.model.DadosSerie;
import br.app.portela.screenmatch.model.DadosTemporada;
import br.app.portela.screenmatch.model.Episodio;
import br.app.portela.screenmatch.service.ConsumoApi;
import br.app.portela.screenmatch.service.ConverteDados;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.temporada(), d))
                )
                .collect(Collectors.toList());

        System.out.println("A partir de que ano quer fazer a busca?");
        int ano = scanner.nextInt();
        scanner.nextLine();

        episodios = episodios.stream()
                .filter(e -> e.getDataDeLancamento() != null && e.getDataDeLancamento().isAfter(LocalDate.of(ano, 1, 1)))
                .collect(Collectors.toList());

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("\nEpisódios lançados a partir de " + ano);
        episodios.forEach(e -> System.out.println(e.getDataDeLancamento().format(formatador) + " - " + e.getNomeEpisodio()));

    }
}