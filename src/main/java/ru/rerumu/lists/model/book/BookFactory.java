package ru.rerumu.lists.model.book;

import lombok.NonNull;

import java.util.List;

public interface BookFactory {

    Book getBook(Long bookId);
    List<Book> getAllChained(Long readListId);
    List<Book> getAll(Long readListId);
    Book fromDTO(@NonNull BookDTO bookDTO);
    List<Book> fromDTO(@NonNull List<BookDTO> bookDTOList);
}
