package ru.rerumu.lists.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import ru.rerumu.lists.crosscut.exception.BadRequestException;
import ru.rerumu.lists.crosscut.exception.UnauthorizedException;

@Slf4j
public class AuthUserParser {

    public static Long getAuthUser(RequestAttributes requestAttributes) {
        Object value = requestAttributes.getAttribute("authUserId", RequestAttributes.SCOPE_REQUEST);
        log.info("value: '{}'; type: {}", value, value != null ? value.getClass() : null);

        Long authUserId;
        if (value instanceof Long longValue) {
            authUserId = longValue;
        } else if (value instanceof String stringValue) {
            authUserId = Long.valueOf(stringValue);
        } else if (value == null) {
            throw new UnauthorizedException();
        } else {
            throw new BadRequestException();
        }

        log.info("Got authUserId: {}", authUserId);

        return authUserId;
    }

}
