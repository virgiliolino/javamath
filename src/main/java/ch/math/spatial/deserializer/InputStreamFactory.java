package ch.math.spatial.deserializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class InputStreamFactory {
    public InputStream fromFile(String filename) throws FileNotFoundException {
        return new FileInputStream(filename);
    }
}
