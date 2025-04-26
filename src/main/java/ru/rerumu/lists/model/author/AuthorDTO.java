package ru.rerumu.lists.model.author;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.rerumu.lists.model.user.UserDTO;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AuthorDTO {
    private final Long authorId;
    private final Long readListId;
    private final String name;
    private final UserDTO user;


}
