package service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClient {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 8080;

    public static Response sendRequest(Request request) {
        try (Socket socket = new Socket(IP, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            out.flush();
            return (Response) in.readObject();

        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
            return new Response(false, "Server is offline", null);
        }
    }
}
