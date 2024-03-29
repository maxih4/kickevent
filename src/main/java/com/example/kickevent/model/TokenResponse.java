package com.example.kickevent.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
public class TokenResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;
    private final Date expirationDate;
    private final String type ="Bearer";
    private final String refreshToken;
    private final Date expirationDateRefreshToken;
    private final Long userId;
    private final List<Role> roles;


    public TokenResponse(String jwtToken, Date expirationDate, String refreshToken, Date expirationDateRefreshToken, Long id, List<Role> roles) {
        this.jwtToken = jwtToken;
        this.expirationDate= expirationDate;
        this.refreshToken = refreshToken;
        this.expirationDateRefreshToken = expirationDateRefreshToken;
        this.userId=id;
        this.roles=roles;

    }


}
