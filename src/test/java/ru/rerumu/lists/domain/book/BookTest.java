package ru.rerumu.lists.domain.book;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.domain.book.impl.BookImpl;
import ru.rerumu.lists.domain.book.impl.TestBookFactory;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.tag.impl.TagImpl;
import ru.rerumu.lists.domain.tag.impl.TestTagFactory;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class BookTest {

    @Mock
    TagsRepository tagsRepository;

    @Test
    public void shouldUpdateTags(TestInfo testInfo) {
        log.info("Test: {}", testInfo.getDisplayName());

        /*
        Given
         */
        BookImpl bookImpl = TestBookFactory.prepareBookImpl(
                1L,
                List.of(
                        TestTagFactory.prepareTagImpl(1L, tagsRepository),
                        TestTagFactory.prepareTagImpl(2L, tagsRepository),
                        TestTagFactory.prepareTagImpl(3L, tagsRepository)
                )
        );

        List<TagImpl> newTagImpls = List.of(
                TestTagFactory.prepareTagImpl(2L, tagsRepository),
                TestTagFactory.prepareTagImpl(4L, tagsRepository)
        );

        List<Tag> newTags = new ArrayList<>(newTagImpls);


        /*
        When
         */
        ((Book) bookImpl).updateTags(newTags);


        /*
        Then
         */
        List<TagImpl> expectedTags = List.of(
                TestTagFactory.prepareTagImpl(2L, tagsRepository),
                TestTagFactory.prepareTagImpl(4L, tagsRepository)
        );

        Assertions.assertEquals(expectedTags, bookImpl.getTags());

        verify(tagsRepository).add(
                TestTagFactory.prepareTagImpl(4L, tagsRepository),
                bookImpl
        );
        verify(tagsRepository).remove(
                1L,
                1L
        );
        verify(tagsRepository).remove(
                1L,
                3L
        );
    }
}
