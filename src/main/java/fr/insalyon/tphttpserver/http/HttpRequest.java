package fr.insalyon.tphttpserver.http;

import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

    private HttpMethod method;
    private String resource;
    private String protocolVersion;
    private final List<HttpHeader> headers = new ArrayList<>();
    private List<HttpParameter> queryParameters = new ArrayList<>();
    private HttpRequestBody requestBody;

    /* Required HTTP headers */
    private String host;
    private int contentLength = 0;
    private String contentType;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    public void addHeader(HttpHeader header) {
        extractRequiredHeader(header);
        this.headers.add(header);
    }

    public String getHost() {
        return host;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void extractRequiredHeader(HttpHeader header) {
        if(header.getName().equalsIgnoreCase("host")) {
            this.host = header.getValue();
        } else if(header.getName().equalsIgnoreCase("Content-length")) {
            this.contentLength = Integer.parseInt(header.getValue());
        } else if(header.getName().equalsIgnoreCase("content-type")) {
            this.contentType = header.getValue();
        }
    }

    public List<HttpParameter> getQueryParameters() {
        return queryParameters;
    }

    public HttpRequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(HttpRequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public void setQueryParameters(List<HttpParameter> queryParams) {
        queryParameters = queryParams;
    }

}
