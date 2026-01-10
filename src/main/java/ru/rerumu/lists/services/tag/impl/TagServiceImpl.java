package ru.rerumu.lists.services.tag.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.controller.tag.view.in.TagAddView;
import ru.rerumu.lists.dao.tag.TagsRepository;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.tag.TagService;

import java.util.List;

@Component("TagService")
public class TagServiceImpl implements TagService {

    private final UsersRepository usersRepository;
    private final TagsRepository tagsRepository;

    @Autowired
    public TagServiceImpl(
            UsersRepository usersRepository,
            TagsRepository tagsRepository
    ) {
        this.usersRepository = usersRepository;
        this.tagsRepository = tagsRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOne(TagAddView tagAddView, Long userId) {
        User user = usersRepository.findById(userId);
        tagsRepository.create(tagAddView.getName(), user);
    }

    @Override
    public void deleteOne(Long tagId, Long userId) {
        User user = usersRepository.findById(userId);
        List<Tag> tags = tagsRepository.findByIds(List.of(tagId), user);

        for (Tag tag: tags) {
            tagsRepository.delete(tag.getId(), tag.getUser());
        }
    }

    @Override
    public List<Tag> getAll(Long userId) {
        User user = usersRepository.findById(userId);
        return tagsRepository.findAll(user);
    }

}
