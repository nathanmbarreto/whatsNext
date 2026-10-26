package com.whatsnext.backend.dto;

public class MovieSummary {
    private Long id;
    private String title;
    private String posterPath;
    private String posterUrl;

    public MovieSummary(Long id, String title, String posterPath) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.posterUrl = "https://image.tmdb.org/t/p/w500";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        if (posterPath == null) {
            return null;
        }
        return posterUrl + posterPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}