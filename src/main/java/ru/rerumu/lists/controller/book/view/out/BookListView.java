package ru.rerumu.lists.controller.book.view.out;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.rerumu.lists.controller.DeepCopyable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
@Builder(toBuilder = true, access = AccessLevel.PRIVATE)
public class BookListView implements DeepCopyable<BookListView> {

    private final List<BookView> items;

    public BookListView(@NonNull List<BookView> items) {
        this.items = new ArrayList<>(items);
    }

    public List<BookView> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public BookListView deepCopy() {
        return this.toBuilder()
                .items(items.stream()
                        .map(BookView::deepCopy)
                        .collect(Collectors.toCollection(ArrayList::new))
                )
                .build();
    }
}
