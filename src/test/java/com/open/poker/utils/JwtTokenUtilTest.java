package com.open.poker.utils;

import com.open.poker.exception.InvalidJwtTokenException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.open.poker.constants.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JwtTokenUtil.class)
public class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void generateToken() {
        Assert.assertNotNull(jwtTokenUtil.generateToken(1L, USERNAME, EMAIL));
    }

    @Test
    public void testGetUserDetailsFromToken() {
        var token = jwtTokenUtil.generateToken(1L, USERNAME, EMAIL);
        assertEquals(USERNAME, jwtTokenUtil.getUsername(token));
        assertEquals(EMAIL, jwtTokenUtil.getEmail(token));
        assertEquals(1L, Long.parseLong(jwtTokenUtil.getId(token)));
    }

    @Test
    public void testTokenValidity() {
        var token = jwtTokenUtil.generateToken(2L, USERNAME_NEW, EMAIL);
        assertTrue(jwtTokenUtil.validateToken(token));
    }

    @Test(expected = InvalidJwtTokenException.class)
    public void testInvalidTokenException() {
        var token = jwtTokenUtil.generateToken(2L, USERNAME_NEW, EMAIL);
        jwtTokenUtil.validateToken(token.substring(20));
    }
}
