package org.die6sheeshs.projectx.entities;

public class Pictures {
    private String id, picture;

    public Pictures(String id, String picture){
        this.id = id;
        this.id = picture;
    }

    public String getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
