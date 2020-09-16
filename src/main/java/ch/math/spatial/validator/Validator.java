package ch.math.spatial.validator;

/**
 * Deserialized objects can be filtered or cleaned according to business requirements
 * @param <T>
 */
public interface Validator<T> {
    public void validate(T elem) throws InvalidLengthException;
}
