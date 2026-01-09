package ru.rerumu.lists.services.tag.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.controller.tag.view.in.TagAddView;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.tag.TagFactory;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.tag.TagService;

import java.util.List;

@Component("TagService")
public class TagServiceImpl implements TagService {

    private final TagFactory tagFactory;
    private final UsersRepository usersRepository;

    public TagServiceImpl(
            TagFactory tagFactory,
            UsersRepository usersRepository
    ) {
        this.tagFactory = tagFactory;
        this.usersRepository = usersRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOne(TagAddView tagAddView, Long userId) {
        User user = usersRepository.findById(userId);
        tagFactory.create(tagAddView.getName(), user);
    }

    @Override
    public void deleteOne(Long tagId, Long userId) {
        User user = usersRepository.findById(userId);
        List<Tag> tags = tagFactory.findByIds(List.of(tagId), user);

        for (Tag tag: tags) {
            tag.delete();
        }
    }

    @Override
    public List<Tag> getAll(Long userId) {
        User user = usersRepository.findById(userId);
        return tagFactory.findALl(user);
    }

}
