package ru.rerumu.lists.domain.series.impl;

import com.jcabi.aspects.Loggable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.rerumu.lists.crosscut.exception.UnsupportedMethodException;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.series.SeriesDTOv2;
import ru.rerumu.lists.domain.series.item.SeriesItem;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
public class SeriesImpl implements Series {

    private final Long seriesId;

    @Getter
    private final String title;

    @Getter
    private final List<SeriesItem> itemsList;

    @Getter
    private final User user;


    public SeriesImpl(
            Long seriesId,
            @NonNull String title,
            @NonNull List<SeriesItem> itemsList,
            @NonNull User user
    ) {
        this.seriesId = seriesId;
        this.user = user;
        this.title = title;
        this.itemsList = new ArrayList<>(itemsList);
    }

    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public SeriesDTOv2 toDTO(){
        return new SeriesDTOv2(
                seriesId,
                user.userId(),
                title,
                itemsList.stream()
                        .map(SeriesItem::toDTO)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }

    @Override
    public Long getItemsCountAsLong() {
        return (long) itemsList.size();
    }

    @Override
    public SeriesImpl clone() {
        throw new UnsupportedMethodException();
    }

    @Override
    public Long getId() {
        return seriesId;
    }
}
