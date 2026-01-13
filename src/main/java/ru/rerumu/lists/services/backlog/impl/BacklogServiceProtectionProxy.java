package ru.rerumu.lists.services.backlog.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemCreateView;
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemEventCreateView;
import ru.rerumu.lists.controller.backlog.view.in.BacklogItemUpdateView;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.backlog.BacklogItem;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.backlog.BacklogService;

import java.util.List;

@Service("backlogServiceProtectionProxy")
@Primary
@RequestScope
@Slf4j
public class BacklogServiceProtectionProxy implements BacklogService {

    private final User authUser;
    private final BacklogService backlogService;
    private final UsersRepository usersRepository;

    @Autowired
    public BacklogServiceProtectionProxy(
            @Qualifier("backlogServiceImpl") BacklogService backlogService,
            UsersRepository usersRepository
    ) {
        this.backlogService = backlogService;
        this.usersRepository = usersRepository;

        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        this.authUser = usersRepository.findById(authUserId);
        log .info(String.format("GOT USER %d", authUser.getId()));
    }

    @Override
    public BacklogItem addItemToBacklog(@NonNull Long userId, @NonNull BacklogItemCreateView backlogItemCreateView) {
        checkUser(userId);

        return backlogService.addItemToBacklog(userId, backlogItemCreateView);
    }

    @Override
    public List<BacklogItem> getBacklog(@NonNull Long userId) {
        checkUser(userId);

        return backlogService.getBacklog(userId);
    }

    @Override
    public BacklogItem updateBacklogItem(@NonNull Long userId, @NonNull Long backlogItemId, @NonNull BacklogItemUpdateView backlogItemUpdateView) {
        checkUser(userId);

        return backlogService.updateBacklogItem(userId, backlogItemId, backlogItemUpdateView);
    }

    @Override
    public void deleteBacklogItem(@NonNull Long userId, @NonNull Long backlogItemId) {
        checkUser(userId);

        backlogService.deleteBacklogItem(userId, backlogItemId);
    }

    @Override
    public void processEvent(@NonNull Long userId, @NonNull Long backlogItemId, @NonNull BacklogItemEventCreateView backlogItemEventCreateView) {
        checkUser(userId);
        backlogService.processEvent(userId, backlogItemId, backlogItemEventCreateView);
    }

    private void checkUser(@NonNull Long userId) {
        // Get passed user
        User user = usersRepository.findById(userId);
        log.debug("user: {}", user);

        // Check if actual user has access
        log.debug("authUser: {}", authUser);
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }
    }
}
