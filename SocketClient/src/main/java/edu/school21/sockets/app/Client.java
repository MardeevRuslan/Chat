package edu.school21.sockets.app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;

import static java.lang.System.*;

public class Client {
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter out;
    private BufferedReader in;
    private String serverWord;
    private String clientWord;
    private boolean status = true;


    public void run(String host, Integer port) throws IOException {
        clientSocket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(System.in));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        new WriteMsg().start();
        new ReadMsg().start();
    }


    private class ReadMsg extends Thread {
        @Override
        public void run() {
            try {
                while (status) {
                    serverWord = getString(in.readLine());
                    System.out.println(serverWord);
                    if (serverWord.equals("You have left the chat.")) {
                        status = false;
                    }
                }
            } catch (IOException e) {
            }
        }

        private String getString(String jsonString) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();
            String extractedString = jsonObject.get("message").getAsString();
            return extractedString;
        }
    }


    private class WriteMsg extends Thread {
        @Override
        public void run() {
            while (status) {
                try {
                    if (reader.ready()) {
                        clientWord = reader.readLine();
                        if (clientWord.equals("Exit")) {
                            sendMessage("Exit" + "\n");
                            status = false;
                        } else {
                            sendMessage(clientWord + "\n");
                        }
                    } else {
                        Thread.sleep(100);
                    }
                } catch (IOException | InterruptedException e) {
                }
            }
        }

        private void sendMessage(String string) throws IOException {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", string);
            String jsonString = new Gson().toJson(jsonObject);
            out.write(jsonString + "\n");
            out.flush();
        }
    }
}
