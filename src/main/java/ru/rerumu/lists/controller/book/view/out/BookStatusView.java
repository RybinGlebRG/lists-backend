package ru.rerumu.lists.controller.book.view.out;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import ru.rerumu.lists.crosscut.DeepCopyable;

@Builder(toBuilder = true, access = AccessLevel.PRIVATE)
@Getter
public class BookStatusView implements DeepCopyable<BookStatusView> {

    private final Integer statusId;
    private final String statusName;

    public BookStatusView(@NonNull Integer statusId, @NonNull String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }

    @Override
    public BookStatusView deepCopy() {
        return this.toBuilder().build();
    }
}
