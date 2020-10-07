package ch.math.spatial.deserializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

final public class DeserializerImpl<T> implements Deserializer<T> {

    private final DeserializationProblemHandler typeHandler;
    private final TypeReference<List<T>> typeReference;
    private NamedType[] namedTypes;

    public DeserializerImpl(
            DeserializationProblemHandler typeHandler,
            TypeReference<List<T>> typeReference,
            NamedType[] namedTypes
    ) {
        this.typeHandler = typeHandler;
        this.typeReference = typeReference;
        this.namedTypes = namedTypes;
    }

    //todo: implement Either :) to contain side effects inside this method
    public List<T> deserialize(InputStream content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(namedTypes);
        objectMapper.addHandler(this.typeHandler);

        return objectMapper.readValue(content, typeReference);
    }

}
