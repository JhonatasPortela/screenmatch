package br.app.portela.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer numeroTemporada;
    private String nomeEpisodio;
    private Double avaliacao;
    private LocalDate dataDeLancamento;
    private Integer numeroEpisodio;


    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.numeroTemporada = numeroTemporada;
        this.nomeEpisodio = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.episodio();
        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }
        try {
            this.dataDeLancamento = LocalDate.parse(dadosEpisodio.dataDeLancamento());
        } catch (DateTimeParseException e) {
            this.dataDeLancamento = null;
        }
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public Integer getNumeroTemporada() {
        return numeroTemporada;
    }

    public String getNomeEpisodio() {
        return nomeEpisodio;
    }

    public LocalDate getDataDeLancamento() {
        return dataDeLancamento;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public void setDataDeLancamento(LocalDate dataDeLancamento) {
        this.dataDeLancamento = dataDeLancamento;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public void setNomeEpisodio(String nomeEpisodio) {
        this.nomeEpisodio = nomeEpisodio;
    }

    public void setNumeroTemporada(Integer numeroTemporada) {
        this.numeroTemporada = numeroTemporada;
    }

    @Override
    public String toString() {
        return "numeroTemporada=" + numeroTemporada +
                ", numeroEpisodio=" + numeroEpisodio +
                ", nomeEpisodio='" + nomeEpisodio + '\'' +
                ", avaliacao=" + avaliacao +
                ", dataDeLancamento=" + dataDeLancamento;
    }
}
