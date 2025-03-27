package ru.rerumu.lists.model.tag.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.tag.Tag;
import ru.rerumu.lists.model.tag.TagDTO;
import ru.rerumu.lists.model.tag.TagFactory;
import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.model.user.UserFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagFactoryImpl implements TagFactory {

    private final TagsRepository tagsRepository;
    private final UserFactory userFactory;

    @Autowired
    public TagFactoryImpl(
            TagsRepository tagsRepository,
            UserFactory userFactory
    ) {
        this.tagsRepository = tagsRepository;
        this.userFactory = userFactory;
    }


    @NonNull
    @Override
    public Tag create(@NonNull String name, @NonNull User user) {
        Long nextId = tagsRepository.getNextId();

        Tag tag = new TagImpl(nextId, name, user, tagsRepository);

        tagsRepository.create(tag);

        return tag;
    }

    @NonNull
    @Override
    public Tag fromDTO(@NonNull TagDTO tagDTO) {
        User user = userFactory.findById(tagDTO.getUserId());

        return new TagImpl(
                tagDTO.getTagId(),
                tagDTO.getName(),
                user,
                tagsRepository
        );
    }

    @Override
    public List<Tag> findALl(@NonNull User user) {
        List<TagDTO> tagDTOs = tagsRepository.findByUser(user);

        return tagDTOs.stream()
                .map(this::fromDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @NonNull
    @Override
    public List<Tag> findByIds(@NonNull List<Long> tagIds, @NonNull User user) {
        List<TagDTO> tagDTOs;

        if (tagIds.isEmpty()) {
            tagDTOs = new ArrayList<>();
        } else {
            tagDTOs = tagsRepository.findByIds(tagIds, user);
            if (tagDTOs == null) {
                throw new ServerException();
            }
        }

        return tagDTOs.stream()
                .map(this::fromDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
