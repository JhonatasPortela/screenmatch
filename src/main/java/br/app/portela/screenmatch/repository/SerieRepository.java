package br.app.portela.screenmatch.repository;

import br.app.portela.screenmatch.model.Categoria;
import br.app.portela.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nome);


    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String trim, double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int maximoTemporadas, double avaliacaoMinima);
}
