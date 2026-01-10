package ru.rerumu.lists.controller.movies.views.out;

import lombok.Getter;

import java.util.List;

public class MovieListView {

    @Getter
    private final List<MovieView> titles;

    public MovieListView(List<MovieView> items) {
        this.titles = items;
    }
}
