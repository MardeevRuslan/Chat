package edu.school21.sockets.rowMapper;


import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MessageRowMapper implements RowMapper<Message> {

    private UserRowMapper userRowMapper;

    private ChatroomRowMapper chatroomRowMapper;

    @Autowired
    public MessageRowMapper(UserRowMapper userRowMapper, ChatroomRowMapper chatroomRowMapper) {
        this.userRowMapper = userRowMapper;
        this.chatroomRowMapper = chatroomRowMapper;
    }

    @Override
    public Message mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("id");
        User author = userRowMapper.mapRow(resultSet, rowNum);
        Chatroom room = chatroomRowMapper.mapRow(resultSet, rowNum);
        String text = resultSet.getString("text_message");
        Date date = resultSet.getDate("date");
        return new Message(id, author, room, text, date);
    }
}

