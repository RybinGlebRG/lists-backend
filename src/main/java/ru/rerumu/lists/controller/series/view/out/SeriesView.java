package ru.rerumu.lists.controller.series.view.out;

import java.util.ArrayList;
import java.util.List;

public record SeriesView(
        Long seriesId,
        Long userId,
        String title,

        // TODO: Specific class???
        List<?> items
) {

    public SeriesView {
        items = new ArrayList<>(items);
    }
}
