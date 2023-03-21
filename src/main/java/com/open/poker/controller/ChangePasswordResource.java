package com.open.poker.controller;

import com.google.common.flogger.LazyArgs;
import com.open.poker.repository.UserProfileRepository;
import com.open.poker.schema.ChangePasswordRequest;
import com.open.poker.utils.JwtUtil;
import com.open.poker.utils.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.open.poker.utils.PasswordUtil.hashPwd;
import static com.open.poker.utils.PasswordUtil.isValidChangePwdReq;
import static com.open.poker.utils.PasswordUtil.matchPwd;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@Flogger
@RequestMapping("/changePassword")
@Tag(name = "Change Password Resource", description = "Change password of User")
public class ChangePasswordResource {

    @Autowired
    private UserProfileRepository repository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ValidationUtil validationUtil;

    @PostMapping
    @Operation(summary = "Change password of user")
    public ResponseEntity<Boolean> changePassword(
            @Parameter(required = true, description = "A Valid JWT")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
            @Parameter(description = "Change Password Request", required = true)
            @Valid @RequestBody ChangePasswordRequest request) {

        log.atInfo().log("Change Password Req with %s", authToken);

        return !isValidChangePwdReq(request) ? ResponseEntity.badRequest().body(false) : validationUtil.isValidToken(authToken)
                .fold(x -> {
                    log.atSevere().withCause(x).log("%s", LazyArgs.lazy(() -> "Error Validating Token " + authToken));
                    return ResponseEntity.badRequest().body(false);
                }, x ->  x ?
                        Try.of(() -> updatePwd(authToken.substring(7), request)).getOrElse(() -> {
                                    log.atSevere().log("%s", LazyArgs.lazy(() ->"Exception while updating password for " + authToken));
                                    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(false);
                                })
                        : ResponseEntity.status(FORBIDDEN).body(false));
    }

    private ResponseEntity<Boolean> updatePwd(final String jwt, final ChangePasswordRequest request) {
        return repository.findById(Long.parseLong(jwtUtil.getId(jwt))).filter(up -> matchPwd(request.getOldPassword(), up.getPassword()))
                .map(up -> repository.save(up.withPassword(hashPwd(request.getNewPassword()))))
                .isPresent() ? ResponseEntity.ok(true) : ResponseEntity.status(NOT_FOUND).body(false);
    }
}
