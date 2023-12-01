package edu.school21.sockets.services.chat;

import edu.school21.sockets.models.Chatroom;

import java.util.List;

public interface ChatRoomService {
    List findAll();

    boolean save(Chatroom chatroom);

    Chatroom findById(long roomId);
}
