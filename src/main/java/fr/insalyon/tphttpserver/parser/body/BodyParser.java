package fr.insalyon.tphttpserver.parser.body;

import fr.insalyon.tphttpserver.http.HttpRequest;
import fr.insalyon.tphttpserver.http.HttpRequestBody;
import fr.insalyon.tphttpserver.parser.exception.UnsupportMimeTypeException;

public abstract class BodyParser {

    public abstract HttpRequestBody parse(final char[] content, HttpRequest request);

    public static BodyParser of(final String mimeType) throws UnsupportMimeTypeException {
        for(ContentType mime : ContentType.values()) {
            if(mime.getCode().equals(mimeType)) {
                return mime.getParser();
            }
        }
        throw new UnsupportMimeTypeException(mimeType);
    }
}
