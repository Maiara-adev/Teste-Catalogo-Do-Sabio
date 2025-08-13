package com.br.catalogodosabio.application.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Test
    void geraTokenValido() {
        ReflectionTestUtils.setField(tokenService, "jwtSecret", "QzcwQjAyQjctQUJDRC00ODQyLTgzNkMtREIwRUY5QUY3QkZDQUJDRDEyMzQ1Njc4OQlDRUZFR0hJSktMTU5PUFFSU1RVVldYWVphYmNkZWZnaGlqa2xtbm9wcXJzdHV2d3h5eg==");
        ReflectionTestUtils.setField(tokenService, "jwtExpiration", TimeUnit.DAYS.toMillis(1));
        UserDetails userDetails = User.withUsername("testuser").password("password").authorities(Collections.emptyList()).build();

        String token = tokenService.generateToken(userDetails);

        assertFalse(token.isEmpty());
        assertEquals("testuser", tokenService.extractUsername(token));
        assertTrue(tokenService.isTokenValid(token, userDetails));
    }

    @Test
    void invalidaTokenExpirado() {
        String expiredSecret = "QzcwQjAyQjctQUJDRC00ODQyLTgzNkMtREIwRUY5QUY3QkZDQUJDRDEyMzQ1Njc4OQlDRUZFR0hJSktMTU5PUFFSU1RVVldYWVphYmNkZWZnaGlqa2xtbm9wcXJzdHV2d3h5eg==";
        UserDetails userDetails = User.withUsername("testuser").password("password").authorities(Collections.emptyList()).build();

        String expiredToken = Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)))
                .expiration(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(expiredSecret)))
                .compact();

        ReflectionTestUtils.setField(tokenService, "jwtSecret", expiredSecret);
        ReflectionTestUtils.setField(tokenService, "jwtExpiration", TimeUnit.DAYS.toMillis(1));

        assertFalse(tokenService.isTokenValid(expiredToken, userDetails));
    }
}