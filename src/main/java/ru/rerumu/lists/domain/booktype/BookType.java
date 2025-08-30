package ru.rerumu.lists.domain.booktype;

import org.json.JSONObject;
import ru.rerumu.lists.domain.base.Dictionary;

public interface BookType extends Dictionary {

    BookTypeDTO toDTO();

    String getName();

    JSONObject toJsonObject();

}
