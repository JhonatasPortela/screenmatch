package br.app.portela.screenmatch.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.app.portela.screenmatch.model.*;
import br.app.portela.screenmatch.repository.SerieRepository;
import br.app.portela.screenmatch.service.ConsumoApi;
import br.app.portela.screenmatch.service.ConverteDados;

public class Principal {
    Scanner scanner = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?apikey=";
    private final String API = System.getenv("APIKEY_OMDB");
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados conversor = new ConverteDados();
    List<DadosSerie> dadosSeries = new ArrayList<>();
    private final SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    --------------------------------
                    Bem-vindo ao ScreenMatch!
                    --------------------------------
                    Escolha o número da opção:
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Exibir séries buscadas
                    4 - Buscar série por nome
                    5 - Buscar séries por ator
                    6 - Buscar Top 5 séries
                    7 - Buscar série por categoria
                    8 - Buscar séries por quantidade de temporada e avaliações
                    9 - Buscar episódio por trecho
                    10 - Top 5 episódios por série
                    11 - Buscar episódios lançados após data selecionada

                    0 - Sair
                    --------------------------------
                    """;

            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    exibirSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorNome();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 8:
                    buscarSeriesPorTemporadasEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    top5EpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodioPosData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");

            }
        }
    }




    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
        // dadosSeries.add(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série:");
        var nomeSerie = scanner.nextLine();
        var json = consumoApi.obterDados(ENDERECO + API + nomeSerie.replace(" ", "+"));
        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie() {
        exibirSeriesBuscadas();
        System.out.println("Digite o nome da série a ser buscada:");
        var nomeSerie = scanner.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie.trim());

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi
                        .obterDados(ENDERECO + API + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i);
                var dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }

            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.temporada(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        } else {
            System.out.println("Série não encontrada.");
        }
    }

    private void exibirSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorNome() {
        System.out.println("Digite o nome da série a ser buscada:");
        var nomeSerie = scanner.nextLine();
        serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie.trim());

        if (serieBuscada.isPresent()){
            System.out.println("Série encontrada: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void buscarSeriesPorAtor(){
        System.out.println("Digite o nome do ator para fazer a busca: ");
        var nomeAtor = scanner.nextLine();
        System.out.println("Qual a avaliação para filtrar?");
        var avaliacao = scanner.nextDouble();

        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor.trim(), avaliacao);
        seriesEncontradas.stream()
                .forEach(s -> System.out.println(s.getTitulo() + " Avaliação: " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByAvaliacaoDesc();

        topSeries.stream()
                .forEach(s -> System.out.println(s.getTitulo() + " Avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorCategoria() {
        System.out.println("Quer buscar por qual gênero/categoria?");
        var nomeCategoria = scanner.nextLine().trim();
        Categoria categoria = Categoria.fromPortugues(nomeCategoria);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        seriesPorCategoria.stream()
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTemporadasEAvaliacao() {
        System.out.println("Quer séries com até quantas temporadas?");
        var maximoTemporadas = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Qual o mínimo de avaliação a série deve ter?");
        var avaliacaoMinima = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("******* Séies filtradas *********");
        List<Serie> seriesFiltradas = repositorio.filtrarPorTemporadaEAvaliacao(maximoTemporadas, avaliacaoMinima);
        seriesFiltradas.stream()
                .forEach(System.out::println);
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite o trecho do episódio para fazer a busca: ");
        var trechoEpisodio = scanner.nextLine();

        List<Episodio> episodiosComTrecho = repositorio.buscaEpisodioPorTrecho(trechoEpisodio);
        episodiosComTrecho.stream()
                .forEach(e -> System.out.printf("Série: %s - Temporada: %d - Episódio: %d : %s%n", e.getSerie().getTitulo(), e.getNumeroTemporada(), e.getNumeroEpisodio(), e.getNomeEpisodio()));

    }

    private void top5EpisodiosPorSerie() {
        buscarSeriePorNome();

        if (serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topCincoEpisodios = repositorio.filtrarTop5EpisodiosPorSerie(serie);
            topCincoEpisodios.forEach(e -> System.out.printf("Série: %s - Temporada: %d - Episódio: %d : %s - Avaliação: %.2f%n", e.getSerie().getTitulo(), e.getNumeroTemporada(), e.getNumeroEpisodio(), e.getNomeEpisodio(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodioPosData() {
        buscarSeriePorNome();

        if (serieBuscada.isPresent()) {
            System.out.println("Quer buscar episódios lançados a partir de qual ano?");
            int ano = scanner.nextInt();
            scanner.nextLine();
            Serie serie = serieBuscada.get();
            List<Episodio> episodiosPosAno = repositorio.buscarEpisodiosPosAno(serie, ano);
            episodiosPosAno.forEach(System.out::println);
        }
    }

    /*
     * temporadas.forEach(t -> t.episodios().forEach(e ->
     * System.out.println(e.titulo())));
     * 
     * List<DadosEpisodio> dadosEpisodios = temporadas.stream()
     * .flatMap(t -> t.episodios().stream())
     * .collect(Collectors.toList());
     * 
     * System.out.println("\n top 5");
     * dadosEpisodios.stream()
     * .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
     * .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
     * .limit(5)
     * .forEach(System.out::println);
     * 
     * List<Episodio> episodios = temporadas.stream()
     * .flatMap(t -> t.episodios().stream()
     * .map(d -> new Episodio(t.temporada(), d))
     * )
     * .collect(Collectors.toList());
     * 
     * System.out.println("Digite o trecho do episodio: ");
     * String trecho = scanner.nextLine();
     * 
     * Optional<Episodio> episodioEncontrado = episodios.stream()
     * .filter(e ->
     * e.getNomeEpisodio().toLowerCase().contains(trecho.toLowerCase()))
     * .findFirst();
     * 
     * if (episodioEncontrado.isPresent()) {
     * System.out.println("Episodio encontrado\n: " + "Temporada " +
     * episodioEncontrado.get().getNumeroTemporada() + ", Episódio " +
     * episodioEncontrado.get().getNomeEpisodio());
     * } else {
     * System.out.println("Episódio não encontrado.");
     * }
     * 
     * System.out.println("A partir de que ano quer fazer a busca?");
     * int ano = scanner.nextInt();
     * scanner.nextLine();
     * 
     * episodios = episodios.stream()
     * .filter(e -> e.getDataDeLancamento() != null &&
     * e.getDataDeLancamento().isAfter(LocalDate.of(ano, 1, 1)))
     * .collect(Collectors.toList());
     * 
     * DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
     * System.out.println("\nEpisódios lançados a partir de " + ano);
     * episodios.forEach(e ->
     * System.out.println(e.getDataDeLancamento().format(formatador) + " - " +
     * e.getNomeEpisodio()));
     * 
     * Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
     * .filter(e -> e.getAvaliacao() != 0.0)
     * .collect(Collectors.groupingBy(Episodio::getNumeroTemporada,
     * Collectors.averagingDouble(Episodio::getAvaliacao)));
     * 
     * System.out.println("\nAvaliações por temporada:");
     * avaliacoesPorTemporada.forEach((temporada, avaliacao) ->
     * System.out.println("Temporada " + temporada + ": " + avaliacao));
     * 
     * DoubleSummaryStatistics estatisticas = episodios.stream()
     * .filter(e -> e.getAvaliacao() > 0.0)
     * .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
     * 
     * System.out.println("\nEstatísticas das avaliações:");
     * System.out.println("Média: " + estatisticas.getAverage());
     * System.out.println("Mínimo: " + estatisticas.getMin());
     * System.out.println("Máximo: " + estatisticas.getMax());
     * System.out.println("Total: " + estatisticas.getCount());
     * }
     */

}