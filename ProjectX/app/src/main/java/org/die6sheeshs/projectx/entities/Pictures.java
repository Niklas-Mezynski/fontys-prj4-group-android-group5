package org.die6sheeshs.projectx.entities;

public class Pictures {
    private String id, uuid, picture;

    public Pictures(String eventId,String uuid, String picture){
        this.id = id;
        this.uuid = uuid;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
