package com.open.poker.constants;

public final class Constants {

    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60;
    public static final String JWT_TOKEN_ISSUER = "open_poker";

    // Error Constants
    public static final String BEARER = "Bearer";
    public static final String INVALID_USER_PWD = "Invalid Username or password";
    public static final String USERNAME_NOT_AVAILABLE = "Username is already taken!!";
    public static final String EMAIL_NOT_AVAILABLE = "Email is already taken!!";

    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
