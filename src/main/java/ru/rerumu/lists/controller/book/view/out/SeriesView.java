package ru.rerumu.lists.controller.book.view.out;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.rerumu.lists.crosscut.DeepCopyable;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder(toBuilder = true, access = AccessLevel.PRIVATE)
public class SeriesView  implements DeepCopyable<SeriesView> {

    @Getter
    private final Long seriesId;

    @Getter
    private final String title;

    @Override
    public SeriesView deepCopy() {
        return this.toBuilder().build();
    }

}
