package fr.insalyon.tphttpserver.serialiser;

import fr.insalyon.tphttpserver.fs.ResourceManager;
import fr.insalyon.tphttpserver.http.HttpHeader;
import fr.insalyon.tphttpserver.http.HttpRequest;
import fr.insalyon.tphttpserver.parser.HttpHeaderParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PhpSerialiser extends ResourceSerialiser {

    private static final int DEFAULT_BUFFER_SIZE = (int)3e6;

    private final ResourceManager resourceManager = new ResourceManager();
    private final HttpHeaderParser httpHeaderParser = new HttpHeaderParser();

    @Override
    public void serialise(HttpRequest request, PrintStream out) {
        try {
            Process exec = exec(request);
            if(exec != null) {
                if(request.getFormData() != null) {
                    OutputStream outputStream = exec.getOutputStream();
                    outputStream.write(request.getFormData().getBytes());
                    outputStream.close();
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
                List<HttpHeader> headers = httpHeaderParser.parse(bufferedReader);
                char[] buffer = new char[DEFAULT_BUFFER_SIZE];
                int read = bufferedReader.read(buffer);
                out.println(request.getProtocolVersion() + " 200 OK");
                byte[] content = null;
                if(read > 0) {
                    content = new String(buffer).getBytes(StandardCharsets.UTF_8);
                    out.println("Content-Length: "+content.length);
                } else {
                    out.println("Content-Length: 0");
                }
                for(HttpHeader header: headers) {
                    out.println(header.getName()+": "+header.getValue());
                }
                out.println("\n");
                if(content != null)
                    out.write(content);

                bufferedReader.close();
                exec.destroy();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Process exec(HttpRequest request) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // set base directory
        processBuilder.directory(resourceManager.getBaseDirectory());
        // set cgi environment
        String filename = request.getResource().replaceFirst("^/", "");
        processBuilder.environment().put("GATEWAY_INTERFACE", "CGI/1.1");
        processBuilder.environment().put("SCRIPT_FILENAME", filename);
        processBuilder.environment().put("QUERY_STRING", "id=123&name=title&parm=333");
        processBuilder.environment().put("REQUEST_METHOD", request.getMethod().name());
        processBuilder.environment().put("REDIRECT_STATUS", "1");
        processBuilder.environment().put("CONTENT_LENGTH", String.valueOf(request.getContentLength()));
        if(request.getContentType() != null)
            processBuilder.environment().put("CONTENT_TYPE", request.getContentType());
        // add request headers to cgi env
        for(HttpHeader header : request.getHeaders()) {
            processBuilder.environment().put(headerNameToCgiEnv(header.getName()), header.getValue());
        }
        // start process
        if(request.getFormData() != null)
            processBuilder.command("php-cgi", filename, request.getFormData());
        else
            processBuilder.command("php-cgi", filename);
        try {
            return processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String headerNameToCgiEnv(String headerName) {
        return "HTTP_" + headerName.replace("-", "_").toUpperCase();
    }
}
