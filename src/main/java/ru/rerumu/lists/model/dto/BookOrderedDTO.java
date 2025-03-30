package ru.rerumu.lists.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.rerumu.lists.model.book.BookDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class BookOrderedDTO {

    public BookDTO bookDTO;
    public Integer order;

}
