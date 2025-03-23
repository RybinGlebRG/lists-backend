package ru.rerumu.lists.services.tag.impl;

import ru.rerumu.lists.controller.tag.view.in.TagAddView;
import ru.rerumu.lists.exception.UserPermissionException;
import ru.rerumu.lists.model.tag.Tag;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.user.UserFactory;
import ru.rerumu.lists.services.tag.TagService;

import java.util.List;

public class TagServiceProtectionProxy implements TagService {

    private final TagService tagService;
    private final User authUser;
    private final UserFactory userFactory;

    public TagServiceProtectionProxy(TagService tagService, User authUser, UserFactory userFactory) {
        this.tagService = tagService;
        this.authUser = authUser;
        this.userFactory = userFactory;
    }

    @Override
    public void addOne(TagAddView tagAddView, Long userId) {
        User user = userFactory.findById(userId);

        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }

        tagService.addOne(tagAddView, userId);
    }

    @Override
    public void deleteOne(Long tagId, Long userId) {
        User user = userFactory.findById(userId);

        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }

        tagService.deleteOne(tagId, userId);
    }

    @Override
    public List<Tag> getAll(Long userId) {
        User user = userFactory.findById(userId);
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }

        return tagService.getAll(userId);
    }
}
