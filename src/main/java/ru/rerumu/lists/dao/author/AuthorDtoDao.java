package ru.rerumu.lists.dao.author;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.rerumu.lists.dao.user.UserDtoDao;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AuthorDtoDao {
    private Long authorId;
    private String name;
    private UserDtoDao user;
}
