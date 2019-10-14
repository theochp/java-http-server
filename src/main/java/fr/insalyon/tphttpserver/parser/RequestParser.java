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
    private final HttpHeaderParser httpHeaderParser = new HttpHeaderParser();

    public RequestParser(final InputStream input) {
        this.inputReader = new BufferedReader(new InputStreamReader(input));
        this.request = new HttpRequest();
    }

    public HttpRequest getRequest() {
        boolean hasLine = parseRequestLine();
        if(hasLine) {
            parseHeaders();
            parseBody();
            return request;
        }
        return null;
    }

    private boolean parseRequestLine() {
        try {
            String requestLine = inputReader.readLine();
            if(requestLine != null) {
                Pattern pattern = Pattern.compile("([A-Z]{3,6}) (\\S+) (HTTP/[0-9.]+)");
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
                    return true;
                } else
                    return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void parseHeaders() {
        List<HttpHeader> headers = httpHeaderParser.parse(inputReader);
        for(HttpHeader h : headers) {
            request.addHeader(h);
        }
    }

    private void parseBody() {
        if(request.getContentType() != null && !"".equals(request.getContentType())) {
            try {
                BodyParser bodyParser = BodyParser.of(request.getContentType());
                char[] buffer = new char[request.getContentLength()];
                try {
                    inputReader.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                request.setRequestBody(bodyParser.parse(buffer, request));
            } catch (UnsupportMimeTypeException e) {
                System.out.println("Unsupport mime type");
            }
        }
    }
}
