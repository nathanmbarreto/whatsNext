package com.whatsnext.backend.controller;

import com.whatsnext.backend.dto.MovieSummary;
import com.whatsnext.backend.service.TmdbService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                        m.getId(), m.getTitle(), m.getPoster_path()
                ))
                .toList();
    }

    @GetMapping("/search")
    public List<MovieSummary> buscaFilmesPorNome(@RequestParam("query") String titulo){
        return tmdbService.buscarPorTitulo(titulo).getResults().stream()
                .map(m -> new MovieSummary(
                        m.getId(), m.getTitle(), m.getPoster_path()
                ))
                .toList();
    }
}