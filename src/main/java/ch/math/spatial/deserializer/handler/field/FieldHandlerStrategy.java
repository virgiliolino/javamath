package ch.math.spatial.deserializer.handler.field;

public interface FieldHandlerStrategy {
    public boolean canHandle(String propertyName, Object beanOrClass);
}
