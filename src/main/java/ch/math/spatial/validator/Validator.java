package ch.math.spatial.validator;

/**
 * Deserialized objects can be filtered or cleaned according to business requirements
 * @param <T>
 */
public interface Validator<T> {
    void validate(T elem) throws InvalidStructureException;
}
