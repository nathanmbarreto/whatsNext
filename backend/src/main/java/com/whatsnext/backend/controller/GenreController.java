package com.whatsnext.backend.controller;

import com.whatsnext.backend.dto.GenreSummaryResponse;
import com.whatsnext.backend.service.GenreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<GenreSummaryResponse> listarTodos (){
        return genreService.listarTodos();
    }

}
