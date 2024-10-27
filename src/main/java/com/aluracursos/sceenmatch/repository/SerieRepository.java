package com.aluracursos.sceenmatch.repository;

import com.aluracursos.sceenmatch.model.Categoria;
import com.aluracursos.sceenmatch.model.Episodio;
import com.aluracursos.sceenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository  extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Serie> findTop5ByOrderByEvaluacionDesc();

    List<Serie> findByGenero(Categoria categoria);

    //QUERY DERIVADA
    //List<Serie> findByEvaluacionGreaterThanAndTotalDeTemporadasLessThan(Double evaluacion, Integer temporadas);

    //QUERY NATIVA Es muy rígido y podemos hacerlo mas flexible
    /*
    @Query(value = "select * from series Where series.evaluacion >7 AND series.total_de_temporadas <= 10", nativeQuery = true)
    List<Serie> seriesPorTemporadaYEvaluacion();
    */

    //JPQL
    @Query("select s from Serie s Where s.evaluacion > :evaluacion AND s.totalDeTemporadas <= :temporadas")
    List<Serie> seriesPorTemporadaYEvaluacion(Double evaluacion, Integer temporadas);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5Episodios(Serie serie);
}
