package ru.rerumu.lists.controller.series.view.out;

import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.series.SeriesDTOv2;
import ru.rerumu.lists.domain.series.item.SeriesItemDTOv2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SeriesViewFactory {

    public SeriesView buildSeriesView(SeriesDTOv2 seriesDTO) {
        return new SeriesView(
                seriesDTO.getSeriesId(),
                seriesDTO.getUserId(),
                seriesDTO.getTitle(),
                seriesDTO.getItems()
        );
    }

    public SeriesListView buildSeriesListView(List<SeriesDTOv2> seriesDTOList) {
        Comparator<SeriesDTOv2> comparator = Comparator
                .comparing((SeriesDTOv2 series) -> {
                    Optional<LocalDateTime> maxDate = series.getItems().stream()
                            .map(SeriesItemDTOv2::getLastUpdateDate)
                            .max(LocalDateTime::compareTo);
                    return maxDate.orElse(LocalDateTime.MIN);
                }).reversed()
                .thenComparing(SeriesDTOv2::getTitle)
                .thenComparing(SeriesDTOv2::getSeriesId);

        seriesDTOList.sort(comparator);

        return new SeriesListView(
            seriesDTOList.stream()
                    .map(this::buildSeriesView)
                    .collect(Collectors.toCollection(ArrayList::new))
        );
    }
}
