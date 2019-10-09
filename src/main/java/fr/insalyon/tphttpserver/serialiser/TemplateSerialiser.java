package fr.insalyon.tphttpserver.serialiser;

import fr.insalyon.tphttpserver.fs.ResourceManager;
import fr.insalyon.tphttpserver.http.HttpParameter;
import fr.insalyon.tphttpserver.http.HttpRequest;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateSerialiser extends ResourceSerialiser{

    private final ResourceManager resourceManager = new ResourceManager();

    @Override
    public void serialise(HttpRequest request, PrintStream out) {
        byte[] content = resourceManager.readFileContents(request.getResource());
        String template = new String(content);
        Pattern remplacePattern = Pattern.compile("\\{\\{\\s*([a-zA-Z_]+)\\s*\\}\\}");
        Matcher remplacements = remplacePattern.matcher(template);
        while(remplacements.find()) {
            template = template.replace(remplacements.group(0), getParam(request, remplacements.group(1)));
        }
        out.println(request.getProtocolVersion() + " 200 OK");
        out.println("Content-Type: text/html");
        out.println("Content-Length: " + template.getBytes().length);
        out.println("Connection: close");
        out.print("\n");
        try {
            out.write(template.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getParam(HttpRequest request, String name) {
        Optional<HttpParameter> parameter = request.getQueryParameters().stream().filter(p -> p.getName().equals(name))
                .findFirst();
        if(parameter.isPresent())
            return parameter.get().getValue();
        else {
            parameter = request.getBodyParameters().stream().filter(p -> p.getName().equals(name))
                    .findFirst();
            if(parameter.isPresent())
                return parameter.get().getValue();
        }
        return "<not set>";
    }
}
