package ru.rerumu.lists.dao.tag.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryDtoImpl;
import ru.rerumu.lists.dao.tag.BookTagDTO;
import ru.rerumu.lists.dao.tag.mapper.BookTagMapper;
import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.dao.tag.mapper.TagsMapper;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.book.Book;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.dao.tag.TagDTO;
import ru.rerumu.lists.domain.tag.impl.TagImpl;
import ru.rerumu.lists.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagsRepositoryImpl extends CrudRepositoryDtoImpl<TagDTO,Long> implements TagsRepository {

    private final TagsMapper tagsMapper;
    private final BookTagMapper bookTagMapper;
    private final UsersRepository usersRepository;

    @Autowired
    public TagsRepositoryImpl(TagsMapper tagsMapper, BookTagMapper bookTagMapper, UsersRepository usersRepository) {
        super(tagsMapper);
        this.tagsMapper = tagsMapper;
        this.bookTagMapper = bookTagMapper;
        this.usersRepository = usersRepository;
    }

    @Override
    public void remove(Long bookId, Long tagId) {
            // Removing tag from book
            bookTagMapper.delete(bookId, tagId);
    }

    private void create(Tag tag) {

        tagsMapper.create(new TagDTO(
                tag.getId(),
                tag.getName(),
                tag.getUser().getId()
        ));
    }

    @Override
    public void add(Tag tag, Book book) {
        Long nextId = bookTagMapper.nextval();

        BookTagDTO bookTagDTO = new BookTagDTO(
                nextId,
                book.getId(),
                book.getUser().getId(),
                tag.getId(),
                tag.getUser().getId()
        );
        bookTagMapper.add(bookTagDTO);
    }

    @Override
    public @NonNull Tag create(@NonNull String name, @NonNull User user) {
        Long nextId = getNextId();

        Tag tag = new TagImpl(nextId, name, user);

        create(tag);

        return tag;
    }

    @Override
    public @NonNull Tag attach(@NonNull TagDTO tagDTO) {
        User user = usersRepository.findById(tagDTO.getUserId());

        return new TagImpl(
                tagDTO.getTagId(),
                tagDTO.getName(),
                user
        );
    }

    @Override
    public List<Tag> findAll(@NonNull User user) {
        List<TagDTO> tagDTOs = findByUser(user);

        return tagDTOs.stream()
                .map(tagDTO -> new TagImpl(
                        tagDTO.getTagId(),
                        tagDTO.getName(),
                        user
                ))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public @NonNull List<Tag> findByIds(@NonNull List<Long> tagIds, @NonNull User user) {
        List<TagDTO> tagDTOs;

        if (tagIds.isEmpty()) {
            tagDTOs = new ArrayList<>();
        } else {
            tagDTOs = tagsMapper.findByIds(tagIds, user.getId());
            if (tagDTOs == null) {
                throw new ServerException();
            }
        }

        return tagDTOs.stream()
                .map(this::attach)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
