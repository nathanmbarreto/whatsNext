package com.whatsnext.backend.dto;

public class MovieDetails {
    private Long id;
    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;
    private String posterUrl;

    public MovieDetails(Long id, String title, String overview, String posterPath, String releaseDate) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.posterUrl = "https://image.tmdb.org/t/p/w500";
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
}
