package fr.insalyon.tphttpserver.parser.body;

import fr.insalyon.tphttpserver.http.HttpRequest;
import fr.insalyon.tphttpserver.parser.QueryStringParser;

public class FormUrlEncodedBodyParser extends BodyParser {

    @Override
    public void parse(final char[] input, HttpRequest request) {
        String inputString = new String(input);
        QueryStringParser qsp = new QueryStringParser();
        request.setBodyParameters(qsp.parse(inputString));
    }
}
