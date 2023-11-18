package com.example.kickevent.model;



import java.io.Serializable;


public class JwtTokenRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String userName;
    private String password;

    //need default constructor for JSON Parsing
    public JwtTokenRequest()
    {

    }

    public JwtTokenRequest(String userName, String password) {
        this.setUserName(userName);
        this.setPassword(password);
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
