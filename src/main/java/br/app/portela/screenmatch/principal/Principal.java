package br.app.portela.screenmatch.principal;

import br.app.portela.screenmatch.model.DadosSerie;
import br.app.portela.screenmatch.model.DadosTemporada;
import br.app.portela.screenmatch.model.Serie;
import br.app.portela.screenmatch.service.ConsumoApi;
import br.app.portela.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {
    Scanner scanner = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?apikey=";
    private final String API = "92a14529&t=";
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados conversor = new ConverteDados();
    List<DadosSerie> dadosSeries = new ArrayList<>();

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
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");

            }
        }
    }

    private void buscarSerieWeb(){
        DadosSerie dados = getDadosSerie();
        System.out.println(dados);
        dadosSeries.add(dados);
    }

    private DadosSerie getDadosSerie(){
        System.out.println("Digite o nome da série:");
        var nomeSerie = scanner.nextLine();
        var json = consumoApi.obterDados(ENDERECO + API + nomeSerie.replace(" ", "+"));
        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie(){
        DadosSerie dados = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            var json = consumoApi.obterDados(ENDERECO + API + dados.titulo().replace(" ", "+") + "&season=" + i);
            var temporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(temporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void exibirSeriesBuscadas(){
        List<Serie> series = new ArrayList<>();
        series = dadosSeries.stream()
                .map(d -> new Serie(d))
                .toList();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

        /* temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

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

       System.out.println("Digite o trecho do episodio: ");
       String trecho = scanner.nextLine();

        Optional<Episodio> episodioEncontrado = episodios.stream()
                .filter(e -> e.getNomeEpisodio().toLowerCase().contains(trecho.toLowerCase()))
                .findFirst();

        if (episodioEncontrado.isPresent()) {
            System.out.println("Episodio encontrado\n: " + "Temporada " + episodioEncontrado.get().getNumeroTemporada() + ", Episódio " + episodioEncontrado.get().getNomeEpisodio());
        } else {
            System.out.println("Episódio não encontrado.");
        }

        System.out.println("A partir de que ano quer fazer a busca?");
        int ano = scanner.nextInt();
        scanner.nextLine();

        episodios = episodios.stream()
                .filter(e -> e.getDataDeLancamento() != null && e.getDataDeLancamento().isAfter(LocalDate.of(ano, 1, 1)))
                .collect(Collectors.toList());

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("\nEpisódios lançados a partir de " + ano);
        episodios.forEach(e -> System.out.println(e.getDataDeLancamento().format(formatador) + " - " + e.getNomeEpisodio()));

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() != 0.0)
                .collect(Collectors.groupingBy(Episodio::getNumeroTemporada,
                            Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println("\nAvaliações por temporada:");
        avaliacoesPorTemporada.forEach((temporada, avaliacao) -> System.out.println("Temporada " + temporada + ": " + avaliacao));

        DoubleSummaryStatistics estatisticas = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                        .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("\nEstatísticas das avaliações:");
        System.out.println("Média: " + estatisticas.getAverage());
        System.out.println("Mínimo: " + estatisticas.getMin());
        System.out.println("Máximo: " + estatisticas.getMax());
        System.out.println("Total: " + estatisticas.getCount());
    }
 */

}