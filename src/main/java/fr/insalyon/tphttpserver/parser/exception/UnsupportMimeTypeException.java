package fr.insalyon.tphttpserver.parser.exception;

public class UnsupportMimeTypeException extends Exception {
    public UnsupportMimeTypeException(final String mimeType) {
        super("Mime type inconnue : "+mimeType);
    }
}
