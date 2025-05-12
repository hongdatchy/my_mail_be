package com.vtidc.mymail.config.security;
import com.vtidc.mymail.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;


@Component
public class TokenProvider {
    protected static final String SECRET = Base64.getEncoder().encodeToString(("hongdatchy12345678910" +
            "hongdatchy12345678910hongdatchy12345678910hongdatchy12345678910hongdatchy12345678910" +
            "hongdatchy12345678910hongdatchy12345678910").getBytes());

    @Value("${timeExpirationToken}")
    private long EXPIRED;

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String createToken(Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", myUserDetails.getUser().getId());
        claims.put("username", myUserDetails.getUsername());
        claims.put("password", myUserDetails.getPassword());
        claims.put("role", myUserDetails.getAuthorities());
        long time = System.currentTimeMillis();
        return Jwts.builder().setClaims(claims).setSubject(authentication.getName()).setIssuedAt(new Date(time))
                .setExpiration(new Date(time + EXPIRED)).signWith(SignatureAlgorithm.HS512, SECRET).compact();

    }

    @SuppressWarnings("unchecked")
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();

        User user = new User();
        user.setUsername(claims.get("username").toString());
        user.setPassword(claims.get("password") != null ? claims.get("password").toString() : null);
        user.setId(Integer.parseInt(claims.get("id").toString()));
        List<LinkedHashMap<String, String>> linkedHashMaps = (List<LinkedHashMap<String, String>>) claims.get("role");
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (LinkedHashMap<String, String> linkedHashMap : linkedHashMaps) {
            authorities.add(new SimpleGrantedAuthority(linkedHashMap.get("authority")));
        }
        // hiện tại chưa thêm role vào user --> vẫn hoạt động được vì token đã có role, chỉ khi dùng hàm getCurrent user thì không thấy role thôi
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

}