package com.vtidc.mymail.config.soap;

import com.vtidc.mymail.dto.zimbra.AuthResponse;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class TokenMemory {

    @Getter
    private String token;
    private Long expirationTime;
    @Getter
    private String sessionId;

    public void saveToken(AuthResponse authResponse) {
        this.token = authResponse.getAuthToken();
        this.expirationTime = System.currentTimeMillis() + authResponse.getLifetime();
        this.sessionId = authResponse.getSession() != null ? authResponse.getSession().getValue() : "0";
    }

    public boolean isTokenExpired() {
        return token == null || expirationTime == null || System.currentTimeMillis() > expirationTime ;
    }

}
