package org.die6sheeshs.projectx.entities;

import java.time.LocalDateTime;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String nick_name;
    private LocalDateTime birth_date;
    private String profile_pic;
    private String about_me;
    private String password;

    public User(String firstName, String lastName, String email, String nick_name, LocalDateTime birth_date, String profile_pic, String about_me, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.nick_name = nick_name;
        this.birth_date = birth_date;
        this.profile_pic = profile_pic;
        this.about_me = about_me;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getNick_name() {
        return nick_name;
    }

    public LocalDateTime getBirth_date() {
        return birth_date;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String getAbout_me() {
        return about_me;
    }
    
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", birth_date=" + birth_date +
                ", profile_pic='" + profile_pic + '\'' +
                ", about_me='" + about_me + '\'' +
                '}';
    }
}
