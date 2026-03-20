package br.app.portela.screenmatch.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}