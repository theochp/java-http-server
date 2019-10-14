package fr.insalyon.tphttpserver.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class ResourceManager {

    private static final int BUFFER_SIZE = (int) 3e6;
    private static final String SERVER_BASE_PATH = "D:\\web";

    public byte[] readFileContents(String filename) {
        if(filename.startsWith("/"))
            filename = filename.replaceFirst("/", "");
        File file = new File(SERVER_BASE_PATH + "\\"  + filename);
        if(file.exists()) {
            try {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[BUFFER_SIZE];
                int read = input.read(buffer);
                input.close();
                if(read > 0) {
                    return Arrays.copyOfRange(buffer, 0, read);
                } else {
                    return new byte[0];
                }
            } catch (IOException e) {
                return new byte[0];
            }
        }
        return new byte[0];
    }

    public String getResourcePath(String filename) {
        if(filename.startsWith("/"))
            filename = filename.replaceFirst("/", "");
        return SERVER_BASE_PATH + "\\"  + filename;
    }

    public boolean fileExists(String filename) {
        return new File(getResourcePath(filename)).exists();
    }

    public File getBaseDirectory() {
        return new File(SERVER_BASE_PATH);
    }

    public File getFile(String path) {
        if(path.startsWith("/") || path.startsWith("\\"))
            path = path.substring(1);
        return new File(SERVER_BASE_PATH+"\\"+path);
    }

}
