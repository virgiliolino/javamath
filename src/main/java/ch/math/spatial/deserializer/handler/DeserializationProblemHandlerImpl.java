package ch.math.spatial.deserializer.handler;

import ch.math.spatial.deserializer.handler.field.FieldHandlerStrategy;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

/**
 * The type field is used in the json file for adopting a polymorphic approach.
 * this field will not be mapped to nay field in the Shape hierarchy of objects
 * for this reason we need to handle this exception here.
 * Whenever we'd like to extend the functionalities, for example a new Shape,
 * we should create a FieldHandlerStrategy and inject it here.
 */
@Component("TypeHandler")
public class DeserializationProblemHandlerImpl extends DeserializationProblemHandler {

    private final FieldHandlerStrategy[] fieldHandlerStrategies;

    public DeserializationProblemHandlerImpl(FieldHandlerStrategy[] fieldHandlerStrategies) {
        this.fieldHandlerStrategies = fieldHandlerStrategies;
    }

    @Override
    public boolean handleUnknownProperty(DeserializationContext ctxt, com.fasterxml.jackson.core.JsonParser p,
                                         JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName)
            throws IOException {
        return Arrays.stream(fieldHandlerStrategies).anyMatch(strategy ->
                strategy.canHandle(propertyName, beanOrClass)
        );
    }
}
