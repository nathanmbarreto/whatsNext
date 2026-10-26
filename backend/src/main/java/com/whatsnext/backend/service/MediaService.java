package com.whatsnext.backend.service;

import com.whatsnext.backend.dto.TmdbMovie;
import com.whatsnext.backend.entity.Media;
import com.whatsnext.backend.repository.MediaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MediaService {
    private final MediaRepository mediaRepository;
    private final TmdbService tmdbService;

    public MediaService(MediaRepository mediaRepository, TmdbService tmdbService) {
        this.mediaRepository = mediaRepository;
        this.tmdbService = tmdbService;
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

    private Media transformarEmMedia(TmdbMovie filme) {
        Media media = new Media();
        media.setExternalId(String.valueOf(filme.getId()));
        media.setSource("TMDB");
        media.setType("MOVIE");
        media.setTitle(filme.getTitle());
        media.setOverview(filme.getOverview());
        if (filme.getPoster_path() == null) {
            media.setPosterUrl(null);
        } else {
            media.setPosterUrl("https://image.tmdb.org/t/p/w500" + filme.getPoster_path());
        }
        if (filme.getRelease_date() == null || filme.getRelease_date().isBlank()) {
            media.setReleaseDate(null);
        } else {
            media.setReleaseDate(LocalDate.parse(filme.getRelease_date()));
        }
        media.setLastSyncedAt(LocalDateTime.now());

        return media;
    }

    public Media salvarDoTmdb(Long tmdbId) {
        Optional<Media> existente =
                mediaRepository.findByExternalIdAndSource(
                        String.valueOf(tmdbId),
                        "TMDB"
                );

        if (existente.isPresent()) {
            return existente.get();
        }

        TmdbMovie filme = tmdbService.consultarDetalhes(tmdbId);
        Media novaMedia = transformarEmMedia(filme);

        return mediaRepository.save(novaMedia);
    }

}
