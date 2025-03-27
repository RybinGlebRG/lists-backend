package ru.rerumu.lists.services.series.impl;

import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.model.series.Series;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.services.user.UserService;
import ru.rerumu.lists.services.series.SeriesService;

import java.util.List;

public class SeriesServiceProtectionProxy implements SeriesService {
    private final SeriesService seriesService;
    private final UserService userService;
    private final User authUser;


    public SeriesServiceProtectionProxy(SeriesService seriesService, UserService userService, User authUser) {
        this.seriesService = seriesService;
        this.userService = userService;
        this.authUser = authUser;
    }

    @Override
    public List<Series> getAll(Long readListId) {
        try {
            userService.checkOwnershipList(authUser.name(), readListId);
        } catch (UserIsNotOwnerException e){
            throw new UserPermissionException();
        }
        return seriesService.getAll(readListId);
    }
}
