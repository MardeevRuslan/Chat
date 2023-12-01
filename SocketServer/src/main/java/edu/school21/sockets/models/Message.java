package edu.school21.sockets.models;


import java.util.Date;


public class Message {
    private Long id;
    private final User author;
    private final Chatroom room;
    private String text;
    private Date date;

    public Message(Long id, User author, Chatroom room, String text, Date date) {
        this.id = id;
        this.author = author;
        this.room = room;
        this.text = text;
        this.date = date;
    }


    @Override
    public String toString() {
        return "ID = " + this.id + ' ' +
                "author = " + this.author.getName() + ' ' +
                "room = " + this.room.getName() + ' ' +
                "date = " + this.date + ' ' +
                "text = " + this.text + ' ';
    }

    public long getID() {
        return this.id;
    }

    public User getAuthor() {
        return this.author;
    }

    public Chatroom getRoom() {
        return this.room;
    }

    public String getText() {
        return this.text;
    }

    public Date getDate() {
        return this.date;
    }

    public void setID(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
