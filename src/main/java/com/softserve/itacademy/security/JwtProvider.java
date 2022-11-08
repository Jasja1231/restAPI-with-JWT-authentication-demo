package com.softserve.itacademy.security;

import com.softserve.itacademy.exception.JwtAuthenticationException;
import com.softserve.itacademy.model.Role;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
/**
 * Class that generates/creates Token
 * */
@Component
public class JwtProvider {
    //    @Value("${security.jwt.token.secret-key}")
//    private String secretKey;
//
//    public String generateToken(String login){
//        Date date = Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
//        return Jwts.builder()
//                .setSubject(login)
//                .setExpiration(date)
//                .signWith(SignatureAlgorithm.HS512,secretKey)
//                .compact();
//    }
//    public  boolean validateToken(String token){
//        try {
//            Jwts.parser().setSigningKey(secretKey).parsePlaintextJws(token);
//            return true;
//        }catch (Exception e){
//            return false;
//        }
//    }
//    public String getLoginFromToken(String token){
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
//
//    }
//    public Date getExpirationDateFromToken(String token){
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
//    }
//}
    @Value("${security.jwt.token.secret-key}")
    private String secret;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
    @Autowired
     private   UserDetailsService userDetailsService;

    public  String createToken(String username, Role role){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role",getRoleName(role));
        Date now = new Date();
        Date date = Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public Authentication getAuthentication (String token){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(token);
        return  new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public  String getUsername (String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer_")){
            return  bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }

    public  boolean validateToken(String token) throws JwtAuthenticationException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            if(claims.getBody().getExpiration().before(new Date())){
                return false;
            }
            return  true;
        }catch (JwtException|IllegalArgumentException e){
          throw new JwtAuthenticationException("Jwt taken is expired or invalid");
        }
    }

    private String getRoleName (Role accountRole){
            return accountRole.getName();
    }
}
