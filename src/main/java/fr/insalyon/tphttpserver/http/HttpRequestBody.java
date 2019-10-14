package fr.insalyon.tphttpserver.http;

import java.util.List;

public class HttpRequestBody {

    private List<HttpParameter> parameters;
    private byte[] content;

    public List<HttpParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<HttpParameter> parameters) {
        this.parameters = parameters;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
