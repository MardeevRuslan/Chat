package edu.school21.sockets.services.chat;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.repositories.chat.ChatroomRepository;
import edu.school21.sockets.services.chat.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatroomRepository repository;

    @Autowired
    public ChatRoomServiceImpl(ChatroomRepository repository) {
        this.repository = repository;
    }

    @Override
    public List findAll() {
        return repository.findAll();
    }

    @Override
    public boolean save(Chatroom chatroom) {
        return repository.save(chatroom);
    }

    public Chatroom findById(long roomId) {
        Optional<Chatroom> chatroom = repository.findById(roomId);
        return chatroom.orElse(null);
    }
}
