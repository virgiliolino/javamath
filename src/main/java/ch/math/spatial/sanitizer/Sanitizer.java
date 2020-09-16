package ch.math.spatial.sanitizer;

/**
 * Deserialized objects can be filtered or cleaned according to business requirements
 * @param <T>
 */
public interface Sanitizer<T> {
    public T sanitize(T elem);
}
