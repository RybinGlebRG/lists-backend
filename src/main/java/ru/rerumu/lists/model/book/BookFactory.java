package ru.rerumu.lists.model.book;

import lombok.NonNull;
import ru.rerumu.lists.dao.book.BookDtoDao;
import ru.rerumu.lists.model.user.User;

import java.util.List;

public interface BookFactory {

    Book getBook(Long bookId);
    List<Book> getAllChained(Long readListId);
    List<Book> findAll(User user, Boolean isChained);
    List<Book> getAll(Long readListId);
    Book fromDTO(@NonNull BookDTO bookDTO);
    Book fromDTO(@NonNull BookDtoDao bookDTO);
    List<Book> fromDTO(@NonNull List<BookDTO> bookDTOList);
}
