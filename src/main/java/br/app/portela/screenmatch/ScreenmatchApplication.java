package br.app.portela.screenmatch;

import br.app.portela.screenmatch.model.DadosEpisodio;
import br.app.portela.screenmatch.model.DadosSerie;
import br.app.portela.screenmatch.model.DadosTemporada;
import br.app.portela.screenmatch.principal.Principal;
import br.app.portela.screenmatch.service.ConsumoApi;
import br.app.portela.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		 Principal principal = new Principal();
		 principal.exibeMenu();

	}
}