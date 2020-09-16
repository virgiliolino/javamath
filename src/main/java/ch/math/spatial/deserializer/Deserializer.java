package ch.math.spatial.deserializer;

import java.io.IOException;
import java.io.InputStream;

public interface Deserializer<T> {
    public T deserialize(InputStream content) throws IOException;
}
