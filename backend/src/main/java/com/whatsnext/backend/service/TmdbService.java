package com.whatsnext.backend.service;

import com.whatsnext.backend.dto.TmdbMovie;
import com.whatsnext.backend.dto.TmdbPopularResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TmdbService {

    private final RestClient restClient;
    private final String apiKey;

    public TmdbService(
            RestClient.Builder builder,
            @Value("${tmdb.api.key}") String apiKey
    ) {
        this.restClient = builder
                .baseUrl("https://api.themoviedb.org/3")
                .build();
        this.apiKey = apiKey;
    }

    public TmdbPopularResponse buscarFilmesPopulares(int page) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/popular")
                        .queryParam("api_key", apiKey)
                        .queryParam("page", page)
                        .queryParam("language", "pt-BR")
                        .build())
                .retrieve()
                .body(TmdbPopularResponse.class);
    }

    public TmdbPopularResponse buscarPorTitulo(String titulo, int page) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("api_key", apiKey)
                        .queryParam("language", "pt-BR")
                        .queryParam("query", titulo)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .body(TmdbPopularResponse.class);
    }

    public TmdbMovie consultarDetalhes(Long id){
        return restClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/movie/{id}")
                        .queryParam("api_key", apiKey)
                        .queryParam("language", "pt-BR")
                        .build(id))
                .retrieve()
                .body(TmdbMovie.class);
    }
}