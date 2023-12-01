package edu.school21.sockets.dataBase;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class CreateDataBaseImpl implements CreateDataBase{
    private DataSource dataSource;

    @Autowired
    public CreateDataBaseImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        createDataBase();
    }

    private void createDataBase() {
        try {
            Connection connection = this.dataSource.getConnection();
            Statement statement = connection.createStatement();
            String sqlScript = new String(Files.readAllBytes(Paths.get("target/classes/schema.sql")));
            statement.execute(sqlScript);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}

