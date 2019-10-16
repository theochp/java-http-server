package fr.insalyon.tphttpserver.parser.body;

import fr.insalyon.tphttpserver.http.HttpRequest;
import fr.insalyon.tphttpserver.http.HttpRequestBody;
import fr.insalyon.tphttpserver.parser.QueryStringParser;

public class FormUrlEncodedBodyParser extends BodyParser {

    @Override
    public HttpRequestBody parse(final char[] input, HttpRequest request) {
        String inputString = new String(input);
        QueryStringParser qsp = new QueryStringParser();
        HttpRequestBody requestBody = new HttpRequestBody();
        requestBody.setParameters(qsp.parse(inputString));
        request.setFormData(inputString);
        return requestBody;
    }
}
