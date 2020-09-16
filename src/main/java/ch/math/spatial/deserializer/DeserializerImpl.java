package ch.math.spatial.deserializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component("Deserializer")
final public class DeserializerImpl implements Deserializer<List<Shape>> {

    private final DeserializationProblemHandler typeHandler;

    public DeserializerImpl(DeserializationProblemHandler typeHandler) {
        this.typeHandler = typeHandler;
    }

    //todo: implement Either :) to contain side effects inside this method
    public List<Shape> deserialize(InputStream content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(
            new NamedType(java.awt.Rectangle.class, "rect")
        );
        objectMapper.addHandler(this.typeHandler);
        return objectMapper.readValue(
            content,
            new TypeReference<List<Rectangle>>() {}
        );
    }

}
