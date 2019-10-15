package fr.insalyon.tphttpserver.handler;

import fr.insalyon.tphttpserver.http.HttpRequest;

import java.io.*;

public class PutRequestHandler extends RequestHandler {
    @Override
    public void handle(HttpRequest request, PrintStream out) {
        File file = resourceManager.getFile(request.getResource());
        boolean created = false;
        try {
            if(!file.exists())
                created = file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(request.getRequestBody().getContent());
            fos.close();
        } catch (FileNotFoundException e) {
            response500(request, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(created)
            out.println(request.getProtocolVersion() + " 201 Created");
        else
            out.println(request.getProtocolVersion() + " 200 OK");
        out.println("Content-Length: 0");
        out.println("Connection: close");
        out.print("\n");
    }
}
