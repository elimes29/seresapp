package com.aluracursos.sceenmatch.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name="series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name = "nombre")
    private String titulo;
    private Integer totalDeTemporadas;
    private Double evaluacion;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String sinopsis;
    private String actores;
    private String poster;
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios;

    public Serie() {
    }

    public Serie(DatosSerie datosSerie){
        this.titulo = datosSerie.titulo();
        this.totalDeTemporadas = datosSerie.totalDeTemporadas();
        this.evaluacion = OptionalDouble.of(
                Double.valueOf(datosSerie.evaluacion())).orElse(0.0);//Estamos convirtiendo String a Double
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
        this.sinopsis = datosSerie.sinopsis();
        this.actores = datosSerie.actores();
        this.poster = datosSerie.poster();
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalDeTemporadas() {
        return totalDeTemporadas;
    }

    public void setTotalDeTemporadas(Integer totalDeTemporadas) {
        this.totalDeTemporadas = totalDeTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return
                "genero=" + genero +
                ", titulo='" + titulo + '\'' +
                ", totalDeTemporadas=" + totalDeTemporadas +
                ", evaluacion=" + evaluacion +
                ", sinopsis='" + sinopsis + '\'' +
                ", actores='" + actores + '\'' +
                ", poster='" + poster + '\''+
                        ", episodios='" + episodios;
    }
}
