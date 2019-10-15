package fr.insalyon.tphttpserver.handler;

import fr.insalyon.tphttpserver.http.HttpRequest;

import java.io.*;

public class DeleteRequestHandler extends RequestHandler {
    @Override
    public void handle(HttpRequest request, PrintStream out) {
        File file = resourceManager.getFile(request.getResource());
        boolean delete = file.delete();
        if(delete) {
            out.println(request.getProtocolVersion() + " 200 OK");
            out.println("Content-Length: 0");
            out.println("Connection: close");
            out.print("\n");
        } else {
            response404(request, out);
        }
    }
}
