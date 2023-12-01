package edu.school21.sockets.server;

import edu.school21.sockets.services.chat.ChatRoomService;
import edu.school21.sockets.services.message.MessageService;
import edu.school21.sockets.services.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;



@Component
public class Server {
    public  static LinkedList<ServerSomthing> serverList = new LinkedList<>();
    private final UsersService usersService;
    private  final MessageService messageService;
    private final ChatRoomService chatRoomService;

    @Autowired
    public Server(UsersService usersService, MessageService messageService, ChatRoomService chatRoomService) {
        this.usersService = usersService;
        this.messageService = messageService;
        this.chatRoomService = chatRoomService;
    }

    public void run(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server up");
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    serverList.add(new ServerSomthing(messageService, usersService, chatRoomService, socket));
                    System.out.println(serverList.get(serverList.size() - 1) + " joined");
                    System.out.println("All " + serverList.size() + " clients");
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            serverSocket.close();
        }
    }
}
