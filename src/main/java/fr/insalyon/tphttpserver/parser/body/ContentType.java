package fr.insalyon.tphttpserver.parser.body;

public enum ContentType {

    FORM_URL_ENCODED("application/x-www-form-urlencoded", new FormUrlEncodedBodyParser()),
    FORM_MULTIPART("multipart/form-data", new FormUrlEncodedBodyParser()),
    TEXT_PLAIN("text/plain", new TextBodyParser()),
    TEXT_HTML("text/html", new TextBodyParser()),
    APPLICATION_JSON("application/json", new TextBodyParser());

    private String code;
    private BodyParser parser;

    ContentType(String code, BodyParser bodyParser) {
        this.code = code;
        parser = bodyParser;
    }


    public String getCode() {
        return code;
    }

    public BodyParser getParser() {
        return parser;
    }
}
