package com.open.poker.controller;

import com.google.common.flogger.LazyArgs;
import com.open.poker.repository.UserProfileRepository;
import com.open.poker.schema.ChangePasswordRequest;
import com.open.poker.utils.JwtTokenUtil;
import com.open.poker.utils.PasswordUtil;
import com.open.poker.utils.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.vavr.control.Try;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

import static com.open.poker.utils.PasswordUtil.matchPwd;

@RestController
@Flogger
@RequestMapping("/changePassword")
@Api(value = "Change password of User", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
public class ChangePasswordResource {

    @Autowired
    private UserProfileRepository repository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ValidationUtil validationUtil;

    @PostMapping
    @ApiOperation(value = "Sign up a new user", response = String.class)
    public ResponseEntity<Boolean> changePassword(
            @ApiParam(required = true, value = "A Valid JWT")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
            @ApiParam(value = "Change Password Request", required = true)
            @Valid @RequestBody ChangePasswordRequest request) {

        log.atInfo().log("Change Password Req with %s", authToken);

        return validatePwd(request) ? ResponseEntity.badRequest().body(false) : validationUtil.isValidToken(authToken)
                .fold(x -> {
                    log.atSevere().withCause(x).log("%s", LazyArgs.lazy(() -> "Error Validating Token " + authToken));
                    return ResponseEntity.badRequest().body(false);
                }, x ->  x ?
                        Try.of(() -> updatePwd(authToken.substring(7), request))
                                .getOrElse(() -> {
                                    log.atSevere().log("%s", LazyArgs.lazy(() ->"Exception while updating password for " + authToken));
                                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
                                })
                        : ResponseEntity.badRequest().body(false));
    }

    private ResponseEntity<Boolean> updatePwd(final String token, final ChangePasswordRequest request) {
        return repository.findById(Long.parseLong(jwtTokenUtil.getIdFromToken(token.substring(7))))
                .filter(up -> matchPwd(request.getOldPassword(), up.getPassword()))
                .map(up -> repository.save(up.withPassword(PasswordUtil.hashPwd(request.getNewPassword()))))
                .isPresent() ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }

    private boolean validatePwd(final ChangePasswordRequest request) {
        return Objects.equals(request.getNewPassword(), request.getOldPassword()) ||
                !Objects.equals(request.getNewPassword(), request.getConfirmPassword());
    }
}
