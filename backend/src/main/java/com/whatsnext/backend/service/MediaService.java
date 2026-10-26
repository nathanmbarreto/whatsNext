package com.whatsnext.backend.service;

import com.whatsnext.backend.entity.Media;
import com.whatsnext.backend.repository.MediaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MediaService {
    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public List<Media> listarTodos() {
        return mediaRepository.findAll();
    }

    @Transactional
    public Media salvar(Media media) {
        return mediaRepository.save(media);
    }

    public Optional<Media> buscarPorId(Long id) {
        return mediaRepository.findById(id);
    }

    public Media atualizar(Long id, Media novosDados) {
        if (novosDados.getTitle() == null || novosDados.getTitle().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Titulo é obrigatório.");
        }

        if (novosDados.getType() == null || novosDados.getType().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Tipo é obrigatório.");
        }
        if (novosDados.getSource() == null || novosDados.getSource().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Origem é obrigatório.");
        }
        if (novosDados.getExternalId() == null || novosDados.getExternalId().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Id externo é obrigatório.");
        }

        Media mediaExistente = buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mídia não encontrada"));

        mediaExistente.setTitle(novosDados.getTitle());
        mediaExistente.setSource(novosDados.getSource());
        mediaExistente.setExternalId(novosDados.getExternalId());
        mediaExistente.setOverview(novosDados.getOverview());
        mediaExistente.setType(novosDados.getType());
        mediaExistente.setPosterUrl(novosDados.getPosterUrl());
        mediaExistente.setReleaseDate(novosDados.getReleaseDate());

        return mediaRepository.save(mediaExistente);
    }

    public void excluir(Long id) {
        buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Midia não encontrada."));

        mediaRepository.deleteById(id);

    }
}
