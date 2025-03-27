package ru.rerumu.lists.model.factories;

import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.services.user.UserService;

public interface UserServiceProxyFactory {

    UserService getUserServiceProtectionProxy(Long authUserId) throws EntityNotFoundException;
}
