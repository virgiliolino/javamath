package ch.math.spatial.sanitizer;

import java.util.List;

/**
 * Deserialized objects can be filtered or cleaned according to business requirements
 * @param <T>
 */
public interface Sanitizer<T> {
    public List<T> sanitize(List<T> elem);
}
