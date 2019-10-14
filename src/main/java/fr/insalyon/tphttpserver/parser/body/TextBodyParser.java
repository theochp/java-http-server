package fr.insalyon.tphttpserver.parser.body;

import fr.insalyon.tphttpserver.http.HttpRequest;
import fr.insalyon.tphttpserver.http.HttpRequestBody;

public class TextBodyParser extends BodyParser {

    @Override
    public HttpRequestBody parse(final char[] input, HttpRequest request) {
        HttpRequestBody requestBody = new HttpRequestBody();
        requestBody.setContent(new String(input).getBytes());
        return requestBody;
    }
}
