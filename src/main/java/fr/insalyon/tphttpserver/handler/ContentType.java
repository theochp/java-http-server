package fr.insalyon.tphttpserver.handler;

import fr.insalyon.tphttpserver.serialiser.PhpSerialiser;
import fr.insalyon.tphttpserver.serialiser.ResourceSerialiser;
import fr.insalyon.tphttpserver.serialiser.TemplateSerialiser;

public enum ContentType {

    TEXT_HTML("text/html", new String[]{"html", "htm"}),
    TEXT_PLAIN("text/plain", "txt"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_JPEG("image/jpeg", "jpeg"),
    IMAGE_JPG("image/jpg", "jpg"),
    IMAGE_GIF("image/gif", "gif"),
    APPLICATION_PDF("application/pdf", "pdf"),
    APPLICATION_JSON("application/json", "json"),
    APPLICATION_TEMPLATE("text/html", "template", new TemplateSerialiser()),
    APPLICATION_PHP("text/html", "php", new PhpSerialiser());

    String code;
    String[] extensions;
    ResourceSerialiser serialiser;

    ContentType(String code, String extension) {
        this(code, new String[] {extension}, null);
    }

    ContentType(String code, String[] extensions) {
        this.code = code;
        this.extensions = extensions;
        this.serialiser = null;
    }

    ContentType(String code, String extension, ResourceSerialiser serialiser) {
        this(code, new String[] {extension}, serialiser);
    }

    ContentType(String code, String[] extensions, ResourceSerialiser serialiser) {
        this.code = code;
        this.extensions = extensions;
        this.serialiser = serialiser;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }

    public ResourceSerialiser getSerialiser() {
        return serialiser;
    }

    public void setSerialiser(ResourceSerialiser serialiser) {
        this.serialiser = serialiser;
    }
}
