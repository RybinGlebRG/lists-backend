package ru.rerumu.lists.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.rerumu.lists.domain.book.BookDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class BookOrderedDTO {

    public BookDTO bookDTO;
    public Integer order;

}
