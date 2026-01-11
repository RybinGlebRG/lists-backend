package ru.rerumu.lists.services.series.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.controller.series.views.BookSeriesAddView;
import ru.rerumu.lists.controller.series.views.in.SeriesUpdateView;
import ru.rerumu.lists.crosscut.exception.NotImplementedException;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.series.SeriesService;
import ru.rerumu.lists.services.user.UserService;

import java.util.List;

@Service("seriesServiceProtectionProxy")
@Primary
@RequestScope
@Slf4j
public class SeriesServiceProtectionProxy implements SeriesService {

    private final SeriesService seriesService;
    private final User authUser;
    private final UsersRepository usersRepository;

    @Autowired
    public SeriesServiceProtectionProxy(
            @Qualifier("seriesServiceImpl") SeriesService seriesService,
            UsersRepository usersRepository
    ) {
        this.seriesService = seriesService;
        this.usersRepository = usersRepository;

        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        authUser = usersRepository.findById(authUserId);
        log.info(String.format("GOT USER %d", authUser.getId()));
    }

    @Override
    public List<Series> findAll(Long userId) {
        // Get passed user
        User user = usersRepository.findById(userId);

        // Check if actual user has access
        if (!user.equals(authUser)) {
            throw new UserPermissionException();
        }

        return seriesService.findAll(userId);
    }

    @Override
    public Series findById(Long seriesId, Long userId) {
        throw new NotImplementedException();
    }

    @Override
    public Series add(Long userId, BookSeriesAddView bookSeriesAddView) {
        // Get passed user
        User user = usersRepository.findById(userId);

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
