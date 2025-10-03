package ru.rerumu.lists.domain.series.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public enum SeriesItemType {
    BOOK(0L),
    TITLE(1L),
    GAME(2L);

    private final Long id;

    public static SeriesItemType findById(@NonNull Long id) {
        for (SeriesItemType type: values()) {
            if (id.equals(type.id)) {
                return type;
            }
        }

        return null;
    }

}
