package ru.rerumu.lists.dao.backlog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rerumu.lists.dao.base.EntityDTOv2;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BacklogItemDTO implements EntityDTOv2 {

    private Long id;
    private String title;
    private Long typeId;
    private String note;
    private Long userId;
    private LocalDateTime creationDate;

}
