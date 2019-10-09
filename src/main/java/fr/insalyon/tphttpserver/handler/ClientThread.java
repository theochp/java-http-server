package fr.insalyon.tphttpserver.handler;

import fr.insalyon.tphttpserver.fs.ResourceManager;
import fr.insalyon.tphttpserver.http.HttpRequest;
import fr.insalyon.tphttpserver.parser.RequestParser;
import fr.insalyon.tphttpserver.serialiser.ResourceSerialiser;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;

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
            handleRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(HttpRequest request) {
        switch (request.getMethod()) {
            case GET: handleGet(request);break;
            case POST: handlePost(request);break;
        }
    }

    private void handleGet(final HttpRequest request) {
        try {
            String resourcePath = getResourceUrl(request);
            if(resourceManager.fileExists(resourcePath))  {
                ContentType contentType = getResourceType(request);
                ResourceSerialiser serialiser = contentType.getSerialiser();
                if(serialiser != null) {
                    serialiser.serialise(request, out);
                } else {
                    byte[] content = resourceManager.readFileContents(getResourceUrl(request));
                    out.println(request.getProtocolVersion() + " 200 OK");
                    out.println("Content-Type: "+getResourceType(request).code);
                    out.println("Content-Length: " + content.length);
                    out.println("Connection: close");
                    out.print("\n");
                    out.write(content);
                }
            } else {
                send404(request);
            }
        } catch (IOException e) {
            send404(request);
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

    private void handlePost(final HttpRequest request) {
        handleGet(request);
    }

    private String getResourceUrl(HttpRequest request) {
        String resource = request.getResource();
        if("/".equals(resource)) {
            return "index.html";
        } else {
            return resource;
        }
    }

    private ContentType getResourceType(HttpRequest request) {
        for(ContentType type : ContentType.values()) {
            if(Arrays.stream(type.extensions).anyMatch(s->getFileExtension(getResourceUrl(request)).equals(s))) {
                return type;
            }
        }
        return ContentType.TEXT_PLAIN;
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
