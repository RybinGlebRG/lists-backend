package ru.rerumu.lists.dao.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class BookOrderedDtoDao {

    public BookDtoDao bookDTO;
    public Integer order;

}
