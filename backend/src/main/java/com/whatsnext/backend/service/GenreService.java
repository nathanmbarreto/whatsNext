package com.whatsnext.backend.service;

import com.whatsnext.backend.dto.GenreSummaryResponse;
import com.whatsnext.backend.entity.Genre;
import com.whatsnext.backend.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreSummaryResponse> listarTodos() {
        return genreRepository.findAllByOrderByNameAsc().stream()
                .map(this::transformarEmGenreSummary)
                .toList();
    }

    private GenreSummaryResponse transformarEmGenreSummary(Genre genre) {
        GenreSummaryResponse resumo = new GenreSummaryResponse();
        resumo.setId(genre.getId());
        resumo.setName(genre.getName());

        return resumo;
    }

}
