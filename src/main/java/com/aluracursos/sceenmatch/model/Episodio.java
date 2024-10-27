package com.aluracursos.sceenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
@Entity
@Table(name = "episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numeroTemporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double evaluacion;
    private LocalDate fechaDeLanzamiento;
    @ManyToOne()
    private Serie serie;

    public Episodio() {
    }

    public Episodio(Integer numero, DatosEpisodio e) {
        this.numeroTemporada = numero;
        this.titulo = e.titulo();
        this.numeroEpisodio = e.numeroEpisodio();
        try {
            this.evaluacion = Double.valueOf(e.evaluacion());
        } catch (NumberFormatException ex) {
            this.evaluacion = 0.0;
        }
        try {
            this.fechaDeLanzamiento = LocalDate.parse(e.fechaDeLanzamiento());
        }catch (DateTimeParseException ex){
            this.fechaDeLanzamiento = null;
        }

    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Integer getNumeroTemporada() {
        return numeroTemporada;
    }

    public void setNumeroTemporada(Integer numeroTemporada) {
        this.numeroTemporada = numeroTemporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public LocalDate getFechaDeLanzamiento() {
        return fechaDeLanzamiento;
    }

    public void setFechaDeLanzamiento(LocalDate fechaDeLanzamiento) {
        this.fechaDeLanzamiento = fechaDeLanzamiento;
    }

    @Override
    public String toString() {
        return
                "numeroTemporada=" + numeroTemporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", evaluacion=" + evaluacion +
                ", fechaDeLanzamiento=" + fechaDeLanzamiento;
    }
}
