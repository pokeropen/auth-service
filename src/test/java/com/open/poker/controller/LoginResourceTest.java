package com.open.poker.controller;

import com.open.poker.exception.UserNotFoundException;
import com.open.poker.repository.UserProfileRepository;
import com.open.poker.schema.LoginRequest;
import com.open.poker.schema.TokenResponse;
import com.open.poker.utils.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static com.open.poker.constants.Constants.INVALID_USER_PWD;
import static com.open.poker.constants.TestConstants.EMAIL;
import static com.open.poker.constants.TestConstants.EMAIL_NEW;
import static com.open.poker.constants.TestConstants.PASSWORD;
import static com.open.poker.constants.TestConstants.PASSWORD_NEW;
import static com.open.poker.constants.TestConstants.USERNAME;
import static com.open.poker.constants.TestConstants.USERNAME_NEW;
import static com.open.poker.constants.TestConstants.user;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {LoginResource.class, JwtUtil.class})
public class LoginResourceTest {

    @Autowired
    private LoginResource loginResource;

    @MockBean
    private UserProfileRepository userProfileRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final LoginRequest loginReq = new LoginRequest(USERNAME, PASSWORD);

    @Before
    public void setUp() {
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(EMAIL, EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    public void validateLoginPositiveWithUsername() {
        var response = loginResource.loginUser(loginReq);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody() instanceof TokenResponse);
    }

    @Test
    public void validateLoginPositiveWithEmail() {
        var response = loginResource.loginUser(loginReq.withUsernameOrEmail(EMAIL));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody() instanceof TokenResponse);
    }

    @Test(expected = UserNotFoundException.class)
    public void validateLoginFailWithUsername() {
        loginResource.loginUser(loginReq.withUsernameOrEmail(USERNAME_NEW));
    }

    @Test(expected = UserNotFoundException.class)
    public void validateLoginFailWithEmail() {
        loginResource.loginUser(loginReq.withUsernameOrEmail(EMAIL_NEW));
    }

    @Test
    public void validateLoginFailWithUsernamePassword() {
        var response = loginResource.loginUser(loginReq.withPassword(PASSWORD_NEW));
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(), INVALID_USER_PWD);
    }

    @Test
    public void validateLoginFailWithEmailPassword() {
        var response = loginResource.loginUser(loginReq.withUsernameOrEmail(EMAIL).withPassword(PASSWORD_NEW));
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(), INVALID_USER_PWD);
    }
}
