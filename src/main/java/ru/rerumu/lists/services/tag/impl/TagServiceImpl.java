package ru.rerumu.lists.services.tag.impl;

import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.controller.tag.view.in.TagAddView;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.tag.Tag;
import ru.rerumu.lists.model.tag.TagFactory;
import ru.rerumu.lists.model.user.UserFactory;
import ru.rerumu.lists.services.tag.TagService;

import java.util.List;

public class TagServiceImpl implements TagService {

    private final TagFactory tagFactory;
    private final UserFactory userFactory;

    public TagServiceImpl(TagFactory tagFactory, UserFactory userFactory) {
        this.tagFactory = tagFactory;
        this.userFactory = userFactory;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOne(TagAddView tagAddView, Long userId) {
        User user = userFactory.findById(userId);
        tagFactory.create(tagAddView.getName(), user);
    }

    @Override
    public void deleteOne(Long tagId, Long userId) {
        User user = userFactory.findById(userId);
        List<Tag> tags = tagFactory.findByIds(List.of(tagId), user);

        for (Tag tag: tags) {
            tag.delete();
        }
    }

    @Override
    public List<Tag> getAll(Long userId) {
        User user = userFactory.findById(userId);
        return tagFactory.findALl(user);
    }

}
