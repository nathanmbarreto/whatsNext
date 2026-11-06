package com.whatsnext.backend.repository;

import com.whatsnext.backend.entity.WatchListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WatchListItemRepository extends JpaRepository<WatchListItem, Long> {
    boolean existsByUser_IdAndMedia_Id(Long userId, Long mediaId);
    List<WatchListItem> findByUser_Id(Long userId);
}
