package com.whatsnext.backend.repository;

import com.whatsnext.backend.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {

    Optional<Media> findByExternalIdAndSource(String externalId, String source);

    List<Media> findByType(String type);

    Page<Media> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Media> findByTitleContainingIgnoreCaseAndType(String title, String type, Pageable pageable);

    Page<Media> findByType(String type, Pageable pageable);

    Page<Media> findDistinctByGenres_NameIgnoreCase(
            String genreName, Pageable pageable
    );

    Page<Media> findDistinctByTypeAndGenres_NameIgnoreCase(
            String type, String genreName, Pageable pageable
    );

    Page<Media> findDistinctByTitleContainingIgnoreCaseAndGenres_NameIgnoreCase(
            String title, String genreName, Pageable pageable
    );

    Page<Media> findDistinctByTitleContainingIgnoreCaseAndGenres_NameIgnoreCaseAndType(
            String title, String genreName, String type, Pageable pageable
    );

}

