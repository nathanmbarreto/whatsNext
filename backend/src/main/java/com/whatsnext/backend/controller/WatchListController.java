package com.whatsnext.backend.controller;

import com.whatsnext.backend.dto.WatchListResponse;
import com.whatsnext.backend.dto.WatchStatusUpdateRequest;
import com.whatsnext.backend.entity.WatchStatus;
import com.whatsnext.backend.service.WatchListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {
    private final WatchListService watchListService;
    private static final int MAX_PAGE_SIZE = 100;

    public WatchListController(WatchListService watchListService) {
        this.watchListService = watchListService;
    }

    @GetMapping
    public Page<WatchListResponse> listar(
            Authentication authentication,
            @RequestParam(required = false) WatchStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title,asc") String sort) {

        Pageable pageable = criarPageable(page, size, sort);

        return watchListService.listarDoUsuario(authentication.getName(), status, pageable);
    }

    private Pageable criarPageable(int page, int size, String sort) {
        if (page < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Número de página inválido."
            );
        }

        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O tamanho da página deve ser maior que zero e até " + MAX_PAGE_SIZE + "."
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

        String propriedade = switch (campo) {
            case "title" -> "media.title";
            case "releaseDate" -> "media.releaseDate";
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Campo de ordenação inválido."
            );
        };

        return Sort.by(direcao, propriedade);
    }

    @PostMapping("/{mediaId}")
    public WatchListResponse adicionar(
            @PathVariable("mediaId") Long mediaId,
            Authentication authentication) {
        String email = authentication.getName();
        return watchListService.adicionar(email, mediaId);
    }

    @DeleteMapping("/{itemId}")
    public void remover(
            @PathVariable("itemId") Long itemId,
            Authentication authentication) {
        String email = authentication.getName();
        watchListService.remover(email, itemId);
    }

    @PatchMapping("/{itemId}/status")
    public WatchListResponse atualizarStatus(
            @RequestBody WatchStatusUpdateRequest request,
            Authentication authentication,
            @PathVariable("itemId") Long itemId) {
        return watchListService.atualizarStatus(authentication.getName(), itemId, request.getStatus());
    }
}
