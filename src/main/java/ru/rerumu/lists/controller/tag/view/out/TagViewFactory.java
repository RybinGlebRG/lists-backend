package ru.rerumu.lists.controller.tag.view.out;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.tag.Tag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TagViewFactory {

    public TagView buildTagView(@NonNull Tag tag) {
        return new TagView(
                tag.getId(),
                tag.getName()
        );
    }

    public TagListView buildTagListView(@NonNull List<Tag> tags) {
        Comparator<Tag> tagComparator = Comparator.comparing(Tag::getName);

        List<Tag> tagList = new ArrayList<>(tags);
        tagList.sort(tagComparator);

        return new TagListView(
                tags.stream()
                        .sorted(tagComparator)
                        .map(this::buildTagView)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }

}
