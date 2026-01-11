package ru.rerumu.lists.services.tag.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.controller.tag.view.in.TagAddView;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.tag.Tag;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.tag.TagService;

import java.util.List;

@Service("TagServiceProtectionProxy")
@Primary
@RequestScope
@Slf4j
public class TagServiceProtectionProxy implements TagService {

    private final TagService tagService;
    private final User authUser;
    private final UsersRepository usersRepository;

    @Autowired
    public TagServiceProtectionProxy(
            @Qualifier("TagService") TagService tagService,
            UsersRepository usersRepository
    ) {
        this.tagService = tagService;
        this.usersRepository = usersRepository;

        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        authUser = usersRepository.findById(authUserId);
        log.info(String.format("GOT USER %d", authUser.getId()));
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
