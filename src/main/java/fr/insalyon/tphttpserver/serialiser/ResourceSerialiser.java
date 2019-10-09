package fr.insalyon.tphttpserver.serialiser;

import fr.insalyon.tphttpserver.http.HttpRequest;

import java.io.PrintStream;

public abstract class ResourceSerialiser {

    public abstract void serialise(HttpRequest request, PrintStream out);

}
