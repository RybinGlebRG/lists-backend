package ru.rerumu.lists.model.tag.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.model.tag.Tag;
import ru.rerumu.lists.model.tag.TagFactory;
import ru.rerumu.lists.dao.tag.TagsRepository;

@Component
public class TagFactoryImpl implements TagFactory {

    private final TagsRepository tagsRepository;

    @Autowired
    public TagFactoryImpl(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }


    @Override
    public Tag create(String name) {
        Long nextId = tagsRepository.getNextId();

        return new TagImpl(nextId, name, tagsRepository);
    }
}
