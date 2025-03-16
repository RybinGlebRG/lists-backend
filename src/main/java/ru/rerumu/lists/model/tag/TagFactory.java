package ru.rerumu.lists.model.tag;

import lombok.NonNull;
import ru.rerumu.lists.model.user.User;

import java.util.List;

public interface TagFactory {

    @NonNull
    Tag create(@NonNull String name, @NonNull User user);

    @NonNull
    List<Tag> findByIds(@NonNull List<Long> tagIds, @NonNull User user);

    @NonNull
    Tag fromDTO(@NonNull TagDTO tagDTO);

    List<Tag> findALl(@NonNull User user);
}
