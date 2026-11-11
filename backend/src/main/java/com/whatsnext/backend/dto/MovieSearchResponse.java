package com.whatsnext.backend.dto;

import java.util.List;

public class MovieSearchResponse {
    private int page;
    private List<MovieSummary> results;
    private int totalPages;
    private int totalResults;

    public MovieSearchResponse() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieSummary> getResults() {
        return results;
    }

    public void setResults(List<MovieSummary> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
