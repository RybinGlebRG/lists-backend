package ru.rerumu.lists.domain.factories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.dao.user.mapper.UserMapper;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryEntityImpl;
import ru.rerumu.lists.services.user.impl.UserServiceImpl;

@Component
public class UserServiceImplFactory {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

//    @Value("${jwt.secret}")
    private final byte[] jwtSecret;

    public UserServiceImplFactory(UsersRepository usersRepository, UserMapper userMapper, @Value("${jwt.secret}") byte[] jwtSecret) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
        this.jwtSecret = jwtSecret;
    }

    public UserServiceImpl getUSerServiceImpl(){
        CrudRepositoryEntityImpl<User,Long> usersRepositoryImpl = new CrudRepositoryEntityImpl<>(userMapper);
        return new UserServiceImpl(
                usersRepository,
                usersRepositoryImpl,
                jwtSecret
        );
    }
}
