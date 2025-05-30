package ru.rerumu.lists.model.book.impl;


import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.model.author.AuthorFactory;
import ru.rerumu.lists.model.book.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.model.book.readingrecords.status.StatusFactory;
import ru.rerumu.lists.model.tag.Tag;
import ru.rerumu.lists.model.user.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;

public class TestBookFactory {

    public static BookImpl prepareBookImpl(
            Long bookId,
            List<Tag> tags
    ) {
        BookImpl bookImpl = new BookBuilder(
                mock(StatusFactory.class),
                mock(DateFactory.class),
                mock(ReadingRecordFactory.class),
                mock(BookRepository.class),
                mock(AuthorsBooksRepository.class),
                mock(AuthorFactory.class)
        )
                .bookId(bookId)
                .title("Test book")
                .insertDate(new Date())
                .lastUpdateDate(LocalDateTime.now())
                .tags(tags)
                .user(mock(User.class))
                .build();

        return bookImpl;
    }
}
