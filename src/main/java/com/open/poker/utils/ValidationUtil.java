package com.open.poker.utils;

import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.open.poker.constants.Constants.BEARER;

@Component
public class ValidationUtil {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Try<Boolean> isValidToken(final String authToken) {
        return Try.of(() -> Objects.nonNull(authToken) && authToken.startsWith(BEARER) && jwtTokenUtil.validateToken(authToken.substring(7)));
    }
}
