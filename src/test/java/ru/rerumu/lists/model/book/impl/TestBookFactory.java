package ru.rerumu.lists.model.book.impl;


import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.model.tag.Tag;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class TestBookFactory {

    public static BookImpl prepareBookImpl(
            Long bookId,
            List<Tag> tags
    ) {
        BookImpl bookImpl = new BookBuilder(null)
                .bookId(bookId)
                .title("Test book")
                .insertDate(new Date())
                .lastUpdateDate(LocalDateTime.now())
                .tags(tags)
                .build();

        return bookImpl;
    }
}
