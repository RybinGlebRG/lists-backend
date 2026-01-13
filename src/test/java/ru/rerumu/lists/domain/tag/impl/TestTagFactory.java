package ru.rerumu.lists.domain.tag.impl;

public class TestTagFactory {

    public static TagImpl prepareTagImpl(
            Long tagId
    ) {
        TagImpl tag = new TagImpl(
                tagId,
                "Test tag",
                null
        );

        return tag;
    }
}
