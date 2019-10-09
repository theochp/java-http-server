package fr.insalyon.tphttpserver.handler;

public enum ContentType {

    TEXT_HTML("text/html", new String[]{"html", "htm"}),
    TEXT_PLAIN("text/plain", "txt"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_JPEG("image/jpeg", "jpeg"),
    IMAGE_JPG("image/jpg", "jpg"),
    IMAGE_GIF("image/gif", "gif"),
    APPLICATION_PDF("application/pdf", "pdf"),
    APPLICATION_JSON("application/json", "json");

    String code;
    String[] extensions;

    ContentType(String code, String extension) {
        this(code, new String[] {extension});
    }

    ContentType(String code, String[] extensions) {
        this.code = code;
        this.extensions = extensions;
    }

}
