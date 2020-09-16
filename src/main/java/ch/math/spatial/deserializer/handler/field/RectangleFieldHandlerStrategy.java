package ch.math.spatial.deserializer.handler.field;

import org.springframework.stereotype.Component;

@Component("FieldHandlerStrategy")
public class RectangleFieldHandlerStrategy implements FieldHandlerStrategy {
    @Override
    public boolean canHandle(String propertyName, Object beanOrClass) {
        return beanOrClass.getClass().getSimpleName().equals("Rectangle")
                && propertyName.equals("type");
    }
}
