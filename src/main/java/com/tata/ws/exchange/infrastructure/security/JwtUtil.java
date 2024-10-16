package com.tata.ws.exchange.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpirationInMilliseconds;

    @Value("${security.jwt.expiration-time-reset-password}")
    private long jwtExpirationResetClaveInMilliseconds;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpirationInMilliseconds);
    }

    public String generateResetToken(String email) {
        Date currentTime = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(currentTime.getTime() + jwtExpirationResetClaveInMilliseconds);

        return Jwts
                .builder()
                .claims(new HashMap<>())
                .subject(email)
                .issuedAt(currentTime)
                .expiration(expirationDate)
                .signWith(getSignInKey())
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        Date currentTime = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(currentTime.getTime() + expiration);

        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(currentTime)
                .expiration(expirationDate)
                .signWith(getSignInKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
