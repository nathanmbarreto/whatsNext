package com.whatsnext.backend.dto;

public class MovieSummary {
    private Long id;
    private String title;
    private String posterPath;
    private String posterUrl;
    private String overview;
    private String releaseDate;
    private Double voteAverage;
    private Double popularity;

    public MovieSummary(Long id, String title, String posterPath,
                        Double voteAverage, String releaseDate, String overview,
                        Double popularity) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.posterUrl = "https://image.tmdb.org/t/p/w500";
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.popularity = popularity;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }
}