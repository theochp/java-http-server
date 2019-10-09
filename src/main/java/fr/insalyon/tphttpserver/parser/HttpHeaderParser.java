package fr.insalyon.tphttpserver.parser;

import fr.insalyon.tphttpserver.http.HttpHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpHeaderParser {

    public List<HttpHeader> parse(BufferedReader reader) {
        List<HttpHeader> headers = new ArrayList<>();
        try {
            String line = reader.readLine();
            while(!"".equals(line)) {
                HttpHeader httpHeader = parseHttpHeader(line);
                if(httpHeader != null) {
                    headers.add(httpHeader);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return headers;
    }

    private HttpHeader parseHttpHeader(final String line) {
        if(line == null)
            return null;
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
