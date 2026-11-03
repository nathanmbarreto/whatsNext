package com.whatsnext.backend.dto;

public class TmdbGenre {
    private Long id;
    private String name;

    public TmdbGenre() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
