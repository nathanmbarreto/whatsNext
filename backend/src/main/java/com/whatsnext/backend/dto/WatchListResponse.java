package com.whatsnext.backend.dto;


public class WatchListResponse {
    private Long watchListItemId;
    private Long mediaId;
    private String title;
    private String posterUrl;
    private String releaseDate;

    public WatchListResponse() {}

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
}
