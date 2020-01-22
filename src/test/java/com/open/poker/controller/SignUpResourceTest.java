package com.open.poker.controller;

import com.open.poker.model.UserProfile;
import com.open.poker.repository.UserProfileRepository;
import com.open.poker.schema.SignupRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Objects;
import java.util.Optional;

import static com.open.poker.constants.TestConstants.*;
import static com.open.poker.constants.Constants.EMAIL_NOT_AVAILABLE;
import static com.open.poker.constants.Constants.USERNAME_NOT_AVAILABLE;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SignUpResource.class)
public class SignUpResourceTest {

    @Autowired
    private SignUpResource signUpResource;

    @MockBean
    private UserProfileRepository userProfileRepository;

    private final SignupRequest signUpReq = new SignupRequest(USERNAME, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);

    @Before
    public void setUp() {
        Mockito.when(userProfileRepository.findByUsernameOrEmail(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(Optional.empty());
        Mockito.when(userProfileRepository.save(Mockito.any(UserProfile.class))).thenReturn(UserProfile.of(signUpReq).withId(1L));
    }

    @Test
    public void validateSignupPositive() {
        var response = signUpResource.signUpUser(signUpReq);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assert.assertTrue(Objects.requireNonNull(response.getHeaders().getLocation()).toString().endsWith("1"));
    }

    @Test
    public void validateSignupFailUsername() {
        Mockito.when(userProfileRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(UserProfile.of(signUpReq)));
        Mockito.when(userProfileRepository.findByUsernameOrEmail(EMAIL, EMAIL)).thenReturn(Optional.of(UserProfile.of(signUpReq)));
        var response = signUpResource.signUpUser(signUpReq.withEmail(EMAIL_NEW));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertEquals(response.getBody(), USERNAME_NOT_AVAILABLE);
    }

    @Test
    public void validateSignupFailEmail() {
        Mockito.when(userProfileRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(UserProfile.of(signUpReq)));
        Mockito.when(userProfileRepository.findByUsernameOrEmail(EMAIL, EMAIL)).thenReturn(Optional.of(UserProfile.of(signUpReq)));
        var response = signUpResource.signUpUser(signUpReq.withUsername(USERNAME_NEW));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertEquals(response.getBody(), EMAIL_NOT_AVAILABLE);
    }

    @Test
    public void validateSignupFailException() {
        Mockito.when(userProfileRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(UserProfile.of(signUpReq)));
        Mockito.when(userProfileRepository.findByUsernameOrEmail(EMAIL, EMAIL)).thenReturn(Optional.of(UserProfile.of(signUpReq)));
        Mockito.when(userProfileRepository.save(Mockito.any(UserProfile.class))).thenThrow(RuntimeException.class);
        var response = signUpResource.signUpUser(signUpReq.withUsername(USERNAME_NEW).withEmail(EMAIL_NEW));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertTrue(Objects.requireNonNull(response.getBody()).toString().contains(USERNAME_NEW));
    }

    @Test
    public void validateSignupPositiveNewUsernameEmail() {
        Mockito.when(userProfileRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(UserProfile.of(signUpReq)));
        Mockito.when(userProfileRepository.findByUsernameOrEmail(EMAIL, EMAIL)).thenReturn(Optional.of(UserProfile.of(signUpReq)));
        var response = signUpResource.signUpUser(signUpReq.withUsername(USERNAME_NEW).withEmail(EMAIL_NEW));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assert.assertTrue(Objects.requireNonNull(response.getHeaders().getLocation()).toString().endsWith("1"));
    }
}
