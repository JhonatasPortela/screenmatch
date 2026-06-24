package br.app.portela.screenmatch.service;

import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Service
public class ConverteDados implements IConverteDados {

    private final JsonMapper mapper;

    public ConverteDados(JsonMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JacksonException e) {
            throw new IllegalArgumentException(
                    "Erro ao converter JSON para " + classe.getSimpleName(), e);
        }
    }
}
