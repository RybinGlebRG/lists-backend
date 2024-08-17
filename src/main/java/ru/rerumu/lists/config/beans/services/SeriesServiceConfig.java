package ru.rerumu.lists.config.beans.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.SeriesBooksRespository;
import ru.rerumu.lists.repository.SeriesRepository;
import ru.rerumu.lists.services.*;

import java.util.Optional;

@Configuration
@Slf4j
public class SeriesServiceConfig {

    @Bean("seriesService")
    public SeriesService seriesService(
            SeriesServiceImpl seriesServiceImpl
    ){
        return seriesServiceImpl;
    }

    @Bean("seriesServiceImpl")
    public SeriesServiceImpl seriesServiceImpl(
            SeriesRepository seriesRepository,
            BookSeriesRelationService bookSeriesRelationService,
            SeriesBooksRespository seriesBooksRespository,
            ReadListService readListService
    ){
        return new SeriesServiceImpl(
                seriesRepository,
                bookSeriesRelationService,
                seriesBooksRespository,
                readListService
        );
    }

    @Bean("seriesServiceProtectionProxy")
    @Primary
    @RequestScope
    public SeriesService seriesServiceProtectionProxy(
            @Qualifier("seriesService") SeriesService seriesService,
            UserService userService
    ) throws EntityNotFoundException {
        Long authUserId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("authUserId", RequestAttributes.SCOPE_REQUEST);
        Optional<User> authUser = userService.getOne(authUserId);
        log.info(String.format("GOT USER %d", authUser.orElseThrow().userId()));
        var seriesServiceProtectionProxy = new SeriesServiceProtectionProxy(
                seriesService,
                userService,
                authUser.orElseThrow()
        );
        return seriesServiceProtectionProxy;
    }
}
