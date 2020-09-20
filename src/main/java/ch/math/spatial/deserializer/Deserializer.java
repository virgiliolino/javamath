package ch.math.spatial.deserializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface Deserializer<T> {
    List<T> deserialize(InputStream content) throws IOException;
}
