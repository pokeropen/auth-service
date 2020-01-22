package com.open.poker.controller;

import com.open.poker.constants.Constants;
import com.open.poker.exception.InvalidJwtTokenException;
import com.open.poker.jwt.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.vavr.control.Try;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@Flogger
@RequestMapping("/authenticate")
public class AuthenticateResource {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping
    @ApiOperation(value = "Authenticate User on basis of JWT", response = Boolean.class)
    public ResponseEntity<Boolean> authenticate(
            @ApiParam(required = true, value = "A Valid JWT")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {

        log.atInfo().log("Authenticating User for token %s", authorization);

        if (Objects.nonNull(authorization) && authorization.startsWith(Constants.BEARER) && Try.of(() -> jwtTokenUtil.validateToken(authorization.substring(7)))
                .getOrElseThrow(e -> new InvalidJwtTokenException(e.getMessage(), e))) {
           return ResponseEntity.ok(true);
       } else {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
       }
    }
}
