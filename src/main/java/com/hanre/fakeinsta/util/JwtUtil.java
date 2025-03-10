package com.hanre.fakeinsta.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
@PropertySource("classpath:security.properties")
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private long expiration;

    public String generateToken(String username, Map<String,Object> claims) {
        claims = (claims==null)?new HashMap<String,Object>():claims;
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes()) // Use the secret key
                    .build() // Build the parser
                    .parseClaimsJws(token) // Parse the token
                    .getBody(); // Get the claims

            Date issuedAt = claims.getIssuedAt();
            if (issuedAt.before(new Date(System.currentTimeMillis() - expiration))) { // Check if issued too long ago
                return false; // Token was issued too long ago
            }
            Date expirationDate = claims.getExpiration();
            return !expirationDate.before(new Date()); // Check if token has expired

        } catch (SignatureException e) {
            System.out.println("Invalid token signature: " + e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            System.out.println("Token has expired: " + e.getMessage());
            return false;
        } catch (JwtException e) {
            return false;
        }
    }
    public String decodeToken(String token) {
        try {
//            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes()) // Use the secret key
                    .build() // Build the parser
                    .parseClaimsJws(token) // Parse the token
                    .getBody();

            return claims.getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove "Bearer " prefix
        }
        return null; // No token found
    }

    public List<GrantedAuthority> extractAuthorities(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String role = claims.get("role", String.class); // Get role from claims
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

}