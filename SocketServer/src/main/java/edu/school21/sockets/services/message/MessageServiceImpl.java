package edu.school21.sockets.services.message;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.message.MessagesRepository;
import edu.school21.sockets.services.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageServiceImpl implements MessageService {

    private final MessagesRepository repository;

    @Autowired
    public MessageServiceImpl(MessagesRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Message message) {
        repository.save(message);
    }

    @Override
    public List<String> getLastMessages(long roomId) {
        List<String> messageLast = repository.getLastMessages(roomId);
        return messageLast;
    }
}
