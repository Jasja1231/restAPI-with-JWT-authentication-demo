package com.softserve.itacademy.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public String generateToken(String login){
        Date date = Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .compact();
    }
    public  boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secretKey).parsePlaintextJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public String getLoginFromToken(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();

    }
    public Date getExpirationDateFromToken(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
    }
}
