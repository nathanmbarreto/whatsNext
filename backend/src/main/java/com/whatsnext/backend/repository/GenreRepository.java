package com.whatsnext.backend.repository;

import com.whatsnext.backend.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
