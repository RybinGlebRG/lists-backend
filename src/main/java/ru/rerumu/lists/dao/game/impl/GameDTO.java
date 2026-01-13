package ru.rerumu.lists.dao.game.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GameDTO {

    private Long gameId;
    private Long userId;
    private String title;
    private LocalDateTime insertDate;
    private String note;

}
