package org.die6sheeshs.projectx.entities;

import java.util.Objects;

public class Friend {
    String friend_id,nick_name,profile_pic,about_me;

    public Friend(String friend_id, String nick_name, String profile_pic, String about_me) {
        this.friend_id = friend_id;
        this.nick_name = nick_name;
        this.profile_pic = profile_pic;
        this.about_me = about_me;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String getAbout_me() {
        return about_me;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "friend_id='" + friend_id + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                ", about_me='" + about_me + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return Objects.equals(friend_id, friend.friend_id) && Objects.equals(nick_name, friend.nick_name) && Objects.equals(profile_pic, friend.profile_pic) && Objects.equals(about_me, friend.about_me);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friend_id, nick_name, profile_pic, about_me);
    }
}
