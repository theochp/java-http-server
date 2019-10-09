package fr.insalyon.tphttpserver.fs;

public class FileNotFoundException extends Exception {

    public FileNotFoundException(final String filename) {
        super("Fichier non trouvé : "+filename);
    }
}
