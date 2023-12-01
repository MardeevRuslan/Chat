package edu.school21.sockets.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.chat.ChatRoomService;
import edu.school21.sockets.services.message.MessageService;
import edu.school21.sockets.services.users.UsersService;



import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;




public class ServerSomthing extends Thread {
    private final UsersService usersService;
    private final MessageService messageService;
    private final ChatRoomService chatRoomService;
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private long roomId;
    User user;
    private String clientWord;
    private String clientName;

    private String roomName;




    public ServerSomthing(MessageService messageService, UsersService usersService, ChatRoomService chatRoomService, Socket socket) throws IOException {
        this.messageService = messageService;
        this.usersService = usersService;
        this.chatRoomService = chatRoomService;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        try {
            hello();
            menu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(String string) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", string);
        String jsonString = new Gson().toJson(jsonObject);
        out.write(jsonString + "\n");
        out.flush();
    }

    private void hello() throws IOException {
        sendMessage("Hello from Server!");

    }

    private void menu() throws IOException {
        sendMessage("\n");
        sendMessage("1 signIn\n");
        sendMessage("2 SignUp\n");
        sendMessage("3 Exit\n");
        selectionMenu();
    }

    private String getString(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();
        String extractedString = jsonObject.get("message").getAsString();
        extractedString = extractedString.trim();
        return extractedString;
    }

    private void selectionMenu() throws IOException {
        clientWord = getString(in.readLine());
        System.out.println(clientWord);
        if (clientWord.equals("3")) {
            this.downService();
        } else if (clientWord.equals("2")) {
            signUp();
        } else if (clientWord.equals("1")) {
            signIn();
        } else {
            sendMessage("Exception!\n");
            menu();
        }
    }

    private void signIn() throws IOException {
        sendMessage("Enter username:\n");
        clientName = getString(in.readLine());
        if (checkingExit(clientName)) {
            return;
        }
        System.out.println("name " + clientName);
        sendMessage("Enter password:\n");
        String password = getString(in.readLine());
        if (checkingExit(password)) {
            return;
        }
        System.out.println("password *****");
        this.user = new User(clientName, password);
        if (usersService.authenticate(user)) {
            user = usersService.getByName(user.getName());
            System.out.println(user.getName() + " sign in ");
            menuRoom();
        } else {
            System.out.println("Incorrect name or password");
            sendMessage("Incorrect name or password\n");
            menu();
        }
    }

    private void signUp() throws IOException {
        sendMessage("Registration, enter username:\n");
        String name = getString(in.readLine());
        if (checkingExit(name)) {
            return;
        }
        System.out.println("name " + name);
        sendMessage("Registration, enter password:\n");
        String password = getString(in.readLine());
        if (checkingExit(password)) {
            return;
        }
        System.out.println("password ******");
        User user = new User(name, password);
        if (usersService.signUp(user)) {
            System.out.println(user + " sign up");
            sendMessage("Successful!\n");
        } else {
            System.out.println(user + " not sign up");
            sendMessage("name " + name + " is occupied\n");
        }
        menu();

    }

    private void menuRoom() throws IOException {
        sendMessage("\n");
        sendMessage("1 Create room\n");
        sendMessage("2 Choose room\n");
        sendMessage("3 Exit\n");
        selectionMenuRoom();
    }

    private void selectionMenuRoom() throws IOException {
        clientWord = getString(in.readLine());
        System.out.println(clientWord);
        if (clientWord.equals("3")) {
            this.downService();
        } else if (clientWord.equals("2")) {
            chooseRoom();
        } else if (clientWord.equals("1")) {
            createRoom();
        } else {
            sendMessage("Exception!\n");
            selectionMenuRoom();
        }
    }


    private void createRoom() throws IOException {
        out.write("Enter name chatroom\n");
        out.flush();
        String nameRoom = getString(in.readLine());
        System.out.println(nameRoom);
        if (checkingExit(nameRoom)) {
            return;
        }
        if (chatRoomService.save(new Chatroom(null, nameRoom))) {
            System.out.println(user.getName() + " create room " + nameRoom);
            sendMessage("Successful!\n");
        } else {
            System.out.println(user.getName() + " not create room " + nameRoom);
            sendMessage("Exception!\n");
        }
        menuRoom();
    }


    private void chooseRoom() throws IOException {
        printAllRooms();
        clientWord = getString(in.readLine());
        try {
            roomId = Long.valueOf(clientWord);
            System.out.println(roomId + " room");
        } catch (NumberFormatException e) {
            sendMessage("No room number " + clientWord + "\n");
            menuRoom();
        }
        if (chatRoomService.findById(roomId) == null) {
            sendMessage("No room number " + clientWord + "\n");
            menuRoom();
            return;
        }
        roomName = chatRoomService.findById(roomId).getName();
        sendMessage("Room name " + roomName + " \n");
        printLastMessages();
        message();
    }

    private void printAllRooms() throws IOException {
        List<Chatroom> list = chatRoomService.findAll();
        int size = list.size();
        sendMessage("Rooms:\n");
        for (int i = 0; i < size; i++) {
            sendMessage(list.get(i).getID() + ". " + list.get(i).getName() + "\n");
        }
    }

    private void printLastMessages() throws IOException {
        List<String> messageLast = messageService.getLastMessages(roomId);
        for (String string :messageLast
             ) {
            sendMessage(string + "\n");
        }
    }

    private void message() throws IOException {
        while (true) {
            clientWord = getString(in.readLine());
            if (clientWord.equals("Exit")) {
                this.downService();
                for (ServerSomthing vr : Server.serverList) {
                    vr.send(clientName + " left the chat. ");
                }
                break;
            }
            System.out.println(clientName + " said in room " + roomName + " : " + clientWord);
            Chatroom chatroom = new Chatroom(roomId, roomName);
            messageService.save(new Message(null, this.user, chatroom, clientWord, new Date()));
            for (ServerSomthing vr : Server.serverList) {
                if (vr.getRoomId() == this.roomId && vr != this) {
                    vr.send(clientName + " said: " + clientWord);
                }
            }
        }
    }


    private void send(String msg) {
        try {
            sendMessage(msg);
        } catch (IOException ignored) {
        }

    }

    private boolean checkingExit(String clientWord) throws IOException {
        if (clientWord.equals("Exit")) {
            this.downService();
            return true;
        }
        return false;
    }


    private void downService() {
        try {
            if (!socket.isClosed()) {
                sendMessage("You have left the chat.");
                System.out.println(clientName + " left the chat. ");
                socket.close();
                in.close();
                out.close();
                for (ServerSomthing vr : Server.serverList) {
                    if (vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                    System.out.println("All " + Server.serverList.size() + " clients");
                }
            }
        } catch (IOException ignored) {
        }
    }

    public long getRoomId() {
        return roomId;
    }
}
