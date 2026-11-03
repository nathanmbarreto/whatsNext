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
    List<Media> findByTitleContainingIgnoreCase(String title);
    List<Media> findByTitleContainingIgnoreCaseAndType(String title, String type);
    Page<Media> findByType(String type, Pageable pageable);
}

