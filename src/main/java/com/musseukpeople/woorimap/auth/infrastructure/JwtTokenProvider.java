package com.musseukpeople.woorimap.auth.infrastructure;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.musseukpeople.woorimap.auth.application.JwtProvider;
import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider implements JwtProvider {

    private static final String COUPLE_CLAIM_NAME = "coupleId";

    private final String issuer;
    private final Key secretKey;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public JwtTokenProvider(
        @Value("${jwt.issuer}") String issuer,
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.access-token.expire-length}") long accessTokenValidityInMilliseconds,
        @Value("${jwt.refresh-token.expire-length}") long refreshTokenValidityInMilliseconds) {
        this.issuer = issuer;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    @Override
    public TokenDto createAccessToken(String payload, Long coupleId) {
        String token = createToken(payload, coupleId, accessTokenValidityInMilliseconds);
        return new TokenDto(token, accessTokenValidityInMilliseconds);
    }

    @Override
    public TokenDto createRefreshToken() {
        String token = createToken(UUID.randomUUID().toString(), null, refreshTokenValidityInMilliseconds);
        return new TokenDto(token, refreshTokenValidityInMilliseconds);
    }

    private String createToken(String payload, Long coupleId, long validityInMilliseconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setSubject(payload)
            .claim(COUPLE_CLAIM_NAME, coupleId)
            .setIssuer(issuer)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = getClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Date getExpiredDate(String token) {
        return getClaims(token).getExpiration();
    }

    @Override
    public Claims getClaims(String token) {
        try {
            return getClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(token, ErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public String getClaimName() {
        return COUPLE_CLAIM_NAME;
    }

    private Jws<Claims> getClaimsJws(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token);
    }
}
