package com.whatsnext.backend.service;

import com.whatsnext.backend.dto.WatchListResponse;
import com.whatsnext.backend.entity.AppUser;
import com.whatsnext.backend.entity.Media;
import com.whatsnext.backend.entity.WatchListItem;
import com.whatsnext.backend.repository.AppUserRepository;
import com.whatsnext.backend.repository.MediaRepository;
import com.whatsnext.backend.repository.WatchListItemRepository;
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
    public WatchListItem adicionar(Long userId, Long mediaId) {
        Optional<AppUser> user = appUserRepository.findById(userId);
        Optional<Media> media = mediaRepository.findById(mediaId);

        if (user.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }
        if (media.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Media não encontrado.");
        }
        if (watchListItemRepository.existsByUser_IdAndMedia_Id(userId, mediaId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Media já adicionada.");
        }

        WatchListItem item = new WatchListItem(user.get(), media.get());

        return watchListItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<WatchListResponse> listarDoUsuario(Long userId) {
        if (!appUserRepository.existsById(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuário não encontrado."
            );
        }
        return watchListItemRepository.findByUser_Id(userId).stream()
                .map(item -> {
                    Media media = item.getMedia();

                    WatchListResponse resumo = new WatchListResponse();
                    resumo.setWatchListItemId(item.getId());
                    resumo.setMediaId(media.getId());
                    resumo.setTitle(media.getTitle());
                    resumo.setPosterUrl(media.getPosterUrl());

                    if (media.getReleaseDate() == null) {
                        resumo.setReleaseDate(null);
                    } else {
                        resumo.setReleaseDate(media.getReleaseDate().toString());
                    }
                    return resumo;
                })
                .toList();
    }
}

