package ru.rerumu.lists.services.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.tag.TagService;
import ru.rerumu.lists.services.tag.impl.TagServiceProtectionProxy;
import ru.rerumu.lists.services.user.UserService;

@Configuration
@Slf4j
public class TagServiceConfig {

    @Bean("TagServiceProtectionProxy")
    @Primary
    @RequestScope
    public TagService getTagServiceProtectionProxy(
        @Qualifier("TagService") TagService tagService,
        UserService userService,
        UsersRepository usersRepository
    ) {
        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        User authUser = userService.findById(authUserId);
        log .info(String.format("GOT USER %d", authUser.getId()));
        return new TagServiceProtectionProxy(tagService, authUser, usersRepository);
    }
 }
