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
@ContextConfiguration(classes = JwtUtil.class)
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void generateToken() {
        Assert.assertNotNull(jwtUtil.generateToken(1L, USERNAME, EMAIL));
    }

    @Test
    public void testGetUserDetailsFromToken() {
        var token = jwtUtil.generateToken(1L, USERNAME, EMAIL);
        assertEquals(USERNAME, jwtUtil.getUsername(token));
        assertEquals(EMAIL, jwtUtil.getEmail(token));
        assertEquals(1L, Long.parseLong(jwtUtil.getId(token)));
    }

    @Test
    public void testTokenValidity() {
        var token = jwtUtil.generateToken(2L, USERNAME_NEW, EMAIL);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test(expected = InvalidJwtTokenException.class)
    public void testInvalidTokenException() {
        var token = jwtUtil.generateToken(2L, USERNAME_NEW, EMAIL);
        jwtUtil.validateToken(token.substring(20));
    }
}
