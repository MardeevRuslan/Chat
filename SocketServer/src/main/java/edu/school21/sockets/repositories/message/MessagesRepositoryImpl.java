package edu.school21.sockets.repositories.message;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.rowMapper.ChatroomRowMapper;
import edu.school21.sockets.rowMapper.MessageRowMapper;
import edu.school21.sockets.rowMapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@Repository
public class MessagesRepositoryImpl implements MessagesRepository {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Map<String, Object> paramMap = new HashMap<>();

    private MessageRowMapper messageRowMapper;

    @Autowired
    public MessagesRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MessageRowMapper messageRowMapper) throws SQLException {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.messageRowMapper = messageRowMapper;
    }


    @Override
    public boolean save(Message message) {
        String sqlScript = "INSERT INTO message_simple (author, chatroom, text_message, date) VALUES ( :author, :chatroom, :text_message, :date)";
        paramMap.clear();
        paramMap.put("author", message.getAuthor().getId());
        paramMap.put("chatroom", message.getRoom().getID());
        paramMap.put("text_message", message.getText());
        paramMap.put("date", message.getDate());
        namedParameterJdbcTemplate.update(sqlScript, paramMap);
        return true;
    }


    @Override
    public List<String> getLastMessages(long roomId) {
        String sql = "SELECT * FROM message_simple AS m\n" +
                "JOIN user_simple AS u ON m.author = u.id\n" +
                "JOIN chatroom AS ch ON m.chatroom = ch.id\n" +
                "WHERE chatroom = :roomId LIMIT 30;";
        paramMap.clear();
        paramMap.put("roomId", roomId);
        List<Message> messageList = namedParameterJdbcTemplate.query(sql, paramMap, messageRowMapper);
        List<String> messageLast = new LinkedList<>();
        if (messageList.isEmpty()) {
            String str = "No new messages";
            messageLast.add(str);
            return messageLast;
        }
        for (Message message : messageList) {
            String string = message.getAuthor().getName() + " said: " + message.getText();
            messageLast.add(string);
        }
        return messageLast;
    }


    @Override
    public Optional findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List findAll() {
        return null;
    }


    @Override
    public void update(Message message) {

    }

    @Override
    public void delete(Long id) {

    }
}

