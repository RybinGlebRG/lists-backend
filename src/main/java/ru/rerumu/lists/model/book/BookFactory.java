package ru.rerumu.lists.model.book;

import lombok.NonNull;
import ru.rerumu.lists.dao.book.BookDtoDao;
import ru.rerumu.lists.model.user.User;

import java.util.List;

public interface BookFactory {

    Book getBook(Long bookId, Long userId);
    List<Book> findAll(User user, Boolean isChained);
    Book fromDTO(@NonNull BookDTO bookDTO);
    Book fromDTO(@NonNull BookDtoDao bookDTO);

    @Deprecated
    List<Book> fromDTOOld(@NonNull List<BookDTO> bookDTOList);

    List<Book> fromDTO(@NonNull List<BookDtoDao> bookDTOList);
}
