package edu.school21.sockets.app;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.dataBase.CreateDataBaseImpl;
import edu.school21.sockets.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;




public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        int port = getPort(args);
        Server server = context.getBean(Server.class);
        System.out.println("port " + port);
        try {
            server.run(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Integer getPort(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--port=")) {
                String[] split = arg.split("=");
                return Integer.parseInt(split[1]);
            }
        }
        throw new IllegalArgumentException("Port argument not found");
    }
}

