package com.whatsnext.backend.dto;


import com.whatsnext.backend.entity.WatchStatus;

public class WatchListResponse {
    private Long watchListItemId;
    private Long mediaId;
    private String title;
    private String posterUrl;
    private String releaseDate;
    private WatchStatus status;

    public WatchListResponse() {
    }

    public Long getWatchListItemId() {
        return watchListItemId;
    }

    public void setWatchListItemId(Long watchListItemId) {
        this.watchListItemId = watchListItemId;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public WatchStatus getStatus() {
        return status;
    }

    public void setStatus(WatchStatus status) {
        this.status = status;
    }
}
