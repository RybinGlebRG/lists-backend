package ru.rerumu.lists.model.tag.impl;

import ru.rerumu.lists.dao.tag.TagsRepository;

public class TestTagFactory {

    public static TagImpl prepareTagImpl(
            Long tagId,
            TagsRepository tagsRepository
    ) {
        TagImpl tag = new TagImpl(
                tagId,
                "Test tag",
                null,
                tagsRepository
        );

        return tag;
    }
}
