package com.whatsnext.backend.controller;

import com.whatsnext.backend.dto.MediaDetailsResponse;
import com.whatsnext.backend.dto.MediaSummaryResponse;
import com.whatsnext.backend.entity.Media;
import com.whatsnext.backend.service.MediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medias")
public class MediaController {
    private final MediaService mediaService;
    private static final int MAX_PAGE_SIZE = 100;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping
    public Page<MediaSummaryResponse> listarTodas(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title,asc") String sort,
            @RequestParam(value = "genre", required = false)
            String genre
    ) {

        Pageable pageable = criarPageable(page, size, sort);


        return mediaService.listarTodos(type, genre, pageable);
    }


    @PostMapping
    public Media salvar(@RequestBody Media media) {
        return mediaService.salvar(media);
    }


    @GetMapping("/{id}")
    public MediaDetailsResponse buscarPorId(@PathVariable Long id) {
        return mediaService.buscarDetalhesPorId(id);
    }

    @PutMapping("/{id}")
    public Media atualizar(@PathVariable Long id, @RequestBody Media novosDados) {
        return mediaService.atualizar(id, novosDados);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        mediaService.excluir(id);
    }

    @PostMapping("/tmdb/{tmdbId}")
    public Media importarDoTmdb(@PathVariable Long tmdbId) {
        return mediaService.salvarDoTmdb(tmdbId);
    }

    @GetMapping("/search")
    public Page<MediaSummaryResponse> buscarPorTitulo(
            @RequestParam("title") String title,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title,asc") String sort,
            @RequestParam(value = "genre", required = false) String genre
    ) {
        Pageable pageable = criarPageable(page, size, sort);
        return mediaService.buscarPorTitulo(title, type, genre, pageable);
    }

    private Pageable criarPageable(int page, int size, String sort) {
        if (page < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Número de página inválido."
            );
        }

        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O tamanho da página deve ser maior que zero e menor que " + MAX_PAGE_SIZE + "."
            );
        }

        Sort ordenacao = criarSort(sort);

        Pageable pageable = PageRequest.of(page, size, ordenacao);

        return pageable;
    }

    private Sort criarSort(String sort) {
        String[] partesSort = sort.split(",");
        if (partesSort.length != 2
                || partesSort[0].isBlank()
                || partesSort[1].isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ordenação inválida. Use o formato campo,asc ou campo,desc."
            );
        }

        String campo = partesSort[0].trim();
        if (!campo.equals("title") && !campo.equals("releaseDate")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Campo de ordenação inválido."
            );
        }
        Sort.Direction direcao;

        try {
            direcao = Sort.Direction.fromString(partesSort[1].trim());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A direção deve ser 'asc' ou 'desc'."
            );
        }

        return Sort.by(direcao, campo);
    }
}
