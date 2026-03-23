package br.app.portela.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer numeroTemporada;
    private String nomeEpisodio;
    private Double avaliacao;
    private LocalDate dataDeLancamento;


    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.numeroTemporada = numeroTemporada;
        this.nomeEpisodio = dadosEpisodio.titulo();
        try {
            this.avaliacao = Double.parseDouble(dadosEpisodio.avaliacao());
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

    @Override
    public String toString() {
        return "numeroTemporada=" + numeroTemporada +
                ", nomeEpisodio='" + nomeEpisodio + '\'' +
                ", avaliacao=" + avaliacao +
                ", dataDeLancamento=" + dataDeLancamento;
    }
}
