package com.open.poker.controller;

import com.open.poker.utils.JwtUtil;
import com.open.poker.utils.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@Flogger
@RequestMapping("/authorize")
@Tag(name = "Authorization Resource", description = "Authorize user request on basis of Jwt Token")
public class AuthorizeResource {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ValidationUtil validationUtil;

    @GetMapping
    @Operation(summary = "Authorize User on basis of JWT")
    public ResponseEntity<Boolean> authorize(
            @Parameter(required = true, description = "A Valid JWT")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken) {

        log.atInfo().log("Authenticating User for token %s", authToken);

        return validationUtil.isValidToken(authToken).fold(x -> ResponseEntity.badRequest().body(false),
                x -> x ? ResponseEntity.ok(true) : ResponseEntity.status(FORBIDDEN).body(false));
    }
}
