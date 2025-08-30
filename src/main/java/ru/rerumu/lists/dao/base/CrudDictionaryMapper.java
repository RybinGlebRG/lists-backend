package ru.rerumu.lists.dao.base;

public interface CrudDictionaryMapper<T,ID> extends CrudMapper<T, ID, T>{

    T findById(ID id);

}
