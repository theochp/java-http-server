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
                return Arrays.copyOfRange(buffer, 0, read);
            } catch (IOException e) {
                return new byte[0];
            }
        }
        return new byte[0];
    }

}
