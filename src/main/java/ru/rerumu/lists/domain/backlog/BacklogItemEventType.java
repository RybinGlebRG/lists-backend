package ru.rerumu.lists.domain.backlog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public enum BacklogItemEventType {

    MOVE_TO_LIST(0L);

    private final Long id;

    public static BacklogItemEventType findById(@NonNull Long id) {
        for (BacklogItemEventType type: values()) {
            if (id.equals(type.id)) {
                return type;
            }
        }

        return null;
    }

}
