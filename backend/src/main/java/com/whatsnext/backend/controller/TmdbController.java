package com.whatsnext.backend.controller;

import com.whatsnext.backend.dto.MovieDetails;
import com.whatsnext.backend.dto.MovieSummary;
import com.whatsnext.backend.dto.TmdbMovie;
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
                        m.getId(), m.getTitle(), m.getPoster_path()
                ))
                .toList();
    }

    @GetMapping("/search")
    public List<MovieSummary> buscaFilmesPorNome(@RequestParam("query") String titulo, @RequestParam(defaultValue = "1") int page){
        return tmdbService.buscarPorTitulo(titulo, page).getResults().stream()
                .map(m -> new MovieSummary(
                        m.getId(), m.getTitle(), m.getPoster_path()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public MovieDetails consultarDetalhes (@PathVariable("id") Long id){
       TmdbMovie movie = tmdbService.consultarDetalhes(id);

       return new MovieDetails(
               movie.getId(),
               movie.getTitle(),
               movie.getOverview(),
               movie.getPoster_path(),
               movie.getRelease_date(),
               movie.getNotaTmdb(),
               movie.getGenres()
       );
    }
}