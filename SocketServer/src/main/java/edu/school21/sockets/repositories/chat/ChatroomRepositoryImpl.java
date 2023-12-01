package edu.school21.sockets.repositories.chat;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.rowMapper.ChatroomRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChatroomRepositoryImpl implements ChatroomRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ChatroomRowMapper chatroomRowMapper;
    private final Map<String, Object> parameters = new HashMap<>();


    @Autowired
    public ChatroomRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, ChatroomRowMapper chatroomRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.chatroomRowMapper = chatroomRowMapper;
    }

    @Override
    public Optional<Chatroom> findById(Long id) {
        String sqlQuery = "SELECT * FROM chatroom WHERE id = :id";
        parameters.clear();
        parameters.put("id", id);
        List<Chatroom> chatroomList = this.namedParameterJdbcTemplate.query(sqlQuery, parameters, new ChatroomRowMapper());
        return chatroomList.isEmpty() ? Optional.empty() : Optional.of(chatroomList.get(0));
    }


    @Override
    public List<Chatroom> findAll() {
        String sqlQuery = "SELECT * FROM chatroom";
        List<Chatroom> chatroomList = this.namedParameterJdbcTemplate.query(sqlQuery, chatroomRowMapper);
        return chatroomList;
    }


    @Override
    public void update(Chatroom entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public boolean save(Chatroom object) {
        Chatroom chatroom = (Chatroom) object;
        String sql = "INSERT INTO chatroom (name) VALUES ( :name)";
        parameters.clear();
        parameters.put("name", chatroom.getName());
        try {
            namedParameterJdbcTemplate.update(sql, parameters);
            return true;
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


}
