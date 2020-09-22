package ch.math.spatial.deserializer.handler.field;

public interface FieldHandlerStrategy {
    boolean canHandle(String propertyName, Object beanOrClass);
}
