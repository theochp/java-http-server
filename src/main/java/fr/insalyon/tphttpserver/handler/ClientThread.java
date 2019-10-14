package fr.insalyon.tphttpserver.handler;

import fr.insalyon.tphttpserver.fs.ResourceManager;
import fr.insalyon.tphttpserver.http.HttpRequest;
import fr.insalyon.tphttpserver.parser.RequestParser;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread extends Thread {

    private final ResourceManager resourceManager = new ResourceManager();
    private final Socket socket;
    private PrintStream out;

    public ClientThread(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintStream(socket.getOutputStream());

            RequestParser parser = new RequestParser(socket.getInputStream());
            HttpRequest request = parser.getRequest();
            if(request != null)
                handleRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(HttpRequest request) {
        RequestHandler requestHandler = RequestHandler.of(request);
        if(requestHandler != null) {
            requestHandler.handle(request, out);
        }
    }

    private void send404(final HttpRequest request) {
        out.println(request.getProtocolVersion() + " 404 NOT FOUND");
        out.println("Content-Type: text/html");
        out.println("Content-Length: 9");
        out.println("Connection: close");
        out.print("\n");
        out.write("Not found".getBytes(), 0, 9);
    }

}
