package fr.insalyon.tphttpserver.parser;

import fr.insalyon.tphttpserver.http.HttpParameter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QueryStringParser {

    public List<HttpParameter> parse(final String queryString) {
        List<HttpParameter> params = new ArrayList<>();
        String[] parts = queryString.split("&");
        for(String paramString : parts) {
            String[] paramParts = paramString.split("=");
            try {
                HttpParameter queryParam = new HttpParameter(paramParts[0], URLDecoder.decode(paramParts[1], StandardCharsets.UTF_8.name()));
                params.add(queryParam);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return params;
    }
}
