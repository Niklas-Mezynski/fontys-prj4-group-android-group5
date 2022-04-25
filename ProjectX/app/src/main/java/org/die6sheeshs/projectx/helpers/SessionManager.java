package org.die6sheeshs.projectx.helpers;

import android.util.Log;

import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SessionManager {
    private static final SessionManager sessionManager = new SessionManager();
    private String jwtToken;
    private String userId;
    private User user;

    private SessionManager() {
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
