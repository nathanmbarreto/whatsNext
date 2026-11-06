package com.whatsnext.backend.service;

import com.whatsnext.backend.dto.MediaDetailsResponse;
import com.whatsnext.backend.dto.MediaSummaryResponse;
import com.whatsnext.backend.dto.TmdbGenre;
import com.whatsnext.backend.dto.TmdbMovie;
import com.whatsnext.backend.entity.Genre;
import com.whatsnext.backend.entity.Media;
import com.whatsnext.backend.repository.GenreRepository;
import com.whatsnext.backend.repository.MediaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MediaService {
    private final MediaRepository mediaRepository;
    private final TmdbService tmdbService;
    private final GenreRepository genreRepository;

    public MediaService(MediaRepository mediaRepository, TmdbService tmdbService, GenreRepository genreRepository) {
        this.mediaRepository = mediaRepository;
        this.tmdbService = tmdbService;
        this.genreRepository = genreRepository;
    }

    @Transactional(readOnly = true)
    public Page<MediaSummaryResponse> listarTodos(String type, Long genreId, Pageable pageable) {
    validarGenreId(genreId);
    Page<Media> pagina = buscarPorFiltros(type, genreId, pageable);
        return pagina.map(this::converterParaResumo);

    }

    private Page<Media> buscarPorFiltros(String type, Long genreId, Pageable pageable){
        Page<Media> pagina;
        boolean temType = type != null && !type.isBlank();
        boolean temGenreId = genreId != null;

        if (temType && !temGenreId) {
            pagina = mediaRepository.findByType(type, pageable);
        } else if (temGenreId && !temType) {
            pagina = mediaRepository.findDistinctByGenres_Id(genreId, pageable);
        } else if (temGenreId && temType) {
            pagina = mediaRepository.findDistinctByTypeAndGenres_Id(type, genreId, pageable);
        } else {
            pagina = mediaRepository.findAll(pageable);
        }

        return pagina;
    }

    @Transactional(readOnly = true)
    public Page<MediaSummaryResponse> buscarPorTitulo(String title, String type, Long genreId, Pageable pageable) {
        if (title == null || title.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Titulo é obrigatório.");
        }
        validarGenreId(genreId);

        boolean temType = type != null && !type.isBlank();
        boolean temGenreId = genreId != null;

        Page<Media> medias;

        if (!temType && !temGenreId) {
            medias = mediaRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else  if (temType && !temGenreId){
            medias = mediaRepository.findByTitleContainingIgnoreCaseAndType(title, type, pageable);
        }else  if (!temType && temGenreId){
            medias = mediaRepository.findDistinctByTitleContainingIgnoreCaseAndGenres_Id(title, genreId, pageable);
        } else {
            medias = mediaRepository.findDistinctByTitleContainingIgnoreCaseAndTypeAndGenres_Id(title, type, genreId, pageable);
        }

        return medias
                .map(this::converterParaResumo);
    }

    private MediaSummaryResponse converterParaResumo(Media media) {

        MediaSummaryResponse resumo = new MediaSummaryResponse();
        resumo.setId(media.getId());
        resumo.setPosterUrl(media.getPosterUrl());
        if (media.getReleaseDate() == null) {
            resumo.setReleaseDate(null);
        } else {
            resumo.setReleaseDate(media.getReleaseDate().toString());
        }
        resumo.setTitle(media.getTitle());

        List<String> nomesGeneros = media.getGenres().stream()
                .map(genre -> genre.getName())
                .toList();
        resumo.setGenres(nomesGeneros);

        return resumo;
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

    private Media preencherMedia(Media media, TmdbMovie filme) {

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

        Set<Genre> generos = new HashSet<>();
        if (filme.getGenres() != null) {
            for (TmdbGenre tmdbGenre : filme.getGenres()) {
                generos.add(buscarOuCriar(tmdbGenre));
            }
        }
        media.setGenres(generos);

        return media;
    }

    public Media salvarDoTmdb(Long tmdbId) {
        Optional<Media> existente =
                mediaRepository.findByExternalIdAndSource(
                        String.valueOf(tmdbId),
                        "TMDB"
                );
        Media media;
        if (existente.isPresent()) {
            media = existente.get();
        } else {
            media = new Media();
        }

        TmdbMovie filme;
        try {
            filme = tmdbService.consultarDetalhes(tmdbId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Filme não encontrado no TMDB."
            );
        }

        preencherMedia(media, filme);
        return mediaRepository.save(media);
    }

    private Genre buscarOuCriar(TmdbGenre tmdbGenre) {
        Optional<Genre> existente =
                genreRepository.findByExternalIdAndSource(
                        tmdbGenre.getId(), "TMDB");

        if (existente.isPresent()) {
            return existente.get();
        }

        Genre genre = new Genre();

        genre.setExternalId(tmdbGenre.getId());
        genre.setName(tmdbGenre.getName());
        genre.setSource("TMDB");

        return genreRepository.save(genre);
    }

    @Transactional(readOnly = true)
    public MediaDetailsResponse buscarDetalhesPorId(Long id) {
        Media media = buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Mídia não encontrada"));

        MediaDetailsResponse novaMedia = new MediaDetailsResponse();
        novaMedia.setId(media.getId());
        novaMedia.setTitle(media.getTitle());
        novaMedia.setPosterUrl(media.getPosterUrl());
        if (media.getReleaseDate() == null) {
            novaMedia.setReleaseDate(null);
        } else {
            novaMedia.setReleaseDate(media.getReleaseDate().toString());
        }
        novaMedia.setOverview(media.getOverview());
        List<String> nomeGeneros = media.getGenres().stream()
                .map(genre -> genre.getName())
                .toList();
        novaMedia.setGenres(nomeGeneros);

        return novaMedia;
    }

    private void validarGenreId(Long genreId) {
        if(genreId != null && genreId < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O ID do gênero deve ser positivo.."
            );
        }
    }

}
