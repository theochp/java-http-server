package fr.insalyon.tphttpserver.serialiser;

import fr.insalyon.tphttpserver.fs.ResourceManager;
import fr.insalyon.tphttpserver.http.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class PhpSerialiser extends ResourceSerialiser {

    private final ResourceManager resourceManager = new ResourceManager();

    @Override
    public void serialise(HttpRequest request, PrintStream out) {
        try {
            Process exec = exec(request);
            if(exec != null) {
                byte[] buffer = new byte[(int)3e6];
                InputStream inputStream = exec.getInputStream();
                int read = inputStream.read(buffer);
                out.println(request.getProtocolVersion() + " 200 OK");
                out.println("Content-Type: text/html");
                out.println("Content-Length: " + read);
                out.println("Connection: close");
                out.print("\n");
                out.write(buffer, 0, read);
                inputStream.close();
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
        processBuilder.environment().put("GATEWAY_INTERFACE", "CGI/1.1");
        processBuilder.environment().put("SCRIPT_FILENAME", "script.php");
        processBuilder.environment().put("QUERY_STRING", "id=123&name=title&parm=333");
        processBuilder.environment().put("REQUEST_METHOD", request.getMethod().name());
        processBuilder.environment().put("REDIRECT_STATUS", "1");
        // start process
        processBuilder.command("php-cgi");
        try {
            return processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
