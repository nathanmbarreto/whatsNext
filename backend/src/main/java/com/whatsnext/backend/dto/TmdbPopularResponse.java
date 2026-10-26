package com.whatsnext.backend.dto;

import java.util.List;

public class TmdbPopularResponse {
    private int page;
    private List<TmdbMovie> results;

    public TmdbPopularResponse(){}

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<TmdbMovie> getResults() {
        return results;
    }

    public void setResults(List<TmdbMovie> results) {
        this.results = results;
    }
}
