package ru.rerumu.lists.controller.backlog.view.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.backlog.BacklogItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BacklogViewFactory {

    public BacklogOutView build(List<BacklogItem> backlogItems) {
        List<BacklogItemOutView> backlogItemOutViews = backlogItems.stream()
                .map(item -> new BacklogItemOutView(
                        item.getTitle(),
                        item.getType(),
                        item.getNote(),
                        item.getCreationDate()
                ))
                .collect(Collectors.toCollection(ArrayList::new));
        return new BacklogOutView(backlogItemOutViews);
    }

}
