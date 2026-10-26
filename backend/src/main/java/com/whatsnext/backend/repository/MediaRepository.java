package com.whatsnext.backend.repository;

import com.whatsnext.backend.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {

    Optional<Media> findByExternalIdAndSource(String externalId, String source);
}

