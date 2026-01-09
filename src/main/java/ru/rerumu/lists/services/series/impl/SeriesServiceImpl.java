package ru.rerumu.lists.services.series.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.controller.series.view.in.SeriesUpdateView;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.domain.series.Series;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;
import ru.rerumu.lists.services.series.SeriesService;
import ru.rerumu.lists.views.BookSeriesAddView;

import java.util.List;

@Component("seriesServiceImpl")
@Slf4j
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;
    private final UserFactory userFactory;

    public SeriesServiceImpl(
            SeriesRepository seriesRepository,
            UserFactory userFactory
    ) {
        this.seriesRepository = seriesRepository;
        this.userFactory = userFactory;
    }

    @Override
    public List<Series> findAll(@NonNull Long userId) {
        User user = userFactory.findById(userId);
        return seriesRepository.findByUser(user);
    }

    @Override
    public Series findById(@NonNull Long seriesId, @NonNull Long userId) {
        User user = userFactory.findById(userId);
        return seriesRepository.findById(seriesId, user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Series add(Long userId, BookSeriesAddView bookSeriesAddView) {
        User user = userFactory.findById(userId);
        return seriesRepository.create(bookSeriesAddView.getTitle(), user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long seriesId, Long userId) {
        seriesRepository.delete(seriesId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSeries(Long seriesId, Long userId, SeriesUpdateView seriesUpdateView) {
        Series series = findById(seriesId, userId);
        seriesRepository.save(series);
    }
}
