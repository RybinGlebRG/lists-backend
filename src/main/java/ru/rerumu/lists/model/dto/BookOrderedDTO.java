package ru.rerumu.lists.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.rerumu.lists.model.book.BookDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookOrderedDTO {

    public BookDTO bookDTO;
    public Integer order;

}
