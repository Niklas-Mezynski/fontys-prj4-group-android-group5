package org.die6sheeshs.projectx.entities;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
    }
}
