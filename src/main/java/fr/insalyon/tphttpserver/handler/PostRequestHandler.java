package fr.insalyon.tphttpserver.handler;

import fr.insalyon.tphttpserver.http.HttpRequest;
import fr.insalyon.tphttpserver.serialiser.ResourceSerialiser;

import java.io.IOException;
import java.io.PrintStream;

public class PostRequestHandler extends RequestHandler {
    @Override
    public void handle(HttpRequest request, PrintStream out) {
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
                response404(request, out);
            }
        } catch (IOException e) {
            response404(request, out);
        }
    }
}
