package com.open.poker.controller;

import com.open.poker.utils.JwtUtil;
import com.open.poker.utils.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@Flogger
@RequestMapping("/authorize")
@Api(value = "Authorize user request on basis of Jwt Token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
public class AuthorizeResource {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ValidationUtil validationUtil;

    @GetMapping
    @ApiOperation(value = "Authorize User on basis of JWT", response = Boolean.class)
    public ResponseEntity<Boolean> authorize(
            @ApiParam(required = true, value = "A Valid JWT")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken) {

        log.atInfo().log("Authenticating User for token %s", authToken);

        return validationUtil.isValidToken(authToken).fold(x -> ResponseEntity.badRequest().body(false),
                x -> x ? ResponseEntity.ok(true) : ResponseEntity.status(FORBIDDEN).body(false));
    }
}
