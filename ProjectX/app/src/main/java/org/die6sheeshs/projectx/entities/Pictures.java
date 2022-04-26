package org.die6sheeshs.projectx.entities;

public class Pictures {
    private String event_id, img_uuid, base64;

    public Pictures(String event_id,String img_uuid, String base64){
        this.event_id = event_id;
        this.img_uuid = img_uuid;
        this.base64 = base64;
    }

    public Pictures(String event_id, String base64){
        this.event_id = event_id;
        this.base64 = base64;
    }

    public String getId() {
        return event_id;
    }

    public void setId(String id) {
        this.event_id = event_id;
    }

    public String getUuid() {
        return img_uuid;
    }

    public void setUuid(String uuid) {
        this.img_uuid = uuid;
    }

    public String getPicture() {
        return base64;
    }

    public void setPicture(String base64) {
        this.base64 = base64;
    }
}
