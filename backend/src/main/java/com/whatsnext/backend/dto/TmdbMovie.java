package com.whatsnext.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TmdbMovie {
    private Long id;
    private String title;
    private String overview;
    private String release_date;
    private String poster_path;
    private List<TmdbGenre> genres;
    private Integer runtime;
    private String status;
    private Double popularity;

    @JsonProperty("vote_average")
    private Double notaTmdb;

    @JsonProperty("original_title")
    private String originalTitle;


    public TmdbMovie() {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public List<TmdbGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<TmdbGenre> genres) {
        this.genres = genres;
    }

    public Double getNotaTmdb() {
        return notaTmdb;
    }

    public void setNotaTmdb(Double notaTmdb) {
        this.notaTmdb = notaTmdb;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }
}
