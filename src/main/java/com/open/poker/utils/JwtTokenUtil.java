package com.open.poker.utils;

import com.open.poker.exception.InvalidJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

import static com.open.poker.constants.Constants.JWT_TOKEN_VALIDITY;

@Component
@PropertySource("classpath:application.properties")
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.secret}")
    private String secret;

    // retrieve username from jwt
    public String getUsername(final String jwt) {
        return getClaim(jwt, Claims::getSubject);
    }

    // retrieve username from jwt
    public String getId(final String jwt) {
        return getClaim(jwt, Claims::getId);
    }

    // retrieve username from jwt jwt
    public String getEmail(final String jwt) {
        return getClaim(jwt, Claims::getAudience);
    }

    //  retrieve expiration date from jwt jwt
    private Date getExpirationDate(String jwt) {
        return getClaim(jwt, Claims::getExpiration);
    }

    private <T> T getClaim(final String jwt, final Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    // for retrieveing any information from jwt we will need the secret key
    private Claims getAllClaims(final String jwt) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody();
    }

    // check if the token has expired
    private boolean hasExpired(String token) {
        final Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }

    // generate token for user with username & email
    public String generateToken(final Long id, final String username, final String email) {
        return Jwts.builder().setId(id.toString()).setSubject(username).setAudience(email).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)).signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // validate jwt
    public boolean validateToken(final String jwt) {
        return Try.of(() -> !hasExpired(jwt)).getOrElseThrow(x -> new InvalidJwtTokenException(x.getMessage(), x.getCause()));
    }
}
