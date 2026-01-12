package ru.rerumu.lists.controller.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rerumu.lists.controller.users.views.out.UserView;
import ru.rerumu.lists.crosscut.exception.ServerException;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.user.UserService;

@CrossOrigin
@RestController
public class UsersController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UsersController(@Qualifier("UserService")  UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(
            value = "/api/v1/users/tokens",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> createToken(@RequestBody TokenRequest tokenRequest) {
        ResponseEntity<String> resEnt;
        try {
            String token = userService.createToken(tokenRequest);
            User user = userService.findByToken(token);
            UserView userView = new UserView(user.getId(), user.getName(), token);
            resEnt = new ResponseEntity<>(objectMapper.writeValueAsString(userView), HttpStatus.OK);
        } catch (Exception e){
            throw new ServerException(e.getMessage(), e);
        }
        return resEnt;
    }
}

