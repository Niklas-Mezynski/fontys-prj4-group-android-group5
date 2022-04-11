package org.die6sheeshs.projectx.entities;

public class LoginResponse {
    private String token;
    private String user_id;

    public LoginResponse(String token, String user_id){
        this.token = token;
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public String getUser_id() {
        return user_id;
    }
}
