package com.whatsnext.backend.repository;

import com.whatsnext.backend.entity.WatchListItem;
import com.whatsnext.backend.entity.WatchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface WatchListItemRepository extends JpaRepository<WatchListItem, Long> {
    boolean existsByUser_IdAndMedia_Id(Long userId, Long mediaId);
    Page<WatchListItem> findByUser_Id(Long userId, Pageable pageable);
    Optional<WatchListItem> findByIdAndUser_Id(Long itemId, Long userId);
    Page<WatchListItem> findByUser_IdAndStatus(Long userId, WatchStatus status, Pageable pageable);
}
