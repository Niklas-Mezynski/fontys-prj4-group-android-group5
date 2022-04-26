package org.die6sheeshs.projectx.entities;

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
}
