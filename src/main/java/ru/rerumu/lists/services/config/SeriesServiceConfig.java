package ru.rerumu.lists.services.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.repository.SeriesBooksRespository;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.domain.series.impl.SeriesFactoryImpl;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;
import ru.rerumu.lists.services.BookSeriesRelationService;
import ru.rerumu.lists.services.book.impl.ReadListService;
import ru.rerumu.lists.services.series.SeriesService;
import ru.rerumu.lists.services.series.impl.SeriesServiceImpl;
import ru.rerumu.lists.services.series.impl.SeriesServiceProtectionProxy;
import ru.rerumu.lists.services.user.UserService;

@Configuration
@Slf4j
public class SeriesServiceConfig {

//    @Bean("seriesServiceImpl")
//    public SeriesService seriesService(
//            SeriesServiceImpl seriesServiceImpl
//    ){
//        return seriesServiceImpl;
//    }

    @Bean("seriesServiceImpl")
    public SeriesServiceImpl seriesServiceImpl(
            SeriesRepository seriesRepository,
            BookSeriesRelationService bookSeriesRelationService,
            SeriesBooksRespository seriesBooksRespository,
            ReadListService readListService,
            SeriesFactoryImpl seriesFactory,
            UserFactory userFactory
    ){
        return new SeriesServiceImpl(
                seriesRepository,
                bookSeriesRelationService,
                seriesBooksRespository,
                readListService,
                seriesFactory,
                userFactory
        );
    }

    @Bean("seriesServiceProtectionProxy")
    @Primary
    @RequestScope
    public SeriesService seriesServiceProtectionProxy(
            @Qualifier("seriesServiceImpl") SeriesService seriesService,
            UserService userService
    ) throws EntityNotFoundException {
        Long authUserId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("authUserId", RequestAttributes.SCOPE_REQUEST);
        User authUser = userService.getOne(authUserId);
        log.info(String.format("GOT USER %d", authUser.userId()));
        var seriesServiceProtectionProxy = new SeriesServiceProtectionProxy(
                seriesService,
                userService,
                authUser
        );
        return seriesServiceProtectionProxy;
    }
}
