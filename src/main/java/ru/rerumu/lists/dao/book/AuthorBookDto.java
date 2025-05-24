package ru.rerumu.lists.dao.book;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.rerumu.lists.dao.author.AuthorDtoDao;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class AuthorBookDto {

    private Long bookId;
    private AuthorDtoDao authorDtoDao;
    private Long userId;
    private Long roleId;

}
