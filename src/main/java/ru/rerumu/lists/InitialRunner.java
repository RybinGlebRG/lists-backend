package ru.rerumu.lists;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.services.user.UserService;

import java.util.UUID;

@Component
@Slf4j
public class InitialRunner implements CommandLineRunner {
    private final static String DEFAULT_USERNAME = "admin";
    private final static Long DEFAULT_USER_ID = 0L;


    private final UserService userService;


    public InitialRunner(
            @Qualifier("UserService") UserService userService
    ) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            userService.findById(DEFAULT_USER_ID);
        } catch (EntityNotFoundException e) {
            // If failed to find default user
            // TODO: Maybe more specific exception?
            String defaultPassword = UUID.randomUUID().toString();
            log.warn(String.format("Initial username = '%s'", DEFAULT_USERNAME));
            log.warn(String.format("Initial password = '%s'", defaultPassword));

            userService.create(DEFAULT_USER_ID, DEFAULT_USERNAME, defaultPassword.toCharArray());
        }


    }
}
