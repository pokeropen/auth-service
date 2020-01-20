package com.open.poker.controller;

import com.open.poker.constants.Constants;
import com.open.poker.jwt.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.vavr.control.Try;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@RestController
@Flogger
@RequestMapping("/authenticate")
public class AuthenticateResource {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/{username}")
    @ApiOperation(value = "Authenticate User on basis of JWT", response = Boolean.class)
    public ResponseEntity<Boolean> authenticate(
            @ApiParam(required = true, value = "A Valid JWT")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization,
            @ApiParam(required = true, value = "User trying to log-in")
            @PathVariable("username") @NotBlank @Size(min = 4, max = 20) String username) {

        log.atInfo().log("Authenticating User %s", username);

        if (Objects.nonNull(authorization) && authorization.startsWith(Constants.BEARER) && jwtTokenUtil.validateToken(authorization.substring(7), username)) {
           return ResponseEntity.ok(true);
       } else {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
       }
    }
}
