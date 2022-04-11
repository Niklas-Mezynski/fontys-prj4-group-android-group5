package org.die6sheeshs.projectx.helpers;

public class SessionManager {
    private static final SessionManager sessionManager = new SessionManager();
    private String jwtToken;
    private String userId;

    private SessionManager() {}

    public void saveAuthToken(String token) {
        jwtToken = token;
    }

    public String getToken() {
        return jwtToken;
    }

    public static SessionManager getInstance() {
        return sessionManager;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
