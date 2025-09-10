package ru.rerumu.lists.dao.book.readingrecord.status;

import lombok.NonNull;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;

public interface StatusRepository {

    @NonNull
    BookStatusRecord findById(@NonNull Long statusId);
}
