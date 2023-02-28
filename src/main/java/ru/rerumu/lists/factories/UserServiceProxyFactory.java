package ru.rerumu.lists.factories;

import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.services.UserService;

public interface UserServiceProxyFactory {

    UserService getUserServiceProtectionProxy(Long authUserId) throws EntityNotFoundException;
}
