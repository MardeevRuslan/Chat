package edu.school21.sockets.repositories.users;

import edu.school21.sockets.rowMapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import edu.school21.sockets.models.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Map<String, Object> param = new HashMap<>();
    private final UserRowMapper userRowMapper;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UsersRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, UserRowMapper userRowMapper, PasswordEncoder passwordEncoder) {
        this.userRowMapper = userRowMapper;
        this.passwordEncoder = passwordEncoder;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public Optional<User> findById(Long id) {
        String sqlQuery = "SELECT * FROM user_simple WHERE id = :id";
        param.clear();
        param.put("id", id);
        List<User> userList = namedParameterJdbcTemplate.query(sqlQuery, param, userRowMapper);
        return userList.isEmpty() ? Optional.empty() : Optional.of(userList.get(0));
    }

    @Override
    public Optional<User> findByName(String name) {
        String sqlQuery = "SELECT * FROM user_simple WHERE name = :name";
        param.clear();
        param.put("name", name);
        List<User> userList = namedParameterJdbcTemplate.query(sqlQuery, param, userRowMapper);
        return userList.isEmpty() ? Optional.empty() : Optional.of(userList.get(0));
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT * FROM user_simple";
        List<User> userList = namedParameterJdbcTemplate.query(sqlQuery, userRowMapper);
        return userList;
    }

    @Override
    public boolean save(User entity) {
        User user = (User) entity;
        String sql = "INSERT INTO user_simple (name, password) VALUES ( :name, :password)";
        param.clear();
        param.put("name", user.getName());
        String hashPassword = passwordEncoder.encode(user.getPassword());
        param.put("password", hashPassword);
        try {
            namedParameterJdbcTemplate.update(sql, param);
            return true;
        } catch (DataAccessException e) {
            return false;
        }

    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void delete(Long id) {

    }

}

