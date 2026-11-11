package com.whatsnext.backend.service;

import com.whatsnext.backend.dto.WatchListResponse;
import com.whatsnext.backend.entity.AppUser;
import com.whatsnext.backend.entity.Media;
import com.whatsnext.backend.entity.WatchListItem;
import com.whatsnext.backend.entity.WatchStatus;
import com.whatsnext.backend.repository.AppUserRepository;
import com.whatsnext.backend.repository.MediaRepository;
import com.whatsnext.backend.repository.WatchListItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class WatchListService {
    private final AppUserRepository appUserRepository;
    private final MediaRepository mediaRepository;
    private final WatchListItemRepository watchListItemRepository;

    public WatchListService(AppUserRepository appUserRepository, MediaRepository mediaRepository, WatchListItemRepository watchListItemRepository) {
        this.appUserRepository = appUserRepository;
        this.mediaRepository = mediaRepository;
        this.watchListItemRepository = watchListItemRepository;
    }

    @Transactional
    public WatchListResponse adicionar(String email, Long mediaId) {
        AppUser usuario = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado."
                ));

        Optional<Media> media = mediaRepository.findById(mediaId);

        if (media.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Media não encontrado.");
        }
        if (watchListItemRepository.existsByUser_IdAndMedia_Id(usuario.getId(), mediaId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Media já adicionada.");
        }

        WatchListItem novoItem = new WatchListItem(usuario, media.get());
        WatchListItem salvo = watchListItemRepository.save(novoItem);

        return converterParaResponse(salvo);
    }

    private WatchListResponse converterParaResponse(WatchListItem item) {
        Media media = item.getMedia();

        WatchListResponse resposta = new WatchListResponse();
        resposta.setWatchListItemId(item.getId());
        resposta.setMediaId(media.getId());
        resposta.setTitle(media.getTitle());
        resposta.setPosterUrl(media.getPosterUrl());
        resposta.setReleaseDate(
                media.getReleaseDate() == null ? null : media.getReleaseDate().toString()
        );
        resposta.setStatus(item.getStatus());
        return resposta;
    }

    @Transactional(readOnly = true)
    public Page<WatchListResponse> listarDoUsuario(String email, WatchStatus status, Pageable pageable) {
        AppUser usuario = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado."
                ));
        Page<WatchListItem> pagina;
        if (status == null) {
            pagina = watchListItemRepository.findByUser_Id(usuario.getId(), pageable);
        } else {pagina = watchListItemRepository
                .findByUser_IdAndStatus(usuario.getId(), status, pageable);
        }

        return pagina.map(this::converterParaResponse);
    }

    @Transactional
    public void remover(String email, Long itemId) {
        AppUser usuario = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario não encontrado."));

        WatchListItem item = watchListItemRepository.findByIdAndUser_Id(itemId, usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Item não encontrado para o usuario."
                ));
        watchListItemRepository.delete(item);
    }

    @Transactional
    public WatchListResponse atualizarStatus(String email, Long itemId, WatchStatus status) {
        if (status == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Status não pode ser nulo."
            );
        }
        AppUser usuario = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario não encontrado."));

        WatchListItem item = watchListItemRepository.findByIdAndUser_Id(itemId, usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Item não encontrado para o usuario."
                ));

        item.setStatus(status);
        WatchListItem salvo = watchListItemRepository.save(item);
        return converterParaResponse(salvo);
    }

    @Transactional(readOnly = true)
    public boolean estaNaLista(String email, Long tmdbId, String type) {
        AppUser usuario = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado."
                ));

        String tipoMedia;
        if ("movie".equalsIgnoreCase(type)) {
            tipoMedia = "MOVIE";
        } else if ("series".equalsIgnoreCase(type)) {
            tipoMedia = "SERIES";
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Tipo de mídia inválido."
            );
        }

        return mediaRepository.findByExternalIdAndSourceAndType(
                        String.valueOf(tmdbId), "TMDB", tipoMedia
                )
                .map(media -> watchListItemRepository.existsByUser_IdAndMedia_Id(
                        usuario.getId(), media.getId()
                ))
                .orElse(false);
    }
}

