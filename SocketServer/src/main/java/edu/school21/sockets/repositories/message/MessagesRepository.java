package edu.school21.sockets.repositories.message;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MessagesRepository  extends CrudRepository<Message> {

    List<String> getLastMessages(long roomId);

    Optional findByName(String name);
}
