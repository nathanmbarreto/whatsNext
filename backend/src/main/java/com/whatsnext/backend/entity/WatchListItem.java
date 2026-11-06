package com.whatsnext.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "watchlist_items",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "media_id"}
        ))
public class WatchListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    protected WatchListItem() {
    }

    public WatchListItem(AppUser user, Media media) {
        this.user = user;
        this.media = media;
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public Media getMedia() {
        return media;
    }
}
