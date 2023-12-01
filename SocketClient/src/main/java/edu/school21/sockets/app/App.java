package edu.school21.sockets.app;

import java.io.IOException;


public class App 
{
    public static void main( String[] args )
    {

        Integer port = getPort(args);
        String host = "localhost";
        try {
            new Client().run(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Integer getPort(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--server-port=")) {
                String[] split = arg.split("=");
                return Integer.parseInt(split[1]);
            }
        }
        throw new IllegalArgumentException("Port argument not found");
    }
}
