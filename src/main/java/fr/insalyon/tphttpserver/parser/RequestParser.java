package fr.insalyon.tphttpserver.parser;

import fr.insalyon.tphttpserver.http.HttpHeader;
import fr.insalyon.tphttpserver.http.HttpMethod;
import fr.insalyon.tphttpserver.http.HttpParameter;
import fr.insalyon.tphttpserver.http.HttpRequest;
import fr.insalyon.tphttpserver.parser.body.BodyParser;
import fr.insalyon.tphttpserver.parser.exception.UnsupportMimeTypeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {

    private BufferedReader inputReader;
    private HttpRequest request;

    public RequestParser(final InputStream input) {
        this.inputReader = new BufferedReader(new InputStreamReader(input));
        this.request = new HttpRequest();
    }

    public HttpRequest getRequest() {
        parseRequestLine();
        parseHeaders();
        if(request.getMethod() == HttpMethod.POST) {
            if(request.getContentType() != null && !"".equals(request.getContentType())) {
                try {
                    BodyParser bodyParser = BodyParser.of(request.getContentType());
                    char[] buffer = new char[request.getContentLength()];
                    try {
                        inputReader.read(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bodyParser.parse(buffer, request);
                } catch (UnsupportMimeTypeException e) {

                }
            }
        }

        return request;
    }

    private void parseRequestLine() {
        try {
            String requestLine = inputReader.readLine();

            Pattern pattern = Pattern.compile("([A-Z]{3,6}) (\\S+) (HTTP/[0-9.]+)");
            try {
                Matcher matcher = pattern.matcher(requestLine);
                if(matcher.find()) {
                    String method = matcher.group(1);
                    String[] resourceParts = matcher.group(2).split("\\?");
                    String resource = resourceParts[0];
                    List<HttpParameter> queryParams = new ArrayList<>();
                    if(resourceParts.length > 1) {
                        QueryStringParser qsp = new QueryStringParser();
                        queryParams = qsp.parse(resourceParts[1]);
                    }
                    String httpVersion = matcher.group(3);
                    request.setMethod(HttpMethod.valueOf(method));
                    request.setResource(resource);
                    request.setProtocolVersion(httpVersion);
                    request.setQueryParameters(queryParams);
                }
            } catch (NullPointerException e) {
                System.out.println("npe:"+requestLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseHeaders() {
        try {
            String line = inputReader.readLine();
            while(!"".equals(line)) {
                HttpHeader httpHeader = parseHttpHeader(line);
                if(httpHeader != null) {
                    request.addHeader(httpHeader);
                }
                line = inputReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpHeader parseHttpHeader(final String line) {
        Pattern pattern = Pattern.compile("([A-Za-z0-9-]+)\\s?:\\s?(.*)");
        Matcher matcher = pattern.matcher(line.trim());
        if(matcher.find()) {
            String name = matcher.group(1);
            String value = matcher.group(2);
            return new HttpHeader(name, value);
        }
        return null;
    }

}
