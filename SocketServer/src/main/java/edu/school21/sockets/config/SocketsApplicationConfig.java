package edu.school21.sockets.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.school21.sockets.repositories.chat.ChatroomRepository;
import edu.school21.sockets.repositories.chat.ChatroomRepositoryImpl;
import edu.school21.sockets.repositories.message.MessagesRepository;
import edu.school21.sockets.repositories.message.MessagesRepositoryImpl;
import edu.school21.sockets.repositories.users.UsersRepository;
import edu.school21.sockets.repositories.users.UsersRepositoryImpl;
import edu.school21.sockets.rowMapper.ChatroomRowMapper;
import edu.school21.sockets.rowMapper.MessageRowMapper;
import edu.school21.sockets.rowMapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.boot.ApplicationArguments;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;
import java.util.*;


@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan(basePackages = {"edu/school21/sockets/*"})
public class SocketsApplicationConfig {

    @Value("${db.url}")
    String url;

    @Value("${db.user}")
    String user;

    @Value("${db.password}")
    String password;

    @Value("${db.driver.name}")
    String driverName;

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        return hikariConfig;
    }

    @Bean
    public DataSource hikariDataSource() {
        return new HikariDataSource(hikariConfig());
    }



    @Bean
    public UserRowMapper userRowMapper() {
        return new UserRowMapper();
    }

    @Bean
    public ChatroomRowMapper chatroomRowMapper() {
        return new ChatroomRowMapper();
    }

    @Bean
    public MessageRowMapper messageRowMapper() {
        return new MessageRowMapper(userRowMapper(), chatroomRowMapper());
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(hikariDataSource());
    }


}


