package edu.school21.sockets.services.message;

import edu.school21.sockets.models.Message;

import java.util.List;

public interface MessageService {

    void save(Message message);

    List<String> getLastMessages(long roomId);
}
