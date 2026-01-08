package ru.rerumu.lists.domain.book;

import lombok.NonNull;
import ru.rerumu.lists.crosscut.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.dao.book.BookMyBatisEntity;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.domain.booktype.BookType;
import ru.rerumu.lists.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookFactory {

    Book fromDTO(@NonNull BookDTO bookDTO);
    Book fromDTO(@NonNull BookMyBatisEntity bookDTO);

    @Deprecated
    List<Book> fromDTOOld(@NonNull List<BookDTO> bookDTOList);

    List<Book> fromDTO(@NonNull List<BookMyBatisEntity> bookDTOList);

    Book createBook(
            String title,
            Integer lastChapter,
            String note,
            BookStatusRecord bookStatus,
            LocalDateTime insertDate,
            BookType bookType,
            String URL,
            User user
    ) throws EmptyMandatoryParameterException;
}
