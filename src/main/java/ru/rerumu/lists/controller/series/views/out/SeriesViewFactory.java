package ru.rerumu.lists.controller.series.views.out;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.series.Series;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeriesViewFactory {

    public SeriesView buildSeriesView(Series series) {
        return new SeriesView(
                series.getId(),
                series.getUser().getId(),
                series.getTitle(),
                series.getItemsList()
        );
    }

    public SeriesListView buildSeriesListView(List<Series> seriesList) {
        Comparator<Series> comparator = Comparator
                // TODO: Update date for series itself?
//                .comparing((Series series) -> {
//                    Optional<LocalDateTime> maxDate = series.getItemsList().stream()
//                            .map(SeriesItem::getUpdateDate)
//                            .max(LocalDateTime::compareTo);
//                    return maxDate.orElse(LocalDateTime.MIN);
//                }).reversed()
                .comparing(Series::getTitle)
                .thenComparing(Series::getId);

        seriesList.sort(comparator);

        return new SeriesListView(
                seriesList.stream()
                        .map(this::buildSeriesView)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }
}
