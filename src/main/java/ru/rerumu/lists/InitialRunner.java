package ru.rerumu.lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.services.user.UserService;

import java.util.Optional;
import java.util.UUID;

@Component
public class InitialRunner implements CommandLineRunner {
    private final static String DEFAULT_USERNAME ="admin";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final UserService userService;


    public InitialRunner(
            @Qualifier("UserService") UserService userService
    ) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<User> user = userService.getOne(0L);
        if (user.isPresent()){
            return;
        }
        String defaultPassword = UUID.randomUUID().toString();
        logger.error(String.format("Initial username = '%s'", DEFAULT_USERNAME));
        logger.error(String.format("Initial password = '%s'", defaultPassword));

        userService.add(new User(0L, DEFAULT_USERNAME, defaultPassword));

    }
}
