package ru.rerumu.lists.controller.tag.view.out;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.rerumu.lists.crosscut.DeepCopyable;

@EqualsAndHashCode
@ToString
@Getter
@Builder(toBuilder = true, access = AccessLevel.PRIVATE)
public class TagView implements DeepCopyable<TagView> {

    private final Long tagId;
    private final String name;

    public TagView(Long tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

    @Override
    public TagView deepCopy() {
        return this.toBuilder().build();
    }
}
