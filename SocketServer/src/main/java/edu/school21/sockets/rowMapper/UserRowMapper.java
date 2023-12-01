package edu.school21.sockets.rowMapper;


import edu.school21.sockets.models.User;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long userID = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String password = resultSet.getString("password");
        return new User(userID, name, password);
    }
}

