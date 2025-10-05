package ru.rerumu.lists.controller.backlog.view.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.backlog.BacklogItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BacklogViewFactory {

    public BacklogOutView build(List<BacklogItem> backlogItems) {

        Comparator<BacklogItemOutView> backlogItemOutViewComparator = Comparator.comparing(BacklogItemOutView::getCreationDate);

        List<BacklogItemOutView> backlogItemOutViews = backlogItems.stream()
                .map(this::build)
                .sorted(backlogItemOutViewComparator)
                .collect(Collectors.toCollection(ArrayList::new));
        return new BacklogOutView(backlogItemOutViews);
    }

    public BacklogItemOutView build(BacklogItem backlogItem) {
        return new BacklogItemOutView(
                backlogItem.getId(),
                backlogItem.getTitle(),
                backlogItem.getType().getId(),
                backlogItem.getNote(),
                backlogItem.getCreationDate()
        );
    }

}
