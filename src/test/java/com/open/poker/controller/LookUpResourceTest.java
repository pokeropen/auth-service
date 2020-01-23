package com.open.poker.controller;

import com.open.poker.repository.UserProfileRepository;
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

import static com.open.poker.constants.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LookUpResource.class)
public class LookUpResourceTest {

    @Autowired
    private LookUpResource lookUpResource;

    @MockBean
    private UserProfileRepository userProfileRepository;

    @Before
    public void setUp() {
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(EMAIL, EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    public void existingUsername() {
        var response = lookUpResource.checkUserName(USERNAME);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(true, response.getBody());
    }

    @Test
    public void existingEmail() {
        var response = lookUpResource.checkEmail(EMAIL);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(true, response.getBody());
    }

    @Test
    public void nonExistingUsername() {
        var response = lookUpResource.checkUserName(USERNAME_NEW);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(false, response.getBody());
    }

    @Test
    public void nonExistingEmail() {
        var response = lookUpResource.checkEmail(EMAIL_NEW);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(false, response.getBody());
    }
}
