package ru.rerumu.lists.services.series.impl;

import ru.rerumu.lists.controller.series.view.in.SeriesUpdateView;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;
import ru.rerumu.lists.services.series.SeriesService;
import ru.rerumu.lists.services.user.UserService;
import ru.rerumu.lists.views.BookSeriesAddView;

import java.util.List;

public class SeriesServiceProtectionProxy implements SeriesService {
    private final SeriesService seriesService;
    private final UserService userService;
    private final User authUser;
    private final UserFactory userFactory;


    public SeriesServiceProtectionProxy(SeriesService seriesService, UserService userService, User authUser, UserFactory userFactory) {
        this.seriesService = seriesService;
        this.userService = userService;
        this.authUser = authUser;
        this.userFactory = userFactory;
    }

    @Override
    public List<Series> findAll(Long readListId) {
        throw new NotImplementedException();
//        try {
//            userService.checkOwnershipList(authUser.name(), readListId);
//        } catch (UserIsNotOwnerException e){
//            throw new UserPermissionException();
//        }
//        return seriesService.findAll(readListId);
    }

    @Override
    public Series findById(Long seriesId, Long userId) {
        throw new NotImplementedException();
    }

    @Override
    public Series add(Long userId, BookSeriesAddView bookSeriesAddView) {
        // Get passed user
        User user = userFactory.findById(userId);

        // Check if actual user has access
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }

        return seriesService.add(userId, bookSeriesAddView);
    }

    @Override
    public void delete(Long seriesId, Long userId) {
        throw new NotImplementedException();
    }

    @Override
    public void updateSeries(Long seriesId, Long userId, SeriesUpdateView seriesUpdateView) {
        throw new NotImplementedException();
    }

//    @Override
//    public List<Series> findByBook(Book book, Long userId) {
//        throw new NotImplementedException();
//    }
}
