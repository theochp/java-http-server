package fr.insalyon.tphttpserver;

import fr.insalyon.tphttpserver.handler.ClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class App {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket client = serverSocket.accept();
                ClientThread thread = new ClientThread(client);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
