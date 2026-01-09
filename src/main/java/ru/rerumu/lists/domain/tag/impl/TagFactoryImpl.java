package ru.rerumu.lists.domain.tag.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.tag.TagDTO;
import ru.rerumu.lists.domain.tag.TagFactory;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagFactoryImpl implements TagFactory {

    private final TagsRepository tagsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public TagFactoryImpl(
            TagsRepository tagsRepository,
            UsersRepository usersRepository
    ) {
        this.tagsRepository = tagsRepository;
        this.usersRepository = usersRepository;
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
        User user = usersRepository.findById(tagDTO.getUserId());

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
