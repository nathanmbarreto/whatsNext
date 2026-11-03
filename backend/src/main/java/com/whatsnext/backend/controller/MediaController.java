package com.whatsnext.backend.controller;

import com.whatsnext.backend.dto.MediaDetailsResponse;
import com.whatsnext.backend.dto.MediaSummaryResponse;
import com.whatsnext.backend.entity.Media;
import com.whatsnext.backend.service.MediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medias")
public class MediaController {
    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping
    public Page<MediaSummaryResponse> listarTodas(@RequestParam(required = false) String type, Pageable pageable) {
        return mediaService.listarTodos(type, pageable);
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
    public List<MediaSummaryResponse> buscarPorTitulo(@RequestParam("title") String title, @RequestParam(value = "type", required = false) String type) {
        return mediaService.buscarPorTitulo(title, type);
    }
}
