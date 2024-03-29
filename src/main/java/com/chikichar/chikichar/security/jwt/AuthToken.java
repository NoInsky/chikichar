package com.chikichar.chikichar.security.jwt;

import com.chikichar.chikichar.model.MemberRole;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
@Getter
@RequiredArgsConstructor
public class AuthToken {

    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    AuthToken(String id, Date expireDate, Key key) {
        this.key = key;
        this.token = createAuthToken(id, expireDate);
    }

    AuthToken(String id, MemberRole role, Date expireDate, Key key) {
        this.key = key;
        this.token = createAuthToken(id, role, expireDate);
    }

    public String createAuthToken(String id, Date expireDate) {
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expireDate)
                .compact();
    }

    public String createAuthToken(String id, MemberRole memberRole , Date expireDate){
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, memberRole.getKey())
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expireDate)
                .compact();
    }

    public boolean validateToken(){
        return this.getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }
    public Claims getExpiredTokenClaims() {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            return e.getClaims();
        }
        return null;
    }
}
