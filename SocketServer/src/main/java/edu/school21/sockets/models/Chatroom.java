package edu.school21.sockets.models;

import java.util.List;

public class Chatroom {
    private final Long id;
    private final String name;


    public Chatroom(Long id, String name) {
        this.id = id;
        this.name = name;

    }


    @Override
    public String toString() {
        return "ID = " + this.id + ' ' +
                "name = " + this.name + ' ';
    }




    public String getName() {
        return name;
    }


    public long getID() {
        return id;
    }
}
