package ru.rerumu.lists.config;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.rerumu.lists.exception.NoJWTException;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.services.UserServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private byte[] secret;

    @Autowired
    private UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {


        try {
            final String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            if (header==null){
                throw new NoJWTException();
            }
            final String token = header.split(" ")[1].trim();
            String identity = userService.checkTokenAndGetIdentity(token);
            User user = userService.checkTokenAndGetUser(token);
            httpServletRequest.setAttribute("username",user.name());
            httpServletRequest.setAttribute("authUserId",user.userId());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(identity,null, new ArrayList<>()));
        }
        catch (ExpiredJwtException e){
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (NoJWTException e){
        }


        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
