package org.die6sheeshs.projectx.entities;

import java.time.LocalDateTime;

public class Party {

    private String id, name, description;
    private LocalDateTime start, end;
    private int max_people;
    public Party(String name, String description, LocalDateTime start, LocalDateTime end, int max_people){
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.max_people = max_people;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getMax_people() {
        return max_people;
    }

    public void setMax_people(int max_people) {
        this.max_people = max_people;
    }

    @Override
    public String toString(){

        return "Party{" + "\n"+
                "id=" + this.getId()+ "\n"+
                "name=" + this.getName()+ "\n"+
                "description=" + this.getDescription()+ "\n"+
                "start=" + this.getStart()+ "\n"+
                "end=" +this.getEnd()+ "\n"+
                "max_people=" + this.getMax_people()+ "\n"+
                "}";
    }
}
