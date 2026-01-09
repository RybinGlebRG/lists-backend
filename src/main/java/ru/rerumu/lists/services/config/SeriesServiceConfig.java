package ru.rerumu.lists.services.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.series.SeriesService;
import ru.rerumu.lists.services.series.impl.SeriesServiceProtectionProxy;
import ru.rerumu.lists.services.user.UserService;

@Configuration
@Slf4j
public class SeriesServiceConfig {

    @Bean("seriesServiceProtectionProxy")
    @Primary
    @RequestScope
    public SeriesService seriesServiceProtectionProxy(
            @Qualifier("seriesServiceImpl") SeriesService seriesService,
            UserService userService,
            UsersRepository usersRepository
    ) throws EntityNotFoundException {
        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        User authUser = userService.getOne(authUserId);
        log.info(String.format("GOT USER %d", authUser.getId()));
        var seriesServiceProtectionProxy = new SeriesServiceProtectionProxy(
                seriesService,
                userService,
                authUser,
                usersRepository
        );
        return seriesServiceProtectionProxy;
    }
}
