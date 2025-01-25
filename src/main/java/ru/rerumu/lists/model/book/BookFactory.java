package ru.rerumu.lists.model.book;

import lombok.NonNull;
import ru.rerumu.lists.model.book.reading_records.ReadingRecord;

import java.util.List;
import java.util.Map;

public interface BookFactory {

    Book getBook(Long bookId);
    List<Book> getAllChained(Long readListId);
    List<Book> getAll(Long readListId);
    Book fromDTO(@NonNull BookDTO bookDTO);
    List<Book> fromDTO(@NonNull List<BookDTO> bookDTOList);
}
