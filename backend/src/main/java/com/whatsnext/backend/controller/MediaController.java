package com.whatsnext.backend.controller;

import com.whatsnext.backend.entity.Media;
import com.whatsnext.backend.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medias")
public class MediaController {
    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping
    public List<Media> listarTodas() {
        return mediaService.listarTodos();
    }

    @PostMapping
    public Media salvar(@RequestBody Media media) {
        return mediaService.salvar(media);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Media> buscarPorId(@PathVariable Long id) {
        Optional<Media> resultado = mediaService.buscarPorId(id);

        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public Media atualizar(@PathVariable Long id, @RequestBody Media novosDados) {
        return mediaService.atualizar(id, novosDados);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        mediaService.excluir(id);
    }
}
