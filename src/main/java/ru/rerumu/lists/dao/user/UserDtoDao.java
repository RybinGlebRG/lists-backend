package ru.rerumu.lists.dao.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoDao {

    private Long userId;
    private String name;
    private String password;
    private String refreshTokenId;

}
