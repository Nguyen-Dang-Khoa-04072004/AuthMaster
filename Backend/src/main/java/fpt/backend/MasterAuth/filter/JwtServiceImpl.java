package fpt.backend.MasterAuth.filter;


import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
class JwtServiceImpl implements JwtService{

    private final String JWT_SECRETE = "504f31964cf4d76d6e1bac7ad32ec9870d9223abf687077f9bfeba187930c4b8\n";

    @Override
    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(JWT_SECRETE)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    @Override
    public String generateToken(String username, long expiredTime) {
        Claims claims = Jwts.claims();
        claims.setSubject(username);
        claims.setExpiration(new Date(System.currentTimeMillis() + expiredTime ));
        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256,JWT_SECRETE)
            .compact();
    }

    @Override
    public boolean isValidToken(String token) {
        return true;
    }
}
