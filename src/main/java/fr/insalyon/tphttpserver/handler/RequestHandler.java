package fr.insalyon.tphttpserver.handler;

import fr.insalyon.tphttpserver.fs.ResourceManager;
import fr.insalyon.tphttpserver.http.HttpRequest;

import java.io.PrintStream;
import java.util.Arrays;

public abstract class RequestHandler {

    protected final ResourceManager resourceManager = new ResourceManager();

    public abstract void handle(HttpRequest request, PrintStream out);

    public static RequestHandler of(HttpRequest request) {
        switch (request.getMethod()) {
            case HEAD: return new GetRequestHandler();
            case GET: return new GetRequestHandler();
            case POST: return new PostRequestHandler();
            case PUT: return new PutRequestHandler();
        }
        return null;
    }

    String getResourceUrl(HttpRequest request) {
        String resource = request.getResource();
        if("/".equals(resource)) {
            return "index.html";
        } else {
            return resource;
        }
    }

    ContentType getResourceType(HttpRequest request) {
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
