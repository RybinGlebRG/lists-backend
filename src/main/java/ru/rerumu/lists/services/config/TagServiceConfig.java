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
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.tag.TagFactory;
import ru.rerumu.lists.model.user.UserFactory;
import ru.rerumu.lists.services.tag.TagService;
import ru.rerumu.lists.services.user.UserService;
import ru.rerumu.lists.services.tag.impl.TagServiceImpl;
import ru.rerumu.lists.services.tag.impl.TagServiceProtectionProxy;

@Configuration
@Slf4j
public class TagServiceConfig {

    @Bean("TagService")
    public TagService getTagService(
        TagFactory tagFactory,
        UserFactory userFactory
    ) {
        return new TagServiceImpl(tagFactory, userFactory);
    }

    @Bean("TagServiceProtectionProxy")
    @Primary
    @RequestScope
    public TagService getTagServiceProtectionProxy(
        @Qualifier("TagService") TagService tagService,
        UserService userService,
        UserFactory userFactory
    ) {
        Long authUserId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("authUserId", RequestAttributes.SCOPE_REQUEST);
        User authUser = userService.getOne(authUserId).orElseThrow(EntityNotFoundException::new);
        log .info(String.format("GOT USER %d", authUser.userId()));
        return new TagServiceProtectionProxy(tagService, authUser, userFactory);
    }
 }
