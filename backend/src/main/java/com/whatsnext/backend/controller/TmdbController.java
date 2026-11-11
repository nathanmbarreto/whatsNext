package com.whatsnext.backend.controller;

import com.whatsnext.backend.dto.*;
import com.whatsnext.backend.service.TmdbService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;

    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/popular")
    public List<MovieSummary> buscarFilmesPopulares(@RequestParam(defaultValue = "1") int page) {
        return tmdbService.buscarFilmesPopulares(page).getResults()
                .stream()
                .map(m -> new MovieSummary(
                        m.getId(), m.getTitle(), m.getPoster_path(), m.getNotaTmdb(),
                        m.getRelease_date(), m.getOverview(), m.getPopularity()
                ))
                .toList();
    }

    @GetMapping("/series/popular")
    public TmdbSeriesPopularResponse buscarSeriesPopulares(
            @RequestParam(defaultValue = "1") int page) {
        return tmdbService.buscarSeriesPopulares(page);
    }

    @GetMapping("/search")
    public MovieSearchResponse buscaFilmesPorNome(@RequestParam("query") String titulo, @RequestParam(defaultValue = "1") int page) {
        TmdbPopularResponse response = tmdbService.buscarPorTitulo(titulo, page);
        List<MovieSummary> filmes = response.getResults().stream()
                .map(m -> new MovieSummary(
                        m.getId(),
                        m.getTitle(),
                        m.getPoster_path(),
                        m.getNotaTmdb(),
                        m.getRelease_date(),
                        m.getOverview(),
                        m.getPopularity()
                ))
                .toList();

        MovieSearchResponse resposta = new MovieSearchResponse();
        resposta.setPage(response.getPage());
        resposta.setResults(filmes);
        resposta.setTotalPages(response.getTotalPages());
        resposta.setTotalResults(response.getTotalResults());

        return resposta;
    }

    @GetMapping("/series/search")
    public TmdbSeriesPopularResponse buscarSeriesPorTitulo(
            @RequestParam("query") String titulo,
            @RequestParam(defaultValue = "1") int page
    ) {
        return tmdbService.buscarSeriesPorTitulo(titulo, page);
    }

    @GetMapping("/{id}")
    public MovieDetails consultarDetalhes(@PathVariable("id") Long id) {
        TmdbMovie movie = tmdbService.consultarDetalhes(id);

        return new MovieDetails(
                movie.getId(),
                movie.getTitle(),
                movie.getOverview(),
                movie.getPoster_path(),
                movie.getRelease_date(),
                movie.getNotaTmdb(),
                movie.getGenres(),
                movie.getRuntime(),
                movie.getOriginalTitle(),
                movie.getStatus()
        );
    }

    @GetMapping("/series/{id}")
    public TmdbSeries consultarDetalhesSeries(@PathVariable("id") Long id) {
        return tmdbService.consultarDetalhesSerie(id);
    }
}