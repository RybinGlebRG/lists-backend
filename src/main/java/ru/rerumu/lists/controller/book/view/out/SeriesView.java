package ru.rerumu.lists.controller.book.view.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SeriesView {

    @Getter
    private final Long seriesId;

    @Getter
    private final String title;

}
