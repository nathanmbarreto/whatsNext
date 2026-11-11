package com.whatsnext.backend.dto;

import java.util.List;

public class MovieDetails {
    private Long id;
    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;
    private String posterUrl;
    private Double notaTmdb;
    private List<TmdbGenre> genres;
    private Integer runtime;
    private String originalTitle;
    private String status;



    public MovieDetails(
            Long id, String title, String overview,
            String posterPath, String releaseDate,
            Double notaTmdb, List<TmdbGenre> genres,
            Integer runtime, String originalTitle, String status)
    {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.posterUrl = "https://image.tmdb.org/t/p/w500";
        this.notaTmdb = notaTmdb;
        this.genres = genres;
        this.runtime = runtime;
        this.originalTitle = originalTitle;
        this.status = status;
    }

    public Long getId() {
        return id;
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

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterUrl() {
        if (posterPath == null) {
            return null;
        }
        return posterUrl + posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getNotaTmdb() {
        return notaTmdb;
    }

    public void setNotaTmdb(Double notaTmdb) {
        this.notaTmdb = notaTmdb;
    }

    public List<TmdbGenre> getGenres() {
        return genres;
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
}
