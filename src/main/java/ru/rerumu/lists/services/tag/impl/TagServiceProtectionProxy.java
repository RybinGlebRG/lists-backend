package ru.rerumu.lists.services.tag.impl;

import ru.rerumu.lists.controller.tag.view.in.TagAddView;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.tag.TagService;

import java.util.List;

public class TagServiceProtectionProxy implements TagService {

    private final TagService tagService;
    private final User authUser;
    private final UsersRepository usersRepository;

    public TagServiceProtectionProxy(TagService tagService, User authUser, UsersRepository usersRepository) {
        this.tagService = tagService;
        this.authUser = authUser;
        this.usersRepository = usersRepository;
    }

    @Override
    public void addOne(TagAddView tagAddView, Long userId) {
        User user = usersRepository.findById(userId);

        if (!user.equals(authUser)){
            throw new UserPermissionException();
        }

        tagService.addOne(tagAddView, userId);
    }

    @Override
    public void deleteOne(Long tagId, Long userId) {
        User user = usersRepository.findById(userId);

        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }

        tagService.deleteOne(tagId, userId);
    }

    @Override
    public List<Tag> getAll(Long userId) {
        User user = usersRepository.findById(userId);
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }

        return tagService.getAll(userId);
    }
}
