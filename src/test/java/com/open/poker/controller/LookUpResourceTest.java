package com.open.poker.controller;

import com.open.poker.repository.UserProfileRepository;
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

import java.util.Optional;

import static com.open.poker.constants.TestConstants.*;

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
        Mockito.when(userProfileRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        Mockito.when(userProfileRepository.findByUsernameOrEmail(EMAIL, EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    public void existingUsername() {
        var response = lookUpResource.checkUserName(USERNAME);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(true, response.getBody());
    }

    @Test
    public void existingEmail() {
        var response = lookUpResource.checkEmail(EMAIL);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(true, response.getBody());
    }

    @Test
    public void nonExistingUsername() {
        var response = lookUpResource.checkUserName(USERNAME_NEW);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(false, response.getBody());
    }

    @Test
    public void nonExistingEmail() {
        var response = lookUpResource.checkEmail(EMAIL_NEW);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(false, response.getBody());
    }
}
